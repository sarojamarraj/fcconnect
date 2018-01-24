package com.freightcom.api.services;

import static com.freightcom.api.model.support.OrderStatusCode.BOOKED;
import static com.freightcom.api.model.support.OrderStatusCode.CANCELLED;
import static com.freightcom.api.model.support.OrderStatusCode.DELETED;
import static com.freightcom.api.model.support.OrderStatusCode.DELIVERED;
import static com.freightcom.api.model.support.OrderStatusCode.DRAFT;
import static com.freightcom.api.model.support.OrderStatusCode.IN_TRANSIT;
import static com.freightcom.api.model.support.OrderStatusCode.QUOTED;
import static com.freightcom.api.model.support.OrderStatusCode.READY_FOR_SHIPPING;
import static com.freightcom.api.model.support.OrderStatusCode.SCHEDULED;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.ConfigurablePropertyAccessor;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.freightcom.api.ApiSession;
import com.freightcom.api.ReportableError;
import com.freightcom.api.carrier.CarrierServices;
import com.freightcom.api.carrier.RatingsService;
import com.freightcom.api.events.OrderBookedEvent;
import com.freightcom.api.events.OrderChargeChange;
import com.freightcom.api.events.OrderDeliveredEvent;
import com.freightcom.api.events.OrderStatusChange;
import com.freightcom.api.events.QuoteSendEvent;
import com.freightcom.api.model.AccessorialServices;
import com.freightcom.api.model.ApplicableTax;
import com.freightcom.api.model.Charge;
import com.freightcom.api.model.ChargeTax;
import com.freightcom.api.model.Claim;
import com.freightcom.api.model.Customer;
import com.freightcom.api.model.CustomerOrder;
import com.freightcom.api.model.CustomsInvoice;
import com.freightcom.api.model.DisputeInformation;
import com.freightcom.api.model.DistributionAddress;
import com.freightcom.api.model.DistributionGroup;
import com.freightcom.api.model.Invoice;
import com.freightcom.api.model.LoggedEvent;
import com.freightcom.api.model.MessageAction;
import com.freightcom.api.model.OrderAccessorials;
import com.freightcom.api.model.OrderDocument;
import com.freightcom.api.model.OrderRateQuote;
import com.freightcom.api.model.OrderStatus;
import com.freightcom.api.model.Package;
import com.freightcom.api.model.PackageType;
import com.freightcom.api.model.Pallet;
import com.freightcom.api.model.PalletTemplate;
import com.freightcom.api.model.PalletType;
import com.freightcom.api.model.Pickup;
import com.freightcom.api.model.SelectedQuote;
import com.freightcom.api.model.Service;
import com.freightcom.api.model.ShippingAddress;
import com.freightcom.api.model.TaxDefinition;
import com.freightcom.api.model.User;
import com.freightcom.api.model.UserRole;
import com.freightcom.api.model.support.OrderStatusCode;
import com.freightcom.api.model.views.ClaimOrderView;
import com.freightcom.api.model.views.DisputedOrderView;
import com.freightcom.api.model.views.DuplicateOrderView;
import com.freightcom.api.model.views.JobBoardView;
import com.freightcom.api.model.views.SubmittedOrderView;
import com.freightcom.api.model.views.View;
import com.freightcom.api.repositories.CustomerOrderSummaryRepository;
import com.freightcom.api.repositories.DistributionAddressRepository;
import com.freightcom.api.repositories.DistributionGroupRepository;
import com.freightcom.api.repositories.ObjectBase;
import com.freightcom.api.repositories.OrderAccessorialsRepository;
import com.freightcom.api.repositories.PackageRepository;
import com.freightcom.api.repositories.PackageTypeRepository;
import com.freightcom.api.repositories.PalletRepository;
import com.freightcom.api.repositories.SelectedQuoteRepository;
import com.freightcom.api.repositories.ShippingAddressRepository;
import com.freightcom.api.repositories.UserRepository;
import com.freightcom.api.repositories.custom.CarrierRepository;
import com.freightcom.api.repositories.custom.CustomerRepository;
import com.freightcom.api.repositories.custom.LoggedEventRepository;
import com.freightcom.api.repositories.custom.LoggedEventSpecification;
import com.freightcom.api.repositories.custom.OrderRateQuoteRepository;
import com.freightcom.api.repositories.custom.OrderRepository;
import com.freightcom.api.repositories.custom.OrderSpecification;
import com.freightcom.api.repositories.custom.OrderSpecification.Mode;
import com.freightcom.api.repositories.custom.OrderStatusRepository;
import com.freightcom.api.repositories.custom.UserSpecification;
import com.freightcom.api.services.converters.InvoiceConverter;
import com.freightcom.api.services.converters.LoggedEventConverter;
import com.freightcom.api.services.converters.OrderSummaryConverter;
import com.freightcom.api.services.dataobjects.PickupData;
import com.freightcom.api.services.orders.ClaimActions;
import com.freightcom.api.services.orders.Dispute;
import com.freightcom.api.services.orders.OrderLogic;
import com.freightcom.api.services.orders.RateRequestResult;
import com.freightcom.api.util.MapBuilder;
import com.freightcom.api.util.StringList;
import com.freightcom.api.util.YesValue;

/**
 * @author bryan
 *
 */
@Component
public class OrderServiceImpl implements OrderService
{
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final OrderRepository orderRepository;
    private final CustomerOrderSummaryRepository orderSummaryRepository;
    private final ShippingAddressRepository shippingAddressRepository;
    private final LoggedEventRepository loggedEventRepository;
    private final CustomerRepository customerRepository;
    private final PackageTypeRepository packageTypeRepository;
    private final UserRepository userRepository;

    private final DistributionGroupRepository distributionGroupRepository;
    private final DistributionAddressRepository distributionAddressRepository;
    private final OrderStatusRepository orderStatusRepository;
    private final OrderRateQuoteRepository orderRateQuoteRepository;

    private final PagedResourcesAssembler<View> assembler;
    private final ApiSession apiSession;
    private final ApplicationEventPublisher publisher;
    private final PermissionChecker permissionChecker;
    private final ObjectBase objectBase;
    private final DocumentManager documentManager;
    private final RatingsService ratingsService;
    private final CarrierServices carrierServices;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public OrderServiceImpl(final OrderRepository orderRepository,
            final ShippingAddressRepository shippingAddressRepository, final PackageRepository packageRepository,
            final PalletRepository palletRepository, final LoggedEventRepository loggedEventRepository,
            final OrderStatusRepository orderStatusRepository,
            final PagedResourcesAssembler<CustomerOrder> pagedAssembler, final PagedResourcesAssembler<View> assembler,
            final DistributionGroupRepository distributionGroupRepository,
            final PackageTypeRepository packageTypeRepository,
            final CustomerOrderSummaryRepository orderSummaryRepository,
            final DistributionAddressRepository distributionAddressRepository,
            final CustomerRepository customerRepository, final OrderAccessorialsRepository accessorialRepository,
            final SelectedQuoteRepository selectedQuoteRepository,
            final OrderRateQuoteRepository orderRateQuoteRepository, final CarrierRepository carrierRepository,
            final ObjectBase objectBase, final ApplicationEventPublisher publisher,
            final PermissionChecker permissionChecker, final ApiSession apiSession,
            final DocumentManager documentManager, final RatingsService ratingsService,
            final CarrierServices carrierServices, final UserRepository userRepository)
    {
        this.permissionChecker = permissionChecker;
        this.objectBase = objectBase;
        this.customerRepository = customerRepository;
        this.orderRepository = orderRepository;
        this.orderSummaryRepository = orderSummaryRepository;
        this.assembler = assembler;
        this.shippingAddressRepository = shippingAddressRepository;
        this.loggedEventRepository = loggedEventRepository;
        this.distributionAddressRepository = distributionAddressRepository;
        this.distributionGroupRepository = distributionGroupRepository;
        this.orderStatusRepository = orderStatusRepository;
        this.packageTypeRepository = packageTypeRepository;
        this.orderRateQuoteRepository = orderRateQuoteRepository;
        this.publisher = publisher;
        this.documentManager = documentManager;
        this.ratingsService = ratingsService;
        this.carrierServices = carrierServices;
        this.userRepository = userRepository;

        this.apiSession = apiSession;
    }

    public PagedResources<Resource<View>> getOrders(Map<String, Object> criteria, Pageable pageable)
    {
        return getOrders(criteria, pageable, OrderSpecification.Mode.ALL);
    }

    public PagedResources<Resource<View>> getOrders(Map<String, Object> criteria, Pageable pageable, Mode mode)
    {
        log.debug("GET ORDERS START");
        fixCriteria(criteria);

        List<CustomerOrder> queryResult = objectBase.orderQuery(new OrderSpecification(criteria), pageable);

        Page<View> orders = new PageImpl<View>(queryResult.stream()
                .limit(pageable.getPageSize())
                .map(order -> listView(order, criteria))
                .collect(Collectors.toList()), pageable, pageable.getOffset() + queryResult.size());

        log.debug("GET ORDERS END");

        return assembler.toResource(orders);
    }

