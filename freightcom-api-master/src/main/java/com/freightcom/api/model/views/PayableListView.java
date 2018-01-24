package com.freightcom.api.model.views;


import java.math.BigDecimal;
import java.time.ZonedDateTime;

import org.springframework.hateoas.core.Relation;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.freightcom.api.model.Payable;
import com.freightcom.api.model.TransactionalEntity;

@Relation(collectionRelation = "payable")
public class PayableListView extends BaseView
{
    private final Payable payable;

    public PayableListView(Payable payable)
    {
        this.payable = payable;
    }

    public TransactionalEntity object()
    {
        return payable;
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    public ZonedDateTime getPaidAt()
    {
        return payable.getPaidAt();
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    public ZonedDateTime getDueAt()
    {
        return payable.getDueAt();
    }

    public Long getCarrierId()
    {
        if (payable.getService() != null) {
            return payable.getService().getId();
        } else {
            return null;
        }
    }

    public String getCarrierName()
    {
        if (payable.getService() != null) {
            return payable.getService().getName();
        } else {
            return null;
        }
    }

    public BigDecimal getTotalAmount()
    {
        return payable.getTotalAmount();
    }

    public BigDecimal getPaidAmount()
    {
        return payable.getPaidAmount();
    }

    public String getStatus()
    {
        return payable.getStatus();
    }

    public Object getOrderCount()
    {
        return payable.getOrderCount();
    }

    public Object getChargeCount()
    {
        return payable.getChargeCount();
    }
}
