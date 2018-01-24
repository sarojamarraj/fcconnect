package com.freightcom.api.model.views;


import org.springframework.hateoas.core.Relation;

import com.freightcom.api.model.TransactionalEntity;
import com.freightcom.api.model.UserRole;

@Relation(collectionRelation = "userRole")
public abstract class UserRoleView extends BaseView
{
    protected abstract UserRole roleObject();

    public UserRoleView()
    {
    }

    @Override
    public TransactionalEntity object()
    {
        return roleObject();
    }

    public String getRoleName() {
        return roleObject().getRoleName();
    }

    public boolean getCanManageCredits()
    {
        return roleObject().canManageCredits();
    }
}
