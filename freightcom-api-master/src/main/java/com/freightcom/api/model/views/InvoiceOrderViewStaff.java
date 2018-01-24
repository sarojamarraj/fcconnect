package com.freightcom.api.model.views;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.freightcom.api.model.CustomerOrder;


public class InvoiceOrderViewStaff extends InvoiceOrderView
{
    @SuppressWarnings("unused")
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public InvoiceOrderViewStaff(CustomerOrder order)
    {
        super(order);
    }
}
