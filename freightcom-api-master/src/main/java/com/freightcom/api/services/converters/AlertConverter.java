package com.freightcom.api.services.converters;

import org.springframework.core.convert.converter.Converter;


import com.freightcom.api.model.Alert;
import com.freightcom.api.model.views.AlertView;

public class AlertConverter implements Converter<Alert, AlertView>
{

    @Override
    public AlertView convert(Alert order)
    {
        return new AlertView(order);
    }

}
