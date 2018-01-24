package com.freightcom.api.model.views;

import com.freightcom.api.model.Address;
import com.freightcom.api.model.TransactionalEntity;

public class AddressView extends BaseView
{
    private final Address address;

    public AddressView(final Address address)
    {
        this.address = address;
    }

    @Override
    public TransactionalEntity object()
    {
        return address;
    }

    public String getCity()
    {
        return address.getCity();
    }

    public String getProvince()
    {
        if (address.getProvince() == null) {
            return null;
        } else {
            return address.getProvince().getName();
        }
    }

    public String getCountry()
    {
        if (address.getCountry() == null) {
            return null;
        } else {
            return address.getCountry().getName();
        }
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
        return address.getPhoneNo();
    }

    public String getCompanyName()
    {
        return address.getCompanyName();
    }

    public String getPostalCode()
    {
        return address.getPostalCode();
    }
}
