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

import java.time.ZonedDateTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Entity(name = "CustomerBilling")
@Table(name = "customer_billing")
public class CustomerBilling extends TransactionalEntity
{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JsonIgnore
    @OneToOne(fetch=FetchType.LAZY)
    private Customer customer;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_invoiced")
    private Date lastInvoiced;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_charged")
    private Date lastCharged;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_invoice_run")
    private Date lastInvoiceRun;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_charge_run")
    private Date lastChargeRun;

    public CustomerBilling()
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

    @JsonFormat(pattern = "yyyy-MM-dd")
    public ZonedDateTime getLastInvoiced()
    {
        return asDate(lastInvoiced);
    }

    public void setLastInvoiced(final ZonedDateTime lastInvoiced)
    {
        this.lastInvoiced = legacyDate(lastInvoiced);
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    public ZonedDateTime getLastCharged()
    {
        return asDate(lastCharged);
    }

    public void setLastCharged(final ZonedDateTime lastCharged)
    {
        this.lastCharged = legacyDate(lastCharged);
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    public ZonedDateTime getLastInvoiceRun()
    {
        return asDate(lastInvoiceRun);
    }

    public void setLastInvoiceRun(final ZonedDateTime lastInvoiceRun)
    {
        this.lastInvoiceRun = legacyDate(lastInvoiceRun);
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    public ZonedDateTime getLastChargeRun()
    {
        return asDate(lastChargeRun);
    }

    public void setLastChargeRun(final ZonedDateTime lastChargeRun)
    {
        this.lastChargeRun = legacyDate(lastChargeRun);
    }

    public Customer getCustomer()
    {
        return customer;
    }

    public void setCustomer(Customer customer)
    {
        this.customer = customer;
    }

}
