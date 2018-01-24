package com.freightcom.api.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.freightcom.api.repositories.ObjectBase;

/**
 * @author bryan
 *
 */
@Component
public class ParametersService
{
    private final Logger log = LoggerFactory.getLogger(this.getClass());


    private final PermissionChecker permissionChecker;
    private final ObjectBase objectBase;

    @Autowired
    public ParametersService(final PermissionChecker permissionChecker,
                             final ObjectBase objectBase)
    {
        this.objectBase = objectBase;
        this.permissionChecker = permissionChecker;
    }


    public Object getCurrencies()
    {
        return objectBase.listCurrencies();
    }

}
