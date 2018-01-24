package com.freightcom.api.services.logic;

import java.util.List;

import com.freightcom.api.model.Customer;
import com.freightcom.api.model.CustomerAdmin;
import com.freightcom.api.model.CustomerStaff;
import com.freightcom.api.model.User;

public interface CustomerUsers
{
    List<CustomerStaff> getStaff(Customer customer);
    List<CustomerAdmin> getAdmins(Customer customer);
    List<User> notifyUsers(Customer customer);
    List<User> getInvoiceEmails(Customer customer);
}
