package com.freightcom.api.events;

import com.freightcom.api.model.CustomerOrder;
import com.freightcom.api.model.User;

public class QuoteSendEvent extends OrderEvent
{
    private final CustomerOrder order;
    private final User loggedInUser;

    public QuoteSendEvent(final CustomerOrder order,
                          final User loggedInUser)
    {
        this.order = order;
        this.loggedInUser = loggedInUser;
    }

    @Override
    public User getUser()
    {
        return loggedInUser;
    }

    public CustomerOrder getOrder()
    {
        return order;
    }

    public String toString()
    {
        return "QuoteSendEvent " + order;
    }
}
