package com.freightcom.api.services.invoices;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.freightcom.api.model.Invoice;

/**
 * @author bryan
 *
 */

public class ChargesInvoiceView
{
    private final Invoice invoice;

    public ChargesInvoiceView(final Invoice invoice)
    {
        this.invoice = invoice;
    }

    public Long getId()
    {
        return invoice.getId();
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    public ZonedDateTime getCreatedAt()
    {
        return invoice.getCreatedAt();
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    public ZonedDateTime getDateGenerated()
    {
        return invoice.getDueAt();
    }

    public BigDecimal getPaidAmount()
    {
        return invoice.getPaidAmount();
    }

    public BigDecimal getAmount()
    {
        return invoice.getAmount();
    }

    public BigDecimal getAmountRemaining()
    {
        return invoice.amountRemaining();
    }

    public String getCurrency()
    {
        return invoice.getCurrency();
    }

    public Integer getPaymentStatus()
    {
        return invoice.getPaymentStatus();
    }
}
