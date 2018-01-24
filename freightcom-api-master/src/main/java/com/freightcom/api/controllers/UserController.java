package com.freightcom.api.controllers;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.type.TypeReference;
import com.freightcom.api.ApiSession;
import com.freightcom.api.model.Admin;
import com.freightcom.api.model.Agent;
import com.freightcom.api.model.CustomerAdmin;
import com.freightcom.api.model.CustomerStaff;
import com.freightcom.api.model.FreightcomStaff;
import com.freightcom.api.model.User;
import com.freightcom.api.model.UserRole;
import com.freightcom.api.model.views.UserRoleView;
import com.freightcom.api.model.views.UserRoleViewFactory;
import com.freightcom.api.model.views.UserView;
import com.freightcom.api.repositories.UserRepository;
import com.freightcom.api.repositories.custom.UserRoleRepository;
import com.freightcom.api.services.UserDetails;
import com.freightcom.api.services.UserDetailsImpl;
import com.freightcom.api.services.UserService;
import com.freightcom.api.services.ValidationException;

@Controller
public class UserController extends BaseController
{
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final UserService userService;
    @Autowired
    private ApiSession apiSession;

    @Autowired
    @Qualifier("mappingJackson2HttpMessageConverter")
    private MappingJackson2HttpMessageConverter messageConverter;

    /**
     *
     */
    @Autowired
    public UserController(final UserRepository userRepository, final UserService userService,
            final UserRoleRepository userRoleRepository, final PagedResourcesAssembler<User> pagedAssembler)
    {
        this.userRepository = userRepository;
        this.userService = userService;
        this.userRoleRepository = userRoleRepository;
    }

