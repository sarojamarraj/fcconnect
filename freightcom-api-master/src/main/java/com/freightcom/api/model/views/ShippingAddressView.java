package com.freightcom.api.model.views;

import com.freightcom.api.model.ShippingAddress;
import com.freightcom.api.model.TransactionalEntity;
import com.freightcom.api.util.MapBuilder;

public class ShippingAddressView extends BaseView
{
    private final ShippingAddress address;
    private final boolean nullId;

    public static Object create(ShippingAddress address)
    {
        return address == null ? MapBuilder.emptyMap() : new ShippingAddressView(address);
    }

    public ShippingAddressView(final ShippingAddress address)
    {
        this.address = address;
        this.nullId = false;
    }

    public ShippingAddressView(final ShippingAddress address, final boolean nullId)
    {
        this.address = address;
        this.nullId = nullId;
    }

    @Override
    public Long getId()
    {
        return nullId ? null : address.getId();
    }

    @Override
    public TransactionalEntity object()
    {
        return address;
    }

    public Long getAddressBookId()
    {
        return address.getAddressBookId();
    }

    public String getCity()
    {
        return address.getCity();
    }

    public String getProvince()
    {
        return address.getProvince();
    }

    public String getCountry()
    {
        return address.getCountry();
    }

    public String getAddress2()
    {
        return address.getAddress2();
    }

    public String getAddress1()
    {
        return address.getAddress1();
    }

    public String getPhone()
    {
        return address.getPhone();
    }

    public String getCompanyName()
    {
        return address.getCompanyName();
    }

    public String getConsigneeName()
    {
        return address.getConsigneeName();
    }

    public String getContactName()
    {
        return address.getContactName();
    }

    public String getPostalCode()
    {
        return address.getPostalCode();
    }

    public String getEmail()
    {
        return address.getEmail();
    }

    public String getContactEmail()
    {
        return address.getContactEmail();
    }

    public Boolean getEmailNotification()
    {
        return address.getEmailNotification();
    }

    public String getIsResidential()
    {
        return address.getIsResidential();
    }
}
