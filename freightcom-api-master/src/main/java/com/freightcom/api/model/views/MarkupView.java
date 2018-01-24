package com.freightcom.api.model.views;


import java.math.BigDecimal;
import java.time.ZonedDateTime;

import org.springframework.hateoas.core.Relation;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.freightcom.api.model.Markup;

@Relation(collectionRelation = "markup")
public class MarkupView
{
    private final Markup markup;

    public MarkupView(Markup markup)
    {
        this.markup = markup;
    }

    public Long getCustomerId()
    {
        return markup.getCustomerId();
    }

    public Long getPackageTypeId()
    {
        return markup.getPackageTypeId();
    }

    public BigDecimal getMaxAmount()
    {
        return markup.getMaxAmount();
    }

    public Long getServiceId()
    {
        return markup.getServiceId();
    }

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm")
    public ZonedDateTime getUpdatedAt()
    {
        return markup.getUpdatedAt();
    }

    public String getCalcFrom()
    {
        return markup.getCalcFrom();
    }

    public Double getFraction()
    {
        return markup.getFraction();
    }

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm")
    public ZonedDateTime getStartAt()
    {
        return markup.getStartAt();
    }

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm")
    public ZonedDateTime getCreatedAt()
    {
        return markup.getCreatedAt();
    }

    public BigDecimal getMinAmount()
    {
        return markup.getMinAmount();
    }

    public Long getAccessorialTypeId()
    {
        return markup.getAccessorialTypeId();
    }

    public Long getAgentId()
    {
        return markup.getAgentId();
    }

    public BigDecimal getFixedAmount()
    {
        return markup.getFixedAmount();
    }

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm")
    public ZonedDateTime getEndAt()
    {
        return markup.getEndAt();
    }

    public Long getId()
    {
        return markup.getId();
    }
}
