package com.freightcom.api.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.type.TypeReference;
import com.freightcom.api.model.XTX;
import com.freightcom.api.model.views.XTXView;
import com.freightcom.api.services.XTXService;


@RestController
public class XTXController extends BaseController
{
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final XTXService xtxService;

    @Autowired
    @Qualifier("mappingJackson2HttpMessageConverter")
    private MappingJackson2HttpMessageConverter messageConverter;

    @Autowired
    public XTXController(final XTXService xtxService,
                         final PagedResourcesAssembler<XTX> pagedAssembler
                         ) {
        this.xtxService = xtxService;
    }

    /**
     *
     */
    @RequestMapping(value = "/xtxp", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Object getXTX(@RequestParam Map<String, Object> criteria, Pageable pageable) throws Exception
    {
        return xtxService.getXTXsConverted(criteria, pageable);
    }

    /**
     *
     */
    @RequestMapping(value = "/xtxp/{id:\\d+}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<XTXView> getById(@PathVariable("id") Long xtxId) throws Exception
    {
        return new ResponseEntity<XTXView>(new XTXView(xtxService.findOne(xtxId)), HttpStatus.OK);
    }

    @RequestMapping(value = "/xtxp/{id:\\d+}", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<XTXView> updateXTX(@PathVariable(value = "id") Long id,
                                             @RequestBody String json) throws Exception {
        XTX xtxData = messageConverter.getObjectMapper()
                .readValue(json, XTX.class);
        Map<String, String> attributes = messageConverter.getObjectMapper()
                .readValue(json, new TypeReference<HashMap<String, Object>>() {
                });

        XTX xtx = xtxService.updateXTX(id, xtxData, attributes);

        return new ResponseEntity<XTXView>(new XTXView(xtx), HttpStatus.OK);
    }

    @RequestMapping(value = "/xtxp", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<XTXView> create_xtx(@RequestBody String json) throws Exception {
        XTX xtxData = messageConverter.getObjectMapper()
                .readValue(json, XTX.class);
        Map<String, Map> attributes = messageConverter.getObjectMapper()
                .readValue(json, new TypeReference<HashMap<String, Object>>() {
                });

        XTX xtx = xtxService.createXTX(xtxData, attributes);

        log.debug("create xtx " + xtx);

        return new ResponseEntity<XTXView>(new XTXView(xtx), HttpStatus.OK);
    }

    /**
     *
     */
    @RequestMapping(value = "/xtxp/{id:\\d+}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<String> deleteXTX(@PathVariable("id") Long xtxId) throws Exception
    {
        List<String> result = new ArrayList<String>(1);

        result.add(xtxService.deleteXTX(xtxId));

        return result;
    }

}
