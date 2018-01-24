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

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Formula;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *
 *
 * @author
 */
@Entity(name = "DeletedInvoice")
@Table(name = "invoice")
public class DeletedInvoice extends TransactionalEntity implements OwnedByCustomer
{

    private static final long serialVersionUID = 1L;
    private static final String CANCELLED = "cancelled";


    @Transient
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "us_state_tax")
    private BigDecimal usStateTax;

    @Column(name = "notes")
    private String notes;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createdAt;

    @Column(name = "discount")
    private BigDecimal discount;

    @Column(name = "qst_tax")
    private BigDecimal qstTax;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "updated_at")
    private Date updatedAt;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "sent_at")
    private Date sentAt;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "viewed_at")
    private Date viewedAt;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "date_paid")
    private Date datePaid;

    @Column(name = "terms")
    private String terms;

    @Column(name = "gst_tax")
    private BigDecimal gstTax;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "modified_due_date")
    private Date modifiedDueDate;

    @Column(name = "hst_tax")
    private BigDecimal hstTax;

    @Column(name = "cost")
    private BigDecimal cost;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "due_at")
    private Date dueAt;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "effective_at")
    private Date invoiceDate;

    @Column(name = "tax")
    private BigDecimal tax;

    @Column(name = "other_taxes")
    private BigDecimal otherTaxes;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "deleted_at")
    private Date deletedAt;

    @Column(name = "cur_id")
    private Integer curId;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "paid_at")
    private Date paidAt;

    @Column(name = "us_fed_tax")
    private BigDecimal usFedTax;

    @Column(name = "payment_type")
    private Integer paymentType;

    @Column(name = "adjustment")
    private BigDecimal adjustment;

    @Column(name = "pst_tax")
    private BigDecimal pstTax;

    @Column(name = "exclude_from_claim")
    private Integer excludeFromClaim;

    @Column(name = "order_id")
    private Long orderId;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToMany
    @JoinTable(name = "order_invoice_workorder", joinColumns = @JoinColumn(name = "order_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "elt", referencedColumnName = "id"))
    @OrderColumn(name = "position")
    private List<CustomerOrder> orders;

    @Column(name = "status")
    private String status;

    @OneToMany(mappedBy = "invoiceId", cascade = { CascadeType.ALL })
    @OrderColumn(name = "sequence")
    private List<AppliedCredit> credits;

    @OneToMany(mappedBy = "invoiceId", cascade = { CascadeType.ALL })
    @OrderColumn(name = "sequence")
    private List<AppliedPayment> payments;

    @OneToMany()
    @JoinColumn(name = "invoice_id", referencedColumnName = "id")
    private List<Charge> charges = new ArrayList<Charge>();

    @Formula("(select IF(((select IFNULL(sum(IFNULL(T.amount,0)),0) from applied_payment T where T.invoice_id=id) + (select IFNULL(sum(IFNULL(AC.amount,0)),0) from applied_credit AC where AC.invoice_id=id)) >= (select IFNULL(sum(IFNULL(C.total,0)),0) from charge C where C.invoice_id=id), 1, 0) as 'paymentStatus')")
    private Integer paymentStatus;

    @Formula("(select IFNULL(sum(IFNULL(C.total,0)),0) as 'total' from charge C where C.invoice_id=id)")
    private BigDecimal amount = BigDecimal.ZERO;

    @Formula("(select IFNULL(sum(IFNULL(T.amount,0)),0) as 'paidAmount' from applied_payment T where T.invoice_id=id)")
    private BigDecimal paidAmount = BigDecimal.ZERO;

    @Formula("(select IFNULL(sum(IFNULL(T.amount,0)),0) as 'paidAmount' from applied_credit T where T.invoice_id=id)")
    private BigDecimal creditedAmount = BigDecimal.ZERO;

    public DeletedInvoice()
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

    public BigDecimal getUsStateTax()
    {
        return usStateTax;
    }

    public void setUsStateTax(BigDecimal usStateTax)
    {
        this.usStateTax = usStateTax;
    }

    public String getNotes()
    {
        return notes;
    }

    public void setNotes(String notes)
    {
        this.notes = notes;
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

    public BigDecimal getDiscount()
    {
        return discount;
    }

    public void setDiscount(BigDecimal discount)
    {
        this.discount = discount;
    }

    public BigDecimal getQstTax()
    {
        return qstTax;
    }

    public void setQstTax(BigDecimal qstTax)
    {
        this.qstTax = qstTax;
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

    @JsonFormat(pattern = "yyyy-MM-dd")
    public ZonedDateTime getSentAt()
    {
        return asDate(sentAt);
    }

    public void setSentAt(Date sentAt)
    {
        this.sentAt = sentAt;
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    public ZonedDateTime getViewedAt()
    {
        return asDate(viewedAt);
    }

    public void setViewedAt(Date viewedAt)
    {
        this.viewedAt = viewedAt;
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    public ZonedDateTime getDatePaid()
    {
        return asDate(datePaid);
    }

    public void setDatePaid(Date datePaid)
    {
        this.datePaid = datePaid;
    }

    public String getTerms()
    {
        return terms;
    }

    public void setTerms(String terms)
    {
        this.terms = terms;
    }

    public BigDecimal getGstTax()
    {
        return gstTax;
    }

    public void setGstTax(BigDecimal gstTax)
    {
        this.gstTax = gstTax;
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    public ZonedDateTime getModifiedDueDate()
    {
        return asDate(modifiedDueDate);
    }

    public void setModifiedDueDate(Date modifiedDueDate)
    {
        this.modifiedDueDate = modifiedDueDate;
    }

    public BigDecimal getHstTax()
    {
        return hstTax;
    }

    public void setHstTax(BigDecimal hstTax)
    {
        this.hstTax = hstTax;
    }

    public BigDecimal getAmount()
    {
        return amount;
    }

    public void setAmount(BigDecimal amount)
    {
        this.amount = amount;
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    public ZonedDateTime getDateGenerated()
    {
        return asDate(createdAt);
    }

    public BigDecimal getCost()
    {
        return cost;
    }

    public void setCost(BigDecimal cost)
    {
        this.cost = cost;
    }

    public Integer getPaymentStatus()
    {
        return paymentStatus;
    }

    public void setPaymentStatus(Integer paymentStatus)
    {
        this.paymentStatus = paymentStatus;
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    public ZonedDateTime getDueAt()
    {
        return asDate(dueAt);
    }

    public void setDueAt(Date dueAt)
    {
        this.dueAt = dueAt;
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    public ZonedDateTime getInvoiceDate()
    {
        return asDate(invoiceDate);
    }

    public void setInvoiceDate(Date invoiceDate)
    {
        this.invoiceDate = invoiceDate;
    }

    public BigDecimal getTax()
    {
        return tax;
    }

    public void setTax(BigDecimal tax)
    {
        this.tax = tax;
    }

    public BigDecimal getOtherTaxes()
    {
        return otherTaxes;
    }

    public void setOtherTaxes(BigDecimal otherTaxes)
    {
        this.otherTaxes = otherTaxes;
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

    public Integer getCurId()
    {
        return curId;
    }

    public void setCurId(Integer curId)
    {
        this.curId = curId;
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    public ZonedDateTime getPaidAt()
    {
        return asDate(paidAt);
    }

    public void setPaidAt(Date paidAt)
    {
        this.paidAt = paidAt;
    }

    public BigDecimal getUsFedTax()
    {
        return usFedTax;
    }

    public void setUsFedTax(BigDecimal usFedTax)
    {
        this.usFedTax = usFedTax;
    }

    public Integer getPaymentType()
    {
        return paymentType;
    }

    public void setPaymentType(Integer paymentType)
    {
        this.paymentType = paymentType;
    }

    public BigDecimal getPaidAmount()
    {
        return paidAmount;
    }

    public void setPaidAmount(BigDecimal paidAmount)
    {
        this.paidAmount = paidAmount;
    }

    public BigDecimal getCreditedAmount()
    {
        return creditedAmount;
    }

    public void setCreditedAmount(BigDecimal creditedAmount)
    {
        this.creditedAmount = creditedAmount;
    }

    public BigDecimal getAdjustment()
    {
        return adjustment;
    }

    public void setAdjustment(BigDecimal adjustment)
    {
        this.adjustment = adjustment;
    }

    public BigDecimal getPstTax()
    {
        return pstTax;
    }

    public void setPstTax(BigDecimal pstTax)
    {
        this.pstTax = pstTax;
    }

    public Long getCustomerId()
    {
        if (getCustomer() != null) {
            return getCustomer().getId();
        } else {
            return null;
        }
    }

    @JsonIgnore
    public Customer getCustomer()
    {
        return customer;
    }

    public void setCustomer(Customer customer)
    {
        this.customer = customer;
    }

    public Integer getExcludeFromClaim()
    {
        return excludeFromClaim;
    }

    public void setExcludeFromClaim(Integer excludeFromClaim)
    {
        this.excludeFromClaim = excludeFromClaim;
    }

    public Long getOrderId()
    {
        return orderId;
    }

    public void setOrderId(Long orderId)
    {
        this.orderId = orderId;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public List<CustomerOrder> getOrders()
    {
        return orders;
    }

    public void setOrders(List<CustomerOrder> orders)
    {
        this.orders = orders;
    }

    public List<AppliedCredit> getCredits()
    {
        return credits;
    }

    public void setCredits(List<AppliedCredit> credits)
    {
        this.credits = credits;
    }

    public BigDecimal amountRemaining()
    {
        return getAmount().subtract(getPaidAmount()).subtract(getCreditedAmount());
    }

    public boolean fullyPaid()
    {
        return amountRemaining().compareTo(BigDecimal.ZERO) <= 0;
    }

    public BigDecimal applyCredit(Credit credit, BigDecimal limit)
    {
        BigDecimal limitRemaining = limit;

        log.debug(String.format("APPLY CREDIT INVOICE %b amount %10.2f remaining %10.2f paid %10.2f",
                                fullyPaid(),
                                getAmount(),
                                amountRemaining(),
                                getPaidAmount()));

        log.debug(String.format("APPLY CREDIT CREDIT amount %10.2f remaining %10.2f",
                                credit.getAmount(),
                                credit.getAmountRemaining()));

        if (!fullyPaid() && credit.hasRemaining()) {
            BigDecimal creditAvailable = credit.getAmountRemaining();

            if (limit != null && limit.compareTo(creditAvailable) < 0) {
                // credit exceeds limit, subtract
                creditAvailable = limit;
            }

            BigDecimal difference = creditAvailable.subtract(amountRemaining());
            BigDecimal amountApplied = BigDecimal.ZERO;

            switch (difference.compareTo(BigDecimal.ZERO)) {
            case -1:
                // Not enough to cover invoice, use whole amount
                credit.setAmountRemaining(BigDecimal.ZERO);
                amountApplied = creditAvailable;
                break;

            default:
                // Credit is larger than or equal to the amount remaining on
                // invoice, invoice is completely paid
                credit.setAmountRemaining(difference);
                amountApplied = difference;
                break;
            }

            setCreditedAmount(getCreditedAmount().add(amountApplied));

            AppliedCredit applied = new AppliedCredit();
            applied.setAmount(amountApplied);
            applied.setInvoiceId(getId());
            applied.setCreditId(credit.getId());

            if (credits == null) {
                credits = new ArrayList<AppliedCredit>(1);
            }

            applied.setSequence(credits.size());
            credits.add(applied);

            if (limit != null) {
                limitRemaining = limit.subtract(amountApplied);
            }
        }

        return limitRemaining;
    }

    public void applyPayment(AppliedPayment appliedPayment)
    {
        setPaidAmount(getPaidAmount().add(appliedPayment.getAmount()));

        if (getPayments() == null) {
            setPayments(new ArrayList<AppliedPayment>(1));
        }

        appliedPayment.setSequence(getPayments().size());

        getPayments().add(appliedPayment);
    }

    public List<AppliedPayment> getPayments()
    {
        return payments;
    }

    public void setPayments(List<AppliedPayment> payments)
    {
        this.payments = payments;
    }

    public List<Customer> findOrderCustomers()
    {
        List<Customer> customers = new ArrayList<Customer>(1);

        if (this.getOrders() != null) {
            for (CustomerOrder order : this.getOrders()) {
                if (order.getCustomer() != null && !customers.contains(order.getCustomer())) {
                    customers.add(order.getCustomer());
                }
            }
        }

        return customers;
    }

    /**
     * Add invoice id to all associated charges
     */
    public void markRates()
    {
        log.debug("INVOICE MARK RATES");

        for (CustomerOrder order : getOrders()) {
            log.debug("ADDING ORDER " + order);

            for (Charge charge : order.getCharges()) {
                if (charge.getInvoiceId() == null) {
                    // Don't add charges already invoiced
                    log.debug("ADDING CHARGE " + charge);
                    charge.setInvoiceId(getId());
                }
            }
        }
    }

    /**
     * Add up all rates
     */
    public BigDecimal sumRates()
    {
        BigDecimal total = BigDecimal.ZERO;

        log.debug("INVOICE SUM RATES");

        for (CustomerOrder order : getOrders()) {
            log.debug("ADDING ORDER " + order);

            for (Charge charge : order.getCharges()) {
                if (getId().equals(charge.getInvoiceId())) {
                    total = total.add(charge.getTotal());
                }
            }
        }

        return total;
    }

    public List<Charge> getCharges()
    {
        return charges;
    }

    public void cancel()
    {
        status = CANCELLED;
    }

    public boolean isCancelled()
    {
        return CANCELLED.equals(status);
    }

    public String toString()
    {
        return "INVOICE " + getId();
    }
}
