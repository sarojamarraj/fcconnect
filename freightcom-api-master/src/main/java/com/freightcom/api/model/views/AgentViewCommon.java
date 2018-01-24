package com.freightcom.api.model.views;


import java.math.BigDecimal;

import org.springframework.hateoas.core.Relation;
import com.freightcom.api.model.Agent;
import com.freightcom.api.model.UserRole;

@Relation(collectionRelation = "agent")
public class AgentViewCommon extends UserRoleView
{
    private final Agent agent;

    public AgentViewCommon(Agent agent) throws Exception
    {
        super();
        this.agent = agent;

        if (agent == null) {
            throw new Exception("NO AGENT TO VIEW");
        }
    }

    @Override
    public UserRole roleObject()
    {
        return agent;
    }

    protected Agent agent()
    {
        return agent;
    }

    public Long getUserId()
    {
        return agent.getUser() == null ? null : agent.getUser().getId();
    }

    public String getName()
    {
        return agent.getAgentName();
    }

    public Long getCustomerCount()
    {
        return agent.getCustomerCount();
    }

    public Float getCommissionPercent()
    {
        return agent.getCommissionPercent();
    }

    public BigDecimal getPaidCommission()
    {
        if (agent.getPaidCommission() == null) {
            return BigDecimal.ZERO;
        } else {
            return agent.getPaidCommission();
        }
    }

    public String getPhone()
    {
        return agent.getPhone();
    }

    public BigDecimal getUnpaidCommission()
    {
        if (agent.getUnpaidCommission() == null) {
            return  BigDecimal.ZERO;
        } else {
            return agent.getUnpaidCommission();
        }
    }

    public Long getParentSalesAgentId()
    {
        if (agent.getParentSalesAgent() != null) {
            return agent.getParentSalesAgent().getId();
        } else {
            return null;
        }
    }

    public String getParentSalesAgentName()
    {
        return agent.getParentSalesAgentName();
    }

    public Boolean getViewInvoices()
    {
        return agent.getViewInvoices();
    }

    public Boolean getAllowNewOrders()
    {
        return agent.getAllowNewOrders();
    }

    public Object getTerm()
    {
        return agent.getTerm();
    }

    public Object getAddress()
    {
        if (agent.getAddress() == null) {
            return null;
        } else {
            return new ShippingAddressView(agent.getAddress());
        }
    }

}
