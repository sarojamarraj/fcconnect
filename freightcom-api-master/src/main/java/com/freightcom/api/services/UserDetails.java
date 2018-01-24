package com.freightcom.api.services;

import com.freightcom.api.model.User;

public interface UserDetails extends org.springframework.security.core.userdetails.UserDetails
{
    Long getId();

    boolean hasCustomerId(Long customerId);

    boolean testSecurity(String id);

    boolean isCustomer();

    boolean isAdmin();

    boolean hasRole(String role);

    User getUser();
}
