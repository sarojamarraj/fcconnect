package com.freightcom.api.carrier.eshipper.schema;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

// EShipper xmlns="http://www.eshipper.net/xml/XMLSchema

@XmlRootElement(name = "EShipper")
public class EshipperQuoteReply
{
    private QuoteReply quoteReply;
    private ErrorReply errorReply;

    public EshipperQuoteReply()
    {

    }

    @XmlElement(name = "QuoteReply")
    public QuoteReply getQuoteReply()
    {
        return quoteReply;
    }

    public void setQuoteReply(QuoteReply quoteReply)
    {
        this.quoteReply = quoteReply;
    }

    @XmlElement(name = "ErrorReply")
    public ErrorReply getErrorReply()
    {
        return errorReply;
    }

    public void setErrorReply(ErrorReply errorReply)
    {
        this.errorReply = errorReply;
    }
}
