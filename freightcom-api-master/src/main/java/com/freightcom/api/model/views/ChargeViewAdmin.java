package com.freightcom.api.model.views;

import java.math.BigDecimal;

import org.springframework.hateoas.core.Relation;

import com.freightcom.api.model.Charge;

@Relation(collectionRelation = "charge")
public class ChargeViewAdmin extends ChargeView
{
    protected ChargeViewAdmin(Charge charge)
    {
        super(charge);
    }

    public BigDecimal getCost()
    {
        return charge.getCost();
    }

    public BigDecimal getCommission()
    {
        return charge.getCommission();
    }

    public BigDecimal getMarkup()
    {
        return charge.getMarkup();
    }

    public Boolean getApplyCommission()
    {
        return charge.getApplyCommission();
    }

}
