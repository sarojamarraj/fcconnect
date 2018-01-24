package com.freightcom.api.model.views;


import org.springframework.hateoas.core.Relation;
import com.freightcom.api.model.CustomerAdmin;
import com.freightcom.api.model.UserRole;

@Relation(collectionRelation = "customerAdmin")
public class CustomerAdminView extends UserRoleView
{
    private final CustomerAdmin customerAdmin;

    @Override
    public UserRole roleObject()
    {
        return customerAdmin;
    }

    public CustomerAdminView(CustomerAdmin customerAdmin)
    {
        this.customerAdmin = customerAdmin;
    }

    public Long getCustomerId()
    {
        return customerAdmin.getCustomerId();
    }

    public String getCustomerName()
    {
        return customerAdmin.getCustomerName();
    }

}
