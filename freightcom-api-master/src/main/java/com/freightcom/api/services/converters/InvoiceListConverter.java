package com.freightcom.api.services.converters;

import org.springframework.core.convert.converter.Converter;


import com.freightcom.api.model.Invoice;
import com.freightcom.api.model.views.InvoiceListView;

public class InvoiceListConverter implements Converter<Invoice, InvoiceListView>
{

    @Override
    public InvoiceListView convert(Invoice order)
    {
        return new InvoiceListView(order);
    }

}
