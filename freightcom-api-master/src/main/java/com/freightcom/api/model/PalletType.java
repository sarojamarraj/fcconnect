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
@Entity(name = "PalletType")
@Table(name = "pallet_type")
public class PalletType extends TransactionalEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="updated_at")
    private Date updatedAt;

    @Column(name="name")
    private String name;

    @Column(name="description")
    private String description;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="created_at")
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="deleted_at")
    private Date deletedAt;

    public PalletType() {
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

    @JsonIgnore
    public ZonedDateTime getUpdatedAt()
    {
         return asDate(updatedAt);
    }

    public void setUpdatedAt(Date updatedAt)
    {
         this.updatedAt = legacyDate(updatedAt);
    }


    public String getName()
    {
         return name;
    }

    public void setName(String name)
    {
         this.name = name;
    }


    public String getDescription()
    {
         return description;
    }

    public void setDescription(String description)
    {
         this.description = description;
    }

    @JsonIgnore
    public ZonedDateTime getCreatedAt()
    {
         return asDate(createdAt);
    }

    public void setCreatedAt(Date createdAt)
    {
         this.createdAt = legacyDate(createdAt);
    }

    @JsonIgnore
    public ZonedDateTime getDeletedAt()
    {
         return asDate(deletedAt);
    }

    public void setDeletedAt(Date deletedAt)
    {
         this.deletedAt = legacyDate(deletedAt);
    }

}
