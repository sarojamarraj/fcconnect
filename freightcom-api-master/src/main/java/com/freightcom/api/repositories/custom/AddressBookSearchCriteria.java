package com.freightcom.api.repositories.custom;

public class AddressBookSearchCriteria
{
    private Long customerId;
    private String query;
    private String company;
    private String address;
    private String city;
    private String province;
    private String country;
    private String postalCode;
    private String contactName;
    private String contactEmail;
    private String phone;


    public AddressBookSearchCriteria()
    {
    }

    public Long getCustomerId()
    {
        return customerId;
    }

    public AddressBookSearchCriteria setCustomerId(Long customerId)
    {
        this.customerId = customerId;

        return this;
    }

    public String getQuery()
    {
        return query;
    }

    public AddressBookSearchCriteria setQuery(String query)
    {
        this.query = query;

        return this;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getCity()
    {
        return city;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public String getProvince()
    {
        return province;
    }

    public void setProvince(String province)
    {
        this.province = province;
    }

    public String getCompany()
    {
        return company;
    }

    public void setCompany(String company)
    {
        this.company = company;
    }

    public String getCountry()
    {
        return country;
    }

    public void setCountry(String country)
    {
        this.country = country;
    }

    public String getContactName()
    {
        return contactName;
    }

    public void setContactName(String contactName)
    {
        this.contactName = contactName;
    }

    public String getContactEmail()
    {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail)
    {
        this.contactEmail = contactEmail;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getPostalCode()
    {
        return postalCode;
    }

    public void setPostalCode(String postalCode)
    {
        this.postalCode = postalCode;
    }
}
