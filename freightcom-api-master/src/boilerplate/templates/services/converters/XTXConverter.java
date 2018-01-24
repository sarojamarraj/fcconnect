package com.freightcom.api.services.converters;

import org.springframework.core.convert.converter.Converter;


import com.freightcom.api.model.XTX;
import com.freightcom.api.model.views.XTXView;

public class XTXConverter implements Converter<XTX, XTXView>
{

    @Override
    public XTXView convert(XTX order)
    {
        return new XTXView(order);
    }

}
