package com.freightcom.api.services.converters;

import org.springframework.core.convert.converter.Converter;


import com.freightcom.api.model.Markup;
import com.freightcom.api.model.views.MarkupView;

public class MarkupConverter implements Converter<Markup, MarkupView>
{

    @Override
    public MarkupView convert(Markup order)
    {
        return new MarkupView(order);
    }

}
