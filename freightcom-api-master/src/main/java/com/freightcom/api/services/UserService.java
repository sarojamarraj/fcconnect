package com.freightcom.api.services;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.ConfigurablePropertyAccessor;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MissingServletRequestParameterException;

import com.freightcom.api.ApiSession;
import com.freightcom.api.events.AgentDeletedEvent;
import com.freightcom.api.events.PasswordResetEvent;
import com.freightcom.api.events.RoleDeletedEvent;
import com.freightcom.api.events.SystemLogEvent;
import com.freightcom.api.model.Admin;
import com.freightcom.api.model.Agent;
import com.freightcom.api.model.Customer;
import com.freightcom.api.model.CustomerAdmin;
import com.freightcom.api.model.CustomerStaff;
import com.freightcom.api.model.FreightcomStaff;
import com.freightcom.api.model.User;
import com.freightcom.api.model.UserRole;
import com.freightcom.api.model.views.UserView;
import com.freightcom.api.repositories.ObjectBase;
import com.freightcom.api.repositories.UserRepository;
import com.freightcom.api.repositories.custom.CustomerRepository;
import com.freightcom.api.repositories.custom.UserRoleRepository;
import com.freightcom.api.repositories.custom.UserSpecification;
import com.freightcom.api.services.converters.UserConverter;

/**
 * @author bryan
 *
 */
/**
 * @author bryan
 *
 */
@Component
public class UserService extends ServicesCommon
{
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private SecureRandom random = new SecureRandom();

    private final Boolean roleLock = Boolean.TRUE;

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final CustomerRepository customerRepository;
    private final UserRoleService userRoleService;
    private final ApplicationEventPublisher publisher;
    private final PasswordEncoder passwordEncoder;
    private final ObjectBase objectBase;

    private final char[] characterMap = "abcdefghijklmnopqrstuv012345679_#*$!".toCharArray();
    private final PagedResourcesAssembler<User> pagedAssembler;
    private final PagedResourcesAssembler<UserView> userAssembler;

    /**
     *
     */
    @Autowired
    public UserService(final ApplicationEventPublisher publisher, final UserRepository userRepository,
            final UserRoleRepository userRoleRepository, final PasswordEncoder passwordEncoder,
            final UserRoleService userRoleService, final CustomerRepository customerRepository,
            final ObjectBase objectBase, final PagedResourcesAssembler<UserView> userAssembler,
            final ApiSession apiSession, final PagedResourcesAssembler<User> pagedAssembler)
    {
        super(apiSession);

        this.publisher = publisher;
        this.userRepository = userRepository;
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
        this.pagedAssembler = pagedAssembler;
        this.userRoleRepository = userRoleRepository;
        this.userAssembler = userAssembler;
        this.userRoleService = userRoleService;
        this.objectBase = objectBase;
    }

    public void noRoleCreateAuthorized(User user)
    {
        UserRole role = apiSession.getRole();

        if (role == null || (!role.isAdmin() && !role.isFreightcomStaff() && !role.isCustomerAdmin())) {
            throw new AccessDeniedException("Not authorized");
        }
    }

    public void writeAuthorized(User user)
    {
        UserRole role = apiSession.getRole();

        if (role == null || user == null || !user.canWrite(role)) {
            log.debug("FAILED USER WRITE " + role + " " + user);
            throw new AccessDeniedException("Not authorized");
        }
    }

    public void readAuthorized(User user)
    {
        UserRole role = apiSession.getRole();

        if (role == null || user == null || !user.canRead(apiSession.getRole())) {
            throw new AccessDeniedException("Not authorized");
        }
    }

    private Map<String, String> addError(Map<String, String> errors, String variable, String message)
    {
        if (errors == null) {
            errors = new HashMap<String, String>(2);
        }

        errors.put(variable, message);

        return errors;
    }

    private Map<String, String> checkLogin(User user, Map<String, String> errors)
    {
        if (user.getLogin() == null) {
            errors = addError(errors, "login", "Login required");
        } else {
            for (User matchingUser : userRepository.findByLogin(user.getLogin())) {
                if (!matchingUser.getId()
                        .equals(user.getId())) {
                    errors = addError(errors, "login", "Login must be unique");
                    break;
                }
            }
        }

        return errors;
    }

