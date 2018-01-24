package com.freightcom.api.services;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.freightcom.api.events.PasswordResetEvent;
import com.freightcom.api.events.RoleDeletedEvent;

@Component
public class UserEventListener
{
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final MailService mailService;
    private final AlertService alertService;
    private final CustomerService customerService;

    @Autowired
    public UserEventListener(final CustomerService customerService, final AlertService alertService,
            final MailService mailService, final UserService userService)
    {
        this.mailService = mailService;
        this.alertService = alertService;
        this.customerService = customerService;
    }

    @EventListener
    @Async
    public void handlePasswordReset(PasswordResetEvent event) throws Exception
    {
        try {
            Map<String, Object> templateVariables = new HashMap<String, Object>();

            templateVariables.put("password", event.getNewPassword());
            templateVariables.put("name", event.getUser()
                    .getLogin());

            log.debug("RECEIVED PASSWORD RESET EVENT " + event);
            mailService.send("Your password has been reset", event.getUser()
                    .getEmail(), "mail/html/reset-password.html", templateVariables);
        } catch (Exception e) {
            log.error("EXCEPTION IN PASSWORD RESET LISTENER " + e.getMessage(), e);
        }
    }

    @EventListener
    @Transactional
    @Async
    public void handleRoleDeletedEvent(RoleDeletedEvent event) throws Exception
    {
        try {
            customerService.alertRoleDeleted(alertService, event);
        } catch (Exception e) {
            log.error("EXCEPTION IN RoleDeletedEvent LISTENER " + e.getMessage(), e);
            throw e;
        }
    }
}
