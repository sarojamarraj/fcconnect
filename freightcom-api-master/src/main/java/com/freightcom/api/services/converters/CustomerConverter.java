package com.freightcom.api.services.converters;

import org.springframework.core.convert.converter.Converter;


import com.freightcom.api.model.Customer;
import com.freightcom.api.model.views.CustomerListView;
import com.freightcom.api.model.views.View;

public class CustomerConverter implements Converter<Customer, View>
{

    @Override
    public View convert(Customer order)
    {
        return new CustomerListView(order);
    }

}
