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
import java.util.Date;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.freightcom.api.services.dataobjects.PickupData;

/**
 *
 *
 * @author
 */
@Entity(name = "Pickup")
@Table(name = "pickup")
@SQLDelete(sql = "update pickup SET deleted_at = UTC_TIMESTAMP() where id=?")
@Where(clause = "deleted_at is null")
public class Pickup extends TransactionalEntity
{

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "num_packages")
    private Integer numPackages;

    @Column(name = "confirmation_number")
    private String confirmationNumber;

    @Column(name = "charge_card_expiry_date")
    private String chargeCardExpiryDate;

    @Column(name = "carrier_confirmation")
    private String carrierConfirmation;

    @Column(name = "country")
    private String country;

    @Column(name = "contact_phone")
    private String contactPhone;

    @Column(name = "contact_email")
    private String contactEmail;

    @Column(name = "city")
    private String city;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "next_pickup_date")
    private Date nextPickupDate;

    @Column(name = "ready_time")
    private String pickupReadyTime;

    @Column(name = "afterhours_closing_time")
    private String afterhoursClosingTime;

    @Column(name = "contact_extension")
    private String contactExtension;

    @Column(name = "carrier_id")
    private Long carrierId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "pickup_date")
    private Date pickupDate;

    @Column(name = "province")
    private String province;

    @Column(name = "cc_transaction_id")
    private Long ccTransactionId;

    @Column(name = "currency")
    private String currency;

    @Column(name = "afterhours_closing_location")
    private String afterhoursClosingLocation;

    @Column(name = "special_instructions")
    private String pickupInstructions;

    @Column(name = "delivery_instructions")
    private String deliveryInstructions;

    @Column(name = "contact_name")
    private String contactName;

    @Column(name = "charge")
    private Float charge;

    @Column(name = "address2")
    private String address2;

    @Column(name = "address1")
    private String address1;

    @Column(name = "call_time")
    private String callTime;

    @Column(name = "weight")
    private String weight;

    @Column(name = "close_time")
    private String pickupCloseTime;

    @Column(name = "delivery_close_time")
    private String deliveryCloseTime;

    @Column(name = "ready_time_2")
    private String readyTime2;

    @Column(name = "location_type")
    private String locationType;

    @Column(name = "charge_card_number")
    private String chargeCardNumber;

    @Column(name = "package_location")
    private String packageLocation;

    @Column(name = "charge_card_type")
    private String chargeCardType;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "pickup_time")
    private String pickupTime;

    @Column(name = "postal_code")
    private String postalCode;

    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "call_time_2")
    private String callTime2;

    @Column(name = "status")
    private Long status;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "deleted_at")
    private Date deletedAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;

    public Pickup()
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

    public Integer getNumPackages()
    {
        return numPackages;
    }

    public void setNumPackages(Integer numPackages)
    {
        this.numPackages = numPackages;
    }

    public String getConfirmationNumber()
    {
        return confirmationNumber;
    }

    public void setConfirmationNumber(String confirmationNumber)
    {
        this.confirmationNumber = confirmationNumber;
    }

    public String getChargeCardExpiryDate()
    {
        return chargeCardExpiryDate;
    }

    public void setChargeCardExpiryDate(String chargeCardExpiryDate)
    {
        this.chargeCardExpiryDate = chargeCardExpiryDate;
    }

    public String getCountry()
    {
        return country;
    }

    public void setCountry(String country)
    {
        this.country = country;
    }

    public String getContactEmail()
    {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail)
    {
        this.contactEmail = contactEmail;
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

    public ZonedDateTime getNextPickupDate()
    {
        return asDate(nextPickupDate);
    }

    public void setNextPickupDate(Date nextPickupDate)
    {
        this.nextPickupDate = nextPickupDate;
    }

    public String getPickupReadyTime()
    {
        return pickupReadyTime;
    }

    public void setPickupReadyTime(String readyTime)
    {
        this.pickupReadyTime = readyTime;
    }

    public String getAfterhoursClosingTime()
    {
        return afterhoursClosingTime;
    }

    public void setAfterhoursClosingTime(String afterhoursClosingTime)
    {
        this.afterhoursClosingTime = afterhoursClosingTime;
    }

    public String getContactExtension()
    {
        return contactExtension;
    }

    public void setContactExtension(String contactExtension)
    {
        this.contactExtension = contactExtension;
    }

    public Long getCarrierId()
    {
        return carrierId;
    }

    public void setCarrierId(Long carrierId)
    {
        this.carrierId = carrierId;
    }

    public ZonedDateTime getPickupDate()
    {
        return asDate(pickupDate);
    }

    public void setPickupDate(Date pickupDate)
    {
        this.pickupDate = pickupDate;
    }

    public String getProvince()
    {
        return province;
    }

    public void setProvince(String province)
    {
        this.province = province;
    }

    public Long getCcTransactionId()
    {
        return ccTransactionId;
    }

    public void setCcTransactionId(Long ccTransactionId)
    {
        this.ccTransactionId = ccTransactionId;
    }

    public String getCurrency()
    {
        return currency;
    }

    public void setCurrency(String currency)
    {
        this.currency = currency;
    }

    public String getAfterhoursClosingLocation()
    {
        return afterhoursClosingLocation;
    }

    public void setAfterhoursClosingLocation(String afterhoursClosingLocation)
    {
        this.afterhoursClosingLocation = afterhoursClosingLocation;
    }

    public String getPickupInstructions()
    {
        return pickupInstructions;
    }

    public void setPickupInstructions(String specialInstructions)
    {
        this.pickupInstructions = specialInstructions;
    }

    public String getContactName()
    {
        return contactName;
    }

    public void setContactName(String contactName)
    {
        this.contactName = contactName;
    }

    public Float getCharge()
    {
        return charge;
    }

    public void setCharge(Float charge)
    {
        this.charge = charge;
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

    public String getCallTime()
    {
        return callTime;
    }

    public void setCallTime(String callTime)
    {
        this.callTime = callTime;
    }

    public String getWeight()
    {
        return weight;
    }

    public void setWeight(String weight)
    {
        this.weight = weight;
    }

    public String getPickupCloseTime()
    {
        return pickupCloseTime;
    }

    public void setPickupCloseTime(String closeTime)
    {
        this.pickupCloseTime = closeTime;
    }

    public String getReadyTime2()
    {
        return readyTime2;
    }

    public void setReadyTime2(String readyTime2)
    {
        this.readyTime2 = readyTime2;
    }

    public String getLocationType()
    {
        return locationType;
    }

    public void setLocationType(String locationType)
    {
        this.locationType = locationType;
    }

    public String getChargeCardNumber()
    {
        return chargeCardNumber;
    }

    public void setChargeCardNumber(String chargeCardNumber)
    {
        this.chargeCardNumber = chargeCardNumber;
    }

    public String getPackageLocation()
    {
        return packageLocation;
    }

    public void setPackageLocation(String packageLocation)
    {
        this.packageLocation = packageLocation;
    }

    public String getChargeCardType()
    {
        return chargeCardType;
    }

    public void setChargeCardType(String chargeCardType)
    {
        this.chargeCardType = chargeCardType;
    }

    public String getCompanyName()
    {
        return companyName;
    }

    public void setCompanyName(String companyName)
    {
        this.companyName = companyName;
    }

    public String getPickupTime()
    {
        return pickupTime;
    }

    public void setPickupTime(String pickupTime)
    {
        this.pickupTime = pickupTime;
    }

    public String getPostalCode()
    {
        return postalCode;
    }

    public void setPostalCode(String postalCode)
    {
        this.postalCode = postalCode;
    }

    public Long getCustomerId()
    {
        return customerId;
    }

    public void setCustomerId(Long customerId)
    {
        this.customerId = customerId;
    }

    public String getCallTime2()
    {
        return callTime2;
    }

    public void setCallTime2(String callTime2)
    {
        this.callTime2 = callTime2;
    }

    public Long getStatus()
    {
        return status;
    }

    public void setStatus(Long status)
    {
        this.status = status;
    }

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

    public void update(PickupData data)
    {
        pickupDate = data.getPickupdate();
        pickupInstructions = data.getPickupInstructions();
        pickupReadyTime = data.getPickupReadyTime();
        pickupCloseTime = data.getPickupCloseTime();
        deliveryCloseTime = data.getDeliveryCloseTime();
        deliveryInstructions = data.getDeliveryInstructions();
        contactEmail = data.getContactEmail();
        contactPhone = data.getContactPhone();
        contactName = data.getContactName();
    }

    public String getDeliveryCloseTime()
    {
        return deliveryCloseTime;
    }

    public void setDeliveryCloseTime(String deliveryCloseTime)
    {
        this.deliveryCloseTime = deliveryCloseTime;
    }

    public String getDeliveryInstructions()
    {
        return deliveryInstructions;
    }

    public void setDeliveryInstructions(String deliveryInstructions)
    {
        this.deliveryInstructions = deliveryInstructions;
    }

    public String getCarrierConfirmation()
    {
        return carrierConfirmation;
    }

    public void setCarrierConfirmation(String carrierConfirmation)
    {
        this.carrierConfirmation = carrierConfirmation;
    }

    public Pickup updateFrom(Pickup source)
    {
        numPackages = source.numPackages;
        confirmationNumber = source.confirmationNumber;
        chargeCardExpiryDate = source.chargeCardExpiryDate;
        carrierConfirmation = source.carrierConfirmation;
        country = source.country;
        contactPhone = source.contactPhone;
        contactEmail = source.contactEmail;
        city = source.city;
        nextPickupDate = source.nextPickupDate;
        pickupReadyTime = source.pickupReadyTime;
        afterhoursClosingTime = source.afterhoursClosingTime;
        contactExtension = source.contactExtension;
        carrierId = source.carrierId;
        pickupDate = source.pickupDate;
        province = source.province;
        ccTransactionId = source.ccTransactionId;
        currency = source.currency;
        afterhoursClosingLocation = source.afterhoursClosingLocation;
        pickupInstructions = source.pickupInstructions;
        deliveryInstructions = source.deliveryInstructions;
        contactName = source.contactName;
        charge = source.charge;
        address2 = source.address2;
        address1 = source.address1;
        callTime = source.callTime;
        weight = source.weight;
        pickupCloseTime = source.pickupCloseTime;
        deliveryCloseTime = source.deliveryCloseTime;
        readyTime2 = source.readyTime2;
        locationType = source.locationType;
        chargeCardNumber = source.chargeCardNumber;
        chargeCardType = source.chargeCardType;
        companyName = source.companyName;
        pickupTime = source.pickupTime;
        postalCode = source.postalCode;
        customerId = source.customerId;
        callTime2 = source.callTime2;
        status = source.status;
        createdAt = source.createdAt;
        
        return this;
    }
}

