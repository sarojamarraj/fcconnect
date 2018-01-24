package com.freightcom.api.model.views;

import java.util.HashMap;
import java.util.Map;

import com.freightcom.api.model.Customer;
import com.freightcom.api.model.CustomerOrderSummary;
import com.freightcom.api.model.ShippingAddress;

public class OrderSummaryView implements View
{
    private CustomerOrderSummary order;

    public OrderSummaryView(CustomerOrderSummary object)
    {
        this.order = object;
    }

    public String getViewName()
    {
        return "OrderSummaryView";
    }

    public Long getId()
    {
        return order.getId();
    }

    public Map<String,Object> getCustomer()
    {
        Map<String,Object> data = new HashMap<String,Object>();

        Customer customer = order.getCustomer();

        if (customer != null) {
            data.put("id",  customer.getId());
            data.put("name",  customer.getName());
        }


        return data;
    }

    public ShippingAddress getShipTo()
    {
        return order.getShipTo();
    }

    public ShippingAddress getShipFrom()
    {
        return order.getShipFrom();
    }

}
