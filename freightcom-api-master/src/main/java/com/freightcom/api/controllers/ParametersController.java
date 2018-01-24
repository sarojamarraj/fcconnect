package com.freightcom.api.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.freightcom.api.ApiSession;
import com.freightcom.api.services.ParametersService;
import com.freightcom.api.services.SystemPropertyService;

@RestController
public class ParametersController extends BaseController
{
    @SuppressWarnings("unused")
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final ParametersService parametersService;
    @SuppressWarnings("unused")
    private final ApiSession apiSession;

    @Autowired
    public ParametersController(final SystemPropertyService systemPropertyService, final ApiSession apiSession,
            final ParametersService parametersService)
    {
        this.parametersService = parametersService;
        this.apiSession = apiSession;
    }

    /**
     *
     */
    @RequestMapping(value = "/currencies", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Object getCurrencies() throws Exception
    {
        return parametersService.getCurrencies();
    }
}
