package com.freightcom.api.model.views;

import java.math.BigDecimal;

import com.freightcom.api.model.AccessorialServices;

public class AccessorialServicesView
{
    private final AccessorialServices service;

    public AccessorialServicesView(final AccessorialServices service)
    {
        this.service = service;
    }
    
    public String getType() {
        return service.getType();
    }
    
    public String getName() {
        return service.getName();
    }
    
    public String getDescription() {
        return service.getDescription();
    }
    
    public Long getId() {
        return service.getId();
    }
    
    public BigDecimal getRate() {
        return service.getRate();
    }

}
