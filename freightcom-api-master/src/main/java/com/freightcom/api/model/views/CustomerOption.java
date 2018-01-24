package com.freightcom.api.model.views;

import org.springframework.hateoas.core.Relation;

@Relation(collectionRelation = "customer")
public class CustomerOption implements View
{
    private final Long id;
    private final String name;

    public CustomerOption(Long id, String name)
    {
        this.id = id;
        this.name = name;
    }

    public Long getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }
}
