package com.freightcom.api.carrier.eshipper.schema;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAttribute;


public class IndividualPackage
{
    private Integer length;
    private Integer width;
    private Integer height;
    private Double weight;
    private Double weightOz;
    private String type;
    private String freightClass;
    private String nmfcCode;
    private BigDecimal insuranceAmount;
    private BigDecimal codAmount;
    private String description;

    public IndividualPackage()
    {

    }

    public IndividualPackage(Integer length, Integer width, Integer height,
                             Double weight)
    {
        this.length = length;
        this.width = width;
        this.height = height;
        this.weight = weight;
    }

    @XmlAttribute
    public Integer getLength()
    {
        return length;
    }

    public void setLength(Integer length)
    {
        this.length = length;
    }

    @XmlAttribute
    public Integer getWidth()
    {
        return width;
    }

    public void setWidth(Integer width)
    {
        this.width = width;
    }

    @XmlAttribute
    public Integer getHeight()
    {
        return height;
    }

    public void setHeight(Integer height)
    {
        this.height = height;
    }

    @XmlAttribute
    public Double getWeight()
    {
        return weight;
    }

    public void setWeight(Double weight)
    {
        this.weight = weight;
    }

    @XmlAttribute
    public Double getWeightOz()
    {
        return weightOz;
    }

    public void setWeightOz(Double weightOz)
    {
        this.weightOz = weightOz;
    }

    @XmlAttribute
    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    @XmlAttribute
    public String getFreightClass()
    {
        return freightClass;
    }

    public void setFreightClass(String freightClass)
    {
        this.freightClass = freightClass;
    }

    @XmlAttribute
    public String getNmfcCode()
    {
        return nmfcCode;
    }

    public void setNmfcCode(String nmfcCode)
    {
        this.nmfcCode = nmfcCode;
    }

    @XmlAttribute
    public BigDecimal getInsuranceAmount()
    {
        return insuranceAmount;
    }

    public void setInsuranceAmount(BigDecimal insuranceAmount)
    {
        this.insuranceAmount = insuranceAmount;
    }

    @XmlAttribute
    public BigDecimal getCodAmount()
    {
        return codAmount;
    }

    public void setCodAmount(BigDecimal codAmount)
    {
        this.codAmount = codAmount;
    }

    @XmlAttribute
    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }
}
