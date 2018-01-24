package com.freightcom.api.controllers;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
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

import com.freightcom.api.model.Charge;
import com.freightcom.api.model.views.View;
import com.freightcom.api.services.ChargeService;
import com.fasterxml.jackson.core.type.TypeReference;



@RestController
public class ChargeController extends BaseController
{
    @SuppressWarnings("unused")
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final ChargeService chargeService;

    @Autowired
    @Qualifier("mappingJackson2HttpMessageConverter")
    private MappingJackson2HttpMessageConverter messageConverter;

    @Autowired
    public ChargeController(final ChargeService chargeService)
    {
        this.chargeService = chargeService;
    }

    /**
     *
     */
    @RequestMapping(value = "/charge", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Object getInvoice(@RequestParam Map<String, Object> criteria, Pageable pageable) throws Exception
    {
        return chargeService.getCharges(criteria, pageable);
    }

    /**
     *
     */
    @RequestMapping(value = "/charge/search/findByInvoiceId", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Object findByInvoiceId(@RequestParam Long invoice_id, Pageable pageable) throws Exception
    {
        return chargeService.findByInvoiceId(invoice_id, pageable);
    }

    /**
     *
     */
    @RequestMapping(value = "/charge/search/findByOrderId", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Object findByOrderId(@RequestParam Long order_id, Pageable pageable) throws Exception
    {
        return chargeService.findByOrderId(order_id, pageable);
    }

    @RequestMapping(value = "/charge/reconcile/{id:\\d+}", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<View> reconcileCharge(@PathVariable(value = "id") Long id,
                                                @RequestBody String json) throws Exception
    {
        Charge chargeData = messageConverter.getObjectMapper()
                .readValue(json, Charge.class);
        Map<String, Object> attributes = messageConverter.getObjectMapper()
                .readValue(json, new TypeReference<HashMap<String, Object>>() {
                });

        return new ResponseEntity<View>(chargeService.reconcileCharge(id, chargeData, attributes), HttpStatus.OK);
    }
}
