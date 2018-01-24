package com.freightcom.api.model.views;


import java.math.BigDecimal;
import java.time.ZonedDateTime;

import org.springframework.hateoas.core.Relation;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.freightcom.api.model.CommissionPayable;
import com.freightcom.api.model.TransactionalEntity;

@Relation(collectionRelation = "commissionPayable")
public class CommissionPayableListView extends BaseView
{
    private final CommissionPayable commissionPayable;

    public CommissionPayableListView(CommissionPayable commissionPayable)
    {
        this.commissionPayable = commissionPayable;
    }

    public TransactionalEntity object()
    {
        return commissionPayable;
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    public ZonedDateTime getPaidAt()
    {
        return commissionPayable.getPaidAt();
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    public ZonedDateTime getDueAt()
    {
        return commissionPayable.getDueAt();
    }

    public Long getAgentId()
    {
        if (commissionPayable.getAgent() != null) {
            return commissionPayable.getAgent().getId();
        } else {
            return null;
        }
    }

    public String getAgentName()
    {
        if (commissionPayable.getAgent() != null) {
            return commissionPayable.getAgent().getAgentName();
        } else {
            return null;
        }
    }

    public BigDecimal getTotalAmount()
    {
        return commissionPayable.getTotalAmount();
    }

    public BigDecimal getPaidAmount()
    {
        return commissionPayable.getPaidAmount();
    }

    public String getStatus()
    {
        return commissionPayable.getStatus();
    }

    public Object getOrderCount()
    {
        return commissionPayable.getOrderCount();
    }

    public Object getChargeCount()
    {
        return commissionPayable.getChargeCount();
    }
}
