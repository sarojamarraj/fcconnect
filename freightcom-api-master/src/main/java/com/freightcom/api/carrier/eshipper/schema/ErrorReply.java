package com.freightcom.api.carrier.eshipper.schema;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;


public class ErrorReply
{
    private List<Error> errors = new ArrayList<Error>();

    public ErrorReply()
    {

    }

    @XmlElement(name="Error")
    public List<Error> getErrors()
    {
        return errors;
    }

    public void setErrors(List<Error> errors)
    {
        this.errors = errors;
    }
}
