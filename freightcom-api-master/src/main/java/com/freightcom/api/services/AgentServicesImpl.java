package com.freightcom.api.services;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.ConfigurablePropertyAccessor;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.freightcom.api.ApiSession;
import com.freightcom.api.events.AgentDeletedEvent;
import com.freightcom.api.model.Agent;
import com.freightcom.api.model.Customer;
import com.freightcom.api.model.UserRole;
import com.freightcom.api.repositories.ObjectBase;
import com.freightcom.api.repositories.custom.CustomerRepository;
import com.freightcom.api.repositories.custom.UserRoleRepository;
import com.freightcom.api.repositories.custom.UserRoleSpecification;

@Component
public class AgentServicesImpl extends ServicesCommon implements AgentServices
{
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    UserRoleRepository userRoleRepository;

    @Autowired
    ObjectBase objectBase;

    /**
     *
     */
    @Autowired
    public AgentServicesImpl(final ApiSession apiSession)
    {
        super(apiSession);
    }

    @EventListener
    @Transactional
    public void handleRoleDeletedEvent(AgentDeletedEvent event) throws Exception
    {
        try {
            log.debug("AGENT DELETED " + event);
            reassignCustomers(event.getRole(), event.getReassignment());
        } catch (Exception e) {
            log.error("EXCEPTION IN AgentDeletedEvent LISTENER " + e.getMessage(), e);
            throw e;
        }
    }

    /**
     * If no destination agent ID is given, then accounts get transfered to the
     * parent agent. If there is no parent, then the destination agent ID is
     * mandatory and operation will fail if not given.
     *
     * @throws Exception
     */
    private void reassignCustomers(final UserRole role, final String reassignment) throws Exception
    {
        log.debug("REASSIGN CUSTOMER EVENT " + reassignment);
        Long reassignToId = null;

        if (reassignment != null) {
            reassignToId = Long.parseLong(reassignment, 10);
        } else {
            reassignToId = role.asAgent()
                    .getParentSalesAgentId();
        }

        UserRole reassignTo = reassignToId == null ? null : userRoleRepository.findOne(reassignToId);

        if (reassignTo != null) {

            log.debug("REASSIGN TO AGENT " + reassignTo + " id " + reassignToId);

            for (Customer customer : customerRepository.findBySalesAgent_Id(role.getId())) {
                if (reassignTo == null) {
                    ValidationException.get()
                            .add("reassignments", "No reassignment for deleted sales agent")
                            .doThrow();
                }

                log.debug("AGENT CUSTOMER " + customer + " " + reassignTo);
                customer.setSalesAgent(reassignTo.asAgent());
                customerRepository.save(customer);
            }
        }

    }

    @Override
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('FREIGHTCOM_STAFF') or hasAuthority('AGENT')")
    public Agent getAgent(Long agentId)
    {
        try {
            log.debug("FETCHING AGENT " + agentId);
            Agent agent = userRoleRepository.findById(agentId)
                    .asAgent();

            if (apiSession.isAgent() && ! agent.isSubAgentOf(apiSession.getRole().asAgent())) {
                throw new AccessDeniedException("Not authorized");
            }

            return agent;
        } catch (Exception e) {
            log.debug("PROBLEM " + agentId + " " + e);
            throw new ResourceNotFoundException("No agent " + agentId);
        }
    }

    @Override
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('FREIGHTCOM_STAFF') or hasAuthority('AGENT')")
    public Page<UserRole> listAgents(Map<String, String> criteria, Pageable pageable)
    {
        if (criteria == null) {
            criteria = new HashMap<String, String>();
        }

        if (apiSession.isAgent()) {
            criteria.put("ancestorid", apiSession.getRole().getId().toString());
        }

        criteria.put("type", "agent");

        return objectBase.findAgents(new UserRoleSpecification(criteria), pageable);
    }

    @Override
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('FREIGHTCOM_STAFF') or hasAuthority('AGENT')")
    public Agent update(Long agentId, Agent agentData, Map<String, Object> attributes) throws ValidationException, AccessDeniedException
    {
        Agent agent = getAgent(agentId);

        if (agent == null) {
            throw new ResourceNotFoundException("Not found " + agentId);
        }

        if (apiSession.isAgent() && ! agent.isSubAgentOf(apiSession.getRole().asAgent())) {
            throw new AccessDeniedException("Not authorized");
        }

        ConfigurablePropertyAccessor source = PropertyAccessorFactory.forDirectFieldAccess(agentData);
        ConfigurablePropertyAccessor dest = PropertyAccessorFactory.forDirectFieldAccess(agent);

        // Only copy attributes supplied in the JSON input, leave others alone
        for (String key : attributes.keySet()) {
            if (key.equalsIgnoreCase("parentAgentId") || key.equalsIgnoreCase("parentSalesAgentId") ) {
                if (attributes.get(key) == null || attributes.get(key).toString().isEmpty()) {
                     agent.setParentSalesAgent(null);
                     agent.setParentSalesAgentName(null);
                } else {
                    Agent parent = getAgent(Long.parseLong(attributes.get(key)
                                                           .toString()));

                    if (parent == null) {
                        ValidationException.get()
                            .add("parentAgentId", "invalid value")
                            .doThrow();
                    } else if (parent.isSubAgentOf(agent)) {
                        ValidationException.get()
                            .add("parentAgentId", "Can't be subagent of descendant")
                            .doThrow();
                    } else {
                        agent.setParentSalesAgent(parent);
                        agent.setParentSalesAgentName(parent.getAgentName());
                    }
                }
            } else {
                dest.setPropertyValue(key, source.getPropertyValue(key));
            }
        }

        return agent;
    }

}
