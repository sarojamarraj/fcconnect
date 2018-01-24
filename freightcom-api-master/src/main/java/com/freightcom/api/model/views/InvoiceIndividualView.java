package com.freightcom.api.model.views;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.core.Relation;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.freightcom.api.model.AppliedCredit;
import com.freightcom.api.model.CustomerOrder;
import com.freightcom.api.model.GroupedCharge;
import com.freightcom.api.model.GroupedTax;
import com.freightcom.api.model.Invoice;
import com.freightcom.api.model.LoggedEvent;
import com.freightcom.api.model.UserRole;

@Relation(collectionRelation = "invoice")
public class InvoiceIndividualView implements View
{
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final Invoice invoice;
    private final UserRole role;

    private List<GroupedCharge> groupedCharges;
    private List<GroupedTax> groupedTaxes;

    public InvoiceIndividualView(Invoice invoice)
    {
        this.invoice = invoice;
        this.role = null;
    }

    public InvoiceIndividualView(Invoice invoice, UserRole role)
    {
        this.invoice = invoice;
        this.role = role;
    }

    public String getViewName()
    {
        return "InvoiceIndividualView";
    }

    public Long getId()
    {
        return invoice.getId();
    }

    public Map<String, Object> getCustomer()
    {
        Map<String, Object> customer = new HashMap<String, Object>();

        if (invoice.getCustomer() != null) {
            customer.put("id", invoice.getCustomer()
                    .getId());
            customer.put("name", invoice.getCustomer()
                    .getName());
            customer.put("address", invoice.getCustomer()
                    .getAddress());
            customer.put("city", invoice.getCustomer()
                    .getCity());
            customer.put("postalCode", invoice.getCustomer()
                    .getPostalCode());
            customer.put("province", invoice.getCustomer()
                    .getProvince());
            customer.put("country", invoice.getCustomer()
                    .getCountry());
            customer.put("contact", invoice.getCustomer()
                    .getContact());
            customer.put("email", invoice.getCustomer()
                    .getEmail());
            customer.put("phone", invoice.getCustomer()
                    .getPhone());
        }

        return customer;
    }

    public List<AppliedCredit> getCredits()
    {
        return invoice.getCredits();
    }

    public BigDecimal getPaidAmount()
    {
        return invoice.getPaidAmount();
    }

    public BigDecimal getCreditedAmount()
    {
        return invoice.getCreditedAmount();
    }

    public List<InvoiceOrderView> getOrders()
    {
        int count = 0;
        List<CustomerOrder> orders = invoice.getOrders();
        List<InvoiceOrderView> projected = new ArrayList<InvoiceOrderView>();

        if (orders != null) {
            for (CustomerOrder order : orders) {
                if (order != null) {
                    if (role == null) {
                        projected.add(new InvoiceOrderView(order));
                    } else {
                        switch (role.getRoleName()) {
                        case UserRole.ROLE_ADMIN:
                            projected.add(new InvoiceOrderViewAdmin(order));
                            break;

                        case UserRole.ROLE_FREIGHTCOM_STAFF:
                            projected.add(new InvoiceOrderViewStaff(order));
                            break;

                        default:
                            projected.add(new InvoiceOrderView(order));
                            break;
                        }
                    }
                }

                if (count > 200) {
                    log.debug("INVOICE VIEW MULTI ORDERS " + invoice.getId() + " " + orders.size());
                    break;
                }

                count++;
            }
        }

        return projected;
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    public ZonedDateTime getPaidAt()
    {
        return invoice.getPaidAt();
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    public ZonedDateTime getViewedAt()
    {
        return invoice.getViewedAt();
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    public ZonedDateTime getDueDate()
    {
        return invoice.getDueAt();
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    public ZonedDateTime getInvoiceDate()
    {
        return invoice.getInvoiceDate();
    }

    public Integer getPaymentStatus()
    {
        return invoice.getPaymentStatus();
    }

    public BigDecimal getAmount()
    {
        return invoice.getAmount();
    }

    public BigDecimal getSubtotal()
    {
        return invoice.getSubtotal();
    }

    public String getCurrency()
    {
        return invoice.getCurrency();
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    public ZonedDateTime getDatePaid()
    {
        return invoice.getDatePaid();
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    public ZonedDateTime getUpdatedAt()
    {
        return invoice.getUpdatedAt();
    }

    public BigDecimal getAmountRemaining()
    {
        return invoice.amountRemaining();
    }

    public List<GroupedCharge> getGroupedCharges()
    {
        return groupedCharges;
    }

    public InvoiceIndividualView setGroupedCharges(List<GroupedCharge> groupedCharges)
    {
        this.groupedCharges = groupedCharges;

        return this;
    }

    public List<GroupedTax> getGroupedTaxes()
    {
        return groupedTaxes;
    }

    public List<MessageView> getMessages()
    {
        return invoice.getMessages()
                .stream()
                .map(message -> new MessageView(message))
                .collect(Collectors.toList());
    }

    public InvoiceIndividualView setGroupedTaxes(List<GroupedTax> groupedTaxes)
    {
        this.groupedTaxes = groupedTaxes;

        return this;
    }

    public Object getPaymentHistory()
    {
        return invoice.getPaymentHistory();
    }

    private class MessageView
    {
        private final LoggedEvent message;

        public MessageView(final LoggedEvent message)
        {
            this.message = message;
        }

        @SuppressWarnings("unused")
        public Object getId()
        {
            return message.getId();
        }

        @SuppressWarnings("unused")
        public Object getMessageType()
        {
            return message.getMessageType();
        }

        @SuppressWarnings("unused")
        public Object getPrivate()
        {
            return message.getPrivate();
        }

        @SuppressWarnings("unused")
        public Object getComment()
        {
            return message.getComment();
        }

        @SuppressWarnings("unused")
        public Object getMessage()
        {
            return message.getMessage();
        }

        @SuppressWarnings("unused")
        public Object getOrderId()
        {
            return message.getEntityId();
        }
    }
}
