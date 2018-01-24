package com.freightcom.api.carrier.ups.service;

import com.freightcom.api.model.Carrier;
import com.freightcom.api.model.CustomerOrder;
import com.freightcom.api.repositories.ObjectBase;
import com.freightcom.api.services.orders.RateRequestResult;

public interface UPSService
{

    Object rateShipment(CustomerOrder order) throws Exception;

    Object rateShipment(CustomerOrder order, long serviceId) throws Exception;

    Object rateShipment(CustomerOrder order, boolean getTimes) throws Exception;

    Object timeInTransit(CustomerOrder order) throws Exception;

    RateRequestResult getRates(CustomerOrder order, Carrier carrier, ServiceChecker serviceChecker, ObjectBase objectBase)
            throws Exception;

    void bookOrder(CustomerOrder order) throws Exception;

}
