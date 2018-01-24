package com.freightcom.api.model.views;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.hateoas.core.Relation;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.freightcom.api.model.Invoice;

@Relation(collectionRelation = "invoice")
public class InvoiceListView implements View
{
    private final Invoice invoice;

    public InvoiceListView(Invoice invoice)
    {
        this.invoice = invoice;
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
    public ZonedDateTime getViewedAt()
    {
        return invoice.getViewedAt();
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    public ZonedDateTime getDueDate()
    {
        return invoice.getDueAt();
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

    public BigDecimal getAmountRemaining()
    {
        return invoice.amountRemaining();
    }
}
