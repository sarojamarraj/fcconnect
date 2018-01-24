package com.freightcom.api.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.ConfigurablePropertyAccessor;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.freightcom.api.model.ApplicableTax;
import com.freightcom.api.events.CustomerInvitationEvent;
import com.freightcom.api.events.RoleDeletedEvent;
import com.freightcom.api.model.Agent;
import com.freightcom.api.model.Alert;
import com.freightcom.api.model.Customer;
import com.freightcom.api.model.CustomerAdmin;
import com.freightcom.api.model.Service;
import com.freightcom.api.model.User;
import com.freightcom.api.model.UserRole;
import com.freightcom.api.model.views.View;
import com.freightcom.api.repositories.ObjectBase;
import com.freightcom.api.repositories.custom.CustomerRepository;
import com.freightcom.api.repositories.custom.CustomerSpecification;
import com.freightcom.api.services.converters.CustomerConverter;
import com.freightcom.api.util.MapBuilder;

/**
 * @author bryan
 *
 */
@Component
public class CustomerServiceImpl extends ServicesCommon implements CustomerService
{
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final CustomerRepository customerRepository;

    private final PagedResourcesAssembler<View> pagedAssembler;
    private final PermissionChecker permissionChecker;
    private final ObjectBase objectBase;
    private final PasswordEncoder passwordEncoder;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public CustomerServiceImpl(final CustomerRepository customerRepository, final PasswordEncoder passwordEncoder,
            final PagedResourcesAssembler<View> pagedAssembler, final PermissionChecker permissionChecker,
            final ObjectBase objectBase)
    {
        super(permissionChecker.getApiSession());

        this.customerRepository = customerRepository;
        this.pagedAssembler = pagedAssembler;
        this.permissionChecker = permissionChecker;
        this.objectBase = objectBase;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public PagedResources<Resource<View>> getCustomers(Map<String, Object> criteria, Pageable pageable)
    {
        return getCustomersConverted(criteria, pageable);
    }

    @Override
    public PagedResources<Resource<View>> getCustomersConverted(Map<String, Object> criteria, Pageable pageable)
    {
        permissionChecker.checkCriteria(criteria);
        Page<View> customers = customerRepository.findAll(new CustomerSpecification(criteria), pageable)
                .map(new CustomerConverter());

        return pagedAssembler.toResource(customers, new Link("/customer"));
    }

    @Override
    public Customer createOrUpdateCustomer(Customer customer) throws Exception
    {
        if (customer.getAutoInvoice() == null) {
            customer.setAutoInvoice(null);
        }

        if (customer.countDefaultCards() > 1) {
            ValidationException.get()
                    .add("creditCards", "More than one default")
                    .doThrow();
        }

        if (customer.getSalesAgent() != null) {
            if (customer.getSalesAgent()
                    .getId() == null) {
                ValidationException.get()
                        .add("agentId", "Invalid sales agent")
                        .doThrow();
            }
        }

        if (customer.getPackagePreference() != null) {
            customer.setPackagePreference(objectBase.getPackagePreference(customer.getPackagePreference().getId()));
        }

        objectBase.save(customer);
        log.debug("SAVED CUSTOMER " + customer);

        return customer;
    }

    private String asString(Object value)
    {
        if (value == null) {
            return null;
        } else if (value instanceof String) {
            return (String) value;
        } else {
            return value.toString();
        }
    }

    public ValidationException validationCommon(Map<String, ?> attributes) throws ValidationException
    {
        ValidationException exception = null;

        if (attributes.containsKey("invoiceCurrency")) {
            if (attributes.get("invoiceCurrency") == null) {
                exception = ValidationException.get()
                        .add("invoiceCurrency", "cannot be null");
            } else if (objectBase.lookupCurrency(attributes.get("invoiceCurrency")
                    .toString()) == null) {
                // Invalid currency
                exception = ValidationException.get()
                        .add("invoiceCurrency", "invalid value");
            }
        }

        return exception;
    }

    @Override
    public void validateCustomerRequest(Map<String, Object> attributes) throws ValidationException
    {
        String[] required = new String[] { "login", "name", "password", "passwordConfirm", "contact", "email", "phone",
                "address", "city", "country", "province", "autoInvoice", "autoCharge", "invoiceTerm",
                "invoiceTermWarning", "invoiceCurrency", "invoiceEmail", "pastDueAction", "shippingPODRequired",
                "shippingNMFCRequired" };

        ValidationException exception = validationCommon(attributes);

        for (String field : required) {
            Object value = attributes.get(field);
            String message = null;

            if (field.equals("autoInvoice")) {
                try {
                    Customer.AutoInvoice.valueOf(asString(value));
                } catch (Exception e) {
                    message = "invalid value";
                }
            } else if (field.equals("autoCharge")) {
                try {
                    Customer.AutoCharge.valueOf(asString(value));
                } catch (Exception e) {
                    message = "invalid value";
                }
            } else if (field.equals("pastDueAction")) {
                try {
                    Customer.PastDueAction.valueOf(asString(value));
                } catch (Exception e) {
                    message = "invalid value";
                }
            } else if (value == null || asString(value).equals("")) {
                message = "is required";
            }

            if (message != null) {
                if (exception == null) {
                    exception = ValidationException.get();
                }

                exception.add(field, message);
            }
        }

        if (attributes.get("password") != null && !attributes.get("password")
                .equals(attributes.get("passwordConfirm"))) {

            if (exception == null) {
                exception = ValidationException.get();
            }

            exception.add("password", "passwords must match");
        }

        if (attributes.get("login") != null) {
            User matchingUser = objectBase.getUserByLogin(asString(attributes.get("login")));

            if (matchingUser != null) {
                if (exception == null) {
                    exception = ValidationException.get();
                }

                exception.add("login", "login must be unique");
            }
        }

        if (exception != null) {
            exception.doThrow();
        }
    }

    @Override
    public void validateCustomerOnly(Map<String, Object> attributes) throws ValidationException
    {
        String[] required = new String[] { "address", "city", "country", "province", "postalCode" };

        ValidationException exception = validationCommon(attributes);

        for (String field : required) {
            Object value = attributes.get(field);
            String message = null;

            if (field.equals("autoInvoice")) {
                try {
                    Customer.AutoInvoice.valueOf(asString(value));
                } catch (Exception e) {
                    message = "invalid value";
                }
            } else if (field.equals("autoCharge")) {
                try {
                    Customer.AutoCharge.valueOf(asString(value));
                } catch (Exception e) {
                    message = "invalid value";
                }
            } else if (field.equals("pastDueAction")) {
                try {
                    Customer.PastDueAction.valueOf(asString(value));
                } catch (Exception e) {
                    message = "invalid value";
                }
            } else if (value == null || asString(value).equals("")) {
                message = "is required";
            }

            if (message != null) {
                if (exception == null) {
                    exception = ValidationException.get();
                }

                exception.add(field, message);
            }
        }

        if (exception != null) {
            exception.doThrow();
        }
    }

    /**
     * @param attributes
     * @throws Exception
     *
     */
    @Override
    public Customer createCustomerOnly(final Customer customerData, Map<String, Object> attributes) throws Exception
    {
        permissionChecker.isAdmin();
        return createCustomer(customerData, attributes, false);
    }

    /**
     * @param attributes
     * @throws Exception
     *
     */
    @Override
    public Customer createCustomer(final Customer customerData, Map<String, Object> attributes) throws Exception
    {
        return createCustomer(customerData, attributes, true);
    }

    private void setExcludedServices(Customer customer, Object serviceIdList)
    {
        if (serviceIdList != null) {
            List<Service> excludedServices = new ArrayList<Service>();

            for (String id : serviceIdList.toString()
                    .split("\\s*,\\s*")) {
                Service service = objectBase.getService(id);

                if (service == null) {
                    throw new ResourceNotFoundException("Service not found: " + id);
                }

                excludedServices.add(service);
            }

            customer.setExcludedServices(excludedServices);
        }
    }

    /**
     * @param attributes
     * @throws Exception
     *
     */
    @Scope(proxyMode = ScopedProxyMode.INTERFACES)
    @Transactional
    @Override
    public Customer createCustomer(final Customer customerData, Map<String, Object> attributes, boolean validateStaff)
            throws Exception
    {
        if (!permissionChecker.isFreightcom() && !permissionChecker.isAgent()) {
            throw new AccessDeniedException("Not authorized");
        }

        log.debug("CREATE CUSTOMER DATA 1 " + customerData
                  + " " + customerData.getPackagePreference());

        Customer newCustomer;

        if (validateStaff) {
            validateCustomerRequest(attributes);
        } else {
            validateCustomerOnly(attributes);
        }

        if (customerData.getId() != null) {
            newCustomer = new Customer();
            BeanUtils.copyProperties(customerData, newCustomer);
            newCustomer.setId(null);
        } else {
            newCustomer = customerData;
        }

        setExcludedServices(newCustomer, attributes.get("excludedServiceIds"));

        log.debug("CREATE CUSTOMER DATA " + customerData + " new " + newCustomer + " args "
                + MapBuilder.toString(attributes)
                + " package preference " + customerData.getPackagePreference()
                + " new pp " + newCustomer.getPackagePreference());

        log.debug("CREATE CUSTOMER KEY active " + newCustomer.getActive());
        log.debug("CREATE CUS324TOMER KEY active at " + newCustomer.getActivatedAt());

        log.debug("CREATE CUSTOMER KEY suspended " + newCustomer.getSuspended());
        log.debug("CREATE CUSTOMER KEY suspended at " + newCustomer.getActivatedAt());

        if (permissionChecker.isAgent()) {
            newCustomer.setSalesAgent(permissionChecker.getRole()
                    .asAgent());
        }

        Customer customer = createOrUpdateCustomer(newCustomer);

        if (attributes.get("login") != null) {
            User adminUser = new User();

            adminUser.setLogin(asString(attributes.get("login")));
            adminUser.setEmail(asString(attributes.get("email")));
            adminUser.setPhone(asString(attributes.get("phone")));
            adminUser.setPassword(passwordEncoder.encode(asString(attributes.get("password"))));
            adminUser.setLastname(asString(attributes.get("contact")));

            if (attributes.get("enabled") == null || !attributes.get("enabled")
                    .equals("0")) {
                adminUser.setEnabled(true);
            }

            objectBase.save(adminUser);

            CustomerAdmin adminRole = new CustomerAdmin();

            adminRole.setUser(adminUser);
            adminRole.setCustomerId(customer.getId());

            objectBase.save(adminRole);
        }

        log.debug("CREATE CUSTOMER RESULT " + customer);

        return customer;
    }

    /**
     *
     */
    @Transactional
    @Override
    public Customer updateCustomer(Long id, Customer customer, Map<String, String> attributes, UserDetails loggedInUser,
            UserRole userRole) throws Exception
    {
        Customer existing = customerRepository.findOne(id);

        if (existing == null) {
            throw new ResourceNotFoundException("No such customer");
        }

        if (! existing.getVersion().equals(customer.getVersion())) {
            throw new ResourceNotFoundException("Customer updated by another user");
        }

        ValidationException exception = validationCommon(attributes);

        if (exception != null) {
            throw exception;
        }

        log.debug("UPDATE CUSTOMER DATA " + customer + " existing "
                + existing
                + " args " + MapBuilder.toString(attributes)
                + " package preference " + customer.getPackagePreference()
                + " existing pp " + existing.getPackagePreference());

        ConfigurablePropertyAccessor source = PropertyAccessorFactory.forDirectFieldAccess(customer);
        ConfigurablePropertyAccessor dest = PropertyAccessorFactory.forDirectFieldAccess(existing);

        // Only copy attributes supplied in the JSON input, leave others alone
        for (String key : attributes.keySet()) {

            if (key.equals("id")) {
                // skip
            } else if (key.equals("applicableTaxes")) {
                if (customer.getApplicableTaxes() != null) {
                    List<ApplicableTax> taxes = existing.getApplicableTaxes();
                    int taxCount = 0;

                    for (ApplicableTax tax: customer.getApplicableTaxes()) {
                        ApplicableTax useTax;

                        if (tax.getId() != null) {
                            useTax = objectBase.getApplicableTax(tax.getId());
                        } else {
                            useTax = tax;
                        }

                        useTax.setCustomerId(existing.getId());

                        if (taxCount >= taxes.size()) {
                            taxes.add(useTax);
                        } else {
                            taxes.set(taxCount, useTax);
                        }

                        taxCount++;
                    }
                }
            } else if (key.equals("active")) {
                log.debug("TOGGLING ACTIVE " + existing.getActive() + " in " + customer.getActive());

                Object value = attributes.get(key);

                Boolean set = value instanceof Boolean ? (Boolean) value : asString(value).equalsIgnoreCase("true");

                if (!existing.getActive()
                        .equals(set)) {
                    log.debug("TOGGLING ACTIVE");
                    existing.setActive(set);
                } else {
                    log.debug("NOT TOGGLING ACTIVE");
                }
            } else if (key.equals("suspended")) {
                log.debug("TOGGLING SUSPENDED " + existing.getSuspended() + " in " + customer.getSuspended());

                existing.setSuspended(customer.getSuspended());
            } else if (key.equals("salesAgent")) {
                if (customer.getSalesAgent() == null) {
                    existing.setSalesAgent(null);
                } else {
                    existing.setSalesAgent(objectBase.getAgent(customer.getSalesAgent()
                            .getId()));
                }
            } else if (key.equals("activatedAt") || key.equals("updatedAt") || key.equals("version")) {
                // no op
            } else if (key.equals("creditCards")) {
                existing.updateCreditCards(customer.getCreditCards());
            } else {
                if (dest.isWritableProperty(key)) {
                    dest.setPropertyValue(key, source.getPropertyValue(key));
                }

            }
        }

        setExcludedServices(existing, attributes.get("excludedServiceIds"));

        log.debug("CUSTOMER KEY active " + existing.getActive());
        log.debug("CUSTOMER KEY active at " + existing.getActivatedAt());
        log.debug("CUSTOMER KEY suspended " + existing.getSuspended());
        log.debug("CUSTOMER KEY suspended at " + existing.getSuspendedAt());
        log.debug("CUSTOMER SALES AGENT " + existing.getSalesAgent());

        return createOrUpdateCustomer(existing);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @Override
    public String deleteCustomer(Long customerId, UserDetailsImpl loggedInUser)
    {
        String result = "ok";
        Customer customer = customerRepository.findOne(customerId);

        if (customer == null) {
            throw new ResourceNotFoundException("No such customer " + customerId);
        }

        customerRepository.delete(customer);

        return result;
    }

    /**
     * @param alertService
     * @param event
     */
    @Transactional
    @Override
    public void alertRoleDeleted(AlertService alertService, RoleDeletedEvent event)
    {
        // 6b05c
        log.debug("CUSTOMER PROCESSING EVENT " + event);

        if (event.getRole()
                .getRoleName()
                .equals(UserRole.ROLE_CUSTOMER_STAFF) && event.getCustomerId() != null) {
            Customer customer = customerRepository.findOne(event.getCustomerId());

            if (customer != null) {
                Agent salesAgent = customer.getSalesAgent();

                log.debug("SALES AGENT " + salesAgent);

                if (salesAgent != null) {
                    Alert alert = new Alert();

                    alert.setMessage("Customer staff deleted");
                    alert.setUserId(salesAgent.getUserId());
                    alert.setObjectId(event.getRole()
                            .getId());
                    alert.setObjectName("CUSTOMER_STAFF");

                    alertService.save(alert);
                } else {
                    log.debug("NULL SALES AGENT FOR CUSTOMER " + customer + " " + customer.getSubAgentId());
                }
            } else {
                log.debug("NULL CUSTOMER");
            }
        }
    }

    @Override
    public Customer getCustomer(Long customerId)
    {
        Customer customer = customerRepository.findOne(customerId);

        permissionChecker.check(customer);

        return customer;
    }

    @Override
    public void inviteCustomer(Long customerId, String emailAddress) throws ValidationException
    {
        Customer customer = getCustomer(customerId);

        if (emailAddress == null || emailAddress.isEmpty()) {
            ValidationException.get()
                    .add("emailAddress", "required")
                    .doThrow();
        }

        publishEvent(new CustomerInvitationEvent(customer, emailAddress, getRole()));
    }

    @Override
    public PagedResources<Resource<View>> getByName(Map<String, Object> criteria, Pageable pageable)
    {
        if (permissionChecker.isAgent()) {
            criteria.put("agentId", permissionChecker.getRole().getId());
        } else if (permissionChecker.isCustomer()) {
            criteria.put("customerId", permissionChecker.getRole().getCustomerId());
        }

        return pagedAssembler.toResource(objectBase.findCustomerOptions(new CustomerSpecification(criteria), pageable));
    }
}