    private View listView(CustomerOrder order, Map<String, Object> criteria)
    {
        if (!(criteria.get("mode") instanceof OrderSpecification.Mode)) {
            return new SubmittedOrderView(order, criteria, this);
        } else {
            switch ((OrderSpecification.Mode) criteria.get("mode")) {
            case JOB_BOARD:
                return new JobBoardView(order, criteria, this);

            case DISPUTED:
                return new DisputedOrderView(order, criteria, this);

            case HAS_CLAIM:
                return new ClaimOrderView(order, criteria, this);

            case SUBMITTED_ONLY:
            case ALL:
            case DRAFT_ONLY:
            default:
                return new SubmittedOrderView(order, criteria, this);
            }
        }
    }

    @Override
    public Object getOrderCounts(Map<String, Object> criteria)
    {
        log.debug("GET ORDERS START");
        fixCriteria(criteria);

        return objectBase.orderQueryCounts(new OrderSpecification(criteria));
    }

    public Page<View> getOrdersQuick(Map<String, Object> criteria, Pageable pageable)
    {
        return orderSummaryRepository.findAll(pageable)
                .map(new OrderSummaryConverter());
    }

    public PagedResources<Resource<View>> getCustomerOrders(Map<String, Object> criteria, Pageable pageable)
    {
        return getOrders(criteria, pageable);
    }

    protected void fixCriteria(Map<String, Object> criteria)
    {
        if (criteria.get("packageTypeName") != null) {
            PackageType packageType = packageTypeRepository.findByNameOrDescription(criteria.get("packageTypeName")
                    .toString(),
                    criteria.get("packageTypeName")
                            .toString());

            if (packageType != null) {
                criteria.put("packageTypeId", packageType.getId());
            }
        }
    }

    @Override
    public Object getStatusMessages(Long orderId, Map<String, Object> criteria, Pageable pageable) throws Exception
    {
        CustomerOrder order = getOrder(orderId);

        criteria.put("entity_id", order.getId());
        criteria.put("type", "order");
        criteria.put("restrictPrivate", apiSession.getUser()
                .getId());

        Page<View> loggedEvents = loggedEventRepository.findAll(new LoggedEventSpecification(criteria), pageable)
                .map(new LoggedEventConverter());

        return assembler.toResource(loggedEvents);
    }

    protected CustomerOrder setStatus(CustomerOrder order, OrderStatusCode status) throws Exception
    {
        return setStatus(order, status, "");
    }

    protected CustomerOrder setStatus(CustomerOrder order, OrderStatusCode status, String comment) throws Exception
    {
        OrderStatus oldStatus = order.getOrderStatus();
        OrderStatus newStatus = orderStatusRepository.findOne(status.getValue());

        if (newStatus == null) {
            throw new Exception("Missing status code in DB " + status);
        }

        order.setOrderStatus(newStatus);

        if (oldStatus != null && oldStatus.code() != status) {
            // Status changed
            publisher.publishEvent(new OrderStatusChange(order, oldStatus, newStatus, comment, getRole()));
        }

        return order;
    }

    @Transactional
    public CustomerOrder createOrUpdateOrder(CustomerOrder order) throws Exception
    {
        boolean saveAsDistributionList = false;

        if (order.getCustomerId() == null) {
            ValidationException.get()
                    .add("customerId", "MISSING CUSTOMER ID IN ORDER")
                    .doThrow();
        }

        PackageType packageType = packageTypeRepository.findByNameOrDescription(order.getPackageTypeName(),
                order.getPackageTypeName());

        if (packageType != null) {
            order.setPackageType(packageType);
        } else {
            packageType = order.getPackageType();
        }

        if (packageType == null) {
            throw new ValidationException(new HashMap<String, String>() {
                private static final long serialVersionUID = 1L;

                {
                    put("packageTypeName", "Missing package type");
                }
            });
        }

        if (order.getPackages() != null) {
            for (Package packageRecord : order.getPackages()) {
                log.debug("UPDATE OR CREATE PACKAGE " + packageRecord);
                packageRecord.setOrder(order);

                if (packageRecord.getId() != null && packageRecord.getId() == 0) {
                    packageRecord.setId(null);
                }
            }
        }

        if (order.getPallets() != null) {
            for (Pallet palletRecord : order.getPallets()) {
                palletRecord.setOrder(order);

                if (palletRecord.getId() != null && palletRecord.getId() == 0) {
                    palletRecord.setId(null);
                }
            }
        }

        if (order.getScheduledPickup() != null) {
            setConfirmation(order.getScheduledPickup());
        }

        if (order.getId() == null) {
            if (order.getShipFrom() != null && order.getShipFrom()
                    .getId() != null) {
                order.getShipFrom()
                        .setId(null);
                shippingAddressRepository.save(order.getShipFrom());
            }

            if (order.getShipTo() != null && order.getShipTo()
                    .getId() != null) {
                order.getShipTo()
                        .setId(null);
                shippingAddressRepository.save(order.getShipTo());
            }
        }

        if (order.getAccessorials() != null) {
            // Deserialize includes wrong type, exclude those
            List<OrderAccessorials> cleaned = new ArrayList<OrderAccessorials>();

            for (OrderAccessorials item : order.getAccessorials()) {
                if (item.getService() != null && item.getService()
                        .getType()
                        .equals(packageType.getName())) {
                    cleaned.add(item);
                    item.setOrder(order);
                }
            }

            order.setAccessorials(cleaned);
        }

        if (order.getSelectedQuote() != null && order.getSelectedQuote()
                .getServiceId() == null) {
            throw new ValidationException(new HashMap<String, String>() {
                /**
                 *
                 */
                private static final long serialVersionUID = 1L;

                {
                    put("selectedQuote.service.id", "Missing service");
                }
            });
        }

        orderRepository.save(order);

        order.setEshipperOid(order.getId());

        orderRepository.save(order);

        if (saveAsDistributionList) {
            saveAsDistribution(order);
        }

        if (order.getCustomer() == null) {
            order.setCustomer(customerRepository.findOne(order.getCustomerId()));
        }

        return order;
    }

    /**
     * @throws Exception
     *
     */
    public CustomerOrder createOrder(final CustomerOrder order) throws Exception
    {
        order.setId(null);

        log.debug("CREATE ORDER " + order.getCustomer() + " " + order.getCustomerId());

        if (order.getCustomer() == null && order.getCustomerId() == null) {
            log.debug("SET SESSION CUSTOMER " + apiSession.getRole() + " " + apiSession.getRole()
                    .getCustomerId());
            order.setCustomerId(apiSession.getRole()
                    .getCustomerId());
        }

        if (order.getCustomer() == null && order.getCustomerId() != null) {
            Customer customer = objectBase.getCustomer(order.getCustomerId());

            if (customer == null) {
                throw new ResourceNotFoundException("no customer");
            }

            order.setCustomer(customer);
        }

        if (order.getCustomer() == null) {
            permissionChecker.checkCustomer();
            Customer customer = objectBase.getCustomer(permissionChecker.getCustomerId());

            if (customer == null) {
                throw new ResourceNotFoundException("no customer");
            }

            order.setCustomer(customer);
        }

        if (!order.getCustomer()
                .isActive()
                || !order.getCustomer()
                        .getAllowNewOrders()) {
            log.debug("CUSTOMER AUTH FAILED " + order.getCustomer());
            log.debug("CUSTOMER AUTH FAILED ACTIVE" + order.getCustomer().isActive());
            log.debug("CUSTOMER AUTH FAILED ALLOWED " + order.getCustomer().getAllowNewOrders());
            throw new AccessDeniedException("Not authorized");
        }

        if (order.getPackages() != null) {
            for (Package item : order.getPackages()) {
                item.setId(null);
            }
        }

        if (order.getPallets() != null) {
            for (Pallet item : order.getPallets()) {
                item.setId(null);
            }
        }

        if (order.getCharges() != null && order.getCharges()
                .size() > 0) {
            ValidationException.get()
                    .add("charges", "not allowed")
                    .doThrow();
        }

        order.setAgent(objectBase.getAgent(order.getCustomer()
                .getSubAgentId()));

        CustomerOrder newOrder = createOrUpdateOrder(order);

        setStatus(newOrder, DRAFT);

        addStatusUpdate(newOrder, MapBuilder.getNew()
                .put("comment", "New draft order")
                .toMap());

        return newOrder;
    }

    public CustomerOrder getOrder(Long id, UserDetails loggedInUser)
    {
        CustomerOrder existing = orderRepository.findOne(id);

        if (existing == null) {
            throw new ResourceNotFoundException("No such order");
        }

        return existing;
    }

    private MapBuilder skipKeys = MapBuilder.getNew()
            .set("id", "1", "status", "1", "customerId", "1", "statusName", "1", "statusId", "1", "updatedAt", "1",
                    "createdAt", "1", "updatedBy", "1", "deletedAt", "1", "files", "1", "colorCode", "1");

