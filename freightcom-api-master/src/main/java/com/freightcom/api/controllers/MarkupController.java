package com.freightcom.api.controllers;

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
import com.freightcom.api.model.Markup;
import com.freightcom.api.model.views.MarkupView;
import com.freightcom.api.services.MarkupService;
import com.freightcom.api.repositories.custom.MarkupRepository;
import com.freightcom.api.services.UserDetailsImpl;

import com.fasterxml.jackson.core.type.TypeReference;


@RestController
public class MarkupController extends BaseController
{
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final MarkupService markupService;
    private final MarkupRepository markupRepository;
    private final ApiSession apiSession;
    @Autowired
    @Qualifier("mappingJackson2HttpMessageConverter")
    private MappingJackson2HttpMessageConverter messageConverter;

    @Autowired
    public MarkupController(final MarkupService markupService,
                         final ApiSession apiSession,
                         final MarkupRepository markupRepository,
                         final PagedResourcesAssembler<Markup> pagedAssembler
                         ) {
        this.markupService = markupService;
        this.apiSession = apiSession;
        this.markupRepository = markupRepository;
    }

    /**
     *
     */
    @RequestMapping(value = "/markup", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Object getMarkup(@RequestParam Map<String, String> criteria, Principal principal, Pageable pageable) throws Exception
    {
        log.debug("FETCHING markup");
        getLoggedInUser(principal);

        return markupService.getMarkupsConverted(criteria, pageable);
    }

    /**
     *
     */
    @RequestMapping(value = "/markup/{id:\\d+}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<MarkupView> getById(@PathVariable("id") Long markupId, Principal principal) throws Exception
    {
        getLoggedInUser(principal);

        Markup markup = markupRepository.findOne(markupId);

        if (markup == null) {
            throw new ResourceNotFoundException("Not authorized");
        }

        return new ResponseEntity<MarkupView>(new MarkupView(markup), HttpStatus.OK);
    }

    @RequestMapping(value = "/markup/{id:\\d+}", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<MarkupView> updateMarkup(@PathVariable(value = "id") Long id,
                                             @RequestBody String json, Principal principal) throws Exception {
        UserDetailsImpl userDetails = getLoggedInUser(principal);

        Markup markupData = messageConverter.getObjectMapper()
                .readValue(json, Markup.class);
        Map<String, String> attributes = messageConverter.getObjectMapper()
                .readValue(json, new TypeReference<HashMap<String, Object>>() {
                });

        Markup markup = markupService.updateMarkup(id, markupData, attributes, userDetails, apiSession.getRole());

        return new ResponseEntity<MarkupView>(new MarkupView(markup), HttpStatus.OK);
    }

    @RequestMapping(value = "/markup", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<MarkupView> create_markup(@RequestBody Markup markupData) throws Exception {
        Markup markup = markupService.createMarkup(markupData);
        log.debug("create markup " + markupData);

        return new ResponseEntity<MarkupView>(new MarkupView(markup), HttpStatus.OK);
    }

    /**
     *
     */
    public Object getMarkupsImpl(Principal principal, Pageable pageable) throws Exception {
        Map<String,String> criteria = new HashMap<String,String>();
        return markupService.getMarkups(criteria, pageable);
    }

    /**
     *
     */
    @RequestMapping(value = "/markup/{id:\\d+}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<String> deleteMarkup(@PathVariable("id") Long markupId, Principal principal) throws Exception
    {
        List<String> result = new ArrayList<String>(1);

        result.add(markupService.deleteMarkup(markupId, getLoggedInUser(principal)));

        return result;
    }

}
