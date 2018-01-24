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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
import com.freightcom.api.model.OrderStatus;
import com.freightcom.api.model.views.OrderStatusView;
import com.freightcom.api.services.OrderStatusService;
import com.freightcom.api.services.UserDetails;
import com.freightcom.api.repositories.custom.OrderStatusRepository;
import com.freightcom.api.services.UserDetailsImpl;

import com.fasterxml.jackson.core.type.TypeReference;


@RestController
public class OrderStatusController extends BaseController
{
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final OrderStatusService orderStatusService;
    private final OrderStatusRepository orderStatusRepository;
    private final ApiSession apiSession;
    @Autowired
    @Qualifier("mappingJackson2HttpMessageConverter")
    private MappingJackson2HttpMessageConverter messageConverter;

    @Autowired
    public OrderStatusController(final OrderStatusService orderStatusService,
                         final ApiSession apiSession,
                         final OrderStatusRepository orderStatusRepository,
                         final PagedResourcesAssembler<OrderStatus> pagedAssembler
                         ) {
        this.orderStatusService = orderStatusService;
        this.apiSession = apiSession;
        this.orderStatusRepository = orderStatusRepository;
    }

    /**
     *
     */
    @RequestMapping(value = { "/order_status" , "/orderStatuses" }, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Object getOrderStatus(@RequestParam Map<String, String> criteria, Principal principal, Pageable pageable) throws Exception
    {
        log.debug("FETCHING orderStatus");
        UserDetails loggedInUser = getLoggedInUser(principal);

        if (loggedInUser.isCustomer()) {
            criteria.put("shipping", "true");
            
            if (pageable.getSort() != null) {
            pageable = new PageRequest(0, pageable.getPageSize(), 
                    pageable.getSort().and(new Sort(Sort.Direction.ASC, "sequence")));
            } else {
                pageable = new PageRequest(0, pageable.getPageSize(), Sort.Direction.ASC, "sequence");
            }
        }


        return orderStatusService.getOrderStatuses(criteria, pageable);
    }

    /**
     *
     */
    @RequestMapping(value = "/order_status/{id:\\d+}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<OrderStatusView> getById(@PathVariable("id") Long orderStatusId, Principal principal) throws Exception
    {
        getLoggedInUser(principal);

        OrderStatus orderStatus = orderStatusRepository.findOne(orderStatusId);

        if (orderStatus == null) {
            throw new ResourceNotFoundException("Not authorized");
        }

        return new ResponseEntity<OrderStatusView>(new OrderStatusView(orderStatus), HttpStatus.OK);
    }

    @RequestMapping(value = "/order_status/{id:\\d+}", method = RequestMethod.PUT)
    @ResponseBody
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<OrderStatusView> updateOrderStatus(@PathVariable(value = "id") Long id,
                                             @RequestBody String json, Principal principal) throws Exception {
        UserDetailsImpl userDetails = getLoggedInUser(principal);

        OrderStatus orderStatusData = messageConverter.getObjectMapper()
                .readValue(json, OrderStatus.class);
        Map<String, String> attributes = messageConverter.getObjectMapper()
                .readValue(json, new TypeReference<HashMap<String, Object>>() {
                });

        OrderStatus orderStatus = orderStatusService.updateOrderStatus(id, orderStatusData, attributes, userDetails, apiSession.getRole());

        return new ResponseEntity<OrderStatusView>(new OrderStatusView(orderStatus), HttpStatus.OK);
    }

    @RequestMapping(value = "/order_status", method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<OrderStatusView> create_orderStatus(@RequestBody OrderStatus orderStatusData) throws Exception {
        OrderStatus orderStatus = orderStatusService.createOrderStatus(orderStatusData);
        log.debug("create orderStatus " + orderStatusData);

        return new ResponseEntity<OrderStatusView>(new OrderStatusView(orderStatus), HttpStatus.OK);
    }

    /**
     *
     */
    public Object getOrderStatusesImpl(Principal principal, Pageable pageable) throws Exception {
        Map<String,String> criteria = new HashMap<String,String>();
        return orderStatusService.getOrderStatuses(criteria, pageable);
    }

    /**
     *
     */
    @RequestMapping(value = "/order_status/{id:\\d+}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<String> deleteOrderStatus(@PathVariable("id") Long orderStatusId, Principal principal) throws Exception
    {
        List<String> result = new ArrayList<String>(1);

        result.add(orderStatusService.deleteOrderStatus(orderStatusId, getLoggedInUser(principal)));

        return result;
    }

}