    /**
     * @throws Exception
     *
     */
    @RequestMapping(value = "/session-role", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Object selectSessionRole(Principal principal) throws Exception
    {
        UserRole role = apiSession.getRole();

        if (role != null) {
            return new ResponseEntity<UserRoleView>(UserRoleViewFactory.get(apiSession.getRole()), HttpStatus.OK);
        } else {
            Map<String, String> map = new HashMap<String, String>();
            return new ResponseEntity<Map<String, String>>(map, HttpStatus.OK);
        }
    }

    /**
     * @throws Exception
     *
     */
    @RequestMapping(value = "/set-session-role/{id}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserRoleView> selectSessionRole(@PathVariable("id") Long roleId, Principal principal)
            throws Exception
    {
        UserDetailsImpl loggedInuser = getLoggedInUser(principal);
        UserRole role = userService.identifyRole(loggedInuser, roleId);

        apiSession.setRole(role);

        return new ResponseEntity<UserRoleView>(UserRoleViewFactory.get(role), HttpStatus.OK);
    }

    /**
     *
     */
    @RequestMapping(value = "/user/resetPassword/{id}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Transactional
    public List<String> resetPassword(@PathVariable("id") Long userId)
    {
        List<String> result = new ArrayList<String>();
        log.debug("Reset password " + userId);
        User user = userService.findUser(userId);

        if (user != null && user.canWrite(apiSession.getRole())) {
            userService.resetPassword(user);

            result.add("ok");
        } else {
            result.add("fail");
        }

        return result;
    }

    /**
     *
     */
    @RequestMapping(value = "/user/resetPasswordByEmail", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Transactional
    public String resetPasswordByEmail(@RequestBody Map<String, String> fields)
    {
        log.debug("Reset password " + fields.get("email"));

        List<User> users = userRepository.findByEmail(fields.get("email"));

        try {
            if (users.size() == 1 && users.get(0)
                    .getEmail() != null) {
                log.debug("FOUND USER " + users.get(0));
                userService.resetPassword(users.get(0));

                return "[ \"ok\" ]";
            } else {
                return "[ \"failed\" ]";
            }
        } catch (Exception e) {
            log.info("ERROR IN PASSWORD RESET " + e.getMessage());
            return "[ \"error\" ]";
        }
    }

    /**
     *
     */
    @RequestMapping(value = "/user/update/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<UserView> updateUserOld(@PathVariable(value = "id") Long id, @RequestBody String json,
            Principal principal) throws Exception
    {
        UserDetailsImpl userDetails = getLoggedInUser(principal);

        User userData = messageConverter.getObjectMapper()
                .readValue(json, User.class);
        Map<String, String> attributes = messageConverter.getObjectMapper()
                .readValue(json, new TypeReference<HashMap<String, Object>>() {
                });

        User user = userService.updateUser(id, userData, attributes, userDetails, apiSession.getRole());

        return new ResponseEntity<UserView>(userService.getView(user), HttpStatus.OK);
    }

    /**
     *
     */
    @RequestMapping(value = "/user/search/findByLogin", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Object getUsersSearch(@RequestParam Map<String, Object> criteria, Principal principal, Pageable pageable)
            throws Exception
    {
        getLoggedInUser(principal);

        if (criteria.containsKey("name")) {
            log.debug("SETTING LOGIN QUERY " + criteria.get("name"));
            criteria.put("login", criteria.get("name"));
            criteria.remove("name");
        }

        return userService.getUsersConverted(criteria, pageable);
    }

    /**
     *
     */
    @RequestMapping(value = "/user", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Object getUsers(@RequestParam Map<String, Object> criteria, Principal principal, Pageable pageable)
            throws Exception
    {
        return userService.getUsersConverted(criteria, pageable);
    }

    /**
     *
     */
    @RequestMapping(value = "/user/{id:\\d+}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<UserView> getById(@PathVariable("id") Long userId, Principal principal) throws Exception
    {
        UserDetails userInfo = getLoggedInUser(principal);

        List<User> users = userRepository.findById(userId);

        if (users.size() != 1) {
            throw new ResourceNotFoundException("Not authorized");
        }

        if (! users.get(0).equals(userInfo.getUser())) {
            userService.writeAuthorized(users.get(0));
        }

        return new ResponseEntity<UserView>(userService.getView(users.get(0), users.get(0).equals(userInfo.getUser())), HttpStatus.OK);
    }

    /**
     *
     */
    @RequestMapping(value = "/user/{id:\\d+}", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<UserView> updateUser(@PathVariable(value = "id") Long id, @RequestBody String json,
            Principal principal) throws Exception
    {
        UserDetailsImpl userDetails = getLoggedInUser(principal);

        User userData = messageConverter.getObjectMapper()
                .readValue(json, User.class);
        Map<String, String> attributes = messageConverter.getObjectMapper()
                .readValue(json, new TypeReference<HashMap<String, Object>>() {
                });

        User user = userService.updateUser(id, userData, attributes, userDetails, apiSession.getRole());

        return new ResponseEntity<UserView>(userService.getView(user), HttpStatus.OK);
    }

    /**
     *
     */
    @RequestMapping(value = "/user/{id:\\d+}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<String> deleteUser(@PathVariable("id") Long userId,
            @RequestParam(value = "reassignment", required = false) String reassignment, Principal principal)
            throws Exception
    {
        List<String> result = new ArrayList<String>(1);

        result.add(userService.deleteUser(userId, reassignment, getLoggedInUser(principal)));

        return result;
    }

    /**
     *
     */
    @RequestMapping(value = "/user", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<UserView> createUser(@RequestBody User user) throws Exception
    {
        return new ResponseEntity<UserView>(userService.getView(userService.createUser(user)), HttpStatus.OK);
    }

    /**
     *
     */
    @RequestMapping(value = "/user/admin", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<UserView> createUserAdmin(@RequestBody User user) throws Exception
    {
        userService.addAdmin(userService.createUser(user));

        return new ResponseEntity<UserView>(userService.getView(user), HttpStatus.OK);
    }

    /**
     *
     */
    @RequestMapping(value = "/user/freightcom_staff", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<UserView> createUserStaff(@RequestBody User user, Principal principal) throws Exception
    {
        userService.addFreightcomStaff(userService.createUser(user));

        return new ResponseEntity<UserView>(userService.getView(user), HttpStatus.OK);
    }

    /**
     *
     */
    @RequestMapping(value = "/user/agent", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Transactional
    public ResponseEntity<UserView> createUserAgent(@RequestBody String json) throws Exception
    {
        User user = messageConverter.getObjectMapper()
                .readValue(json, User.class);
        Agent agentRole = messageConverter.getObjectMapper()
                .readValue(json, Agent.class);
        Map<String, Object> attributes = messageConverter.getObjectMapper()
                .readValue(json, new TypeReference<HashMap<String, Object>>() {
                });

        userService.createUser(user, agentRole, attributes);

        return new ResponseEntity<UserView>(userService.getView(user), HttpStatus.OK);
    }

    /**
     *
     */
    @RequestMapping(value = "/user/customer_admin/{customerId}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<UserView> createUserCustomerAdmin(@PathVariable("customerId") Long customerId,
            @RequestBody User user, Principal principal) throws Exception
    {

        userService.checkCustomer(getLoggedInUser(principal), customerId);
        userService.addCustomerAdmin(userService.createUser(user), customerId);

        return new ResponseEntity<UserView>(userService.getView(user), HttpStatus.OK);
    }

    /**
     *
     */
    @RequestMapping(value = "/user/customer_staff/{customerId}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<UserView> createUserCustomerStaff(@PathVariable("customerId") Long customerId,
            @RequestBody User user, Principal principal) throws Exception
    {

        userService.checkCustomer(getLoggedInUser(principal), customerId);
        userService.addCustomerStaff(userService.createUser(user), customerId);

        return new ResponseEntity<UserView>(userService.getView(user), HttpStatus.OK);
    }

    /**
     *
     */
    @RequestMapping(value = "/logged-in-user", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<UserView> loggedInUser(Principal principal)
    {
        UserDetailsImpl userDetails = getLoggedInUser(principal);

        User user = userService.findUser(userDetails.getUser()
                .getId());

        return new ResponseEntity<UserView>(userService.getView(user), HttpStatus.OK);
    }

    /**
     *
     */
    @RequestMapping(value = "/customer-staff", method = RequestMethod.GET)
    @ResponseBody
    public PagedResources<Resource<UserView>> getCustomerStaff(
            @RequestParam(value = "customer_id", required = false) Long customerId,
            @RequestParam(value = "q", required = false) String query,
            @RequestParam(value = "firstname", required = false) String firstname,
            @RequestParam(value = "lastname", required = false) String lastname,
            @RequestParam(value = "login", required = false) String login,
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "email", required = false) String email, Principal principal,
            HttpServletResponse response, Pageable pageable) throws Exception
    {

        Map<String, Object> criteria = new HashMap<String, Object>();
        getLoggedInUser(principal);
        UserRole userRole = apiSession.getRole();

        if (userRole != null && userRole.isCustomer()) {
            if (customerId == null) {
                customerId = userRole.getCustomerId();
            } else if (!customerId.equals(userRole.getCustomerId())) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            }
        } else if (customerId == null) {
            ValidationException.get()
                    .add("customerId", "Need a customer id")
                    .doThrow();
        }

        criteria.put("customer_id", customerId);

        if (query != null) {
            criteria.put("query", query);
        }

        if (login != null) {
            criteria.put("login", login);
        }

        if (phone != null) {
            criteria.put("phone", phone);
        }

        if (firstname != null) {
            criteria.put("firstname", firstname);
        }

        if (email != null) {
            criteria.put("email", email);
        }

        if (lastname != null) {
            criteria.put("lastname", lastname);
        }

        return userService.getUsersConverted(criteria, pageable);
    }

    /**
     *
     */
    @RequestMapping(value = "/customer-staff/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<UserView> getCustomerStaffUser(@PathVariable(value = "id") Long id, Principal principal)
            throws Exception
    {
        getLoggedInUser(principal);
        User user = userRepository.findOne(id);

        if (user == null) {
            throw new ResourceNotFoundException("No such user");
        }

        return new ResponseEntity<UserView>(userService.getView(user), HttpStatus.OK);
    }

    /**
     *
     */
    @RequestMapping(value = "/customer-staff", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<UserView> createCustomerStaffUser(@RequestBody User user, Principal principal)
            throws Exception
    {
        return new ResponseEntity<UserView>(userService.getView(userService.createUser(user)), HttpStatus.OK);
    }

    /**
     *
     */
    @RequestMapping(value = "/customer-staff/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<UserView> updateCustomerStaff(@PathVariable(value = "id") Long id, @RequestBody String json,
            Principal principal) throws Exception
    {
        UserDetailsImpl loggedInuser = getLoggedInUser(principal);
        List<User> matches = userRepository.findById(id);

        if (matches.size() != 1) {
            throw new ResourceNotFoundException("Not authorized");
        }

        User user = matches.get(0);

        User userData = messageConverter.getObjectMapper()
                .readValue(json, User.class);
        Map<String, String> attributes = messageConverter.getObjectMapper()
                .readValue(json, new TypeReference<HashMap<String, Object>>() {
                });

        log.debug("UPDATE CUSTOMER STAFF " + user);
        log.debug(json);
        log.debug("X " + attributes);
        log.debug("Y " + userData);
        log.debug("Y " + loggedInuser);

        userService.updateUser(id, userData, attributes, loggedInuser, apiSession.getRole());

        return new ResponseEntity<UserView>(userService.getView(user), HttpStatus.OK);
    }

    /**
     * @throws Exception
     *
     */
    @RequestMapping(value = "/user/role/{id:\\d+}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<UserRoleView> getRole(@PathVariable(value = "id") Long id, Principal principal)
            throws Exception
    {
        UserRole role = userRoleRepository.findOne(id);

        return new ResponseEntity<UserRoleView>(UserRoleViewFactory.get(role), HttpStatus.OK);
    }

    /**
     * @throws Exception
     *
     */
    @RequestMapping(value = "/user/role/{id:\\d+}", method = RequestMethod.DELETE)
    @ResponseBody
    public String roleDelete(@PathVariable(value = "id") Long id,
            @RequestParam(value = "reassignment", required = false) String reassignment, Principal principal)
            throws Exception
    {
        userService.deleteUserRole(id, reassignment, getLoggedInUser(principal));
        return "[ \"ok\" ]";
    }

    /**
     * @throws Exception
     *
     */
    @RequestMapping(value = "/user/role/{userId}/{name:[a-zA-Z].+}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity<List<UserRoleView>> roleDelete(@PathVariable(value = "userId") Long userId,
            @PathVariable(value = "name") String name,
            @RequestParam(value = "customerId", required = false) Long customerId,
            @RequestParam(value = "reassignment", required = false) String reassignment, Principal principal)
            throws Exception
    {
        List<UserRoleView> deleted = userService
                .deleteNamedRole(userId, name, customerId, reassignment, getLoggedInUser(principal))
                .stream()
                .map((Function<? super UserRole, ? extends UserRoleView>) (UserRole role) -> {
                    try {
                        return UserRoleViewFactory.get(role);
                    } catch (Exception e) {
                        throw new Error("Not expected", e);
                    }
                })
                .collect(Collectors.toList());

        return new ResponseEntity<List<UserRoleView>>(deleted, HttpStatus.OK);
    }

    /**
     *
     */
    @RequestMapping(value = "/user/role/admin", method = { RequestMethod.PUT, RequestMethod.POST })
    @ResponseBody
    public ResponseEntity<UserView> roleAdmin(@RequestBody Admin admin, Principal principal) throws Exception
    {
        userService.saveAdmin(admin, getLoggedInUser(principal));

        return new ResponseEntity<UserView>(userService.getView(userService.roleUser(admin)), HttpStatus.OK);
    }

    /**
     *
     */
    @RequestMapping(value = "/user/role/agent", method = { RequestMethod.PUT, RequestMethod.POST })
    @ResponseBody
    public ResponseEntity<UserView> roleAgent(@RequestBody String json, Principal principal) throws Exception
    {
        Agent agent = messageConverter.getObjectMapper()
                .readValue(json, Agent.class);
        Map<String, Object> attributes = messageConverter.getObjectMapper()
                .readValue(json, new TypeReference<HashMap<String, Object>>() {
                });

        userService.saveAgent(agent, attributes, getLoggedInUser(principal));

        return new ResponseEntity<UserView>(userService.getView(userService.roleUser(agent)), HttpStatus.OK);
    }

    /**
     *
     */
    @RequestMapping(value = "/user/role/freightcom_staff", method = { RequestMethod.PUT, RequestMethod.POST })
    @ResponseBody
    public ResponseEntity<UserView> roleFreightcomStaff(@RequestBody FreightcomStaff freightcomStaff,
            Principal principal) throws Exception
    {
        log.debug("CREATE FREIGHTCOM STAFF " + freightcomStaff);

        userService.saveFreightcomStaff(freightcomStaff, getLoggedInUser(principal));

        return new ResponseEntity<UserView>(userService.getView(userService.roleUser(freightcomStaff)), HttpStatus.OK);
    }

    /**
     *
     */
    @RequestMapping(value = "/user/role/customer_staff", method = { RequestMethod.PUT, RequestMethod.POST })
    @ResponseBody
    public ResponseEntity<UserView> roleCustomerStaff(@RequestBody CustomerStaff customerStaff, Principal principal)
            throws Exception
    {
        userService.saveCustomerStaff(customerStaff, getLoggedInUser(principal));

        return new ResponseEntity<UserView>(userService.getView(userService.roleUser(customerStaff)), HttpStatus.OK);
    }

    /**
     *
     */
    @RequestMapping(value = "/user/role/customer_admin", method = { RequestMethod.PUT, RequestMethod.POST })
    @ResponseBody
    public ResponseEntity<UserView> roleCustomerAdmin(@RequestBody CustomerAdmin customerAdmin, Principal principal)
            throws Exception
    {
        userService.saveCustomerAdmin(customerAdmin, getLoggedInUser(principal));

        return new ResponseEntity<UserView>(userService.getView(userService.roleUser(customerAdmin)), HttpStatus.OK);
    }

}
