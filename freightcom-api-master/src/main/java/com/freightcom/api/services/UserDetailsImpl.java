package com.freightcom.api.services;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.freightcom.api.model.User;
import com.freightcom.api.model.UserRole;

public class UserDetailsImpl implements UserDetails
{
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private User user;

    public UserDetailsImpl(User user)
    {
        log.debug("CONSTRUCT USER DETAILS IMPL " + user);
        this.user = user;
    }

    public Long getId()
    {
        if (user != null) {
            log.debug("RETURNING ID FOR USER " + user.getId());
            return user.getId();
        } else {
            return new Long(0);
        }
    }

    public boolean hasCustomerId(Long customerId)
    {
        boolean hasCustomerId = false;

        for (UserRole role : getUser().getAuthorities()) {
            if (customerId != null && customerId.equals(role.getCustomerId())) {
                hasCustomerId = true;
                break;
            }
        }

        return hasCustomerId;
    }

    public boolean testSecurity(String id)
    {
        log.debug("TESTING SECURITY " + this.user + " AGAINST " + id);
        return user.getId()
                .equals(Long.parseLong(id));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

        for (UserRole authority : user.getAuthorities()) {
            log.debug("AUTHORITY " + authority.getRoleName());
            authorities.add(new SimpleGrantedAuthority(authority.getRoleName()));
        }

        return authorities;
    }

    @Override
    public String getPassword()
    {
        return user.getPassword();
    }

    @Override
    public String getUsername()
    {
        return user.getLogin();
    }

    @Override
    public boolean isAccountNonExpired()
    {
        return user.getEnabled();
    }

    @Override
    public boolean isAccountNonLocked()
    {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired()
    {
        return user != null && (user.getPasswordExpiry() == null || !ZonedDateTime.now()
                .isBefore(user.getPasswordExpiry()));
    }

    @Override
    public boolean isEnabled()
    {
        return user.getEnabled();
    }

    public boolean isCustomer()
    {
        return hasRole(UserRole.ROLE_CUSTOMER_ADMIN) || hasRole(UserRole.ROLE_CUSTOMER_STAFF);
    }

    public boolean isAdmin()
    {
        return hasRole("ADMIN");
    }

    public boolean hasRole(String role)
    {
        log.debug("HAS ROLE");
        for (GrantedAuthority auth : getAuthorities()) {
            log.debug("AUTH " + auth);
        }
        return getAuthorities().contains(new SimpleGrantedAuthority(role));
    }

    public User getUser()
    {
        return user;
    }

}
