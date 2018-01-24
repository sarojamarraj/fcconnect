package com.freightcom.api.services;

import java.math.BigDecimal;
import java.util.Random;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.freightcom.api.model.CustomerOrder;
import com.freightcom.api.model.OrderRateQuote;
import com.freightcom.api.model.Service;
import com.freightcom.api.repositories.CustomerOrderRepository;
import com.freightcom.api.repositories.ServiceRepositoryBase;
import com.freightcom.api.repositories.custom.OrderRateQuoteRepository;

/**
 * @author bryan
 *
 */
@Component
@Async
public class QuoteProvider
{
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final OrderRateQuoteRepository orderRateQuoteRepository;
    private final CustomerOrderRepository orderRepository;
    private final ServiceRepositoryBase serviceRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public QuoteProvider(final OrderRateQuoteRepository orderRateQuoteRepository,
            final ServiceRepositoryBase serviceRepository,
            final CustomerOrderRepository orderRepository)
    {
        this.orderRateQuoteRepository = orderRateQuoteRepository;
        this.orderRepository = orderRepository;
        this.serviceRepository = serviceRepository;
    }

    public void fakeQuote(CustomerOrder order)
    {
        final int count = 20;

        order = orderRepository.findOne(order.getId());

        Pageable page = new PageRequest(0, count, Sort.Direction.DESC, "id");
        Page<Service> services = serviceRepository.findAll(page);
        Random generator = new Random();

        int remaining = count;

        for (Service service : services) {
            double choice = generator.nextDouble() * remaining;
            log.debug("SERVICE LOOP " + remaining + " " + service + " " + choice);

            if (choice < 1) {
                OrderRateQuote quote = new OrderRateQuote();
                quote.setOrder(order);
                quote.setService(service);
                quote.setCarrier(service.getCarrier());
                quote.setTotalCharges(new BigDecimal(343));

                orderRateQuoteRepository.save(quote);
                log.debug("SAVED QUOTE " + quote);
                break;
            }

            remaining--;
        }
    }
}
