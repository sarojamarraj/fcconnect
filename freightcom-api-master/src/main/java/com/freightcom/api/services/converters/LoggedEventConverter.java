package com.freightcom.api.services.converters;

import org.springframework.core.convert.converter.Converter;


import com.freightcom.api.model.LoggedEvent;
import com.freightcom.api.model.views.LoggedEventView;

public class LoggedEventConverter implements Converter<LoggedEvent, LoggedEventView>
{

    @Override
    public LoggedEventView convert(LoggedEvent order)
    {
        return new LoggedEventView(order);
    }

}
