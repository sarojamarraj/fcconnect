package com.freightcom.api.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.bugsnag.Bugsnag;
import com.bugsnag.Severity;
import com.freightcom.api.events.ApplicationEvent;

@Component
public class ApplicationEventListener
{
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final Bugsnag bugsnag;

    @Autowired
    public ApplicationEventListener(final Bugsnag bugsnag)
    {
        this.bugsnag =  bugsnag;
    }

    @EventListener
    @Transactional
    public void handleApplicationEvent(ApplicationEvent event)
    {
        try {
            if (bugsnag != null) {
                bugsnag.notify(new ApplicationEventException(event), Severity.INFO);
            }
        } catch (Exception bugSnagEvent) {
            log.error("Bugsnag problem: " + bugSnagEvent);
        } catch (Throwable bugSnagEvent) {
            log.error("Bugsnag problem: " + bugSnagEvent);
        }
    }
}
