package com.freightcom.api.services.converters;

import org.springframework.core.convert.converter.Converter;


import com.freightcom.api.model.OrderRateQuote;
import com.freightcom.api.model.views.OrderRateQuoteView;

public class OrderRateQuoteConverter implements Converter<OrderRateQuote, OrderRateQuoteView>
{

    @Override
    public OrderRateQuoteView convert(OrderRateQuote order)
    {
        return new OrderRateQuoteView(order);
    }

}
