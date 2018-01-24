package com.freightcom.api.carrier.eshipper.schema;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "EShipper")
@XmlType(namespace="http://www.eshipper.net/XMLSchema")
public class EshipperQuoteRequest
{
    private String username;
    private String password;
    private final String version = "3.0.0";
    private QuoteRequest quoteRequest;

    public EshipperQuoteRequest(String username, String password)
    {
        this.username = username;
        this.password = password;
    }

    public EshipperQuoteRequest()
    {
    }

    @XmlAttribute
    public String getUsername()
    {
        return username;
    }

    @XmlAttribute
    public String getPassword()
    {
        return password;
    }

    public QuoteRequest addQuoteRequest()
    {
        quoteRequest = new QuoteRequest();

        return quoteRequest;
    }

    @XmlElement(name = "QuoteRequest")
    public QuoteRequest getQuoteRequest()
    {
        return quoteRequest;
    }

    @XmlAttribute
    public String getVersion()
    {
        return version;
    }
}