    private Map<String, String> checkEmail(User user, Map<String, String> errors)
    {
        if (user.getEmail() == null) {
            errors = addError(errors, "email", "Email required");
        } else if (!user.getEmail()
                .equals("noexist@Freightcom.com")) {
            for (User matchingUser : userRepository.findByEmail(user.getEmail())) {
                if (!matchingUser.getId()
                        .equals(user.getId())) {
                    errors = addError(errors, "email", "Email must be unique");
                    break;
                }
            }
        }

        return errors;
    }

    public void validateUser(User user) throws ValidationException
    {
        Map<String, String> errors = checkLogin(user, null);
        errors = checkEmail(user, errors);

        if (isEmpty(user.getFirstname())) {
            errors = addError(errors, "firstname", "Firstname required");
        }

        if (isEmpty(user.getLastname())) {
            errors = addError(errors, "lastname", "Lastname required");
        }

        if (errors != null) {
            throw new ValidationException(errors);
        }
    }

    @Transactional
    public User createOrUpdateUser(User user) throws Exception
    {
        if (user.getId() == null) {
            noRoleCreateAuthorized(user);
        } else {
            writeAuthorized(user);
        }

        validateUser(user);

        userRepository.save(user);

        log.debug("USER PASSWORD FIELD " + user.getPassword());

        return user;
    }

    /**
     * @param
     * @throws Exception
     *
     */
    public User createUser(final User user) throws Exception
    {
        return createUser(user, null, new HashMap<String, Object>());
    }

    /**
     * @param
     * @throws Exception
     *
     */
    public User createUser(final User user, final Map<String, Object> attributes) throws Exception
    {
        return createUser(user, null, attributes);
    }

    /**
     * @param
     * @throws Exception
     *
     */
    public User createUser(final User user, UserRole role, Map<String, Object> attributes) throws Exception
    {
        User newUser;

        log.debug("HAD AUTHORITIES NEW USER ");

        if (role != null) {
            if (attributes.containsKey("parentSalesAgentId") && role.asAgent() != null) {

                if (attributes.get("parentSalesAgentId") == null) {
                    role.asAgent()
                            .setParentSalesAgent(null);
                } else {
                    Agent parentAgent = objectBase.getAgent(attributes.get("parentSalesAgentId"));

                    if (parentAgent == null) {
                        throw new ResourceNotFoundException("No agent " + attributes.get("parentSalesAgentId"));
                    }

                    if (parentAgent.asAgent()
                            .isSubAgentOf(role.asAgent())) {
                        throw createValidationException("parentSalesAgentId", "Can't be subagent of descendant");
                    }

                    role.asAgent()
                            .setParentSalesAgent(parentAgent);
                }
            }

            role.setId(null);
            user.getAuthorities()
                    .add(role);
            writeAuthorized(user);
        } else {
            // New user has no roles, so can't do a writeAuthorized.
            noRoleCreateAuthorized(user);
        }

        if (user.getId() != null) {
            newUser = new User();
            BeanUtils.copyProperties(user, newUser);
            newUser.setId(null);
        } else {
            newUser = user;
        }

        Collection<UserRole> authorities = newUser.getAuthorities();
        newUser.setAuthorities(null);

        log.debug("HAD AUTHORITIES 1 " + authorities + (authorities == null ? -1 : authorities.size()));

        validateUser(newUser);

        User createdUser = createOrUpdateUser(newUser);

        log.debug("HAD AUTHORITIES 2 " + createdUser.getAuthorities() + " "
                + (createdUser.getAuthorities() == null ? -1 : createdUser.getAuthorities()
                        .size()));

        if (role != null) {
            role.setUser(createdUser);
            createdUser.setAuthorities(authorities);

            userRepository.save(createdUser);

            log.debug("HAD AUTHORITIES 3 " + createdUser.getAuthorities() + " " + createdUser.getAuthorities()
                    .size());
        }

        return createdUser;
    }

    /**
     *
     */
    @Transactional
    public User updateUser(Long id, User user, Map<String, String> attributes, UserDetails loggedInUser,
            UserRole userRole) throws Exception
    {
        User existing = userRepository.findOne(id);

        if (existing == null) {
            throw new ResourceNotFoundException("No such user");
        }

        writeAuthorized(existing);

        ConfigurablePropertyAccessor source = PropertyAccessorFactory.forDirectFieldAccess(user);
        ConfigurablePropertyAccessor dest = PropertyAccessorFactory.forDirectFieldAccess(existing);

        // Only copy attributes supplied in the JSON input, leave others alone
        for (String key : attributes.keySet()) {
            if (dest.isWritableProperty(key)) {
                dest.setPropertyValue(key, source.getPropertyValue(key));
            }
        }

        validateUser(existing);

        return createOrUpdateUser(existing);
    }

