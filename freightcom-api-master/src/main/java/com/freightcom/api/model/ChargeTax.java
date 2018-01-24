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
import java.math.RoundingMode;
import java.time.ZonedDateTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *
 *
 * @author
 */
@Entity(name = "ChargeTax")
@Table(name = "charge_tax")
public class ChargeTax extends TransactionalEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="amount", precision=30, scale=2)
    private BigDecimal amount;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="updated_at")
    private Date updatedAt;

    @Column(name="tax_number")
    private String taxNumber;

    @Column(name="name")
    private String name;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="created_at")
    private Date createdAt;

    @Column(name="currency")
    private String currency;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="deleted_at")
    private Date deletedAt;

    @Column(name="rate")
    private Double rate;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "charge_id")
    private Charge charge;

    public ChargeTax() {
        super();
    }


    public ChargeTax(String name, String taxNumber, double rate, BigDecimal subTotal)
    {
        this.name = name;
        this.taxNumber = taxNumber;
        this.rate = rate;
        this.amount = subTotal.multiply(new BigDecimal(rate));
        this.amount.setScale(2, RoundingMode.HALF_UP);
        this.amount = new BigDecimal(String.format("%.2f", this.amount));
    }


    public Long getId()
    {
         return id;
    }

    public void setId(Long id)
    {
         this.id = id;
    }


    public BigDecimal getAmount()
    {
         return amount == null ? BigDecimal.ZERO : amount;
    }

    public void setAmount(BigDecimal amount)
    {
         this.amount = amount;
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    public ZonedDateTime getUpdatedAt()
    {
         return asDate(updatedAt);
    }

    public void setUpdatedAt(ZonedDateTime updatedAt)
    {
         this.updatedAt = legacyDate(updatedAt);
    }


    public String getTaxNumber()
    {
         return taxNumber;
    }

    public void setTaxNumber(String taxNumber)
    {
         this.taxNumber = taxNumber;
    }


    public String getName()
    {
         return name;
    }

    public void setName(String name)
    {
         this.name = name;
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    public ZonedDateTime getCreatedAt()
    {
         return asDate(createdAt);
    }

    public void setCreatedAt(ZonedDateTime createdAt)
    {
         this.createdAt = legacyDate(createdAt);
    }


    public String getCurrency()
    {
         return currency;
    }

    public void setCurrency(String currency)
    {
         this.currency = currency;
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    public ZonedDateTime getDeletedAt()
    {
         return asDate(deletedAt);
    }

    public void setDeletedAt(ZonedDateTime deletedAt)
    {
         this.deletedAt = legacyDate(deletedAt);
    }


    public Double getRate()
    {
        return rate;
    }


    public void setRate(Double rate)
    {
        this.rate = rate;
    }


    @JsonIgnore
    public Charge getCharge()
    {
        return charge;
    }


    public void setCharge(Charge charge)
    {
        this.charge = charge;
    }

}
