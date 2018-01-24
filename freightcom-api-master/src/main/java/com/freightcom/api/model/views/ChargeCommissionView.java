package com.freightcom.api.model.views;

import org.springframework.hateoas.core.Relation;

import com.freightcom.api.model.ChargeCommission;
import com.freightcom.api.model.TransactionalEntity;
import com.freightcom.api.util.MapBuilder;

@Relation(collectionRelation = "chargeCommission")
public class ChargeCommissionView extends CommissionCommon
{
    private final ChargeCommission chargeCommission;

    public ChargeCommissionView(ChargeCommission chargeCommission)
    {
        this.chargeCommission = chargeCommission;
    }

    public TransactionalEntity object()
    {
        return chargeCommission;
    }

    public Object getAgent()
    {
        if (chargeCommission.getAgent() == null) {
            return null;
        } else {
            return MapBuilder.getNew()
                    .put("id", chargeCommission.getAgent()
                            .getId())
                    .put("name", chargeCommission.getAgent()
                            .getAgentName())
                    .toMap();
        }
    }

    public Object getAmount()
    {
        return chargeCommission.getAmount();
    }

    public Object getCurrency()
    {
        return chargeCommission.getCurrency();
    }

    public Object getOrder()
    {
        return new OrderView(chargeCommission.getCharge().getOrder());
    }
}
