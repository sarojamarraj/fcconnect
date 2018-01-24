package com.freightcom.api.model.views;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.hateoas.core.Relation;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.freightcom.api.model.Address;
import com.freightcom.api.model.CustomerOrder;
import com.freightcom.api.model.Payable;
import com.freightcom.api.model.Service;
import com.freightcom.api.model.TransactionalEntity;
import com.freightcom.api.util.MapBuilder;

@Relation(collectionRelation = "payable")
public class PayableView extends BaseView
{
    private final Payable payable;

    public PayableView(Payable payable)
    {
        this.payable = payable;
    }

    @Override
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

    public Object getCarrier()
    {
        return MapBuilder.getNew()
                .put("id", payable.getServiceId())
                .put("name", payable.getServiceName())
                .toMap();
    }

    public Object getToAddress()
    {
        Object addressView = null;
        Service service = payable.getService();

        if (service != null) {
            Address address = service.getAddress();

            if (address != null) {
                addressView = new AddressView(address);
            }
        }

        if (addressView == null) {
            return new HashMap<String, String>();
        } else {
            return addressView;
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

    public Object getFromAddress()
    {
        if (payable.getFromAddress() == null) {
            return MapBuilder.emptyMap();
        } else {
            return new AddressView(payable.getFromAddress());
        }
    }

    public Object getOrders()
    {
        return payable.getOrders()
                .stream()
                .map(order -> new PayableOrderView(order, payable))
                .collect(Collectors.toList());
    }

    public Object getGroupedCharges()
    {
        return payable.getGroupedCharges();
    }

    private class PayableOrderView
    {
        private final CustomerOrder order;
        private final Payable payable;

        public PayableOrderView(CustomerOrder order, Payable payable)
        {
            this.order = order;
            this.payable = payable;
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
        public String getMasterTrackingNum()
        {
            return order.getMasterTrackingNum();
        }

        @SuppressWarnings("unused")
        public BigDecimal getTotalCost()
        {
            return payable.getOrderCost(order);
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
