package com.freightcom.api.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.freightcom.api.events.SystemLogEvent;
import com.freightcom.api.model.SystemLog;
import com.freightcom.api.repositories.SystemLogRepository;

/**
 * @author bryan
 *
 */
/**
 * @author bryan
 *
 */
@Component
public class SystemLogService
{
    final SystemLogRepository SystemLogReposititory;

    @Autowired
    public SystemLogService(final SystemLogRepository SystemLogReposititory)
    {
        this.SystemLogReposititory = SystemLogReposititory;
    }

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMPLETION)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleLogEvent(SystemLogEvent event) throws Exception
    {
        log.debug("System log event LISTENER " + event);

        try {
            SystemLog logEntry = new SystemLog();

            if (event.getObject() != null) {
                logEntry.setObjectId(event.getObject()
                        .getId());
            }

            if (event.getLoggedInUser() != null) {
                logEntry.setUserId(event.getLoggedInUser()
                        .getId());
            }

            logEntry.setAction(event.getAction());
            logEntry.setComments(event.getMessage());

            SystemLogReposititory.save(logEntry);
        } catch (Exception e) {
            log.error("EXCEPTION IN SystemLogEvent LISTENER " + e.getMessage(), e);
            throw e;
        }
    }
}