    /**
     *
     */
    public void resetPassword(User user)
    {
        String password = generatePassword();

        log.debug("RESET PASSWORD " + user + " " + password);
        user.setPassword(passwordEncoder.encode(password));
        user.setNewPasswordRequired(1);

        publisher.publishEvent(new PasswordResetEvent(user, password));
        publisher.publishEvent(new SystemLogEvent(user, null, "password reset", null));
    }

    /**
     *
     */
    private String generatePassword()
    {
        final StringBuilder password = new StringBuilder();
        final int n = 8 + random.nextInt(3);
        final int bound = characterMap.length;

        for (int i = 0; i < n; i++) {
            password.append(characterMap[random.nextInt(bound)]);
        }

        return password.toString();
    }

    /**
     *
     */
    public PagedResources<Resource<User>> getUsers(Map<String, Object> criteria, Pageable pageable)
    {
        UserRole role = apiSession.getRole();

        if (role == null || role.isCustomerStaff()) {
            throw new AccessDeniedException("Not authorized");
        }

        if (role.isAgent()) {
            criteria.put("parentSalesAgentId", role.getId());
        } else if (role.isCustomerAdmin()) {
            criteria.put("customer_id", role.getCustomerId());
        }

        Page<User> users = userRepository.findAll(new UserSpecification(criteria), pageable);

        return pagedAssembler.toResource(users);
    }

    /**
     *
     */
    public PagedResources<Resource<UserView>> getUsersConverted(Map<String, Object> criteria, Pageable pageable)
            throws Exception
    {
        UserRole role = apiSession.getRole();

        if (role == null || role.isCustomerStaff()) {
            throw new AccessDeniedException("Not authorized");
        }

        if (role.isAgent()) {
            criteria.put("parentSalesAgentId", role.getId());
        } else if (role.isCustomerAdmin()) {
            criteria.put("customer_id", role.getCustomerId());
        }

        Page<UserView> users = userRepository.findAll(new UserSpecification(criteria), pageable)
                .map(new UserConverter(apiSession.getRole()));

        return userAssembler.toResource(users, new Link("/user"));
    }

    /**
     * Logged in user is allowed to access user if role is admin, role is
     * customer admin and customer ids match or user is user
     */
    public void checkCustomer(UserDetails loggedInUser)
    {
        checkCustomer(loggedInUser, null);
    }

    /**
     * Logged in user is allowed to access user if role is admin, role is
     * customer admin and customer ids match or user is user
     */
    public void checkCustomer(UserDetails loggedInUser, Long customerId)
    {
        UserRole loggedInRole = apiSession.getRole();

        if (loggedInUser == null || loggedInRole == null) {
            throw new ResourceNotFoundException("Not authorized");
        } else {
            boolean ok = false;

            if (loggedInRole.isAdmin() || loggedInRole.isFreightcomStaff()) {
                ok = true;
            } else if (loggedInRole.isAgent()) {
                if (customerId != null) {
                    Customer customer = customerRepository.findOne(customerId);

                    ok = customer != null && customer.getSubAgentId()
                            .equals(loggedInRole.getId());
                }
            } else if (loggedInRole.isCustomerAdmin() && loggedInRole.getCustomerId() != null
                    && loggedInRole.getCustomerId()
                            .equals(customerId)) {
                ok = true;
            }

            if (!ok) {
                throw new AccessDeniedException("Not authorized");
            }
        }
    }

    /**
     *
     */
    public UserRole identifyRole(UserDetails loggedInuser, Long roleId)
    {
        UserRole role = userRoleRepository.findOne(roleId);

        if (role == null) {
            throw new ResourceNotFoundException("No such role");
        }

        if (!((UserDetailsImpl) loggedInuser).getId()
                .equals(role.getUserId())) {
            throw new AccessDeniedException("Not Role For User");
        }

        return role;
    }

