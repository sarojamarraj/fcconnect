package com.freightcom.api.controllers;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.freightcom.api.ApiSession;
import com.freightcom.api.model.CarrierInvoice;
import com.freightcom.api.model.Service;
import com.freightcom.api.model.views.ServiceView;
import com.freightcom.api.model.views.View;
import com.freightcom.api.services.CarrierService;
import com.freightcom.api.services.UserDetailsImpl;

@RestController
public class CarrierController extends BaseController
{
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final CarrierService carrierService;
    private final ApiSession apiSession;

    private final PagedResourcesAssembler<View> pagedAssembler;
    private final PagedResourcesAssembler<CarrierInvoice> carrierInvoiceAssembler;

    @Autowired
    @Qualifier("mappingJackson2HttpMessageConverter")
    private MappingJackson2HttpMessageConverter messageConverter;

    @Autowired
    public CarrierController(final CarrierService carrierService, final ApiSession apiSession,
            final PagedResourcesAssembler<View> pagedAssembler,
            final PagedResourcesAssembler<CarrierInvoice> carrierInvoiceAssembler)
    {
        this.carrierService = carrierService;
        this.apiSession = apiSession;
        this.pagedAssembler = pagedAssembler;
        this.carrierInvoiceAssembler = carrierInvoiceAssembler;
    }

    /**
     *
     */
    @RequestMapping(value = "/carrier", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Object getCarrier(@RequestParam Map<String, String> criteria, Principal principal, Pageable pageable)
            throws Exception
    {
        long startTime = System.currentTimeMillis();
        log.debug("ENTER ROUTE /carrier");

        if (! apiSession.getRole().isFreightcom()) {
            throw new AccessDeniedException("Not authorized");
        }

        try {
            return pagedAssembler.toResource(carrierService.getServicesConverted(criteria, pageable));
        } finally {
            log.debug("LEAVE ROUTE /carrier " + (double) (System.currentTimeMillis() - startTime) / 1000.0);
        }
    }

    /**
     *
     */
    @RequestMapping(value = "/carrier-dropdown", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Object getCarrierDropdown(@RequestParam Map<String, String> criteria, Principal principal, Pageable pageable)
            throws Exception
    {
        return carrierService.getDropdownService();
    }

    /**
     *
     */
    @RequestMapping(value = "/carrier/{id:\\d+}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<View> getById(@PathVariable("id") Long serviceId, Principal principal) throws Exception
    {
        return new ResponseEntity<View>(new ServiceView(carrierService.getService(serviceId)), HttpStatus.OK);
    }

    @RequestMapping(value = "/carrier/{id:\\d+}", method = RequestMethod.PUT)
    @ResponseBody
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<View> updateService(@PathVariable(value = "id") Long id, @RequestBody String json,
            Principal principal) throws Exception
    {
        UserDetailsImpl userDetails = getLoggedInUser(principal);

        Service serviceData = messageConverter.getObjectMapper()
                .readValue(json, Service.class);
        Map<String, String> attributes = messageConverter.getObjectMapper()
                .readValue(json, new TypeReference<HashMap<String, Object>>() {
                });

        Service service = carrierService.updateService(id, serviceData, attributes, userDetails, apiSession.getRole());

        return new ResponseEntity<View>(new ServiceView(service), HttpStatus.OK);
    }

    @RequestMapping(value = "/carrier", method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ServiceView> create_service(@RequestBody Service serviceData) throws Exception
    {
        Service service = carrierService.createService(serviceData);
        log.debug("create service " + serviceData);

        return new ResponseEntity<ServiceView>(new ServiceView(service), HttpStatus.OK);
    }

    /**
     *
     */
    public Object getServicesImpl(Principal principal, Pageable pageable) throws Exception
    {
        Map<String, String> criteria = new HashMap<String, String>();

        return carrierService.getServices(criteria, pageable);
    }

    /**
     *
     */
    @RequestMapping(value = "/carrier/{id:\\d+}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<String> deleteService(@PathVariable("id") Long serviceId, Principal principal) throws Exception
    {
        List<String> result = new ArrayList<String>(1);

        result.add(carrierService.deleteService(serviceId, getLoggedInUser(principal)));

        return result;
    }

    @RequestMapping(value = "/carrier/{id:\\d+}/upload-invoice", method = RequestMethod.POST)
    @ResponseBody
    public Object uploadINVOICE(@PathVariable("id") Long serviceid, @RequestParam("file") MultipartFile file)
            throws Exception
    {
        return carrierService.uploadInvoice(serviceid, file);
    }

    @RequestMapping(value = "/carrier/invoice", method = RequestMethod.GET)
    @ResponseBody
    public Object listAllInvoices(@RequestParam Map<String, String> criteria, Pageable pageable) throws Exception
    {
        return carrierInvoiceAssembler.toResource(carrierService.listAllInvoices(criteria, pageable));
    }

    @RequestMapping(value = "/carrier/invoice/{id:\\d+}", method = RequestMethod.GET)
    @ResponseBody
    public Object listInvoices(@PathVariable("id") Long serviceId, @RequestParam Map<String, String> criteria,
            Pageable pageable) throws Exception
    {
        return carrierInvoiceAssembler.toResource(carrierService.listInvoices(serviceId, criteria, pageable));
    }

    @RequestMapping(value = "/carrier/invoice/{serviceId:\\d+}/{id:\\d+}", method = RequestMethod.GET)
    @ResponseBody
    public Object downloadInvoice(@PathVariable("serviceId") Long serviceId, @PathVariable("id") Long invoiceId)
            throws Exception
    {
        return carrierService.downloadInvoice(serviceId, invoiceId);
    }

}
