package com.freightcom.api.events;

import com.freightcom.api.model.CustomerOrder;
import com.freightcom.api.model.Claim;
import com.freightcom.api.model.Claim.Status;
import com.freightcom.api.model.LoggedEvent;
import com.freightcom.api.model.User;
import com.freightcom.api.model.UserRole;

public class ClaimUpdatedEvent extends OrderEvent
{
    private final Status previousStatus;
    private final CustomerOrder order;
    private final UserRole role;
    private final LoggedEvent statusMessage;

    public ClaimUpdatedEvent(final CustomerOrder order,
                             final Status status,
                             final LoggedEvent statusMessage,
                             final UserRole role)
    {
        this.order = order;
        this.previousStatus = status;
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

    public Claim getClaim()
    {
        return order.getClaim();
    }

    public CustomerOrder getOrder()
    {
        return order;
    }

    public Status getPreviousStatus()
    {
        return previousStatus;
    }

    public String toString()
    {
        return "Claim status change " + order.getClaim();
    }

    public LoggedEvent getStatusMessage()
    {
        return statusMessage;
    }
}
