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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *
 *
 * @author
 */
@Entity(name = "Carrier")
@Table(name = "carrier")
@Cacheable(value = true)
public class Carrier extends TransactionalEntity
{
    private static final long serialVersionUID = 1L;

    @Transient
    private final Logger log = LoggerFactory.getLogger(this.getClass());


    public enum Term { DAILY, WEEKLY, BIWEEKLY, MONTHLY, UNSPECIFIED };

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dba")
    private String dba;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(name = "implementing_class")
    private String implementingClass;

    @Column(name = "name")
    private String name;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "type")
    private Integer type;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "deleted_at")
    private Date deletedAt;

    @Column(name = "term")
    private String term;

    @OneToMany(cascade=CascadeType.ALL, orphanRemoval = true, mappedBy = "carrier")
    private List<Service> services = new ArrayList<Service>();

    public Carrier()
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

    public String getDba()
    {
        return dba;
    }

    public void setDba(String dba)
    {
        this.dba = dba;
    }

    public ZonedDateTime getUpdatedAt()
    {
        return asDate(updatedAt);
    }

    public void setUpdatedAt(Date updatedAt)
    {
        this.updatedAt = updatedAt;
    }

    public String getImplementingClass()
    {
        return implementingClass;
    }

    public void setImplementingClass(String implementingClass)
    {
        this.implementingClass = implementingClass;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
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

    public Term getTerm()
    {
        return term == null ? Term.UNSPECIFIED : Term.valueOf(term);
    }

    public void setTerm(String term)
    {
        this.term = term;
    }

    public String toString()
    {
        return "Carrier " + id + " " + name;
    }

    @JsonIgnore
    public List<Service> getServices()
    {
        return services;
    }

    public void setServices(List<Service> services)
    {
        this.services = services;
    }

    public Service findService(String code, String name, String description)
    {
        Service match = null;

        for (Service service: getServices()) {
            log.debug("CARRIER SERVICE " + code + " " + service.getCode() + " " + service.getDiscontinued());
            if (service.getDiscontinued() != 1 && service.getCode() != null && service.getCode().equals(code)) {
                match = service;
                break;
            }
        }

        return match;
    }

}
