package com.freightcom.api.services.dataobjects;

import java.util.Date;

public class PickupData
{
    private String contactName;
    private String contactPhone;
    private String contactEmail;
    private String deliveryCloseTime;
    private String deliveryInstructions;
    private String pickupCloseTime;
    private String pickupReadyTime;
    private Date pickupDate;
    private String pickupInstructions;



    public Date getPickupdate()
    {
        return pickupDate;
    }

    public void setPickupdate(Date pickupDate)
    {
        this.pickupDate = pickupDate;
    }

    public String getPickupInstructions()
    {
        return pickupInstructions;
    }

    public void setPickupInstructions(String specialInstructions)
    {
        this.pickupInstructions = specialInstructions;
    }

    public String getPickupReadyTime()
    {
        return pickupReadyTime;
    }

    public void setPickupReadyTime(String readyTime)
    {
        this.pickupReadyTime = readyTime;
    }

    public String getPickupCloseTime()
    {
        return pickupCloseTime;
    }

    public void setPickupCloseTime(String closeTime)
    {
        this.pickupCloseTime = closeTime;
    }

    public String getContactEmail()
    {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail)
    {
        this.contactEmail = contactEmail;
    }

    public String getContactPhone()
    {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone)
    {
        this.contactPhone = contactPhone;
    }

    public String getContactName()
    {
        return contactName;
    }

    public void setContactName(String contactName)
    {
        this.contactName = contactName;
    }

    public String getDeliveryCloseTime()
    {
        return deliveryCloseTime;
    }

    public void setDeliveryCloseTime(String deliveryCloseTime)
    {
        this.deliveryCloseTime = deliveryCloseTime;
    }

    public String getDeliveryInstructions()
    {
        return deliveryInstructions;
    }

    public void setDeliveryInstructions(String deliveryInstructions)
    {
        this.deliveryInstructions = deliveryInstructions;
    }

}
