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
import com.freightcom.api.model.Alert;
import com.freightcom.api.model.UserRole;
import com.freightcom.api.model.views.AlertView;
import com.freightcom.api.repositories.custom.AlertRepository;
import com.freightcom.api.repositories.custom.AlertSpecification;
import com.freightcom.api.services.converters.AlertConverter;

/**
 * @author bryan
 *
 */
@Component
public class AlertService
{
    @SuppressWarnings("unused")
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final AlertRepository alertRepository;

    private final PagedResourcesAssembler<Alert> pagedAssembler;
    private final PagedResourcesAssembler<AlertView> alertAssembler;
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public AlertService(final AlertRepository alertRepository,
            final PagedResourcesAssembler<Alert> pagedAssembler,
            final PagedResourcesAssembler<AlertView> alertAssembler,
            final ApiSession apiSession)
    {
        this.alertRepository = alertRepository;
        this.pagedAssembler = pagedAssembler;
        this.alertAssembler = alertAssembler;
    }

    public PagedResources<Resource<Alert>> getAlerts(Map<String,String> criteria, Pageable pageable)
    {
        Page<Alert> alerts = alertRepository.findAll(new AlertSpecification(criteria), pageable);

        return pagedAssembler.toResource(alerts);
    }

    public PagedResources<Resource<AlertView>> getAlertsConverted(Map<String,String> criteria, Pageable pageable)
    {
        Page<AlertView> alerts = alertRepository.findAll(new AlertSpecification(criteria), pageable)
                .map(new AlertConverter());

        return alertAssembler.toResource(alerts, new Link("/customerAlert"));
    }

    @Transactional
    public Alert createOrUpdateAlert(Alert alert) throws Exception
    {
        alertRepository.save(alert);

        return alert;
    }

    /**
     * @throws Exception
     *
     */
    public Alert createAlert(final Alert alert) throws Exception
    {
        Alert newAlert;

        if (alert.getId() != null) {
            newAlert = new Alert();
            BeanUtils.copyProperties(alert, newAlert);
        } else {
            newAlert = alert;
        }

        return createOrUpdateAlert(newAlert);
    }

    /**
     *
     */
    @Transactional
    public Alert updateAlert(Long id, Alert alert, Map<String,String> attributes, UserDetails loggedInUser, UserRole userRole) throws Exception
    {
        Alert existing = alertRepository.findOne(id);

        if (existing == null) {
            throw new ResourceNotFoundException("No such alert");
        }

        ConfigurablePropertyAccessor source = PropertyAccessorFactory.forDirectFieldAccess(alert);
        ConfigurablePropertyAccessor dest = PropertyAccessorFactory.forDirectFieldAccess(existing);

        // Only copy attributes supplied in the JSON input, leave others alone
        for (String key : attributes.keySet()) {
            if (dest.isWritableProperty(key)) {
                dest.setPropertyValue(key, source.getPropertyValue(key));
            }
        }

        return createOrUpdateAlert(existing);
    }

    /**
     *
     */
    @Transactional
    public String deleteAlert(Long alertId, UserDetails loggedInUser)
    {
       Alert alert = alertRepository.findOne(alertId);

       if (alert == null) {
           throw new ResourceNotFoundException("No such alert");
       }

       alertRepository.delete(alert);

       return "ok";
    }

    public void save(Alert alert) {
        alertRepository.save(alert);        
    }
}
