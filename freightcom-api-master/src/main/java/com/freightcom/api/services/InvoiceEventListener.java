package com.freightcom.api.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import com.freightcom.api.ApiSession;
import com.freightcom.api.Messages;
import com.freightcom.api.events.InvoiceCreatedEvent;
import com.freightcom.api.events.InvoicePaidEvent;
import com.freightcom.api.model.CreditCard;
import com.freightcom.api.model.Customer;
import com.freightcom.api.model.Invoice;
import com.freightcom.api.model.User;
import com.freightcom.api.model.UserRole;
import com.freightcom.api.repositories.ObjectBase;
import com.freightcom.api.services.dataobjects.InvoicePayment;
import com.freightcom.api.services.invoices.InvoicePaymentProvider;
import com.freightcom.api.services.logic.CustomerUsers;
import com.freightcom.api.services.payment.PaymentProcessor;

@Component
public class InvoiceEventListener implements ServiceProvider
{
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final MailService mailService;
    private final ObjectBase objectBase;
    private final CustomerUsers customerUsers;
    private final Messages messages;
    private final PaymentProcessor paymentProcessor;
    private final ApplicationEventPublisher publisher;

    @Value("${freightcom.invoice.email.from}")
    private String emailFromAddress;

    @Value("${freightcom.frontend.host}")
    private String host;
    private String invoiceLink = "/invoices/";

    @Autowired
    public InvoiceEventListener(final CustomerService customerService, final AlertService alertService,
            final MailService mailService, final UserService userService, final ObjectBase objectBase,
            final CustomerUsers customerUsers, final Messages messages, final PaymentProcessor paymentProcessor,
            final ApplicationEventPublisher publisher)
    {
        this.mailService = mailService;
        this.objectBase = objectBase;
        this.customerUsers = customerUsers;
        this.messages = messages;
        this.paymentProcessor = paymentProcessor;
        this.publisher = publisher;
    }

    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Async
    public void autoChargeInvoice(InvoiceCreatedEvent event) throws Exception
    {
        log.debug("INVOICE AUTOCHARGE " + event);

        try {
            Invoice invoice = objectBase.getInvoice(event.getInvoice()
                    .getId());

            if (invoice != null) {
                log.debug("ASYNC HAVE INVOICE " + invoice);
                Customer customer = invoice.getCustomer();

                if (customer != null && customer.getAutoCharge() == Customer.AutoCharge.IMMEDIATELY) {
                    // Invoice is to be charged immediately
                    List<Invoice> toPay = new ArrayList<Invoice>();
                    toPay.add(invoice);

                    InvoicePayment payment = new InvoicePayment();

                    CreditCard card = customer.getDefaultCreditCard();

                    if (card == null) {
                        payment.setCreditOnly(true);
                    } else {
                        payment.setCreditCardId(card.getId());
                    }

                    InvoicePaymentProvider.get(this, paymentProcessor)
                            .payInvoiceNow(event.getLoggedInRole(), customer, payment, toPay);
                }
            }
        } catch (Exception e) {
            log.error("EXCEPTION IN Invoice autocharge LISTENER " + e.getMessage(), e);
            throw e;
        }
    }

    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Async
    public void handleInvoiceCreated(InvoiceCreatedEvent event) throws Exception
    {
        log.debug("INVOICE CREATED EVENT " + event);

        try {
            log.debug("ASYNC INVOICE SENT " + event + " I " + event.getInvoice()
                    .getId());
            Invoice invoice = objectBase.getInvoice(event.getInvoice()
                    .getId());

            if (invoice != null) {
                log.debug("ASYNC HAVE INVOICE " + invoice);
                Customer customer = invoice.getCustomer();

                for (User invoiceUser: customerUsers.getInvoiceEmails(customer)) {
                    sendInvoiceEmail(event, invoiceUser);
                }

                invoice.setSentAt(new Date());
                invoice.setUpdatedBy(event.getLoggedInRole()
                        .getUser()
                        .getLogin());
            }
        } catch (Exception e) {
            log.error("EXCEPTION IN Invoice Created LISTENER " + e.getMessage(), e);
            throw e;
        }
    }

    private void sendInvoiceEmail(InvoiceCreatedEvent event, User user)
    {
        try {
            Invoice invoice = event.getInvoice();

            Map<String, Object> templateVariables = new HashMap<String, Object>();

            templateVariables.put("invoiceId", invoice.getId());
            templateVariables.put("name", user.fullName());
            templateVariables.put("invoiceCurrency", "$");
            templateVariables.put("invoiceTotal", invoice.getAmount());
            templateVariables.put("link", host + invoiceLink + invoice.getId());
            // Add line if invoice paid by credit card
            templateVariables.put("invoiceCredit", "");

            mailService.send(messages.get("freightcom.invoice.email.subject", new Object[] { invoice.getId() }),
                    user.getEmail(), "mail/html/new-invoice.html", templateVariables, emailFromAddress);
            log.debug("ASYNC MESSAGE SENT TO " + user.getEmail());
        } catch (Exception e) {
            log.error("ASYNC EXCEPTION IN INVOICE CREATE LISTENER " + e.getMessage(), e);
        }
    }

    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Async
    public void handleInvoicePaid(InvoicePaidEvent event) throws Exception
    {
        log.debug("INVOICE PAID EVENT");

        try {
            log.debug("ASYNC INVOICE SENT " + event + " I " + event.getInvoice()
                    .getId());
            Invoice invoice = objectBase.getInvoice(event.getInvoice()
                    .getId());

            if (invoice != null) {
                log.debug("ASYNC HAVE INVOICE " + invoice);
                Customer customer = invoice.getCustomer();

                log.debug("ASYNC HAVE CUSTOMER " + customer);

                for (User user: customerUsers.getInvoiceEmails(customer)) {
                    sendInvoicePaidEmail(event, user);
                }

                invoice.setSentAt(new Date());
                invoice.setUpdatedBy(event.getLoggedInRole()
                        .getUser()
                        .getLogin());
            }
        } catch (Exception e) {
            log.error("EXCEPTION IN Invoice Created LISTENER " + e.getMessage(), e);
            throw e;
        }
    }

    private void sendInvoicePaidEmail(InvoicePaidEvent event, User user)
    {
        try {
            Invoice invoice = event.getInvoice();

            Map<String, Object> templateVariables = new HashMap<String, Object>();
            log.debug(
                    "USER NAME " + user + " " + user.getFirstname() + " " + user.getLastname() + " " + user.fullName());

            templateVariables.put("invoiceId", invoice.getId());
            templateVariables.put("name", user.fullName());
            templateVariables.put("invoiceCurrency", "$");
            templateVariables.put("invoiceRemaining", invoice.amountRemaining());
            templateVariables.put("link", host + invoiceLink + invoice.getId());

            mailService.send(messages.get("freightcom.invoice.email.paid.subject", new Object[] { invoice.getId() }),
                    user.getEmail(), "mail/html/invoice-paid.html", templateVariables, emailFromAddress);
            log.debug("ASYNC MESSAGE SENT TO " + user.getEmail());
        } catch (Exception e) {
            log.error("ASYNC EXCEPTION IN INVOICE CREATE LISTENER " + e.getMessage(), e);
        }
    }

    @Override
    public void publishEvent(Object event)
    {
        publisher.publishEvent(event);

    }

    @Override
    public ApiSession getApiSession()
    {
        return new ApiSession();
    }

    @Override
    public ObjectBase getObjectBase()
    {
        return objectBase;
    }

    @Override
    public UserRole getRole()
    {
        // Returns system role
        return objectBase.getSystemRole();
    }
}
