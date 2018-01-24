package com.freightcom.api.model.views;


import org.springframework.hateoas.core.Relation;
import com.freightcom.api.model.SuperAdmin;
import com.freightcom.api.model.UserRole;

@Relation(collectionRelation = "superAdmin")
public class SuperAdminView extends UserRoleView
{
    private final SuperAdmin superAdmin;

    @Override
    public UserRole roleObject()
    {
        return superAdmin;
    }

    public SuperAdminView(SuperAdmin superAdmin)
    {
        this.superAdmin = superAdmin;
    }

}
