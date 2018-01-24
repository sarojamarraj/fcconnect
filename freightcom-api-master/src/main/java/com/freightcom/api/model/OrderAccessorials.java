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

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *
 *
 * @author
 */
@Entity(name = "OrderAccessorials")
@Table(name = "order_accessorials")
public class OrderAccessorials extends TransactionalEntity
{
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = { CascadeType.REFRESH })
    @JoinColumn(name = "accessorial_id")
    private AccessorialServices service;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private CustomerOrder order;

    public OrderAccessorials()
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

    public Long getOrderId()
    {
        return this.order != null ? this.order.getId() : null;
    }

    public Long getAccessorialId()
    {
        return getService() != null ? getService().getId() : null;
    }

    public String getAccessorialName()
    {
        return getService() != null ? getService().getName() : null;
    }

    public String toString()
    {
        return "[OA " + getId() + " " + getOrderId() + " " + getAccessorialId() + "]";
    }

    @JsonIgnore
    public AccessorialServices getService()
    {
        return service;
    }

    public void setService(AccessorialServices service)
    {
        this.service = service;
    }

    public Long getAccessorialServiceId()
    {
        return service == null ? null : service.getId();
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
}
