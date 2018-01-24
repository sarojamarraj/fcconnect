package com.freightcom.api.model.views;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.hateoas.core.Relation;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.freightcom.api.model.Invoice;

@Relation(collectionRelation = "invoice")
public class InvoiceView implements View
{
    private final Invoice invoice;

    public InvoiceView(Invoice invoice)
    {
        this.invoice = invoice;
    }

    public String getViewName()
    {
        return "InvoiceView";
    }

    public Invoice invoice()
    {
        return invoice;
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
            customer.put("creditAvailable", invoice.getCustomer()
                    .getCreditAvailableMap());
        }

        return customer;
    }

    public BigDecimal getPaidAmount()
    {
        return invoice.getPaidAmount();
    }

    public BigDecimal getCreditedAmount()
    {
        return invoice.getCreditedAmount();
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

    @JsonFormat(pattern = "yyyy-MM-dd")
    public ZonedDateTime getDateGenerated()
    {
        return invoice.getDateGenerated();
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
    public ZonedDateTime getCreatedAt()
    {
        return invoice.getCreatedAt();
    }

    public BigDecimal getAmountRemaining()
    {
        return invoice.amountRemaining();
    }
}
