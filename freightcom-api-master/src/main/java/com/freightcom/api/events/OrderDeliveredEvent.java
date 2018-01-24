package com.freightcom.api.events;

import com.freightcom.api.model.CustomerOrder;
import com.freightcom.api.model.User;

public class OrderDeliveredEvent extends OrderEvent
{
    private final CustomerOrder order;
    private final User loggedInUser;

    public OrderDeliveredEvent(CustomerOrder order, User loggedInUser)
    {
        this.order = order;
        this.loggedInUser = loggedInUser;
    }

    public String toString()
    {
        return "Order booked " + getLoggedInUser();
    }

    public User getLoggedInUser()
    {
        return loggedInUser;
    }

    public CustomerOrder getOrder()
    {
        return order;
    }

}
