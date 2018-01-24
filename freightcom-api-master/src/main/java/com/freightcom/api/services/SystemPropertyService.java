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
import com.freightcom.api.model.SystemProperty;
import com.freightcom.api.model.UserRole;
import com.freightcom.api.model.views.SystemPropertyView;
import com.freightcom.api.repositories.custom.SystemPropertyRepository;
import com.freightcom.api.repositories.custom.SystemPropertySpecification;
import com.freightcom.api.services.converters.SystemPropertyConverter;

/**
 * @author bryan
 *
 */
@Component
public class SystemPropertyService
{
    @SuppressWarnings("unused")
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final SystemPropertyRepository systemPropertyRepository;

    private final PagedResourcesAssembler<SystemProperty> pagedAssembler;
    private final PagedResourcesAssembler<SystemPropertyView> systemPropertyAssembler;
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public SystemPropertyService(final SystemPropertyRepository systemPropertyRepository,
            final PagedResourcesAssembler<SystemProperty> pagedAssembler,
            final PagedResourcesAssembler<SystemPropertyView> systemPropertyAssembler,
            final ApiSession apiSession)
    {
        this.systemPropertyRepository = systemPropertyRepository;
        this.pagedAssembler = pagedAssembler;
        this.systemPropertyAssembler = systemPropertyAssembler;
    }

    public PagedResources<Resource<SystemProperty>> getSystemPropertys(Map<String,String> criteria, Pageable pageable)
    {
        Page<SystemProperty> systemPropertys = systemPropertyRepository.findAll(new SystemPropertySpecification(criteria), pageable);

        return pagedAssembler.toResource(systemPropertys);
    }

    public PagedResources<Resource<SystemPropertyView>> getSystemPropertysConverted(Map<String,String> criteria, Pageable pageable)
    {
        Page<SystemPropertyView> systemPropertys = systemPropertyRepository.findAll(new SystemPropertySpecification(criteria), pageable)
                .map(new SystemPropertyConverter());

        return systemPropertyAssembler.toResource(systemPropertys, new Link("/customerSystemProperty"));
    }

    @Transactional
    public SystemProperty createOrUpdateSystemProperty(SystemProperty systemProperty) throws Exception
    {
        systemPropertyRepository.save(systemProperty);

        return systemProperty;
    }

    /**
     * @throws Exception
     *
     */
    public SystemProperty createSystemProperty(final SystemProperty systemProperty) throws Exception
    {
        SystemProperty newSystemProperty;

        if (systemProperty.getId() != null) {
            newSystemProperty = new SystemProperty();
            BeanUtils.copyProperties(systemProperty, newSystemProperty);
        } else {
            newSystemProperty = systemProperty;
        }

        return createOrUpdateSystemProperty(newSystemProperty);
    }

    /**
     *
     */
    @Transactional
    public SystemProperty updateSystemProperty(Long id, SystemProperty systemProperty, Map<String,String> attributes, UserDetails loggedInUser, UserRole userRole) throws Exception
    {
        SystemProperty existing = systemPropertyRepository.findOne(id);

        if (existing == null) {
            throw new ResourceNotFoundException("No such systemProperty");
        }

        ConfigurablePropertyAccessor source = PropertyAccessorFactory.forDirectFieldAccess(systemProperty);
        ConfigurablePropertyAccessor dest = PropertyAccessorFactory.forDirectFieldAccess(existing);

        // Only copy attributes supplied in the JSON input, leave others alone
        for (String key : attributes.keySet()) {
            if (dest.isWritableProperty(key)) {
                dest.setPropertyValue(key, source.getPropertyValue(key));
            }
        }

        return createOrUpdateSystemProperty(existing);
    }

    /**
     *
     */
    @Transactional
    public String deleteSystemProperty(Long systemPropertyId, UserDetails loggedInUser)
    {
       SystemProperty systemProperty = systemPropertyRepository.findOne(systemPropertyId);

       if (systemProperty == null) {
           throw new ResourceNotFoundException("No such systemProperty");
       }

       systemPropertyRepository.delete(systemProperty);

       return "ok";
    }
}
