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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.freightcom.api.ApiSession;
import com.freightcom.api.model.Alert;
import com.freightcom.api.model.views.AlertView;
import com.freightcom.api.services.AlertService;
import com.freightcom.api.repositories.custom.AlertRepository;
import com.freightcom.api.services.UserDetailsImpl;

import com.fasterxml.jackson.core.type.TypeReference;


@RestController
public class AlertController extends BaseController
{
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final AlertService alertService;
    private final AlertRepository alertRepository;
    private final ApiSession apiSession;
    @Autowired
    @Qualifier("mappingJackson2HttpMessageConverter")
    private MappingJackson2HttpMessageConverter messageConverter;

    @Autowired
    public AlertController(final AlertService alertService,
                         final ApiSession apiSession,
                         final AlertRepository alertRepository,
                         final PagedResourcesAssembler<Alert> pagedAssembler
                         ) {
        this.alertService = alertService;
        this.apiSession = apiSession;
        this.alertRepository = alertRepository;
    }

    /**
     *
     */
    @RequestMapping(value = "/alert", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Object getAlert(@RequestParam Map<String, String> criteria, Principal principal, Pageable pageable) throws Exception
    {
        log.debug("FETCHING alert");
        getLoggedInUser(principal);

        return alertService.getAlerts(criteria, pageable);
    }

    /**
     *
     */
    @RequestMapping(value = "/alert/{id:\\d+}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<AlertView> getById(@PathVariable("id") Long alertId, Principal principal) throws Exception
    {
        getLoggedInUser(principal);

        Alert alert = alertRepository.findOne(alertId);

        if (alert == null) {
            throw new ResourceNotFoundException("Not authorized");
        }

        return new ResponseEntity<AlertView>(new AlertView(alert), HttpStatus.OK);
    }

    @RequestMapping(value = "/alert/{id:\\d+}", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<AlertView> updateAlert(@PathVariable(value = "id") Long id,
                                             @RequestBody String json, Principal principal) throws Exception {
        UserDetailsImpl userDetails = getLoggedInUser(principal);

        Alert alertData = messageConverter.getObjectMapper()
                .readValue(json, Alert.class);
        Map<String, String> attributes = messageConverter.getObjectMapper()
                .readValue(json, new TypeReference<HashMap<String, Object>>() {
                });

        Alert alert = alertService.updateAlert(id, alertData, attributes, userDetails, apiSession.getRole());

        return new ResponseEntity<AlertView>(new AlertView(alert), HttpStatus.OK);
    }

    @RequestMapping(value = "/alert", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<AlertView> create_alert(@RequestBody Alert alertData) throws Exception {
        Alert alert = alertService.createAlert(alertData);
        log.debug("create alert " + alertData);

        return new ResponseEntity<AlertView>(new AlertView(alert), HttpStatus.OK);
    }

    /**
     *
     */
    public Object getAlertsImpl(Principal principal, Pageable pageable) throws Exception {
        Map<String,String> criteria = new HashMap<String,String>();
        return alertService.getAlerts(criteria, pageable);
    }

    /**
     *
     */
    @RequestMapping(value = "/alert/{id:\\d+}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<String> deleteAlert(@PathVariable("id") Long alertId, Principal principal) throws Exception
    {
        List<String> result = new ArrayList<String>(1);

        result.add(alertService.deleteAlert(alertId, getLoggedInUser(principal)));

        return result;
    }

}
