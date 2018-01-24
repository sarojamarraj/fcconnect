package com.freightcom.api.model.views;

import java.time.ZonedDateTime;

import org.springframework.hateoas.core.Relation;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.freightcom.api.model.TransactionalEntity;

@Relation(collectionRelation = "charge")
public abstract class BaseView implements View
{
    public abstract TransactionalEntity object();

    public Long getId()
    {
        return object().getId();
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    public ZonedDateTime getCreatedAt() {
        return object().getCreatedAt();
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    public ZonedDateTime getUpdatedAt() {
        return object().getUpdatedAt();
    }
}
