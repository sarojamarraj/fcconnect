package com.freightcom.api.model.views;


import org.springframework.hateoas.core.Relation;
import com.freightcom.api.model.SystemProperty;

@Relation(collectionRelation = "systemProperty")
public class SystemPropertyView
{
    private final SystemProperty systemProperty;

    public SystemPropertyView(SystemProperty systemProperty)
    {
        this.systemProperty = systemProperty;
    }

    public Long getId() {
        return systemProperty.getId();
    }
    
    public String getName() {
         return systemProperty.getName(); 
    }

    public String getData() {
         return systemProperty.getData(); 
    }

}
