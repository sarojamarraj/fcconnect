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
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Formula;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

/**
 *
 *
 * @author
 */
@Entity(name = "UserRole")
@Table(name = "user_role")
@Inheritance(strategy = InheritanceType.JOINED)
@SQLDelete(sql = "update user_role SET deleted_at = UTC_TIMESTAMP() where id=?")
@Where(clause = "deleted_at is null")
@DiscriminatorColumn(name = "role_name")
@Cacheable(value = true)
public abstract class UserRole extends TransactionalEntity
{

    private static final long serialVersionUID = 1L;

    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_FREIGHTCOM_STAFF = "FREIGHTCOM_STAFF";
    public static final String ROLE_CUSTOMER_ADMIN = "CUSTOMER_ADMIN";
    public static final String ROLE_CUSTOMER_STAFF = "CUSTOMER_STAFF";
    public static final String ROLE_AGENT = "AGENT";
    public static final String ROLE_SUPER_ADMIN = "ROLE_ADMIN";

    @Transient
    Long userId = null;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(cascade = CascadeType.REFRESH)
    private User user;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm")
    @JsonProperty(access = Access.READ_ONLY)
    private Date createdAt = null;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "deleted_at")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm")
    @JsonProperty(access = Access.READ_ONLY)
    private Date deletedAt = null;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    @JsonProperty(access = Access.READ_ONLY)
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm")
    private Date updatedAt = null;

    @Formula("(select IF(AGE.agent_name is null or AGE.agent_name='', (select concat(AGU.firstname, ' ', AGU.lastname) from user AGU where AGU.id=user_id), AGE.agent_name) from agent AGE where AGE.id=id)")
    private String name;

    @Formula("(select user_role_phone(id))")
    private String phone;


    public UserRole()
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

    public Long getUserId()
    {
        if (userId != null) {
            // transient user id set by someone
            return userId;
        } else if (getUser() != null) {
            return getUser().getId();
        } else {
            return null;
        }
    }

    public abstract String getRoleName();

    public abstract String getPrettyRoleName();

    public ZonedDateTime getCreatedAt()
    {
        return asDate(createdAt);
    }

    public void setCreatedAt(Date createdAt)
    {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getDeletedAt()
    {
        return asDate(deletedAt);
    }

    public void setDeletedAt(Date deletedAt)
    {
        this.deletedAt = deletedAt;
    }

    public ZonedDateTime getUpdatedAt()
    {
        return asDate(updatedAt);
    }

    public void setUpdatedAt(Date updatedAt)
    {
        this.updatedAt = updatedAt;
    }

    public String getCustomerName()
    {
        return null;
    }

    public Long getCustomerId()
    {
        return null;
    }

    public boolean sameCustomer(Customer customer)
    {
        return false;
    }

    public boolean sameCustomer(UserRole uploadedByRole)
    {
        return false;
    }


    public boolean isCustomer()
    {
        return false;
    }

    public boolean isAdmin()
    {
        return false;
    }

    public boolean isFreightcomStaff()
    {
        return false;
    }

    public boolean isFreightcom()
    {
        return isAdmin() || isFreightcomStaff();
    }

    public boolean isCustomerAdmin()
    {
        return false;
    }

    public boolean isSuperAdmin()
    {
        return false;
    }

    public String toString()
    {
        return "UR " + id + " " + getRoleName() + " " + getUserId();
    }

    public boolean isAgent()
    {
        return false;
    }

    public Long getParentSalesAgentId()
    {
        return null;
    }

    public Agent asAgent()
    {
        return (Agent) null;
    }

    public boolean canView(UserRole userRole)
    {
        if (this.getUserId()
                .equals(userRole.getUserId())) {
            // Can always see own role
            return true;
        } else if (isAdmin()) {
            // Admin sees all
            return true;
        } else if (userRole.isAdmin()) {
            // Nobody else can see admin
            return false;
        } else if (isFreightcomStaff()) {
            // freightcom staff sees all other roles
            return true;
        } else if (isCustomerAdmin() && getCustomerId() != null) {
            return getCustomerId().equals(userRole.getCustomerId());
        } else if (isAgent() && userRole.getParentSalesAgentId() != null) {
            return userRole.getParentSalesAgentId()
                    .equals(getId());
        } else {
            return false;
        }
    }

    public boolean isCustomerStaff()
    {
        return false;
    }

    @JsonIgnore
    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public String getName()
    {
        return name;
    }

    public String getPhone()
    {
        return phone;
    }

    public Agent findRootAgent()
    {
        return null;
    }

    public boolean canManageDisputes()
    {
        return false;
    }

    public boolean canViewCost()
    {
        return false;
    }

    public boolean canManageClaims()
    {
        return false;
    }

    public Boolean canEnterPayments()
    {
        return false;
    }

    public Boolean canGenerateInvoices()
    {
        return false;
    }

    @SuppressWarnings("unchecked")
    public  <T extends UserRole> T  updateFrom(T role) throws Exception
    {
        return (T) this;
    }

    public String getJSONPermissions()
    {
        StringBuilder result = new StringBuilder();

        result.append("{")
            .append("\"canManageDisputes\": ")
            .append(canManageDisputes() ? "true" : "false")
            .append(", \"canEnterPayments\": ")
            .append(canEnterPayments() ? "true" : "false")
            .append(", \"canGenerateInvoices\": ")
            .append(canGenerateInvoices() ? "true" : "false")
            .append(", \"canManageClaims\": ")
            .append(canManageClaims() ? "true" : "false")
            .append("}");

        return result.toString();
    }

    public boolean isAgentFor(Customer customer)
    {
        return false;
    }

    public boolean canManageCredits()
    {
        return false;
    }
}
