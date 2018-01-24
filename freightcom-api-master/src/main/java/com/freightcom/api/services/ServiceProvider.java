package com.freightcom.api.services;

import com.freightcom.api.ApiSession;
import com.freightcom.api.model.UserRole;
import com.freightcom.api.repositories.ObjectBase;

public interface ServiceProvider
{
    void publishEvent(Object event);
    ApiSession getApiSession();
    ObjectBase getObjectBase();
    UserRole getRole();
}
