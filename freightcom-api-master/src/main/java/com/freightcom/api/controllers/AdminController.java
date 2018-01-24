package com.freightcom.api.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class  AdminController extends BaseController
{
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public AdminController()
    {

    }

    /**
     *
     */
    @RequestMapping(value = "/test/error", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Object error(@RequestParam String message) throws Exception
    {
        log.debug("Trigger an error");

        throw new Error("Testing an error");
    }

    /**
     *
     */
    @RequestMapping(value = "/test/exception", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Object xexception(@RequestParam(required=false) String message) throws Exception
    {
        log.debug("Trigger an error");

        if (message == null || message.isEmpty()) {
            throw new Exception("Testing an error");
        } else {
            throw new Exception(message);
        }
    }

    /**
     * @throws Throwable
     *
     */
    @RequestMapping(value = "/test/throwable", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Object xthrowable(@RequestParam(required=false) String message) throws Throwable
    {
        log.debug("Trigger an error");

        if (message == null || message.isEmpty()) {
            throw new Throwable("Testing an error");
        } else {
            throw new Throwable(message);
        }
    }
}
