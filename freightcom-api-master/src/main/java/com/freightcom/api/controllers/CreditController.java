package com.freightcom.api.controllers;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
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

import com.freightcom.api.ApiSession;
import com.freightcom.api.controllers.transfer.CreditRefund;
import com.freightcom.api.model.Credit;
import com.freightcom.api.model.views.CreditView;
import com.freightcom.api.services.CreditService;
import com.freightcom.api.repositories.custom.CreditRepository;
import com.freightcom.api.services.UserDetailsImpl;
import com.freightcom.api.services.dataobjects.CreditPayment;
import com.fasterxml.jackson.core.type.TypeReference;
import com.freightcom.api.services.ValidationException;

@RestController
public class CreditController extends BaseController
{
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final CreditService creditService;
    private final CreditRepository creditRepository;
    private final ApiSession apiSession;
    @Autowired
    @Qualifier("mappingJackson2HttpMessageConverter")
    private MappingJackson2HttpMessageConverter messageConverter;

    @Autowired
    public CreditController(final CreditService creditService, final ApiSession apiSession,
            final CreditRepository creditRepository, final PagedResourcesAssembler<Credit> pagedAssembler)
    {
        this.creditService = creditService;
        this.apiSession = apiSession;
        this.creditRepository = creditRepository;
    }

    /**
     *
     */
    @RequestMapping(value = "/credit", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Object getCredit(@RequestParam Map<String, String> criteria, Principal principal, Pageable pageable)
            throws Exception
    {
        log.debug("FETCHING credit");
        getLoggedInUser(principal);

        return creditService.getCreditsConverted(criteria, pageable);
    }

    /**
     *
     */
    @RequestMapping(value = "/credit/{id:\\d+}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<CreditView> getById(@PathVariable("id") Long creditId, Principal principal) throws Exception
    {
        getLoggedInUser(principal);

        Credit credit = creditRepository.findOne(creditId);

        if (credit == null) {
            throw new ResourceNotFoundException("Not authorized");
        }

        return new ResponseEntity<CreditView>(new CreditView(credit), HttpStatus.OK);
    }

    @RequestMapping(value = "/credit/{id:\\d+}", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<CreditView> updateCredit(@PathVariable(value = "id") Long id, @RequestBody String json,
            Principal principal) throws Exception
    {
        UserDetailsImpl userDetails = getLoggedInUser(principal);

        Credit creditData = messageConverter.getObjectMapper()
                .readValue(json, Credit.class);
        Map<String, String> attributes = messageConverter.getObjectMapper()
                .readValue(json, new TypeReference<HashMap<String, Object>>() {
                });

        Credit credit = creditService.updateCredit(id, creditData, attributes, userDetails, apiSession.getRole());

        return new ResponseEntity<CreditView>(new CreditView(credit), HttpStatus.OK);
    }

    @RequestMapping(value = "/credit", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<CreditView> create_credit(@RequestBody Credit creditData) throws Exception
    {
        if (creditData.getAmount() == null || creditData.getAmount()
                .compareTo(BigDecimal.ZERO) < 0) {
            ValidationException.get()
                    .add("amount", "Credit amount must be greater than 0")
                    .doThrow();
        }

        Credit credit = creditService.createCredit(creditData);
        log.debug("create credit " + creditData);

        return new ResponseEntity<CreditView>(new CreditView(credit), HttpStatus.OK);
    }

    @RequestMapping(value = "/refund-credit", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<CreditView> refund_credit(@RequestBody CreditRefund creditRefund) throws Exception
    {
        Credit credit = creditService.refundCredit(creditRefund);
        log.debug("create credit " + creditRefund);

        return new ResponseEntity<CreditView>(new CreditView(credit), HttpStatus.OK);
    }

    /**
     *
     */
    public Object getCreditsImpl(Principal principal, Pageable pageable) throws Exception
    {
        Map<String, String> criteria = new HashMap<String, String>();

        return creditService.getCredits(criteria, pageable);
    }

    /**
     *
     */
    @RequestMapping(value = "/credit/{id:\\d+}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<String> deleteCredit(@PathVariable("id") Long creditId, Principal principal) throws Exception
    {
        List<String> result = new ArrayList<String>(1);

        result.add(creditService.deleteCredit(creditId, getLoggedInUser(principal)));

        return result;
    }

    /**
     * Create a credit from the amount applied to credit card.
     * Generate a payment transaction from that credit.
     */
    @RequestMapping(value = "/credit/add-from-card", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public CreditView addFromCard(@RequestBody CreditPayment payment) throws Exception
    {
        return new CreditView(creditService.addFromCard(payment));
    }

}
