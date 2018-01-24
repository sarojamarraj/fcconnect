package com.freightcom.api.services;

import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.freightcom.api.events.RoleDeletedEvent;
import com.freightcom.api.model.Customer;
import com.freightcom.api.model.UserRole;
import com.freightcom.api.model.views.View;

/**
 * @author bryan
 *
 */
@Component
public interface CustomerService
{

    PagedResources<Resource<View>> getCustomers(Map<String, Object> criteria, Pageable pageable);

    PagedResources<Resource<View>> getCustomersConverted(Map<String, Object> criteria, Pageable pageable);

    Customer createOrUpdateCustomer(Customer customer) throws Exception;

    void validateCustomerRequest(Map<String, Object> attributes) throws ValidationException;

    Customer createCustomerOnly(Customer customerData, Map<String, Object> attributes) throws Exception;

    Customer getCustomer(Long customerId);

    void alertRoleDeleted(AlertService alertService, RoleDeletedEvent event);

    String deleteCustomer(Long customerId, UserDetailsImpl loggedInUser);

    Customer updateCustomer(Long id, Customer customer, Map<String, String> attributes, UserDetails loggedInUser,
            UserRole userRole) throws Exception;

    Customer createCustomer(Customer customerData, Map<String, Object> attributes, boolean validate) throws Exception;

    Customer createCustomer(Customer customerData, Map<String, Object> attributes) throws Exception;

    void validateCustomerOnly(Map<String, Object> attributes) throws ValidationException;

    void inviteCustomer(Long customerId, String emailAddress) throws ValidationException;

    PagedResources<Resource<View>> getByName(Map<String, Object> criteria, Pageable pageable);

}
