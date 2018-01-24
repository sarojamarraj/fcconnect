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

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import org.springframework.hateoas.core.Relation;

import java.time.ZonedDateTime;
import java.util.Date;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

/**
 *
 *
 * @author
 */
@Entity(name = "AddressBook")
@Table(name = "address_book")
@Relation(collectionRelation = "addressbook")
public class AddressBook extends TransactionalEntity
{

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "country")
    private String country;

    @Column(name = "city")
    private String city;

    @Column(name = "consignee_name")
    private String consigneeName;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "default_to")
    private Integer defaultTo;

    @Column(name = "notify")
    private Integer notify;

    @Column(name = "contact_email")
    private String contactEmail;

    @Column(name = "province")
    private String province;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(name = "country_name")
    private String countryName;

    @Column(name = "default_from")
    private Integer defaultFrom;

    @Column(name = "consignee_id")
    private String consigneeId;

    @Column(name = "contact_name")
    private String contactName;

    @Column(name = "distribution_list_name")
    private String distributionListName;

    @Column(name = "address2")
    private String address2;

    @Column(name = "address1")
    private String address1;

    @Column(name = "address_id")
    private Long addressId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "deleted_at")
    private Date deletedAt;

    @Column(name = "tax_id")
    private String taxId;

    @Column(name = "residential")
    private Boolean residential = false;

    @Column(name = "phone")
    private String phone;

    @Column(name = "instruction")
    private String instruction;

    @NotNull
    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "postal_code")
    private String postalCode;

    public AddressBook()
    {
        super();
    }

    /**
     * Set fields for new entry from a shipping addrss
     */
    public AddressBook(ShippingAddress address, Customer customer)
    {
        super();

        customerId = customer.getId();
        contactName = address.getContactName();
        contactEmail = address.getContactEmail();
        address1 = address.getAddress1();
        address2 = address.getAddress2();
        consigneeName = address.getCompany();
        country = address.getCountry();
        city = address.getCity();
        phone = address.getPhone();
        postalCode = address.getPostalCode();
        residential = address.isResidential();
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getCountry()
    {
        return country;
    }

    public void setCountry(String country)
    {
        this.country = country;
    }

    public String getCity()
    {
        return city;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public String getConsigneeName()
    {
        return consigneeName;
    }

    public void setConsigneeName(String consigneeName)
    {
        this.consigneeName = consigneeName;
    }

    public ZonedDateTime getCreatedAt()
    {
        return asDate(createdAt);
    }

    public void setCreatedAt(Date createdAt)
    {
        this.createdAt = createdAt;
    }

    public Integer getDefaultTo()
    {
        return defaultTo;
    }

    public void setDefaultTo(Integer defaultTo)
    {
        this.defaultTo = defaultTo;
    }

    public Integer getNotify()
    {
        return notify;
    }

    public void setNotify(Integer notify)
    {
        this.notify = notify;
    }

    public String getContactEmail()
    {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail)
    {
        this.contactEmail = contactEmail;
    }

    public String getProvince()
    {
        return province;
    }

    public void setProvince(String province)
    {
        this.province = province;
    }

    public ZonedDateTime getUpdatedAt()
    {
        return asDate(updatedAt);
    }

    public void setUpdatedAt(Date updatedAt)
    {
        this.updatedAt = updatedAt;
    }

    public String getCountryName()
    {
        return countryName;
    }

    public void setCountryName(String countryName)
    {
        this.countryName = countryName;
    }

    public Integer getDefaultFrom()
    {
        return defaultFrom;
    }

    public void setDefaultFrom(Integer defaultFrom)
    {
        this.defaultFrom = defaultFrom;
    }

    public String getConsigneeId()
    {
        return consigneeId;
    }

    public void setConsigneeId(String consigneeId)
    {
        this.consigneeId = consigneeId;
    }

    public String getContactName()
    {
        return contactName;
    }

    public void setContactName(String contactName)
    {
        this.contactName = contactName;
    }

    public String getDistributionListName()
    {
        return distributionListName;
    }

    public void setDistributionListName(String distributionListName)
    {
        this.distributionListName = distributionListName;
    }

    public String getAddress2()
    {
        return address2;
    }

    public void setAddress2(String address2)
    {
        this.address2 = address2;
    }

    public String getAddress1()
    {
        return address1;
    }

    public void setAddress1(String address1)
    {
        this.address1 = address1;
    }

    public Long getAddressId()
    {
        return addressId;
    }

    public void setAddressId(Long addressId)
    {
        this.addressId = addressId;
    }

    public ZonedDateTime getDeletedAt()
    {
        return asDate(deletedAt);
    }

    public void setDeletedAt(Date deletedAt)
    {
        this.deletedAt = deletedAt;
    }

    public String getTaxId()
    {
        return taxId;
    }

    public void setTaxId(String taxId)
    {
        this.taxId = taxId;
    }

    public Boolean getResidential()
    {
        return residential;
    }

    public void setResidential(Boolean residential)
    {
        this.residential = residential;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getInstruction()
    {
        return instruction;
    }

    public void setInstruction(String instruction)
    {
        this.instruction = instruction;
    }

    public Long getCustomerId()
    {
        return customerId;
    }

    public void setCustomerId(Long customerId)
    {
        this.customerId = customerId;
    }

    public String getPostalCode()
    {
        return postalCode;
    }

    public void setPostalCode(String postalCode)
    {
        this.postalCode = postalCode;
    }

}
