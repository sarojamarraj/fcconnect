package com.freightcom.api.carrier;

import com.freightcom.api.model.CustomerOrder;

public interface CarrierServices
{
    void bookOrder(CustomerOrder order) throws Exception;

    boolean hasImplementation(CustomerOrder order) throws Exception;
}
