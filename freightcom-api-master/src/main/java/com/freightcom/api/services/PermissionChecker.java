package com.freightcom.api.services;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import com.freightcom.api.ApiSession;
import com.freightcom.api.model.Credit;
import com.freightcom.api.model.CurrencyExchange;
import com.freightcom.api.model.Customer;
import com.freightcom.api.model.CustomerOrder;
import com.freightcom.api.model.Invoice;
import com.freightcom.api.model.TransactionalEntity;
import com.freightcom.api.model.User;
import com.freightcom.api.model.UserRole;

/**
 * @author bryan
 *
 */
@Component
public class PermissionChecker
{
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final ApiSession apiSession;

    @Autowired
    public PermissionChecker(final ApiSession apiSession)
    {
        this.apiSession = apiSession;
    }

    public ApiSession getApiSession()
    {
        return apiSession;
    }

    public boolean isFreightcom()
    {
        return apiSession.isFreightcom();
    }

    public boolean isAgent()
    {
        return apiSession.isAgent();
    }

    public boolean isAdmin()
    {
        return apiSession.isAdmin();
    }

    public boolean isFreightcomOrAdmin()
    {
        return apiSession.isAdmin() || apiSession.isFreightcom();
    }

    public void checkFreightcom()
    {
        if (! isFreightcom()) {
            throw new AccessDeniedException("Must be freightcom");
        }
    }

    public boolean isCustomer()
    {
        log.debug("IS CUSTOMER " + apiSession);
        return apiSession.isCustomer() && apiSession.getCustomerId() != null;
    }

    public void checkCustomer()
    {
        if (! isCustomer()) {
            throw new AccessDeniedException("Must be customer " + apiSession.getRole());
        }
    }

    public void checkCriteria(Map<String, Object> criteria)
    {
        if (apiSession.isAgent()) {
            Long agentId = apiSession.getRole()
                    .getId();

            if (agentId == null) {
                throw new AccessDeniedException("Bad agent");
            }

            if (criteria.get("agentId") == null) {
                criteria.put("agentId", agentId);
            } else if (!agentId.equals(criteria.get("agentId"))) {
                throw new AccessDeniedException("Bad agent");
            }
        } else if (apiSession.isCustomer()) {
            Long customerId = apiSession.getCustomerId();

            if (customerId == null) {
                throw new AccessDeniedException("Bad customer");
            }

            if (criteria.get("customerId") == null) {
                criteria.put("customerId", customerId);
            } else if (!customerId.equals(criteria.get("customerId"))) {
                throw new AccessDeniedException("Bad customer");
            }
        }
    }

    /**
     *
     */
    public PermissionChecker check(final Invoice invoice)
    {
        if (invoice.isCancelled() && ! apiSession.isFreightcom()) {
            throw new AccessDeniedException("Cancelled invoice");
        }

        if (apiSession.isAgent()) {
            Long agentId = apiSession.getRole()
                    .getId();

            if (agentId == null) {
                throw new AccessDeniedException("Bad agent");
            }

            boolean ok = false;

            if (invoice.getOrders() != null) {
                ok = true;

                for (CustomerOrder order : invoice.getOrders()) {
                    if (!agentId.equals(order.getAgentId())) {
                        ok = false;
                        break;
                    }
                }
            }

            if (!ok) {
                throw new AccessDeniedException("Bad agent");
            }
        } else if (apiSession.isCustomer()) {
            Long customerId = apiSession.getCustomerId();

            if (customerId == null) {
                throw new AccessDeniedException("Bad customer");
            }

            if (!customerId.equals(invoice.getCustomerId())) {
                throw new AccessDeniedException("Bad customer");
            }

        }

        return this;
    }

    public PermissionChecker check(final Customer customer)
    {
        if (apiSession.isAgent()) {
            Long agentId = apiSession.getRole()
                    .getId();

            if (agentId == null) {
                throw new AccessDeniedException("Bad agent");
            }

            if (customer.getSalesAgent() == null) {
                throw new AccessDeniedException("Bad agent");
            }

            if (! agentId.equals(customer.getSalesAgent().getId())) {
                throw new AccessDeniedException("Bad agent");
            }
        } else if (apiSession.isCustomer()) {
            Long customerId = apiSession.getCustomerId();

            if (customerId == null) {
                throw new AccessDeniedException("Bad customer");
            }

            if (!customerId.equals(customer.getId())) {
                throw new AccessDeniedException("Bad customer");
            }
        }

        return this;
    }

    public void checkCreate(final Invoice invoice)
    {
        if (apiSession.isAgent()) {
            Long agentId = apiSession.getRole()
                    .getId();

            if (agentId == null) {
                throw new AccessDeniedException("Bad agent");
            }

            boolean ok = false;

            if (invoice.getOrders() != null) {
                ok = true;

                for (CustomerOrder order : invoice.getOrders()) {
                    if (!agentId.equals(order.getAgentId())) {
                        ok = false;
                        break;
                    }
                }
            }

            if (!ok) {
                throw new AccessDeniedException("Bad agent");
            }
        } else if (apiSession.isCustomer()) {
            throw new AccessDeniedException("Customer can't create invoice");
        }
    }

    public PermissionChecker check(CurrencyExchange currencyExchange)
    {
        UserRole role = apiSession.getRole();

        if (role == null || role.isCustomer() || role.isAgent()) {
            throw new AccessDeniedException("Bad currency administrator");
        }

        return this;
    }

    /**
     * @param object
     */
    public void setUpdatedBy(TransactionalEntity object)
    {
        if (apiSession.getUser() != null) {
            object.setUpdatedBy(apiSession.getUser()
                    .getLogin());
        }
    }


    public Long getUserId()
    {
        return apiSession.getUser().getId();
    }

    public Long getCustomerId()
    {
        return apiSession.getCustomerId();
    }

    public boolean isLoggedInCustomer(Customer customer)
    {
        return customer != null
            && apiSession.getCustomerId() != null
            && apiSession.getCustomerId().equals(customer.getId());
    }

    public PermissionChecker check(final Credit credit)
    {
        if (apiSession.isAgent()) {
            boolean ok = false;

            if (!ok) {
                throw new AccessDeniedException("Bad agent");
            }
        } else if (apiSession.isCustomer()) {
            Long customerId = apiSession.getCustomerId();

            if (customerId == null) {
                throw new AccessDeniedException("Bad customer");
            }

            if (!customerId.equals(credit.getCustomerId())) {
                throw new AccessDeniedException("Bad customer");
            }

        }

        return this;
    }

    public User getUser()
    {
        User user = null;
        UserRole role = apiSession.getRole();

        if (role != null) {
            user = role.getUser();
        }

        return user;
    }

    public UserRole getRole()
    {
        return apiSession.getRole();
    }
}
