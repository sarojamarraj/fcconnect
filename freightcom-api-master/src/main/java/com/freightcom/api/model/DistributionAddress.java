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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.rest.core.annotation.RestResource;

/**
 *
 *
 * @author
 */
@Entity(name = "DistributionAddress")
@Table(name = "distribution_address")
public class DistributionAddress extends TransactionalEntity
{

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "distribution_group_id")
    private Long distributionGroupId;

    @Column(name = "shipto_id")
    private Long shiptoId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "deleted_at")
    private Date deletedAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;

    @OneToOne
    @JoinColumn(name = "shipto_id", referencedColumnName = "id", updatable = false, insertable = false, nullable = true)
    private ShippingAddress address;

    @Column(name = "has_error")
    private Boolean hasError;

    public DistributionAddress()
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

    public Long getDistributionGroupId()
    {
        return distributionGroupId;
    }

    public void setDistributionGroupId(Long distributionGroupId)
    {
        this.distributionGroupId = distributionGroupId;
    }

    public Long getShiptoId()
    {
        return shiptoId;
    }

    public void setShiptoId(Long shiptoId)
    {
        this.shiptoId = shiptoId;
    }

    public ZonedDateTime getCreatedAt()
    {
        return asDate(createdAt);
    }

    public void setCreatedAt(Date createdAt)
    {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getDeletedAt()
    {
        return asDate(deletedAt);
    }

    public void setDeletedAt(Date deletedAt)
    {
        this.deletedAt = deletedAt;
    }

    public ZonedDateTime getUpdatedAt()
    {
        return asDate(updatedAt);
    }

    public void setUpdatedAt(Date updatedAt)
    {
        this.updatedAt = updatedAt;
    }

    @RestResource(exported = false)
    public ShippingAddress getAddress()
    {
        return address;
    }

    public void setHasError(Boolean hasError)
    {
        this.hasError = hasError;
    }

    public Boolean getHasError()
    {
        return hasError != null && hasError;
    }

}
