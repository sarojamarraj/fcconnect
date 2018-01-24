package com.freightcom.api.model.views;


import org.springframework.hateoas.core.Relation;
import com.freightcom.api.model.Carrier;

@Relation(collectionRelation = "carrier")
public class CarrierView
{
    private final Carrier carrier;

    public CarrierView(Carrier carrier)
    {
        this.carrier = carrier;
    }

    public Long getId() {
        return carrier.getId();
    }

    public String getName()
    {
        return carrier.getName();
    }

    public Integer getType() {
        return carrier.getType();
    }

    public String getReferenceId() {
        return carrier.getReferenceId();
    }

    public String getDba() {
        return carrier.getDba();
    }

    public Object getTerm() {
        return carrier.getTerm();
    }
}
