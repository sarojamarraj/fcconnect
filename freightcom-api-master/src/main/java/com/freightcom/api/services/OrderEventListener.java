package com.freightcom.api.services;

import static com.freightcom.api.model.support.OrderStatusCode.BOOKED;
import static com.freightcom.api.model.support.OrderStatusCode.CANCELLED;
import static com.freightcom.api.model.support.OrderStatusCode.DELETED;
import static com.freightcom.api.model.support.OrderStatusCode.DELIVERED;
import static com.freightcom.api.model.support.OrderStatusCode.DRAFT;
import static com.freightcom.api.model.support.OrderStatusCode.EXCEPTION;
import static com.freightcom.api.model.support.OrderStatusCode.IN_TRANSIT;
import static com.freightcom.api.model.support.OrderStatusCode.MISSED_PICKUP;
import static com.freightcom.api.model.support.OrderStatusCode.PREDISPATCHED;
import static com.freightcom.api.model.support.OrderStatusCode.QUOTED;
import static com.freightcom.api.model.support.OrderStatusCode.READY_FOR_DISPATCH;
import static com.freightcom.api.model.support.OrderStatusCode.READY_FOR_SHIPPING;
import static com.freightcom.api.model.support.OrderStatusCode.SCHEDULED;
import static com.freightcom.api.model.support.OrderStatusCode.WAITING_BORDER;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import com.freightcom.api.ApiSession;
import com.freightcom.api.Messages;
import com.freightcom.api.SlackService;
import com.freightcom.api.events.ChargeDisputeResolved;
import com.freightcom.api.events.ChargeDisputeUpdate;
import com.freightcom.api.events.ChargeDisputed;
import com.freightcom.api.events.ClaimFiledEvent;
import com.freightcom.api.events.ClaimUpdatedEvent;
import com.freightcom.api.events.OrderBookedEvent;
import com.freightcom.api.events.OrderCancelledEvent;
import com.freightcom.api.events.OrderChargeChange;
import com.freightcom.api.events.OrderDeletedEvent;
import com.freightcom.api.events.OrderDeliveredEvent;
import com.freightcom.api.events.OrderStatusChange;
import com.freightcom.api.events.QuoteSendEvent;
import com.freightcom.api.events.SystemLogEvent;
import com.freightcom.api.model.Alert;
import com.freightcom.api.model.Charge;
import com.freightcom.api.model.Claim;
import com.freightcom.api.model.Customer;
import com.freightcom.api.model.CustomerOrder;
import com.freightcom.api.model.Invoice;
import com.freightcom.api.model.LoggedEvent;
import com.freightcom.api.model.MessageAction;
import com.freightcom.api.model.User;
import com.freightcom.api.model.UserRole;
import com.freightcom.api.model.support.OrderStatusCode;
import com.freightcom.api.repositories.ObjectBase;
import com.freightcom.api.services.invoices.InvoiceWrapper;
import com.freightcom.api.services.logic.CustomerUsers;
import com.freightcom.api.services.orders.StatusNotifier;

@Component
public class OrderEventListener implements ServiceProvider
{
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Value("${freightcom.order.email.from}")
    private String emailFromAddress;

    @Value("${freightcom.frontend.host}")
    private String host;
    private String orderLink = "/orders/";

    private final MailService mailService;
    private final AlertService alertService;
    private final ObjectBase objectBase;
    private final CustomerUsers customerUsers;
    private final ApiSession apiSession;
    private final Messages messages;
    private final SlackService slackService;
    private final ApplicationEventPublisher publisher;

    private static final Map<OrderStatusCode, String> statusMessage = new HashMap<OrderStatusCode, String>();

    static {
        statusMessage.put(READY_FOR_SHIPPING, "Order is booked");
        statusMessage.put(IN_TRANSIT, "Order is in transit");
        statusMessage.put(DELIVERED, "Order is delivered");
        statusMessage.put(CANCELLED, "Order is cancelled");
        statusMessage.put(PREDISPATCHED, "Order is predispatched");
        statusMessage.put(READY_FOR_DISPATCH, "Order is ready for dispatch");
        statusMessage.put(MISSED_PICKUP, "Missed pickup");
        statusMessage.put(QUOTED, "Order is quoted");
        statusMessage.put(DRAFT, "Order is creatd");
        statusMessage.put(DELETED, "Order is deleted");
        statusMessage.put(BOOKED, "Order is booked");
        statusMessage.put(SCHEDULED, "Order is scheduled");
        statusMessage.put(EXCEPTION, "Order is exception");
        statusMessage.put(WAITING_BORDER, "Order is waiting at the border");
    }

