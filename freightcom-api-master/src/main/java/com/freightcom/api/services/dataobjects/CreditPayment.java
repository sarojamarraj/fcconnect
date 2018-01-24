package com.freightcom.api.services.dataobjects;

public class CreditPayment extends Payment
{
    private Long customerId;

    public String getCcType()
    {
        return getCardType();
    }

    public void setCcType(String ccType)
    {
        setCardType(ccType);
    }

    public Long getCustomerId()
    {
        return customerId;
    }

    public void setCustomerId(Long customerId)
    {
        this.customerId = customerId;
    }
}
