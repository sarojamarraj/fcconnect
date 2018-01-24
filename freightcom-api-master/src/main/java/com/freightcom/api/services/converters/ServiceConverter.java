package com.freightcom.api.services.converters;

import org.springframework.core.convert.converter.Converter;


import com.freightcom.api.model.Service;
import com.freightcom.api.model.views.ServiceView;

public class ServiceConverter implements Converter<Service, ServiceView>
{

    @Override
    public ServiceView convert(Service order)
    {
        return new ServiceView(order);
    }

}
