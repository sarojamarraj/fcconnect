package com.freightcom.api.model.views;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Map;

import org.springframework.hateoas.core.Relation;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.freightcom.api.model.CustomerOrder;
import com.freightcom.api.repositories.custom.OrderSpecification;
import com.freightcom.api.services.ServiceProvider;
import com.freightcom.api.services.orders.OrderLogic;

@Relation(collectionRelation = "customerOrders")
public class DisputedOrderView implements View
{
    private final CustomerOrder order;
    private final OrderLogic orderLogic;
    private final Map<String, Object> criteria;

    public DisputedOrderView(CustomerOrder order, Map<String, Object> criteria,  final ServiceProvider provider)
    {
        this.order = order;
        this.orderLogic = OrderLogic.get(order, provider);
        this.criteria = criteria;
    }

    public Long getId()
    {
        return order.getId();
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    public ZonedDateTime getShipDate()
    {
        return order.getShipDate();
    }

    public String getViewName()
    {
        return "DisputedOrderView";
    }

    public String getBolId()
    {
        return order.getBolId();
    }

    public String getReferenceCode()
    {
        return order.getReferenceCode();
    }

    public String getPackageTypeName()
    {
        return order.getPackageTypeName();
    }

    public Long getStatusId()
    {
        return order.getStatusId();
    }

    public String getStatusName()
    {
        if (criteria != null && criteria.get("mode") == OrderSpecification.Mode.SUBMITTED_ONLY) {
            String name = order.getOrderStatusName();

            if (name != null && name.matches("INVOIC")) {
                name = "DELIVERED";
            }

            return name;
        } else {
            return order.getOrderStatusName();
        }
    }

    public SimpleCustomerView getCustomer()
    {
        if (order.getCustomer() == null) {
            return null;
        } else {
            return new SimpleCustomerView(order.getCustomer());
        }
    }

    public String getLatestComment()
    {
        return orderLogic.getLatestComment();
    }

    public Long getDisputeAgeInDays()
    {
        return orderLogic.getDisputeAgeInDays();
    }

    public BigDecimal getDisputeAmount()
    {
        return orderLogic.getDisputeAmount();
    }

    public String getDisputeLatestComment()
    {
        return orderLogic.getDisputeLatestComment();
    }
}
