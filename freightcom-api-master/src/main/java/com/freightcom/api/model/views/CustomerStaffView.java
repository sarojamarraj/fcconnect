package com.freightcom.api.model.views;


import org.springframework.hateoas.core.Relation;
import com.freightcom.api.model.CustomerStaff;
import com.freightcom.api.model.UserRole;

@Relation(collectionRelation = "customerStaff")
public class CustomerStaffView extends UserRoleView
{
    private final CustomerStaff customerStaff;

    @Override
    public UserRole roleObject()
    {
        return customerStaff;
    }

    public CustomerStaffView(CustomerStaff customerStaff)
    {
        this.customerStaff = customerStaff;
    }

    public Long getCustomerId()
    {
        return customerStaff.getCustomerId();
    }

    public String getCustomerName()
    {
        return customerStaff.getCustomerName();
    }

}
