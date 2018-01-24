package com.freightcom.api.services.converters;

import org.springframework.core.convert.converter.Converter;


import com.freightcom.api.model.Invoice;
import com.freightcom.api.model.views.InvoiceView;

public class InvoiceConverter implements Converter<Invoice, InvoiceView>
{

    @Override
    public InvoiceView convert(Invoice order)
    {
        return new InvoiceView(order);
    }

}
