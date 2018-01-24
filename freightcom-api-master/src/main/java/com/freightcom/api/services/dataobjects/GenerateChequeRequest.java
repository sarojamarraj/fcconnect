package com.freightcom.api.services.dataobjects;

import javax.xml.bind.annotation.XmlAttribute;

public class GenerateChequeRequest extends BaseAPIRequest
{
    private String date = "";
    private String name = "";
    private String numericAmount = "";
    private String textAmount = "";
    private String memo = "";
    private String number = "";

    public static GenerateChequeRequest get()
    {
        return new GenerateChequeRequest();
    }


    public GenerateChequeRequest()
    {
        super();

        setCommand("pdf.createPDF");
    }

    @XmlAttribute
    public String getDate()
    {
        return date;
    }


    public GenerateChequeRequest setDate(String date)
    {
        this.date = date;

        return this;
    }

    @XmlAttribute
    public String getName()
    {
        return name;
    }


    public GenerateChequeRequest setName(String name)
    {
        this.name = name;

        return this;
    }

    @XmlAttribute
    public String getNumericAmount()
    {
        return numericAmount;
    }


    public GenerateChequeRequest setNumericAmount(String numericAmount)
    {
        this.numericAmount = numericAmount;

        return this;
    }

    @XmlAttribute
    public String getTextAmount()
    {
        return textAmount;
    }


    public GenerateChequeRequest setTextAmount(String textAmount)
    {
        this.textAmount = textAmount;

        return this;
    }


    @XmlAttribute
    public String getMemo()
    {
        return memo;
    }


    public GenerateChequeRequest setMemo(String memo)
    {
        this.memo = memo;

        return this;
    }


    @XmlAttribute
    public String getNumber()
    {
        return number;
    }


    public GenerateChequeRequest setNumber(String number)
    {
        this.number = number;

        return this;
    }
}
