package com.freightcom.api.events;

import com.freightcom.api.model.CustomerOrder;
import com.freightcom.api.model.LoggedEvent;
import com.freightcom.api.model.User;
import com.freightcom.api.model.UserRole;

public class ChargeDisputeResolved extends OrderEvent
{
    private final CustomerOrder order;
    private final UserRole role;
    private final LoggedEvent statusMessage;

    public ChargeDisputeResolved(final CustomerOrder order,
                          final LoggedEvent statusMessage,
                          final UserRole role)
    {
        this.order = order;
        this.role = role;
        this.statusMessage = statusMessage;
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
        return "Charge disputed resolved " + order;
    }

    public LoggedEvent getStatusMessage()
    {
        return statusMessage;
    }
}
