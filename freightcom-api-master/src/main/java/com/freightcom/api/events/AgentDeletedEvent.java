package com.freightcom.api.events;

import com.freightcom.api.model.User;
import com.freightcom.api.model.UserRole;
import com.freightcom.api.services.UserDetailsImpl;

public class AgentDeletedEvent extends RoleDeletedEvent
{
    private final String reassignment;

    public AgentDeletedEvent(User user, UserRole role, Long customerId, final String reassignment, UserDetailsImpl loggedInUser)
    {
        super(user, role, customerId, loggedInUser);
        
        this.reassignment = reassignment;
    }

    public String getReassignment()
    {
        return reassignment;
    }

}
