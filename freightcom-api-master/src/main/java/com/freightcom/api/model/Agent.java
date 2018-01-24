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
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.Formula;

import com.freightcom.api.util.StringList;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;

/**
 *
 *
 * @author
 */
@Entity(name = "Agent")
@Table(name = "agent")
@DiscriminatorValue("AGENT")
@Cacheable(value = true)
public class Agent extends UserRole
{

    public enum Term {
        NEVER, WEEKLY, BIWEEKLY, MONTHLY
    };

    private static final long serialVersionUID = 1L;

    @Column(name = "agent_name")
    private String agentName;

    @Column(name = "promo_code")
    private String promoCode;

    @OneToOne(cascade = { CascadeType.ALL })
    @JoinColumn(name = "address_id", referencedColumnName = "id", nullable = true)
    private ShippingAddress address;

    @ManyToOne(cascade = { CascadeType.REFRESH })
    @JoinColumn(name = "parent_sales_agent_id")
    private UserRole parentSalesAgent;

    @OneToMany(mappedBy = "parentSalesAgent", cascade = { CascadeType.REFRESH })
    private List<Agent> subagents = new ArrayList<Agent>();

    @Column(name = "commission_percent")
    private Float commissionPercent;

    @Column(name = "comm_wo_op_expense")
    private BigDecimal commWoOpExpense;

    @Column(name = "mfw_key")
    private String mfwKey;

    @Column(name = "comm_courier_op_expense")
    private BigDecimal commCourierOpExpense;

    @Column(name = "comm_pallet_op_expense")
    private BigDecimal commPalletOpExpense;

    @Column(name = "deleted")
    private Integer deleted;

    @Column(name = "term")
    @Enumerated(EnumType.STRING)
    private Term term = Term.MONTHLY;

    @Column(name = "allow_new_order")
    private Boolean allowNewOrders;

    @Column(name = "view_invoices")
    private Boolean viewInvoices;

    @Formula("(select count(*) from customer where customer.sub_agent_id=id)")
    private Long customerCount;

    @Formula("(select sum(commission_total_amount(P.id)) from commission_payable P where P.agent_id = id and P.paid_at is null)")
    private BigDecimal unpaidCommission = BigDecimal.ZERO;

    @Formula("(select sum(commission_total_amount(P.id)) from commission_payable P where P.agent_id = id and P.paid_at is not null)")
    private BigDecimal paidCommission = BigDecimal.ZERO;

    @Formula("(select agent_name(parent_sales_agent_id))")
    private String parentSalesAgentName;

    public Agent()
    {
        super();
    }

    public String getAgentName()
    {
        if (agentName == null || agentName.isEmpty()) {
            User user = getUser();

            if (user == null) {
                return "";
            } else {
                return StringList.get()
                        .add(user.getFirstname())
                        .add(user.getLastname())
                        .join(" ");
            }
        } else {
            return agentName;
        }
    }

    public String getPhone()
    {
        User user = getUser();

        if (user == null) {
            return "";
        } else {
            return user.getPhone();
        }
    }

    public void setAgentName(String agentName)
    {
        this.agentName = agentName;
    }

    public String getParentSalesAgentName()
    {
        return parentSalesAgentName;
    }

    public void setParentSalesAgentName(String name)
    {
        parentSalesAgentName = name;
    }

    public ShippingAddress getAddress()
    {
        return address;
    }

    public void setAddress(ShippingAddress address)
    {
        this.address = address;
    }

    @Override
    public Long getParentSalesAgentId()
    {
        if (getParentSalesAgent() != null) {
            return getParentSalesAgent().getId();
        } else {
            return null;
        }
    }

    public String getPromoCode()
    {
        return promoCode;
    }

    public void setPromoCode(String promoCode)
    {
        this.promoCode = promoCode;
    }

    public Float getCommissionPercent()
    {
        return commissionPercent;
    }

    public void setCommissionPercent(Float commissionPercent)
    {
        this.commissionPercent = commissionPercent;
    }

