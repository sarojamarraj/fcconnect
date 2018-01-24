package com.freightcom.api.controllers;

import java.security.Principal;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.freightcom.api.ApiSession;
import com.freightcom.api.model.Service;
import com.freightcom.api.model.views.ServiceView;
import com.freightcom.api.services.ServiceService;
import com.freightcom.api.repositories.custom.ServiceRepository;
import com.freightcom.api.services.UserDetailsImpl;

import com.fasterxml.jackson.core.type.TypeReference;


@RestController
public class ServiceController extends BaseController
{
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final ServiceService serviceService;
    private final ServiceRepository serviceRepository;
    private final ApiSession apiSession;
    @Autowired
    @Qualifier("mappingJackson2HttpMessageConverter")
    private MappingJackson2HttpMessageConverter messageConverter;

    @Autowired
    public ServiceController(final ServiceService serviceService,
                         final ApiSession apiSession,
                         final ServiceRepository serviceRepository,
                         final PagedResourcesAssembler<Service> pagedAssembler
                         ) {
        this.serviceService = serviceService;
        this.apiSession = apiSession;
        this.serviceRepository = serviceRepository;
    }

    /**
     *
     */
    @RequestMapping(value = "/service", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Object getService(@RequestParam Map<String, Object> criteria, Principal principal, Pageable pageable) throws Exception
    {
        log.debug("FETCHING service");
        getLoggedInUser(principal);

        return serviceService.getServices(criteria, pageable);
    }

    /**
     *
     */
    @RequestMapping(value = "/service/{id:\\d+}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<ServiceView> getById(@PathVariable("id") Long serviceId, Principal principal) throws Exception
    {
        getLoggedInUser(principal);

        Service service = serviceRepository.findOne(serviceId);

        if (service == null) {
            throw new ResourceNotFoundException("Not authorized");
        }

        return new ResponseEntity<ServiceView>(new ServiceView(service), HttpStatus.OK);
    }

    @RequestMapping(value = "/service/{id:\\d+}", method = RequestMethod.PUT)
    @ResponseBody
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ServiceView> updateService(@PathVariable(value = "id") Long id,
                                             @RequestBody String json, Principal principal) throws Exception {
        UserDetailsImpl userDetails = getLoggedInUser(principal);

        Service serviceData = messageConverter.getObjectMapper()
                .readValue(json, Service.class);
        Map<String, String> attributes = messageConverter.getObjectMapper()
                .readValue(json, new TypeReference<HashMap<String, Object>>() {
                });

        Service service = serviceService.updateService(id, serviceData, attributes, userDetails, apiSession.getRole());

        return new ResponseEntity<ServiceView>(new ServiceView(service), HttpStatus.OK);
    }

    @RequestMapping(value = "/service", method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ServiceView> create_service(@RequestBody Service serviceData) throws Exception {
        Service service = serviceService.createService(serviceData);
        log.debug("create service " + serviceData);

        return new ResponseEntity<ServiceView>(new ServiceView(service), HttpStatus.OK);
    }

    /**
     *
     */
    public Object getServicesImpl(Principal principal, Pageable pageable) throws Exception {
        Map<String,Object> criteria = new HashMap<String,Object>();
        getLoggedInUser(principal);


        return serviceService.getServices(criteria, pageable);
    }

    /**
     *
     */
    @RequestMapping(value = "/service/{id:\\d+}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<String> deleteService(@PathVariable("id") Long serviceId, Principal principal) throws Exception
    {
        List<String> result = new ArrayList<String>(1);

        result.add(serviceService.deleteService(serviceId, getLoggedInUser(principal)));

        return result;
    }

}
