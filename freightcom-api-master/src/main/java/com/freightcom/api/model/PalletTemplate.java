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
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 *
 *
 * @author
 */
@Entity(name = "PalletTemplate")
@Table(name = "pallet_template")
@SQLDelete(sql = "update pallet SET deleted_at = UTC_TIMESTAMP() where id=?")
@Where(clause = "deleted_at is null")
public class PalletTemplate extends TransactionalEntity {

    private static final long serialVersionUID = 1L;

    @Transient
    private Long palletTypeId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @ManyToOne
    private Customer customer;

    @ManyToOne
    private PalletType palletType;

    @Column(name="name")
    private String name;

    @Column(name="length")
    private Integer length;

    @Column(name="height")
    private Integer height;

    @Column(name="width")
    private Integer width;

    @Column(name="pieces")
    private Integer pieces;

    @Column(name="weight")
    private Double weight;

    @Column(name="description")
    private String description;

    @Column(name="type_id")
    private String typeId;

    @Column(name="insurance")
    private BigDecimal insurance;

    @Column(name="nmfc_code")
    private String nmfcCode;

    @Column(name="freightclass")
    private String freightclass;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="created_at")
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="updated_at")
    private Date updatedAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="deleted_at")
    private Date deletedAt;

    public PalletTemplate() {
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


    public Customer getCustomer()
    {
         return customer;
    }

    public void setCustomer(Customer customer)
    {
         this.customer = customer;
    }


    public String getName()
    {
         return name;
    }

    public void setName(String name)
    {
         this.name = name;
    }


    public Integer getLength()
    {
         return length;
    }

    public void setLength(Integer length)
    {
         this.length = length;
    }


    public Integer getHeight()
    {
         return height;
    }

    public void setHeight(Integer height)
    {
         this.height = height;
    }


    public Integer getWidth()
    {
         return width;
    }

    public void setWidth(Integer width)
    {
         this.width = width;
    }


    public Integer getPieces()
    {
         return pieces;
    }

    public void setPieces(Integer pieces)
    {
         this.pieces = pieces;
    }


    public Double getWeight()
    {
         return weight;
    }

    public void setWeight(Double weight)
    {
         this.weight = weight;
    }


    public String getDescription()
    {
         return description;
    }

    public void setDescription(String description)
    {
         this.description = description;
    }


    public String getTypeId()
    {
         return typeId;
    }

    public void setTypeId(String typeId)
    {
         this.typeId = typeId;
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

    public void setNmfcCode(String nmfcCode)
    {
         this.nmfcCode = nmfcCode;
    }


    public String getFreightclass()
    {
         return freightclass;
    }

    public void setFreightclass(String freightclass)
    {
         this.freightclass = freightclass;
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    public ZonedDateTime getCreatedAt()
    {
         return asDate(createdAt);
    }

    public void setCreatedAt(Date createdAt)
    {
         this.createdAt = legacyDate(createdAt);
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    public ZonedDateTime getUpdatedAt()
    {
         return asDate(updatedAt);
    }

    public void setUpdatedAt(Date updatedAt)
    {
         this.updatedAt = legacyDate(updatedAt);
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    public ZonedDateTime getDeletedAt()
    {
         return asDate(deletedAt);
    }

    public void setDeletedAt(Date deletedAt)
    {
         this.deletedAt = legacyDate(deletedAt);
    }


    public PalletType getPalletType()
    {
        return palletType;
    }


    public void setPalletType(PalletType palletType)
    {
        this.palletType = palletType;
    }


    public Long getPalletTypeId()
    {
        return palletTypeId;
    }


    public void setPalletTypeId(Long palletTypeId)
    {
        this.palletTypeId = palletTypeId;
    }

}
