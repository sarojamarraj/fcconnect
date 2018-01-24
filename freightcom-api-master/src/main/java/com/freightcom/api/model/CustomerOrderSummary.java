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
import javax.persistence.Transient;

import org.hibernate.annotations.Where;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *
 *
 * @author
 */
@Entity(name = "CustomerOrderSummary")
@Table(name = "customer_order")
@Where(clause = "deleted_at is null")
public class CustomerOrderSummary extends TransactionalEntity implements OwnedByCustomer
{
    @Transient
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id", updatable = false, insertable = false, nullable = true)
    private Customer customer;

    @OneToOne(cascade = { CascadeType.ALL })
    @JoinColumn(name = "ship_to_id", referencedColumnName = "id", nullable = true)
    private ShippingAddress shipTo;

    @OneToOne(cascade = { CascadeType.ALL })
    @JoinColumn(name = "ship_from_id", referencedColumnName = "id", nullable = true)
    private ShippingAddress shipFrom;

    @OneToMany(targetEntity = Package.class, mappedBy = "order", cascade = { CascadeType.ALL })
    private List<Package> packages;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        throw new Error("Not allowed");
    }

    @JsonIgnore
    public Customer getCustomer()
    {
        return customer;
    }

    public void setCustomer(Customer customer)
    {
        throw new Error("Not allowed");
    }

    public ShippingAddress getShipTo()
    {
        return shipTo;
    }

    public ShippingAddress getShipFrom()
    {
        return shipFrom;
    }

}
