package com.freightcom.api.model.views;

import java.math.BigDecimal;

import com.freightcom.api.model.CustomsInvoiceProduct;


public class CustomsInvoiceProductView implements View
{
    private final CustomsInvoiceProduct product;

    public static CustomsInvoiceProductView get(final CustomsInvoiceProduct product)
    {
        if (product == null) {
            return null;
        }

        return new CustomsInvoiceProductView(product);
    }

    public CustomsInvoiceProductView(final CustomsInvoiceProduct product)
    {
        this.product = product;
    }

    public Long getId()
    {
        return product.getId();
    }

    public String getDescription()
    {
        return product.getDescription();
    }

    public String getHscode()
    {
        return product.getHscode();
    }

    public String getOrigin()
    {
        return product.getOrigin();
    }

    public Integer getQuantity()
    {
        return product.getQuantity();
    }

    public BigDecimal getUnitPrice()
    {
        return product.getUnitPrice();
    }
}

