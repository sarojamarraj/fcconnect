package com.freightcom.api.controllers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.type.TypeReference;
import com.freightcom.api.model.Invoice;
import com.freightcom.api.model.views.View;
import com.freightcom.api.services.InvoiceService;
import com.freightcom.api.services.dataobjects.InvoicePayment;
import com.freightcom.api.util.MapBuilder;

@RestController
public class InvoiceController extends BaseController
{
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final InvoiceService invoiceService;

    @Autowired
    @Qualifier("mappingJackson2HttpMessageConverter")
    private MappingJackson2HttpMessageConverter messageConverter;

    @Autowired
    public InvoiceController(final InvoiceService invoiceService)
    {
        this.invoiceService = invoiceService;
    }

    /**
     *
     */
    @RequestMapping(value = "/invoice", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Object getInvoice(@RequestParam Map<String, Object> criteria, Pageable pageable) throws Exception
    {
        return invoiceService.getInvoicesConverted(criteria, pageable);
    }

    /**
     *
     */
    @RequestMapping(value = "/invoice/{id:\\d+}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<View> getById(@PathVariable("id") Long invoiceId) throws Exception
    {
        return new ResponseEntity<View>(invoiceService.getIndividualInvoiceView(invoiceId), HttpStatus.OK);
    }

    /**
     *
     */
    @RequestMapping(value = "/invoice/view/{id:\\d+}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Object viewInvoice(@PathVariable("id") Long invoiceId) throws Exception
    {
        try {
            log.debug("START VIEW INVOICE " + invoiceId);

            return invoiceService.view(invoiceId);
        } finally {
            log.debug("DONE VIEW INVOICE " + invoiceId);
        }
    }

    /**
     *
     */
    @RequestMapping(value = "/invoice/pay", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Object payInvoice(@RequestBody InvoicePayment payment) throws Exception
    {
        return invoiceService.pay(payment);
    }

    /**
     * @throws Throwable
     *
     */
    @RequestMapping(value = "/invoice/pay-now", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Object payNow(@RequestBody InvoicePayment payment) throws Throwable
    {
        Object result = invoiceService.payNow(payment);

        if (result instanceof Throwable) {
            throw (Throwable) result;
        }

        return MapBuilder.getNew()
                .put("status", "SUCCESS")
                .getMap();
    }

    @RequestMapping(value = "/invoice/{id:\\d+}", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<View> updateInvoice(@PathVariable(value = "id") Long id, @RequestBody String json)
            throws Exception
    {
        Invoice invoiceData = messageConverter.getObjectMapper()
                .readValue(json, Invoice.class);
        Map<String, Object> attributes = messageConverter.getObjectMapper()
                .readValue(json, new TypeReference<HashMap<String, Object>>() {
                });

        View invoice = invoiceService.updateInvoice(id, invoiceData, attributes);

        return new ResponseEntity<View>(invoice, HttpStatus.OK);
    }

    @RequestMapping(value = "/generate-invoice", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<View> generateInvoice(@RequestBody String json) throws Exception
    {
        Invoice invoiceData = messageConverter.getObjectMapper()
                .readValue(json, Invoice.class);
        Map<String, Object> attributes = messageConverter.getObjectMapper()
                .readValue(json, new TypeReference<HashMap<String, Object>>() {
                });

        View invoice = invoiceService.createInvoice(invoiceData, attributes);

        log.debug("create invoice " + invoice);

        return new ResponseEntity<View>(invoice, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(value = "/run-auto-invoice", method = RequestMethod.POST)
    @ResponseBody
    public Object runAutoInvoice() throws Exception
    {
        invoiceService.autoInvoiceAll();

        return null;
    }

    /**
     *
     */
    @RequestMapping(value = "/invoice", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Object deleteInvoices(@RequestBody Map<String, Object> attributes) throws Exception
    {
        return invoiceService.deleteInvoices(attributes);
    }

    /**
     * For testing
     */
    @RequestMapping(value = "/invoice/{id:\\d+}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @PreAuthorize("hasAuthority('ADMIN')")
    public Object realDeleteInvoices(@PathVariable(value = "id") Long id) throws Exception
    {
        return invoiceService.realDeleteInvoice(id);
    }

    /**
     * Show the calculated values for the apply credit dialog
     */
    @RequestMapping(value = "/view-apply-credit/{id:\\d+}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Object viewApplyCredit(@PathVariable(value = "id") Long id)
    {
        return invoiceService.viewApplyCredit(id);
    }

    /**
     * @throws Throwable
     *
     */
    @RequestMapping(value = "/apply-credit", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Object applyCredit(@RequestBody Map<String, Object> attributes) throws Throwable
    {
        return invoiceService.applyCredit(attributes)
                .stream()
                .map(view -> MapBuilder.ok()
                        .put("creditedAmount", view.invoice().mostRecentCredit())
                        .toMap())
                .collect(Collectors.toList());
    }

    /**
     *
     */
    @RequestMapping(value = "/deleted-invoices", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Object deletedInvoices() throws Exception
    {
        return invoiceService.deletedInvoices();
    }

    /**
     *
     */
    @RequestMapping(value = "/invoice-charges/{ids:(?:\\d+)(?:,\\d+)*}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Object calculateCharges(@PathVariable("ids") String invoiceIds) throws Exception
    {
        return invoiceService.calculateCharges(Arrays.stream(invoiceIds.split(","))
                .map(stringId -> Long.parseLong(stringId)));
    }

}
