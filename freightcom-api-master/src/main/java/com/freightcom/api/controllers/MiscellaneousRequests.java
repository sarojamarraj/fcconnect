package com.freightcom.api.controllers;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.freightcom.api.model.AccessorialServices;
import com.freightcom.api.model.views.AccessorialServicesView;
import com.freightcom.api.repositories.AccessorialServicesRepository;

@Controller
public class MiscellaneousRequests
{
    @Autowired
    private AccessorialServicesRepository accessorialServicesRepository;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(value = "/accessorial_services", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<AccessorialServicesView> getServices1(Pageable pageable) throws Exception
    {
        return mapServices(accessorialServicesRepository.findAll(pageable));
    }

    @RequestMapping(value = "/accessorial_services/{type}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<AccessorialServicesView> getServicesByType(@PathVariable(value = "type") String type, Pageable pageable) throws Exception
    {
        return mapServices(accessorialServicesRepository.findByType(type, pageable));
    }

    private List<AccessorialServicesView> mapServices(final Iterable<AccessorialServices> services) {
        List<AccessorialServicesView> mapped = new ArrayList<AccessorialServicesView>();

        log.debug("MAP SERVICES");

        for (AccessorialServices service: services) {
            mapped.add(new AccessorialServicesView(service));
        }

        return mapped;
    }
}