    @Autowired
    public OrderEventListener(final CustomerService orderService, final AlertService alertService,
            final MailService mailService, final ObjectBase objectBase, final InvoiceService invoiceService,
            final CustomerUsers customerUsers, final Messages messages, final ApiSession apiSession,
            final ApplicationEventPublisher publisher, final SlackService slackService)
    {
        this.mailService = mailService;
        this.alertService = alertService;
        this.objectBase = objectBase;
        this.customerUsers = customerUsers;
        this.messages = messages;
        this.apiSession = apiSession;
        this.publisher = publisher;
        this.slackService = slackService;
    }

    @EventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleOrderBooked(OrderBookedEvent event) throws Exception
    {
        try {
            CustomerOrder order = event.getOrder();

            log.debug("ORDER BOOKED EVENT LISTENER " + event + " " + order);

            if (order != null) {
                Customer customer = order.getCustomer();
                UserRole agent = customer == null ? null : customer.getSalesAgent();

                Alert alert = new Alert();

                alert.setObjectName("ORDER");
                alert.setObjectId(order.getId());
                alert.setMessage("Order booked");
                alert.setUserRoleId(agent == null ? null : agent.getId());

                alertService.createAlert(alert);

                if (customer.getAutoInvoice() == Customer.AutoInvoice.ON_BOOKING) {
                    // Create the invoice for customers set to on booking
                    Invoice invoice = InvoiceWrapper.get(this)
                            .setCustomer(customer)
                            .addOrder(order)
                            .done()
                            .getInvoice();

                    publishEvent(new SystemLogEvent(invoice, event.getUser(), "invoice created on booking", null));
                }
            }
        } catch (Exception e) {
            log.error("EXCEPTION IN OrderBooked LISTENER " + e.getMessage(), e);
            throw e;
        }
    }

    @EventListener
    @Transactional
    public void handleOrderCancelled(OrderCancelledEvent event) throws Exception
    {
        try {
            CustomerOrder order = event.getOrder();

            log.debug("ORDER CANCELLED EVENT LISTENER " + event + " " + order);

            if (order != null) {

            }
        } catch (Exception e) {
            log.error("EXCEPTION IN OrderCancelledEvent LISTENER " + e.getMessage(), e);
            throw e;
        }
    }

    @EventListener
    @Transactional
    public void handleOrderDeleted(OrderDeletedEvent event) throws Exception
    {
        try {
            CustomerOrder order = event.getOrder();

            log.debug("ORDER DELETED EVENT LISTENER " + event + " " + order);

            if (order != null) {

            }
        } catch (Exception e) {
            log.error("EXCEPTION IN OrderDeletedEvent LISTENER " + e.getMessage(), e);
            throw e;
        }
    }

    @EventListener
    @Transactional
    public void handleOrderDelivered(OrderDeliveredEvent event) throws Exception
    {
        try {
            CustomerOrder order = refetch(event.getOrder());

            log.debug("ORDER DELIVERED EVENT LISTENER " + event + " " + order);

            if (order != null) {

            }
        } catch (Exception e) {
            log.error("EXCEPTION IN OrderDeliveredEvent LISTENER " + e.getMessage(), e);
            throw e;
        }
    }

