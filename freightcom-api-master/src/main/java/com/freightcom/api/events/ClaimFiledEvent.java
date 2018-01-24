package com.freightcom.api.events;

import com.freightcom.api.model.CustomerOrder;
import com.freightcom.api.model.UserRole;

public class ClaimFiledEvent extends OrderEvent
{
    private final CustomerOrder order;
    private final UserRole loggedInRole;

    public ClaimFiledEvent(CustomerOrder order, UserRole loggedInRole)
    {
        this.order = order;
        this.loggedInRole = loggedInRole;
    }

    public String toString()
    {
        return "Claim Filed " + getLoggedInRole();
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
