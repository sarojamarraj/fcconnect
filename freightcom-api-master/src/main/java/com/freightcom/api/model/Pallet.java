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

import org.hibernate.annotations.Formula;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import java.util.Date;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 *
 * @author
 */
@Entity(name="Pallet")
@Table(name = "pallet")
@SQLDelete(sql = "update pallet SET deleted_at = UTC_TIMESTAMP() where id=?")
@Where(clause = "deleted_at is null")
public class Pallet extends TransactionalEntity
{

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dim_height")
    private String dimHeight;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(name = "type_id")
    private Long typeId;

    @Column(name = "insurance")
    private BigDecimal insurance;

    @Column(name = "nmfc_code")
    private String nmfcCode;

    @Column(name = "pieces")
    private Integer pieces;

    @Column(name = "freightclass")
    private String freightclass;

    @Column(name = "dim_width")
    private String dimWidth;

    @Column(name = "weight")
    private String weight;

    @Column(name = "description")
    private String description;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "dim_length")
    private String dimLength;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private CustomerOrder order;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "deleted_at")
    private Date deletedAt;

    @Formula("(select PT.name from pallet_type PT where PT.id=type_id)")
    private String palletType;

    public Pallet()
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

    public String getDimHeight()
    {
        return dimHeight;
    }

    public void setDimHeight(String dimHeight)
    {
        this.dimHeight = dimHeight;
    }

    public String getHeight()
    {
        return dimHeight;
    }

    public void setHeight(String dimHeight)
    {
        this.dimHeight = dimHeight;
    }

    @Override
    @JsonIgnore
    public ZonedDateTime getUpdatedAt()
    {
        return asDate(updatedAt);
    }

    @Override
    public void setUpdatedAt(Date updatedAt)
    {
        this.updatedAt = updatedAt;
    }

    public Long getTypeId()
    {
        return typeId;
    }

    public void setTypeId(Long typeId)
    {
        this.typeId = typeId;
    }

    @JsonProperty(access = Access.READ_ONLY)
    public String getPalletType()
    {
        return palletType == null ? "Pallet" : palletType;
    }

    public void setPalletType(String palletType)
    {
        this.palletType = palletType;
    }

    public String getFreightclass()
    {
        return freightclass;
    }

    public void setFreightclass(String freightclass)
    {
        this.freightclass = freightclass;
    }

    public String getFreightClass()
    {
        return freightclass;
    }

    public void setFreightClass(String freightclass)
    {
        this.freightclass = freightclass;
    }

    public String getDimWidth()
    {
        return dimWidth;
    }

    public void setDimWidth(String dimWidth)
    {
        this.dimWidth = dimWidth;
    }

    public String getWidth()
    {
        return dimWidth;
    }

    public void setWidth(String dimWidth)
    {
        this.dimWidth = dimWidth;
    }

    public String getWeight()
    {
        return weight;
    }

    public void setWeight(String weight)
    {
        this.weight = weight;
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

    public String getDimLength()
    {
        return dimLength;
    }

    public void setDimLength(String dimLength)
    {
        this.dimLength = dimLength;
    }

    public String getLength()
    {
        return dimLength;
    }

    public void setLength(String dimLength)
    {
        this.dimLength = dimLength;
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

    @JsonIgnore
    public ZonedDateTime getDeletedAt()
    {
        return asDate(deletedAt);
    }

    public void setDeletedAt(Date deletedAt)
    {
        this.deletedAt = deletedAt;
    }

    public BigDecimal getInsurance()
    {
        return insurance;
    }

    public void setInsurance(BigDecimal insurance)
    {
        this.insurance = insurance;
    }

    public String getNmfcCode()
    {
        return nmfcCode;
    }

    public void setNmfcCode(String onmfcCode)
    {
        this.nmfcCode = onmfcCode;
    }

    public Integer getPieces()
    {
        return pieces;
    }

    public void setPieces(Integer pieces)
    {
        this.pieces = pieces;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public void setTypeId(int type_id)
    {
        typeId = new Long(type_id);

    }

    public void setOrder(CustomerOrder order)
    {
        this.order = order;
    }

    public void  update(Pallet from)
    {
      setId(from.getId());
      setDimHeight(from.getDimHeight());
      setHeight(from.getHeight());
      setUpdatedAt(from.getUpdatedAt());
      setTypeId(from.getTypeId());
      setPalletType(from.getPalletType());
      setFreightclass(from.getFreightclass());
      setFreightClass(from.getFreightClass());
      setDimWidth(from.getDimWidth());
      setWidth(from.getWidth());
      setWeight(from.getWeight());
      setDimLength(from.getDimLength());
      setLength(from.getLength());
      setInsurance(from.getInsurance());
      setNmfcCode(from.getNmfcCode());
      setPieces(from.getPieces());
      setDescription(from.getDescription());
      setTypeId(from.getTypeId());
    }

    public String toString()
    {
        return "<Pallet " + id + " " + getPalletType() + " " + typeId + ">";
    }
}
