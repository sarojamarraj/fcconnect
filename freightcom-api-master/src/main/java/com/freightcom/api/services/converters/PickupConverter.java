package com.freightcom.api.services.converters;

import com.fasterxml.jackson.databind.util.StdConverter;
import com.freightcom.api.model.Pickup;
import com.freightcom.api.model.views.PickupView;

public class PickupConverter extends StdConverter<Pickup,PickupView>
{

    @Override
    public PickupView convert(Pickup pickup)
    {
        return new PickupView(pickup);
    }

}
