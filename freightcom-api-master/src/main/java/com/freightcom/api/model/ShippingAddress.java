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

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *
 *
 * @author
 */
@Entity(name = "ShippingAddress")
@Table(name = "shipping_address")
@SQLDelete(sql = "update shipping_address SET deleted_at = UTC_TIMESTAMP() where id=?")
@Where(clause = "deleted_at is null")
@Cacheable(value = true)
public class ShippingAddress extends TransactionalEntity
{

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "country")
    private String country;

    @Column(name = "instructions")
    private String instructions;

    @Column(name = "postal_zip")
    private String postalZip;

    @Column(name = "care_of")
    private String careOf;

    @Column(name = "contact_phone")
    private String contactPhone;

    @Column(name = "city")
    private String city;

    @Column(name = "tailgate")
    private Integer tailgate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "street_name")
    private String streetName;

    @Column(name = "confirm_delivery")
    private Integer confirmDelivery;

    @Column(name = "unit_number")
    private String unitNumber;

    @Column(name = "province")
    private String province;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(name = "country_name")
    private String countryName;

    @Column(name = "is_residential")
    private String isResidential;

    @Column(name = "company")
    private String company;

    @Column(name = "email")
    private String email;

    @Column(name = "res")
    private Integer res;

    @Column(name = "contact_name")
    private String contactName;

    @Column(name = "address2")
    private String address2;

    @Column(name = "address1")
    private String address1;

    @Column(name = "province_state")
    private String provinceState;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "deleted_at")
    private Date deletedAt;

    @Column(name = "notify_recipient")
    private Integer notifyRecipient;

    @Column(name = "phone")
    private String phone;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "street_number")
    private String streetNumber;

    @Column(name = "attention")
    private String attention;

    @Column(name = "postal_code")
    private String postalCode;

    @Column(name = "address_book_id")
    private Long addressBookId;

    @Column(name = "email_notification")
    private Boolean emailNotification = false;

    @Column(name="save_to_address_book")
    private Boolean saveToAddressBook = false;

    public ShippingAddress()
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

    public String getCountry()
    {
        return country;
    }

    public void setCountry(String country)
    {
        this.country = country;
    }

    public String getInstructions()
    {
        return instructions;
    }

    public void setInstructions(String instructions)
    {
        this.instructions = instructions;
    }

    public String getPostalZip()
    {
        return postalZip;
    }

    public void setPostalZip(String postalZip)
    {
        this.postalZip = postalZip;
    }

    public String getCareOf()
    {
        return careOf;
    }

    public void setCareOf(String careOf)
    {
        this.careOf = careOf;
    }

    public String getContactPhone()
    {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone)
    {
        this.contactPhone = contactPhone;
    }

    public String getCity()
    {
        return city;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public Integer getTailgate()
    {
        return tailgate;
    }

    public void setTailgate(Integer tailgate)
    {
        this.tailgate = tailgate;
    }

    @Override
    public ZonedDateTime getCreatedAt()
    {
        return asDate(createdAt);
    }

    @Override
    public void setCreatedAt(Date createdAt)
    {
        this.createdAt = createdAt;
    }

    public String getStreetName()
    {
        return streetName;
    }

    public void setStreetName(String streetName)
    {
        this.streetName = streetName;
    }

    public Integer getConfirmDelivery()
    {
        return confirmDelivery;
    }

    public void setConfirmDelivery(Integer confirmDelivery)
    {
        this.confirmDelivery = confirmDelivery;
    }

    public String getUnitNumber()
    {
        return unitNumber;
    }

    public void setUnitNumber(String unitNumber)
    {
        this.unitNumber = unitNumber;
    }

    public String getProvince()
    {
        return province;
    }

    public void setProvince(String province)
    {
        this.province = province;
    }

    @Override
    public ZonedDateTime getUpdatedAt()
    {
        return asDate(updatedAt);
    }

    @Override
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

    public String getIsResidential()
    {
        return isResidential;
    }

    public void setIsResidential(String isResidential)
    {
        this.isResidential = isResidential;
    }

    public String getResidential()
    {
        return isResidential;
    }

    public void setResidential(String isResidential)
    {
        this.isResidential = isResidential;
    }

    public boolean isResidential()
    {
        return "1".equals(isResidential) || "yes".equalsIgnoreCase(isResidential);
    }

    public String getCompany()
    {
        return company;
    }

    public void setCompany(String company)
    {
        this.company = company;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getContactEmail()
    {
        return email;
    }

    public void setContactEmail(String email)
    {
        this.email = email;
    }

    public Integer getRes()
    {
        return res;
    }

    public void setRes(Integer res)
    {
        this.res = res;
    }

    public String getContactName()
    {
        return contactName;
    }

    public void setContactName(String contactName)
    {
        this.contactName = contactName;
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

    public String getProvinceState()
    {
        return provinceState;
    }

    public void setProvinceState(String provinceState)
    {
        this.provinceState = provinceState;
    }

    public ZonedDateTime getDeletedAt()
    {
        return asDate(deletedAt);
    }

    public void setDeletedAt(Date deletedAt)
    {
        this.deletedAt = deletedAt;
    }

    public Integer getNotifyRecipient()
    {
        return notifyRecipient;
    }

    public void setNotifyRecipient(Integer notifyRecipient)
    {
        this.notifyRecipient = notifyRecipient;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getCompanyName()
    {
        return companyName;
    }

    public void setCompanyName(String companyName)
    {
        this.companyName = companyName;
    }

    public String getConsigneeName()
    {
        return companyName;
    }

    public void setConsigneeName(String consigneeName)
    {
        this.companyName = consigneeName;
    }

    public String getStreetNumber()
    {
        return streetNumber;
    }

    public void setStreetNumber(String streetNumber)
    {
        this.streetNumber = streetNumber;
    }

    public String getAttention()
    {
        return attention;
    }

    public void setAttention(String attention)
    {
        this.attention = attention;
    }

    public String getPostalCode()
    {
        return postalCode;
    }

    public void setPostalCode(String postalCode)
    {
        this.postalCode = postalCode;
    }

    public Long getAddressBookId()
    {
        return addressBookId;
    }

    public void setAddressBookId(Long addressBookId)
    {
        this.addressBookId = addressBookId;
    }

    public Boolean getEmailNotification()
    {
        return emailNotification;
    }

    public void setEmailNotification(Boolean emailNotification)
    {
        this.emailNotification = emailNotification;
    }

    @JsonIgnore
    public List<User> getNotifyUsers()
    {
        List<User> notifyUsers = new ArrayList<User>();

        if (emailNotification && email != null) {
            boolean first = true;

            for (String emailAddress: email.split("\\s*,\\s*")) {
                User notifyUser = new User();

                notifyUser.setEmail(emailAddress);

                if (first) {
                    notifyUser.setLastname(contactName);
                    notifyUser.setPhone(phone);
                }

                notifyUsers.add(notifyUser);
            }
        }

        return notifyUsers;
    }

    public Boolean getSaveToAddressBook()
    {
         return saveToAddressBook;
    }

    public void setSaveToAddressBook(Boolean saveToAddressBook)
    {
         this.saveToAddressBook = saveToAddressBook;
    }

    public ShippingAddress updateFrom(ShippingAddress source)
    {
        country = source.country;
        instructions = source.instructions;
        postalZip = source.postalZip;
        careOf = source.careOf;
        contactPhone = source.contactPhone;
        city = source.city;
        tailgate = source.tailgate;
        streetName = source.streetName;
        confirmDelivery = source.confirmDelivery;
        unitNumber = source.unitNumber;
        countryName = source.countryName;
        isResidential = source.isResidential;
        company = source.company;
        email = source.email;
        res = source.res;
        contactName = source.contactName;
        address2 = source.address2;
        address1 = source.address1;
        provinceState = source.provinceState;
        notifyRecipient = source.notifyRecipient;
        phone = source.phone;
        companyName = source.companyName;
        streetNumber = source.streetNumber;
        attention = source.attention;
        postalCode = source.postalCode;
        addressBookId = source.addressBookId;
        emailNotification = source.emailNotification;
        saveToAddressBook = source.saveToAddressBook;

        return this;
    }
}
