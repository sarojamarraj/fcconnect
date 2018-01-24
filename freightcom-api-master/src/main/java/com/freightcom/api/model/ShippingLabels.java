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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *
 *
 * @author
 */
@Entity(name = "ShippingLabels")
@Table(name = "shipping_labels")
public class ShippingLabels extends TransactionalEntity
{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "label_type")
    private Integer labelType;

    @Column(name = "label")
    private byte[] label;

    @JsonIgnore
    @OneToOne
    private CustomerOrder order;

    public ShippingLabels()
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

    public Integer getLabelType()
    {
        return labelType;
    }

    public void setLabelType(Integer labelType)
    {
        this.labelType = labelType;
    }

    public byte[] getLabel()
    {
        return label;
    }

    public void setLabel(byte[] label)
    {
        this.label = label;
    }

    public CustomerOrder getOrder()
    {
        return order;
    }

    public void setOrder(CustomerOrder order)
    {
        this.order = order;
    }

}
