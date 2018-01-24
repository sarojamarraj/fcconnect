package com.freightcom.api.controllers;


/******************************************************************
              WebPal Product Suite Framework Libraries
-------------------------------------------------------------------
(c) 2002-present: all copyrights are with Palomino System Innovations Inc.
(Palomino Inc.) of Toronto, Canada

Unauthorized reproduction, licensing or disclosure of this source
code will be prosecuted. WebPal is a registered trademark of
Palomino System Innovations Inc. To report misuse please contact
info@palominosys.com or call +1 416 964 7333.
*******************************************************************/

import java.security.Principal;

import org.springframework.security.core.Authentication;

import com.freightcom.api.ReportableError;
import com.freightcom.api.services.UserDetailsImpl;


public class BaseController
{
    /**
     *
     */
    protected UserDetailsImpl getLoggedInUser(Principal principal)
    {
        return (UserDetailsImpl) ((Authentication) principal).getPrincipal();
    }

    protected Long asLong(Object value, String message)
    {
        if (value == null) {
            return null;
        } else if (value instanceof Long) {
            return (Long) value;
        } else {
            try {
                return Long.parseLong(value.toString());
            } catch (Exception e) {
                throw new ReportableError(message);
            }
        }
    }
}
