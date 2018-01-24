package com.freightcom.api.services.orders;

import java.util.ArrayList;
import java.util.List;

import com.freightcom.api.model.OrderRateQuote;

public class RateRequestResult
{
    private  List<OrderRateQuote> rates;
    private List<Object> messages = new ArrayList<Object>();

    public RateRequestResult( List<OrderRateQuote> rates)
    {
        this.rates = rates;
    }

    public RateRequestResult()
    {
        this.rates = new ArrayList<OrderRateQuote>();
    }

    public List<OrderRateQuote> getRates()
    {
        return rates;
    }

    public RateRequestResult addRate(OrderRateQuote quote)
    {
        rates.add(quote);

        return this;
    }

    public RateRequestResult addRates(List<OrderRateQuote> quotes)
    {
        rates.addAll(quotes);

        return this;
    }


    public List<Object> getMessages()
    {
        return messages;
    }

    public RateRequestResult addMessage(Object message)
    {
        messages.add(message);

        return this;
    }

    public RateRequestResult  addMessages(List<Object> messages)
    {
        this.messages.addAll(messages);

        return this;
    }

    public RateRequestResult addQuotes(RateRequestResult from)
    {
        addRates(from.getRates());
        addMessages(from.getMessages());
        
        return this;        
    }
    
    public String toString() 
    {
        return "QR " + rates.size() + " M " + messages.size();
    }
}
