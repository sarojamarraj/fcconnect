package com.freightcom.api.controllers;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.freightcom.api.ApiSession;
import com.freightcom.api.model.UserRole;
import com.freightcom.api.model.views.UserRoleView;
import com.freightcom.api.model.views.UserRoleViewFactory;
import com.freightcom.api.repositories.custom.UserRoleRepository;
import com.freightcom.api.services.UserRoleService;


@RestController
public class UserRoleController extends BaseController
{
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final UserRoleService userRoleService;
    private final UserRoleRepository userRoleRepository;
    @Autowired
    @Qualifier("mappingJackson2HttpMessageConverter")
    private MappingJackson2HttpMessageConverter messageConverter;

    @Autowired
    public UserRoleController(final UserRoleService userRoleService,
                         final ApiSession apiSession,
                         final UserRoleRepository userRoleRepository,
                         final PagedResourcesAssembler<UserRole> pagedAssembler
                         ) {
        this.userRoleService = userRoleService;
        this.userRoleRepository = userRoleRepository;
    }

    /**
     *
     */
    @RequestMapping(value = "/user_role", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Object getUserRole(@RequestParam Map<String, String> criteria, Principal principal, Pageable pageable) throws Exception
    {
        log.debug("FETCHING userRole");
        getLoggedInUser(principal);

        return userRoleService.getUserRoles(criteria, pageable);
    }

    /**
     *
     */
    @RequestMapping(value = "/user_role/{id:\\d+}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<UserRoleView> getById(@PathVariable("id") Long userRoleId, Principal principal) throws Exception
    {
        getLoggedInUser(principal);

        UserRole userRole = userRoleRepository.findOne(userRoleId);

        if (userRole == null) {
            throw new ResourceNotFoundException("Not authorized");
        }

        return new ResponseEntity<UserRoleView>(UserRoleViewFactory.get(userRole), HttpStatus.OK);
    }

    /**
     *
     */
    public Object getUserRolesImpl(Principal principal, Pageable pageable) throws Exception {
        Map<String,String> criteria = new HashMap<String,String>();
        return userRoleService.getUserRoles(criteria, pageable);
    }

    /**
     *
     */
    @RequestMapping(value = "/user_role/{id:\\d+}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<String> deleteUserRole(@PathVariable("id") Long userRoleId, Principal principal) throws Exception
    {
        List<String> result = new ArrayList<String>(1);

        result.add(userRoleService.deleteUserRole(userRoleId, getLoggedInUser(principal)));

        return result;
    }

}
