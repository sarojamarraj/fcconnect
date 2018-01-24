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

import com.fasterxml.jackson.core.type.TypeReference;
import com.freightcom.api.ApiSession;
import com.freightcom.api.model.Customer;
import com.freightcom.api.model.views.CustomerFreightcomView;
import com.freightcom.api.model.views.CustomerView;
import com.freightcom.api.repositories.custom.CustomerRepository;
import com.freightcom.api.services.CustomerService;
import com.freightcom.api.services.UserDetailsImpl;
import com.freightcom.api.util.MapBuilder;

@RestController
public class CustomerController extends BaseController
{
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final CustomerService customerService;
    private final CustomerRepository customerRepository;
    private final ApiSession apiSession;
    @Autowired
    @Qualifier("mappingJackson2HttpMessageConverter")
    private MappingJackson2HttpMessageConverter messageConverter;

    @Autowired
    public CustomerController(final CustomerService customerService, final ApiSession apiSession,
            final CustomerRepository customerRepository, final PagedResourcesAssembler<Customer> pagedAssembler)
    {
        this.customerService = customerService;
        this.apiSession = apiSession;
        this.customerRepository = customerRepository;
    }

    /**
     *
     */
    @RequestMapping(value = "/customer", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Object getCustomer(@RequestParam Map<String, Object> criteria, Principal principal, Pageable pageable)
            throws Exception
    {
        log.debug("FETCHING CUSTOMERS");
        getLoggedInUser(principal);

        return customerService.getCustomersConverted(criteria, pageable);
    }

    /**
     *
     */
    @RequestMapping(value = "/customer/search/findByNameContainingAllIgnoringCase", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('FREIGHTCOM_STAFF')")
    public Object getCustomerz(@RequestParam Map<String, Object> criteria, Principal principal, Pageable pageable)
            throws Exception
    {
        log.debug("FETCHING CUSTOMERS");
        getLoggedInUser(principal);

        return customerService.getCustomersConverted(criteria, pageable);
    }

    /**
     *
     */
    @RequestMapping(value = "/customer/search/byName", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('FREIGHTCOM_STAFF')")
    public Object getCustomersByName(@RequestParam Map<String, Object> criteria, Pageable pageable)
            throws Exception
    {
        return customerService.getByName(criteria, pageable);
    }

    /**
     *
     */
    @RequestMapping(value = "/customer/{id:\\d+}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<CustomerView> getById(@PathVariable("id") Long customerId)
            throws Exception
    {
        Customer customer = customerRepository.findOne(customerId);

        if (customer == null) {
            throw new ResourceNotFoundException("Not authorized");
        }

        if (apiSession.isFreightcom()) {
            return new ResponseEntity<CustomerView>(new CustomerFreightcomView(customer), HttpStatus.OK);
        } else {
            return new ResponseEntity<CustomerView>(new CustomerView(customer), HttpStatus.OK);
        }
    }

    /**
     *
     */
    @RequestMapping(value = "/customers", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Object getCustomers(@RequestParam Map<String, Object> criteria, Principal principal, Pageable pageable)
            throws Exception
    {
        log.debug("FETCHING CUSTOMERS");
        getLoggedInUser(principal);

        return customerService.getCustomersConverted(criteria, pageable);
    }

    @RequestMapping(value = "/customer/{id:\\d+}", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<CustomerView> updateCustomer(@PathVariable(value = "id") Long id, @RequestBody String json,
            Principal principal) throws Exception
    {
        UserDetailsImpl userDetails = getLoggedInUser(principal);

        Customer customerData = messageConverter.getObjectMapper()
                .readValue(json, Customer.class);
        Map<String, String> attributes = messageConverter.getObjectMapper()
                .readValue(json, new TypeReference<HashMap<String, Object>>() {
                });

        Customer customer = customerService.updateCustomer(id, customerData, attributes, userDetails,
                apiSession.getRole());

        return new ResponseEntity<CustomerView>(new CustomerView(customer), HttpStatus.OK);
    }

    @RequestMapping(value = "/customer-and-staff", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<CustomerView> create_customer(@RequestBody String json) throws Exception
    {
        Customer customerData = messageConverter.getObjectMapper()
                .readValue(json, Customer.class);
        Map<String, Object> attributes = messageConverter.getObjectMapper()
                .readValue(json, new TypeReference<HashMap<String, Object>>() {
                });

        log.debug("create customer x " + customerData.getShipTo());

        Customer customer = customerService.createCustomer(customerData, attributes);

        log.debug("create customer " + customerData);

        return new ResponseEntity<CustomerView>(new CustomerView(customer), HttpStatus.OK);
    }

    @RequestMapping(value = "/customer", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<CustomerView> create_customer_only(@RequestBody String json) throws Exception
    {
        Customer customerData = messageConverter.getObjectMapper()
                .readValue(json, Customer.class);
        Map<String, Object> attributes = messageConverter.getObjectMapper()
                .readValue(json, new TypeReference<HashMap<String, Object>>() {
                });

        Customer customer = customerService.createCustomerOnly(customerData, attributes);
        log.debug("create customer " + customerData);

        return new ResponseEntity<CustomerView>(new CustomerView(customer), HttpStatus.OK);
    }

    /**
     *
     */
    @RequestMapping(value = "/customer/{id:\\d+}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<String> deleteCustomer(@PathVariable("id") Long customerId, Principal principal) throws Exception
    {
        List<String> result = new ArrayList<String>(1);

        result.add(customerService.deleteCustomer(customerId, getLoggedInUser(principal)));

        return result;
    }

    /**
     *
     */
    @RequestMapping(value = "/customer/{id:\\d+}/invite", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Object invite(@PathVariable("id") Long customerId, @RequestParam("email") String emailAddress)
            throws Exception
    {
        customerService.inviteCustomer(customerId, emailAddress);

        return MapBuilder.getNew()
                .put("status", "SUCCESS")
                .getMap();
    }

    /**
     *
     */
    public Object getCustomersImpl(Principal principal, Pageable pageable) throws Exception
    {
        Map<String, Object> criteria = new HashMap<String, Object>();
        getLoggedInUser(principal);

        return customerService.getCustomersConverted(criteria, pageable);
    }

}
