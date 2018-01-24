package com.freightcom.api.services.converters;

import org.springframework.core.convert.converter.Converter;

import com.freightcom.api.model.Payable;
import com.freightcom.api.model.views.PayableListView;

public class PayablesConverter implements Converter<Payable, PayableListView>
{

    @Override
    public PayableListView convert(Payable order)
    {
        return new PayableListView(order);
    }

}