    @EventListener
    @Transactional
    public void handleOrderStatusChange(final OrderStatusChange event) throws Exception
    {
        try {
            CustomerOrder order = refetch(event.getOrder());

            log.debug("ORDER STATUS CHANGE EVENT LISTENER " + event + " " + order);

            if (order != null) {
                LoggedEvent loggedEvent = LoggedEvent.orderStatusMessage(event.getUser(), order,
                        event.getComment() != null ? event.getComment() : statusMessage.get(order.getStatusCode()),
                        order.getStatusCode()
                                .toString());
                objectBase.save(loggedEvent);
                (new StatusNotifier(order, event, loggedEvent, orderLink(order), messages, mailService,
                        emailFromAddress, slackService)).notifyIfRequired();
            }
        } catch (Exception e) {
            log.error("EXCEPTION IN OrderStatusChange LISTENER " + e.getMessage(), e);
            throw e;
        }
    }

    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Async
    public void handleOrderChargeChange(OrderChargeChange event) throws Exception
    {
        try {
            CustomerOrder order = refetch(event.getOrder());

            log.debug("HANDLE  " + event);

            if (order != null) {
                LoggedEvent message = new LoggedEvent();
                User user = event.getUser();

                message.setComment(event.getComment());
                message.setPrivate(true);
                message.setEntityType(LoggedEvent.ENTITY_TYPE_ORDER);
                message.setMessageType(LoggedEvent.MESSAGE_TYPE_INVOICE);

                if (event.getMessage()
                        .equals("Charge added")) {
                    message.setAction(MessageAction.NEW_CHARGE);
                } else {
                    message.setAction(MessageAction.CHARGE_UPDATED);
                }

                message.setEntityId(order.getId());
                message.setUserId(user == null ? null : user.getId());
                message.setMessage(order.getOrderStatusName());

                objectBase.save(message);

                if (event.getNotiyCustomer()) {
                    orderChargeNotify(event);
                }
            }
        } catch (Exception e) {
            log.error("LISTENER EXCEPTION " + event + " " + e.getMessage(), e);
            throw e;
        }
    }

    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Async
    public void handleQuoteSendEvent(QuoteSendEvent event) throws Exception
    {
        try {
            CustomerOrder order = refetch(event.getOrder());
            Customer customer = order.getCustomer();

            log.debug("QUOTE SEND LISTENER " + event + " " + order);

            if (order != null && customer != null) {
                List<User> users = customerUsers.notifyUsers(customer);

                if (users.size() > 0) {
                    User user = users.get(0);

                    objectBase.save(LoggedEvent.orderStatusMessage(event.getUser(), order, "Quote sent to customer",
                            order.getStatusCode()
                                    .toString()));

                    Map<String, Object> templateVariables = new HashMap<String, Object>();

                    templateVariables.put("name", user.fullName());
                    templateVariables.put("orderCurrency", "$");
                    templateVariables.put("orderTotal", order.getTotalCharge());
                    templateVariables.put("orderId", order.getId());
                    templateVariables.put("link", orderLink(order));

                    mailService.send(messages.get("freightcom.quote.email.subject", new Object[] { order.getId() }),
                            user.getEmail(), "mail/html/quote-ready.html", templateVariables, emailFromAddress);
                }
            }
        } catch (Exception e) {
            log.error("EXCEPTION QuoteSendEvent LISTENER " + e.getMessage(), e);
            throw e;
        }
    }

    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Async
    public void handleClaimFiledEvent(ClaimFiledEvent event) throws Exception
    {
        try {
            log.debug("ENTER CLAIM FILED EVENT LISTENER ClaimFiledEvent");
            CustomerOrder order = refetch(event.getOrder());
            Claim claim = order.getClaim();
            User user = claim.getSubmittedBy()
                    .getUser();

            Map<String, Object> templateVariables = new HashMap<String, Object>();

            templateVariables.put("name", user.fullName());
            templateVariables.put("claimId", claim.getId());
            templateVariables.put("orderId", order.getId());
            templateVariables.put("link", orderLink(order));

            mailService.send(messages.get("freightcom.file.claim.email.subject", new Object[] { claim.getId() }),
                    user.getEmail(), "mail/html/file-claim.html", templateVariables, emailFromAddress);

        } catch (Exception e) {
            log.error("EXCEPTION ClaimFiledEvent LISTENER " + e.getMessage(), e);
            throw e;
        }
    }

    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Async
    public void handleClaimUpdatedEvent(ClaimUpdatedEvent event) throws Exception
    {
        try {
            log.debug("ENTER CLAIM UPDATED EVENT LISTENER ClaimUpdatedEvent");
            CustomerOrder order = refetch(event.getOrder());
            Claim claim = order.getClaim();
            User user = claim.getSubmittedBy()
                    .getUser();

            Map<String, Object> templateVariables = new HashMap<String, Object>();

            templateVariables.put("name", user.fullName());
            templateVariables.put("claimId", claim.getId());
            templateVariables.put("orderId", order.getId());
            templateVariables.put("link", orderLink(order));
            templateVariables.put("comment", event.getStatusMessage().getComment());
            templateVariables.put("status", claim.getStatus().toString());

            mailService.send(messages.get("freightcom.update.claim.email.subject", new Object[] { claim.getId() }),
                    user.getEmail(), "mail/html/update-claim.html", templateVariables, emailFromAddress);

        } catch (Exception e) {
            log.error("EXCEPTION ClaimUpdatedEvent LISTENER " + e.getMessage(), e);
            throw e;
        }
    }

    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Async
    public void handleChargeDisputed(ChargeDisputed event) throws Exception
    {
        try {
            log.debug("ENTER EVENT LISTENER" + event);
            Charge charge = refetch(event.getCharge());
            CustomerOrder order = charge.getOrder();
            User user = order.getDisputeInformation()
                    .getUser();

            Map<String, Object> templateVariables = new HashMap<String, Object>();

            templateVariables.put("name", user.fullName());
            templateVariables.put("orderId", order.getId());
            templateVariables.put("link", orderLink(order));
            templateVariables.put("comment", event.getStatusMessage().getComment());

            mailService.send(messages.get("freightcom.open.dispute.email.subject", new Object[] { order.getId() }),
                    user.getEmail(), "mail/html/open-dispute.html", templateVariables, emailFromAddress);

        } catch (Exception e) {
            log.error("EXCEPTION DisputeUpdatedEvent LISTENER " + e.getMessage(), e);
            throw e;
        }
    }

    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Async
    public void handleChargeDisputeUpdate(ChargeDisputeUpdate event) throws Exception
    {
        try {
            log.debug("ENTER EVENT LISTENER" + event);
            CustomerOrder order = refetch(event.getOrder());
            User user = order.getDisputeInformation()
                    .getUser();

            Map<String, Object> templateVariables = new HashMap<String, Object>();

            templateVariables.put("name", user.fullName());
            templateVariables.put("orderId", order.getId());
            templateVariables.put("link", orderLink(order));
            templateVariables.put("comment", event.getStatusMessage().getComment());

            mailService.send(messages.get("freightcom.update.dispute.email.subject", new Object[] { order.getId() }),
                    user.getEmail(), "mail/html/update-dispute.html", templateVariables, emailFromAddress);

        } catch (Exception e) {
            log.error("EXCEPTION DisputeUpdatedEvent LISTENER " + e.getMessage(), e);
            throw e;
        }
    }

    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Async
    public void handleChargeDisputeResolved(ChargeDisputeResolved event) throws Exception
    {
        try {
            log.debug("ENTER EVENT LISTENER" + event);
            CustomerOrder order = refetch(event.getOrder());
            User user = order.getDisputeInformation()
                    .getUser();

            Map<String, Object> templateVariables = new HashMap<String, Object>();

            templateVariables.put("name", user.fullName());
            templateVariables.put("orderId", order.getId());
            templateVariables.put("link", orderLink(order));
            templateVariables.put("comment", event.getStatusMessage().getComment());

            mailService.send(messages.get("freightcom.resolve.dispute.email.subject", new Object[] { order.getId() }),
                    user.getEmail(), "mail/html/resolve-dispute.html", templateVariables, emailFromAddress);

        } catch (Exception e) {
            log.error("EXCEPTION DisputeResolvedEvent LISTENER " + e.getMessage(), e);
            throw e;
        }
    }

