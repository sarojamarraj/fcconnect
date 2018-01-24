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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

/**
 *
 *
 * @author
 */
@Entity(name = "Package")
@Table(name = "package")
@SQLDelete(sql = "update package SET deleted_at = UTC_TIMESTAMP() where id=?")
@Where(clause = "deleted_at is null")
public class Package extends TransactionalEntity
{

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "description")
    private String description;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "type")
    private String type;

    @Column(name = "nmfc_code")
    private String nmfcCode;

    @Column(name = "cod_amount")
    private String codAmount;

    @Column(name = "pieces")
    private Long pieces;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(name = "tracking_number")
    private String trackingNumber;

    @Column(name = "dim_length")
    private String dimLength;

    @Column(name = "height")
    private Integer height;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "cubed_weight")
    private Integer cubedWeight;

    @Column(name = "type_id")
    private Long typeId;

    @Column(name = "length")
    private Integer length;

    @Column(name = "weight")
    private String weight;

    @Column(name = "old_order_id")
    private Long oldOrderId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "deleted_at")
    private Date deletedAt;

    @Column(name = "insurance_amount")
    private BigDecimal insuranceAmount;

    @Column(name = "dim_height")
    private String dimHeight;

    @Column(name = "dim_width")
    private String dimWidth;

    @Column(name = "width")
    private Integer width;

    @Column(name = "position")
    private Integer position;

    @Column(name = "cod_value")
    private BigDecimal codValue;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private CustomerOrder order;

    @Column(name = "freight_class")
    private String freightClass;

    public Package()
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

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    @Override
    @JsonFormat(pattern = "yyyy-MM-dd")
    public ZonedDateTime getCreatedAt()
    {
        return asDate(createdAt);
    }

    @Override
    public void setCreatedAt(Date createdAt)
    {
        this.createdAt = createdAt;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getNmfcCode()
    {
        return nmfcCode;
    }

    public void setNmfcCode(String nmfcCode)
    {
        this.nmfcCode = nmfcCode;
    }

    public String getCodAmount()
    {
        return codAmount;
    }

    public void setCodAmount(String codAmount)
    {
        this.codAmount = codAmount;
    }

    public Long getPieces()
    {
        return pieces;
    }

    public void setPieces(Long pieces)
    {
        this.pieces = pieces;
    }

    @Override
    @JsonFormat(pattern = "yyyy-MM-dd")
    public ZonedDateTime getUpdatedAt()
    {
        return asDate(updatedAt);
    }

    @Override
    public void setUpdatedAt(Date updatedAt)
    {
        this.updatedAt = updatedAt;
    }

    public String getTrackingNumber()
    {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber)
    {
        this.trackingNumber = trackingNumber;
    }

    public String getDimLength()
    {
        return dimLength;
    }

    public void setDimLength(String dimLength)
    {
        this.dimLength = dimLength;
    }

    public Integer getHeight()
    {
        return height;
    }

    public void setHeight(Integer height)
    {
        this.height = height;
    }

    public Integer getQuantity()
    {
        return quantity;
    }

    public void setQuantity(Integer quantity)
    {
        this.quantity = quantity;
    }

    public Integer getCubedWeight()
    {
        return cubedWeight;
    }

    public void setCubedWeight(Integer cubedWeight)
    {
        this.cubedWeight = cubedWeight;
    }

    public Long getTypeId()
    {
        return typeId;
    }

    public void setTypeId(Long typeId)
    {
        this.typeId = typeId;
    }

    public Integer getLength()
    {
        return length;
    }

    public void setLength(Integer length)
    {
        this.length = length;
    }

    public String getWeight()
    {
        return weight;
    }

    public void setWeight(String weight)
    {
        this.weight = weight;
    }

    public Long getOldOrderId()
    {
        return oldOrderId;
    }

    public void setOldOrderId(Long oldOrderId)
    {
        this.oldOrderId = oldOrderId;
    }

    @JsonIgnore
    public ZonedDateTime getDeletedAt()
    {
        return asDate(deletedAt);
    }

    public void setDeletedAt(Date deletedAt)
    {
        this.deletedAt = deletedAt;
    }

    public BigDecimal getInsuranceAmount()
    {
        return insuranceAmount;
    }

    public void setInsuranceAmount(BigDecimal insuranceAmount)
    {
        this.insuranceAmount = insuranceAmount;
    }

    public String getDimHeight()
    {
        return dimHeight;
    }

    public void setDimHeight(String dimHeight)
    {
        this.dimHeight = dimHeight;
    }

    public String getDimWidth()
    {
        return dimWidth;
    }

    public void setDimWidth(String dimWidth)
    {
        this.dimWidth = dimWidth;
    }

    public Integer getWidth()
    {
        return width;
    }

    public void setWidth(Integer width)
    {
        this.width = width;
    }

    public Integer getPosition()
    {
        return position;
    }

    public void setPosition(Integer position)
    {
        this.position = position;
    }

    public BigDecimal getCodValue()
    {
        return codValue;
    }

    public void setCodValue(BigDecimal codValue)
    {
        this.codValue = codValue;
    }

    @JsonIgnore
    public CustomerOrder getOrder()
    {
        return order;
    }

    public Long getOrderId()
    {
        return this.order != null ? this.order.getId() : null;
    }

    public String getFreightClass()
    {
        return freightClass;
    }

    public void setFreightClass(String freightClass)
    {
        this.freightClass = freightClass;
    }

    public void setOrder(CustomerOrder order)
    {
        this.order = order;
    }

    public void update(Package from)
    {
        // TODO - all fields
        setLength(from.getLength());
        setHeight(from.getHeight());
        setWidth(from.getWidth());
        setWeight(from.getWeight());
        setDescription(from.getDescription());
        setType(from.getType());
        setNmfcCode(from.getNmfcCode());
        setCodAmount(from.getCodAmount());
        setPieces(from.getPieces());
        setTrackingNumber(from.getTrackingNumber());
        setQuantity(from.getQuantity());
        setCubedWeight(from.getCubedWeight());
        setDimLength(from.getDimLength());
        setDimHeight(from.getDimHeight());
        setDimWidth(from.getDimWidth());
        setInsuranceAmount(from.getInsuranceAmount());
        setCodValue(from.getCodValue());
        setFreightClass(from.getFreightClass());
    }

    public String toString()
    {
        StringBuilder buffer = new StringBuilder();

        return buffer.append("PACKAGE ")
                .append(id)
                .append(' ')
                .append(description)
                .toString();
    }

}