    protected boolean skipKey(String key)
    {
        log.debug("SKIP KEY " + key + " = " + (skipKeys.get(key) == null ? "NO " : "YES"));
        return key == null || skipKeys.get(key) != null;
    }

    /**
     *
     */
    public CustomerOrder updateOrder(Long id, CustomerOrder order, Map<String, Object> attributes,
            UserDetails loggedInUser, UserRole userRole) throws Exception
    {
        try {
            synchronized (OrderLock.getLock(id)) {
                return updateOrderTransaction(id, order, attributes, loggedInUser, userRole);
            }
        } finally {
            log.debug("DONE SYNC UPDATE");
        }
    }

    /**
     *
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
    public CustomerOrder updateOrderTransaction(Long id, CustomerOrder order, Map<String, Object> attributes,
            UserDetails loggedInUser, UserRole userRole) throws Exception
    {
        CustomerOrder existing = orderRepository.findOne(id);

        checkOrder(userRole, existing);

        // WARNING! This uses field names, not getter and setter names (e.g.
        // weight)
        ConfigurablePropertyAccessor source = PropertyAccessorFactory.forDirectFieldAccess(order);
        ConfigurablePropertyAccessor dest = PropertyAccessorFactory.forDirectFieldAccess(existing);

        log.debug("UPDATE ORDER NEW " + order);
        log.debug("UPDATE ORDER EXISTING " + existing);

        // Only copy attributes supplied in the JSON input, leave others
        // alone
        for (String key : attributes.keySet()) {
            log.debug("UPDATE ORDER KEY " + key + " " + (attributes.get(key) == null ? "null" : "are"));

            if ("customsInvoice".equals(key)) {
                log.debug("EXISTING GET CUSTOMS INVOICE " + (existing.getCustomsInvoice()));
                if (order.getCustomsInvoice() == null) {
                    existing.setCustomsInvoice(null);
                } else if (existing.getCustomsInvoice() == null) {
                    existing.setCustomsInvoice(order.getCustomsInvoice());
                } else {
                    existing.getCustomsInvoice()
                            .patch(order.getCustomsInvoice(), (Map<?, ?>) attributes.get(key));
                }
            } else if ("weight".equals(key)) {
                existing.setWeight(order.getWeight());
            } else if ("packages".equals(key)) {
                existing.updatePackages(order.getPackages());
            } else if ("pallets".equals(key)) {
                existing.updatePallets(order.getPallets());
            } else if ("accessorialServices".equals(key)) {
                updateAccessorials(existing, order.getAccessorials());
            } else if ("charges".equals(key)) {
                ValidationException.get()
                        .add("charges", "not allowed")
                        .doThrow();
            } else if (!skipKey(key) && dest.isWritableProperty(key)) {
                log.debug("SET ORDER KEY " + key + source.getPropertyValue(key));
                dest.setPropertyValue(key, source.getPropertyValue(key));
            }
        }

        if (order.getCharges() != null && order.getCharges()
                .size() > 0) {
            ValidationException.get()
                    .add("charges", "not allowed")
                    .doThrow();
        }

        // Update clears rates
        existing.setQuotedAt(null);

        log.debug("UPDATE ORDER UPDATED " + existing);
        log.debug("UPDATE ORDER UPDATED " + existing.getWeight());

        return createOrUpdateOrder(existing);
    }

    private void updateAccessorials(CustomerOrder existing, List<OrderAccessorials> accessorials)
    {
        List<OrderAccessorials> newList = new ArrayList<OrderAccessorials>();
        List<OrderAccessorials> existingList = existing.getAccessorials();

        if (accessorials == null) {
            existing.setAccessorials(null);
        } else if (existingList == null) {
            existing.setAccessorials(accessorials);
        } else {
            for (OrderAccessorials item : existingList) {
                log.debug("UPDATE ACC EXISTING ACC " + item);
                if (accessorials.stream()
                        .anyMatch((candidate) -> item.getAccessorialId()
                                .equals(candidate.getAccessorialId()))) {
                    log.debug("UPDATE ACC MATCH " + item);
                    newList.add(item);
                }
            }

            for (OrderAccessorials item : accessorials) {
                log.debug("UPDATE ACC CHECKING FOR NEW " + item);

                if (!existingList.stream()
                        .anyMatch((candidate) -> item.getAccessorialId()
                                .equals(candidate.getAccessorialId()))) {
                    log.debug("UPDATE ACC ADDING " + item);
                    newList.add(item);
                }
            }

            existing.setAccessorials(newList);
        }
    }

    /**
     * Duplicate the order according to the distribution list
     *
     * @param order
     */
    private void saveAsDistribution(CustomerOrder order)
    {
        int count = 0;
        Long groupId = order.getDistributionGroupId();

        log.debug("SAVE AS DISTRIBUTION " + groupId + " " + order);

        List<DistributionAddress> addresses = distributionAddressRepository.findByDistributionGroupId(groupId);

        log.debug("FOUND ADDRESSES " + addresses.size());

        for (DistributionAddress address : addresses) {
            log.debug("SAVE AS DISTRIBUTION LOOP " + count);

            if (order.getReferenceCode() == null) {
                order.setReferenceCode("DL-" + groupId);
            }

            entityManager.detach(order);

            if (count > 0) {
                order.setId(null);
            }

            order.setShipTo(address.getAddress());

            orderRepository.save(order);
            count += 1;
        }

    }

    public Page<LoggedEvent> loggedEvents(Long orderId, UserDetailsImpl user, Pageable pageable)
    {
        // checkOrder(orderRepository.findOne(orderId));

        return loggedEventRepository.findByEntityIdAndEntityType(orderId.toString(), "order", pageable);
    }

    /**
     *
     */
    private CustomerOrder checkOrder(UserRole userRole, CustomerOrder order)
    {
        log.debug("CHECK ORDER " + userRole);

        if (userRole == null || order == null) {
            throw new ResourceNotFoundException("Not authorized");
        } else if (!userRole.isAdmin() && !userRole.isFreightcomStaff()
                && (order.getCustomerId() == null || !order.getCustomerId()
                        .equals(userRole.getCustomerId()))
                && !userRole.isAgentFor(order.getCustomer())) {
            throw new AccessDeniedException(
                    "Not authorized " + order.getCustomerId() + " C " + userRole.getCustomerId());
        }

        return order;
    }

    private CustomerOrder checkOrder(CustomerOrder order)
    {
        return checkOrder(apiSession.getRole(), order);
    }

    public Map<String, Object> storeNoOrder(MultipartFile file, UserDetails loggedInUser, UserRole userRole)
            throws Exception
    {
        return storeDistributionList(null, file, loggedInUser, userRole);
    }

    public Map<String, Object> storeDistributionList(Long orderId, MultipartFile file, UserDetails loggedInUser,
            UserRole userRole) throws Exception
    {
        Map<String, Object> messages = new HashMap<String, Object>();

        if (orderId != null) {
            CustomerOrder order = orderRepository.findOne(orderId);
            checkOrder(userRole, order);
        }

        InputStream stream = file.getInputStream();

        CsvSchema csvSchema = CsvSchema.emptySchema()
                .withHeader();
        ObjectReader mapper = new CsvMapper().readerFor(Map.class)
                .with(csvSchema);

        MappingIterator<Map<String, String>> iterator = mapper.readValues(new InputStreamReader(stream));

        int count = 0;
        int processed = 0;
        int errors = 0;

        final DistributionGroup group = new DistributionGroup();

        group.setOrderId(orderId);
        distributionGroupRepository.save(group);

        while (iterator.hasNext()) {
            Map<String, String> object = iterator.next();

            count += 1;

            final DistributionAddress distributionAddress = new DistributionAddress();
            distributionAddress.setDistributionGroupId(group.getId());
            ShippingAddress address = new ShippingAddress();

            ConfigurablePropertyAccessor dest = PropertyAccessorFactory.forDirectFieldAccess(address);
            List<String> required = new ArrayList<String>();
            required.add("city");
            required.add("province");
            Map<String, Boolean> seen = new HashMap<String, Boolean>();

            for (String key : object.keySet()) {
                String value = object.get(key);

                log.debug("UPLOAD COLUMN NAME " + key + "='" + value + "'" + " " + required.contains(key));
                if (required.contains(key) && (value == null || value.matches("^\\s*$"))) {
                    distributionAddress.setHasError(true);
                } else if (dest.isWritableProperty(key)) {
                    seen.put(key, true);
                    dest.setPropertyValue(key, object.get(key));
                } else {
                    ValidationException.get()
                            .add(key, "Invalid column name")
                            .doThrow();
                }
            }

            for (String requiredKey : required) {
                if (!seen.containsKey(requiredKey)) {
                    distributionAddress.setHasError(true);
                }
            }

            shippingAddressRepository.save(address);
            distributionAddress.setShiptoId(address.getId());

            log.debug("ITERATOR NEXT " + object);

            distributionAddressRepository.save(distributionAddress);

            if (distributionAddress.getHasError()) {
                errors += 1;
            } else {
                processed += 1;
            }
        }

        messages.put("totalEntries", new Integer(count));
        messages.put("processed", new Integer(processed));
        messages.put("errors", new Integer(errors));
        messages.put("id", group.getId());

        return messages;
    }