    private String orderLink(CustomerOrder order)
    {
        return host + orderLink + order.getId();
    }

    private void orderChargeNotify(OrderChargeChange event) throws Exception
    {
        try {
            CustomerOrder order = refetch(event.getOrder());
            Customer customer = order.getCustomer();
            Charge charge = event.getCharge();

            if (order != null && customer != null) {
                // Another status message for customer's eyes
                LoggedEvent message = new LoggedEvent();
                User logUser = event.getUser();

                message.setComment(event.getCustomerComment());
                message.setPrivate(false);
                message.setEntityType(LoggedEvent.ENTITY_TYPE_ORDER);
                message.setMessageType(LoggedEvent.MESSAGE_TYPE_INVOICE);

                if (event.getMessage()
                        .equals("Charge added")) {
                    message.setAction(MessageAction.NEW_CHARGE);
                } else {
                    message.setAction(MessageAction.CHARGE_UPDATED);
                }

                message.setEntityId(order.getId());
                message.setUserId(logUser == null ? null : logUser.getId());
                message.setMessage(event.getMessage());

                objectBase.save(message);

                List<User> users = customerUsers.notifyUsers(customer);

                if (users.size() > 0) {
                    User user = users.get(0);
                    Map<String, Object> templateVariables = new HashMap<String, Object>();

                    templateVariables.put("name", user.fullName());
                    templateVariables.put("orderCurrency", "$");
                    templateVariables.put("orderTotal", order.getTotalCharge());
                    templateVariables.put("orderId", order.getId());
                    templateVariables.put("link", orderLink(order));
                    templateVariables.put("charge", charge);

                    mailService.send(
                            messages.get("freightcom.charge.change.email.subject", new Object[] { order.getId() }),
                            user.getEmail(), "mail/html/charge-change.html", templateVariables, emailFromAddress);
                }
            }
        } catch (Exception e) {
            log.error("EXCEPTION OrderChargeNotify LISTENER " + e.getMessage(), e);
            throw e;
        }
    }

    private CustomerOrder refetch(CustomerOrder order)
    {
        if (order != null) {
            return objectBase.getOrder(order.getId());
        } else {
            return null;
        }
    }

    private Charge refetch(Charge charge)
    {
        if (charge != null) {
            return objectBase.getCharge(charge.getId());
        } else {
            return null;
        }
    }

    public void publishEvent(Object event)
    {
        publisher.publishEvent(event);
    }

    public ApiSession getApiSession()
    {
        return apiSession;
    }

    public ObjectBase getObjectBase()
    {
        return objectBase;
    }

    public UserRole getRole()
    {
        return apiSession.getRole();
    }
}
