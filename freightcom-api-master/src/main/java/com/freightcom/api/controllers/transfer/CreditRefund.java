package com.freightcom.api.controllers.transfer;

import java.math.BigDecimal;



public class CreditRefund
{
    private BigDecimal amount = BigDecimal.ZERO;
    private String currency = "CAD";
    private Long customerId;
    private String note;
    private CreditCardInfo ccInfo;

    public CreditRefund()
    {
    }

    public String toString()
    {
        return "Credit Refund";
    }

    public Long getCustomerId()
    {
        return customerId;
    }

    public void setCustomerId(Long customerId)
    {
        this.customerId = customerId;
    }

    public BigDecimal getAmount()
    {
        return amount;
    }

    public void setAmount(BigDecimal amount)
    {
        this.amount = amount;
    }

    public String getCurrency()
    {
        return currency;
    }

    public void setCurrency(String currency)
    {
        this.currency = currency;
    }

    public String getNote()
    {
        return note;
    }

    public void setNote(String note)
    {
        this.note = note;
    }

    public CreditCardInfo getCcInfo()
    {
        return ccInfo;
    }

    public void setCcInfo(CreditCardInfo ccInfo)
    {
        this.ccInfo = ccInfo;
    }
}
