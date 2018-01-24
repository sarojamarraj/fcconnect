package com.freightcom.api.model.views;


import org.springframework.hateoas.core.Relation;
import com.freightcom.api.model.Admin;
import com.freightcom.api.model.UserRole;

@Relation(collectionRelation = "admin")
public class AdminView extends UserRoleView
{
    private final Admin admin;

    @Override
    public UserRole roleObject()
    {
        return admin;
    }

    public AdminView(Admin admin)
    {
        this.admin = admin;
    }

}
