package com.freightcom.api.model.views;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.hateoas.core.Relation;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.freightcom.api.model.CommissionPayable;
import com.freightcom.api.model.CustomerOrder;
import com.freightcom.api.model.TransactionalEntity;
import com.freightcom.api.util.MapBuilder;

@Relation(collectionRelation = "commissionPayable")
public class CommissionPayableView extends CommissionCommon
{
    private final CommissionPayable commissionPayable;

    public CommissionPayableView(CommissionPayable commissionPayable)
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

    public Object getAgent()
    {
        if (commissionPayable.getAgent() != null) {
            return MapBuilder.getNew()
                .put("id", commissionPayable.getAgent().getId())
                .put("name", commissionPayable.getAgent().getAgentName())
                .toMap();
        } else {
            return null;
        }
    }

    public Object getFromAddress()
    {
        if (commissionPayable.getFromAddress() == null) {
            return MapBuilder.emptyMap();
        } else {
            return new AddressView(commissionPayable.getFromAddress());
        }
    }

    public Object getToAddress()
    {
        if (commissionPayable.getAgent() == null
            || commissionPayable.getAgent().getAddress() == null) {
            return MapBuilder.emptyMap();
        } else {
            return new ShippingAddressView(commissionPayable.getAgent().getAddress());
        }
    }

    public Object getOrders()
    {
        return commissionPayable.getOrders()
            .stream()
            .map(order -> new CommissionOrderView(order, commissionPayable))
            .collect(Collectors.toList());
    }

    public Object getOrderCount()
    {
        return commissionPayable.getOrderCount();
    }

    public Object getChargeCount()
    {
        return commissionPayable.getChargeCount();
    }

    private class CommissionOrderView
    {
        private final CustomerOrder order;
        private final CommissionPayable commissionPayable;

        public CommissionOrderView(CustomerOrder order, CommissionPayable commissionPayable)
        {
            this.order = order;
            this.commissionPayable = commissionPayable;
        }

        @SuppressWarnings("unused")
        public Long getId()
        {
            return order.getId();
        }

        @SuppressWarnings("unused")
        public String getBolId()
        {
            return order.getBolId();
        }

        @SuppressWarnings("unused")
        public BigDecimal getCommission()
        {
            return commissionPayable.getOrderCommission(order);
        }

        @SuppressWarnings("unused")
        public BigDecimal getTotalCharge()
        {
            return commissionPayable.getOrderCharge(order);
        }

        @SuppressWarnings("unused")
        public String getMasterTrackingNum()
        {
            return order.getMasterTrackingNum();
        }

        @SuppressWarnings("unused")
        public Map<String, Object> getCustomer()
        {
            Map<String, Object> customer = new HashMap<String, Object>();

            if (order.getCustomer() != null) {
                customer.put("id", order.getCustomer()
                        .getId());
                customer.put("name", order.getCustomer()
                        .getName());
            }

            return customer;
        }

        @JsonFormat(pattern = "yyyy-MM-dd")
        public ZonedDateTime getShipDate()
        {
            return order.getShipDate();
        }
    }


}
