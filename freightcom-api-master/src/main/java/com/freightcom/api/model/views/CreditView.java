package com.freightcom.api.model.views;


import java.math.BigDecimal;
import java.time.ZonedDateTime;

import org.springframework.hateoas.core.Relation;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.freightcom.api.model.Credit;

@Relation(collectionRelation = "credit")
public class CreditView
{
    private final Credit credit;

    public CreditView(Credit credit)
    {
        this.credit = credit;
    }

    public Long getCreatedByUserId() {
        return credit.getCreatedByUserId();
    }


    public Long getCustomerId() {
        return credit.getCustomerId();
    }


    public String getCurrency() {
        return credit.getCurrency();
    }

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm")
    public ZonedDateTime getCreatedAt() {
        return credit.getCreatedAt();
    }


    @JsonFormat(pattern = "yyyy-MM-dd hh:mm")
    public ZonedDateTime getUpdatedAt() {
        return credit.getUpdatedAt();
    }


    public BigDecimal getAmount() {
        return credit.getAmount();
    }

    public BigDecimal getAmountRemaining() {
        return credit.getAmountRemaining();
    }


    public String getNote() {
        return credit.getNote();
    }


    public Long getId() {
        return credit.getId();
    }

}
