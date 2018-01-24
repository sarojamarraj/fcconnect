package com.freightcom.api.controllers;

import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

import com.freightcom.api.model.CommissionPayable;
import com.freightcom.api.model.views.CommissionPayableListView;
import com.freightcom.api.model.views.CommissionPayableView;
import com.freightcom.api.services.CommissionsService;
import com.freightcom.api.util.MapBuilder;

@RestController
public class CommissionsController extends BaseController
{
    @SuppressWarnings("unused")
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final CommissionsService commissionsService;

    @Autowired
    public CommissionsController(final CommissionsService commissionsService)
    {
        this.commissionsService = commissionsService;
    }

    /**
     *
     */
    @RequestMapping(value = "/commissionstatement", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Object listCommissions(@RequestParam Map<String, Object> criteria, Pageable pageable) throws Exception
    {
        return commissionsService.listCommissions(criteria, pageable);
    }

    /**
     *
     */
    @RequestMapping(value = "/commissionstatement/{id:\\d+}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Object getCommission(@PathVariable("id") Long commissionId) throws Exception
    {
        return commissionsService.getCommission(commissionId);
    }

    /**
     *
     */
    @RequestMapping(value = "/calculate-commissions", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Object calculateCommissions() throws Exception
    {
        return commissionsService
            .calculateCommissions()
            .stream()
            .map(item -> new CommissionPayableListView(item))
            .collect(Collectors.toList());
    }

    /**
     *
     */
    @RequestMapping(value = "/agent/{id:\\d+}/report-commission", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Object calculateCommissionsAgent(@PathVariable("id") Long agentId) throws Exception
    {
        return commissionsService
            .calculateCommissions(agentId)
            .stream()
            .map(item -> new CommissionPayableListView(item))
            .collect(Collectors.toList());
    }

    /**
     *
     */
    @RequestMapping(value = "/commissionstatement/{id:\\d+}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ActionResponse listCommissions(@PathVariable("id") Long commissionId) throws Exception
    {
        commissionsService.deleteCommission(commissionId);

        return new ActionResponse("commission paid");
    }

    /**
     *
     */
    @RequestMapping(value = "/commissionstatement/{id:\\d+}/paid", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public CommissionPayableView markPaid(@PathVariable("id") Long commissionId) throws Exception
    {
        CommissionPayable commission = commissionsService.markPaid(commissionId);

        return new CommissionPayableView(commission);
    }

    /**
     *
     */
    @RequestMapping(value = "/commissionstatement/run-all", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseBody
    public Object runAll()
    {
        commissionsService.runAllCommissions();

        return MapBuilder.ok();
    }
}
