package com.freightcom.api.controllers;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.freightcom.api.ApiSession;
import com.freightcom.api.model.views.ClaimView;
import com.freightcom.api.model.views.DisputeView;
import com.freightcom.api.model.views.View;
import com.freightcom.api.model.PalletTemplate;
import com.freightcom.api.model.PalletType;
import com.freightcom.api.model.views.PalletTemplateView;
import com.freightcom.api.services.OrderService;
import com.freightcom.api.util.MapBuilder;

@RestController
public class OrderManagerController extends BaseController
{
    @SuppressWarnings("unused")
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final OrderService orderService;
    private final ApiSession apiSession;
    @SuppressWarnings("unused")
    private final PagedResourcesAssembler<View> assembler;

    @Autowired
    @Qualifier("mappingJackson2HttpMessageConverter")
    private MappingJackson2HttpMessageConverter messageConverter;

    public OrderManagerController(OrderService orderService, final ApiSession apiSession,
            final PagedResourcesAssembler<View> assembler)
    {
        this.orderService = orderService;
        this.apiSession = apiSession;
        this.assembler = assembler;
    }

    @RequestMapping(value = "/order/{id:\\d+}/respond-to-dispute", method = RequestMethod.POST)
    @ResponseBody
    public Object respondToDispute(@PathVariable("id") Long orderId, @RequestBody Map<String, Object> attributes)
            throws Exception
    {
        return DisputeView.get(apiSession.getRole(), orderService.respondToDispute(orderId, attributes));
    }

    @RequestMapping(value = "/order/charge/{chargeId:\\d+}/dispute", method = RequestMethod.POST)
    @ResponseBody
    public Object disputeCharge(@PathVariable("chargeId") Long chargeId, @RequestBody Map<String, Object> attributes)
            throws Exception
    {
        return DisputeView.get(apiSession.getRole(), orderService.disputeCharge(chargeId, attributes));
    }

    @RequestMapping(value = "/order/{orderId:\\d+}/update-claim", method = RequestMethod.PUT)
    @ResponseBody
    public Object updateClaim(@PathVariable("orderId") Long orderId, @RequestBody Map<String, Object> attributes)
            throws Exception
    {
        orderService.updateClaim(orderId, attributes);

        return MapBuilder.ok()
                .toMap();
    }

    @RequestMapping(value = "/order/{orderId:\\d+}/submit-claim", method = RequestMethod.POST)
    @ResponseBody
    public Object submitClaim(@PathVariable("orderId") Long orderId, @RequestBody Map<String, Object> attributes)
            throws Exception
    {
        return new ClaimView(orderService.submitClaim(orderId, attributes));
    }

    @RequestMapping(value = "/order/pallet_types", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PalletType> getPalletTypes(
            @RequestParam(value = "customer_id", required = false) Long customerId) throws Exception
    {
        return orderService.getPalletTypes();
    }

    @RequestMapping(value = "/order/pallet_templates", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PalletTemplateView> getPalletTemplates(
            @RequestParam(value = "customer_id", required = false) Long customerId) throws Exception
    {
        return orderService.getPalletTemplates(customerId).stream()
            .map(template -> PalletTemplateView.get(template))
            .collect(Collectors.toList());
    }

    @RequestMapping(value = "/order/pallet_templates", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PalletTemplateView> createPalletTemplate(@RequestBody PalletTemplate template,@RequestParam(value = "customer_id") Long customerId) throws Exception
    {
        PalletTemplate savedTemplate = orderService.createPalletTemplate(template, customerId);

        return getPalletTemplates(savedTemplate.getCustomer().getId());
    }

    @RequestMapping(value = "/order/pallet_templates/{id:\\d+}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Object createPalletTemplate(@PathVariable("id") Long templateId) throws Exception
    {
        orderService.removePalletTemplate(templateId);
        return "ok";
    }
}
