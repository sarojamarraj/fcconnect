package com.freightcom.api.carrier.eshipper.schema;

import javax.xml.bind.annotation.XmlAttribute;

public class Error
{
    private String message;

    public Error()
    {

    }

    @XmlAttribute(name="Message")
    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }
}
