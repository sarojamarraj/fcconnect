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

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

/**
 *
 *
 * @author
 */
@Entity(name = "Charge")
@Table(name = "charge")
public class Charge extends TransactionalEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="baserate")
    private String baserate;

    @Column(name="code")
    private String code;

    @Column(name="charge")
    private BigDecimal charge = BigDecimal.ZERO;

    @Column(name="apply_commission")
    private Boolean applyCommission = true;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="created_at")
    private Date createdAt;

    @Column(name="unit_type")
    private String unitType;

    @Column(name="seq_order")
    private String seqOrder;

    @Column(name="final_total_value")
    private BigDecimal finalTotalValue;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="updated_at")
    private Date updatedAt;

    @Column(name="charge_id")
    private Long chargeId;

    @Column(name="unit_value")
    private Float unitValue;

    @Column(name="invoice_id")
    private Long invoiceId;

    @Column(name="currency")
    private String currency;

    @Column(name="rate_unit")
    private Float rateUnit;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="deleted_at")
    private Date deletedAt;

    @Column(name="subtotal")
    private BigDecimal subtotal;

    @Column(name="quantity")
    private Integer quantity = 1;

    @Column(name="description")
    private String description;

    @Column(name="final_total_value_charge")
    private BigDecimal finalTotalValueCharge;

    @ManyToOne
    @JoinColumn(name = "accessorial_id")
    private OrderAccessorials accessorial;

    @Column(name="cost")
    private BigDecimal cost = BigDecimal.ZERO;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="reconciled_at")
    private Date reconciledAt;

    @Column(name="commission")
    private BigDecimal commission;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="commission_calculated_at")
    private Date commissionCalculatedAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="reported_at")
    private Date reportedAt;

    @ManyToOne
    private Service service;

    @ManyToOne
    private CustomerOrder order;

    @ManyToOne
    private Payable payable;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="disputed_at")
    private Date disputedAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="dispute_resolved_at")
    private Date disputedResolvedAt;

    @Column(name="taxable")
    private Boolean taxable = Boolean.TRUE;

    @ManyToMany
    @JoinTable(name = "charge_dispute", joinColumns = @JoinColumn(name = "charge_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "logged_event_id", referencedColumnName = "id"))
    @OrderColumn(name = "position")
    private List<LoggedEvent> disputeThread = new ArrayList<LoggedEvent>();

    @OneToMany
    @JoinColumn(name = "charge_id", referencedColumnName = "id")
    private List<ChargeCommission> chargeCommission;

    @OneToMany(targetEntity = ChargeTax.class, mappedBy = "charge", cascade = { CascadeType.ALL })
    private List<ChargeTax> taxes = new ArrayList<ChargeTax>(0);

    @ManyToOne
    private Agent agent;

    public Charge() {
        super();
    }

    public Charge(Charge from) {
        super();

        id = from.id;
        baserate = from.baserate;
        code = from.code;
        charge = from.charge;
        applyCommission = from.applyCommission;
        createdAt = from.createdAt;
        unitType = from.unitType;
        seqOrder = from.seqOrder;
        finalTotalValue = from.finalTotalValue;
        updatedAt = from.updatedAt;
        chargeId = from.chargeId;
        unitValue = from.unitValue;
        currency = from.currency;
        rateUnit = from.rateUnit;
        subtotal = from.subtotal;
        quantity = from.quantity;
        description = from.description;
        finalTotalValueCharge = from.finalTotalValueCharge;
        accessorial = from.getAccessorial();
        cost = from.getCost();
        reconciledAt = from.reconciledAt;
        commission = from.commission;
        commissionCalculatedAt = from.commissionCalculatedAt;
        reportedAt = from.reportedAt;
        service = from.getService();
        order = from.getOrder();
        payable = from.getPayable();
        chargeCommission = from.getChargeCommission();
        taxes = from.getTaxes();
        agent = from.getAgent();
    }


    public Long getId()
    {
         return id;
    }

    public void setId(Long id)
    {
         this.id = id;
    }


    public String getBaserate()
    {
         return baserate;
    }

    public void setBaserate(String baserate)
    {
         this.baserate = baserate;
    }


    public BigDecimal getTotal()
    {
         return getTotalTax().add(getSubTotal());
    }


    public String getCode()
    {
         return code;
    }

    public void setCode(String code)
    {
         this.code = code;
    }


    public BigDecimal getCharge()
    {
         return charge == null ? BigDecimal.ZERO : charge;
    }

    public void setCharge(BigDecimal charge)
    {
         this.charge = charge;
    }


    public Boolean getApplyCommission()
    {
         return applyCommission == null ? true : applyCommission;
    }

    public void setApplyCommission(Boolean applyCommission)
    {
         this.applyCommission = applyCommission;
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


    public String getUnitType()
    {
         return unitType;
    }

    public void setUnitType(String unitType)
    {
         this.unitType = unitType;
    }


    public String getSeqOrder()
    {
         return seqOrder;
    }

    public void setSeqOrder(String seqOrder)
    {
         this.seqOrder = seqOrder;
    }


    public BigDecimal getFinalTotalValue()
    {
         return finalTotalValue;
    }

    public void setFinalTotalValue(BigDecimal finalTotalValue)
    {
         this.finalTotalValue = finalTotalValue;
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


    public Long getChargeId()
    {
         return chargeId;
    }

    public void setChargeId(Long chargeId)
    {
         this.chargeId = chargeId;
    }


    public Float getUnitValue()
    {
         return unitValue;
    }

    public void setUnitValue(Float unitValue)
    {
         this.unitValue = unitValue;
    }


    public Long getInvoiceId()
    {
         return invoiceId;
    }

    public void setInvoiceId(Long invoiceId)
    {
         this.invoiceId = invoiceId;
    }

    public String getCurrency()
    {
         return currency;
    }

    public void setCurrency(String currency)
    {
         this.currency = currency;
    }

    public Float getRateUnit()
    {
         return rateUnit;
    }

    public void setRateUnit(Float rateUnit)
    {
         this.rateUnit = rateUnit;
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
    public ZonedDateTime getCommissionCalculatedAt()
    {
         return asDate(commissionCalculatedAt);
    }

    public void setCommissionCalculatedAt(ZonedDateTime commissionCalculatedAt)
    {
         this.commissionCalculatedAt = legacyDate(commissionCalculatedAt);
    }


    public Integer getQuantity()
    {
         return quantity;
    }

    public void setQuantity(Integer quantity)
    {
         this.quantity = quantity;
    }


    public String getDescription()
    {
         return description;
    }

    public void setDescription(String description)
    {
         this.description = description;
    }


    public BigDecimal getFinalTotalValueCharge()
    {
         return finalTotalValueCharge;
    }

    public void setFinalTotalValueCharge(BigDecimal finalTotalValueCharge)
    {
         this.finalTotalValueCharge = finalTotalValueCharge;
    }


    public Long getOrderIdx()
    {
        if (getOrder() != null) {
            return getOrder().getId();
        } else {
            return null;
        }
    }

    public Long getAccessorialId()
    {
         return getAccessorial() == null ? null : getAccessorial().getId();
    }

    public Long getAccessorialServiceId()
    {
         return getAccessorial() == null ? null : getAccessorial().getAccessorialServiceId();
    }

    public BigDecimal getCost()
    {
         return cost == null ? BigDecimal.ZERO : cost;
    }

    public void setCost(BigDecimal cost)
    {
         this.cost = cost;
    }


    public boolean getReconciled()
    {
         return reconciledAt != null;
    }

    public void setReconciled(boolean reconciled)
    {
        if (reconciled && reconciledAt == null) {
            reconciledAt = now();
        } else if (! reconciled) {
            reconciledAt = null;
        }
    }

    @JsonIgnore
    public Carrier getCarrier()
    {
         return service == null ? null : service.getCarrier();
    }


    @JsonIgnore
    public Service getService()
    {
         return service;
    }

    public void setService(Service service)
    {
         this.service = service;
    }

    public BigDecimal getSubTotal()
    {
        if (charge == null || quantity == null) {
            return BigDecimal.ZERO;
        }

        return charge.multiply(new BigDecimal(quantity));
    }

    public BigDecimal getTotalCost()
    {
        if (cost == null || quantity == null) {
            return BigDecimal.ZERO;
        }

        return cost.multiply(new BigDecimal(quantity));
    }

    public BigDecimal getTotalTax()
    {
       return getTaxes().stream().map(tax -> tax.getAmount()).reduce(BigDecimal.ZERO, (a, b) -> a.add(b));
    }


    public List<ChargeTax> getTaxes()
    {
        return taxes;
    }


    public void setTaxes(List<ChargeTax> taxes)
    {
        this.taxes = taxes;
    }

    public ChargeTax addTax(String name, String taxId, double rate)
    {
        ChargeTax tax = new ChargeTax(name, taxId, rate, getSubTotal());

        tax.setCharge(this);
        taxes.add(tax);

        return tax;
    }

    @JsonIgnore
    public CustomerOrder getOrder()
    {
        return order;
    }


    public void setOrder(CustomerOrder order)
    {
        this.order = order;
    }


    public String toString()
    {
        return "charge " +  id + " " + (order == null ? "null" : order.getId())
            + " " + cost + " " + description + " " + accessorial + " " + agent
            + disputedAt;
    }


    public ZonedDateTime getReportedAt()
    {
        return asDate(reportedAt);
    }


    public void setReportedAt(ZonedDateTime reportedAt)
    {
        this.reportedAt = legacyDate(reportedAt);
    }

    @JsonIgnore
    public Payable getPayable()
    {
        return payable;
    }

    @JsonProperty(access = Access.READ_ONLY)
    public void setPayable(Payable payable)
    {
        this.payable = payable;
    }

    @JsonIgnore
    public List<ChargeCommission> getChargeCommission()
    {
        return chargeCommission;
    }

    @JsonIgnore
    public Agent getAgent()
    {
        return agent;
    }

    public void setAgent(Agent agent)
    {
        this.agent = agent;
    }

    public BigDecimal getMarkup()
    {
        if (getCharge() != null
            && getCost() != null
            && getCharge().compareTo(BigDecimal.ZERO) > 0) {

            return getCharge().subtract(getCost());

        } else {
            return BigDecimal.ZERO;
        }
    }

    public boolean isBilled()
    {
        return invoiceId != null;
    }

    public BigDecimal getCommission()
    {
        // TODO - apply commission formula
        return BigDecimal.ZERO;
    }

    public String getName()
    {
        if (getAccessorial() == null) {
            return null;
        } else {
            return getAccessorial().getAccessorialName();
        }
    }


    @JsonIgnore
    public OrderAccessorials getAccessorial()
    {
        return accessorial;
    }


    public void setAccessorial(OrderAccessorials accessorial)
    {
        this.accessorial = accessorial;
    }

    public ZonedDateTime getDisputedAt()
    {
         return asDate(disputedAt);
    }

    public Charge dispute()
    {
        disputedAt = new Date();

        return this;
    }

    public boolean isDisputed()
    {
        return disputedAt != null && disputedResolvedAt == null;
    }

    public Charge resolveDispute()
    {
        disputedResolvedAt = new Date();

        return this;
    }

    public ZonedDateTime getDisputeResolvedAt()
    {
         return asDate(disputedResolvedAt);
    }

    public List<LoggedEvent> getDisputeThread()
    {
        return disputeThread;
    }

    public Charge addDisputeComment(LoggedEvent event) {
        disputeThread.add(event);

        return this;
    }

    public Boolean getTaxable()
    {
         return taxable;
    }

    public void setTaxable(Boolean taxable)
    {
         this.taxable = taxable;
    }

    public BigDecimal getProfit()
    {
        if (cost == null) {
            return charge == null ? BigDecimal.ZERO : charge;
        } else {
            return charge == null ? BigDecimal.ZERO : charge.subtract(cost);
        }
    }
}
