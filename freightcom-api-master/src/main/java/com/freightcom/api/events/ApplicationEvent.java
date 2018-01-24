package com.freightcom.api.events;

import com.freightcom.api.model.User;
import com.freightcom.api.model.UserRole;

public class ApplicationEvent
{
    public User getUser()
    {
        UserRole role = getRole();

        if (role != null) {
            return role.getUser();
        } else {
            return null;
        }
    }

    public UserRole getRole()
    {
        return null;
    }
}
