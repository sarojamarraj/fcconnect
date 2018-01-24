package com.freightcom.api.events;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.freightcom.api.model.TransactionalEntity;
import com.freightcom.api.model.User;

public class SystemLogEvent extends ApplicationEvent
{
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final TransactionalEntity object;
    private final User loggedInUser;
    private final String action;
    private final String message;

    public SystemLogEvent(TransactionalEntity object,
                          User loggedInUser,
                          String action,
                          String message)
    {
        this.object = object;;
        this.loggedInUser = loggedInUser;
        this.action = action;
        this.message = message;

        log.debug("CREATED SYSTEM LOG EVENT " + this);
    }

    public TransactionalEntity getObject()
    {
        return object;
    }

    public User getLoggedInUser()
    {
        return loggedInUser;
    }

    public String getAction()
    {
        return action;
    }

    public String getMessage()
    {
        return message;
    }

    public String toString()
    {
        return "System Log Event " + action + " " + object;
    }

}
