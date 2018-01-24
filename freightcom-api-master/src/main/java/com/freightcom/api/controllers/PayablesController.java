package com.freightcom.api.controllers;

import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.freightcom.api.controllers.transfer.PrintChequeRequest;
import com.freightcom.api.model.views.PayableView;
import com.freightcom.api.services.PayablesService;
import com.freightcom.api.services.ValidationException;
import com.freightcom.api.util.MapBuilder;

@RestController
public class PayablesController extends BaseController
{
    @SuppressWarnings("unused")
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final PayablesService payablesService;

    @Autowired
    public PayablesController(final PayablesService payablesService)
    {
        this.payablesService = payablesService;
    }

    /**
     *
     */
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('FREIGHTCOM_STAFF')")
    @RequestMapping(value = "/payablestatement", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Object listPayables(@RequestParam Map<String, Object> criteria, Pageable pageable) throws Exception
    {
        return payablesService.listPayables(criteria, pageable);
    }

    /**
     *
     */
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('FREIGHTCOM_STAFF')")
    @RequestMapping(value = "/payablestatement/{id:\\d+}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Object getPayableStatement(@PathVariable("id") Long payableId) throws Exception
    {
        return payablesService.getPayable(payableId);
    }

    /**
     *
     */
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('FREIGHTCOM_STAFF')")
    @RequestMapping(value = "/payablestatement/{id:\\d+}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Object deletePayableStatement(@PathVariable("id") Long payableId) throws Exception
    {
        boolean ok = payablesService.deletePayable(payableId);

        if (ok) {
            return new ActionResponse("payable deleted");
        } else {
            return new ActionResponse(false, "failed");
        }
    }

    /**
     *
     */
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('FREIGHTCOM_STAFF')")
    @RequestMapping(value = "/payablestatement/{id:\\d+}/paid", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Object markPaid(@PathVariable("id") Long payableId) throws Exception
    {
        return new PayableView(payablesService.markPaid(payableId));
    }

    /**
     *
     */
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('FREIGHTCOM_STAFF')")
    @RequestMapping(value = "/payables/generate", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Object generatePayables() throws Exception
    {
        return payablesService.generatePayables()
                .stream()
                .map(payable -> new PayableView(payable))
                .collect(Collectors.toList());
    }

    /**
     *
     */
    @RequestMapping(value = "/payables/run-all", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseBody
    public Object runAll()
    {
        payablesService.runAllPayables();

        return MapBuilder.ok();
    }

    /**
     * @throws Exception 
     *
     */
    @RequestMapping(value = "/payables/print-cheques", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Object printChecks(@RequestBody PrintChequeRequest request) throws Exception
    {
        return payablesService.printCheques(request);
    }
}
