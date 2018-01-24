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
import com.freightcom.api.model.CurrencyExchange;
import com.freightcom.api.model.views.CurrencyExchangeView;
import com.freightcom.api.services.CurrencyExchangeService;


@RestController
public class CurrencyExchangeController extends BaseController
{
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final CurrencyExchangeService currencyExchangeService;

    @Autowired
    @Qualifier("mappingJackson2HttpMessageConverter")
    private MappingJackson2HttpMessageConverter messageConverter;

    @Autowired
    public CurrencyExchangeController(final CurrencyExchangeService currencyExchangeService,
                         final PagedResourcesAssembler<CurrencyExchange> pagedAssembler
                         ) {
        this.currencyExchangeService = currencyExchangeService;
    }

    /**
     *
     */
    @RequestMapping(value = "/currency-exchange", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Object getCurrencyExchange(@RequestParam Map<String, Object> criteria, Pageable pageable) throws Exception
    {
        return currencyExchangeService.getCurrencyExchangesConverted(criteria, pageable);
    }

    /**
     *
     */
    @RequestMapping(value = "/currency-exchange/{id:\\d+}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<CurrencyExchangeView> getById(@PathVariable("id") Long currencyExchangeId) throws Exception
    {
        return new ResponseEntity<CurrencyExchangeView>(new CurrencyExchangeView(currencyExchangeService.findOne(currencyExchangeId)), HttpStatus.OK);
    }

    @RequestMapping(value = "/currency-exchange/{id:\\d+}", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<CurrencyExchangeView> updateCurrencyExchange(@PathVariable(value = "id") Long id,
                                             @RequestBody String json) throws Exception {
        CurrencyExchange currencyExchangeData = messageConverter.getObjectMapper()
                .readValue(json, CurrencyExchange.class);
        Map<String, String> attributes = messageConverter.getObjectMapper()
                .readValue(json, new TypeReference<HashMap<String, Object>>() {
                });

        CurrencyExchange currencyExchange = currencyExchangeService.updateCurrencyExchange(id, currencyExchangeData, attributes);

        return new ResponseEntity<CurrencyExchangeView>(new CurrencyExchangeView(currencyExchange), HttpStatus.OK);
    }

    @RequestMapping(value = "/currency-exchange", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<CurrencyExchangeView> create_currencyExchange(@RequestBody String json) throws Exception {
        CurrencyExchange currencyExchangeData = messageConverter.getObjectMapper()
                .readValue(json, CurrencyExchange.class);
        Map<String, Object> attributes = messageConverter.getObjectMapper()
                .readValue(json, new TypeReference<HashMap<String, Object>>() {
                });

        CurrencyExchange currencyExchange = currencyExchangeService.createCurrencyExchange(currencyExchangeData, attributes);

        log.debug("create currencyExchange " + currencyExchange);

        return new ResponseEntity<CurrencyExchangeView>(new CurrencyExchangeView(currencyExchange), HttpStatus.OK);
    }

    /**
     *
     */
    @RequestMapping(value = "/currency-exchange/{id:\\d+}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<String> deleteCurrencyExchange(@PathVariable("id") Long currencyExchangeId) throws Exception
    {
        List<String> result = new ArrayList<String>(1);

        result.add(currencyExchangeService.deleteCurrencyExchange(currencyExchangeId));

        return result;
    }

}
