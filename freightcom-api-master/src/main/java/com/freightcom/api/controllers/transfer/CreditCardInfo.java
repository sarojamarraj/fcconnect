package com.freightcom.api.controllers.transfer;

public class CreditCardInfo
{
    private String ccType = "VISA";
    private String creditCardNumber = "";
    private String cvs = "";
    private String expiryMonth = "01";
    private String expiryYear = "18";
    private String nameOnCard = "";
    private String useNew = "new";

    public CreditCardInfo()
    {
    }

    public String toString()
    {
        return "Credit Refund";
    }

    public String getCcType()
    {
        return ccType;
    }

    public void setCcType(String ccType)
    {
        this.ccType = ccType;
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

    public String getExpiryMonth()
    {
        return expiryMonth;
    }

    public void setExpiryMonth(String expiryMonth)
    {
        this.expiryMonth = expiryMonth;
    }

    public String getExpiryYear()
    {
        return expiryYear;
    }

    public void setExpiryYear(String expiryYear)
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

    public String getUseNew()
    {
        return useNew;
    }

    public void setUseNew(String useNew)
    {
        this.useNew = useNew;
    }
}
