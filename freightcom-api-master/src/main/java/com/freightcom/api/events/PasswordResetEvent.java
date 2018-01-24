package com.freightcom.api.events;

import com.freightcom.api.model.User;

public class PasswordResetEvent extends ApplicationEvent
{
    private final User user;
    private final String newPassword;

    public PasswordResetEvent(User user, String newPassword)
    {
        this.user = user;
        this.newPassword = newPassword;
    }

    @Override
    public User getUser()
    {
        return user;
    }

    public String getNewPassword()
    {
        return newPassword;
    }

    public String toString()
    {
        return "Password reset " + user;
    }

}
