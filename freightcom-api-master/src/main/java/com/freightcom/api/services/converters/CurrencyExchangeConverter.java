package com.freightcom.api.services.converters;

import org.springframework.core.convert.converter.Converter;

import com.freightcom.api.model.CurrencyExchange;
import com.freightcom.api.model.views.CurrencyExchangeView;

public class CurrencyExchangeConverter implements Converter<CurrencyExchange, CurrencyExchangeView>
{

    private boolean isAdmin = false;

    public CurrencyExchangeConverter()
    {

    }

    public CurrencyExchangeConverter(boolean isAdmin)
    {
        this.isAdmin = isAdmin;
    }

    @Override
    public CurrencyExchangeView convert(CurrencyExchange order)
    {
        if (isAdmin) {
            return new CurrencyExchangeView(order, true);
        } else {
            return new CurrencyExchangeView(order);
        }
    }

}
