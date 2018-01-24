package com.freightcom.api.events;

import com.freightcom.api.model.Payable;
import com.freightcom.api.model.UserRole;

public class PayablesCreatedEvent extends ApplicationEvent
{
    private final Payable payable;
    private final UserRole role;

    public PayablesCreatedEvent(Payable payable, UserRole role)
    {
        this.payable = payable;
        this.role = role;
    }

    public String toString()
    {
        return "Payable created " + payable + " R " + getRole();
    }

    public UserRole getRole()
    {
        return role;
    }

    public Payable getPayable()
    {
        return payable;
    }

}
