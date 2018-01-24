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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.freightcom.api.ApiSession;
import com.freightcom.api.model.SystemProperty;
import com.freightcom.api.model.views.SystemPropertyView;
import com.freightcom.api.services.SystemPropertyService;
import com.freightcom.api.repositories.custom.SystemPropertyRepository;
import com.freightcom.api.services.UserDetailsImpl;

import com.fasterxml.jackson.core.type.TypeReference;


@RestController
public class SystemPropertyController extends BaseController
{
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final SystemPropertyService systemPropertyService;
    private final SystemPropertyRepository systemPropertyRepository;
    private final ApiSession apiSession;
    @Autowired
    @Qualifier("mappingJackson2HttpMessageConverter")
    private MappingJackson2HttpMessageConverter messageConverter;

    @Autowired
    public SystemPropertyController(final SystemPropertyService systemPropertyService,
                         final ApiSession apiSession,
                         final SystemPropertyRepository systemPropertyRepository,
                         final PagedResourcesAssembler<SystemProperty> pagedAssembler
                         ) {
        this.systemPropertyService = systemPropertyService;
        this.apiSession = apiSession;
        this.systemPropertyRepository = systemPropertyRepository;
    }

    /**
     *
     */
    @RequestMapping(value = "/system_property", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Object getSystemProperty(@RequestParam Map<String, String> criteria, Principal principal, Pageable pageable) throws Exception
    {
        log.debug("FETCHING systemProperty");
        getLoggedInUser(principal);

        return systemPropertyService.getSystemPropertys(criteria, pageable);
    }

    /**
     *
     */
    @RequestMapping(value = "/system_property/{id:\\d+}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<SystemPropertyView> getById(@PathVariable("id") Long systemPropertyId, Principal principal) throws Exception
    {
        getLoggedInUser(principal);

        SystemProperty systemProperty = systemPropertyRepository.findOne(systemPropertyId);

        if (systemProperty == null) {
            throw new ResourceNotFoundException("Not authorized");
        }

        return new ResponseEntity<SystemPropertyView>(new SystemPropertyView(systemProperty), HttpStatus.OK);
    }

    /**
     * Find the named system property, return just the value
     */
    @RequestMapping(value = "/system_property/{name:[a-zA-Z].*}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Map<String,Object>> getByName(@PathVariable("name") String name, Principal principal) throws Exception
    {
        Map<String,Object> result = new HashMap<String,Object>();
        getLoggedInUser(principal);

        SystemProperty systemProperty = systemPropertyRepository.findByName(name);

        if (systemProperty == null) {
            throw new ResourceNotFoundException("Not authorized");
        }

        result.put("data", systemProperty.getData());
        result.put("id", systemProperty.getId());

        return new ResponseEntity<Map<String,Object>>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/system_property/{id:\\d+}", method = RequestMethod.PUT)
    @ResponseBody
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<SystemPropertyView> updateSystemProperty(@PathVariable(value = "id") Long id,
                                             @RequestBody String json, Principal principal) throws Exception {
        UserDetailsImpl userDetails = getLoggedInUser(principal);

        SystemProperty systemPropertyData = messageConverter.getObjectMapper()
                .readValue(json, SystemProperty.class);
        Map<String, String> attributes = messageConverter.getObjectMapper()
                .readValue(json, new TypeReference<HashMap<String, Object>>() {
                });

        SystemProperty systemProperty = systemPropertyService.updateSystemProperty(id, systemPropertyData, attributes, userDetails, apiSession.getRole());

        return new ResponseEntity<SystemPropertyView>(new SystemPropertyView(systemProperty), HttpStatus.OK);
    }

    @RequestMapping(value = "/system_property/{name:[a-zA-Z].*}", method = RequestMethod.PUT)
    @ResponseBody
    @Transactional
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<SystemPropertyView> updateSystemProperty(@PathVariable(value = "name") String name,
                                             @RequestBody SystemProperty values, Principal principal) throws Exception {
        getLoggedInUser(principal);

        SystemProperty systemProperty = systemPropertyRepository.findByName(name);

        if (systemProperty == null) {
            throw new ResourceNotFoundException("Not authorized");
        }

        systemProperty.setData(values.getData());
        systemPropertyRepository.save(systemProperty);

        return new ResponseEntity<SystemPropertyView>(new SystemPropertyView(systemProperty), HttpStatus.OK);
    }

    @RequestMapping(value = "/system_property", method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<SystemPropertyView> create_systemProperty(@RequestBody SystemProperty systemPropertyData) throws Exception {
        SystemProperty systemProperty = systemPropertyService.createSystemProperty(systemPropertyData);
        log.debug("create systemProperty " + systemPropertyData);

        return new ResponseEntity<SystemPropertyView>(new SystemPropertyView(systemProperty), HttpStatus.OK);
    }

    /**
     *
     */
    public Object getSystemPropertysImpl(Principal principal, Pageable pageable) throws Exception {
        Map<String,String> criteria = new HashMap<String,String>();
        getLoggedInUser(principal);


        return systemPropertyService.getSystemPropertys(criteria, pageable);
    }

    /**
     *
     */
    @RequestMapping(value = "/system_property/{id:\\d+}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<String> deleteSystemProperty(@PathVariable("id") Long systemPropertyId, Principal principal) throws Exception
    {
        List<String> result = new ArrayList<String>(1);

        result.add(systemPropertyService.deleteSystemProperty(systemPropertyId, getLoggedInUser(principal)));

        return result;
    }

}
