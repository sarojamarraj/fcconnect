package com.freightcom.api.services.converters;

import org.springframework.core.convert.converter.Converter;

import com.freightcom.api.model.UserRole;
import com.freightcom.api.model.views.UserRoleView;
import com.freightcom.api.model.views.UserRoleViewFactory;

public class UserRoleConverter implements Converter<UserRole, UserRoleView>
{

    @Override
    public UserRoleView convert(UserRole role)
    {
        try {
            return UserRoleViewFactory.get(role);
        } catch (Exception e) {
            return null;
        }
    }

}
