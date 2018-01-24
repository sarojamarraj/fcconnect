package com.freightcom.api.services;

import com.freightcom.api.events.ApplicationEvent;
import com.freightcom.api.model.User;
import com.freightcom.api.model.UserRole;

public class ApplicationEventException extends RuntimeException
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private final ApplicationEvent event;

    public ApplicationEventException(final ApplicationEvent event)
    {
        super(event.toString());

        this.event = event;
    }

    public ApplicationEvent getEvent()
    {
        return event;
    }

    public User getUser()
    {
        return event.getUser();
    }

    public UserRole getRole()
    {
        return event.getRole();
    }
}
