package com.freightcom.api.services.orders;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.freightcom.api.model.AccessorialServices;
import com.freightcom.api.model.CustomerOrder;
import com.freightcom.api.model.OrderAccessorials;
import com.freightcom.api.services.ServiceProvider;

public class OrderAccessorialCharges
{
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final CustomerOrder order;
    private final ServiceProvider provider;

    /**
     *
     */
    public static OrderAccessorialCharges get(final CustomerOrder order, final ServiceProvider provider)
    {
        return new OrderAccessorialCharges(order, provider);
    }

    /**
     *
     */
    private OrderAccessorialCharges(final CustomerOrder order, final ServiceProvider provider)
    {
        this.order = order;
        this.provider = provider;
    }

    public void add()
    {
        for (OrderAccessorials accessorial : order.getAccessorials()) {
            AccessorialServices service = accessorial.getService();
        }
    }
}
