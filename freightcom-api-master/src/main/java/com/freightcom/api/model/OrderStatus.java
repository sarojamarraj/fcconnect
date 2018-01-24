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

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.freightcom.api.model.support.OrderStatusCode;

import javax.persistence.Cacheable;
import javax.persistence.Column;

/**
 *
 *
 * @author
 */
@Entity(name = "OrderStatus")
@Table(name = "order_status")
@Cacheable(value = true)
public class OrderStatus extends TransactionalEntity
{

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "sequence")
    private String sequence;

    public OrderStatus()
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

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getSequence()
    {
        return sequence;
    }

    public void setSequence(String sequence)
    {
        this.sequence = sequence;
    }

    public OrderStatusCode code()
    {
        try {
            return OrderStatusCode.get(id);
        } catch (Exception e) {
            return OrderStatusCode.EXCEPTION;
        }
    }

    public String toString()
    {
        return "OrderStatus " + id + " " + name;
    }
}
