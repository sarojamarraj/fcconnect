/******************************************************************
              WebPal Product Suite Framework Libraries
-------------------------------------------------------------------
(c) 2002-present: all copyrights are with Palomino System Innovations Inc.
(Palomino Inc.) of Toronto, Canada

Unauthorized reproduction, licensing or disclosure of this source
code will be prosecuted. WebPal is a registered trademark of
Palomino System Innovations Inc. To report misuse please contact
info@palominosys.com or call +1 416 964 7333.
*******************************************************************/
package com.freightcom.api.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *
 *
 * @author
 */
@Entity(name = "SelectedQuote")
@Table(name = "selected_quote")
@SQLDelete(sql = "update selected_quote SET deleted_at = UTC_TIMESTAMP() where id=?")
@Where(clause = "deleted_at is null")
public class SelectedQuote extends TransactionalEntity
{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne(cascade = { CascadeType.REFRESH })
    private Service service;

    @Column(name = "transit_days")
    private Integer transitDays;

    @Column(name = "total_charges")
    private BigDecimal totalCharges;

    @Column(name = "base_fee")
    private BigDecimal baseFee;

    @Column(name = "fuel_fee")
    private BigDecimal fuelFee;

    @Column(name = "insurance")
    private BigDecimal insurance;

    @Column(name = "cross_border_fee")
    private BigDecimal crossBorderFee;

    @Column(name = "guaranteed_delivery")
    private Boolean guaranteedDelivery = false;

    @Column(name = "currency")
    private String currency;

    @Column(name = "scheduled_delivery_time")
    private String scheduledDeliveryTime;

    @OneToMany(targetEntity = SelectedQuoteTax.class, mappedBy = "selectedQuote", cascade = { CascadeType.ALL })
    private List<SelectedQuoteTax> taxes = new ArrayList<SelectedQuoteTax>(0);

    public SelectedQuote()
    {
        super();
    }

    public SelectedQuote(OrderRateQuote quote)
    {
        super();

        this.crossBorderFee = quote.getCrossBorderFee();
        this.insurance = quote.getInsurance();
        this.fuelFee = quote.getFuelFee();
        this.baseFee = quote.getBaseFee();
        this.totalCharges = quote.getTotalCharges();
        this.transitDays = quote.getTransitDays();
        this.guaranteedDelivery = quote.getGuaranteedDelivery();
        this.currency = quote.getCurrency();
        this.scheduledDeliveryTime = quote.getScheduledDeliveryTime();
        this.setService(quote.getService());

        for (QuoteTax tax: quote.getTaxes()) {
            this.addTax(tax);
        }
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Integer getTransitDays()
    {
        return transitDays;
    }

    public void setTransitDays(Integer transitDays)
    {
        this.transitDays = transitDays;
    }

    public BigDecimal getTotalCharges()
    {
        return totalCharges;
    }

    public void setTotalCharges(BigDecimal totalCharges)
    {
        this.totalCharges = totalCharges;
    }

    public BigDecimal getBaseFee()
    {
        return baseFee;
    }

    public void setBaseFee(BigDecimal baseFee)
    {
        this.baseFee = baseFee;
    }

    public BigDecimal getFuelFee()
    {
        return fuelFee;
    }

    public void setFuelFee(BigDecimal fuelFee)
    {
        this.fuelFee = fuelFee;
    }

    public BigDecimal getInsurance()
    {
        return insurance;
    }

    public void setInsurance(BigDecimal insurance)
    {
        this.insurance = insurance;
    }

    public BigDecimal getCrossBorderFee()
    {
        return crossBorderFee;
    }

    public void setCrossBorderFee(BigDecimal crossBorderFee)
    {
        this.crossBorderFee = crossBorderFee;
    }

    public Service getService()
    {
        return service;
    }

    public void setService(Service service)
    {
        this.service = service;
    }

    public Long getServiceId()
    {
        if (this.getService() == null) {
            return null;
        } else {
            return this.getService().getId();
        }
    }

    @JsonIgnore
    public Carrier getCarrier()
    {
        return service == null ? null : service.getCarrier();
    }

    public Boolean getGuaranteedDelivery()
    {
        return guaranteedDelivery;
    }

    public void setGuaranteedDelivery(Boolean guaranteed)
    {
        guaranteedDelivery = guaranteed;
    }

    public String getCurrency()
    {
        return currency;
    }

    public void setCurrency(String currency)
    {
        this.currency = currency;
    }

    public String getScheduledDeliveryTime()
    {
        return scheduledDeliveryTime;
    }

    public void setScheduledDeliveryTime(String scheduledDeliveryTime)
    {
        this.scheduledDeliveryTime = scheduledDeliveryTime;
    }

    private SelectedQuoteTax addTax(QuoteTax quoteTax)
    {
        SelectedQuoteTax tax = addTax(quoteTax.getName(), quoteTax.getTaxNumber(), quoteTax.getRate());
        
        tax.setAmount(quoteTax.getAmount());
        
        return tax;
    }

    public SelectedQuoteTax addTax(String name, String taxId, double rate)
    {
        SelectedQuoteTax tax = new SelectedQuoteTax(name, taxId, rate, totalCharges);

        tax.setSelectedQuote(this);
        getTaxes().add(tax);

        return tax;
    }

    public List<SelectedQuoteTax> getTaxes()
    {
        return taxes;
    }

    public void setTaxes(List<SelectedQuoteTax> taxes)
    {
        this.taxes = taxes;
    }
}
