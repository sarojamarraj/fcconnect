package com.freightcom.api.services;

import java.util.Map;

import org.springframework.beans.ConfigurablePropertyAccessor;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import com.freightcom.api.ApiSession;
import com.freightcom.api.model.UserRole;
import com.freightcom.api.repositories.ObjectBase;

@Component
public class ServicesCommon implements ServiceProvider
{
    @Autowired
    private ApplicationEventPublisher publisher;

    @Autowired
    protected  ObjectBase objectBase;

    protected final ApiSession apiSession;

    public ServicesCommon(final ApiSession apiSession)
    {
        this.apiSession = apiSession;
    }

    protected void copyFields(Object from, Object to, Map<String, String> fields) throws Exception
    {
        ConfigurablePropertyAccessor dest = PropertyAccessorFactory.forDirectFieldAccess(to);
        ConfigurablePropertyAccessor source = PropertyAccessorFactory.forDirectFieldAccess(from);

        for (String key : fields.keySet()) {
            dest.setPropertyValue(key, source.getPropertyValue(key));
        }
    }

    @Override
    public void publishEvent(Object event)
    {
        publisher.publishEvent(event);
    }

    @Override
    public  UserRole getRole()
    {
        return apiSession.getRole();
    }

    @Override
    public  ApiSession getApiSession()
    {
        return apiSession;
    }

    @Override
    public  ObjectBase getObjectBase()
    {
        return objectBase;
    }

    protected boolean isEmpty(String value)
    {
        return value == null || value.matches("^\\s*$");
    }
}
