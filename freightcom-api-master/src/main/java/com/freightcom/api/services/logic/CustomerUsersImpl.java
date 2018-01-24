package com.freightcom.api.services.logic;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.freightcom.api.model.Customer;
import com.freightcom.api.model.CustomerAdmin;
import com.freightcom.api.model.CustomerStaff;
import com.freightcom.api.model.User;
import com.freightcom.api.model.UserRole;
import com.freightcom.api.repositories.ObjectBase;

@Component
public class CustomerUsersImpl implements CustomerUsers
{
    @Autowired
    private  ObjectBase objectBase;

    public CustomerUsersImpl() {
    }

    @Override
    public List<CustomerStaff> getStaff(Customer customer)
    {
        return objectBase.getStaff(customer);
    }

    @Override
    public List<CustomerAdmin> getAdmins(Customer customer)
    {
       return customer.getAdmins();
    }

    public List<User> notifyUsers(Customer customer)
    {
        List<User> notify = new ArrayList<User>();

        boolean found = false;

        for (UserRole userRole: getAdmins(customer)) {
            if (userRole.getUser().getEmail() != null) {
                found = true;
                notify.add(userRole.getUser());
                break;
            }
        }

        if (! found) {
            for (UserRole userRole: getStaff(customer)) {
                if (userRole.getUser().getEmail() != null) {
                    found = true;
                    notify.add(userRole.getUser());
                    break;
                }
            }
        }

        return notify;
    }

    public List<User> getInvoiceEmails(Customer customer)
    {
        List<User> result = new ArrayList<User>();

        String email = customer.getInvoiceEmail();

        if (email != null) {
            for (String emailAddress: email.split("\\s*,\\s*")) {
                User user = new User();
                user.setEmail(emailAddress);
                user.setLastname(customer.getName());

                result.add(user);
            }
        }

        return result;
    }


}
