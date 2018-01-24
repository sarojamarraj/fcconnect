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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *
 *
 * @author
 */
@Entity(name = "Payable")
@Table(name = "payable")
public class Payable extends TransactionalEntity implements PayableEntity
{

    private static final long serialVersionUID = 1L;
    @Transient
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "due_at")
    private Date dueAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "paid_at")
    private Date paidAt;

    @Column(name = "paid_amount")
    private BigDecimal paidAmount = BigDecimal.ZERO;
    
    private String chequeNumber;

    @Formula("(IF(paid_at is null, 'unpaid', 'paid'))")
    private String status;

    @Column(name = "currency")
    private String currency;

    @OneToMany()
    @JoinColumn(name = "payable_id", referencedColumnName = "id")
    private List<Charge> charges = new ArrayList<Charge>();

    @ManyToOne
    @JoinColumn(name = "service_id")
    private Service service;

    @Formula("(select IFNULL(sum(IFNULL(CH.quantity,0) * IFNULL(CH.cost, 0)), 0) from charge CH where CH.payable_id=id )")
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @Transient
    private List<CustomerOrder> orders = new ArrayList<CustomerOrder>();

    @Transient
    private List<GroupedCharge> groupedCharges = new ArrayList<GroupedCharge>();

    @Formula("(select count(distinct CO.id) from charge CH left join customer_order CO on CO.id=CH.order_id where CH.payable_id=id)")
    private Integer orderCount = 0;

    @Formula("(select count(*) from charge where charge.payable_id=id)")
    private Integer chargeCount = 0;

    @Transient
    private Address fromAddress;

    public Payable()
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
    public ZonedDateTime getCreatedAt()
    {
        return asDate(createdAt);
    }

    public void setCreatedAt(Date createdAt)
    {
        this.createdAt = legacyDate(createdAt);
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

    public void setDueAt(ZonedDateTime dueAt)
    {
        this.dueAt = legacyDate(dueAt);
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

    public void setPaidAt(ZonedDateTime paidAt)
    {
        this.paidAt = legacyDate(paidAt);
    }

    public Long getServiceId()
    {
        return service == null ? null : service.getId();
    }

    public String getServiceName()
    {
        return service == null ? null : service.getName();
    }

    public String getStatus()
    {
        return paidAt == null ? "unpaid" : "paid";
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getCurrency()
    {
        return currency;
    }

    public void setCurrency(String currency)
    {
        this.currency = currency;
    }


    public List<Charge> getCharges()
    {
        return charges;
    }

    public void setCharges(List<Charge> charges)
    {
        this.charges = charges;
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

    public List<CustomerOrder> getOrders()
    {
        Map<Long, Boolean> seen = new HashMap<Long, Boolean>();
        List<CustomerOrder> orders = new ArrayList<CustomerOrder>();

        for (Charge charge : getCharges()) {
            log.debug("CCCCCC " + charge);
            CustomerOrder order = charge.getOrder();

            if (order != null && order.getId() == null) {
                log.debug("ORDER ID IS NULL " + charge + " " + order);
            }

            if (order != null && seen.get(order.getId()) == null) {
                orders.add(order);
                seen.put(order.getId(), true);
            }
        }

        return orders;
    }

    public BigDecimal getOrderCost(CustomerOrder order)
    {
        BigDecimal total = BigDecimal.ZERO;

        for (Charge charge : getCharges()) {
            if (order == charge.getOrder()) {
                log.debug("GET ORDER COST MATCH");
                total = total.add(charge.getTotalCost());
            }

            log.debug("GET ORDER COST " + order + " " + charge + " " + charge.getTotalCost() + " " + total);
        }

        return total;
    }

    public BigDecimal getPaidAmount()
    {
        return paidAmount;
    }

    public void setPaidAmount(BigDecimal paidAmount)
    {
        this.paidAmount = paidAmount;
    }

    public BigDecimal getTotalAmount()
    {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount)
    {
        this.totalAmount = totalAmount;
    }

    public Payable addCharge(Charge charge)
    {
        charge.setPayable(this);
        charges.add(charge);

        return this;
    }

    public void setOrders(List<CustomerOrder> orders)
    {
        this.orders = orders;
    }

    public List<GroupedCharge> getGroupedCharges()
    {
        return groupedCharges;
    }

    public void setGroupedCharges(List<GroupedCharge> groupedCharges)
    {
        this.groupedCharges = groupedCharges;
    }

    public Integer getOrderCount()
    {
        return orderCount;
    }

    public void setOrderCount(Integer orderCount)
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

    public Address getFromAddress()
    {
        return fromAddress;
    }

    public void setFromAddress(Address fromAddress)
    {
        this.fromAddress = fromAddress;
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

    public void markPaid()
    {
        paidAt = Date.from(Instant.now());
        paidAmount = totalAmount;
    }

    @Override
    public void markPaid(Integer chequeNumber, ZonedDateTime transactionDate)
    {
        setPaidAt(transactionDate);
        setChequeNumber(chequeNumber);
        paidAmount = totalAmount;
    }

    @Override
    public boolean isPaid()
    {
        return "paid".equals(status);
    }

    @Override
    public String getName()
    {
        // TODO - correct cheque name for service
        return service.getCarrierServiceName();
    }
}
