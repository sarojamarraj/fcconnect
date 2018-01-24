package com.freightcom.api.events;

import com.freightcom.api.model.Customer;
import com.freightcom.api.model.User;
import com.freightcom.api.model.UserRole;

public class CustomerInvitationEvent extends ApplicationEvent
{
    private final Customer customer;
    private final String emailAddress;
    private final UserRole role;

    public CustomerInvitationEvent(final Customer customer,
                                   final String emailAddress,
                                   final UserRole role)
    {
        this.customer = customer;
        this.emailAddress = emailAddress;
        this.role = role;
    }

    @Override
    public User getUser()
    {
        return role == null ? null : role.getUser();
    }

    public String toString()
    {
        return "Customer invitation " + customer + " " + emailAddress;
    }

}
