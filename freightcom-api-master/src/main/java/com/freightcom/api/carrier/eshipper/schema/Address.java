package com.freightcom.api.carrier.eshipper.schema;

import javax.xml.bind.annotation.XmlAttribute;


public class Address
{
    private String id;
    private String company;
    private String address1;
    private String address2;
    private String city;
    private String state;
    private String country;
    private String zip;
    private Boolean residential;
    private Boolean tailgateRequired;
    private String phone;
    private String attention;
    private String email;
    private String instructions;
    private Boolean confirmDelivery;
    private Boolean notifyRecipient;


    @XmlAttribute
    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    @XmlAttribute
    public String getCompany()
    {
        return company;
    }

    public void setCompany(String company)
    {
        this.company = company;
    }

    @XmlAttribute
    public String getAddress1()
    {
        return address1;
    }

    public void setAddress1(String address1)
    {
        this.address1 = address1;
    }

    @XmlAttribute
    public String getAddress2()
    {
        return address2;
    }

    public void setAddress2(String address2)
    {
        this.address2 = address2;
    }

    @XmlAttribute
    public String getCity()
    {
        return city;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    @XmlAttribute
    public String getState()
    {
        return state;
    }

    public void setState(String state)
    {
        this.state = state;
    }

    @XmlAttribute
    public String getCountry()
    {
        return country;
    }

    public void setCountry(String country)
    {
        this.country = country;
    }

    @XmlAttribute
    public String getZip()
    {
        return zip;
    }

    public void setZip(String zip)
    {
        this.zip = zip;
    }

    @XmlAttribute
    public Boolean getResidential()
    {
        return residential;
    }

    public void setResidential(Boolean residential)
    {
        this.residential = residential;
    }

    @XmlAttribute
    public Boolean getTailgateRequired()
    {
        return tailgateRequired;
    }

    public void setTailgateRequired(Boolean tailgateRequired)
    {
        this.tailgateRequired = tailgateRequired;
    }

    @XmlAttribute
    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    @XmlAttribute
    public String getAttention()
    {
        return attention;
    }

    public void setAttention(String attention)
    {
        this.attention = attention;
    }

    @XmlAttribute
    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    @XmlAttribute
    public String getInstructions()
    {
        return instructions;
    }

    public void setInstructions(String instructions)
    {
        this.instructions = instructions;
    }

    @XmlAttribute
    public Boolean getConfirmDelivery()
    {
        return confirmDelivery;
    }

    public void setConfirmDelivery(Boolean confirmDelivery)
    {
        this.confirmDelivery = confirmDelivery;
    }

    @XmlAttribute
    public Boolean getNotifyRecipient()
    {
        return notifyRecipient;
    }

    public void setNotifyRecipient(Boolean notifyRecipient)
    {
        this.notifyRecipient = notifyRecipient;
    }
}
