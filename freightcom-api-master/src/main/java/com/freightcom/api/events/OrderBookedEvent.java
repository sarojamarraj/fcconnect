package com.freightcom.api.events;

import com.freightcom.api.model.CustomerOrder;
import com.freightcom.api.model.User;

public class OrderBookedEvent extends OrderEvent
{
    private final CustomerOrder order;
    private final User loggedInUser;

    public OrderBookedEvent(CustomerOrder order, User loggedInUser)
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
