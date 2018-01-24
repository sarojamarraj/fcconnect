package com.freightcom.api;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.freightcom.api.events.SystemLogEvent;
import com.freightcom.api.services.SystemLogService;

@Component
public class FailedAuthentication implements AuthenticationFailureHandler
{
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SystemLogService logService;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
            throws IOException, ServletException
    {
        log.debug("AUTHENTICATION ERROR");
        log.debug(exception.toString());
        try {
            logService.handleLogEvent(new SystemLogEvent(null, null,
                                                         "login failed", request.getParameter("login")));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            log.error("problem", e);
        }

        response.setStatus(401);
    }

}
