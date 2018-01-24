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
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
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
@Entity(name = "CustomsInvoice")
@Table(name = "customs_invoice")
public class CustomsInvoice extends TransactionalEntity
{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    private CustomerOrder order;

    @OneToOne(cascade = { CascadeType.ALL })
    @JoinColumn(name = "bill_to_id", referencedColumnName = "id", nullable = true)
    private ShippingAddress billTo;

    @OneToOne(cascade = { CascadeType.ALL })
    @JoinColumn(name = "contact_info_id", referencedColumnName = "id", nullable = true)
    private CustomsInvoiceContactInfo contactInfo;

    @OneToMany(targetEntity = CustomsInvoiceProduct.class, mappedBy = "customsInvoice", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CustomsInvoiceProduct> products;

    @Column(name = "dutiable")
    private Boolean dutiable;

    @Column(name = "bill")
    private String bill;

    @Column(name = "consigneeaccount")
    private String consigneeAccount;

    @Column(name = "sednumber")
    private String sedNumber;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modified_at")
    private Date modifiedAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "deleted_at")
    private Date deletedAt;

    public CustomsInvoice()
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

    public Boolean getDutiable()
    {
        return dutiable;
    }

    public void setDutiable(Boolean dutiable)
    {
        this.dutiable = dutiable;
    }

    public String getBill()
    {
        return bill;
    }

    public void setBill(String bill)
    {
        this.bill = bill;
    }

    public String getConsigneeAccount()
    {
        return consigneeAccount;
    }

    public void setConsigneeAccount(String consigneeAccount)
    {
        this.consigneeAccount = consigneeAccount;
    }

    public String getSedNumber()
    {
        return sedNumber;
    }

    public void setSedNumber(String sedNumber)
    {
        this.sedNumber = sedNumber;
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

    public CustomerOrder getOrder()
    {
        return order;
    }

    public void setOrder(CustomerOrder order)
    {
        this.order = order;
    }

    public ShippingAddress getBillTo()
    {
        return billTo;
    }

    public void setBillTo(ShippingAddress billTo)
    {
        this.billTo = billTo;
    }

    public CustomsInvoiceContactInfo getContactInfo()
    {
        return contactInfo;
    }

    public void setContactInfo(CustomsInvoiceContactInfo contactInfo)
    {
        this.contactInfo = contactInfo;
    }

    public List<CustomsInvoiceProduct> getProducts()
    {
        return products;
    }

    public void setProducts(List<CustomsInvoiceProduct> products)
    {
        if (products == null) {
            products = new ArrayList<CustomsInvoiceProduct>();
        } else {
            for (CustomsInvoiceProduct product : products) {
                product.setCustomsInvoice(this);
            }
        }

        this.products = products;
    }

    /**
     * handle products during patch
     */
    @Override
    public boolean patchProperty(String key, TransactionalEntity source, Object value) throws Exception
    {
        if (!(source instanceof CustomsInvoice)) {
            throw new Exception("Can't patch from invalid class " + this + " " + source);
        }

        if (key.equals("products")) {
            if (products == null) {
                products = new ArrayList<CustomsInvoiceProduct>();
            }

            List<CustomsInvoiceProduct> newValues = ((CustomsInvoice) source).getProducts();

            if (newValues == null) {
                products = null;
            } else {
                int n = newValues.size();
                int m = products.size();
                int i = 0;

                while (i < n && i < m) {
                    products.get(i)
                            .patch(newValues.get(i), (Map<?, ?>) ((List<?>) value).get(i));
                    i++;
                }

                while (i < n) {
                    newValues.get(i)
                            .setCustomsInvoice(this);
                    products.add(newValues.get(i));
                    i++;
                }

                int last = i;

                while (i < m) {
                    products.remove(last);
                    i++;
                }
            }

            return true;
        } else {
            return super.patchProperty(key, source, value);
        }
    }

    public boolean billToShipper()
    {
        // TODO Auto-generated method stub
        return false;
    }

    public CustomsInvoice updateFrom(CustomsInvoice source)
    {
        order = source.order;

        if (billTo == null) {
            billTo = source.billTo;
        } else {
            billTo.updateFrom(source.billTo);
        }

        contactInfo = source.contactInfo;
        dutiable = source.dutiable;
        bill = source.bill;
        consigneeAccount = source.consigneeAccount;
        sedNumber = source.sedNumber;

        return this;
    }

}
