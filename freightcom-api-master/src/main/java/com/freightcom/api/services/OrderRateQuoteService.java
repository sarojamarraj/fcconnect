package com.freightcom.api.services;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Random;

import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.ConfigurablePropertyAccessor;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.freightcom.api.ApiSession;
import com.freightcom.api.carrier.ups.service.UPSService;
import com.freightcom.api.controllers.transfer.MessagePayload;
import com.freightcom.api.model.CustomerOrder;
import com.freightcom.api.model.OrderRateQuote;
import com.freightcom.api.model.UserRole;
import com.freightcom.api.model.views.OrderRateQuoteView;
import com.freightcom.api.repositories.CustomerOrderRepository;
import com.freightcom.api.repositories.ObjectBase;
import com.freightcom.api.repositories.ServiceRepositoryBase;
import com.freightcom.api.repositories.custom.OrderRateQuoteRepository;
import com.freightcom.api.repositories.custom.OrderRateQuoteSpecification;
import com.freightcom.api.services.converters.OrderRateQuoteConverter;
import com.freightcom.api.services.orders.RateRequestResult;

/**
 * @author bryan
 *
 */
@Component
public class OrderRateQuoteService
{
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final OrderRateQuoteRepository orderRateQuoteRepository;
    private final CustomerOrderRepository orderRepository;
    private final OrderService orderService;

    private final PagedResourcesAssembler<OrderRateQuote> pagedAssembler;
    private final PagedResourcesAssembler<OrderRateQuoteView> orderRateQuoteAssembler;
    private final ApplicationEventPublisher publisher;
    private final ObjectBase objectBase;
    private final ApiSession apiSession;

    @Autowired
    public OrderRateQuoteService(final OrderRateQuoteRepository orderRateQuoteRepository,
            final PagedResourcesAssembler<OrderRateQuote> pagedAssembler,
            final PagedResourcesAssembler<OrderRateQuoteView> orderRateQuoteAssembler, final ApiSession apiSession,
            final ServiceRepositoryBase serviceRepository, final ApplicationEventPublisher publisher,
            final CustomerOrderRepository orderRepository, final OrderService orderService, final ObjectBase objectBase,
            final UPSService upsService)
    {
        this.orderRateQuoteRepository = orderRateQuoteRepository;
        this.pagedAssembler = pagedAssembler;
        this.orderRateQuoteAssembler = orderRateQuoteAssembler;
        this.publisher = publisher;
        this.orderRepository = orderRepository;
        this.orderService = orderService;
        this.objectBase = objectBase;
        this.apiSession = apiSession;
    }

    public PagedResources<Resource<OrderRateQuote>> getOrderRateQuotes(Map<String, String> criteria, Pageable pageable)
    {
        Page<OrderRateQuote> orderRateQuotes = orderRateQuoteRepository
                .findAll(new OrderRateQuoteSpecification(criteria), pageable);

        return pagedAssembler.toResource(orderRateQuotes);
    }

    public PagedResources<Resource<OrderRateQuoteView>> getOrderRateQuotesConverted(Map<String, String> criteria,
            Pageable pageable)
    {
        Page<OrderRateQuoteView> orderRateQuotes = orderRateQuoteRepository
                .findAll(new OrderRateQuoteSpecification(criteria), pageable)
                .map(new OrderRateQuoteConverter());

        return orderRateQuoteAssembler.toResource(orderRateQuotes, new Link("/customerOrderRateQuote"));
    }

    @Transactional
    public OrderRateQuote createOrUpdateOrderRateQuote(OrderRateQuote orderRateQuote) throws Exception
    {
        orderRateQuoteRepository.save(orderRateQuote);

        return orderRateQuote;
    }

    /**
     * @throws Exception
     *
     */
    public OrderRateQuote createOrderRateQuote(final OrderRateQuote orderRateQuote) throws Exception
    {
        OrderRateQuote newOrderRateQuote;

        if (orderRateQuote.getId() != null) {
            newOrderRateQuote = new OrderRateQuote();
            BeanUtils.copyProperties(orderRateQuote, newOrderRateQuote);
        } else {
            newOrderRateQuote = orderRateQuote;
        }

        return createOrUpdateOrderRateQuote(newOrderRateQuote);
    }

    /**
     *
     */
    @Transactional
    public OrderRateQuote updateOrderRateQuote(Long id, OrderRateQuote orderRateQuote, Map<String, String> attributes,
            UserDetails loggedInUser, UserRole userRole) throws Exception
    {
        OrderRateQuote existing = orderRateQuoteRepository.findOne(id);

        if (existing == null) {
            throw new ResourceNotFoundException("No such orderRateQuote");
        }

        ConfigurablePropertyAccessor source = PropertyAccessorFactory.forDirectFieldAccess(orderRateQuote);
        ConfigurablePropertyAccessor dest = PropertyAccessorFactory.forDirectFieldAccess(existing);

        // Only copy attributes supplied in the JSON input, leave others alone
        for (String key : attributes.keySet()) {
            if (dest.isWritableProperty(key)) {
                dest.setPropertyValue(key, source.getPropertyValue(key));
            }
        }

        return createOrUpdateOrderRateQuote(existing);
    }

