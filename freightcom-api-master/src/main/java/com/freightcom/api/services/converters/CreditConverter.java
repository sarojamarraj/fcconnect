package com.freightcom.api.services.converters;

import org.springframework.core.convert.converter.Converter;


import com.freightcom.api.model.Credit;
import com.freightcom.api.model.views.CreditView;

public class CreditConverter implements Converter<Credit, CreditView>
{

    @Override
    public CreditView convert(Credit order)
    {
        return new CreditView(order);
    }

}
