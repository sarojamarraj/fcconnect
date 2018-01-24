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
import com.freightcom.api.model.CustomerOrder;
import com.freightcom.api.model.ShippingAddress;
import com.freightcom.api.repositories.custom.OrderSpecification;
import com.freightcom.api.services.ServiceProvider;

@Relation(collectionRelation = "customerOrders")
public class SubmittedOrderView implements View
{
    @SuppressWarnings("unused")
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    protected final CustomerOrder order;
    private final Map<String, Object> criteria;
    private final ServiceProvider provider;

    public SubmittedOrderView(CustomerOrder order, Map<String, Object> criteria, final ServiceProvider provider)
    {
        this.criteria = criteria;
        this.order = order;
        this.provider = provider;

        if (order == null) {
            throw new Error("Null order to view");
        }
    }

    public String getViewName()
    {
        return "SubmittedOrderView";
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

    public Boolean getHasClaim()
    {
        if (provider.getRole()
                .canManageClaims()
                || order.getCustomer()
                        .getId()
                        .equals(provider.getRole()
                                .getCustomerId())) {
            return order.getClaim() != null
                && ! order.getClaim().isResolved();
        } else {
            return (Boolean) null;
        }
    }

    public Boolean getCancellable()
    {
        return order.getCancellable();
    }

    public Boolean getTrackable()
    {
        return order.getTrackable();
    }

    public Boolean getClaimCanBeFiled()
    {
        return order.getClaimCanBeFiled();
    }

    public Boolean getIsSchedulable()
    {
        return order.isSchedulable();
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

    public String getLogoPath()
    {
        String prefix = "/public/carrier/";
        String logoName = "";

        if (order.getCarrier() == null) {
            // No logo yet
        } else if ("ups".equalsIgnoreCase(order.getCarrier()
                .getName())) {
            logoName = "UPS-large";
        } else if ("federal express".equalsIgnoreCase(order.getCarrier()
                .getName())) {
            logoName = "Fedex";
        } else if (order.getCarrier()
                .getId() == 40) {
            logoName = order.getCarrier()
                    .getName();
        } else if (order.getCarrier()
                .getName()
                .equalsIgnoreCase("freightcom")) {
            logoName = order.getService()
                    .getProvider();
        } else if (order.getService() != null) {
            logoName = order.getService()
                    .getName();
        } else {
            logoName = null;
        }

        if (logoName == null) {

        } else if (logoName.equalsIgnoreCase("dhl ground")) {
            logoName = "dhl";
        }

        return prefix + logoName;
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

    public Boolean getDisputed()
    {
        return order.isDisputed();
    }

    public Boolean getStatusChangeable()
    {
        return order.statusChangeable();
    }

    public Boolean getCanSchedulePickup()
    {
        return order.canSchedulePickup();
    }
}
