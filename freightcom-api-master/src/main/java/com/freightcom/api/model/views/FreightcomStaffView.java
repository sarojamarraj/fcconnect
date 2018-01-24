package com.freightcom.api.model.views;


import org.springframework.hateoas.core.Relation;
import com.freightcom.api.model.FreightcomStaff;
import com.freightcom.api.model.UserRole;

@Relation(collectionRelation = "freightcomStaff")
public class FreightcomStaffView extends UserRoleView
{
    private final FreightcomStaff freightcomStaff;

    @Override
    public UserRole roleObject()
    {
        return freightcomStaff;
    }

    public FreightcomStaffView(FreightcomStaff freightcomStaff)
    {
        this.freightcomStaff = freightcomStaff;
    }

    public Boolean getCanManageDisputes()
    {
        return freightcomStaff.getCanManageDisputes();
    }

    public Boolean getCanManageClaims()
    {
        return freightcomStaff.getCanManageClaims();
    }

    public Boolean getCanEnterPayments()
    {
        return freightcomStaff.getCanEnterPayments();
    }

    public Boolean getCanGenerateInvoices()
    {
        return freightcomStaff.getCanGenerateInvoices();
    }

}
