package com.freightcom.api.model.views;

import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.freightcom.api.model.Pickup;


public class PickupView implements View
{
    private final Pickup pickup;

    public PickupView(Pickup pickup) {
        this.pickup = pickup;
    }

    public String getContactName()
    {
        return pickup.getContactName();
    }

    public String getContactPhone()
    {
        return pickup.getContactPhone();
    }

    public String getContactEmail()
    {
        return pickup.getContactEmail();
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    public ZonedDateTime getPickupDate()
    {
        return pickup.getPickupDate();
    }

    public String getPickupReadyTime()
    {
        return pickup.getPickupReadyTime();
    }

    public String getPickupCloseTime()
    {
        return pickup.getPickupCloseTime();
    }

    public String getDeliveryCloseTime()
    {
        return pickup.getDeliveryCloseTime();
    }

    public String getPickupInstructions()
    {
        return pickup.getPickupInstructions();
    }

    public String getDeliveryInstructions()
    {
        return pickup.getDeliveryInstructions();
    }

    public String getCarrierConfirmation()
    {
        return pickup.getCarrierConfirmation();
    }

    public Long getId()
    {
        return pickup.getId();
    }
}
