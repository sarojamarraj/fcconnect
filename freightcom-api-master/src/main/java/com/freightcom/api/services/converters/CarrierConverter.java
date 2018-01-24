package com.freightcom.api.services.converters;

import org.springframework.core.convert.converter.Converter;


import com.freightcom.api.model.Carrier;
import com.freightcom.api.model.views.CarrierView;

public class CarrierConverter implements Converter<Carrier, CarrierView>
{

    @Override
    public CarrierView convert(Carrier order)
    {
        return new CarrierView(order);
    }

}
