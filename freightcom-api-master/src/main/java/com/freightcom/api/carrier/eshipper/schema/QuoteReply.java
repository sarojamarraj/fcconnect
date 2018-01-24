package com.freightcom.api.carrier.eshipper.schema;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;


public class QuoteReply
{
    private List<Quote> quotes = new ArrayList<Quote>();

    public QuoteReply()
    {

    }

    @XmlElement(name="Quote")
    public List<Quote> getQuotes()
    {
        return quotes;
    }

    public void setQuotes(List<Quote> quotes)
    {
        this.quotes = quotes;
    }
}
