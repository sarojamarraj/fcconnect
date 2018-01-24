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

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *
 *
 * @author
 */
@Entity(name = "PackageType")
@Table(name = "package_type")
@Cacheable(value = true)
public class PackageType extends TransactionalEntity
{
    private static final long serialVersionUID = 1L;

    public enum Type {
        PACKAGE_ENV("env"), PACKAGE_PAK("pak"), PACKAGE_PACKAGE("package"), PACKAGE_PALLET("pallet");

        private final String value;

        Type(final String value)
        {
            this.value = value;
        }

        public String getValue()
        {
            return value;
        }

        public static Type fromString(String value) throws Exception
        {
           Type type = null;

           for (Type candidate: Type.values()) {
               if (candidate.getValue().equalsIgnoreCase(value)) {
                   type = candidate;
                   break;
               }
           }

           if (type == null) {
               throw new Exception("No such package type " + value);
           }

           return type;
        }
    };

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(name = "markup_id")
    private Long markupId;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "type")
    private Integer type;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "deleted_at")
    private Date deletedAt;

    public PackageType()
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

    public ZonedDateTime getUpdatedAt()
    {
        return asDate(updatedAt);
    }

    public void setUpdatedAt(Date updatedAt)
    {
        this.updatedAt = updatedAt;
    }

    public Long getMarkupId()
    {
        return markupId;
    }

    public void setMarkupId(Long markupId)
    {
        this.markupId = markupId;
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

    public ZonedDateTime getCreatedAt()
    {
        return asDate(createdAt);
    }

    public void setCreatedAt(Date createdAt)
    {
        this.createdAt = createdAt;
    }

    public Integer getType()
    {
        return type;
    }

    public void setType(Integer type)
    {
        this.type = type;
    }

    public ZonedDateTime getDeletedAt()
    {
        return asDate(deletedAt);
    }

    public void setDeletedAt(Date deletedAt)
    {
        this.deletedAt = deletedAt;
    }

    public boolean isLetter()
    {
        return Type.PACKAGE_ENV.toString()
                .equals(name);
    }

    public boolean isPak()
    {
        return Type.PACKAGE_PAK.toString()
                .equals(name);
    }

    public boolean isPackage()
    {
        return Type.PACKAGE_PACKAGE.toString()
                .equals(name);
    }

    public boolean isLTL()
    {
        return Type.PACKAGE_PALLET.toString()
                .equals(name);
    }

    @JsonIgnore
    public Type getTypeCode() throws Exception
    {
        return Type.fromString(name);
    }

    @Override
    public String toString()
    {
        return "Package Type " + id + ": " + name;
    }
}
