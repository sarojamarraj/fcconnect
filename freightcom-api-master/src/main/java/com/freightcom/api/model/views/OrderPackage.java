package com.freightcom.api.model.views;

import com.freightcom.api.model.Package;


public class OrderPackage implements View
{
    private final Package orderPackage;
    private final boolean nullId;

    public OrderPackage(final Package orderPackage)
    {
        this.orderPackage = orderPackage;
        nullId = false;
    }

    public OrderPackage(final Package orderPackage, final boolean nullId)
    {
        this.orderPackage = orderPackage;
        this.nullId = nullId;
    }

    public Object getId()
    {
        return nullId ? null : orderPackage.getId();
    }

    public String getHeight()
    {
        return asString(orderPackage.getHeight());
    }

    public String getWidth()
    {
        return asString(orderPackage.getWidth());
    }

    public String getLength()
    {
        return asString(orderPackage.getLength());
    }

    public String getWeight()
    {
        return orderPackage.getWeight();
    }

    public Object getDescription()
    {
        return orderPackage.getDescription();
    }

    private String asString(Object o)
    {
        if (o == null) {
            return null;
        } else {
            return o.toString();
        }
    }
}
