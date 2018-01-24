package com.freightcom.api.model.views;

import java.util.List;
import java.util.stream.Collectors;

import com.freightcom.api.model.CustomsInvoice;
import com.freightcom.api.model.CustomsInvoiceContactInfo;
import com.freightcom.api.model.ShippingAddress;

public class CustomsInvoiceView implements View
{
    private final CustomsInvoice invoice;

    public static CustomsInvoiceView get(final CustomsInvoice invoice)
    {
        if (invoice == null) {
            return null;
        }

        return new CustomsInvoiceView(invoice);
    }

    public CustomsInvoiceView(final CustomsInvoice invoice)
    {
        this.invoice = invoice;
    }

    public Long getId()
    {
        return invoice.getId();
    }

    public Boolean getDutiable()
    {
        return invoice.getDutiable();
    }

    public String getBill()
    {
        return invoice.getBill();
    }

    public String getConsigneeAccount()
    {
        return invoice.getConsigneeAccount();
    }

    public String getSedNumber()
    {
        return invoice.getSedNumber();
    }

    public ShippingAddress getBillTo()
    {
        return invoice.getBillTo();
    }

    public CustomsInvoiceContactInfo getContactInfo()
    {
        return invoice.getContactInfo();
    }

    public List<CustomsInvoiceProductView> getProducts()
    {
        if (invoice.getProducts() == null) {
            return null;
        } else {
            return invoice.getProducts()
                    .stream()
                    .map(product -> CustomsInvoiceProductView.get(product))
                    .collect(Collectors.toList());
        }
    }

}
