package com.freightcom.api.services.converters;

import org.springframework.core.convert.converter.Converter;

import com.freightcom.api.model.CustomerOrderSummary;
import com.freightcom.api.model.views.OrderSummaryView;


public class OrderSummaryConverter implements Converter<CustomerOrderSummary, OrderSummaryView>
{

    @Override
    public OrderSummaryView convert(CustomerOrderSummary order)
    {
        return new OrderSummaryView(order);
    }

}
