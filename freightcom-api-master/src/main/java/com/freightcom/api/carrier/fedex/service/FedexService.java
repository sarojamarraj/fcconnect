package com.freightcom.api.carrier.fedex.service;

import com.freightcom.api.carrier.ups.service.ServiceChecker;
import com.freightcom.api.model.Carrier;
import com.freightcom.api.model.CustomerOrder;
import com.freightcom.api.repositories.ObjectBase;
import com.freightcom.api.services.orders.RateRequestResult;

public interface FedexService
{

    public RateRequestResult getRates(CustomerOrder order, Carrier carrier, ServiceChecker serviceChecker, ObjectBase objectBase)
            throws Exception;

}
