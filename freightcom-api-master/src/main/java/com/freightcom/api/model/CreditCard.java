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

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;


/**
 *
 *
 * @author
 */
@Entity(name = "CreditCard")
@Table(name = "credit_card")
public class CreditCard extends TransactionalEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="created_at")
    private Date createdAt;

    @Column(name="active")
    private Boolean active;

    @Column(name="is_default")
    private Boolean isDefault = false;

    @Column(name="type")
    private String type;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="deleted_at")
    private Date deletedAt;

    @Column(name="expiry_year")
    private String expiryYear;

    @Column(name="expiry_month")
    private String expiryMonth;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="updated_at")
    private Date updatedAt;

    @Column(name="name")
    private String name;

    @JsonIgnore
    @OneToOne
    private Customer customer;

    @OneToOne( cascade = { CascadeType.ALL })
    private StoredCreditCard storedCreditCard;

    public CreditCard() {
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

    @JsonFormat(pattern = "yyyy-MM-dd")
    public ZonedDateTime getCreatedAt()
    {
         return asDate(createdAt);
    }

    public void setCreatedAt(Date createdAt)
    {
         this.createdAt = createdAt;
    }


    public Boolean getActive()
    {
         return active;
    }

    public void setActive(Boolean active)
    {
         this.active = active;
    }


    public String getType()
    {
        String type = storedCreditCard == null ? null : storedCreditCard.getCardType();

        return type == null ? this.type : type;
    }

    public void setType(String type)
    {
         this.type = type;
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    public ZonedDateTime getDeletedAt()
    {
         return asDate(deletedAt);
    }

    public void setDeletedAt(Date deletedAt)
    {
         this.deletedAt = deletedAt;
    }


    public String getExpiryYear()
    {
         return expiryYear;
    }

    public void setExpiryYear(String expiryYear)
    {
         this.expiryYear = expiryYear;
    }


    public String getNumber()
    {
        return storedCreditCard == null ? null : storedCreditCard.getMaskedCard();
    }


    public String getExpiryMonth()
    {
         return expiryMonth;
    }

    public void setExpiryMonth(String expiryMonth)
    {
         this.expiryMonth = expiryMonth;
    }


    @JsonFormat(pattern = "yyyy-MM-dd")
    public ZonedDateTime getUpdatedAt()
    {
         return asDate(updatedAt);
    }

    public void setUpdatedAt(Date updatedAt)
    {
         this.updatedAt = updatedAt;
    }


    public String getName()
    {
         return name;
    }

    public void setName(String name)
    {
         this.name = name;
    }

    @JsonIgnore
    public Customer getCustomer()
    {
         return customer;
    }

    @JsonIgnore
    public void setCustomer(Customer customer)
    {
         this.customer = customer;
    }


    public Boolean getIsDefault()
    {
        return isDefault == null ? false : isDefault;
    }


    public void setIsDefault(Boolean isDefault)
    {
        this.isDefault = isDefault;
    }

    public CreditCard update(CreditCard source)
    {
        setActive(source.getActive());
        setType(source.getType());
        setExpiryYear(source.getExpiryYear());
        setExpiryMonth(source.getExpiryMonth());
        setName(source.getName());
        setIsDefault(source.getIsDefault());

        return this;
    }


    public StoredCreditCard getStoredCreditCard()
    {
        return storedCreditCard;
    }


    public void setStoredCreditCard(StoredCreditCard storedCreditCard)
    {
        this.storedCreditCard = storedCreditCard;
    }
}
