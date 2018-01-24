package com.freightcom.api.carrier.eshipper.schema;

import java.math.BigDecimal;
import java.util.Date;

import javax.xml.bind.annotation.XmlAttribute;

public class Quote
{
    private String carrierId;
    private String carrierName;
    private String serviceId;
    private String serviceName;
    private String modeTransport;
    private Integer transitDays;
    private Date deliveryDate;
    private String currency;
    private BigDecimal baseCharge;
    private BigDecimal totalTariff;
    private BigDecimal baseChargeTariff;
    private BigDecimal fuelSurchargeTariff;
    private BigDecimal fuelSurcharge;
    private BigDecimal totalCharge;

    public Quote()
    {

    }

    @XmlAttribute
    public String getCarrierId()
    {
        return carrierId;
    }

    public void setCarrierId(String carrierId)
    {
        this.carrierId = carrierId;
    }

    @XmlAttribute
    public String getCarrierName()
    {
        return carrierName;
    }

    public void setCarrierName(String carrierName)
    {
        this.carrierName = carrierName;
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
    public String getServiceName()
    {
        return serviceName;
    }

    public void setServiceName(String serviceName)
    {
        this.serviceName = serviceName;
    }

    @XmlAttribute
    public String getModeTransport()
    {
        return modeTransport;
    }

    public void setModeTransport(String modeTransport)
    {
        this.modeTransport = modeTransport;
    }

    @XmlAttribute
    public Integer getTransitDays()
    {
        return transitDays;
    }

    public void setTransitDays(Integer transitDays)
    {
        this.transitDays = transitDays;
    }

    @XmlAttribute
    public Date getDeliveryDate()
    {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate)
    {
        this.deliveryDate = deliveryDate;
    }

    @XmlAttribute
    public String getCurrency()
    {
        return currency;
    }

    public void setCurrency(String currency)
    {
        this.currency = currency;
    }

    @XmlAttribute
    public BigDecimal getBaseCharge()
    {
        return baseCharge;
    }

    public void setBaseCharge(BigDecimal baseCharge)
    {
        this.baseCharge = baseCharge;
    }

    @XmlAttribute
    public BigDecimal getTotalTariff()
    {
        return totalTariff;
    }

    public void setTotalTariff(BigDecimal totalTariff)
    {
        this.totalTariff = totalTariff;
    }

    @XmlAttribute
    public BigDecimal getBaseChargeTariff()
    {
        return baseChargeTariff;
    }

    public void setBaseChargeTariff(BigDecimal baseChargeTariff)
    {
        this.baseChargeTariff = baseChargeTariff;
    }

    @XmlAttribute
    public BigDecimal getFuelSurchargeTariff()
    {
        return fuelSurchargeTariff;
    }

    public void setFuelSurchargeTariff(BigDecimal fuelSurchargeTariff)
    {
        this.fuelSurchargeTariff = fuelSurchargeTariff;
    }

    @XmlAttribute
    public BigDecimal getFuelSurcharge()
    {
        return fuelSurcharge;
    }

    public void setFuelSurcharge(BigDecimal fuelSurcharge)
    {
        this.fuelSurcharge = fuelSurcharge;
    }

    @XmlAttribute
    public BigDecimal getTotalCharge()
    {
        return totalCharge;
    }

    public void setTotalCharge(BigDecimal totalCharge)
    {
        this.totalCharge = totalCharge;
    }
}
