package com.freightcom.api.controllers;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.format.annotation.DateTimeFormat;
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
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.freightcom.api.ApiSession;
import com.freightcom.api.model.CustomerOrder;
import com.freightcom.api.model.CustomsInvoice;
import com.freightcom.api.model.views.ChargeView;
import com.freightcom.api.model.views.OrderView;
import com.freightcom.api.model.views.UpdateForBookView;
import com.freightcom.api.model.views.View;
import com.freightcom.api.repositories.custom.OrderSpecification;
import com.freightcom.api.services.OrderLock;
import com.freightcom.api.services.OrderService;
import com.freightcom.api.services.UserDetailsImpl;
import com.freightcom.api.services.dataobjects.PickupData;
import com.freightcom.api.util.MapBuilder;

@RestController
public class OrderController extends BaseController
{
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final OrderService orderService;
    private final ApiSession apiSession;
    private final PagedResourcesAssembler<View> assembler;

    @Autowired
    @Qualifier("mappingJackson2HttpMessageConverter")
    private MappingJackson2HttpMessageConverter messageConverter;

    public OrderController(OrderService orderService, final ApiSession apiSession,
            final PagedResourcesAssembler<View> assembler)
    {
        this.orderService = orderService;
        this.apiSession = apiSession;
        this.assembler = assembler;
    }