    @Transactional
    public void deleteUserRole(Long roleId, String reassignment, UserDetailsImpl loggedInUser) throws Exception
    {
        synchronized (roleLock) {
            UserRole role = userRoleRepository.findOne(roleId);

            if (role == null) {
                throw new ResourceNotFoundException("No such role");
            }

            checkRoleAccess(role);

            User user = findUser(role.getUserId());
            Collection<UserRole> authorities = user.getAuthorities();

            log.debug("HIGH LEVEL DELETE ROLE " + role);

            triggerDeleteRole(user, role, role.getCustomerId(), reassignment, loggedInUser);

            authorities.remove(role);

            user.setAuthorities(authorities);
            userRepository.save(user);
        }
    }

    /**
     * Allowed: Customer admin if id matches Allowed: admin or freightcom staff
     * freightcom staff cannot delete admin
     *
     * agent - role must be for one of the agent's customers
     *
     */
    private void checkRoleAccess(UserRole role)
    {
        UserRole loggedInRole = apiSession.getRole();

        if (loggedInRole.isAgent()) {
            if (role.isCustomer() && role.getCustomerId() != null) {
                Customer customer = customerRepository.findOne(role.getCustomerId());

                if (customer != null && customer.getSubAgentId()
                        .equals(loggedInRole.getId())) {
                    // OK
                } else {
                    throw new AccessDeniedException("Not Role For User");
                }
            } else if (role.isAgent() && loggedInRole.getId()
                    .equals(role.getParentSalesAgentId())) {
                // ok
            } else {
                throw new AccessDeniedException("Not Role For User");
            }

        } else if (loggedInRole.isCustomerAdmin()) {
            if (loggedInRole.getCustomerId() == null || !loggedInRole.getCustomerId()
                    .equals(role.getCustomerId())) {
                throw new AccessDeniedException("Not Role For User");
            }
        } else if (loggedInRole.isFreightcomStaff()) {
            if (role.isAdmin()) {
                throw new AccessDeniedException("Not Role For User");
            }
        } else if (!loggedInRole.isAdmin()) {
            throw new AccessDeniedException("Not Role For User");
        }
    }

    /**
     * Delete the named role from the user. If customer role, must have customer
     * id.
     *
     * @throws Exception
     */
    @Transactional
    public List<UserRole> deleteNamedRole(Long userId, String roleName, Long customerId, String reassignment,
            UserDetailsImpl loggedInUser) throws Exception
    {
        User user = findUser(userId);
        boolean deleteCustomerRole = false;
        List<UserRole> deleted = new ArrayList<UserRole>();

        if (user == null) {
            throw new ResourceNotFoundException("No access to user");
        }

        if ((roleName.equalsIgnoreCase(UserRole.ROLE_CUSTOMER_ADMIN)
                || roleName.equalsIgnoreCase(UserRole.ROLE_CUSTOMER_STAFF))) {
            if (customerId == null) {
                throw new MissingServletRequestParameterException("Missing customer id", roleName);
            }

            deleteCustomerRole = true;
        }

        Collection<UserRole> authorities = user.getAuthorities();

        for (UserRole role : userRoleService.findByUserIdAndRoleName(userId, roleName)) {
            log.debug("DELETE ROLE " + role);

            if (deleteCustomerRole) {
                // Only delete customer roles if match
                log.debug("CHECKING " + customerId + " " + role.getCustomerId());

                if (customerId.equals(role.getCustomerId())) {
                    checkRoleAccess(role);
                    authorities.remove(role);
                    triggerDeleteRole(user, role, customerId, reassignment, loggedInUser);

                    log.debug("END DELETE ROLE ALERT");

                    deleted.add(role);
                }
            } else {
                checkRoleAccess(role);
                authorities.remove(role);
                triggerDeleteRole(user, role, customerId, reassignment, loggedInUser);
                deleted.add(role);
            }
        }

        log.debug("NEW AUTHORITIES " + authorities.size());

        user.setAuthorities(authorities);
        userRepository.save(user);

        return deleted;
    }

    /**
     * Trigger an event on role deletion
     *
     * @throws Exception
     *
     */
    public void triggerDeleteRole(User user, UserRole role, Long customerId, String reassignment,
            UserDetailsImpl loggedInUser) throws Exception
    {
        if (role.isAgent()) {
            log.debug("PUBLISH DELETED AGENT " + role + " R " + reassignment);
            if (reassignment != null) {
                UserRole reassignTo = userRoleRepository.findOne(Long.parseLong(reassignment));

                if (reassignTo == null || !reassignTo.isAgent()) {
                    ValidationException.get()
                            .add("reassignTo", "No such agent " + reassignment)
                            .doThrow();
                }
            }

            publisher.publishEvent(new AgentDeletedEvent(user, role, customerId, reassignment, loggedInUser));
        } else {
            log.debug("PUBLISH DELETED ROLE " + role);
            publisher.publishEvent(new RoleDeletedEvent(user, role, customerId, loggedInUser));
        }
    }

