package com.freightcom.api.events;

import com.freightcom.api.model.Charge;
import com.freightcom.api.model.LoggedEvent;
import com.freightcom.api.model.User;
import com.freightcom.api.model.UserRole;

public class ChargeDisputed extends OrderEvent
{
    private final Charge charge;
    private final UserRole role;
    private final LoggedEvent statusMessage;

    public ChargeDisputed(final Charge charge,
                          final LoggedEvent statusMessage,
                          final UserRole role)
    {
        this.charge = charge;
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

    public Charge getCharge()
    {
        return charge;
    }

    public String toString()
    {
        return "Charge disputed " + charge;
    }

    public LoggedEvent getStatusMessage()
    {
        return statusMessage;
    }
}
