package com.freightcom.api.services;

import java.io.File;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.freightcom.api.ApiSession;
import com.freightcom.api.model.CarrierInvoice;
import com.freightcom.api.model.Service;
import com.freightcom.api.model.UserRole;
import com.freightcom.api.model.views.View;
import com.freightcom.api.repositories.CarrierInvoiceRepository;
import com.freightcom.api.repositories.ObjectBase;
import com.freightcom.api.repositories.custom.ServiceRepository;
import com.freightcom.api.repositories.custom.ServiceSpecification;
import com.freightcom.api.services.converters.ServiceConverter;
import com.freightcom.api.util.MapBuilder;
import com.freightcom.api.util.YesValue;

/**
 * @author bryan
 *
 */
@Component
public class CarrierServiceImpl implements CarrierService
{
    @SuppressWarnings("unused")
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final ServiceRepository serviceRepository;
    private final CarrierInvoiceRepository carrierInvoiceRepository;

    private final ApiSession apiSession;
    private final DocumentManager documentManager;
    private final ObjectBase objectBase;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public CarrierServiceImpl(final ServiceRepository serviceRepository, final ApiSession apiSession,
            final CarrierInvoiceRepository carrierInvoiceRepository, final DocumentManager documentManager,
            final ObjectBase objectBase)
    {
        this.serviceRepository = serviceRepository;
        this.carrierInvoiceRepository = carrierInvoiceRepository;
        this.apiSession = apiSession;
        this.documentManager = documentManager;
        this.objectBase = objectBase;
    }

    @Override
    public Page<Service> getServices(Map<String, String> criteria, Pageable pageable)
    {
        return serviceRepository.findAll(new ServiceSpecification(criteria), pageable);
    }

    @Override
    public Page<View> getServicesConverted(Map<String, String> criteria, Pageable pageable)
    {
        return serviceRepository.findAll(new ServiceSpecification(criteria), pageable)
                .map(new ServiceConverter());
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
    @Override
    public Service createService(final Service service) throws Exception
    {
        Service newService;

        try {
            service.getTerm();
        } catch (Exception e) {
            ValidationException.get()
                    .add("term", "Invalid term")
                    .doThrow();
        }

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
    @Override
    public Service updateService(Long id, Service service, Map<String, String> attributes, UserDetails loggedInUser,
            UserRole userRole) throws Exception
    {
        Service existing = serviceRepository.findOne(id);

        if (existing == null) {
            throw new ResourceNotFoundException("No such service");
        }

        ConfigurablePropertyAccessor source = PropertyAccessorFactory.forDirectFieldAccess(service);
        ConfigurablePropertyAccessor dest = PropertyAccessorFactory.forDirectFieldAccess(existing);

        // Only copy attributes supplied in the JSON input, leave others alone
        for (String key : attributes.keySet()) {
            if ("term".equals(key)) {
                try {
                    Service.Term term = Service.Term.valueOf(attributes.get(key));

                    existing.setTerm(term);
                } catch (Exception e) {
                    ValidationException.get()
                            .add(key, "Invalid term")
                            .doThrow();
                }
            } else if (dest.isWritableProperty(key)) {
                dest.setPropertyValue(key, source.getPropertyValue(key));
            }
        }

        return createOrUpdateService(existing);
    }

    /**
     *
     */
    @Transactional
    @Override
    public String deleteService(Long serviceId, UserDetails loggedInUser)
    {
        Service service = serviceRepository.findOne(serviceId);

        if (service == null) {
            throw new ResourceNotFoundException("No such service");
        }

        serviceRepository.delete(service);

        return "ok";
    }

    @Transactional
    @Override
    public Object uploadInvoice(Long serviceId, MultipartFile file) throws Exception
    {
        UserRole role = apiSession.getRole();

        if (!role.isAdmin() && !role.isFreightcomStaff()) {
            throw new AccessDeniedException("Not authorized");
        }

        Map<String, Object> result = new HashMap<String, Object>();

        Service service = serviceRepository.findOne(serviceId);

        if (service == null) {
            throw new ResourceNotFoundException("No such service");
        }

        Instant timeStamp = Instant.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd-HH-mm-ss")
                .withZone(ZoneId.of("UTC"));

        result = documentManager.upload("/public", serviceId + "/" + formatter.format(timeStamp) + ".csv", file);

        if (result.get("status") != null && result.get("status")
                .equals("ok") && result.get("path") != null) {
            CarrierInvoice carrierInvoice = new CarrierInvoice(service, result.get("path")
                    .toString(), (new File(file.getOriginalFilename())).getName());
            carrierInvoiceRepository.save(carrierInvoice);

            result.put("invoiceId", carrierInvoice.getId());

        } else {
            throw new Error("Error uploading POD");
        }

        return result;
    }

    @Override
    public Page<CarrierInvoice> listAllInvoices(Map<String, String> criteria, Pageable pageable)
    {
        UserRole role = apiSession.getRole();

        if (!role.isAdmin() && !role.isFreightcomStaff()) {
            throw new AccessDeniedException("Not authorized");
        }

        if (criteria.get("processed") != null) {
            return carrierInvoiceRepository.findByProcessed(YesValue.parse(criteria.get("processed")), pageable);
        } else {
            return carrierInvoiceRepository.findAll(pageable);
        }
    }

    @Override
    public Page<CarrierInvoice> listInvoices(Long serviceId, Map<String, String> criteria, Pageable pageable)
    {
        UserRole role = apiSession.getRole();

        if (!role.isAdmin() && !role.isFreightcomStaff()) {
            throw new AccessDeniedException("Not authorized");
        }

        Service service = serviceRepository.findOne(serviceId);

        if (service == null) {
            throw new ResourceNotFoundException("No such service");
        }

        if (criteria.get("processed") != null) {
            return carrierInvoiceRepository.findByServiceIdAndProcessed(serviceId,
                    YesValue.parse(criteria.get("processed")), pageable);
        } else {
            return carrierInvoiceRepository.findByServiceId(serviceId, pageable);
        }
    }

    @Override
    public Object downloadInvoice(Long serviceId, Long carrierInvoiceId) throws Exception
    {
        UserRole role = apiSession.getRole();

        if (!role.isAdmin() && !role.isFreightcomStaff()) {
            throw new AccessDeniedException("Not authorized");
        }

        Service service = serviceRepository.findOne(serviceId);

        if (service == null) {
            throw new ResourceNotFoundException("No such service");
        }

        CarrierInvoice carrierInvoice = carrierInvoiceRepository.findOne(carrierInvoiceId);

        if (carrierInvoice == null || carrierInvoice.getService() != service) {
            throw new ResourceNotFoundException("invoice not found");
        }

        return documentManager.stream(carrierInvoice.getDocumentId());
    }

    @Override
    public Service getService(Long serviceId) throws Exception
    {
        Service service = serviceRepository.findOne(serviceId);

        if (service == null) {
            throw new ResourceNotFoundException("No such service: " + serviceId);
        }

        UserRole role = apiSession.getRole();

        if (!role.isAdmin() && !role.isFreightcomStaff()) {
            throw new AccessDeniedException("Not authorized");
        }

        return service;
    } 

    @Override
    public Object getDropdownService()
    {
        return objectBase.getOrderedServices()
                .stream()
                .map(service -> MapBuilder.getNew()
                        .set("id", service.getId()
                                .toString(), "name", service.getName())
                        .toMap())
                .collect(Collectors.toList());
    }
}
