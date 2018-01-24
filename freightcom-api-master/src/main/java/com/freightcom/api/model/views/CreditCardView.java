package com.freightcom.api.model.views;

import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.freightcom.api.model.CreditCard;
import com.freightcom.api.model.TransactionalEntity;


public class CreditCardView extends BaseView implements View
{
    private final CreditCard creditCard;

    public CreditCardView(CreditCard creditCard) {
        this.creditCard = creditCard;
    }

    @Override
    public TransactionalEntity object()
    {
        return creditCard;
    }

    @Override
    @JsonIgnore
    public ZonedDateTime getCreatedAt() {
        return null;
    }

    @Override
    @JsonIgnore
    public ZonedDateTime getUpdatedAt() {
        return null;
    }

    public String getNumber()
    {
        String value = creditCard.getNumber();
        StringBuilder builder = new StringBuilder();

        if (value != null) {
            int n = value.length();

            for (int i = 0; i < n - 4; i++) {
                builder.append("*");
            }

            int index = n - 4;

            if (index >= 0) {
                builder.append(value.substring(index));
            }
        }

        return builder.toString();
    }

    public Boolean getActive()
    {
        return creditCard.getActive();
    }

    public Boolean getIsDefault()
    {
        return creditCard.getIsDefault();
    }

    public String getType()
    {
        return creditCard.getType();
    }

    public String getExpiryYear()
    {
        return creditCard.getExpiryYear();
    }

    public String getExpiryMonth()
    {
        return creditCard.getExpiryMonth();
    }

    public String getName()
    {
        return creditCard.getName();
    }

}
