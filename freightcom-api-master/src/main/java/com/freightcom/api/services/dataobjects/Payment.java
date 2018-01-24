package com.freightcom.api.services.dataobjects;

import java.math.BigDecimal;

public class Payment
{
    private BigDecimal payment;
    private String creditCardNumber;
    private String nameOnCard;
    private String cardType;
    private String cvs;
    private Integer expiryMonth;
    private Integer expiryYear;
    private Long creditCardId;

    public BigDecimal getPayment()
    {
        return payment;
    }

    public String getCardType()
    {
        return cardType;
    }

    public void setCardType(String cardType)
    {
        this.cardType = cardType;
    }

    public void setPayment(BigDecimal payment)
    {
        this.payment = payment;
    }

    public String getCreditCardNumber()
    {
        return creditCardNumber;
    }

    public void setCreditCardNumber(String creditCardNumber)
    {
        this.creditCardNumber = creditCardNumber;
    }

    public String getCvs()
    {
        return cvs;
    }

    public void setCvs(String cvs)
    {
        this.cvs = cvs;
    }

    public Integer getExpiryMonth()
    {
        return expiryMonth;
    }

    public void setExpiryMonth(Integer expiryMonth)
    {
        this.expiryMonth = expiryMonth;
    }

    public Integer getExpiryYear()
    {
        return expiryYear;
    }

    public void setExpiryYear(Integer expiryYear)
    {
        this.expiryYear = expiryYear;
    }

    public String getNameOnCard()
    {
        return nameOnCard;
    }

    public void setNameOnCard(String nameOnCard)
    {
        this.nameOnCard = nameOnCard;
    }

    public Long getCreditCardId()
    {
        return creditCardId;
    }

    public void setCreditCardId(Long creditCardId)
    {
        this.creditCardId = creditCardId;
    }
}
