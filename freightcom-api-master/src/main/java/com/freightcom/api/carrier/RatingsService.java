package com.freightcom.api.carrier;

import com.freightcom.api.carrier.ups.service.ServiceChecker;
import com.freightcom.api.model.CustomerOrder;
import com.freightcom.api.repositories.ObjectBase;
import com.freightcom.api.services.orders.RateRequestResult;

public interface RatingsService
{

    RateRequestResult getRates(CustomerOrder order, ServiceChecker serviceChecker, ObjectBase objectBase)
            throws Exception;

}