    /**
     * @throws Exception
     *
     */
    @Transactional
    @Override
    public String cancelOrder(Long orderId) throws Exception
    {
        CustomerOrder order = orderRepository.findOne(orderId);
        checkOrder(order);

        if (order.isCancelled()) {
            ValidationException.get()
                    .add("status", "Order already cancelled")
                    .doThrow();
        } else if (!order.getCancellable()) {
            ValidationException.get()
                    .add("status", "Order cannot be cancelled")
                    .doThrow();
        }

        if (order.hasBilledCharges()) {
            ValidationException.get()
                    .add("_",
                            "Some charges on this Order have already been invoiced. These invoices need to be canceled first.")
                    .doThrow();
        }

        setStatus(order, CANCELLED);
        order.clearCharges();

        return "ok";
    }

    /**
     * @throws Exception
     *
     */
    @Transactional
    @Override
    public String deleteOrder(Long orderId, UserDetails loggedInUser) throws Exception
    {
        setStatus(getOrder(orderId), DELETED);

        return "ok";
    }

    /**
     *
     */
    @Transactional
    @PreAuthorize("hasAuthority('ADMIN')")
    public String realDeleteOrder(Long orderId, UserDetails loggedInUser)
    {
        orderRepository.delete(getOrder(orderId));

        return "ok";
    }

    @Override
    public CustomerOrder bookOrder(Long orderId, CustomerOrder orderData, Map<String, Object> attributes,
            UserDetailsImpl loggedInUser) throws Exception
    {
        try {
            synchronized (OrderLock.getLock(orderId)) {
                return bookOrderTransaction(orderId, orderData, attributes, loggedInUser);
            }
        } finally {
            log.debug("DONE SYNC BOOK");
        }
    }

    @Transactional
    public CustomerOrder bookOrderTransaction(Long orderId, CustomerOrder orderData, Map<String, Object> attributes,
            UserDetailsImpl loggedInUser) throws Exception
    {
        CustomerOrder order = getOrder(orderId);

        if (!order.isQuoted()) {
            ValidationException.get()
                    .add("status", "Order has invalid status: " + order.getStatusCode())
                    .doThrow();
        }

        log.debug("ORDER DATA " + orderData);
        log.debug("BOOK " + (order == orderData) + " " + order);

        // Allow scheduledPickup and customsInvoice updates
        Map<String, Object> allowedAttributes = new HashMap<String, Object>(2);
        boolean update = false;

        if (attributes.containsKey("scheduledPickup")) {
            allowedAttributes.put("scheduledPickup", attributes.get("scheduledPickup"));
            update = true;
        }

        if (attributes.containsKey("customsInvoice")) {
            allowedAttributes.put("customsInvoice", attributes.get("customsInvoice"));
            update = true;
        }

        if (update) {
            updateOrderTransaction(order.getId(), orderData, allowedAttributes, null, apiSession.getRole());
        }

        if (order.hasScheduledPickupDate()) {
            setStatus(order, SCHEDULED);
        } else {
            setStatus(order, BOOKED);
        }

        OrderLogic.get(order, this)
                .possiblySaveToAddressBook();

        if (carrierServices.hasImplementation(order)) {
            carrierServices.bookOrder(order);
        } else {
            order.defaultAccessorialCharges();
        }

        publisher.publishEvent(new OrderBookedEvent(order, loggedInUser.getUser()));

        log.debug("DONE BOOK ORDER " + order + " " + order.getCharges()
                .size());

        return order;
    }

    @Override
    @Transactional
    public CustomsInvoice updateCustomsForm(Long orderId, CustomsInvoice invoiceData, Map<String, Object> attributes)
            throws Exception
    {
        CustomerOrder order = checkOrder(getOrder(orderId));

        if (order.getCustomsInvoice() == null) {
            order.setCustomsInvoice(invoiceData);
        } else {
            order.getCustomsInvoice()
                    .patch(invoiceData, attributes);
        }

        return order.getCustomsInvoice();
    }

    @Override
    @Transactional
    public CustomerOrder schedulePickup(Long orderId, CustomerOrder data) throws Exception
    {
        CustomerOrder order = checkOrder(getOrder(orderId));

        validatePickup(data.getScheduledPickup());

        if (!order.isSchedulable()) {
            ValidationException.get()
                    .add("status", "Order is already picked up")
                    .doThrow();
        }

        order.setScheduledPickup(data.getScheduledPickup());

        return order;
    }

    @Override
    @Transactional
    public CustomerOrder savePickupAndCustoms(Long orderId, CustomerOrder data) throws Exception
    {
        CustomerOrder order = checkOrder(getOrder(orderId));

        validatePickup(data.getScheduledPickup());

        if (!order.isSchedulable()) {
            ValidationException.get()
                    .add("status", "Order is already picked up")
                    .doThrow();
        }

        order.setScheduledPickup(data.getScheduledPickup());
        order.setCustomsInvoice(data.getCustomsInvoice());

        return order;
    }

    private void validatePickup(Pickup pickup) throws Exception
    {
        if (pickup == null) {
            ValidationException.get()
                    .add("scheduledPickup", "Scheduled pickup must be supplied")
                    .doThrow();
        }

        if (pickup.getPickupDate() == null) {
            ValidationException.get()
                    .add("scheduledPickup", "Pickup date many not be null")
                    .doThrow();
        }

        if (pickup.getPickupDate()
                .isBefore(ZonedDateTime.now(ZoneId.of("UTC")))) {
            ValidationException.get()
                    .add("scheduledPickup", "Cannot schedule pickup before now")
                    .doThrow();
        }
    }

    @Override
    @Transactional
    public CustomerOrder sendQuote(Long orderId) throws Exception
    {
        CustomerOrder order = getOrder(orderId);

        publisher.publishEvent(new QuoteSendEvent(order, permissionChecker.getUser()));

        return order;
    }

    @Override
    public CustomerOrder selectRate(Long orderId, Long rateId, CustomerOrder orderData, Map<String, Object> attributes,
            UserDetailsImpl loggedInUser) throws Exception
    {
        CustomerOrder order = getOrder(orderId);
        OrderRateQuote quote = orderRateQuoteRepository.findOne(rateId);

        if (quote == null) {
            throw new ResourceNotFoundException("Missing quote");
        }

        if (quote.getOrder() != order) {
            throw new ResourceNotFoundException("Quote not for order");
        }

        SelectedQuote selectedQuote = new SelectedQuote(quote);

        order.setSelectedQuote(selectedQuote);

        ConfigurablePropertyAccessor source = PropertyAccessorFactory.forDirectFieldAccess(orderData);
        ConfigurablePropertyAccessor dest = PropertyAccessorFactory.forDirectFieldAccess(order);

        // Only copy attributes supplied in the JSON input, leave others alone
        for (String key : attributes.keySet()) {
            log.debug("UPDATE ORDER KEY " + key + " " + (attributes.get(key) == null ? "null" : "are"));

            if ("accessorialServices".equals(key)) {
                updateAccessorials(order, orderData.getAccessorials());
            } else if (invalidSelectRateUpdate(key, order, orderData, attributes)) {
                ValidationException.get()
                        .add(key, "Update forbidden")
                        .doThrow();
            } else if (!skipKey(key) && dest.isWritableProperty(key)) {
                dest.setPropertyValue(key, source.getPropertyValue(key));
            }
        }

        createOrUpdateOrder(order);
        setStatus(order, QUOTED);

        return order;
    }

    private boolean invalidSelectRateUpdate(String key, CustomerOrder order, CustomerOrder newData,
            Map<String, Object> attributes)
    {
        boolean invalid = false;

        switch (key) {
        case "selectedQuote":
            invalid = true;
            break;

        case "accessorialServiceNames":
            try {
                invalid = StringList.different(order.getAccessorialServices(), StringList.convert(attributes.get(key)));
            } catch (Exception e) {
                invalid = true;
            }
            break;

        case "packages":
            invalid = StringList.different(order.getPackages(), newData.getPackages());
            break;

        case "pallets":
            invalid = StringList.different(order.getPallets(), newData.getPallets());
            break;

        case "packageTypeName":
            invalid = !Objects.equals(order.getPackageTypeName(), attributes.get(key));
            break;

        case "shipDate":
            log.debug("SHIP DATE COMPARISON " + order.getShipDate() + " " + order.getShipDate()
                    .getClass() + "\new " + newData.getShipDate() + " "
                    + newData.getShipDate()
                            .getClass());
            invalid = !Objects.equals(order.getShipDate(), newData.getShipDate());
            break;

        case "shipTo":
            invalid = !Objects.equals(order.getShipToPostalCode(), newData.getShipToPostalCode());
            break;

        case "shipFrom":
            invalid = !Objects.equals(order.getShipFromPostalCode(), newData.getShipFromPostalCode());
            break;

        default:
            break;
        }

        return invalid;
    }

