package com.freightcom.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bugsnag.Report;
import com.bugsnag.callbacks.Callback;
import com.freightcom.api.model.User;
import com.freightcom.api.services.ApplicationEventException;

@Component
public class BugsnagCallback implements Callback
{
    @Autowired
    private  ApiSession apiSession;

    public BugsnagCallback()
    {
    }

    @Override
    public void beforeNotify(Report report)
    {
        User user = null;

        if (report.getException() instanceof ApplicationEventException) {
            user = ((ApplicationEventException) report.getException()).getUser();
        }

        if (user == null && apiSession != null) {
            user = apiSession.getUser();
        }

        if (user == null) {
            report.setUserName("user not available");
        } else {
            report.setUserName(user.fullName());
            report.setUserEmail(user.getEmail());
            report.setUserId(user.getLogin());
        }
    }

}
