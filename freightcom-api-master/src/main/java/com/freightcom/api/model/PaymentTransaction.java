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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OrderColumn;
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
@Entity(name = "PaymentTransaction")
@Table(name = "payment_transaction")
public class PaymentTransaction extends TransactionalEntity
{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "chq_image")
    private String chqImage;

    @Column(name = "creditcard_id")
    private Long creditcardId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "type")
    private String type;

    @Column(name = "tx_id")
    private Long txId;

    @Column(name = "created_by_user_id")
    private Long createdByUserId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "deleted_at")
    private Date deletedAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(name = "tx_result")
    private String txResult;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "chq_date")
    private String chqDate;

    @Column(name = "invoice_id")
    private Long invoiceId;

    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "chq_number")
    private String chequeNumber;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name="payment")
    private BigDecimal payment;

    @Column(name="payment_type")
    private String paymentType;

    @Column(name="credit")
    private BigDecimal credit;

    @Column(name="reference")
    private String reference;

    @ManyToMany
    @JoinTable(name = "applied_payment", joinColumns = @JoinColumn(name = "invoice_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "payment_transaction_id", referencedColumnName = "id"))
    @OrderColumn(name = "position")
    private List<Invoice> invoices = new ArrayList<Invoice>();

    @ManyToOne
    @JoinColumn(name = "credit_id")
    private Credit creditObject;


    public PaymentTransaction() {
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

    public String getChqImage()
    {
        return chqImage;
    }

    public void setChqImage(String chqImage)
    {
        this.chqImage = chqImage;
    }

    public Long getCreditcardId()
    {
        return creditcardId;
    }

    public void setCreditcardId(Long creditcardId)
    {
        this.creditcardId = creditcardId;
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    public ZonedDateTime getCreatedAt()
    {
        return asDate(createdAt);
    }

    public void setCreatedAt(Date createdAt)
    {
        this.createdAt = createdAt;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public Long getTxId()
    {
        return txId;
    }

    public void setTxId(Long txId)
    {
        this.txId = txId;
    }

    public Long getCreatedByUserId()
    {
        return createdByUserId;
    }

    public void setCreatedByUserId(Long createdByUserId)
    {
        this.createdByUserId = createdByUserId;
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    public ZonedDateTime getDeletedAt()
    {
        return asDate(deletedAt);
    }

    public void setDeletedAt(Date deletedAt)
    {
        this.deletedAt = deletedAt;
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    public ZonedDateTime getUpdatedAt()
    {
        return asDate(updatedAt);
    }

    public void setUpdatedAt(Date updatedAt)
    {
        this.updatedAt = updatedAt;
    }

    public String getTxResult()
    {
        return txResult;
    }

    public void setTxResult(String txResult)
    {
        this.txResult = txResult;
    }

    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public String getChqDate()
    {
        return chqDate;
    }

    public void setChqDate(String chqDate)
    {
        this.chqDate = chqDate;
    }

    public Long getInvoiceId()
    {
        return invoiceId;
    }

    public void setInvoiceId(Long invoiceId)
    {
        this.invoiceId = invoiceId;
    }

    public Long getCustomerId()
    {
        return customerId;
    }

    public void setCustomerId(Long customerId)
    {
        this.customerId = customerId;
    }

    public String getChequeNumber()
    {
        return chequeNumber;
    }

    public void setChequeNumber(String chequeNumber)
    {
        this.chequeNumber = chequeNumber;
    }

    public BigDecimal getAmount()
    {
        return amount;
    }

    public void setAmount(BigDecimal amount)
    {
         this.amount = amount;
    }


    public BigDecimal getPayment()
    {
         return payment;
    }

    public void setPayment(BigDecimal payment)
    {
         this.payment = payment;
    }


    public String getPaymentType()
    {
         return paymentType;
    }

    public void setPaymentType(String paymentType)
    {
         this.paymentType = paymentType;
    }


    public BigDecimal getCredit()
    {
         return credit;
    }

    public void setCredit(BigDecimal credit)
    {
         this.credit = credit;
    }

    @JsonIgnore
    public List<Invoice> getInvoices()
    {
        return invoices;
    }

    public void setInvoices(List<Invoice> invoices)
    {
        this.invoices = invoices;
    }

    public Credit getCreditObject()
    {
        return creditObject;
    }

    public void setCreditObject(Credit creditObject)
    {
        this.creditObject = creditObject;
    }

    public String getReference()
    {
        return reference;
    }

    public void setReference(String reference)
    {
        this.reference = reference;
    }

}
