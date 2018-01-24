package com.freightcom.api.model.views;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.hateoas.core.Relation;

import com.freightcom.api.model.Customer;

@Relation(collectionRelation = "customer")
public class CustomerListView implements View
{
    private final Customer customer;

    public CustomerListView(Customer customer)
    {
        this.customer = customer;
    }

    public Long getId()
    {
        return customer.getId();
    }

    public String getPostalCode()
    {
        return customer.getPostalCode();
    }

    public String getCity()
    {
        return customer.getCity();
    }

    public String getName()
    {
        return customer.getName();
    }

    public Boolean getActive()
    {
        return customer.isActive();
    }

    public String getProvince()
    {
        return customer.getProvince();
    }

    public String getCountry()
    {
        return customer.getCountry();
    }

    public String getAgentName()
    {
        if (customer.getSalesAgent() != null) {
            return customer.getSalesAgent().getAgentName();
        }

        return "";
    }

    public Map<String,BigDecimal> getCreditAvailable()
    {
        return customer.getCreditAvailableMap();
    }

}
