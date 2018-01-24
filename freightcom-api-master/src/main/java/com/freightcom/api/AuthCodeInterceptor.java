package com.freightcom.api;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.freightcom.api.model.UserRole;
import com.freightcom.api.services.UserDetails;

@Component
public class AuthCodeInterceptor extends HandlerInterceptorAdapter
{
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ApiSession apiSession;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
    {
        if (apiSession.getRole() == null) {
            Authentication authentication = SecurityContextHolder.getContext()
                    .getAuthentication();

            if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
                UserDetails user = (UserDetails) authentication.getPrincipal();

                log.debug("PREHANDLE THIS " + user);
                log.debug("REQUEST INTERCEPT PREHANDLE " + apiSession.getRole());

                for (UserRole role : user.getUser()
                        .getAuthorities()) {
                    apiSession.setRole(role);
                    break;
                }
            }
        }

        if (apiSession.getRole() != null) {
            response.addHeader("api-permissions", apiSession.getRole().getJSONPermissions());
        }

        return super.preHandle(request, response, handler);
    }
}
