package com.freightcom.api.model.views;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.core.Relation;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.freightcom.api.model.Agent;
import com.freightcom.api.model.Carrier;
import com.freightcom.api.model.Charge;
import com.freightcom.api.model.CustomerOrder;
import com.freightcom.api.model.InsuranceType;
import com.freightcom.api.model.Package;
import com.freightcom.api.model.Service;
import com.freightcom.api.repositories.custom.OrderSpecification;
import com.freightcom.api.services.ServiceProvider;
import com.freightcom.api.services.orders.OrderLogic;

@Relation(collectionRelation = "customerOrders")
public class OrderView implements View
{
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final CustomerOrder order;
    private Map<String, Object> criteria = null;
    private final ServiceProvider provider;

    public static OrderView get(ServiceProvider provider, CustomerOrder order, Map<String, Object> criteria)
    {
        return new OrderView(order, criteria, provider);
    }


    public static OrderView get(ServiceProvider provider, CustomerOrder order)
    {
        return new OrderView(order, provider);
    }

    protected OrderView(CustomerOrder order, ServiceProvider provider)
    {
        this.order = order;
        this.provider = provider;

        if (order == null) {
            throw new Error("Null order to view");
        }

        log.debug("\nCONVERTING " + order + "\n");
    }

    protected OrderView(CustomerOrder order, Map<String, Object> criteria, ServiceProvider provider)
    {
        this.criteria = criteria;
        this.order = order;
        this.provider = provider;

        if (order == null) {
            throw new Error("Null order to view");
        }

        log.debug("\nCONVERTING2 " + order + "\n");
    }

    public Long getId()
    {
        return order.getId();
    }

    public String getViewName()
    {
        return "OrderView";
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

    public SimpleCustomerView getCustomer()
    {
        if (order.getCustomer() == null) {
            return null;
        } else {
            return new SimpleCustomerView(order.getCustomer());
        }
    }

    public Object getPackages()
    {
        if (order.getPackages() == null) {
            return new ArrayList<Package>(0);
        } else {
            return order.getPackages().stream()
                .map(orderPackage -> new OrderPackage(orderPackage))
                .collect(Collectors.toList());
        }
    }

    public List<OrderPallet> getPallets()
    {
        if (order.getPallets() == null) {
            return new ArrayList<OrderPallet>(0);
        } else {
            return order.getPallets().stream()
                .map(pallet -> new OrderPallet(pallet))
                .collect(Collectors.toList());
        }
    }

    public Object getShipFrom()
    {
        if (order.getShipFrom() == null) {
            return null;
        } else {
            return new ShippingAddressView(order.getShipFrom());
        }
    }

    public Object getShipTo()
    {
        if (order.getShipTo() == null) {
            return null;
        } else {
            return new ShippingAddressView(order.getShipTo());
        }
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

    public String getPerCubicFeet()
    {
        return order.getPerCubicFeet();
    }

    public Boolean getHasClaim()
    {
        if (provider.getRole().canManageClaims()
            || order.getCustomer().getId().equals(provider.getRole().getCustomerId())) {
            return order.getClaim() != null
                && ! order.getClaim().isResolved();
        } else {
            return (Boolean) null;
        }
    }

    public Object getClaim()
    {
        if (provider.getRole().canManageClaims()
            || order.getCustomer().getId().equals(provider.getRole().getCustomerId())) {
            return ClaimView.get(order.getClaim());
        } else {
            return null;
        }
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

    public String getInvoiceCurrency() {
         return order.getInvoiceCurrency();
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

    public List<String> getAccessorialServiceNames()
    {
        return order.getAccessorialServices();
    }

    public Object getAccessorials()
    {
        return order.getAccessorials();
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

    public BigDecimal getTotalCost()
    {
        return order.getTotalCost();
    }


    public List<ChargeView> getCharges()
    {
        return order.getCharges().stream().map(charge -> ChargeView.get(provider.getApiSession().getRole(), charge)).collect(Collectors.toList());
    }

    public Object getChargeTotalRow()
    {
        Map<String,BigDecimal> result = new HashMap<String,BigDecimal>(4);

        BigDecimal subTotal = BigDecimal.ZERO;
        BigDecimal cost = BigDecimal.ZERO;
        BigDecimal markup = BigDecimal.ZERO;
        BigDecimal commission = BigDecimal.ZERO;

        for (Charge charge: order.getCharges()) {
            subTotal = subTotal.add(charge.getSubTotal());
            cost = cost.add(charge.getCost());
            markup = markup.add(charge.getMarkup());
            commission = commission.add(charge.getCommission());
        }

        if (provider.getApiSession().getRole().canViewCost()) {
            result.put("cost", cost);
            result.put("markup", markup);
            result.put("commission", commission);
        }

        result.put("subTotal", subTotal);

        return result;
    }

    public SelectedQuoteView getSelectedQuote()
    {
        if (order.getSelectedQuote() != null) {
            return new SelectedQuoteView(order.getSelectedQuote());
        } else {
            return null;
        }
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
        return OrderLogic.get(order, provider).getLatestComment();
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

    public Boolean getCustomsSurveyFormRequired()
    {
        return OrderLogic.get(order, provider).getCustomsSurveyFormRequired();
    }

    public View getCustomsInvoice()
    {
        log.debug("GET CUSTOMS INVOICE IN VIEW " + order.getCustomsInvoice());
        return CustomsInvoiceView.get(order.getCustomsInvoice());
    }

    public List<Object> getFiles()
    {
        return order.getFiles().stream().map(file -> OrderDocumentView.get(file, provider))
            .collect(Collectors.toList());
    }

    public Object getWeight()
    {
        return order.getWeight();
    }

    public Boolean getSignatureRequired()
    {
        return order.getSignatureRequired().equals("Yes");
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

    public Boolean getHasShippingLabel()
    {
        return order.hasShippingLabel();
    }

    public Object getInsuranceType()
    {
        return order.getInsuranceType() == null ? InsuranceType.DECLINED.getValue() : order.getInsuranceType().getValue();
    }
}
