package com.freightcom.api.carrier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.freightcom.api.carrier.fedex.service.FedexService;
import com.freightcom.api.carrier.ups.service.UPSService;
import com.freightcom.api.model.CustomerOrder;

@Component
public class CarrierServicesImpl implements CarrierServices
{
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private FedexService fedex;

    @Autowired
    private UPSService ups;

    public CarrierServicesImpl()
    {

    }

    @Override
    public boolean hasImplementation(CustomerOrder order) throws Exception
    {
        return order.getSelectedQuote()
                .getService()
                .getCarrier()
                .getImplementingClass()
                .equalsIgnoreCase("UPSAPI");
    }

    @Override
    public void bookOrder(CustomerOrder order) throws Exception
    {
        if (order.getSelectedQuote()
                .getService()
                .getCarrier()
                .getImplementingClass()
                .equalsIgnoreCase("UPSAPI")) {

            ups.bookOrder(order);
        }
    }

}
