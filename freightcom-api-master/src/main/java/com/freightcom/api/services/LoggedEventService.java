package com.freightcom.api.services;

import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.ConfigurablePropertyAccessor;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.freightcom.api.ApiSession;
import com.freightcom.api.model.LoggedEvent;
import com.freightcom.api.model.UserRole;
import com.freightcom.api.model.views.LoggedEventView;
import com.freightcom.api.repositories.custom.LoggedEventRepository;
import com.freightcom.api.repositories.custom.LoggedEventSpecification;
import com.freightcom.api.services.converters.LoggedEventConverter;

/**
 * @author bryan
 *
 */
@Component
public class LoggedEventService
{
    @SuppressWarnings("unused")
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final LoggedEventRepository loggedEventRepository;

    private final PagedResourcesAssembler<LoggedEvent> pagedAssembler;
    private final PagedResourcesAssembler<LoggedEventView> loggedEventAssembler;
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public LoggedEventService(final LoggedEventRepository loggedEventRepository,
            final PagedResourcesAssembler<LoggedEvent> pagedAssembler,
            final PagedResourcesAssembler<LoggedEventView> loggedEventAssembler,
            final ApiSession apiSession)
    {
        this.loggedEventRepository = loggedEventRepository;
        this.pagedAssembler = pagedAssembler;
        this.loggedEventAssembler = loggedEventAssembler;
    }

    /**
     * @param criteria
     * @param pageable
     * @return
     */
    public PagedResources<Resource<LoggedEvent>> getLoggedEvents(Map<String,Object> criteria, Pageable pageable)
    {
        Page<LoggedEvent> loggedEvents = loggedEventRepository.findAll(new LoggedEventSpecification(criteria), pageable);

        return pagedAssembler.toResource(loggedEvents, new Link("/logged_event"));
    }

    /**
     * @param criteria
     * @param pageable
     * @return
     */
    public PagedResources<Resource<LoggedEventView>> getLoggedEventsConverted(Map<String,Object> criteria, Pageable pageable)
    {
        Page<LoggedEventView> loggedEvents = loggedEventRepository.findAll(new LoggedEventSpecification(criteria), pageable)
                .map(new LoggedEventConverter());

        return loggedEventAssembler.toResource(loggedEvents, new Link("/logged_event"));
    }

    /**
     * @param loggedEvent
     * @return
     * @throws Exception
     */
    @Transactional
    public LoggedEvent createOrUpdateLoggedEvent(LoggedEvent loggedEvent) throws Exception
    {
        loggedEventRepository.save(loggedEvent);

        return loggedEvent;
    }

    /**
     * @throws Exception
     *
     */
    public LoggedEvent createLoggedEvent(final LoggedEvent loggedEvent) throws Exception
    {
        LoggedEvent newLoggedEvent;

        if (loggedEvent.getId() != null) {
            newLoggedEvent = new LoggedEvent();
            BeanUtils.copyProperties(loggedEvent, newLoggedEvent);
            newLoggedEvent.setId(null);
        } else {
            newLoggedEvent = loggedEvent;
        }

        return createOrUpdateLoggedEvent(newLoggedEvent);
    }

    /**
     *
     */
    @Transactional
    public LoggedEvent updateLoggedEvent(Long id, LoggedEvent loggedEvent, Map<String,String> attributes, UserDetails loggedInUser, UserRole userRole) throws Exception
    {
        LoggedEvent existing = loggedEventRepository.findOne(id);

        if (existing == null) {
            throw new ResourceNotFoundException("No such loggedEvent");
        }

        ConfigurablePropertyAccessor source = PropertyAccessorFactory.forDirectFieldAccess(loggedEvent);
        ConfigurablePropertyAccessor dest = PropertyAccessorFactory.forDirectFieldAccess(existing);

        // Only copy attributes supplied in the JSON input, leave others alone
        for (String key : attributes.keySet()) {
            if (dest.isWritableProperty(key)) {
                dest.setPropertyValue(key, source.getPropertyValue(key));
            }
        }

        return createOrUpdateLoggedEvent(existing);
    }

    /**
     *
     */
    @Transactional
    public String deleteLoggedEvent(Long loggedEventId, UserDetails loggedInUser)
    {
       LoggedEvent loggedEvent = loggedEventRepository.findOne(loggedEventId);

       if (loggedEvent == null) {
           throw new ResourceNotFoundException("No such loggedEvent");
       }

       loggedEventRepository.delete(loggedEvent);

       return "ok";
    }
}
