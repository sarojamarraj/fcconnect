package com.freightcom.api.services;

import java.util.Map;

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
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.freightcom.api.ApiSession;
import com.freightcom.api.model.OrderStatus;
import com.freightcom.api.model.UserRole;
import com.freightcom.api.model.views.OrderStatusView;
import com.freightcom.api.repositories.custom.OrderStatusRepository;
import com.freightcom.api.repositories.custom.OrderStatusSpecification;
import com.freightcom.api.services.converters.OrderStatusConverter;

/**
 * @author bryan
 *
 */
@Component
public class OrderStatusService
{
    @SuppressWarnings("unused")
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final OrderStatusRepository orderStatusRepository;

    private final PagedResourcesAssembler<OrderStatus> pagedAssembler;
    private final PagedResourcesAssembler<OrderStatusView> orderStatusAssembler;
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public OrderStatusService(final OrderStatusRepository orderStatusRepository,
            final PagedResourcesAssembler<OrderStatus> pagedAssembler,
            final PagedResourcesAssembler<OrderStatusView> orderStatusAssembler,
            final ApiSession apiSession)
    {
        this.orderStatusRepository = orderStatusRepository;
        this.pagedAssembler = pagedAssembler;
        this.orderStatusAssembler = orderStatusAssembler;
    }

    public PagedResources<Resource<OrderStatus>> getOrderStatuses(Map<String,String> criteria, Pageable pageable)
    {
        Page<OrderStatus> orderStatuses = orderStatusRepository.findAll(new OrderStatusSpecification(criteria), pageable);

        return pagedAssembler.toResource(orderStatuses);
    }

    public PagedResources<Resource<OrderStatusView>> getOrderStatusesConverted(Map<String,String> criteria, Pageable pageable)
    {
        Page<OrderStatusView> orderStatuses = orderStatusRepository.findAll(new OrderStatusSpecification(criteria), pageable)
                .map(new OrderStatusConverter());

        return orderStatusAssembler.toResource(orderStatuses, new Link("/order_status"));
    }

    @Transactional
    public OrderStatus createOrUpdateOrderStatus(OrderStatus orderStatus) throws Exception
    {
        orderStatusRepository.save(orderStatus);

        return orderStatus;
    }

    /**
     * @throws Exception
     *
     */
    public OrderStatus createOrderStatus(final OrderStatus orderStatus) throws Exception
    {
        OrderStatus newOrderStatus;

        if (orderStatus.getId() != null) {
            newOrderStatus = new OrderStatus();
            BeanUtils.copyProperties(orderStatus, newOrderStatus);
        } else {
            newOrderStatus = orderStatus;
        }

        return createOrUpdateOrderStatus(newOrderStatus);
    }

    /**
     *
     */
    @Transactional
    public OrderStatus updateOrderStatus(Long id, OrderStatus orderStatus, Map<String,String> attributes, UserDetails loggedInUser, UserRole userRole) throws Exception
    {
        OrderStatus existing = orderStatusRepository.findOne(id);

        if (existing == null) {
            throw new ResourceNotFoundException("No such orderStatus");
        }

        ConfigurablePropertyAccessor source = PropertyAccessorFactory.forDirectFieldAccess(orderStatus);
        ConfigurablePropertyAccessor dest = PropertyAccessorFactory.forDirectFieldAccess(existing);

        // Only copy attributes supplied in the JSON input, leave others alone
        for (String key : attributes.keySet()) {
            if (dest.isWritableProperty(key)) {
                dest.setPropertyValue(key, source.getPropertyValue(key));
            }
        }

        return createOrUpdateOrderStatus(existing);
    }

    /**
     *
     */
    @Transactional
    public String deleteOrderStatus(Long orderStatusId, UserDetails loggedInUser)
    {
       OrderStatus orderStatus = orderStatusRepository.findOne(orderStatusId);

       if (orderStatus == null) {
           throw new ResourceNotFoundException("No such orderStatus");
       }

       orderStatusRepository.delete(orderStatus);

       return "ok";
    }
}
