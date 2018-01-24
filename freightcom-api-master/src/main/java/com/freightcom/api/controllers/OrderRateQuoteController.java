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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.type.TypeReference;
import com.freightcom.api.ApiSession;
import com.freightcom.api.controllers.transfer.MessagePayload;
import com.freightcom.api.model.CustomerOrder;
import com.freightcom.api.model.OrderRateQuote;
import com.freightcom.api.model.views.OrderRateQuoteView;
import com.freightcom.api.repositories.custom.OrderRateQuoteRepository;
import com.freightcom.api.services.OrderRateQuoteService;
import com.freightcom.api.services.UserDetailsImpl;

@RestController
public class OrderRateQuoteController extends BaseController
{
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final OrderRateQuoteService orderRateQuoteService;
    private final OrderRateQuoteRepository orderRateQuoteRepository;
    private final ApiSession apiSession;
    @Autowired
    @Qualifier("mappingJackson2HttpMessageConverter")
    private MappingJackson2HttpMessageConverter messageConverter;

    @Autowired
    public OrderRateQuoteController(final OrderRateQuoteService orderRateQuoteService, final ApiSession apiSession,
            final OrderRateQuoteRepository orderRateQuoteRepository,
            final PagedResourcesAssembler<OrderRateQuote> pagedAssembler)
    {
        this.orderRateQuoteService = orderRateQuoteService;
        this.apiSession = apiSession;
        this.orderRateQuoteRepository = orderRateQuoteRepository;
    }

    /**
     *
     */
    @RequestMapping(value = "/carrier_rates/{orderId:\\d+}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public MessagePayload getQuotes(@PathVariable(value = "orderId") Long orderId,
            @RequestParam Map<String, String> criteria, Principal principal, Pageable pageable) throws Exception
    {
        log.debug("Trigger Quote");
        CustomerOrder order = orderRateQuoteService.validateOrder(orderId);

        return orderRateQuoteService.getRates(order, getLoggedInUser(principal), pageable);
    }

    /**
     *
     */
    @RequestMapping(value = "/carrier_rates/more/{orderId:\\d+}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public MessagePayload moreQuotes(@PathVariable(value = "orderId") Long orderId,
            @RequestParam Map<String, String> criteria, Principal principal, Pageable pageable) throws Exception
    {
        log.debug("Trigger Quote");
        CustomerOrder order = orderRateQuoteService.validateOrder(orderId);

        return orderRateQuoteService.moreRates(order, getLoggedInUser(principal), pageable);
    }

    /**
     *
     */
    @RequestMapping(value = "/carrier_rates/{orderId:\\d+}/refresh", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public MessagePayload refreshQuotes(@PathVariable(value = "orderId") Long orderId,
            @RequestParam Map<String, String> criteria, Principal principal, Pageable pageable) throws Exception
    {
        log.debug("Trigger Refresh Quote");
        CustomerOrder order = orderRateQuoteService.validateOrder(orderId);

        return  orderRateQuoteService.refreshQuote(order, getLoggedInUser(principal), pageable);
    }

    /**
     *
     */
    @RequestMapping(value = "/carrier_rates/{orderId:\\d+}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Integer> deleteQuotes(@PathVariable(value = "orderId") Long orderId,
            @RequestParam Map<String, String> criteria, Principal principal) throws Exception
    {
        log.debug("Delete Quotes");
        CustomerOrder order = orderRateQuoteService.getOrder(orderId);

        if (order == null) {
            throw new ResourceNotFoundException("Unable to locate order " + orderId);
        }

        List<Integer> result = new ArrayList<Integer>();
        result.add(orderRateQuoteService.deleteRates(order, getLoggedInUser(principal)));

        return result;
    }

    /**
     *
     */
    @RequestMapping(value = "/order_rate_quote", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Object getOrderRateQuote(@RequestParam Map<String, String> criteria, Principal principal, Pageable pageable)
            throws Exception
    {
        log.debug("FETCHING orderRateQuote");
        getLoggedInUser(principal);

        return orderRateQuoteService.getOrderRateQuotes(criteria, pageable);
    }

    /**
     *
     */
    @RequestMapping(value = "/order_rate_quote/{id:\\d+}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<OrderRateQuoteView> getById(@PathVariable("id") Long orderRateQuoteId, Principal principal)
            throws Exception
    {
        getLoggedInUser(principal);

        OrderRateQuote orderRateQuote = orderRateQuoteRepository.findOne(orderRateQuoteId);

        if (orderRateQuote == null) {
            throw new ResourceNotFoundException("Not authorized");
        }

        return new ResponseEntity<OrderRateQuoteView>(new OrderRateQuoteView(orderRateQuote), HttpStatus.OK);
    }

    @RequestMapping(value = "/order_rate_quote/{id:\\d+}", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<OrderRateQuoteView> updateOrderRateQuote(@PathVariable(value = "id") Long id,
            @RequestBody String json, Principal principal) throws Exception
    {
        UserDetailsImpl userDetails = getLoggedInUser(principal);

        OrderRateQuote orderRateQuoteData = messageConverter.getObjectMapper()
                .readValue(json, OrderRateQuote.class);
        Map<String, String> attributes = messageConverter.getObjectMapper()
                .readValue(json, new TypeReference<HashMap<String, Object>>() {
                });

        OrderRateQuote orderRateQuote = orderRateQuoteService.updateOrderRateQuote(id, orderRateQuoteData, attributes,
                userDetails, apiSession.getRole());

        return new ResponseEntity<OrderRateQuoteView>(new OrderRateQuoteView(orderRateQuote), HttpStatus.OK);
    }

    @RequestMapping(value = "/order_rate_quote", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<OrderRateQuoteView> create_orderRateQuote(@RequestBody OrderRateQuote orderRateQuoteData)
            throws Exception
    {
        OrderRateQuote orderRateQuote = orderRateQuoteService.createOrderRateQuote(orderRateQuoteData);
        log.debug("create orderRateQuote " + orderRateQuoteData);

        return new ResponseEntity<OrderRateQuoteView>(new OrderRateQuoteView(orderRateQuote), HttpStatus.OK);
    }

    /**
     *
     */
    public Object getOrderRateQuotesImpl(Principal principal, Pageable pageable) throws Exception
    {
        Map<String, String> criteria = new HashMap<String, String>();
        getLoggedInUser(principal);

        return orderRateQuoteService.getOrderRateQuotes(criteria, pageable);
    }

    /**
     *
     */
    @RequestMapping(value = "/order_rate_quote/{id:\\d+}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<String> deleteOrderRateQuote(@PathVariable("id") Long orderRateQuoteId, Principal principal)
            throws Exception
    {
        List<String> result = new ArrayList<String>(1);

        result.add(orderRateQuoteService.deleteOrderRateQuote(orderRateQuoteId, getLoggedInUser(principal)));

        return result;
    }

    /**
     * Clean up orphaned quotes
     */
    @RequestMapping(value = "/order_rate_quote", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Object> cleanupOrderRateQuote(Principal principal) throws Exception
    {
        List<Object> result = new ArrayList<Object>(1);

        result.add(new Integer(orderRateQuoteService.cleanupOrderRateQuote(getLoggedInUser(principal))));

        return result;
    }

}
