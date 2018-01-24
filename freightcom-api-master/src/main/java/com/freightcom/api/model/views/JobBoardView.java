package com.freightcom.api.model.views;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.core.Relation;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.freightcom.api.model.Agent;
import com.freightcom.api.model.Carrier;
import com.freightcom.api.model.Customer;
import com.freightcom.api.model.CustomerOrder;
import com.freightcom.api.model.Service;
import com.freightcom.api.model.ShippingAddress;
import com.freightcom.api.repositories.custom.OrderSpecification;
import com.freightcom.api.services.ServiceProvider;
import com.freightcom.api.services.orders.OrderLogic;

@Relation(collectionRelation = "customerOrders")
public class JobBoardView implements View
{
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final CustomerOrder order;
    private final Map<String, Object> criteria;
    private final ServiceProvider provider;

    public JobBoardView(CustomerOrder order, Map<String, Object> criteria, final ServiceProvider provider)
    {
        this.criteria = criteria;
        this.order = order;
        this.provider = provider;

        if (order == null) {
            throw new Error("Null order to view");
        }

        log.debug("\nCONVERTING2 " + order + "\n");
    }

    public String getViewName()
    {
        return "JobBoardView";
    }

    public Long getId()
    {
        return order.getId();
    }

    public String getActualCarrierName()
    {
        return order.getActualCarrierName();
    }

    public Integer getDestinationCount()
    {
        return order.getDestinationCount();
    }

    public Long getDistributionGroupId()
    {
        return order.getDistributionGroupId();
    }

    public String getTrackingUrl()
    {
        return order.getTrackingUrl();
    }

    public OrderCustomerView getCustomer()
    {
        if (order.getCustomer() == null) {
            return null;
        } else {
            return new OrderCustomerView(order.getCustomer());
        }
    }

    public ShippingAddress getShipFrom()
    {
        return order.getShipFrom();
    }

    public ShippingAddress getShipTo()
    {
        return order.getShipTo();
    }

    public Long getStatusId()
    {
        return order.getStatusId();
    }

    public String getPackageTypeName()
    {
        return order.getPackageTypeName();
    }

    public String getDangerousGoods()
    {
        return order.getDangerousGoods();
    }

    public Float getActualWeight()
    {
        return order.getActualWeight();
    }

    public String getPerCubicFeet()
    {
        return order.getPerCubicFeet();
    }

    public boolean hasClaim()
    {
        return order.getClaim() != null;
    }

    public Long getEshipperOid()
    {
        return order.getEshipperOid();
    }

    public String getCarrierPickUpConf()
    {
        return order.getCarrierPickUpConf();
    }

    public BigDecimal getTotalCharge()
    {
        return order.getTotalCharge();
    }

    public BigDecimal getUnbilledCharges()
    {
        return order.getUnbilledCharges();
    }

    public BigDecimal getBaseCharge()
    {
        return order.getBaseCharge();
    }

    public String getMasterTrackingNum()
    {
        return order.getMasterTrackingNum();
    }

    public String getReferenceCode()
    {
        return order.getReferenceCode();
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    public ZonedDateTime getCreatedAt()
    {
        return order.getCreatedAt();
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    public ZonedDateTime getUpdatedAt()
    {
        return order.getUpdatedAt();
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    public ZonedDateTime getScheduledShipDate()
    {
        return order.getScheduledShipDate();
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    public ZonedDateTime getShipDate()
    {
        return order.getShipDate();
    }

    public Service getService()
    {
        return order.getService();
    }

    public Carrier getCarrier()
    {
        return order.getCarrier();
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

    public Map<String, Object> getAgent()
    {
        Agent agent = order.getAgent();
        Map<String, Object> data = null;

        if (agent != null) {
            data = new HashMap<String, Object>();

            data.put("userId", agent.getUserId());
            data.put("id", agent.getId());
            data.put("agentName", agent.getAgentName());
        }

        return data;
    }

    public String getCarrierName()
    {
        return order.getActualCarrierName();
    }

    public String getBolId()
    {
        return order.getBolId();
    }

    public String getServiceName()
    {
        return order.getServiceName();
    }

    public Integer getBillingStatus()
    {
        return order.getBillingStatus();
    }

    public String getColorCode()
    {
        return order.getColorCode();
    }

    public String getDeliveryDate()
    {
        return order.getExpectedDeliveryDate();
    }

    public String getPodName()
    {
        return order.getPodName();
    }

    public View getScheduledPickup()
    {
        if (order.getScheduledPickup() != null) {
            return new PickupView(order.getScheduledPickup());
        } else {
            return null;
        }
    }

    public String getInvoiceStatus()
    {
        return order.getInvoiceStatus();
    }

    public String getLatestComment()
    {
        return OrderLogic.get(order, provider)
                .getLatestComment();
    }

    public OrderStatusView getOrderStatus()
    {
        if (order.getOrderStatus() == null) {
            return null;
        } else {
            return new OrderStatusView(order.getOrderStatus());
        }
    }

    public Boolean getCustomWorkOrder()
    {
        return order.getCustomWorkOrder();
    }

    private class OrderCustomerView
    {
        private final Customer customer;

        public OrderCustomerView(final Customer customer)
        {
            this.customer = customer;
        }

        @SuppressWarnings("unused")
        public Long getCustomerId()
        {
            return customer.getId();
        }

        @SuppressWarnings("unused")
        public Long getId()
        {
            return customer.getId();
        }

        @SuppressWarnings("unused")
        public String getName()
        {
            return customer.getName();
        }
    }
}
