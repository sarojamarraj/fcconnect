package com.freightcom.api.controllers;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.type.TypeReference;
import com.freightcom.api.model.Agent;
import com.freightcom.api.model.views.AgentView;
import com.freightcom.api.model.views.View;
import com.freightcom.api.services.AgentServices;
import com.freightcom.api.services.converters.AgentConverter;


@RestController
public class  AgentController extends BaseController
{
    @SuppressWarnings("unused")
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final AgentServices agentServices;
    private final PagedResourcesAssembler<View> assembler;

    @Autowired
    @Qualifier("mappingJackson2HttpMessageConverter")
    private MappingJackson2HttpMessageConverter messageConverter;

    @Autowired
    public AgentController(final AgentServices agentServices,
                           final PagedResourcesAssembler<View> assembler)
    {
        this.agentServices = agentServices;
        this.assembler = assembler;
    }

    /**
     * List agents
     */
    @RequestMapping(value = "/agent", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Object listAgents(@RequestParam(required = false) Map<String,String> criteria, Pageable pageable) throws Exception
    {
        return assembler.toResource(agentServices.listAgents(criteria, pageable).map(new AgentConverter()),
                                    new Link("/agent"));
    }

    /**
     *
     */
    @RequestMapping(value = "/agent/{id:\\d+}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Object agentInfo(@PathVariable(value = "id") Long agentId) throws Exception
    {
        return new AgentView(agentServices.getAgent(agentId));
    }

    @RequestMapping(value = "/agent/{id}", method = RequestMethod.PUT)
    @ResponseBody
    @Transactional
    public Object updateAgent(@PathVariable(value = "id") Long agentId, @RequestBody String json) throws Exception
    {
        Agent agentData = messageConverter.getObjectMapper()
                .readValue(json, Agent.class);
        Map<String, Object> attributes = messageConverter.getObjectMapper()
                .readValue(json, new TypeReference<HashMap<String, Object>>() {
                });

        return new AgentView(agentServices.update(agentId, agentData, attributes));
    }
}
