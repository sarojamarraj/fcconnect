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
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *
 *
 * @author
 */
@Entity(name = "OrderRateQuote")
@Table(name = "order_rate_quote")
public class OrderRateQuote extends TransactionalEntity
{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne(cascade = { CascadeType.REFRESH })
    @JoinColumn(name = "order_id", referencedColumnName = "id", nullable = true)
    private CustomerOrder order;

    @OneToOne(cascade = { CascadeType.REFRESH })
    @JoinColumn(name = "carrier_id", referencedColumnName = "id", nullable = true)
    private Carrier carrier;

    @OneToOne(cascade = { CascadeType.REFRESH })
    @JoinColumn(name = "service_id", referencedColumnName = "id", nullable = true)
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

    @Column(name = "residential_delivery_fee")
    private BigDecimal residentialDeliveryFee = BigDecimal.ZERO;

    @Column(name = "guaranteed_delivery")
    private Boolean guaranteedDelivery = false;

    @Column(name = "currency")
    private String currency;

    @Column(name = "scheduled_delivery_time")
    private String scheduledDeliveryTime;

    @OneToMany(targetEntity = QuoteTax.class, mappedBy = "orderRateQuote", cascade = { CascadeType.ALL })
    private List<QuoteTax> taxes = new ArrayList<QuoteTax>(0);

    public OrderRateQuote()
    {
        super();
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    @JsonIgnore
    public CustomerOrder getOrder()
    {
        return order;
    }

    public void setOrder(CustomerOrder order)
    {
        this.order = order;
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

    public Carrier getCarrier()
    {
        return carrier;
    }

    public void setCarrier(Carrier carrier)
    {
        this.carrier = carrier;
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

    public QuoteTax addTax(String name, String taxId, double rate)
    {
        QuoteTax tax = new QuoteTax(name, taxId, rate, baseFee);

        totalCharges = totalCharges.add(tax.getAmount());

        tax.setOrderRateQuote(this);
        getTaxes().add(tax);

        return tax;
    }

    public List<QuoteTax> getTaxes()
    {
        return taxes;
    }

    public void setTaxes(List<QuoteTax> taxes)
    {
        this.taxes = taxes;
    }

    public BigDecimal getTotalTax()
    {
        return taxes.stream()
                .map(tax -> tax.getAmount())
                .reduce(BigDecimal.ZERO, (amount, total) -> total.add(amount));
    }

    public BigDecimal getResidentialDeliveryFee()
    {
        return residentialDeliveryFee;
    }

    public void setResidentialDeliveryFee(BigDecimal residentialDeliveryFee)
    {
        this.residentialDeliveryFee = residentialDeliveryFee;
    }
}
