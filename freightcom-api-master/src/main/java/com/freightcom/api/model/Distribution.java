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

import java.time.ZonedDateTime;
import java.util.Date;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 *
 * @author
 */
@Entity(name = "Distribution")
@Table(name = "distribution")
public class Distribution extends TransactionalEntity
{

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "distribution_name")
    private String distributionName;

    @Column(name = "batch_id")
    private String batchId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "distribution_charge")
    private Float distributionCharge;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "deleted_at")
    private Date deletedAt;

    @Column(name = "shipment_count")
    private Long shipmentCount;

    @Column(name = "service_type")
    private String serviceType;

    @Column(name = "rate_count")
    private Long rateCount;

    @Column(name = "rate_error_count")
    private Long rateErrorCount;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(name = "service_id")
    private Long serviceId;

    @Column(name = "shipment_error_count")
    private Long shipmentErrorCount;

    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "email")
    private String email;

    @Column(name = "status")
    private String status;

    public Distribution()
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

    public String getDistributionName()
    {
        return distributionName;
    }

    public void setDistributionName(String distributionName)
    {
        this.distributionName = distributionName;
    }

    public String getBatchId()
    {
        return batchId;
    }

    public void setBatchId(String batchId)
    {
        this.batchId = batchId;
    }

    public ZonedDateTime getCreatedAt()
    {
        return asDate(createdAt);
    }

    public void setCreatedAt(Date createdAt)
    {
        this.createdAt = createdAt;
    }

    public Float getDistributionCharge()
    {
        return distributionCharge;
    }

    public void setDistributionCharge(Float distributionCharge)
    {
        this.distributionCharge = distributionCharge;
    }

    public ZonedDateTime getDeletedAt()
    {
        return asDate(deletedAt);
    }

    public void setDeletedAt(Date deletedAt)
    {
        this.deletedAt = deletedAt;
    }

    public Long getShipmentCount()
    {
        return shipmentCount;
    }

    public void setShipmentCount(Long shipmentCount)
    {
        this.shipmentCount = shipmentCount;
    }

    public String getServiceType()
    {
        return serviceType;
    }

    public void setServiceType(String serviceType)
    {
        this.serviceType = serviceType;
    }

    public Long getRateCount()
    {
        return rateCount;
    }

    public void setRateCount(Long rateCount)
    {
        this.rateCount = rateCount;
    }

    public Long getRateErrorCount()
    {
        return rateErrorCount;
    }

    public void setRateErrorCount(Long rateErrorCount)
    {
        this.rateErrorCount = rateErrorCount;
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

    public Long getShipmentErrorCount()
    {
        return shipmentErrorCount;
    }

    public void setShipmentErrorCount(Long shipmentErrorCount)
    {
        this.shipmentErrorCount = shipmentErrorCount;
    }

    public Long getCustomerId()
    {
        return customerId;
    }

    public void setCustomerId(Long customerId)
    {
        this.customerId = customerId;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

}
