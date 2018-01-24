package com.freightcom.api.model.views;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.hateoas.core.Relation;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.freightcom.api.model.CreditCard;
import com.freightcom.api.model.Customer;
import com.freightcom.api.model.Customer.AutoCharge;
import com.freightcom.api.model.Customer.AutoInvoice;
import com.freightcom.api.model.Customer.PastDueAction;
import com.freightcom.api.model.PackagePreference;
import com.freightcom.api.services.converters.CreditCardConverter;

@Relation(collectionRelation = "customer")
public class CustomerView
{
    protected final Customer customer;

    public CustomerView(Customer customer)
    {
        this.customer = customer;
    }

    public Long getId()
    {
        return customer.getId();
    }

    public Long getOrderCount()
    {
        return customer.getOrderCount();
    }

    public UserRoleView getSalesAgent() throws Exception
    {
        if (customer.getSalesAgent() != null) {
            return UserRoleViewFactory.get(customer.getSalesAgent());
        } else {
            return null;
        }
    }

    public boolean getSuspended()
    {
        return customer.getSuspended();
    }

    public String getInvoiceCurrency()
    {
        return customer.getInvoiceCurrency();
    }

    public String getPostalCode()
    {
        return customer.getPostalCode();
    }

    public Integer getInvoiceTermWarning()
    {
        return customer.getInvoiceTermWarning();
    }

    public String getAddress()
    {
        return customer.getAddress();
    }

    public String getEmail()
    {
        return customer.getEmail();
    }

    public Integer getInvoiceTerm()
    {
        return customer.getInvoiceTerm();
    }

    public Long getAgentId()
    {
        return customer.getSubAgentId();
    }

    public String getCity()
    {
        return customer.getCity();
    }

    public String getName()
    {
        return customer.getName();
    }

    public String getPhone()
    {
        return customer.getPhone();
    }

    public Boolean getShippingPODRequired()
    {
        return customer.getShippingPODRequired();
    }

    public Boolean getShippingNMFCRequired()
    {
        return customer.getShippingNMFCRequired();
    }

    public Boolean getActive()
    {
        return customer.isActive();
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    public ZonedDateTime getActivatedAt()
    {
        return customer.getActivatedAt();
    }

    public String getInvoiceEmail()
    {
        return customer.getInvoiceEmail();
    }

    public String getContact()
    {
        return customer.getContact();
    }

    public String getProvince()
    {
        return customer.getProvince();
    }

    public String getCountry()
    {
        return customer.getCountry();
    }

    public Map<String,BigDecimal> getCreditAvailable()
    {
        return customer.getCreditAvailableMap();
    }

    public AutoInvoice getAutoInvoice()
    {
        return customer.getAutoInvoice();
    }

    public Object getShipTo()
    {
        return customer.getShipTo() == null ? null : new ShippingAddressView(customer.getShipTo());
    }

    public Object getShipFrom()
    {
        return customer.getShipFrom() == null ? null : new ShippingAddressView(customer.getShipFrom());
    }

    public Object getPackagePreference()
    {
        return customer.getPackagePreference() == null ? null : new PackageTypeView(customer.getPackagePreference());
    }

    public Boolean getAllowNewOrders()
    {
        return customer.getAllowNewOrders();
    }

    public AutoCharge getAutoCharge()
    {
        return customer.getAutoCharge();
    }

    public boolean getHasCredit()
    {
        return customer.hasCredit();
    }

    public String getTerm()
    {
        return customer.getTerm();
    }

    public PastDueAction getPastDueAction()
    {
        return customer.getPastDueAction();
    }

    public Long getVersion()
    {
        return customer.getVersion();
    }

    @JsonSerialize(contentConverter = CreditCardConverter.class)
    public List<CreditCard> getCreditCards()
    {
        return customer.getCreditCards();
    }

    public Object getApplicableTaxes()
    {
        return customer.getApplicableTaxes();
    }

    public List<Long> getExcludedServicesIds()
    {
        return customer.getExcludedServices()
                .stream()
                .map(service -> service.getId())
                .collect(Collectors.toList());
    }

    public Boolean getCcReceipt()
    {
        return customer.getCcReceipt();
    }

    private class PackageTypeView
    {
        private final PackagePreference preference;

        public PackageTypeView(final PackagePreference preference)
        {
            this.preference = preference;
        }

        @SuppressWarnings("unused")
        public Object getId()
        {
            return preference.getId();
        }

        @SuppressWarnings("unused")
        public Object getName()
        {
            return preference.getName();
        }

        @SuppressWarnings("unused")
        public Object getDescription()
        {
            return preference.getDescription();
        }
    }
}
