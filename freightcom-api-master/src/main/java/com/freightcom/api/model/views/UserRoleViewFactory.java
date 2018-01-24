package com.freightcom.api.model.views;


import org.springframework.hateoas.core.Relation;

import com.freightcom.api.model.Admin;
import com.freightcom.api.model.Agent;
import com.freightcom.api.model.CustomerAdmin;
import com.freightcom.api.model.CustomerStaff;
import com.freightcom.api.model.FreightcomStaff;
import com.freightcom.api.model.SuperAdmin;
import com.freightcom.api.model.UserRole;

@Relation(collectionRelation = "agent")
public class UserRoleViewFactory
{
    public static UserRoleView get(UserRole role) throws Exception
    {
        if (role instanceof Agent) {
            return new AgentView((Agent) role);
        } else if (role instanceof Admin) {
            return new AdminView((Admin) role);
        } else if (role instanceof FreightcomStaff) {
            return new FreightcomStaffView((FreightcomStaff) role);
        } else if (role instanceof CustomerStaff) {
            return new CustomerStaffView((CustomerStaff) role);
        } else if (role instanceof CustomerAdmin) {
            return new CustomerAdminView((CustomerAdmin) role);
        } else if (role instanceof SuperAdmin) {
            return new SuperAdminView((SuperAdmin) role);
        }

        throw new Exception("Problem with role factory");
    }
}
