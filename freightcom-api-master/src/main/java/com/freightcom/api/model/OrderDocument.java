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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Entity(name = "OrderDocument")
@Table(name = "order_document")
public class OrderDocument extends TransactionalEntity
{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JsonIgnore
    @ManyToOne
    private CustomerOrder order;

    @Column(name = "document_id")
    private String documentId;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "type")
    private String type;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modified_at")
    private Date modifiedAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "deleted_at")
    private Date deletedAt;

    @OneToOne
    @JoinColumn(name = "uploaded_by_role_id", referencedColumnName = "id")
    private UserRole uploadedByRole;

    public OrderDocument()
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

    public CustomerOrder getOrder()
    {
        return order;
    }

    public void setOrder(CustomerOrder order)
    {
        this.order = order;
    }

    public String getDocumentId()
    {
        return documentId;
    }

    public void setDocumentId(String documentId)
    {
        this.documentId = documentId;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
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

    @JsonFormat(pattern = "yyyy-MM-dd")
    public ZonedDateTime getDeletedAt()
    {
        return asDate(deletedAt);
    }

    public void setDeletedAt(Date deletedAt)
    {
        this.deletedAt = legacyDate(deletedAt);
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public UserRole getUploadedByRole()
    {
        return uploadedByRole;
    }

    public void setUploadedByRole(UserRole uploadedByRole)
    {
        this.uploadedByRole = uploadedByRole;
    }

    public boolean canDelete(UserRole role)
    {
        return role != null
            && (role.isFreightcom()
                || role.equals(uploadedByRole)
                || (uploadedByRole != null &&  role.isCustomerAdmin() && role.sameCustomer(uploadedByRole))
                );

    }

    public String getUploadedByName (UserRole role)
    {
        if (role == null || uploadedByRole == null) {
            return "";
        } else if (role.isFreightcom()
                   || (role.isCustomerAdmin() && role.sameCustomer(uploadedByRole))
                   || role.equals(uploadedByRole)
                   ) {
             return uploadedByRole.getUser().fullName();
        } else if (uploadedByRole.isFreightcom()) {
            return "Freightcom";
        } else {
            return "";
        }
    }
}