    public BigDecimal getCommWoOpExpense()
    {
        return commWoOpExpense;
    }

    public void setCommWoOpExpense(BigDecimal commWoOpExpense)
    {
        this.commWoOpExpense = commWoOpExpense;
    }

    public String getMfwKey()
    {
        return mfwKey;
    }

    public void setMfwKey(String mfwKey)
    {
        this.mfwKey = mfwKey;
    }

    public BigDecimal getCommCourierOpExpense()
    {
        return commCourierOpExpense;
    }

    public void setCommCourierOpExpense(BigDecimal commCourierOpExpense)
    {
        this.commCourierOpExpense = commCourierOpExpense;
    }

    public BigDecimal getCommPalletOpExpense()
    {
        return commPalletOpExpense;
    }

    public void setCommPalletOpExpense(BigDecimal commPalletOpExpense)
    {
        this.commPalletOpExpense = commPalletOpExpense;
    }

    public Integer getDeleted()
    {
        return deleted;
    }

    public void setDeleted(Integer deleted)
    {
        this.deleted = deleted;
    }

    public Long getCustomerCount()
    {
        return customerCount;
    }

    @Override
    public boolean isAgent()
    {
        return true;
    }

    @Override
    public Agent asAgent()
    {
        return this;
    }

    @Override
    public String getRoleName()
    {
        return ROLE_AGENT;
    }

    @Override
    public String getPrettyRoleName()
    {
        return "Agent";
    }

    public List<Agent> getSubagents()
    {
        return subagents;
    }

    public void setSubagents(List<Agent> subagents)
    {
        this.subagents = subagents;
    }

    public Agent getParentSalesAgent()
    {
        return parentSalesAgent == null ? null : parentSalesAgent.asAgent();
    }

    public void setParentSalesAgent(Agent parentSalesAgent)
    {
        this.parentSalesAgent = parentSalesAgent;
    }

    public Term getTerm()
    {
        return term == null ? Term.MONTHLY : term;
    }

    public void setTerm(Term term)
    {
        this.term = term;
    }

    public BigDecimal getPaidCommission()
    {
        return paidCommission;
    }

    public void setPaidCommission(BigDecimal paidCommission)
    {
        this.paidCommission = paidCommission;
    }

    public BigDecimal getUnpaidCommission()
    {
        return unpaidCommission;
    }

    public void setUnpaidCommission(BigDecimal unpaidCommission)
    {
        this.unpaidCommission = unpaidCommission;
    }

    public Boolean getViewInvoices()
    {
        return viewInvoices;
    }

    public void setViewInvoices(Boolean viewInvoices)
    {
        this.viewInvoices = viewInvoices;
    }

    public Boolean getAllowNewOrders()
    {
        return allowNewOrders;
    }

    public void setAllowNewOrders(Boolean allowNewOrders)
    {
        this.allowNewOrders = allowNewOrders;
    }

    @Override
    public Agent findRootAgent()
    {
        if (parentSalesAgent == null) {
            return this;
        } else {
            return parentSalesAgent.findRootAgent();
        }
    }

    public boolean isSubAgentOf(Agent agent)
    {
        if (this.equals(agent)) {
            return true;
        } else if (parentSalesAgent == null || parentSalesAgent.asAgent() == null) {
            return false;
        } else {
            return parentSalesAgent.asAgent()
                    .isSubAgentOf(agent);
        }
    }

    @Override
    public boolean isAgentFor(Customer customer)
    {
        if (customer == null) {
            return false;
        } else if (this.equals(customer.getSalesAgent())) {
            return true;
        } else {
            return descendantAgents().stream()
                    .anyMatch(agent -> agent.equals(customer.getSalesAgent()));
        }
    }

    public List<Agent> descendantAgents()
    {
        List<Agent> descendants = new ArrayList<Agent>(subagents);

        for (Agent subAgent : subagents) {
            descendants.addAll(subAgent.descendantAgents());
        }

        return descendants;
    }
}
