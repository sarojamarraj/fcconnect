package com.freightcom.api.model.views;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.core.Relation;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.freightcom.api.model.CustomerOrder;

@Relation(collectionRelation = "customerOrders")
public class InvoiceOrderView implements View
{
    @SuppressWarnings("unused")
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    protected final CustomerOrder order;


    public InvoiceOrderView(CustomerOrder order)
    {
        this.order = order;

        if (order == null) {
            throw new Error("Null order to view");
        }
    }

    public String getBolId()
    {
        return order.getBolId();
    }

    public Long getId()
    {
        return order.getId();
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    public ZonedDateTime getCreatedAt()
    {
        return order.getCreatedAt();
    }

    public BigDecimal getTotalCharge()
    {
        return order.getTotalCharge();
    }

    public String getStatusName()
    {
        return order.getOrderStatusName();
    }

    public String getPackageTypeName()
    {
        return order.getPackageTypeName();
    }

}
