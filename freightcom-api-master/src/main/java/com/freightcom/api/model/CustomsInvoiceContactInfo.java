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
@Entity(name = "CustomsInvoiceContactInfo")
@Table(name = "customs_invoice_contact_info")
public class CustomsInvoiceContactInfo extends TransactionalEntity
{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "company")
    private String company;

    @Column(name = "name")
    private String name;

    @Column(name = "broker_name")
    private String brokerName;

    @Column(name = "tax_id")
    private String taxId;

    @Column(name = "phone")
    private String phone;

    @Column(name = "receipts_tax_id")
    private String receiptsTaxId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modified_at")
    private Date modifiedAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "deleted_at")
    private Date deletedAt;

    public CustomsInvoiceContactInfo()
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

    public String getCompany()
    {
        return company;
    }

    public void setCompany(String company)
    {
        this.company = company;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getBrokerName()
    {
        return brokerName;
    }

    public void setBrokerName(String brokerName)
    {
        this.brokerName = brokerName;
    }

    public String getTaxId()
    {
        return taxId;
    }

    public void setTaxId(String taxId)
    {
        this.taxId = taxId;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getReceiptsTaxId()
    {
        return receiptsTaxId;
    }

    public void setReceiptsTaxId(String receiptsTaxId)
    {
        this.receiptsTaxId = receiptsTaxId;
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

}