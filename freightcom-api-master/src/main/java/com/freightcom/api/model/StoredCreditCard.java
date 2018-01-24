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

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.freightcom.api.model.converters.EncryptConverter;

/**
 *
 *
 * @author
 */
@Entity(name = "StoredCreditCard")
@Table(name = "stored_credit_card")
public class StoredCreditCard extends TransactionalEntity
{
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Convert(converter = EncryptConverter.class)
    private String dataKey;
    private String responseCode;
    private String message;
    private String transactionDate;
    private String transactionTime;
    private Boolean complete;
    private Boolean timedOut;
    private Boolean success;
    private String paymentType;
    private String customerId;
    private String phone;
    private String email;
    private String note;
    private String maskedCard;
    private String expiryDate;
    private String cryptType;
    private String streetNumber;
    private String streetName;
    private String cardType;
    private String postalCode;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date deletedAt;

    public StoredCreditCard()
    {
        super();
    }

    @Override
    public Long getId()
    {
        return id;
    }

    public String getDataKey()
    {
        return dataKey;
    }

    public void setDataKey(String dataKey)
    {
        this.dataKey = dataKey;
    }

    public String getResponseCode()
    {
        return responseCode;
    }

    public void setResponseCode(String responseCode)
    {
        this.responseCode = responseCode;
    }

    public String getTransactionDate()
    {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate)
    {
        this.transactionDate = transactionDate;
    }

    public String getTransactionTime()
    {
        return transactionTime;
    }

    public void setTransactionTime(String transactionTime)
    {
        this.transactionTime = transactionTime;
    }

    public Boolean getComplete()
    {
        return complete;
    }

    public void setComplete(Boolean string)
    {
        this.complete = string;
    }

    public Boolean getTimedOut()
    {
        return timedOut;
    }

    public void setTimedOut(Boolean string)
    {
        this.timedOut = string;
    }

    public Boolean getSuccess()
    {
        return success;
    }

    public void setSuccess(Boolean string)
    {
        this.success = string;
    }

    public String getPaymentType()
    {
        return paymentType;
    }

    public void setPaymentType(String paymentType)
    {
        this.paymentType = paymentType;
    }

    public String getCustomerId()
    {
        return customerId;
    }

    public void setCustomerId(String customerId)
    {
        this.customerId = customerId;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getNote()
    {
        return note;
    }

    public void setNote(String note)
    {
        this.note = note;
    }

    public String getMaskedCard()
    {
        return maskedCard;
    }

    public void setMaskedCard(String maskedCard)
    {
        this.maskedCard = maskedCard;
    }

    public String getExpiryDate()
    {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate)
    {
        this.expiryDate = expiryDate;
    }

    public String getCryptType()
    {
        return cryptType;
    }

    public void setCryptType(String cryptType)
    {
        this.cryptType = cryptType;
    }

    public String getStreetNumber()
    {
        return streetNumber;
    }

    public void setStreetNumber(String streetNumber)
    {
        this.streetNumber = streetNumber;
    }

    public String getStreetName()
    {
        return streetName;
    }

    public void setStreetName(String streetName)
    {
        this.streetName = streetName;
    }

    public String getPostalCode()
    {
        return postalCode;
    }

    public void setPostalCode(String postalCode)
    {
        this.postalCode = postalCode;
    }

    @Override
    public void setId(Long id)
    {
        // TODO Auto-generated method stub

    }

    public String getCardType()
    {
        return cardType;
    }

    public void setCardType(String cardType)
    {
        this.cardType = cardType;
    }
}
