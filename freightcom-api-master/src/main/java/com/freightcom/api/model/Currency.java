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

/**
 * 
 *
 * @author
 */
@Entity(name = "Currency")
@Table(name = "currency")
public class Currency extends TransactionalEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id") 
    private Long id;

    @Column(name="name") 
    private String name;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="created_at") 
    private Date createdAt;

    public Currency() {
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


    public String getName()
    {
         return name; 
    }

    public void setName(String name)
    {
         this.name = name; 
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

}