package com.freightcom.api.model.views;

import java.math.BigDecimal;

import org.springframework.hateoas.core.Relation;

import com.freightcom.api.model.Charge;
import com.freightcom.api.model.TransactionalEntity;
import com.freightcom.api.model.UserRole;
import com.freightcom.api.util.MapBuilder;

@Relation(collectionRelation = "charge")
public class ChargeView extends BaseView
{
    protected final Charge charge;

    public static ChargeView get(UserRole role, Charge charge)
    {
        if (role.canViewCost()) {
            return new ChargeViewAdmin(charge);
        } else {
            return new ChargeView(charge);
        }
    }

    protected ChargeView(Charge charge)
    {
        this.charge = charge;
    }

    @Override
    public TransactionalEntity object()
    {
        return charge;
    }

    public String getCurrency()
    {
        return charge.getCurrency();
    }

    public BigDecimal getCharge()
    {
        return charge.getCharge();
    }

    public BigDecimal getSubTotal()
    {
        return charge.getSubTotal();
    }

    public Long getAccessorialId()
    {
        return charge.getAccessorialServiceId();
    }

    public Long getOrderAccessorialId()
    {
        return charge.getAccessorialId();
    }

    public BigDecimal getTotalTax()
    {
        return charge.getTotalTax();
    }

    public BigDecimal getTotal()
    {
        return charge.getTotal();
    }

    public String getDescription()
    {
        return charge.getDescription();
    }

    public String getName()
    {
        return charge.getName();
    }

    public Integer getQuantity()
    {
        return charge.getQuantity();
    }

    public Long getInvoiceId()
    {
        return charge.getInvoiceId();
    }

    public String getStatus()
    {
        if ( charge.getInvoiceId() == null) {
            return "unbilled";
        } else {
            return "billed";
        }
    }

    public Long getOrderId()
    {
        if (charge.getOrder() != null) {
            return charge.getOrder().getId();
        } else {
            return null;
        }
    }

    public Long getAgentId()
    {
        if (charge.getAgent() != null) {
            return charge.getAgent().getId();
        } else {
            return null;
        }
    }

    public Boolean getReconciled()
    {
         return charge.getReconciled();
    }

    public Boolean getPayableReported()
    {
         return charge.getPayable() != null;
    }

    public Boolean getCommissionReported()
    {
         return charge.getChargeCommission() != null && charge.getChargeCommission().size() > 0;
    }

    public Object getCarrier()
    {
        if (charge.getCarrier() == null) {
            return null;
        } else {
            return MapBuilder.getNew()
                .put("id", charge.getService().getId())
                .put("name", charge.getService().getName())
                .toMap();
        }
    }

    public Boolean getDisputed()
    {
        return charge.getDisputedAt() != null;
    }

    public Boolean getDisputeResolved()
    {
        return charge.getDisputeResolvedAt() != null;
    }

}
