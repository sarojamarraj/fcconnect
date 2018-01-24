package com.freightcom.api.model.views;


import java.math.BigDecimal;
import java.time.ZonedDateTime;

import org.springframework.hateoas.core.Relation;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.freightcom.api.model.Customer;

@Relation(collectionRelation = "customer")
public class OrderCustomerView
{
    private final Customer customer;

    public OrderCustomerView(Customer customer)
    {
        this.customer = customer;
    }

    public Long getId() {
        return customer.getId();
    }

    public Long getOrderCount() {
        return customer.getOrderCount();
    }

    public UserRoleView getSalesAgent() throws Exception {
        if (customer.getSalesAgent() != null) {
            return UserRoleViewFactory.get(customer.getSalesAgent());
        } else {
            return null;
        }
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    public ZonedDateTime getSuspendedAt() {
         return customer.getSuspendedAt();
    }

    public String getInvoiceCurrency() {
         return customer.getInvoiceCurrency();
    }

    public String getPostalCode() {
         return customer.getPostalCode();
    }

    public Integer getSmallPackage() {
         return customer.getSmallPackage();
    }

    public Boolean getDeleted() {
         return customer.getDeleted();
    }

    public Boolean getIsWebCustomer() {
         return customer.getIsWebCustomer();
    }

    public String getLanguagePreference() {
         return customer.getLanguagePreference();
    }

    public Integer getBroker() {
         return customer.getBroker();
    }

    public String getTimeZone() {
         return customer.getTimeZone();
    }

    public Integer getInvoiceTermWarning() {
         return customer.getInvoiceTermWarning();
    }

    public String getBrokerName() {
         return customer.getBrokerName();
    }

    public String getAddress() {
         return customer.getAddress();
    }

    public Long getCreator() {
         return customer.getCreator();
    }

    public String getEmail() {
         return customer.getEmail();
    }

    public Integer getInvoiceTerm() {
         return customer.getInvoiceTerm();
    }

    public BigDecimal getCreditLimit() {
         return customer.getCreditLimit();
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    public ZonedDateTime getUpdatedAt() {
         return customer.getUpdatedAt();
    }

    public Long getSubAgentId() {
         return customer.getSubAgentId();
    }

    public Integer getShowRates() {
         return customer.getShowRates();
    }

    public Integer getNmfcCode() {
         return customer.getNmfcCode();
    }

    public String getReference2() {
         return customer.getReference2();
    }

    public String getReference1() {
         return customer.getReference1();
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    public ZonedDateTime getCreatedAt() {
         return customer.getCreatedAt();
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    public ZonedDateTime getActivatedAt() {
         return customer.getActivatedAt();
    }

    public String getCity() {
         return customer.getCity();
    }

    public String getReference3() {
         return customer.getReference3();
    }

    public String getAccountNumber() {
         return customer.getAccountNumber();
    }

    public Integer getStatus() {
         return customer.getStatus();
    }

    public Integer getFreightClass() {
         return customer.getFreightClass();
    }

    public Integer getApplyCreditLimit() {
         return customer.getApplyCreditLimit();
    }

    public String getName() {
         return customer.getName();
    }

    public Float getDiscountPerc() {
         return customer.getDiscountPerc();
    }

    public String getPhone() {
         return customer.getPhone();
    }

    public String getTaxId() {
         return customer.getTaxId();
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    public ZonedDateTime getDeletedAt() {
         return customer.getDeletedAt();
    }

    public Boolean getShippingPODRequired() {
         return customer.getShippingPODRequired();
    }

    public Integer getApplyCurrencyExchange() {
         return customer.getApplyCurrencyExchange();
    }

    public Boolean getActive() {
         return customer.isActive();
    }

    public Boolean getDisableIfUnpaidInvoices() {
         return customer.getDisableIfUnpaidInvoices();
    }

    public String getInvoiceEmail() {
         return customer.getInvoiceEmail();
    }

    public String getDba() {
         return customer.getDba();
    }

    public Long getBillingAddressId() {
         return customer.getBillingAddressId();
    }

    public Boolean getApplyTax() {
         return customer.getApplyTax();
    }

    public String getContact() {
         return customer.getContact();
    }

    public String getProvince() {
         return customer.getProvince();
    }

    public Boolean getIsChargeCreditCard() {
         return customer.getIsChargeCreditCard();
    }

    public Integer getSingleShipmentInvoicing() {
         return customer.getSingleShipmentInvoicing();
    }
    public Integer getInvoiceCurrencyId() {
         return customer.getInvoiceCurrencyId();
    }

    public String getCountry() {
         return customer.getCountry();
    }

}
