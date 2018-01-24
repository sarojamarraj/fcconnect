package com.freightcom.api.model.views;


import org.springframework.hateoas.core.Relation;
import com.freightcom.api.model.OrderStatus;

@Relation(collectionRelation = "orderStatus")
public class OrderStatusView
{
    private final OrderStatus orderStatus;

    public OrderStatusView(OrderStatus orderStatus)
    {
        this.orderStatus = orderStatus;
    }

    public Long getId() {
        return orderStatus.getId();
    }

    public String getName() {
        return orderStatus.getName();
    }
}
