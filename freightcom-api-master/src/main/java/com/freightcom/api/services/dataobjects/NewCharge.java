package com.freightcom.api.services.dataobjects;

import java.math.BigDecimal;

public class NewCharge
{
    private BigDecimal charge;
    private BigDecimal cost;
    private Long accessorialId;
    private String description;

    public BigDecimal getCharge()
    {
        return charge;
    }
    public void setCharge(BigDecimal charge)
    {
        this.charge = charge;
    }
    public BigDecimal getCost()
    {
        return cost;
    }
    public void setCost(BigDecimal cost)
    {
        this.cost = cost;
    }
    public Long getAccessorialId()
    {
        return accessorialId;
    }
    public void setAccessorialId(Long accessorialId)
    {
        this.accessorialId = accessorialId;
    }
    public String getDescription()
    {
        return description;
    }
    public void setDescription(String description)
    {
        this.description = description;
    }
}
