package com.freightcom.api.services;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;

import com.freightcom.api.model.CustomerOrder;

public class OrderLock
{
    private final static Logger log = LoggerFactory.getLogger(OrderLock.class);

    private static final int MAX_ENTRIES = 1000;
    private static int counter = 0;
    private final int id;
    private final long orderId;

    private static Map<Long, OrderLock> locks = new LinkedHashMap<Long, OrderLock>(MAX_ENTRIES + 1, .75F, true) {
        /**
        *
        */
        private static final long serialVersionUID = 1L;

        public boolean removeEldestEntry(Map.Entry<Long, OrderLock> eldest)
        {
            return size() > MAX_ENTRIES;
        }
    };

    private OrderLock(Long orderId)
    {
        id = counter++;
        this.orderId = orderId;
    }

    public String toString()
    {
        return "ORDER LOCK " + orderId + " " + id;
    }

    public static OrderLock getLock(CustomerOrder order)
    {
        if (order == null) {
            return getLock(0L);
        } else {
            return getLock(order.getId());
        }
    }

    public static synchronized OrderLock getLock(Long id)
    {
        OrderLock lock = locks.get(id);

        if (lock == null) {
            lock = new OrderLock(id);

            locks.put(id, lock);
        }

        log.debug("GOT " + lock);

        return lock;
    }
}