    /**
     *
     */
    @Transactional
    public String deleteOrderRateQuote(Long orderRateQuoteId, UserDetails loggedInUser)
    {
        OrderRateQuote orderRateQuote = orderRateQuoteRepository.findOne(orderRateQuoteId);

        if (orderRateQuote == null) {
            throw new ResourceNotFoundException("No such orderRateQuote");
        }

        orderRateQuoteRepository.delete(orderRateQuote);

        return "ok";
    }

    public MessagePayload getRates(CustomerOrder order, UserDetailsImpl userDetailsImpl, Pageable pageable)
            throws Exception
    {
        return getRates(order, userDetailsImpl, pageable, false);
    }

    @Transactional
    public MessagePayload getRates(CustomerOrder order, UserDetailsImpl loggedInUser, Pageable pageable, boolean more)
            throws Exception
    {
        RateRequestResult quoteResult = null;

        log.debug("ORDER QUOTED AT " + order.getQuotedAt());

        if (order.getQuotedAt() == null || more || order.getQuotedAt()
                .toInstant()
                .isBefore(Instant.now()
                        .minus(16, ChronoUnit.MINUTES))) {

            if (!more) {
                deleteRates(order, loggedInUser);
            }

            publisher.publishEvent(new RateRequestEvent(order));

            quoteResult = getQuote(order);

            order.setQuotedAt(new Date());
            orderRepository.save(order);
        }

        log.debug("DONE GET RATES " + quoteResult + " " + quoteResult.getMessages() + " " + quoteResult.getMessages()
                .size());

        Page<OrderRateQuoteView> page = orderRateQuoteRepository.findByOrderId(order.getId(), pageable)
                .map(new OrderRateConverter());

        return new MessagePayload(orderRateQuoteAssembler.toResource(page, new Link("/carrier_rates")),
                quoteResult == null ? new ArrayList<Object>() : quoteResult.getMessages());
    }

    @Async("threadPoolTaskExecutor")
    @EventListener
    public void getRateHandler(RateRequestEvent rateRequest) throws Exception
    {
        CustomerOrder order = rateRequest.getOrder();

        // TODO - not automatically fetching more rates
        log.debug("HANDLE GET RATES REQUEST EVENT START" + order);

        for (int i = 0; i < 10; i++) {
            // getQuoteTransaction(order);
        }

        log.debug("HANDLE GET RATES REQUEST EVENT DONE" + order);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    private RateRequestResult getQuoteTransaction(CustomerOrder order) throws Exception
    {
        return getQuote(order);
    }

    private class OrderRateConverter implements Converter<OrderRateQuote, OrderRateQuoteView>
    {

        @Override
        public OrderRateQuoteView convert(OrderRateQuote quote)
        {
            return new OrderRateQuoteView(quote);
        }
    }

    private class RateRequestEvent
    {
        private final CustomerOrder order;

        public RateRequestEvent(final CustomerOrder order)
        {
            this.order = order;
        }

        public CustomerOrder getOrder()
        {
            return order;
        }
    }

    public CustomerOrder getOrder(Long orderId)
    {
        return orderRepository.findOne(orderId);
    }

    public CustomerOrder validateOrder(Long orderId) throws Exception
    {
        CustomerOrder order = getOrder(orderId);

        if (order == null) {
            throw new ResourceNotFoundException("Not authorized");
        }

        UserRole role = apiSession.getRole();

        if (!role.isAdmin() && !role.isFreightcomStaff() && (order.getCustomerId() == null || !order.getCustomerId()
                .equals(role.getCustomerId())) && !role.isAgentFor(order.getCustomer())) {
            throw new AccessDeniedException("Not authorized " + order.getCustomerId() + " C " + role.getCustomerId());
        }

        return order;
    }

    public RateRequestResult getQuote(CustomerOrder order) throws Exception
    {
        return orderService.getRates(order);
    }

    private Integer getDays(int base, int delta, Random generator)
    {
        return (new Double(base + generator.nextDouble() * delta)).intValue();
    }

    private BigDecimal getNumber(double base, double delta, Random generator)
    {
        return (new BigDecimal(base + generator.nextDouble() * delta)).setScale(2, BigDecimal.ROUND_HALF_EVEN);
    }

    @Transactional
    public int cleanupOrderRateQuote(UserDetailsImpl loggedInUser)
    {
        Query query = objectBase.getEntityManager()
                .createNativeQuery(
                        "delete from order_rate_quote  where not exists (select * from customer_order O where O.id=order_rate_quote.order_id)");

        return query.executeUpdate();
    }

    public MessagePayload moreRates(CustomerOrder order, UserDetailsImpl loggedInUser, Pageable pageable)
            throws Exception
    {
        return getRates(order, loggedInUser, pageable, false);
    }

    @Transactional
    public MessagePayload refreshQuote(CustomerOrder order, UserDetailsImpl loggedInUser, Pageable pageable)
            throws Exception
    {
        log.debug("REFRESHING RATES");
        deleteRates(order, loggedInUser);
        order.setQuotedAt(null);
        return getRates(order, loggedInUser, pageable, false);
    }

    @Transactional
    public int deleteRates(CustomerOrder order, UserDetailsImpl loggedInUser)
    {
        int count = 0;

        for (OrderRateQuote rate : orderRateQuoteRepository.findByOrderId(order.getId())) {
            orderRateQuoteRepository.delete(rate);
            count++;
        }

        return count;
    }
}
