package com.freightcom.api.model.views;


import org.springframework.hateoas.core.Relation;
import com.freightcom.api.model.Service;

@Relation(collectionRelation = "carrier")
public class ServiceView implements View
{
    private final Service service;

    public ServiceView(Service service)
    {
        this.service = service;
    }

    public Long getId() {
        return service.getId();
    }

    public String getName()
    {
        return service.getName();
    }

    public Object getTerm()
    {
        return service.getTerm();
    }

    public String getCarrierName()
    {
        if (service.getCarrier() != null) {
            return service.getCarrier().getName();
        } else {
            return null;
        }
    }

    public String getImplementingClass()
    {
        if (service.getCarrier() != null) {
            return service.getCarrier().getImplementingClass();
        } else {
            return null;
        }
    }

    public String getProvider()
    {
        return service.getProvider();
    }
}
