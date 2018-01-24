package com.freightcom.api.events;

import com.freightcom.api.model.User;
import com.freightcom.api.model.UserRole;
import com.freightcom.api.services.UserDetailsImpl;

public class RoleDeletedEvent extends ApplicationEvent
{
    private final User user;
    private final UserDetailsImpl loggedInUser;
    private final Long customerId;
    private final UserRole role;

    public RoleDeletedEvent(final User user, final UserRole role, final Long customerId, final UserDetailsImpl loggedInUser)
    {
        this.user = user;
        this.role = role;
        this.customerId = customerId;
        this.loggedInUser = loggedInUser;
    }

    @Override
    public User getUser()
    {
        return user;
    }

    @Override
    public UserRole getRole() {
        return role;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public UserDetailsImpl getLoggedInUser() {
        return loggedInUser;
    }

    public String toString()
    {
        return "RoleDeletedEvent " + getUser() + " " + getCustomerId() + " " + getRole();
    }
}
