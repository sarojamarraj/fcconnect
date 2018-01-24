package com.freightcom.api;

import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class SessionEventListener
{
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public SessionEventListener()
    {

    }

    @EventListener
    public void handleSessionExpired(SessionExpiredEvent event) throws SessionExpiredException
    {
        log.debug("RECEIVED SESSION EXPIRED EVENT " + event);
    }

    @EventListener
    public void handleDatabaseException(SQLException event) throws SQLException
    {
        log.debug("SQL EXCEPTION " + event);
    }
}
