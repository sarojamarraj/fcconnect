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
import com.freightcom.api.model.Service;
import com.freightcom.api.model.UserRole;
import com.freightcom.api.model.views.ServiceView;
import com.freightcom.api.repositories.custom.ServiceRepository;
import com.freightcom.api.repositories.custom.ServiceSpecification;
import com.freightcom.api.services.converters.ServiceConverter;

/**
 * @author bryan
 *
 */
@Component
public class ServiceService
{
    @SuppressWarnings("unused")
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final ServiceRepository serviceRepository;

    private final PagedResourcesAssembler<Service> pagedAssembler;
    private final PagedResourcesAssembler<ServiceView> serviceAssembler;
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public ServiceService(final ServiceRepository serviceRepository,
            final PagedResourcesAssembler<Service> pagedAssembler,
            final PagedResourcesAssembler<ServiceView> serviceAssembler,
            final ApiSession apiSession)
    {
        this.serviceRepository = serviceRepository;
        this.pagedAssembler = pagedAssembler;
        this.serviceAssembler = serviceAssembler;
    }

    public PagedResources<Resource<Service>> getServices(Map<String,Object> criteria, Pageable pageable)
    {
        Page<Service> services = serviceRepository.findAll(new ServiceSpecification(criteria), pageable);

        return pagedAssembler.toResource(services);
    }

    public PagedResources<Resource<ServiceView>> getServicesConverted(Map<String,Object> criteria, Pageable pageable)
    {
        Page<ServiceView> services = serviceRepository.findAll(new ServiceSpecification(criteria), pageable)
                .map(new ServiceConverter());

        return serviceAssembler.toResource(services, new Link("/customerService"));
    }

    @Transactional
    public Service createOrUpdateService(Service service) throws Exception
    {
        serviceRepository.save(service);

        return service;
    }

    /**
     * @throws Exception
     *
     */
    public Service createService(final Service service) throws Exception
    {
        Service newService;

        if (service.getId() != null) {
            newService = new Service();
            BeanUtils.copyProperties(service, newService);
        } else {
            newService = service;
        }

        return createOrUpdateService(newService);
    }

    /**
     *
     */
    @Transactional
    public Service updateService(Long id, Service service, Map<String,String> attributes, UserDetails loggedInUser, UserRole userRole) throws Exception
    {
        Service existing = serviceRepository.findOne(id);

        if (existing == null) {
            throw new ResourceNotFoundException("No such service");
        }

        ConfigurablePropertyAccessor source = PropertyAccessorFactory.forDirectFieldAccess(service);
        ConfigurablePropertyAccessor dest = PropertyAccessorFactory.forDirectFieldAccess(existing);

        // Only copy attributes supplied in the JSON input, leave others alone
        for (String key : attributes.keySet()) {
            if (dest.isWritableProperty(key)) {
                dest.setPropertyValue(key, source.getPropertyValue(key));
            }
        }

        return createOrUpdateService(existing);
    }

    /**
     *
     */
    @Transactional
    public String deleteService(Long serviceId, UserDetails loggedInUser)
    {
       Service service = serviceRepository.findOne(serviceId);

       if (service == null) {
           throw new ResourceNotFoundException("No such service");
       }

       serviceRepository.delete(service);

       return "ok";
    }
}
