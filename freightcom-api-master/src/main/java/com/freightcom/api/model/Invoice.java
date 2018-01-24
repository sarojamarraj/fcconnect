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
import java.time.Duration;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
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
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.freightcom.api.model.views.PaymentHistoryView;

/**
 *
 *
 * @author
 */
@Entity(name = "Invoice")
@Table(name = "invoice")
@SQLDelete(sql = "update invoice SET deleted_at = UTC_TIMESTAMP() where id=?")
@Where(clause = "deleted_at is null")
public class Invoice extends TransactionalEntity implements OwnedByCustomer
{

    private static final long serialVersionUID = 1L;
    private static final String CANCELLED = "cancelled";

    @Transient
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Transient
    private List<PaymentHistoryView> paymentHistory;

    @Transient
    private List<LoggedEvent> messages = new ArrayList<LoggedEvent>();

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "notes")
    private String notes;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createdAt;

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

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "modified_due_date")
    private Date modifiedDueDate;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "due_at")
    private Date dueAt;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "effective_at")
    private Date invoiceDate;

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

    @Column(name = "payment_type")
    private Integer paymentType;

    @Column(name = "adjustment")
    private BigDecimal adjustment;

    @Column(name = "exclude_from_claim")
    private Integer excludeFromClaim;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToMany
    @JoinTable(name = "order_invoice_workorder", joinColumns = @JoinColumn(name = "order_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "elt", referencedColumnName = "id"))
    @OrderColumn(name = "position")
    private List<CustomerOrder> orders = new ArrayList<CustomerOrder>();

    @Column(name = "status")
    private String status;

    @ManyToMany
    @JoinTable(name = "applied_payment", joinColumns = @JoinColumn(name = "payment_transaction_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "invoice_id", referencedColumnName = "id"))
    private List<PaymentTransaction> paymentTransactions = new ArrayList<PaymentTransaction>();

    @OneToMany(mappedBy = "invoiceId", cascade = { CascadeType.ALL })
    @OrderColumn(name = "sequence")
    private List<AppliedCredit> credits = new ArrayList<AppliedCredit>(0);

    @OneToMany(mappedBy = "invoiceId", cascade = { CascadeType.ALL })
    @OrderColumn(name = "sequence")
    private List<AppliedPayment> payments = new ArrayList<AppliedPayment>(0);

    @OneToMany()
    @JoinColumn(name = "invoice_id", referencedColumnName = "id")
    private List<Charge> charges = new ArrayList<Charge>(0);

    @Transient
    private List<GroupedCharge> groupedCharges = new ArrayList<GroupedCharge>();

    @Formula("(select IF(((select IFNULL(sum(IFNULL(T.amount,0)),0) from applied_payment T where T.invoice_id=id) + (select IFNULL(sum(IFNULL(AC.amount,0)),0) from applied_credit AC where AC.invoice_id=id)) >= (select IFNULL(sum(IFNULL(C.quantity,0) * IFNULL(C.charge, 0) + IFNULL((select sum(CT.amount) from charge_tax CT where CT.charge_id=C.id and CT.amount != 0),0)),0) from charge C where C.invoice_id=id), 1, 0) as 'paymentStatus')")
    private Integer paymentStatus;

    @Formula("(select IFNULL(sum(IFNULL(C.quantity,0) * IFNULL(C.charge, 0) + IFNULL((select sum(CT.amount) from charge_tax CT where CT.charge_id=C.id and CT.amount != 0),0)),0) as 'total' from charge C where C.invoice_id=id)")
    private BigDecimal amount = BigDecimal.ZERO;

    @Formula("(select IFNULL(sum(IFNULL((select sum(CT.amount) from charge_tax CT where CT.charge_id=C.id and CT.amount != 0),0)),0) as 'total' from charge C where C.invoice_id=id)")
    private BigDecimal tax = BigDecimal.ZERO;

    @Formula("(select IFNULL(sum(IFNULL(T.amount,0)),0) as 'paidAmount' from applied_payment T where T.invoice_id=id)")
    private BigDecimal paidAmount = BigDecimal.ZERO;

    @Formula("(select IFNULL(sum(IFNULL(T.amount,0)),0) as 'creditedAmount' from applied_credit T where T.invoice_id=id)")
    private BigDecimal creditedAmount = BigDecimal.ZERO;

    @Formula("((select IFNULL(sum(IFNULL(T.amount,0)),0) as 'creditedAmount' from applied_credit T where T.invoice_id=id) - IFNULL((select IFNULL(sum(IFNULL(T.amount,0)),0) as 'paidAmount' from applied_payment T where T.invoice_id=id), 0))")
    private BigDecimal amountRemaining = BigDecimal.ZERO;

    public Invoice()
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

    public String getNotes()
    {
        return notes;
    }

    public void setNotes(String notes)
    {
        this.notes = notes;
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Override
    public ZonedDateTime getCreatedAt()
    {
        return asDate(createdAt);
    }

    public void setCreatedAt(Date createdAt)
    {
        this.createdAt = createdAt;
    }

    @Override
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

    @JsonFormat(pattern = "yyyy-MM-dd")
    public ZonedDateTime getModifiedDueDate()
    {
        return asDate(modifiedDueDate);
    }

    public void setModifiedDueDate(Date modifiedDueDate)
    {
        this.modifiedDueDate = modifiedDueDate;
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

    public Integer getPaymentStatus()
    {
        return paymentStatus;
    }

    public boolean isPaid()
    {
        return paymentStatus != null && paymentStatus != 0;
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

    public BigDecimal getAmountRemaining()
    {
        return amountRemaining;
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

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public BigDecimal getSubtotal()
    {
        log.debug("INVOICE SUBTOTAL " + getAmount() + " st " + getTax());

        return getAmount().subtract(getTax());
    }

    public List<CustomerOrder> getOrders()
    {
        if (orders.size() > 200) {
            throw new Error("TOO MANY ORDERS " + getId() + " count " + orders.size());
        }

        return orders;
    }

    public void setOrders(List<CustomerOrder> orders)
    {
        this.orders = orders;
    }

    public void addOrder(CustomerOrder order)
    {
        orders.add(order);
    }

    public List<AppliedCredit> getCredits()
    {
        return credits;
    }

    public void setCredits(List<AppliedCredit> credits)
    {
        this.credits = credits;
    }

    public String debugPaymentStatus()
    {
        return this + " fullyPaid " + fullyPaid() + " amountRemaining " + amountRemaining() + " amount " + getAmount()
                + " paid " + getPaidAmount() + " credited " + getCreditedAmount();
    }

    public BigDecimal amountRemaining()
    {
        amountRemaining = getAmount().subtract(getPaidAmount())
                .subtract(getCreditedAmount());

        log.debug(String.format("INVOICE AMOUNT REMAINING\nAmount %s\nPaid %s\nCredited %s\nAmount Remaining %s\n",
                getAmount().toString(), getPaidAmount().toString(), getCreditedAmount().toString(), amountRemaining)
                .toString());

        return amountRemaining;
    }

    public boolean fullyPaid()
    {
        return amountRemaining().compareTo(BigDecimal.ZERO) <= 0;
    }

    public BigDecimal applyCredit(Credit credit, BigDecimal limit)
    {
        BigDecimal limitRemaining = limit;

        log.debug(String.format("APPLY CREDIT INVOICE %b amount %10.2f remaining %10.2f paid %10.2f", fullyPaid(),
                getAmount(), amountRemaining(), getPaidAmount()));

        log.debug(String.format("APPLY CREDIT CREDIT amount %10.2f remaining %10.2f", credit.getAmount(),
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
                amountApplied = amountRemaining();
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
        log.debug("INVOICE MARK RATES " + this);

        for (CustomerOrder order : getOrders()) {
            log.debug("ADDING ORDER " + order);

            for (Charge charge : order.getCharges()) {
                if (charge.getInvoiceId() == null) {
                    // Don't add charges already invoiced
                    log.debug("ADDING CHARGE " + charge + " " + id);
                    charge.setInvoiceId(id);
                } else {
                    log.debug("HAS ID " + charge.getInvoiceId());
                }
            }
        }
    }

    /**
     * Add up all charges
     */
    public BigDecimal sumRates()
    {
        BigDecimal total = BigDecimal.ZERO;

        log.debug("INVOICE SUM CHARGES (sumRates)");

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

    /**
     * Add up all taxes
     */
    public BigDecimal sumTaxes()
    {
        BigDecimal total = BigDecimal.ZERO;

        log.debug("INVOICE SUM TAXES");

        for (CustomerOrder order : getOrders()) {
            for (Charge charge : order.getCharges()) {
                if (getId().equals(charge.getInvoiceId())) {
                    total = total.add(charge.getTotalTax());
                }
            }
        }

        return total;
    }

    /**
     * Add up all charges that would be included in invoice, i.e. all charges
     * not yet invoiced
     */
    public BigDecimal sumNewCharges()
    {
        BigDecimal total = BigDecimal.ZERO;

        for (CustomerOrder order : getOrders()) {
            for (Charge charge : order.getCharges()) {
                if (charge.getInvoiceId() == null) {
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
        return "INVOICE " + (getId() != null ? getId() : "not saved");
    }

    public List<PaymentTransaction> getPaymentTransactions()
    {
        return paymentTransactions;
    }

    public void setPaymentTransactions(List<PaymentTransaction> paymentTransactions)
    {
        this.paymentTransactions = paymentTransactions;
    }

    public List<GroupedCharge> getGroupedCharges()
    {
        return groupedCharges;
    }

    public void setGroupedCharges(List<GroupedCharge> groupedCharges)
    {
        this.groupedCharges = groupedCharges;
    }

    public List<ChargeTax> getTaxes()
    {
        List<ChargeTax> taxes = new ArrayList<ChargeTax>();

        for (Charge charge : charges) {
            for (ChargeTax tax : charge.getTaxes()) {
                taxes.add(tax);
            }
        }

        return taxes;
    }

    public void computeDueDateIfNotSet()
    {
        if (getDueAt() == null) {
            Integer terms = getCustomer().getInvoiceTerm();

            if (terms == null) {
                terms = 30;
            }

            setDueAt(Date.from(Instant.now()
                    .plus(Duration.ofDays(terms))
                    .truncatedTo(ChronoUnit.DAYS)));
        }
    }

    public String getCurrency()
    {
        // TODO order currencies
        if (orders.size() < 1) {
            return null;
        } else {
            return orders.get(0).getSelectedQuote().getCurrency();
        }
    }

    /**
     * Return false if invoice may not be auto charged
     */
    public boolean autoInvoiceAllowed()
    {
        // TODO
        return true;
    }

    public List<LoggedEvent> getMessages()
    {
        return messages;
    }

    public void setMessages(List<LoggedEvent> messages)
    {
        this.messages = messages;
    }

    public List<PaymentHistoryView> getPaymentHistory()
    {
        if (paymentHistory == null) {
            paymentHistory = new ArrayList<PaymentHistoryView>();

            for (AppliedPayment payment : getPayments()) {
                paymentHistory.add(new PaymentHistoryView(payment));
            }

            for (AppliedCredit credit : getCredits()) {
                paymentHistory.add(new PaymentHistoryView(credit));
            }

            paymentHistory.sort((h1, h2) -> h1.getPaymentDate()
                    .isEqual(h2.getPaymentDate()) ? 0
                            : (h1.getPaymentDate()
                                    .isBefore(h2.getPaymentDate()) ? -1 : 0));
        }

        return paymentHistory;
    }

    public BigDecimal mostRecentCredit()
    {
        if (credits.size() > 0) {
            return credits.get(0).getAmount();
        } else {
            return BigDecimal.ZERO;
        }
    }
}