    private CustomerOrder getOrder(Long orderId)
    {
        return checkOrder(orderRepository.findOne(orderId));
    }

    public List<Map<String, String>> getByMonth(Date from, Date to, Long customerId, Long agentId)
    {
        UserRole role = apiSession.getRole();
        List<Map<String, String>> data = null;

        if (role == null) {
            throw new AccessDeniedException("Not authorized");
        }

        if (from == null) {
            from = Date.from(ZonedDateTime.of(1969, 1, 1, 1, 1, 1, 1, ZoneId.of("UTC"))
                    .toInstant());
        }

        if (to == null) {
            to = Date.from(ZonedDateTime.now(ZoneId.of("UTC"))
                    .plusDays(1)
                    .toInstant());
        }

        if (role.isAdmin() || role.isFreightcomStaff()) {
            if (customerId != null) {
                if (agentId != null) {
                    data = orderRepository.monthHistogramAgentCustomer(from, to, agentId, customerId);
                } else {
                    data = orderRepository.monthHistogramCustomer(from, to, customerId);
                }
            } else if (agentId != null) {
                data = orderRepository.monthHistogramAgent(from, to, agentId);
            } else {
                data = orderRepository.monthHistogram(from, to);
            }
        } else if (role.isCustomer()) {
            if (customerId == null) {
                customerId = role.getCustomerId();
            } else if (!customerId.equals(role.getCustomerId())) {
                throw new AccessDeniedException("Not authorized");
            }

            data = orderRepository.monthHistogramCustomer(from, to, role.getCustomerId());
        } else if (role.isAgent()) {
            if (agentId == null) {
                agentId = role.asAgent()
                        .getId();
            } else if (!agentId.equals(role.asAgent()
                    .getId())) {
                throw new AccessDeniedException("Not authorized");
            }

            if (customerId != null) {
                data = orderRepository.monthHistogramAgentCustomer(from, to, role.getId(), customerId);
            } else {
                data = orderRepository.monthHistogramAgent(from, to, role.getId());
            }
        }

        if (data == null) {
            throw new AccessDeniedException("Not authorized");
        }

        ZonedDateTime datePointer = ZonedDateTime.ofInstant(from.toInstant(), ZoneId.of("UTC"))
                .with(TemporalAdjusters.firstDayOfMonth());

        ZonedDateTime lastDate = ZonedDateTime.ofInstant(to.toInstant(), ZoneId.of("UTC"))
                .with(TemporalAdjusters.lastDayOfMonth());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");

        List<Map<String, String>> result = new ArrayList<Map<String, String>>(data.size());
        int index = 0;

        while (datePointer.compareTo(lastDate) <= 0) {
            String key = datePointer.format(formatter);

            while (index < data.size() && key.compareTo(data.get(index)
                    .get("label")) > 0) {
                index += 1;
            }

            if (index >= data.size() || !key.equals(data.get(index)
                    .get("label"))) {
                Map<String, String> item = new HashMap<String, String>();
                item.put("label", key);
                item.put("count", "0");
                result.add(item);
            } else {
                result.add(data.get(index));
                index += 1;
            }

            datePointer = datePointer.plusMonths(1);
        }

        return result;
    }

    public List<Map<String, String>> getByWeek(Date from, Date to, Long customerId, Long agentId)
    {
        List<Map<String, String>> data = null;
        UserRole role = apiSession.getRole();

        if (role == null) {
            throw new AccessDeniedException("Not authorized");
        }

        if (from == null) {
            from = Date.from(ZonedDateTime.of(1969, 1, 1, 1, 1, 1, 1, ZoneId.of("UTC"))
                    .toInstant());
        }

        if (to == null) {
            to = Date.from(ZonedDateTime.now(ZoneId.of("UTC"))
                    .plusDays(1)
                    .toInstant());
        }

        if (role.isAdmin() || role.isFreightcomStaff()) {
            if (customerId != null) {
                if (agentId != null) {
                    data = orderRepository.weekHistogramAgentCustomer(from, to, agentId, customerId);
                } else {
                    data = orderRepository.weekHistogramCustomer(from, to, customerId);
                }
            } else if (agentId != null) {
                data = orderRepository.weekHistogramAgent(from, to, agentId);
            } else {
                data = orderRepository.weekHistogram(from, to);
            }
        } else if (role.isCustomer()) {
            if (customerId == null) {
                customerId = role.getCustomerId();
            } else if (!customerId.equals(role.getCustomerId())) {
                throw new AccessDeniedException("Not authorized");
            }

            data = orderRepository.weekHistogramCustomer(from, to, role.getCustomerId());
        } else if (role.isAgent()) {
            if (agentId == null) {
                agentId = role.asAgent()
                        .getId();
            } else if (!agentId.equals(role.asAgent()
                    .getId())) {
                throw new AccessDeniedException("Not authorized");
            }

            if (customerId != null) {
                data = orderRepository.weekHistogramAgentCustomer(from, to, role.getId(), customerId);
            } else {
                data = orderRepository.weekHistogramAgent(from, to, role.getId());
            }
        }

        if (data == null) {
            throw new AccessDeniedException("Not authorized");
        }

        ZonedDateTime datePointer = ZonedDateTime.ofInstant(from.toInstant(), ZoneId.of("UTC"));

        ZonedDateTime lastDate = ZonedDateTime.ofInstant(to.toInstant(), ZoneId.of("UTC"));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("YYYY-ww");

        List<Map<String, String>> result = new ArrayList<Map<String, String>>(data.size());
        int index = 0;

        while (datePointer.compareTo(lastDate) <= 0) {
            String key = datePointer.format(formatter);

            while (index < data.size() && key.compareTo(data.get(index)
                    .get("label")) > 0) {
                index += 1;
            }

            if (index >= data.size() || !key.equals(data.get(index)
                    .get("label"))) {
                Map<String, String> item = new HashMap<String, String>();
                item.put("label", key);
                item.put("count", "0");
                result.add(item);
            } else {
                result.add(data.get(index));
                index += 1;
            }

            datePointer = datePointer.plusWeeks(1);
        }

        return result;
    }

    public List<Map<String, String>> getByDay(Date from, Date to, Long customerId, Long agentId)
    {
        List<Map<String, String>> data = null;
        UserRole role = apiSession.getRole();

        if (role == null) {
            throw new AccessDeniedException("Not authorized");
        }

        if (from == null) {
            from = Date.from(ZonedDateTime.of(1969, 1, 1, 1, 1, 1, 1, ZoneId.of("UTC"))
                    .toInstant());
        }

        if (to == null) {
            to = Date.from(ZonedDateTime.now(ZoneId.of("UTC"))
                    .plusDays(1)
                    .toInstant());
        }

        if (role.isAdmin() || role.isFreightcomStaff()) {
            if (customerId != null) {
                if (agentId != null) {
                    data = orderRepository.dayHistogramAgentCustomer(from, to, agentId, customerId);
                } else {
                    data = orderRepository.dayHistogramCustomer(from, to, customerId);
                }
            } else if (agentId != null) {
                data = orderRepository.dayHistogramAgent(from, to, agentId);
            } else {
                data = orderRepository.dayHistogram(from, to);
            }
        } else if (role.isCustomer()) {
            if (customerId == null) {
                customerId = role.getCustomerId();
            } else if (!customerId.equals(role.getCustomerId())) {
                throw new AccessDeniedException("Not authorized");
            }

            data = orderRepository.dayHistogramCustomer(from, to, role.getCustomerId());
        } else if (role.isAgent()) {
            if (agentId == null) {
                agentId = role.asAgent()
                        .getId();
            } else if (!agentId.equals(role.asAgent()
                    .getId())) {
                throw new AccessDeniedException("Not authorized");
            }

            if (customerId != null) {
                data = orderRepository.dayHistogramAgentCustomer(from, to, role.getId(), customerId);
            } else {
                data = orderRepository.dayHistogramAgent(from, to, role.getId());
            }
        }

        if (data == null) {
            throw new AccessDeniedException("Not authorized");
        }

        ZonedDateTime datePointer = ZonedDateTime.ofInstant(from.toInstant(), ZoneId.of("UTC"));

        ZonedDateTime lastDate = ZonedDateTime.ofInstant(to.toInstant(), ZoneId.of("UTC"));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        List<Map<String, String>> result = new ArrayList<Map<String, String>>(data.size());
        int index = 0;

        while (datePointer.compareTo(lastDate) <= 0) {
            String key = datePointer.format(formatter);

            while (index < data.size() && key.compareTo(data.get(index)
                    .get("label")) > 0) {
                index += 1;
            }

            if (index >= data.size() || !key.equals(data.get(index)
                    .get("label"))) {
                Map<String, String> item = new HashMap<String, String>();
                item.put("label", key);
                item.put("count", "0");
                result.add(item);
            } else {
                result.add(data.get(index));
                index += 1;
            }

            datePointer = datePointer.plusDays(1);
        }

        return result;
    }