    /**
     * Find the user identified by the role
     *
     * @param customerStaff
     * @return User
     */
    public User roleUser(UserRole role)
    {
        List<User> matches = userRepository.findById(role.getUserId());

        if (matches.size() == 1) {
            return matches.get(0);
        } else {
            return null;
        }
    }

    @Transactional
    public void saveCustomerStaff(CustomerStaff customerStaff, UserDetailsImpl loggedInUser) throws Exception
    {
        confirmRoleUser(customerStaff);
        checkRoleAccess(customerStaff);
        customerStaff.getUser()
                .addAuthority(customerStaff);
        userRepository.save(customerStaff.getUser());
    }

    @Transactional
    public void saveAdmin(Admin admin, UserDetailsImpl loggedInUser) throws Exception
    {
        confirmRoleUser(admin);
        checkRoleAccess(admin);
        admin.getUser()
                .addAuthority(admin);
        userRepository.save(admin.getUser());

    }

    /**
     * @param agent
     * @param attributes
     * @param loggedInUser
     * @throws Exception
     */
    @Transactional
    public void saveAgent(Agent agent, Map<String, Object> attributes, UserDetailsImpl loggedInUser)
            throws Exception
    {
        confirmRoleUser(agent);
        checkRoleAccess(agent);

        log.debug("RUNNING SAVE AGENT " + agent);

        if (attributes.containsKey("parentSalesAgentId")) {
            log.debug("SAVE AGENT HAVE PARENT ID " + attributes.containsKey("parentSalesAgentId"));

            if (attributes.get("parentSalesAgentId") == null) {
                agent.setParentSalesAgent(null);
            } else {
                try {
                    Long parentId = Long.parseLong(attributes.get("parentSalesAgentId")
                            .toString());
                    UserRole parentAgent = userRoleRepository.findOne(parentId);

                    log.debug("SAVE AGENT FOUND parent " + parentAgent);

                    if (parentAgent == null) {
                        throw new ResourceNotFoundException("Invalid parentAgent");
                    }

                    if (parentAgent.asAgent()
                            .isSubAgentOf(agent)) {
                        throw createValidationException("parentSalesAgentId", "Can't be subagent of descendant");
                    }

                    agent.setParentSalesAgent(parentAgent.asAgent());
                } catch (NumberFormatException exception) {
                    throw createValidationException("parentSalesAgentId", "Invalid agent id");
                }
            }
        }

        agent.getUser()
                .addAuthority(agent);
        userRepository.save(agent.getUser());
    }

    /**
     * @param variable
     * @param message
     * @return
     */
    private ValidationException createValidationException(String variable, String message)
    {
        Map<String, String> errors = new HashMap<String, String>(1);
        errors.put("userId", "Missing user id");

        return new ValidationException(errors);
    }

    @Transactional
    public void saveCustomerAdmin(CustomerAdmin customerAdmin, UserDetailsImpl loggedInUser) throws Exception
    {
        confirmRoleUser(customerAdmin);
        checkRoleAccess(customerAdmin);
        customerAdmin.getUser()
                .addAuthority(customerAdmin);
        userRepository.save(customerAdmin.getUser());
    }

    @Transactional
    public void saveFreightcomStaff(FreightcomStaff freightcomStaff, UserDetailsImpl loggedInUser)
            throws Exception
    {
        // TODO Check permission for changing claim access and dispute access
        confirmRoleUser(freightcomStaff);
        checkRoleAccess(freightcomStaff);
        freightcomStaff.getUser()
                .addAuthority(freightcomStaff);
        userRepository.save(freightcomStaff.getUser());
    }

    protected void confirmRoleUser(UserRole role) throws ValidationException
    {
        if (role.getUserId() != null && role.getUser() == null) {
            role.setUser(findUser(role.getUserId()));
        }

        if (role.getUser() == null) {
            Map<String, String> errors = new HashMap<String, String>(1);
            errors.put("userId", "Missing user id");
            throw new ValidationException(errors);
        }
    }

