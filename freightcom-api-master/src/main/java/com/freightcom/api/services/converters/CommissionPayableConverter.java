package com.freightcom.api.services.converters;

import org.springframework.core.convert.converter.Converter;

import com.freightcom.api.model.CommissionPayable;
import com.freightcom.api.model.views.CommissionPayableListView;

public class CommissionPayableConverter implements Converter<CommissionPayable, CommissionPayableListView>
{

    @Override
    public CommissionPayableListView convert(CommissionPayable commission)
    {
        return new CommissionPayableListView(commission);
    }

}
