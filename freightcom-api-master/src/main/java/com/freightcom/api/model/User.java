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
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Formula;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.hateoas.core.Relation;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.freightcom.api.util.BCryptPasswordDeserializer;

/**
 *
 *
 * @author
 */
@Entity(name = "User")
@Table(name = "user")
@SQLDelete(sql = "update user SET deleted_at = UTC_TIMESTAMP() where id=?")
@Where(clause = "deleted_at is null")
@Relation(value = "user", collectionRelation = "user")
@Cacheable(value = true)
public class User extends TransactionalEntity
{

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "firstname")
    private String firstname;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "password_expiry")
    private Date passwordExpiry;

    @Column(name = "new_password_required")
    private Integer newPasswordRequired;

    @Column(name = "login")
    private String login;

    @Column(name = "type")
    private String type;

    @Column(name = "cell")
    private String cell;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "deleted_at")
    private Date deletedAt;

    @Column(name = "enabled")
    private Boolean enabled = true;

    @Column(name = "lastname")
    private String lastname;

    @Column(name = "password")
    @JsonDeserialize(using = BCryptPasswordDeserializer.class)
    private String password;

    @Column(name = "deleted")
    private Boolean deleted = false;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(name = "phone")
    private String phone;

    @Column(name = "sub_type")
    private String subType;

    @Column(name = "fax")
    private String fax;

    @Column(name = "email")
    private String email;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_login")
    private Date lastLogin;

    @OneToMany(targetEntity = UserRole.class, cascade = {
            CascadeType.ALL }, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private Collection<UserRole> roles = new ArrayList<UserRole>(0);

    @Formula("(concat(ifnull(firstname,''), ' ', ifnull(lastname,'')))")
    private String name = "";

    public User()
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

    public String getFirstname()
    {
        return firstname;
    }

    public void setFirstname(String firstname)
    {
        this.firstname = firstname;
    }

    @Override
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm")
    public ZonedDateTime getCreatedAt()
    {
        return asDate(createdAt);
    }

    public void setCreatedAt(Date createdAt)
    {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getPasswordExpiry()
    {
        return asDate(passwordExpiry);
    }

    public void setPasswordExpriy(Date createdAt)
    {
        this.passwordExpiry = createdAt;
    }

    public String getLogin()
    {
        return login;
    }

    public void setLogin(String login)
    {
        this.login = login;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public Boolean getFirstTimeLogin()
    {
        return lastLogin == null;
    }

    public String getCell()
    {
        return cell;
    }

    public void setCell(String cell)
    {
        this.cell = cell;
    }

    public ZonedDateTime getDeletedAt()
    {
        return asDate(deletedAt);
    }

    public void setDeletedAt(Date deletedAt)
    {
        this.deletedAt = deletedAt;
    }

    public boolean getEnabled()
    {
        return enabled;
    }

    public void setEnabled(Boolean enabled)
    {
        this.enabled = enabled;
    }

    public String getLastname()
    {
        return lastname;
    }

    public void setLastname(String lastname)
    {
        this.lastname = lastname;
    }

    @JsonIgnore
    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public Boolean getDeleted()
    {
        return deleted;
    }

    public void setDeleted(Boolean deleted)
    {
        this.deleted = deleted;
    }

    public ZonedDateTime getUpdatedAt()
    {
        return asDate(updatedAt);
    }

    public void setUpdatedAt(Date updatedAt)
    {
        this.updatedAt = updatedAt;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getSubType()
    {
        return subType;
    }

    public void setSubType(String subType)
    {
        this.subType = subType;
    }

    public Boolean getFirstLoginComplete()
    {
        return lastLogin != null;
    }

    public String getFax()
    {
        return fax;
    }

    public void setFax(String fax)
    {
        this.fax = fax;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String toString()
    {
        return "User " + login + " " + id;
    }

    public Collection<UserRole> getAuthorities()
    {
        if (roles == null) {
            roles = new ArrayList<UserRole>(1);
        }

        return roles;
    }

    public boolean hasAuthority(String name)
    {
        return null != getAuthorities().stream()
                .filter(role -> role.getRoleName()
                        .equals(name))
                .findFirst()
                .orElse(null);
    }

    public UserRole roleForCustomer(Customer customer)
    {
        if (customer == null) {
            return null;
        }

        return getAuthorities().stream()
                .filter(role -> customer.getId()
                        .equals(role.getCustomerId()))
                .findFirst()
                .orElse(null);
    }

    public void setAuthorities(Collection<UserRole> authorities)
    {
        this.roles = authorities;
    }

    public void addAuthority(UserRole role) throws Exception
    {
        if (getAuthorities() == null) {
            Collection<UserRole> authorities = new ArrayList<UserRole>(1);
            authorities.add(role);
            setAuthorities(authorities);
        } else {
            List<UserRole> matches = getAuthorities().stream()
                    .filter(item -> item.equals(role))
                    .collect(Collectors.toList());

            if (matches.size() > 0) {
                matches.get(0)
                        .updateFrom(role);
            } else {
                getAuthorities().add(role);
            }
        }
    }

    public boolean canRead(UserRole accessingRole)
    {
        boolean hasAccess = false;

        if (accessingRole == null) {
            // false
        } else if (accessingRole.isAdmin()) {
            hasAccess = true;
        } else if (accessingRole.getUserId()
                .equals(this.getId())) {
            hasAccess = true;
        } else if (accessingRole.isFreightcomStaff()) {
            hasAccess = true;

            // Not if user is admin
            for (UserRole role : getAuthorities()) {
                if (!role.isAdmin()) {
                    hasAccess = false;
                    break;
                }
            }
        } else if (accessingRole.isCustomerAdmin() && accessingRole.getCustomerId() != null) {
            for (UserRole role : getAuthorities()) {
                if (accessingRole.getCustomerId()
                        .equals(role.getCustomerId())) {
                    hasAccess = true;
                }
            }
        }

        return hasAccess;
    }

    public boolean canWrite(UserRole accessingRole)
    {
        boolean hasAccess = false;

        if (accessingRole == null) {
            // false
        } else if (accessingRole.isAdmin()) {
            hasAccess = true;
        } else if (accessingRole.getUserId()
                .equals(this.getId())) {
            hasAccess = true;
        } else if (accessingRole.isFreightcomStaff()) {
            hasAccess = true;

            // Not if user is admin
            for (UserRole role : getAuthorities()) {
                if (!role.isAdmin()) {
                    hasAccess = false;
                    break;
                }
            }
        } else if (accessingRole.isCustomerAdmin() && accessingRole.getCustomerId() != null) {
            for (UserRole role : getAuthorities()) {
                if (accessingRole.getCustomerId()
                        .equals(role.getCustomerId())) {
                    hasAccess = true;
                }
            }
        }

        return hasAccess;
    }

    public String fullName()
    {
        StringBuilder name = new StringBuilder();
        String separator = "";

        if (firstname != null) {
            name.append(firstname);
            separator = " ";
        }

        if (lastname != null) {
            name.append(separator)
                    .append(lastname);
        }

        return name.toString();
    }

    public String getName()
    {
        return name;
    }

    public Integer getNewPasswordRequired()
    {
        return newPasswordRequired;
    }

    public void setNewPasswordRequired(Integer newPasswordRequired)
    {
        this.newPasswordRequired = newPasswordRequired;
    }

    public ZonedDateTime getLastLogin()
    {
        return asDate(lastLogin);
    }

    public void setLastLogin(Date lastLogin)
    {
        this.lastLogin = lastLogin;
    }
}
