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
@Entity(name = "SentEmail")
@Table(name = "sent_email")
public class SentEmail extends TransactionalEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="from_email")
    private String from;

    @Column(name="subject")
    private String subject;

    @Column(name="to_email")
    private String to;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="created_at")
    private Date createdAt;

    @Column(name="message")
    private String message;

    public SentEmail() {
        super();
    }

    public SentEmail(String from, String to, String subject, String message) {
        super();
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.message = message;
    }


    public Long getId()
    {
         return id;
    }

    public void setId(Long id)
    {
         this.id = id;
    }


    public String getFrom()
    {
         return from;
    }

    public void setFrom(String from)
    {
         this.from = from;
    }


    public String getSubject()
    {
         return subject;
    }

    public void setSubject(String subject)
    {
         this.subject = subject;
    }


    public String getTo()
    {
         return to;
    }

    public void setTo(String to)
    {
         this.to = to;
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


    public String getMessage()
    {
         return message;
    }

    public void setMessage(String message)
    {
         this.message = message;
    }

}
