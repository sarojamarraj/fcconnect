package com.freightcom.api.model.views;

import java.math.BigDecimal;
import java.util.Map;

import com.freightcom.api.model.Customer;



public class SimpleCustomerView
    {
        private final Customer customer;

        public SimpleCustomerView(final Customer customer)
        {
            this.customer = customer;
        }

        public Long getCustomerId()
        {
            return customer.getId();
        }

        public Long getId()
        {
            return customer.getId();
        }

        public String getName()
        {
            return customer.getName();
        }

        public Map<String,BigDecimal> getCreditAvailable()
        {
            return customer.getCreditAvailableMap();
        }
}