    @RequestMapping(value = "/order/upload-distribution-list", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> handleFileUpload(Principal principal,
            @RequestParam("file") MultipartFile file) throws Exception
    {

        return new ResponseEntity<Map<String, Object>>(
                orderService.storeNoOrder(file, getLoggedInUser(principal), apiSession.getRole()), HttpStatus.OK);
    }

    @RequestMapping(value = "/order/update/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<OrderView> updateOrder(@PathVariable(value = "id") Long id, @RequestBody String json,
            Principal principal) throws Exception
    {
        log.debug("UPDATE ORDER " + json);

        UserDetailsImpl userDetails = getLoggedInUser(principal);

        CustomerOrder orderData = messageConverter.getObjectMapper()
                .readValue(json, CustomerOrder.class);
        Map<String, Object> attributes = messageConverter.getObjectMapper()
                .readValue(json, new TypeReference<HashMap<String, Object>>() {
                });

        CustomerOrder order = orderService.updateOrder(id, orderData, attributes, userDetails, apiSession.getRole());

        return new ResponseEntity<OrderView>(OrderView.get(orderService, order), HttpStatus.OK);
    }

    @RequestMapping(value = "/order/{id:\\d+}", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<OrderView> updateOrder_b(@PathVariable(value = "id") Long id, @RequestBody String json,
            Principal principal) throws Exception
    {
        return updateOrder(id, json, principal);
    }

    @RequestMapping(value = "/order/create", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<OrderView> create_order(@RequestBody CustomerOrder orderData) throws Exception
    {
        log.debug("ENTER CREATE ORDER");

        CustomerOrder order = orderService.createOrder(orderData);

        log.debug("create order " + order);

        return new ResponseEntity<OrderView>(OrderView.get(orderService, order), HttpStatus.OK);
    }

    @RequestMapping(value = "/order", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<OrderView> create_order_b(@RequestBody CustomerOrder orderData) throws Exception
    {
        CustomerOrder order = orderService.createOrder(orderData);
        log.debug("create order " + orderData);

        return new ResponseEntity<OrderView>(OrderView.get(orderService, order), HttpStatus.OK);
    }

    @RequestMapping(value = "/order/save-pickup-and-customs/{id:\\d+}", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<UpdateForBookView> savePickupAndCustoms(@PathVariable(value = "id") Long id,
            @RequestBody CustomerOrder data, Principal principal) throws Exception
    {
        try {
        return new ResponseEntity<UpdateForBookView>(UpdateForBookView.get(orderService.savePickupAndCustoms(id, data), orderService),
                HttpStatus.OK);
        } finally {
            log.debug("\n\nDONE SAVE PICKUP AND CUSTOMS\n\n");
        }
    }

    /**
     *
     */
    @RequestMapping(value = "/order/schedule-pickup/{id:\\d+}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Object schedulePickup(@PathVariable("id") Long orderId, @RequestBody PickupData pickupData,
            Principal principal) throws Exception
    {
        return orderService.schedulePickup(orderId, pickupData);
    }

    /**
     * Create a known selected quote for testing
     */
    @RequestMapping(value = "/order/test-rate/{serviceId}/{id:\\d+}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public OrderView testRate(@PathVariable("serviceId") Long serviceId, @PathVariable("id") Long orderId,
            @RequestBody Map<String, BigDecimal> data) throws Exception
    {
        return OrderView.get(orderService, orderService.testRate(orderId, serviceId, data));
    }

    /**
     *
     */
    @RequestMapping(value = "/order/delete/{id:\\d+}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<String> deleteOrder(@PathVariable("id") Long orderId, Principal principal) throws Exception
    {
        List<String> result = new ArrayList<String>(1);

        result.add(orderService.deleteOrder(orderId, getLoggedInUser(principal)));

        return result;
    }

    /**
     *
     */
    @RequestMapping(value = "/order/book/{id:\\d+}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<OrderView> bookOrder(@PathVariable("id") Long orderId, @RequestBody String json,
            Principal principal) throws Exception
    {
        CustomerOrder orderData = messageConverter.getObjectMapper()
                .readValue(json, CustomerOrder.class);
        Map<String, Object> attributes = messageConverter.getObjectMapper()
                .readValue(json, new TypeReference<HashMap<String, Object>>() {
                });

        return new ResponseEntity<OrderView>(
                OrderView.get(orderService,
                        orderService.bookOrder(orderId, orderData, attributes, getLoggedInUser(principal))),
                HttpStatus.OK);
    }

    /**
     *
     */
    @RequestMapping(value = "/order/{id:\\d+}/customs-invoice", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Object updateCustomsForm(@PathVariable("id") Long orderId, @RequestBody String json, Principal principal)
            throws Exception
    {
        CustomsInvoice invoiceData = messageConverter.getObjectMapper()
                .readValue(json, CustomsInvoice.class);
        Map<String, Object> attributes = messageConverter.getObjectMapper()
                .readValue(json, new TypeReference<HashMap<String, Object>>() {
                });

        return orderService.updateCustomsForm(orderId, invoiceData, attributes);
    }

    /**
     *
     */
    @RequestMapping(value = "/order/send-quote/{id:\\d+}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<OrderView> sendQuote(@PathVariable("id") Long orderId) throws Exception
    {
        return new ResponseEntity<OrderView>(OrderView.get(orderService, orderService.sendQuote(orderId)),
                HttpStatus.OK);
    }

    /**
     *
     */
    @RequestMapping(value = "/order/{id:\\d+}/mark-as-in-transit", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<OrderView> markInTransit(@PathVariable("id") Long orderId,
            @RequestBody Map<String, String> attributes) throws Exception
    {
        return new ResponseEntity<OrderView>(
                OrderView.get(orderService, orderService.markIntransit(orderId, attributes.get("comment"))),
                HttpStatus.OK);
    }

    /**
     *
     */
    @RequestMapping(value = "/order/{id:\\d+}/mark-as-delivered", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<OrderView> deliverOrder(@PathVariable("id") Long orderId,
            @RequestBody Map<String, String> attributes) throws Exception
    {
        return new ResponseEntity<OrderView>(
                OrderView.get(orderService, orderService.deliverOrder(orderId, attributes.get("comment"))),
                HttpStatus.OK);
    }

    /**
     *
     */
    @RequestMapping(value = "/order/select-rate/{orderId:\\d+}/{rateId:\\d+}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<OrderView> selectRate(@PathVariable("orderId") Long orderId,
            @PathVariable("rateId") Long rateId, @RequestBody String json, Principal principal) throws Exception
    {
        CustomerOrder orderData;
        Map<String, Object> attributes;

        if (json == null || json.equals("")) {
            orderData = new CustomerOrder();
            attributes = new HashMap<String, Object>();
        } else {
            orderData = messageConverter.getObjectMapper()
                    .readValue(json, CustomerOrder.class);
            attributes = messageConverter.getObjectMapper()
                    .readValue(json, new TypeReference<HashMap<String, Object>>() {
                    });
        }

        if (orderData == null || attributes == null) {
            orderData = new CustomerOrder();
            attributes = new HashMap<String, Object>();
        }

        return new ResponseEntity<OrderView>(
                OrderView.get(orderService,
                        orderService.selectRate(orderId, rateId, orderData, attributes, getLoggedInUser(principal))),
                HttpStatus.OK);
    }

    /**
     *
     */
    @RequestMapping(value = "/order/{id:\\d+}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<String> realDeleteOrder(@PathVariable("id") Long orderId, Principal principal) throws Exception
    {
        List<String> result = new ArrayList<String>(1);

        result.add(orderService.realDeleteOrder(orderId, getLoggedInUser(principal)));

        return result;
    }

    /**
     *
     */
    @RequestMapping(value = "/order/{id:\\d+}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<OrderView> getOneOrder(@PathVariable("id") Long orderId, Principal principal) throws Exception
    {
        try {
            synchronized (OrderLock.getLock(orderId)) {
                return new ResponseEntity<OrderView>(
                        OrderView.get(orderService, orderService.getOrder(orderId, getLoggedInUser(principal))),
                        HttpStatus.OK);
            }
        } finally {
            log.debug("DONE SYNC GET");
        }
    }

    @RequestMapping(value = "/order-events/{orderId}", method = RequestMethod.GET)
    @ResponseBody
    public Object order_events(@PathVariable(value = "orderId") Long orderId,
            @RequestParam Map<String, Object> criteria, @PageableDefault(size = 10, page = 0, sort = {
                    "previousClose" }, direction = Direction.DESC) Pageable pageable)
            throws Exception
    {
        return orderService.getStatusMessages(orderId, criteria, pageable);
    }

    /**
     *
     */
    @RequestMapping(value = "/order/count/{mode}", method = RequestMethod.GET)
    @ResponseBody
    public Object getOrderCounts(@RequestParam Map<String, Object> criteria, @PathVariable(value = "mode") String mode,
            HttpServletResponse response) throws IOException
    {
        if (apiSession.isCustomer()) {
            Object customerId = criteria.get("customerId");

            if (customerId == null) {
                criteria.put("customerId", apiSession.getCustomerId());
            } else if (!asLong(customerId, "Invalid customer id").equals(apiSession.getCustomerId())) {
                log.debug("CUSTOMER MISTMATCH " + customerId + " session customer " + apiSession.getCustomerId());
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            }
        } else if (apiSession.isAgent()) {
            criteria.put("agentId", apiSession.getRole()
                    .getId());
        }

        log.debug("ORDER COUNT MODE " + mode + " --> " + OrderSpecification.Mode.valueOf(mode));

        criteria.put("mode", OrderSpecification.Mode.valueOf(mode));

        return MapBuilder.ok("count", orderService.getOrderCounts(criteria).toString()).toMap();
    }

    /**
     *
     */
    public Object getOrdersImpl(Map<String, Object> criteria, Principal principal, HttpServletResponse response,
            Pageable pageable, OrderSpecification.Mode mode) throws IOException
    {

        UserDetailsImpl user = getLoggedInUser(principal);

        if (apiSession.isCustomer()) {
            Object customerId = criteria.get("customerId");

            if (customerId == null) {
                criteria.put("customerId", apiSession.getCustomerId());
            } else if (!asLong(customerId, "Invalid customer id").equals(apiSession.getCustomerId())) {
                log.debug("CUSTOMER MISTMATCH " + customerId + " session customer " + apiSession.getCustomerId());
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            }
        } else if (apiSession.isAgent()) {
            criteria.put("agentId", apiSession.getRole()
                    .getId());
        }

        criteria.put("mode", mode);

        boolean dateSortUp = false;
        boolean dateSortDown = false;

        if (criteria.get("scheduledShipDateFrom") != null) {
            dateSortUp = true;
        }

        if (criteria.get("scheduledShipDateTo") != null) {
            dateSortUp = true;
        }

        Object scheduledShipDate = criteria.get("scheduledShipDate");

        if (scheduledShipDate != null) {
            if (scheduledShipDate.equals("upcoming")) {
                dateSortUp = true;
            } else {
                dateSortDown = true;
            }

            log.debug("HAS SORT ");
        }

        if (pageable.getSort() != null) {
            for (Sort.Order sorder : pageable.getSort()) {
                log.debug("SORT REQUEST " + sorder);
            }
        }

        if (dateSortUp || dateSortDown) {

            boolean hasSort = false;

            if (pageable.getSort() != null) {
                for (@SuppressWarnings("unused")
                Sort.Order sorder : pageable.getSort()) {
                    hasSort = true;
                    break;
                }
            }

            if (!hasSort) {
                Sort.Direction direction;

                if (dateSortUp) {
                    direction = Sort.Direction.ASC;
                } else {
                    direction = Sort.Direction.DESC;
                }

                log.debug("NO SORT FOUND");

                if (pageable.getSort() != null) {
                    pageable.getSort()
                            .and(new Sort(Sort.Direction.DESC, "scheduledShipDate"));
                } else {
                    pageable = new PageRequest(0, pageable.getPageSize(), direction, "scheduledShipDate");
                }
            }
        }

        if (!user.isAdmin()) {
            return orderService.getCustomerOrders(criteria, pageable);
        } else {
            return orderService.getOrders(criteria, pageable, mode);
        }
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('FREIGHTCOM_STAFF')")
    @RequestMapping(value = "/order-quick", method = RequestMethod.GET)
    @ResponseBody
    public Object getOrders(@RequestParam Map<String, Object> criteria, Pageable pageable) throws IOException
    {
        return assembler.toResource(orderService.getOrdersQuick(criteria, pageable));
    }

    @RequestMapping(value = "/order", method = RequestMethod.GET)
    @ResponseBody
    public Object getOrders(@RequestParam Map<String, Object> criteria, Principal principal,
            HttpServletResponse response, Pageable pageable) throws IOException
    {
        return getOrdersImpl(criteria, principal, response, pageable, OrderSpecification.Mode.ALL);
    }

    @RequestMapping(value = "/customerorder", method = RequestMethod.GET)
    @ResponseBody
    public Object getCustomerOrders(@RequestParam Map<String, Object> criteria, Principal principal,
            HttpServletResponse response, Pageable pageable) throws IOException
    {
        return getOrdersImpl(criteria, principal, response, pageable, OrderSpecification.Mode.ALL);
    }

    @RequestMapping(value = "/order/search/findByCustomerId", method = RequestMethod.GET)
    @ResponseBody
    public Object getCustomerOrders2(@RequestParam Map<String, Object> criteria, Principal principal,
            HttpServletResponse response, Pageable pageable) throws IOException
    {
        return getOrdersImpl(criteria, principal, response, pageable, OrderSpecification.Mode.ALL);
    }

    @RequestMapping(value = "/submitted-orders", method = RequestMethod.GET)
    @ResponseBody
    public Object getSubmittedOrders(@RequestParam Map<String, Object> criteria, Principal principal,
            HttpServletResponse response, Pageable pageable) throws IOException
    {
        long startTime = System.currentTimeMillis();
        log.debug("ENTER ROUTE /submitted-orders");

        try {
            return getOrdersImpl(criteria, principal, response, pageable, OrderSpecification.Mode.SUBMITTED_ONLY);
        } finally {
            log.debug("LEAVE ROUTE /submitted-orders " + (double) (System.currentTimeMillis() - startTime) / 1000.0);
        }
    }

    @RequestMapping(value = "/draft-orders", method = RequestMethod.GET)
    @ResponseBody
    public Object getDraftOrders(@RequestParam Map<String, Object> criteria, Principal principal,
            HttpServletResponse response, Pageable pageable) throws IOException
    {
        return getOrdersImpl(criteria, principal, response, pageable, OrderSpecification.Mode.DRAFT_ONLY);
    }

    @RequestMapping(value = "/job-board", method = RequestMethod.GET)
    @ResponseBody
    public Object getJobBoard(@RequestParam Map<String, Object> criteria, Principal principal,
            HttpServletResponse response, Pageable pageable) throws IOException
    {
        return getOrdersImpl(criteria, principal, response, pageable, OrderSpecification.Mode.JOB_BOARD);
    }

    @RequestMapping(value = "/orders-by-month", method = RequestMethod.GET)
    @ResponseBody
    public Object getOrdersByMonth(
            @RequestParam(value = "from", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date from,
            @RequestParam(value = "to", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date to,
            @RequestParam(value = "customerId", required = false) Long customerId,
            @RequestParam(value = "agentId", required = false) Long agentId)
    {
        return orderService.getByMonth(from, to, customerId, agentId);
    }

    @RequestMapping(value = "/orders-by-week", method = RequestMethod.GET)
    @ResponseBody
    public Object getOrdersByWeek(
            @RequestParam(value = "from", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date from,
            @RequestParam(value = "to", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date to,
            @RequestParam(value = "customerId", required = false) Long customerId,
            @RequestParam(value = "agentId", required = false) Long agentId)
    {
        return orderService.getByWeek(from, to, customerId, agentId);
    }

    @RequestMapping(value = "/orders-by-day", method = RequestMethod.GET)
    @ResponseBody
    public Object getOrdersByDay(
            @RequestParam(value = "from", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date from,
            @RequestParam(value = "to", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date to,
            @RequestParam(value = "customerId", required = false) Long customerId,
            @RequestParam(value = "agentId", required = false) Long agentId)
    {
        return orderService.getByDay(from, to, customerId, agentId);
    }

    @RequestMapping(value = "/order/{id:\\d+}/invoices", method = RequestMethod.GET)
    @ResponseBody
    public Object getInvoices(@PathVariable("id") Long orderId) throws Exception
    {
        return orderService.getInvoices(orderId);
    }

    @RequestMapping(value = "/order/{id:\\d+}/status-messages", method = RequestMethod.GET)
    @ResponseBody
    public Object getStatusMessages(@PathVariable("id") Long orderId, @RequestParam Map<String, Object> criteria,
            Pageable pageable) throws Exception
    {
        return orderService.getStatusMessages(orderId, criteria, pageable);
    }

    @RequestMapping(value = "/order/{id:\\d+}/cancel", method = RequestMethod.PUT)
    @ResponseBody
    public Object cancelOrder(@PathVariable("id") Long orderId) throws Exception
    {
        return orderService.cancelOrder(orderId);
    }

    @RequestMapping(value = "/order/{id:\\d+}/upload", method = RequestMethod.POST)
    @ResponseBody
    public Object upload(@PathVariable("id") Long orderId, @RequestParam("file") MultipartFile file,
            @RequestParam(required = false) String description) throws Exception
    {
        return orderService.upload(orderId, file, description);
    }

    @RequestMapping(value = "/order/file/{id:\\d+}", method = RequestMethod.GET)
    @ResponseBody
    public Object downloadOrderDocument(@PathVariable("id") Long orderId) throws Exception
    {
        return orderService.downloadOrderDocument(orderId);
    }

    @RequestMapping(value = "/order/file/{id:\\d+}", method = RequestMethod.DELETE)
    @ResponseBody
    public Object deleteOrderDocument(@PathVariable("id") Long orderId) throws Exception
    {
        orderService.deleteOrderDocument(orderId);

        return MapBuilder.ok()
                .toMap();
    }

    @RequestMapping(value = "/order/{id:\\d+}/upload-pod", method = RequestMethod.POST)
    @ResponseBody
    public Object uploadPOD(@PathVariable("id") Long orderId, @RequestParam("file") MultipartFile file,
            @RequestParam(required = false) String description) throws Exception
    {
        return orderService.uploadPOD(orderId, file, description);
    }

    @RequestMapping(value = "/order/{id:\\d+}/pod", method = RequestMethod.GET)
    @ResponseBody
    public Object downloadPod(@PathVariable("id") Long orderId) throws Exception
    {
        return orderService.downloadPod(orderId);
    }

    @RequestMapping(value = "/order/{id:\\d+}/legacy-pod", method = RequestMethod.GET)
    @ResponseBody
    public Object downloadLegacyPod(@PathVariable("id") Long orderId) throws Exception
    {
        return orderService.downloadLegacyPod(orderId);
    }

    @RequestMapping(value = "/order/{id:\\d+}/charge", method = RequestMethod.POST)
    @ResponseBody
    public Object addCharge(@PathVariable("id") Long orderId, @RequestBody Map<String, Object> chargeData)
            throws Exception
    {
        return ChargeView.get(apiSession.getRole(), orderService.addCharge(orderId, chargeData));
    }

    @RequestMapping(value = "/order/charge/{id:\\d+}", method = RequestMethod.PUT)
    @ResponseBody
    public Object updateCharge(@PathVariable("id") Long chargeId, @RequestBody Map<String, Object> chargeData)
            throws Exception
    {
        return ChargeView.get(apiSession.getRole(), orderService.updateCharge(chargeId, chargeData));
    }

    @RequestMapping(value = "/order/charge/{id:\\d+}", method = RequestMethod.DELETE)
    @ResponseBody
    public Object deleteCharge(@PathVariable("id") Long chargeId) throws Exception
    {
        return orderService.deleteCharge(chargeId);
    }

    @RequestMapping(value = "/charge/mark-reconciled/{id:\\d+}", method = RequestMethod.PUT)
    @ResponseBody
    public Object reconcileCharge(@PathVariable("id") Long chargeId) throws Exception
    {
        return ChargeView.get(apiSession.getRole(), orderService.reconcileCharge(chargeId));
    }

    @RequestMapping(value = "/order/mark-reconciled/{id:\\d+}", method = RequestMethod.PUT)
    @ResponseBody
    public Object reconcileOrderCharges(@PathVariable("id") Long orderId) throws Exception
    {
        return OrderView.get(orderService, orderService.reconcileOrderCharges(orderId));
    }

    // add a logged event (status message)
    @RequestMapping(value = "/order/{id:\\d+}/update", method = RequestMethod.PUT)
    @ResponseBody
    public Object addStatusUpdate(@PathVariable("id") Long orderId, @RequestBody Map<String, Object> attributes)
            throws Exception
    {
        return orderService.addStatusUpdate(orderId, attributes);
    }

    //

    @RequestMapping(value = "/order/stats-per-type", method = RequestMethod.GET)
    @ResponseBody
    public Object statsPerType() throws Exception
    {
        return orderService.statsPerType();
    }

    @RequestMapping(value = "/orders-with-disputes", method = RequestMethod.GET)
    @ResponseBody
    public Object ordersWithDisputes(@RequestParam Map<String, Object> criteria, Principal principal,
            HttpServletResponse response, Pageable pageable) throws Exception
    {
        return getOrdersImpl(criteria, principal, response, pageable, OrderSpecification.Mode.DISPUTED);
    }

    @RequestMapping(value = "/orders-with-claims", method = RequestMethod.GET)
    @ResponseBody
    public Object ordersWithClaims(@RequestParam Map<String, Object> criteria, Principal principal,
            HttpServletResponse response, Pageable pageable) throws Exception
    {
        if (!apiSession.getRole()
                .canManageClaims()) {
            throw new ResourceNotFoundException("Not authorized");
        }

        return getOrdersImpl(criteria, principal, response, pageable, OrderSpecification.Mode.HAS_CLAIM);
    }

    @RequestMapping(value = "/update-ready-for-shipping", method = RequestMethod.POST)
    @ResponseBody
    @PreAuthorize("hasAuthority('ADMIN')")
    public Object updateReadyForShipping() throws Exception
    {
        orderService.updateReadyForShipping();

        return "ok";
    }

    // test get ups rates
    @RequestMapping(value = "/order/{id:\\d+}/test-ups", method = RequestMethod.GET)
    @ResponseBody
    @PreAuthorize("hasAuthority('ADMIN')")
    public Object testUPS(@PathVariable("id") Long orderId) throws Exception
    {
        return orderService.testUPS(orderId);
    }

    // test get ups rates with times
    @RequestMapping(value = "/order/{id:\\d+}/test-ups-times", method = RequestMethod.GET)
    @ResponseBody
    @PreAuthorize("hasAuthority('ADMIN')")
    public Object testUPSTimes(@PathVariable("id") Long orderId) throws Exception
    {
        // TODO - obsolete
        return orderService.testUPS(orderId);
    }

    /**
     *
     */
    @RequestMapping(value = "/order/duplicate/{id:\\d+}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Object duplicate(@PathVariable("id") Long orderId) throws Exception
    {
        return orderService.duplicate(orderId);
    }

    /**
     *
     */
    @RequestMapping(value = "/order/customer-users/{id:\\d+}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Object customerUsers(@PathVariable("id") Long orderId) throws Exception
    {
        return orderService.customerUsers(orderId);
    }

    /**
     *
     */
    @RequestMapping(value = "/order/shipping-label/{id:\\d+}", method = RequestMethod.GET)
    @ResponseBody
    public Object shippingLabel(@PathVariable("id") Long orderId) throws Exception
    {
        return orderService.shippingLabel(orderId);
    }
}