    @Transactional
    public CustomerOrder testRate(Long orderId, Long serviceId, Map<String, BigDecimal> data) throws Exception
    {
        CustomerOrder order = getOrder(orderId);

        if (order.getSelectedQuote() == null) {
            Service service = objectBase.getService(serviceId);

            if (service == null) {
                throw new ResourceNotFoundException("no such service");
            }

            // idempotent
            SelectedQuote selectedQuote = new SelectedQuote();

            selectedQuote.setBaseFee(data.get("base"));
            selectedQuote.setService(service);

            order.setSelectedQuote(selectedQuote);
            setStatus(order, QUOTED);
            log.debug("TEST RATE SET QUOTED " + order);
        } else {

        }

        return order;
    }

    @Transactional
    public CustomerOrder deliverOrder(Long orderId, String comment) throws Exception
    {
        if (apiSession.isFreightcom()) {
            CustomerOrder order = getOrder(orderId);

            if (order.isDelivered()) {
                ValidationException.get()
                        .add("status", "Order already delivered")
                        .doThrow();
            }

            if (order.getSelectedQuote() == null) {
                ValidationException.get()
                        .add("selectedQuote", "Required")
                        .doThrow();
            }

            setStatus(order, DELIVERED, comment);

            publisher.publishEvent(new OrderDeliveredEvent(order, permissionChecker.getUser()));

            return order;
        } else {
            throw new AccessDeniedException("Not authorized");
        }
    }

    @Transactional
    public CustomerOrder markIntransit(Long orderId, String comment) throws Exception
    {
        if (apiSession.isFreightcom()) {

            CustomerOrder order = getOrder(orderId);

            if (order.isDelivered()) {
                ValidationException.get()
                        .add("status", "Order already delivered")
                        .doThrow();
            }

            if (order.getSelectedQuote() == null) {
                ValidationException.get()
                        .add("selectedQuote", "Required")
                        .doThrow();
            }

            setStatus(order, IN_TRANSIT, comment);

            return order;
        } else {
            throw new AccessDeniedException("Not authorized");
        }
    }

    @Transactional
    public Object schedulePickup(Long orderId, PickupData pickupData)
    {
        CustomerOrder order = getOrder(orderId);
        Pickup pickup = order.getScheduledPickup();

        if (pickup == null) {
            pickup = new Pickup();
            setConfirmation(pickup);
            objectBase.save(pickup);
        }

        pickup.update(pickupData);

        objectBase.save(LoggedEvent.orderStatusMessage(permissionChecker.getUser(), order, "Pickup scheduled",
                order.getOrderStatusName(), MessageAction.MANUAL_UPDATE));

        return order;
    }

    protected void setConfirmation(Pickup pickup)
    {
        if (pickup.getCarrierConfirmation() == null) {
            final SecureRandom random = new SecureRandom();
            final char[] characterMap = "ABCDEFGHIJKLMNOPQRSTUV012345679".toCharArray();
            final StringBuilder confirmation = new StringBuilder();
            final int n = 8 + random.nextInt(3);
            final int bound = characterMap.length;

            for (int i = 0; i < n; i++) {
                confirmation.append(characterMap[random.nextInt(bound)]);
            }

            pickup.setCarrierConfirmation(confirmation.toString());
        }
    }

    @Override
    public Object uploadPOD(Long orderId, MultipartFile file) throws Exception
    {
        return uploadPOD(orderId, file, null);
    }

    @Transactional
    public Object uploadPOD(Long orderId, MultipartFile file, String description) throws Exception
    {
        UserRole role = apiSession.getRole();

        if (!role.isAdmin() && !role.isFreightcomStaff()) {
            throw new AccessDeniedException("Not authorized");
        }

        Map<String, Object> result = new HashMap<String, Object>();

        CustomerOrder order = orderRepository.findOne(orderId);
        checkOrder(order);

        String extension = "";

        int i = file.getOriginalFilename()
                .lastIndexOf('.');
        if (i > 0) {
            extension = "." + file.getOriginalFilename()
                    .substring(i + 1);
        }

        result = documentManager.upload("/public", orderId + "-pod" + extension, file);

        if (result.get("status") != null && result.get("status")
                .equals("ok") && result.get("path") != null) {
            // TODO - legacy pod
            order.setPodName((new File(file.getOriginalFilename())).getName());
            order.setPodFileName(result.get("path")
                    .toString());
            order.setPodTimestamp(new Date());

            OrderDocument document = new OrderDocument();

            document.setName((new File(file.getOriginalFilename())).getName());
            document.setType((String) result.get("mimeType"));
            document.setDescription(description);
            document.setDocumentId((String) result.get("path"));
            document.setUploadedByRole(apiSession.getRole());

            order.addFile(document);

            if (order.isPreDelivery()) {
                setStatus(order, DELIVERED);
                publisher.publishEvent(new OrderDeliveredEvent(order, permissionChecker.getUser()));
            }

            String comment = description == null ? "POD uploaded: " + document.getName()
                    : "POD uploaded: " + document.getName() + (description == null ? "" : " - " + description);

            objectBase.save(LoggedEvent.orderStatusMessage(permissionChecker.getUser(), order, comment,
                    order.getOrderStatusName(), MessageAction.UPLOAD));
        } else {
            throw new Error("Error uploading POD");
        }

        return result;
    }

    @Override
    public Object upload(Long orderId, MultipartFile file) throws Exception
    {
        return upload(orderId, file, null);
    }

    @Transactional
    @Override
    public Object upload(Long orderId, MultipartFile file, String description) throws Exception
    {
        CustomerOrder order = getOrder(orderId);

        Map<String, Object> result = documentManager.upload("/public",
                orderId + "/" + (new File(file.getOriginalFilename())).getName(), file);

        log.debug("UPLOAD ORDER DOCUMENT RESULT " + MapBuilder.toString(result));

        if (result.get("status") != null && result.get("status")
                .equals("ok") && result.get("path") != null) {
            OrderDocument document = new OrderDocument();

            document.setName((new File(file.getOriginalFilename())).getName());
            document.setType((String) result.get("mimeType"));
            document.setDescription(description);
            document.setDocumentId((String) result.get("path"));
            document.setUploadedByRole(apiSession.getRole());

            order.addFile(document);

            String comment = description == null ? "Upload " + document.getName()
                    : "Upload " + document.getName() + " - " + description;

            objectBase.save(LoggedEvent.orderStatusMessage(permissionChecker.getUser(), order, comment,
                    order.getOrderStatusName(), MessageAction.UPLOAD));
        } else {
            throw new Error("Error uploading POD");
        }

        return result;
    }

    @Override
    public Object downloadOrderDocument(Long documentId) throws Exception
    {
        OrderDocument document = objectBase.getOrderDocument(documentId);
        checkOrder(document.getOrder());

        return documentManager.stream(document.getDocumentId());
    }

    @Override
    @Transactional
    public Object deleteOrderDocument(Long documentId) throws Exception
    {
        OrderDocument document = objectBase.getOrderDocument(documentId);

        if (document == null) {
            throw new ResourceNotFoundException("Missing document");
        }

        if (!document.canDelete(apiSession.getRole())) {
            throw new AccessDeniedException("Not authorized");
        }

        CustomerOrder order = document.getOrder();

        checkOrder(order);

        if (order.containsFile(document)) {
            order.removeFile(document);

            objectBase.save(LoggedEvent.orderStatusMessage(permissionChecker.getUser(), order,
                    "Deleted " + document.getName(), order.getOrderStatusName(), MessageAction.UPLOAD));

        } else {
            throw new ResourceNotFoundException("Document not in order");
        }

        return true;
    }

    public Object downloadPod(Long orderId) throws Exception
    {
        CustomerOrder order = orderRepository.findOne(orderId);
        checkOrder(order);

        if (order.getPodFileName() == null) {
            throw new ReportableError("No POD for order");
        }

        return documentManager.stream(order.getPodFileName());
    }

    public ResponseEntity<InputStreamResource> downloadLegacyPod(Long orderId) throws Exception
    {
        CustomerOrder order = orderRepository.findOne(orderId);
        checkOrder(order);

        if (order.getPodFile() == null) {
            throw new ReportableError("No POD for order");
        }

        HttpHeaders headers = new HttpHeaders();

        headers.add("content-disposition", "attachment; filename=pod.bin");

        return ResponseEntity.ok()
                .headers(headers)
                // may need to set content length here
                // .contentLength(pdfFile.contentLength())
                .body(new InputStreamResource(new ByteArrayInputStream(order.getPodFile())));
    }

