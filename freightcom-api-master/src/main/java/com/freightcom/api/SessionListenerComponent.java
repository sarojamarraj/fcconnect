package com.freightcom.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.security.web.session.HttpSessionDestroyedEvent;
import org.springframework.stereotype.Component;

@Component
public class SessionListenerComponent implements ApplicationListener<HttpSessionDestroyedEvent>
{
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public SessionListenerComponent(final ApplicationEventPublisher publisher) {
    }

    public void onApplicationEvent(HttpSessionDestroyedEvent event)
    {
        log.debug("Session destroyed event " + event);
    }
}
