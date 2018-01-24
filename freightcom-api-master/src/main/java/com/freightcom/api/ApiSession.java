package com.freightcom.api;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import com.freightcom.api.model.User;
import com.freightcom.api.model.UserRole;

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ApiSession
{
    private UserRole role = null;

    public UserRole getRole() {
        return role;
    }

    public User getUser() {
        User user = null;

        if (role != null) {
            user = role.getUser();
        }

        return user;
    }

    public Long getCustomerId() {
        return role == null ? null : role.getCustomerId();
    }

    public boolean isCustomer() {
        return role == null ? false : role.isCustomer();
    }

    public boolean isAgent() {
        return role == null ? false : role.isAgent();
    }

    public boolean isFreightcom() {
        return role == null ? false : role.isFreightcom();
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public boolean hasRole() {
        return role != null;
    }

    public void check() throws NoRoleSelectedException
    {
        if (role == null) {
            throw new NoRoleSelectedException("No role selected");
        }
    }

    public String toString()
    {
        return "API SESSION " + role + " c " + getCustomerId();
    }

    public boolean isAdmin()
    {
        return role == null ? false : role.isAdmin();
    }
}
