package com.freightcom.api.model.views;


import org.springframework.hateoas.core.Relation;
import com.freightcom.api.model.Alert;

@Relation(collectionRelation = "alert")
public class AlertView
{
    private final Alert alert;

    public AlertView(Alert alert)
    {
        this.alert = alert;
    }

    public Long getId() {
        return alert.getId();
    }
}