    public List<User> findAdmins(int count)
    {
        Map<String, Object> criteria = new HashMap<String, Object>(1);

        criteria.put("role", "ADMIN");

        return userRepository.findAll(new UserSpecification(criteria), new PageRequest(0, count))
                .getContent();
    }

    @Transactional
    public String deleteUser(Long userId, String reassignment, UserDetailsImpl loggedInUser) throws Exception
    {
        UserRole loggedInRole = apiSession.getRole();

        if (loggedInRole == null) {
            throw new AccessDeniedException("Not authorized");
        }

        if (userId == null) {
            throw new ResourceNotFoundException("No user");
        }

        if (userId.equals(loggedInRole.getUserId())) {
            ValidationException.get()
                    .add("userId", "Not allowed to delete self")
                    .doThrow();
        }

        if (loggedInRole.isAgent() || loggedInRole.isCustomerAdmin()) {
            deleteNamedRole(userId, "CUSTOMER_STAFF", loggedInRole.getCustomerId(), null, loggedInUser);
            deleteNamedRole(userId, "CUSTOMER_ADMIN", loggedInRole.getCustomerId(), null, loggedInUser);
        } else if (loggedInRole.isAdmin() || loggedInRole.isFreightcomStaff()) {
            User user = findUser(userId);

            if (user == null) {
                throw new ResourceNotFoundException("No access to user");
            }

            if (user.hasAuthority(UserRole.ROLE_ADMIN)) {
                if (!loggedInRole.isAdmin()) {
                    throw new AccessDeniedException("Not authorized");
                }

                if (findAdmins(2).size() < 2) {
                    // Can't delete last admin
                    throw new AccessDeniedException("Can't delete last admin");
                }
            }

            for (UserRole role : user.getAuthorities()) {
                triggerDeleteRole(user, role, null, reassignment, loggedInUser);
            }

            userRepository.delete(user);
        }

        return "ok";
    }

    @Transactional
    public UserRole addAdmin(User user) throws Exception
    {
        synchronized (roleLock) {
            Admin admin = new Admin();
            admin.setUser(user);

            checkRoleAccess(admin);

            userRoleRepository.save(admin);
            user.addAuthority(admin);

            return admin;
        }
    }

    @Transactional
    public UserRole addCustomerAdmin(User user, Long customerId) throws Exception
    {
        synchronized (roleLock) {
            CustomerAdmin customerAdmin = new CustomerAdmin();
            customerAdmin.setUser(user);
            customerAdmin.setCustomerId(customerId);

            checkRoleAccess(customerAdmin);

            userRoleRepository.save(customerAdmin);
            user.addAuthority(customerAdmin);

            return customerAdmin;
        }

    }

    @Transactional
    public UserRole addCustomerStaff(User user, Long customerId) throws Exception
    {
        synchronized (roleLock) {
            CustomerStaff customerStaff = new CustomerStaff();
            customerStaff.setUser(user);
            customerStaff.setCustomerId(customerId);

            checkRoleAccess(customerStaff);

            userRoleRepository.save(customerStaff);
            user.addAuthority(customerStaff);

            return customerStaff;
        }
    }

    @Transactional
    public UserRole addFreightcomStaff(User user) throws Exception
    {
        synchronized (roleLock) {
            FreightcomStaff staff = new FreightcomStaff();
            staff.setUser(user);

            checkRoleAccess(staff);

            userRoleRepository.save(staff);
            user.addAuthority(staff);

            return staff;
        }
    }

    /**
     * Look up a user by id. Use instead of findOne because of mysterious crash
     * in user look up using findOne.
     */
    public User findUser(Long id)
    {
        User user = null;
        List<User> users = userRepository.findById(id);

        if (users.size() == 1) {
            user = users.get(0);
        }

        return user;
    }

    /**
    *
    */
    public UserView getView(User user)
    {
        return getView(user, false);
    }

    /**
     *
     */
    public UserView getView(User user, boolean isSelf)
    {
        return new UserView(user, apiSession.getRole(), isSelf);
    }

    @Transactional
    public void updateLastLogin(Long id)
    {
        User user = findUser(id);

        log.debug("UPDATING LAST LOGIN " + (new Date()));

        if (user != null) {
            user.setLastLogin(new Date());
        }
    }
}
