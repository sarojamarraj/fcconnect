package com.freightcom.api.events;

import com.freightcom.api.model.CustomerOrder;
import com.freightcom.api.model.OrderStatus;
import com.freightcom.api.model.User;
import com.freightcom.api.model.UserRole;

public class OrderStatusChange extends OrderEvent
{
    private final CustomerOrder order;
    private final UserRole role;
    private final OrderStatus oldStatus;
    private final OrderStatus newStatus;
    private final String comment;

    public OrderStatusChange(final CustomerOrder order, final OrderStatus oldStatus, final OrderStatus newStatus,
            final String comment, final UserRole role)
    {
        this.order = order;
        this.role = role;
        this.newStatus = newStatus;
        this.oldStatus = oldStatus;
        this.comment = comment;
    }

    public UserRole getRole()
    {
        return role;
    }

    @Override
    public User getUser()
    {
        return role.getUser();
    }

    public CustomerOrder getOrder()
    {
        return order;
    }

    public String toString()
    {
        return "Order Status Change " + order + " " + oldStatus + " to " + newStatus;
    }

    public String getComment()
    {
        return comment;
    }
}
