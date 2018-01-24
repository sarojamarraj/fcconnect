package com.freightcom.api.carrier.ups.service;

import com.freightcom.api.model.Carrier;
import com.freightcom.api.model.Service;

/**
 * @author bryan
 *
 */
public interface ServiceChecker
{
    public Service get(String code, String description, Carrier carrier, String string);
}
