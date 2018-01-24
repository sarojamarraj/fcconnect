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
import java.time.ZonedDateTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

/**
 *
 *
 * @author
 */
@Entity(name = "Markup")
@Table(name = "markup")
@SQLDelete(sql = "update markup SET deleted_at = UTC_TIMESTAMP() where id=?")
@Where(clause = "deleted_at is null")
public class Markup extends TransactionalEntity
{

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "end_at")
    private Date endAt;

    @Column(name = "fixed_amount")
    private BigDecimal fixedAmount;

    @Column(name = "agent_id")
    private Long agentId;

    @Column(name = "accessorial_type_id")
    private Long accessorialTypeId;

    @Column(name = "min_amount")
    private BigDecimal minAmount;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "start_at")
    private Date startAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "deleted_at")
    private Date deletedAt;

    @Column(name = "fraction")
    private Double fraction;

    @Column(name = "calc_from")
    private String calcFrom;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(name = "service_id")
    private Long serviceId;

    @Column(name = "max_amount")
    private BigDecimal maxAmount;

    @Column(name = "package_type_id")
    private Long packageTypeId;

    @Column(name = "customer_id")
    private Long customerId;

    public Markup()
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

    public ZonedDateTime getEndAt()
    {
        return asDate(endAt);
    }

    public void setEndAt(Date endAt)
    {
        this.endAt = endAt;
    }

    public BigDecimal getFixedAmount()
    {
        return fixedAmount;
    }

    public void setFixedAmount(BigDecimal fixedAmount)
    {
        this.fixedAmount = fixedAmount;
    }

    public Long getAgentId()
    {
        return agentId;
    }

    public void setAgentId(Long agentId)
    {
        this.agentId = agentId;
    }

    public Long getAccessorialTypeId()
    {
        return accessorialTypeId;
    }

    public void setAccessorialTypeId(Long accessorialTypeId)
    {
        this.accessorialTypeId = accessorialTypeId;
    }

    public BigDecimal getMinAmount()
    {
        return minAmount;
    }

    public void setMinAmount(BigDecimal minAmount)
    {
        this.minAmount = minAmount;
    }

    public ZonedDateTime getCreatedAt()
    {
        return asDate(createdAt);
    }

    public void setCreatedAt(Date createdAt)
    {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getStartAt()
    {
        return asDate(startAt);
    }

    public void setStartAt(Date startAt)
    {
        this.startAt = startAt;
    }

    public ZonedDateTime getDeletedAt()
    {
        return asDate(deletedAt);
    }

    public void setDeletedAt(Date deletedAt)
    {
        this.deletedAt = deletedAt;
    }

    public Double getFraction()
    {
        return fraction;
    }

    public void setFraction(Double fraction)
    {
        this.fraction = fraction;
    }

    public String getCalcFrom()
    {
        return calcFrom;
    }

    public void setCalcFrom(String calcFrom)
    {
        this.calcFrom = calcFrom;
    }

    public ZonedDateTime getUpdatedAt()
    {
        return asDate(updatedAt);
    }

    public void setUpdatedAt(Date updatedAt)
    {
        this.updatedAt = updatedAt;
    }

    public Long getServiceId()
    {
        return serviceId;
    }

    public void setServiceId(Long serviceId)
    {
        this.serviceId = serviceId;
    }

    public BigDecimal getMaxAmount()
    {
        return maxAmount;
    }

    public void setMaxAmount(BigDecimal maxAmount)
    {
        this.maxAmount = maxAmount;
    }

    public Long getPackageTypeId()
    {
        return packageTypeId;
    }

    public void setPackageTypeId(Long packageTypeId)
    {
        this.packageTypeId = packageTypeId;
    }

    public Long getCustomerId()
    {
        return customerId;
    }

    public void setCustomerId(Long customerId)
    {
        this.customerId = customerId;
    }

}
