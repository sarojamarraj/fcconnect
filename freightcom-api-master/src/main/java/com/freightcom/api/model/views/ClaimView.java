package com.freightcom.api.model.views;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.freightcom.api.model.Claim;

public class ClaimView implements View
{
    private final Claim claim;

    public static ClaimView get(final Claim claim)
    {
        if (claim == null) {
            return null;
        }

        return new ClaimView(claim);
    }

    public ClaimView(final Claim claim)
    {
        this.claim = claim;
    }

    public Long getId()
    {
        return claim.getId();
    }

    public String getStatus()
    {
        return claim.getStatus().toString();
    }

    public String getReason()
    {
        return claim.getReason();
    }

    public String getCurrency()
    {
        return claim.getCurrency();
    }

    public Integer getAgeInDays()
    {
        return claim.getAgeInDays();
    }

    public String getAmount()
    {
        return claim.getAmount().setScale(2, BigDecimal.ROUND_HALF_EVEN).toString();
    }

    public boolean getIsResolved()
    {
        return claim.isResolved();
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    public ZonedDateTime getDateCreated()
    {
        return claim.getCreatedAt();
    }

}
