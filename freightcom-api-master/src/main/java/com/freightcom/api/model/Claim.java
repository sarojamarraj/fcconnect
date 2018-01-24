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
import java.util.Arrays;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Formula;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *
 *
 * @author
 */
@Entity(name = "Claim")
@Table(name = "claim")
public class Claim extends TransactionalEntity
{

    private static final long serialVersionUID = 1L;

    public enum Status {
        NEW("NEW_CLAIM"), REVIEWED("CLAIM_REVIEWED"), IN_PROCESS("CLAIM_IN_PROCESS"), DENIED("CLAIM_DENIED"), SETTLED(
                "CLAIM_SETTLED");

        private String value;

        Status(String value)
        {
            this.value = value;
        }

        Status()
        {
            this.value = name();
        }

        public String getValue()
        {
            return value;
        }

        public String toString()
        {
            return value;
        }

        public static Status fromString(String key)
        {
            return Arrays.stream(Status.values())
                    .filter(status -> status.value.equals(key))
                    .findFirst()
                    .orElse(null);
        }
    };

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "logged_event_id")
    private Long loggedEventId;

    @Column(name = "currency")
    private String currency;

    @Column(name = "reason")
    private String reason;

    @ManyToOne
    @JoinColumn(name = "submitted_by_id")
    private UserRole submittedBy;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status = Status.NEW;

    @Column(name = "amount")
    private BigDecimal amount = BigDecimal.ZERO;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modified_at")
    private Date modifiedAt;

    @OneToOne
    @JoinColumn(name = "created_by_role_id", referencedColumnName = "id")
    private UserRole createdByRole;

    @Formula("(DATEDIFF(current_date(), created_at))")
    private Integer ageInDays = 0;

    public Claim()
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

    public Long getLoggedEventId()
    {
        return loggedEventId;
    }

    public void setLoggedEventId(Long loggedEventId)
    {
        this.loggedEventId = loggedEventId;
    }

    public Status getStatus()
    {
        return status;
    }

    public void setStatus(Status status)
    {
        this.status = status;
    }

    public BigDecimal getAmount()
    {
        return amount;
    }

    public void setAmount(BigDecimal amount)
    {
        this.amount = amount;
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
    public ZonedDateTime getModifiedAt()
    {
        return asDate(modifiedAt);
    }

    public void setModifiedAt(Date modifiedAt)
    {
        this.modifiedAt = legacyDate(modifiedAt);
    }

    public UserRole getSubmittedBy()
    {
        return submittedBy;
    }

    public void setSubmittedBy(UserRole submittedBy)
    {
        this.submittedBy = submittedBy;
    }

    public Integer getAgeInDays()
    {
        return ageInDays;
    }

    @JsonIgnore
    public UserRole getCreatedByRole()
    {
        return createdByRole;
    }

    public void setCreatedByRole(UserRole createdByRole)
    {
        this.createdByRole = createdByRole;
    }

    public boolean isResolved()
    {
        return status == Status.DENIED || status == Status.SETTLED;
    }

    public String toString()
    {
        return "CLAIM " + id + " " + submittedBy + " " + createdByRole;
    }

    public String getCurrency()
    {
        return currency;
    }

    public void setCurrency(String currency)
    {
        this.currency = currency;
    }

    public String getReason()
    {
        return reason;
    }

    public void setReason(String reason)
    {
        this.reason = reason;
    }
}
