package com.freightcom.api.events;

import com.freightcom.api.model.CustomerOrder;
import com.freightcom.api.model.UserRole;

public class OrderDeletedEvent extends OrderEvent
{
    private final CustomerOrder order;
    private final UserRole loggedInRole;

    public OrderDeletedEvent(CustomerOrder order, UserRole loggedInRole)
    {
        this.order = order;
        this.loggedInRole = loggedInRole;
    }

    public String toString()
    {
        return "Order booked " + getLoggedInRole();
    }

    public UserRole getLoggedInRole()
    {
        return loggedInRole;
    }

    public CustomerOrder getOrder()
    {
        return order;
    }

}
