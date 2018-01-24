package com.freightcom.api.services.converters;

import com.fasterxml.jackson.databind.util.StdConverter;
import com.freightcom.api.model.CreditCard;
import com.freightcom.api.model.views.CreditCardView;

public class CreditCardConverter extends StdConverter<CreditCard,CreditCardView>
{

    @Override
    public CreditCardView convert(CreditCard creditCard)
    {
        return new CreditCardView(creditCard);
    }

}
