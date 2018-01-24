package com.freightcom.api.services.invoices;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.freightcom.api.model.CreditCard;
import com.freightcom.api.model.Customer;
import com.freightcom.api.services.converters.CreditCardConverter;

/**
 * @author bryan
 *
 */

public class ChargesCustomerView
{
    private final Customer customer;

    public ChargesCustomerView(final Customer customer)
    {
        this.customer = customer;
    }

    @JsonSerialize(contentConverter = CreditCardConverter.class)
    public List<CreditCard> getCreditCards()
    {
        return customer.getCreditCards();
    }

    public Long getId()
    {
        return customer.getId();
    }

    public String getName()
    {
        return customer.getName();
    }
}
