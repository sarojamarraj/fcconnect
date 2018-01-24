package com.freightcom.api.services.converters;

import org.springframework.core.convert.converter.Converter;


import com.freightcom.api.model.OrderStatus;
import com.freightcom.api.model.views.OrderStatusView;

public class OrderStatusConverter implements Converter<OrderStatus, OrderStatusView>
{

    @Override
    public OrderStatusView convert(OrderStatus order)
    {
        return new OrderStatusView(order);
    }

}
