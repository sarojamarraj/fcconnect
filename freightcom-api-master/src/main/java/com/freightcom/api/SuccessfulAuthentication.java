package com.freightcom.api;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.freightcom.api.events.SystemLogEvent;
import com.freightcom.api.services.SystemLogService;
import com.freightcom.api.services.UserDetailsImpl;
import com.freightcom.api.services.UserService;

@Component
public class SuccessfulAuthentication implements AuthenticationSuccessHandler
{
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    // TODO - get from configuration
    public static int sessiontTimeOutMinutes = 120;

    @Autowired
    private UserService userService;

    @Autowired
    private SystemLogService logService;


    /*
     * (non-Javadoc)
     *
     * @see org.springframework.security.web.authentication.
     * AuthenticationSuccessHandler#onAuthenticationSuccess(javax.servlet.http.
     * HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * org.springframework.security.core.Authentication)
     */
    /*
     * (non-Javadoc)
     *
     * @see org.springframework.security.web.authentication.
     * AuthenticationSuccessHandler#onAuthenticationSuccess(javax.servlet.http.
     * HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * org.springframework.security.core.Authentication)
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        UserDetailsImpl userInfo = (UserDetailsImpl) authentication.getPrincipal();
        log.debug("AUTHENTICATION SUCCESS " + userInfo);

        userService.updateLastLogin(userInfo.getUser().getId());
        try {
            logService.handleLogEvent(new SystemLogEvent(null, userInfo.getUser(),
                                                         "login", null));
        } catch (Exception e) {
            log.error("problem,", e);
        }

        response.setContentType("text/html");

        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Credentials", "true");
        response.addHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
        response.getWriter().format(resultForm(userInfo));

        request.getSession().setMaxInactiveInterval(sessiontTimeOutMinutes * 60);
    }

    private String resultForm(UserDetailsImpl userInfo) {
        StringBuilder result = new StringBuilder();

        result.append("<http>  <headers> <frame-options policy=\"ALLOW-FROM *\" /> </headers><body>")
        .append("<input type=\"hidden\" name=\"user-id\" value=\"")
        .append(userInfo.getId())
        .append("\"/>").append("</body></http>");

        return result.toString();
    }

}
