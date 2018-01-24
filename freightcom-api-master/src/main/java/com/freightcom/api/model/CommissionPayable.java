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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Formula;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *
 *
 * @author
 */
@Entity(name = "CommissionPayable")
@Table(name = "commission_payable")
public class CommissionPayable extends TransactionalEntity implements PayableEntity
{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "paid_amount")
    private BigDecimal paidAmount;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "paid_at")
    private Date paidAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "due_at")
    private Date dueAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(name = "description")
    private String description;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "currency")
    private String currency;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "deleted_at")
    private Date deletedAt;
    
    private String chequeNumber;

    @Formula("(IF(paid_at is null, 'unpaid', 'paid'))")
    private String status;

    @ManyToOne
    @JoinColumn(name = "agent_id")
    private Agent agent;

    @OneToMany()
    @JoinColumn(name = "commission_payable_id", referencedColumnName = "id")
    private List<ChargeCommission> chargeCommissions = new ArrayList<ChargeCommission>();

    @Formula("(select sum(CC.amount) from charge_commission CC where CC.commission_payable_id=id )")
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @Formula("(select count(*) from charge_commission CC where CC.commission_payable_id=id)")
    private Integer chargeCount = 0;

    @Formula("(select count(distinct CHA.order_id) from charge_commission CCX left join charge CHA on CHA.id=CCX.charge_id where CCX.commission_payable_id=id)")
    private Long orderCount = 0L;

    @Transient
    private Address fromAddress;

    @Transient
    private List<GroupedCharge> groupedCharges = new ArrayList<GroupedCharge>(0);

    public CommissionPayable()
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

    @JsonFormat(pattern = "yyyy-MM-dd")
    public ZonedDateTime getUpdatedAt()
    {
        return asDate(updatedAt);
    }

    public void setUpdatedAt(ZonedDateTime updatedAt)
    {
        this.updatedAt = legacyDate(updatedAt);
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
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

    public String getCurrency()
    {
        return currency;
    }

    public void setCurrency(String currency)
    {
        this.currency = currency;
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

    public String getStatus()
    {
        return paidAt == null ? "unpaid" : "paid";
    }

    public void setStatus(String status)
    {
        this.status = status;
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

    public BigDecimal getPaidAmount()
    {
        return paidAmount == null ? BigDecimal.ZERO : paidAmount;
    }

    public void setPaidAmount(BigDecimal paidAmount)
    {
        this.paidAmount = paidAmount;
    }

    public ZonedDateTime getPaidAt()
    {
        return asDate(paidAt);
    }

    public void setPaidAt(Date paidAt)
    {
        this.paidAt = paidAt;
    }

    public void setPaidAt(ZonedDateTime paidAt)
    {
        this.paidAt = legacyDate(paidAt);
    }

    public ZonedDateTime getDueAt()
    {
        return asDate(dueAt);
    }

    public void setDueAt(ZonedDateTime dueAt)
    {
        this.dueAt = legacyDate(dueAt);
    }

    public BigDecimal getTotalAmount()
    {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount)
    {
        this.totalAmount = totalAmount;
    }

    public void addChargeCommission(ChargeCommission chargeCommission)
    {
        getChargeCommissions().add(chargeCommission);

        chargeCount = chargeCommissions.size();
        orderCount = chargeCommissions.stream()
                .map(commission -> commission.getCharge()
                        .getOrder())
                .distinct()
                .count();
    }

    public List<ChargeCommission> getChargeCommissions()
    {
        return chargeCommissions;
    }

    public void setChargeCommissions(List<ChargeCommission> chargeCommissions)
    {
        this.chargeCommissions = chargeCommissions;

        if (chargeCommissions == null) {
            this.chargeCount = 0;
            this.orderCount = 0L;
        } else {
            this.chargeCount = chargeCommissions.size();

            this.orderCount = chargeCommissions.stream()
                    .map(commission -> commission.getCharge()
                            .getOrder())
                    .distinct()
                    .count();
        }
    }

    public Long getOrderCount()
    {
        return orderCount;
    }

    public void setOrderCount(Long orderCount)
    {
        this.orderCount = orderCount;
    }

    public Integer getChargeCount()
    {
        return chargeCount;
    }

    public void setChargeCount(Integer chargeCount)
    {
        this.chargeCount = chargeCount;
    }

    public List<Charge> getCharges()
    {
        return getChargeCommissions().stream()
                .map(commission -> commission.getCharge())
                .distinct()
                .collect(Collectors.toList());
    }

    public List<CustomerOrder> getOrders()
    {
        return getChargeCommissions().stream()
                .map(commission -> commission.getCharge())
                .map(charge -> charge.getOrder())
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * Calculate the commission amount attributable to an order
     */
    public BigDecimal getOrderCommission(CustomerOrder order)
    {
        BigDecimal total = BigDecimal.ZERO;

        for (ChargeCommission commission : getChargeCommissions()) {
            Charge charge = commission.getCharge();

            if (charge != null && charge.getOrder() == order) {
                total = total.add(commission.getAmount());
            }
        }

        return total;
    }

    /**
     * Calculate the order total charge for the agent
     */
    public BigDecimal getOrderCharge(CustomerOrder order)
    {
        BigDecimal total = BigDecimal.ZERO;

        for (ChargeCommission commission : getChargeCommissions()) {
            Charge charge = commission.getCharge();

            if (charge != null && charge.getOrder() == order) {
                total = total.add(charge.getSubTotal());
            }
        }

        return total;
    }

    /**
     * See notes/charge.txt
     */
    public Collection<?> getGroupedCharges()
    {
        Map<String, GroupedChargeCommission> builder = new HashMap<String, GroupedChargeCommission>();

        for (ChargeCommission commission : getChargeCommissions()) {
            Charge charge = commission.getCharge();
            String key = charge.getAccessorialServiceId() + " x " + charge.getDescription();

            GroupedChargeCommission group = builder.get(key);

            if (group == null) {
                builder.put(key, new GroupedChargeCommission(charge.getAccessorialServiceId(), charge.getDescription(),
                        charge.getSubTotal(), new BigDecimal(charge.getTotalTax()
                                .toString()),
                        charge.getTotalCost(), commission.getAmount()));
            } else {
                group.setTotal(group.getTotal()
                        .add(charge.getSubTotal()));
                group.setTotalTax(group.getTotalTax()
                        .add(charge.getTotalTax()));
                group.setCost(group.getCost()
                        .add(charge.getTotalCost()));
                group.setCommission(group.getCommission()
                        .add(commission.getAmount()));
            }
        }

        return builder.values();
    }

    /**
     * See notes/charge.txt
     */
    public Collection<OrderChargeGroup> getByOrderByCharge()
    {
        Map<Long, OrderChargeGroup> builder = new HashMap<Long, OrderChargeGroup>();
        List<OrderChargeGroup> result = new ArrayList<OrderChargeGroup>();

        for (ChargeCommission commission : getChargeCommissions()) {
            Charge charge = commission.getCharge();
            CustomerOrder order = charge.getOrder();

            OrderChargeGroup orderChargeGroup = builder.get(order.getId());

            if (orderChargeGroup == null) {
                orderChargeGroup = new OrderChargeGroup(order);
                builder.put(order.getId(), orderChargeGroup);
                result.add(orderChargeGroup);
            }

            Map<String, GroupedChargeCommission> chargeGroupMap = orderChargeGroup.getChargeGroupMap();

            String key = charge.getAccessorialServiceId() + " x " + charge.getDescription();

            GroupedChargeCommission group = chargeGroupMap.get(key);

            if (group == null) {
                chargeGroupMap.put(key, new GroupedChargeCommission(charge.getAccessorialServiceId(),
                        charge.getDescription(), charge.getSubTotal(), new BigDecimal(charge.getTotalTax()
                                .toString()),
                        charge.getTotalCost(), commission.getAmount()));
            } else {
                group.setTotal(group.getTotal()
                        .add(charge.getSubTotal()));
                group.setTotalTax(group.getTotalTax()
                        .add(charge.getTotalTax()));
                group.setCost(group.getCost()
                        .add(charge.getTotalCost()));
                group.setCommission(group.getCommission()
                        .add(commission.getAmount()));
            }
        }

        return result;
    }

    public Address getFromAddress()
    {
        return fromAddress;
    }

    public void setFromAddress(Address fromAddress)
    {
        this.fromAddress = fromAddress;
    }

    public void markPaid()
    {
        paidAt = Date.from(Instant.now());
        paidAmount = totalAmount;
    }

    public void markPaid(Integer chequeNumber, ZonedDateTime transactionDate)
    {
        setPaidAt(transactionDate);
        setChequeNumber(chequeNumber);
        paidAmount = totalAmount;
    }

    public String getChequeNumber()
    {
        return chequeNumber;
    }

    
    public void setChequeNumber(String chequeNumber)
    {
        this.chequeNumber = chequeNumber;
    }

    public void setChequeNumber(Integer chequeNumber)
    {
        this.chequeNumber = chequeNumber == null ? null : chequeNumber.toString();
    }

    public class OrderChargeGroup
    {
        private CustomerOrder order;
        private Map<String, GroupedChargeCommission> chargeGroupMap = new HashMap<String, GroupedChargeCommission>();

        public OrderChargeGroup(CustomerOrder order)
        {
            this.order = order;
        }

        @JsonIgnore
        public Map<String, GroupedChargeCommission> getChargeGroupMap()
        {
            return chargeGroupMap;
        }

        public Long getId()
        {
            return order.getId();
        }

        public BigDecimal getTotalCharge()
        {
            return order.getTotalCharge();
        }

        public Collection<?> getGroupedCharges()
        {
            return chargeGroupMap.values();
        }
    }

    @Override
    public boolean isPaid()
    {
        return "paid".equals(status);
    }

    @Override
    public String getName()
    {
        return agent.getAgentName();
    }
}
