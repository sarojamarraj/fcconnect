package com.freightcom.api.model.views;

import java.math.BigDecimal;
import com.freightcom.api.model.Customer;



public class CustomerFreightcomView extends CustomerView
{
    public CustomerFreightcomView(Customer customer)
    {
        super(customer);
    }

    public BigDecimal getCreditLimit()
    {
        return customer.getCreditLimit();
    }
}
