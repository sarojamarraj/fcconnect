package com.freightcom.api.carrier.eshipper.schema;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;


public class ShipPackage
{
    private String type;

    private List<IndividualPackage> packages = new ArrayList<IndividualPackage>();

    public ShipPackage(String type)
    {
        this.type = type;
    }

    public IndividualPackage addPackage()
    {
        IndividualPackage item = new IndividualPackage();
        packages.add(item);

        return item;
    }

    public IndividualPackage addPackage(Integer length, Integer width, Integer height,
                             Double weight)
    {
        IndividualPackage item = new IndividualPackage(length, width, height, weight);
        packages.add(item);

        return item;
    }

    @XmlElement(name="Package")
    public List<IndividualPackage> getPackages()
    {
        return packages;
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

}
