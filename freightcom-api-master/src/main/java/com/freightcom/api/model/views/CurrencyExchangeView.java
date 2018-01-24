package com.freightcom.api.model.views;

import java.time.ZonedDateTime;

import org.springframework.hateoas.core.Relation;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.freightcom.api.model.CurrencyExchange;

@Relation(collectionRelation = "currencyExchange")
public class CurrencyExchangeView implements View
{
    private final CurrencyExchange currencyExchange;
    private final boolean isAdmin;

    public CurrencyExchangeView(CurrencyExchange currencyExchange)
    {
        this.currencyExchange = currencyExchange;
        this.isAdmin = false;
    }

    public CurrencyExchangeView(CurrencyExchange currencyExchange, final boolean isAdmin)
    {
        this.isAdmin = isAdmin;
        this.currencyExchange = currencyExchange;
    }

    public Long getId()
    {
        return currencyExchange.getId();
    }

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm")
    public ZonedDateTime getUpdatedAt()
    {
        return currencyExchange.getUpdatedAt();
    }

    public Double getRate()
    {
        return currencyExchange.getRate();
    }

    public String getTo()
    {
        return currencyExchange.getTo();
    }

    public String getFrom()
    {
        return currencyExchange.getFrom();
    }

    public String getUpdatedBy()
    {
        if (isAdmin) {
            return currencyExchange.getUpdatedBy();
        } else {
            return null;
        }
    }
}
