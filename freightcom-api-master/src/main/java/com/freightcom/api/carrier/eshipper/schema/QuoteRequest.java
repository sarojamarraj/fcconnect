package com.freightcom.api.carrier.eshipper.schema;

import java.util.Date;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;


public class QuoteRequest
{
    private String serviceId;
    private String dangerousGoodsType;
    private Boolean isSaturdayService;
    private Boolean signatureRequired;
    private Boolean holdForPickupRequired;
    private Boolean specialEquipment;
    private Boolean deliveryAppointment;
    private Boolean insideDelivery;
    private Date scheduledShipDate;
    private String insuranceType;
    private Boolean insidePickup;
    private Boolean saturdayPickupRequired;
    private Boolean singleShipment;
    private Boolean cressBorderFee;
    private Boolean excessLength;
    private Boolean limitedAccess;
    private Boolean customsInBondFreight;
    private Boolean militaryBaseDelivery;
    private Boolean exhibitionConventionSite;
    private Boolean pierCharge;
    private Boolean homelandSecurity;
    private Boolean stackable;
    private Boolean sortAndSegregateCharge;

    private Address from;
    private Address to;

    private ShipPackage packages;


    public QuoteRequest()
    {
    }

    public Address addFrom()
    {
        from = new Address();

        return from;
    }

    public Address addTo()
    {
        to = new Address();

        return to;
    }

    public ShipPackage addPackages(String type)
    {
        packages = new ShipPackage(type);

        return packages;
    }

    @XmlElement(name="From")
    public Address getFrom()
    {
        return from;
    }

    @XmlElement(name="To")
    public Address getto()
    {
        return to;
    }

    @XmlElement(name="Packages")
    public ShipPackage getPackages()
    {
        return packages;
    }


    @XmlAttribute
    public String getServiceId()
    {
        return serviceId;
    }

    public void setServiceId(String serviceId)
    {
        this.serviceId = serviceId;
    }

    @XmlAttribute
    public String getDangerousGoodsType()
    {
        return dangerousGoodsType;
    }

    public void setDangerousGoodsType(String dangerousGoodsType)
    {
        this.dangerousGoodsType = dangerousGoodsType;
    }

    @XmlAttribute
    public Boolean getIsSaturdayService()
    {
        return isSaturdayService;
    }

    public void setIsSaturdayService(Boolean isSaturdayService)
    {
        this.isSaturdayService = isSaturdayService;
    }

    @XmlAttribute
    public Boolean getSignatureRequired()
    {
        return signatureRequired;
    }

    public void setSignatureRequired(Boolean signatureRequired)
    {
        this.signatureRequired = signatureRequired;
    }

    @XmlAttribute
    public Boolean getHoldForPickupRequired()
    {
        return holdForPickupRequired;
    }

    public void setHoldForPickupRequired(Boolean holdForPickupRequired)
    {
        this.holdForPickupRequired = holdForPickupRequired;
    }

    @XmlAttribute
    public Boolean getSpecialEquipment()
    {
        return specialEquipment;
    }

    public void setSpecialEquipment(Boolean specialEquipment)
    {
        this.specialEquipment = specialEquipment;
    }

    @XmlAttribute
    public Boolean getDeliveryAppointment()
    {
        return deliveryAppointment;
    }

    public void setDeliveryAppointment(Boolean deliveryAppointment)
    {
        this.deliveryAppointment = deliveryAppointment;
    }

    @XmlAttribute
    public Boolean getInsideDelivery()
    {
        return insideDelivery;
    }

    public void setInsideDelivery(Boolean insideDelivery)
    {
        this.insideDelivery = insideDelivery;
    }

    @XmlAttribute
    public Date getScheduledShipDate()
    {
        return scheduledShipDate;
    }

    public void setScheduledShipDate(Date scheduledShipDate)
    {
        this.scheduledShipDate = scheduledShipDate;
    }

    @XmlAttribute
    public String getInsuranceType()
    {
        return insuranceType;
    }

    public void setInsuranceType(String insuranceType)
    {
        this.insuranceType = insuranceType;
    }

    @XmlAttribute
    public Boolean getInsidePickup()
    {
        return insidePickup;
    }

    public void setInsidePickup(Boolean insidePickup)
    {
        this.insidePickup = insidePickup;
    }

    @XmlAttribute
    public Boolean getSaturdayPickupRequired()
    {
        return saturdayPickupRequired;
    }

    public void setSaturdayPickupRequired(Boolean saturdayPickupRequired)
    {
        this.saturdayPickupRequired = saturdayPickupRequired;
    }

    @XmlAttribute
    public Boolean getSingleShipment()
    {
        return singleShipment;
    }

    public void setSingleShipment(Boolean singleShipment)
    {
        this.singleShipment = singleShipment;
    }

    @XmlAttribute
    public Boolean getCressBorderFee()
    {
        return cressBorderFee;
    }

    public void setCressBorderFee(Boolean cressBorderFee)
    {
        this.cressBorderFee = cressBorderFee;
    }

    @XmlAttribute
    public Boolean getExcessLength()
    {
        return excessLength;
    }

    public void setExcessLength(Boolean excessLength)
    {
        this.excessLength = excessLength;
    }

    @XmlAttribute
    public Boolean getLimitedAccess()
    {
        return limitedAccess;
    }

    public void setLimitedAccess(Boolean limitedAccess)
    {
        this.limitedAccess = limitedAccess;
    }

    @XmlAttribute
    public Boolean getCustomsInBondFreight()
    {
        return customsInBondFreight;
    }

    public void setCustomsInBondFreight(Boolean customsInBondFreight)
    {
        this.customsInBondFreight = customsInBondFreight;
    }

    @XmlAttribute
    public Boolean getMilitaryBaseDelivery()
    {
        return militaryBaseDelivery;
    }

    public void setMilitaryBaseDelivery(Boolean militaryBaseDelivery)
    {
        this.militaryBaseDelivery = militaryBaseDelivery;
    }

    @XmlAttribute
    public Boolean getExhibitionConventionSite()
    {
        return exhibitionConventionSite;
    }

    public void setExhibitionConventionSite(Boolean exhibitionConventionSite)
    {
        this.exhibitionConventionSite = exhibitionConventionSite;
    }

    @XmlAttribute
    public Boolean getPierCharge()
    {
        return pierCharge;
    }

    public void setPierCharge(Boolean pierCharge)
    {
        this.pierCharge = pierCharge;
    }

    @XmlAttribute
    public Boolean getHomelandSecurity()
    {
        return homelandSecurity;
    }

    public void setHomelandSecurity(Boolean homelandSecurity)
    {
        this.homelandSecurity = homelandSecurity;
    }

    @XmlAttribute
    public Boolean getStackable()
    {
        return stackable;
    }

    public void setStackable(Boolean stackable)
    {
        this.stackable = stackable;
    }

    @XmlAttribute
    public Boolean getSortAndSegregateCharge()
    {
        return sortAndSegregateCharge;
    }

    public void setSortAndSegregateCharge(Boolean sortAndSegregateCharge)
    {
        this.sortAndSegregateCharge = sortAndSegregateCharge;
    }
}
