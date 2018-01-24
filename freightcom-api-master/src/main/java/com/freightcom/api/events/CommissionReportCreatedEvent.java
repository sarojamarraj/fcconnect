package com.freightcom.api.events;

import com.freightcom.api.model.CommissionPayable;
import com.freightcom.api.model.UserRole;

public class CommissionReportCreatedEvent extends ApplicationEvent
{
    private final CommissionPayable payable;
    private final UserRole role;

    public CommissionReportCreatedEvent(CommissionPayable payable, UserRole role)
    {
        this.payable = payable;
        this.role = role;
    }

    public String toString()
    {
        return "Commission report created " + payable + " R " + getRole();
    }

    public UserRole getRole()
    {
        return role;
    }

    public CommissionPayable getPayable()
    {
        return payable;
    }

}
