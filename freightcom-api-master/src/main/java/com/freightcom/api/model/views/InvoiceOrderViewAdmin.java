package com.freightcom.api.model.views;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.freightcom.api.model.CustomerOrder;


public class InvoiceOrderViewAdmin extends InvoiceOrderViewStaff
{
    @SuppressWarnings("unused")
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public InvoiceOrderViewAdmin(CustomerOrder order)
    {
        super(order);
    }

    public BigDecimal getProfit()
    {
        return order.getProfit();
    }
}
