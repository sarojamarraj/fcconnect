package com.freightcom.api.services.converters;

import org.springframework.core.convert.converter.Converter;


import com.freightcom.api.model.SystemProperty;
import com.freightcom.api.model.views.SystemPropertyView;

public class SystemPropertyConverter implements Converter<SystemProperty, SystemPropertyView>
{

    @Override
    public SystemPropertyView convert(SystemProperty order)
    {
        return new SystemPropertyView(order);
    }

}
