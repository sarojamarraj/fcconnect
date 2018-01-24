package com.freightcom.api.services.converters;

import org.springframework.core.convert.converter.Converter;

import com.freightcom.api.model.User;
import com.freightcom.api.model.UserRole;
import com.freightcom.api.model.views.UserView;

public class UserConverter implements Converter<User, UserView>
{
    private final UserRole role;

    public UserConverter(UserRole role) throws Exception
    {
        this.role = role;

        if (role == null) {
            throw new Exception("null role to user converter");
        }
    }

    @Override
    public UserView convert(User user)
    {
        return new UserView(user, role);
    }

}
