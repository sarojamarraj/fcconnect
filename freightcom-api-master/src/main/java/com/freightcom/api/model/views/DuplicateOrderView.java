package com.freightcom.api.model.views;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.freightcom.api.model.CustomerOrder;
import com.freightcom.api.model.Package;
import com.freightcom.api.services.ServiceProvider;


public class DuplicateOrderView implements View
{
    private final CustomerOrder order;
    @SuppressWarnings("unused")
    private final ServiceProvider provider;


    public static DuplicateOrderView get(ServiceProvider provider, CustomerOrder order)
    {
        return new DuplicateOrderView(order, provider);
    }

    protected DuplicateOrderView(CustomerOrder order, ServiceProvider provider)
    {
        this.order = order;
        this.provider = provider;

        if (order == null) {
            throw new Error("Null order to view");
        }
    }

    public Long getId()
    {
        return order.getId();
    }

    public String getViewName()
    {
        return "DuplicateOrderView";
    }

    public SimpleCustomerView getCustomer()
    {
        if (order.getCustomer() == null) {
            return null;
        } else {
            return new SimpleCustomerView(order.getCustomer());
        }
    }

    public Long getDistributionGroupId()
    {
        return order.getDistributionGroupId();
    }

    public Object getPackages()
    {
        if (order.getPackages() == null) {
            return new ArrayList<Package>(0);
        } else {
            return order.getPackages().stream()
                .map(orderPackage -> new OrderPackage(orderPackage, true))
                .collect(Collectors.toList());
        }
    }

    public List<OrderPallet> getPallets()
    {
        if (order.getPallets() == null) {
            return new ArrayList<OrderPallet>(0);
        } else {
            return order.getPallets().stream()
                .map(pallet -> new OrderPallet(pallet, true))
                .collect(Collectors.toList());
        }
    }

    public Object getShipFrom()
    {
        if (order.getShipFrom() == null) {
            return null;
        } else {
            return new ShippingAddressView(order.getShipFrom(), true);
        }
    }

    public Object getShipTo()
    {
        if (order.getShipTo() == null) {
            return null;
        } else {
            return new ShippingAddressView(order.getShipTo(), true);
        }
    }

    public String getPackageTypeName()
    {
        return order.getPackageTypeName();
    }

    public String getReferenceCode()
    {
        return order.getReferenceCode();
    }

    public Float getWeight()
    {
        return order.getWeight();
    }

    public String getInvoiceCurrency() {
         return order.getInvoiceCurrency();
    }

    public List<String> getAccessorialServiceNames()
    {
        return order.getAccessorialServices();
    }

    public String getBolId()
    {
        return order.getBolId();
    }

    public Object getAccessorials()
    {
        return order.getAccessorials();
    }

    public Boolean getSignatureRequired()
    {
        return order.getSignatureRequired().equals("Yes");
    }
}