    public Object getInvoices(Long orderId)
    {
        CustomerOrder order = orderRepository.findOne(orderId);
        checkOrder(order);

        Page<View> invoices = new PageImpl<Invoice>(order.getInvoices(), new PageRequest(0, order.getInvoices()
                .size()),
                order.getInvoices()
                        .size()).map(new InvoiceConverter());

        if (invoices.getSize() > 0) {
            return assembler.toResource(invoices);
        } else {
            log.debug("ZERO INVOICES FOR " + orderId + " " + order.getInvoices()
                    .size());
            return new HashMap<Object, Object>(0);
        }
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('FREIGHTCOM_STAFF')")
    @Transactional
    @Override
    public Charge reconcileCharge(Long chargeId) throws Exception
    {
        Charge charge = objectBase.getCharge(chargeId);

        if (charge == null) {
            throw new ResourceNotFoundException("Charge not found " + chargeId);
        }

        charge.setReconciled(true);

        return charge;
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('FREIGHTCOM_STAFF')")
    @Transactional
    @Override
    public CustomerOrder reconcileOrderCharges(Long orderId) throws Exception
    {
        CustomerOrder order = orderRepository.findOne(orderId);
        checkOrder(order);

        order.reconcileCharges();

        return order;
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('FREIGHTCOM_STAFF')")
    public Charge addCharge(Long orderId, Map<String, Object> chargeData) throws ValidationException
    {
        CustomerOrder order = orderRepository.findOne(orderId);
        checkOrder(order);

        Charge charge = updateCharge(new Charge(), chargeData, null, order);

        charge.setAgent(order.getAgent());
        charge.setOrder(order);

        if (order.getSelectedQuote() != null) {
            charge.setService(order.getSelectedQuote()
                    .getService());
        }

        order.getCharges()
                .add(charge);

        return charge;
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('FREIGHTCOM_STAFF')")
    public Charge updateCharge(Long chargeId, Map<String, Object> attributes) throws ValidationException
    {
        Charge charge = objectBase.getCharge(chargeId);

        if (charge == null) {
            throw new ResourceNotFoundException("No charge " + chargeId);
        }

        CustomerOrder order = charge.getOrder();
        Charge previous = new Charge(charge);

        checkOrder(order);

        charge = updateCharge(charge, attributes, previous, order);

        return charge;
    }

    /**
     *
     */
    protected Charge updateCharge(Charge charge, Map<String, Object> attributes, Charge previous, CustomerOrder order)
            throws ValidationException
    {
        synchronized (OrderLock.getLock(charge.getOrder())) {
            ValidationException exception = null;
            Boolean notifyCustomer = false;

            log.debug("UPDATING CHARGE " + charge);

            if (charge.getCurrency() == null) {
                charge.setCurrency(order.getCustomer()
                        .getInvoiceCurrency());
            }

            Object description = null;

            for (String key : attributes.keySet()) {
                switch (key) {
                case "charge":
                    BigDecimal chargeAmount = validateBigDecimal(attributes.get(key));

                    if (chargeAmount == null) {
                        exception = validation(exception, key, "invalid number");
                    } else {
                        charge.setCharge(chargeAmount);
                    }
                    break;

                case "applyCommission":
                    Boolean applyCommission = YesValue.parseStrict(attributes.get(key));

                    if (applyCommission == null) {
                        exception = validation(exception, key, "invalid boolean");
                    } else {
                        charge.setApplyCommission(applyCommission);
                    }
                    break;

                case "notifyCustomer":
                    notifyCustomer = YesValue.parseStrict(attributes.get(key));

                    log.debug("PROCESS NOTIFY CUSTOMER " + attributes.get(key) + " " + notifyCustomer);

                    if (notifyCustomer == null) {
                        exception = validation(exception, key, "invalid boolean");
                    }
                    break;

                case "cost":
                    BigDecimal costAmount = validateBigDecimal(attributes.get(key));

                    if (costAmount == null) {
                        exception = validation(exception, key, "invalid number");
                    } else {
                        charge.setCost(costAmount);
                    }
                    break;

                case "quantity":
                    Integer quantity = validateInteger(attributes.get(key));

                    if (quantity == null) {
                        exception = validation(exception, key, "invalid quantity");
                    } else {
                        charge.setQuantity(quantity);
                    }
                    break;

                case "carrierId":
                    Service service = validateCarrier(attributes.get(key));

                    if (service == null) {
                        exception = validation(exception, key, "invalid carrier");
                    }

                    charge.setService(service);
                    break;

                case "accessorialId":
                    AccessorialServices accessorialService = objectBase.getAccessorialServices(attributes.get(key));

                    if (accessorialService == null) {
                        exception = validation(exception, key, "invalid accessorial");
                    } else if (accessorialService.getId()
                            .equals(AccessorialServices.OTHER)) {
                        if (attributes.get("description") == null || attributes.get("description")
                                .toString()
                                .isEmpty()) {
                            exception = validation(exception, "description", "required");
                        } else {
                            OrderAccessorials accessorial = objectBase.getOrderAccessorial(charge.getAccessorialId());

                            if (accessorial == null) {
                                accessorial = new OrderAccessorials();
                                objectBase.save(accessorial);
                                objectBase.flush();
                                charge.setAccessorial(accessorial);
                            }

                            accessorial.setService(accessorialService);
                            charge.setDescription(attributes.get("description")
                                    .toString());
                        }
                    } else {
                        OrderAccessorials accessorial = charge.getAccessorial();

                        if (accessorial == null) {
                            accessorial = new OrderAccessorials();
                            objectBase.save(accessorial);
                            objectBase.flush();
                            charge.setAccessorial(accessorial);
                        }

                        accessorial.setService(accessorialService);
                    }

                    break;

                case "description":
                    // ok
                    description = attributes.get(key);
                    break;

                default:
                    exception = validation(exception, key, "invalid attribute");
                    break;
                }
            }

            if (description != null) {
                charge.setDescription(description.toString());
            }

            if (exception != null) {
                exception.doThrow();
            }

            chargeNotifyChange(order, previous, charge, notifyCustomer);

            log.debug("DONE UPDATING CHARGE " + charge);

            updateTax(charge, order);

            log.debug("RETURN UPDATING CHARGE " + charge);

            return charge;
        }
    }

    // TODO - handle case where taxes for customer have changed
    private void updateTax(Charge charge, CustomerOrder order)
    {
        List<ChargeTax> taxes = charge.getTaxes();

        for (ApplicableTax applicableTax : order.getCustomer()
                .getApplicableTaxes()) {
            TaxDefinition definition = applicableTax.getTaxDefinition();
            Optional<ChargeTax> first = taxes.stream()
                    .filter(tax -> tax.getName()
                            .equals(definition.getName()))
                    .findFirst();

            if (!first.isPresent()) {
                // Tax needs to be added
                log.debug("CHARGE TAX NEW");

                if (charge.getTaxable()) {
                    charge.addTax(definition.getName(), definition.getTaxId(), definition.getRate());
                }
            } else {
                log.debug("CHARGE TAX EXISTING " + first.get());
            }
        }
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('FREIGHTCOM_STAFF')")
    public Object deleteCharge(Long chargeId)
    {
        Charge charge = objectBase.getCharge(chargeId);

        if (charge == null) {
            throw new ResourceNotFoundException("No charge " + chargeId);
        }

        synchronized (OrderLock.getLock(charge.getOrder())) {
            CustomerOrder order = charge.getOrder();
            checkOrder(order);

            order.getCharges()
                    .remove(charge);

            chargeNotifyChange(order, charge, null, false);

            return MapBuilder.getNew()
                    .put("status", "ok")
                    .getMap();
        }
    }

    private void chargeNotifyChange(CustomerOrder order, Charge previous, Charge charge, boolean notifyCustomer)
    {
        publisher.publishEvent(new OrderChargeChange(order, previous, charge, notifyCustomer, apiSession.getRole()));
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('FREIGHTCOM_STAFF')")
    public Object addStatusUpdate(Long orderId, Map<String, Object> attributes) throws Exception
    {
        CustomerOrder order = orderRepository.findOne(orderId);
        checkOrder(order);

        addStatusUpdate(order, attributes);

        return MapBuilder.getNew()
                .put("status", "ok")
                .getMap();
    }

    public LoggedEvent addStatusUpdate(CustomerOrder order, Map<String, Object> attributes) throws Exception
    {
        LoggedEvent message = new LoggedEvent();

        if (attributes.get("comment") == null || attributes.get("comment")
                .toString()
                .isEmpty()) {
            ValidationException.get()
                    .add("comment", "required")
                    .doThrow();
        }

        message.setComment(attributes.get("comment")
                .toString());

        if (YesValue.parse(attributes.get("private"))) {
            message.setPrivate(true);
            message.setAction(MessageAction.PRIVATE_NOTE);
        } else {
            message.setPrivate(false);
            message.setAction(MessageAction.NOTE);
        }

        message.setEntityType(LoggedEvent.ENTITY_TYPE_ORDER);

        if (attributes.get("messageType") != null && "invoice".equalsIgnoreCase(attributes.get("messageType")
                .toString())) {
            message.setMessageType(LoggedEvent.MESSAGE_TYPE_INVOICE);
        } else {
            message.setMessageType(LoggedEvent.MESSAGE_TYPE_STATUS);
        }

        message.setEntityId(order.getId());
        message.setUserId(apiSession.getUser() == null ? null : apiSession.getUser()
                .getId());
        message.setMessage(order.getOrderStatusName());

        objectBase.save(message);

        return message;
    }

    private ValidationException validation(ValidationException exception, String key, String message)
    {
        if (exception == null) {
            exception = ValidationException.get();
        }

        return exception.add(key, message);
    }

    private BigDecimal validateBigDecimal(Object value)
    {
        BigDecimal result;

        try {
            if (value == null) {
                return null;
            } else {
                return new BigDecimal(value.toString());
            }
        } catch (NumberFormatException e) {
            result = null;
        }

        return result;
    }

    private Integer validateInteger(Object value)
    {
        Integer result;

        try {
            if (value == null) {
                result = null;
            } else {
                result = Integer.parseInt(value.toString());
            }
        } catch (NumberFormatException e) {
            result = null;
        }

        return result;
    }

    private Service validateCarrier(Object serviceId)
    {
        return objectBase.getService(serviceId);
    }

    @Override
    public Object statsPerType()
    {
        Map<Object, Object> statistics = new HashMap<Object, Object>();

        UserRole role = apiSession.getRole();
        int workOrders = 0;
        List<Map<String, Object>> statsPerType;

        if (role.getCustomerId() != null) {
            // Restrict to customer
            statsPerType = orderRepository.statsPerType(role.getCustomerId());
        } else {
            // TODO - agent - only agent's customers
            // Admin
            statsPerType = orderRepository.statsPerType();
        }

        for (Map<String, Object> bucket : statsPerType) {
            if (Boolean.TRUE.equals(bucket.get("workorder"))) {
                workOrders += Integer.parseInt(bucket.get("count")
                        .toString());
            } else {
                if (bucket.get("type") == null) {

                } else {
                    statistics.put(bucket.get("type"), bucket.get("count"));
                }
            }
        }

        statistics.put("workorder", new Integer(workOrders));

        return statistics;
    }

    @Override
    @Transactional
    public DisputeInformation disputeCharge(Long chargeId, Map<String, Object> attributes) throws Exception
    {
        Charge charge = objectBase.getCharge(chargeId);

        if (charge == null) {
            throw new ResourceNotFoundException("No charge " + chargeId);
        }

        CustomerOrder order = charge.getOrder();

        log.debug("DISPUTE CHARGE " + MapBuilder.toString(attributes));

        checkOrder(order);

        return (new Dispute(getRole(), publisher, objectBase)).disputeCharge(charge, attributes);
    }

    @Override
    @Transactional
    public DisputeInformation respondToDispute(Long orderId, Map<String, Object> attributes) throws Exception
    {
        return (new Dispute(getRole(), publisher, objectBase)).respondToDispute(getOrder(orderId), attributes);
    }

    @Override
    @Transactional
    public Claim submitClaim(Long orderId, Map<String, Object> attributes) throws Exception
    {
        return (new ClaimActions(getRole(), publisher, this)).submit(checkOrder(getOrder(orderId)), attributes);
    }

    @Override
    @Transactional
    public Object updateClaim(Long orderId, Map<String, Object> attributes) throws Exception
    {
        return (new ClaimActions(getRole(), publisher, this)).update(checkOrder(getOrder(orderId)), attributes);
    }

    public void publishEvent(Object event)
    {
        log.debug("PUBLISH EVENT" + event);
        publisher.publishEvent(event);
    }

    public ApiSession getApiSession()
    {
        return apiSession;
    }

    public ObjectBase getObjectBase()
    {
        return objectBase;
    }

    public UserRole getRole()
    {
        if (apiSession == null || apiSession.getRole() == null) {
            return objectBase.getSystemRole();
        }

        return apiSession.getRole();
    }

    @Override
    public void updateReadyForShipping() throws Exception
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Map<String, Object> criteria = new HashMap<String, Object>();

        criteria.put("status", SCHEDULED.getValue());
        criteria.put("pickupDate", ZonedDateTime.now(ZoneId.of("UTC"))
                .format(formatter));

        for (CustomerOrder order : orderRepository.findAll(new OrderSpecification(criteria))) {
            setStatus(order, READY_FOR_SHIPPING);
        }
    }

    /**
     * UPS Rating engine test
     *
     * @throws Exception
     */
    @Override
    @Transactional
    public Object testUPS(Long orderId) throws Exception
    {
        return getRates(getOrder(orderId));
    }

    /**
     * UPS Rating engine test
     *
     * @throws Exception
     */
    @Override
    @Transactional
    public RateRequestResult getRates(CustomerOrder order) throws Exception
    {
        return ratingsService.getRates(order,
                (code, name, carrier, description) -> carrier.findService(code, name, description), objectBase);
    }

    /**
     * Return a view of the order appropriate for filling out the duplicate form
     */
    @Override
    public Object duplicate(Long orderId) throws Exception
    {
        return DuplicateOrderView.get(this, checkOrder(getOrder(orderId)));
    }

    /**
     * Return a view of the order appropriate for filling out the duplicate form
     */
    @Override
    public Object customerUsers(Long orderId) throws Exception
    {
        log.debug("GET CUSTOMER USERS");
        Customer customer = checkOrder(getOrder(orderId)).getCustomer();

        if (customer == null) {
            throw new ResourceNotFoundException("Order has no customer");
        }

        Map<String, Object> criteria = new HashMap<String, Object>();
        criteria.put("customerid", customer.getId());

        Pageable pageable = new PageRequest(0, 99999, Sort.Direction.ASC, "lastname");
        List<Object> result = new ArrayList<Object>();

        for (User user : userRepository.findAll(new UserSpecification(criteria), pageable)) {
            Map<String, Object> view = new HashMap<String, Object>();
            UserRole role = user.roleForCustomer(customer);

            log.debug("A USER CUSTOMER USERS " + user + " " + role);

            if (role != null) {
                view.put("text", user.fullName() + " (" + user.getEmail() + ")");
                view.put("id", role.getId());

                result.add(view);
            }
        }

        log.debug("DONE CUSTOMER USERS " + customer.getId() + " " + result + " " + result.size());

        return result;
    }

    @Override
    public Object shippingLabel(Long orderId) throws Exception
    {
        CustomerOrder order = checkOrder(getOrder(orderId));

        if (!order.hasShippingLabel()) {
            throw new Exception("No label");
        }

        HttpHeaders headers = new HttpHeaders();

        headers.add("content-type", "image/gif");
        headers.add("content-disposition", "attachment; filename=shipping-label.gif");
        headers.add("content-length", String.format("%d", order.getShippingLabel()
                .getLabel().length));

        return ResponseEntity.ok()
                .headers(headers)
                .body(new ByteArrayResource(order.getShippingLabel()
                        .getLabel()));
    }

    private Customer authorizedCustomer(Long customerId) throws Exception
    {
        boolean valid = false;

        if (customerId == null) {
            customerId = apiSession.getCustomerId();
            valid = true;
        }

        Validation.get()
                .test(customerId != null, "customerId", "Customer id may not be null")
                .throwIfFailed();

        if (!valid) {
            if (apiSession.isFreightcom()) {
                // admin or freightcom staff
                valid = true;
            } else if (apiSession.isCustomer() && customerId.equals(apiSession.getCustomerId())) {
                // Logged in customer
                valid = true;
            }
        }

        Customer customer = objectBase.getCustomer(customerId);

        if (customer == null) {
            throw new ResourceNotFoundException("Missing customer");
        }

        if (!valid) {
            if (apiSession.isAgent() && apiSession.getRole()
                    .isAgentFor(customer)) {
                valid = true;
            }
        }

        if (!valid) {
            throw new AccessDeniedException("Not authorized");
        }

        return customer;
    }

    @Override
    public List<PalletTemplate> getPalletTemplates(Long customerId) throws Exception
    {
        return objectBase.getPalletTemplates(authorizedCustomer(customerId));
    }

    @Override
    public PalletTemplate createPalletTemplate(PalletTemplate template, Long customerId) throws Exception
    {
        template.setCustomer(authorizedCustomer(customerId));

        if (template.getPalletTypeId() != null) {
            template.setPalletType(objectBase.getPalletType(template.getPalletTypeId()));
        } else {
            template.setPalletType(objectBase.getPalletType(1L));
        }

        return objectBase.save(template);
    }

    @Override
    public boolean removePalletTemplate(Long templateId) throws Exception
    {
        PalletTemplate template = objectBase.getPalletTemplate(templateId);

        Validation.get()
                .test(template != null, "template", "Template may not be null")
                .test(apiSession.isFreightcom() || template.getCustomer() != null, "customer",
                        "Customer must not be null")
                .test(authorizedCustomer(template.getCustomer()
                        .getId()) != null, "customer", "Not authorized")
                .throwIfFailed();

        objectBase.delete(template);

        return true;
    }

    @Override
    public List<PalletType> getPalletTypes() throws Exception
    {
        return objectBase.getPalletTypes();
    }
}
