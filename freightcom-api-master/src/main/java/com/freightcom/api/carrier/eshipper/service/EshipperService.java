package com.freightcom.api.carrier.eshipper.service;

import com.freightcom.api.carrier.ups.service.ServiceChecker;
import com.freightcom.api.model.Carrier;
import com.freightcom.api.model.CustomerOrder;
import com.freightcom.api.repositories.ObjectBase;
import com.freightcom.api.services.orders.RateRequestResult;

public interface EshipperService
{

    void bookOrder(CustomerOrder order) throws Exception;

    RateRequestResult getRates(CustomerOrder order, Carrier carrier, ServiceChecker serviceChecker,
            ObjectBase objectBase) throws Exception;

}
