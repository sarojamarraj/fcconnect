package com.freightcom.api.model.views;

import org.springframework.hateoas.core.Relation;

import com.freightcom.api.model.Charge;

@Relation(collectionRelation = "charge")
public class ChargeViewCustomer extends ChargeView
{
    public ChargeViewCustomer(Charge charge)
    {
        super(charge);
    }
}
