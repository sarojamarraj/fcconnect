package com.freightcom.api.services;

import java.math.BigDecimal;
import java.nio.file.AccessDeniedException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.ConfigurablePropertyAccessor;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.freightcom.api.ReportableError;
import com.freightcom.api.events.InvoiceCreatedEvent;
import com.freightcom.api.events.InvoiceDeletedEvent;
import com.freightcom.api.events.InvoicePaidEvent;
import com.freightcom.api.events.SystemLogEvent;
import com.freightcom.api.model.AppliedPayment;
import com.freightcom.api.model.Charge;
import com.freightcom.api.model.Credit;
import com.freightcom.api.model.Customer;
import com.freightcom.api.model.CustomerOrder;
import com.freightcom.api.model.DeletedInvoice;
import com.freightcom.api.model.Invoice;
import com.freightcom.api.model.LoggedEvent;
import com.freightcom.api.model.PaymentTransaction;
import com.freightcom.api.model.support.OrderStatusCode;
import com.freightcom.api.model.views.InvoiceIndividualView;
import com.freightcom.api.model.views.InvoiceView;
import com.freightcom.api.model.views.View;
import com.freightcom.api.repositories.ObjectBase;
import com.freightcom.api.repositories.custom.CustomerRepository;
import com.freightcom.api.repositories.custom.CustomerSpecification;
import com.freightcom.api.repositories.custom.InvoiceRepository;
import com.freightcom.api.repositories.custom.InvoiceSpecification;
import com.freightcom.api.repositories.custom.OrderRepository;
import com.freightcom.api.repositories.custom.OrderSpecification;
import com.freightcom.api.services.converters.InvoiceListConverter;
import com.freightcom.api.services.dataobjects.InvoicePayment;
import com.freightcom.api.services.invoices.Charges;
import com.freightcom.api.services.invoices.InvoicePaymentProvider;
import com.freightcom.api.services.invoices.InvoiceWrapper;
import com.freightcom.api.services.payment.PaymentProcessor;
import com.freightcom.api.util.Empty;

/**
 * @author bryan
 *
 */
@Component
public class InvoiceServiceImpl extends ServicesCommon implements InvoiceService, ServiceProvider
{
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final InvoiceRepository invoiceRepository;
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final PagedResourcesAssembler<View> invoiceAssembler;
    private final PermissionChecker permissionChecker;
    private final PaymentProcessor paymentProcessor;
    private final ObjectBase objectBase;
    private final Boolean invoicing = Boolean.TRUE;

    @Autowired
    public InvoiceServiceImpl(final InvoiceRepository invoiceRepository, final OrderRepository orderRepository,
            final CustomerRepository customerRepository, final PagedResourcesAssembler<View> invoiceAssembler,
            final PermissionChecker permissionChecker, final PaymentProcessor paymentProcessor,
            final ObjectBase objectBase)
    {
        super(null);
        this.customerRepository = customerRepository;
        this.invoiceRepository = invoiceRepository;
        this.invoiceAssembler = invoiceAssembler;
        this.permissionChecker = permissionChecker;
        this.orderRepository = orderRepository;
        this.paymentProcessor = paymentProcessor;
        this.objectBase = objectBase;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.freightcom.api.services.InvoiceService#getInvoicesConverted(java.util
     * .Map, org.springframework.data.domain.Pageable)
     */
    @Override
    public PagedResources<Resource<View>> getInvoicesConverted(Map<String, Object> criteria, Pageable pageable)
    {
        permissionChecker.checkCriteria(criteria);

        if (!permissionChecker.isFreightcom()) {
            // Non freightcom users cannot see deleted invoices
            criteria.put("notcancelled", true);
            log.debug("CUSTOMER LIST INVOICES");
        } else {
            log.debug("FREIGHTCOM LIST INVOICES");
        }

        Page<View> invoices = invoiceRepository.findAll(new InvoiceSpecification(criteria), pageable)
                .map(new InvoiceListConverter());

        return invoiceAssembler.toResource(invoices, new Link("/invoice"));
    }

    @Override
    public InvoiceView getInvoiceView(Long invoiceId)
    {
        Invoice invoice = findOne(invoiceId);

        return getInvoiceView(invoice);
    }

    @Override
    public View getIndividualInvoiceView(Long invoiceId)
    {
        Invoice invoice = findOne(invoiceId);

        return getIndividualInvoiceView(invoice);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.freightcom.api.services.InvoiceService#createOrUpdateInvoice(com.
     * freightcom.api.model.Invoice)
     */
    @Override
    @Transactional
    public Invoice createOrUpdateInvoice(Invoice invoice) throws Exception
    {
        permissionChecker.check(invoice);

        if (invoice.getInvoiceDate() == null) {
            invoice.setInvoiceDate(new Date());
        }

        invoiceRepository.save(invoice);

        return invoice;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.freightcom.api.services.InvoiceService#createInvoice(com.freightcom.
     * api.model.Invoice, java.util.Map)
     */
    @Override
    @Transactional
    public View createInvoice(final Invoice invoiceData, Map<String, Object> attributes) throws Exception
    {
        List<?> orderSpecifications = null;

        synchronized (invoicing) {

            if (attributes.get("orders") instanceof List<?>) {
                orderSpecifications = (List<?>) attributes.get("orders");
            }

            List<CustomerOrder> orders = new ArrayList<CustomerOrder>();

            if (orderSpecifications != null) {
                for (Object orderSpecification : orderSpecifications) {
                    CustomerOrder order = null;

                    if (orderSpecification instanceof Map<?, ?>) {
                        order = objectBase.getOrder(((Map<?, ?>) orderSpecification).get("id"));
                    } else {
                        order = objectBase.getOrder(orderSpecification);
                    }

                    if (order == null) {
                        throw new ReportableError("No such order");
                    }

                    orders.add(order);
                }
            }

            invoiceData.setOrders(orders);

            List<Customer> orderCustomers = invoiceData.findOrderCustomers();

            if (invoiceData.getOrders() == null || invoiceData.getOrders()
                    .size() == 0) {
                throw new ReportableError("No orders provided");
            }

            if (orderCustomers.size() == 0) {
                throw new ReportableError("Orders have no customers");
            }

            if (orderCustomers.size() > 1) {
                throw new ReportableError("Orders are from different customers");
            }

            Invoice newInvoice;

            permissionChecker.checkCreate(invoiceData);

            if (invoiceData.sumNewCharges()
                    .equals(BigDecimal.ZERO)) {
                // Invoice would be for amount 0
                throw new ReportableError("No charges to invoice");
            }

            if (invoiceData.getId() != null) {
                newInvoice = new Invoice();
                BeanUtils.copyProperties(invoiceData, newInvoice);
            } else {
                newInvoice = invoiceData;
            }

            if (invoiceData.getCustomer() == null) {
                invoiceData.setCustomer(objectBase.getCustomer(attributes.get("customerId")));

                if (invoiceData.getCustomer() == null) {
                    invoiceData.setCustomer(orderCustomers.get(0));
                }

                if (invoiceData.getCustomer() == null) {
                    throw new ReportableError("Invoice requires customer");
                }
            } else {
                invoiceData.setCustomer(objectBase.getCustomer(invoiceData.getCustomer()
                        .getId()));
            }

            if (invoiceData.getCustomer() != orderCustomers.get(0)) {
                throw new ReportableError("Specified customer does not match orders");
            }

            Invoice invoice = createOrUpdateInvoice(newInvoice);

            log.debug("INVOICE CREATED, MARKING RATES " + invoice);

            invoice.markRates();
            invoice.setAmount(invoice.sumRates());
            invoice.setTax(invoice.sumTaxes());
            invoice.computeDueDateIfNotSet();

            publishEvent(new InvoiceCreatedEvent(invoice, permissionChecker.getRole()));
            publishEvent(new SystemLogEvent(invoice, permissionChecker.getUser(), "invoice created", null));

            return getIndividualInvoiceView(invoice);
        }
    }

    protected boolean skipKey(String key)
    {
        return key == null || key.equals("id") || key.equals("status") || key.equals("customer")
                || key.equals("updatedAt") || key.equals("createdAt") || key.equals("updatedBy")
                || key.equals("deletedAt") || key.equals("dateGenerated");
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.freightcom.api.services.InvoiceService#updateInvoice(java.lang.Long,
     * com.freightcom.api.model.Invoice, java.util.Map)
     */
    @Override
    @Transactional
    public InvoiceIndividualView updateInvoice(Long id, Invoice invoice, Map<String, Object> attributes)
            throws Exception
    {
        Invoice existing = findOne(id);

        ConfigurablePropertyAccessor source = PropertyAccessorFactory.forDirectFieldAccess(invoice);
        ConfigurablePropertyAccessor dest = PropertyAccessorFactory.forDirectFieldAccess(existing);

        // Only copy attributes supplied in the JSON input, leave others alone
        for (String key : attributes.keySet()) {
            if (!skipKey(key) && dest.isWritableProperty(key)) {
                dest.setPropertyValue(key, source.getPropertyValue(key));
            }
        }

        createOrUpdateInvoice(existing);

        return getIndividualInvoiceView(existing);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.freightcom.api.services.InvoiceService#deleteInvoices(java.util.Map)
     */
    @Override
    @Transactional
    public Object realDeleteInvoice(Long id) throws Exception
    {
        List<Long> deleted = new ArrayList<Long>();

        permissionChecker.checkFreightcom();

        Invoice invoice = objectBase.getInvoice(new Long(id.toString()));

        if (invoice != null) {
            log.debug("REAL DELETE INVOICE " + invoice);
            deleted.add(invoice.getId());

            for (Charge charge : invoice.getCharges()) {
                charge.setInvoiceId(null);
            }

            BigDecimal amountPaid = invoice.getPaidAmount()
                    .add(invoice.getCreditedAmount());

            if (amountPaid.compareTo(BigDecimal.ZERO) > 0) {
                Credit credit = new Credit();

                credit.setAmount(amountPaid);
                credit.setAmountRemaining(amountPaid);
                credit.setCustomerId(invoice.getCustomer()
                        .getId());
                credit.setNote("Invoice " + invoice.getId() + " deleted");

                objectBase.save(credit);
            }

            log.debug("REAL DELETE INVOICE " + invoice);

            objectBase.delete(invoice);

            publishEvent(new InvoiceDeletedEvent(invoice, permissionChecker.getRole()));
        }

        return deleted;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.freightcom.api.services.InvoiceService#deleteInvoices(java.util.Map)
     */
    @Override
    @Transactional
    public Object deleteInvoices(Map<String, Object> values)
    {
        List<Long> deleted = new ArrayList<Long>();

        permissionChecker.checkFreightcom();

        if (values.get("invoices") != null && values.get("invoices") instanceof Iterable<?>) {
            for (Object id : (Iterable<?>) values.get("invoices")) {
                if (!Empty.check(id)) {
                    Invoice invoice = objectBase.getInvoice(new Long(id.toString()));

                    if (invoice != null) {
                        deleted.add(invoice.getId());

                        for (Charge charge : invoice.getCharges()) {
                            charge.setInvoiceId(null);
                        }

                        BigDecimal amountPaid = invoice.getPaidAmount()
                                .add(invoice.getCreditedAmount());

                        if (amountPaid.compareTo(BigDecimal.ZERO) > 0) {
                            Credit credit = new Credit();

                            credit.setAmount(amountPaid);
                            credit.setAmountRemaining(amountPaid);
                            credit.setCustomerId(invoice.getCustomer()
                                    .getId());
                            credit.setNote("Invoice " + invoice.getId() + " deleted");

                            objectBase.save(credit);
                        }

                        invoice.cancel();

                        publishEvent(new InvoiceDeletedEvent(invoice, permissionChecker.getRole()));
                    }
                }
            }
        } else {
            log.debug("NOTHING TO DELETE " + values.get("invoices"));
            throw new ReportableError("No invoices specified");
        }

        return deleted;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.freightcom.api.services.InvoiceService#view(java.lang.Long)
     */
    @Override
    @Transactional
    public InvoiceIndividualView view(Long id)
    {
        Invoice invoice = findOne(id);

        if (invoice.getViewedAt() == null) {
            invoice.setViewedAt(new Date());
        }

        return getIndividualInvoiceView(invoice);
    }

    private List<LoggedEvent> getInvoiceMessages(final Invoice invoice)
    {
        return objectBase.getInvoiceLoggedEvents(invoice, permissionChecker.isFreightcomOrAdmin());
    }

    /*
     * (non-Javadoc)
     *
     * @see com.freightcom.api.services.InvoiceService#findOne(java.lang.Long)
     */
    @Override
    public Invoice findOne(Long id)
    {
        if (id == null) {
            throw new ResourceNotFoundException("No invoice");
        }

        Invoice invoice = invoiceRepository.findOne(id);

        if (invoice == null) {
            throw new ResourceNotFoundException("No invoice");
        }

        permissionChecker.check(invoice);

        return invoice;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.freightcom.api.services.InvoiceService#applyCredit(java.util.Map)
     */
    @Override
    @Transactional
    public List<InvoiceView> applyCredit(Map<String, Object> attributes) throws AccessDeniedException
    {
        Credit credit = objectBase.getCredit(attributes.get("credit"));
        Invoice invoice = objectBase.getInvoice(attributes.get("invoice"));

        permissionChecker.check(invoice);

        if (credit != null) {
            permissionChecker.check(credit);

            if (!credit.getCustomerId()
                    .equals(invoice.getCustomerId())) {
                throw new AccessDeniedException("invoice customer doesn't match credit customer");
            }
        }

        return payInvoice(Arrays.asList(new Invoice[] { invoice }), null, BigDecimal.ZERO, null, null);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.freightcom.api.services.InvoiceService#pay(com.freightcom.api.
     * services.dataobjects.InvoicePayment)
     */
    @Override
    @Transactional
    public Object payNow(InvoicePayment payment) throws AccessDeniedException
    {
        return payInvoiceNow(payment);
    }

    /**
     * @param invoices
     * @param credits
     * @param remaining
     * @param remainingCredit
     * @return
     * @throws AccessDeniedException
     */
    protected Object payInvoiceNow(InvoicePayment payment) throws AccessDeniedException
    {
        return InvoicePaymentProvider.get(this, paymentProcessor)
                .payInvoiceNow(permissionChecker.getRole(), objectBase.getCustomer(permissionChecker.getCustomerId()),
                        payment, payment.getInvoiceObjects(objectBase, permissionChecker));
    }

    private Throwable applyCreditCard(final InvoicePayment payment, final PaymentTransaction transaction,
            final BigDecimal amount)
    {
        return InvoicePaymentProvider.get(this, paymentProcessor)
                .applyCreditCard(permissionChecker.getRole(), objectBase.getCustomer(permissionChecker.getCustomerId()),
                        payment, transaction, amount);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.freightcom.api.services.InvoiceService#pay(com.freightcom.api.
     * services.dataobjects.InvoicePayment)
     */
    @Override
    @Transactional
    public Object pay(InvoicePayment payment) throws AccessDeniedException
    {
        return payInvoice(payment.getInvoiceObjects(objectBase, permissionChecker),
                payment.getCreditObjects(objectBase, permissionChecker), payment.getPayment(), payment.getApplyCredit(),
                payment);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.freightcom.api.services.InvoiceService#autoInvoice()
     */
    @Override
    @Transactional
    public void autoInvoiceAll() throws Exception
    {
        autoInvoiceDaily();
        autoInvoicWeekly();
        autoInvoiceBiWeekly();
        autoInvoiceMonthly();
    }

    /**
     * Update last billing run date in customer for the run paramters
     */
    @Transactional
    private void updateBillingRun(Customer.AutoInvoice period, int days)
    {
        Map<String, Object> criteria = new HashMap<String, Object>();

        criteria.put("autoinvoice", period.toString());

        if (days > 0) {
            criteria.put("lastinvoicerun", new Integer(days));
        }

        for (Customer customer : customerRepository.findAll(new CustomerSpecification(criteria))) {
            customer.getCustomerBilling()
                    .setLastInvoiceRun(ZonedDateTime.now(ZoneId.of("UTC")));
        }
    }

    private void updateBillingRun(Customer.AutoInvoice period)
    {
        updateBillingRun(period, 0);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.freightcom.api.services.InvoiceService#autoInvoice()
     */
    @Override
    @Transactional
    public void autoInvoiceDaily() throws Exception
    {
        Map<String, Object> criteria = new HashMap<String, Object>();

        criteria.put("autoinvoice", Customer.AutoInvoice.DAILY.toString());
        criteria.put("invoicestatus", "unbilled charges");
        criteria.put("statusId", OrderStatusCode.DELIVERED.getValue());

        log.debug("RUNNING AUTO INVOICE DAILY");

        updateBillingRun(Customer.AutoInvoice.DAILY);
        autoInvoice(criteria);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.freightcom.api.services.InvoiceService#autoInvoice()
     */
    @Override
    @Transactional
    public void autoInvoicWeekly() throws Exception
    {
        Map<String, Object> criteria = new HashMap<String, Object>();

        criteria.put("autoinvoice", Customer.AutoInvoice.WEEKLY.toString());
        criteria.put("invoicestatus", "unbilled charges");
        criteria.put("statusId", OrderStatusCode.DELIVERED.getValue());

        log.debug("RUNNING AUTO INVOICE WEEKLY");

        updateBillingRun(Customer.AutoInvoice.WEEKLY);
        autoInvoice(criteria);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.freightcom.api.services.InvoiceService#autoInvoice()
     */
    @Override
    @Transactional
    public void autoInvoiceBiWeekly() throws Exception
    {
        Map<String, Object> criteria = new HashMap<String, Object>();

        criteria.put("autoinvoice", Customer.AutoInvoice.BIWEEKLY.toString());
        criteria.put("invoicestatus", "unbilled charges");
        criteria.put("statusId", OrderStatusCode.DELIVERED.getValue());
        criteria.put("lastInvoiceRun", new Integer(2));

        log.debug("RUNNING AUTO INVOICE BI WEEKLY");

        updateBillingRun(Customer.AutoInvoice.BIWEEKLY, 8);
        autoInvoice(criteria);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.freightcom.api.services.InvoiceService#autoInvoice()
     */
    @Override
    @Transactional
    public void autoInvoiceMonthly() throws Exception
    {
        Map<String, Object> criteria = new HashMap<String, Object>();

        criteria.put("autoinvoice", Customer.AutoInvoice.MONTHLY.toString());
        criteria.put("invoicestatus", "unbilled charges");
        criteria.put("statusId", OrderStatusCode.DELIVERED.getValue());

        log.debug("RUNNING AUTO INVOICE MONTHLY");

        updateBillingRun(Customer.AutoInvoice.MONTHLY);
        autoInvoice(criteria);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.freightcom.api.services.InvoiceService#autoInvoice()
     */
    @Transactional
    public List<Invoice> autoInvoice(final Map<String, Object> criteria) throws Exception
    {
        List<Invoice> invoices = new ArrayList<Invoice>();

        // Only invoice new orders
        criteria.put("createdatefrom", "2017-07-01");

        synchronized (invoicing) {
            for (CustomerOrder order : orderRepository.findAll(new OrderSpecification(criteria))) {
                log.debug("AUTO INVOICE " + order);

                Invoice invoice = InvoiceWrapper.get(this)
                        .setCustomer(order.getCustomer())
                        .addOrder(order)
                        .done()
                        .getInvoice();

                invoices.add(invoice);

                publishEvent(new SystemLogEvent(invoice, objectBase.getSystemRole()
                        .getUser(), "invoice auto-created", null));
            }
        }

        return invoices;
    }

    /**
     * @param invoices
     * @param credits
     * @param remaining
     * @param remainingCredit
     * @return
     * @throws AccessDeniedException
     */
    protected List<InvoiceView> payInvoice(List<Invoice> invoices, List<Credit> credits, BigDecimal paid,
            BigDecimal remainingCredit, InvoicePayment payment) throws AccessDeniedException
    {
        List<Invoice> paidInvoices = new ArrayList<Invoice>();
        BigDecimal remaining = paid;

        PaymentTransaction transaction = new PaymentTransaction();
        transaction.setAmount(paid);

        if (payment != null) {
            log.debug("HAVE PAYMENT " + payment);
            transaction.setReference(payment.getReference());
            log.debug("NEW PAYMENT TRANSACTION 2 " + payment.getReference() + " " + transaction.getReference());
            paymentProcessor.purchase(payment.getPayment(), payment, transaction);
            payment.updateTransaction(transaction);
        } else {
            log.debug("NO HAVE PAYMENT");
            transaction.setPayment(paid);
            transaction.setCredit(remainingCredit);
        }

        objectBase.saveInSubTransaction(transaction);

        if (invoices.size() > 0) {
            Customer customer = invoices.get(0)
                    .getCustomer();

            if (credits == null || credits.size() == 0) {
                // Get credits from database
                credits = objectBase.getPositiveCredits(customer.getId());
            }

            for (Invoice invoice : invoices) {
                boolean updated = false;

                if (remainingCredit == null || remainingCredit.compareTo(BigDecimal.ZERO) == 1) {
                    // Apply credits only if no limit or remaining from the
                    // limit is greater than 0
                    for (Credit credit : credits) {

                        if (!credit.getCustomerId()
                                .equals(invoice.getCustomerId())) {
                            throw new AccessDeniedException("invoice customer doesn't match credit customer");
                        }

                        if (credit.hasRemaining()) {
                            remainingCredit = invoice.applyCredit(credit, remainingCredit);
                            updated = true;
                        }
                    }
                }

                if (!invoice.fullyPaid() && remaining.compareTo(BigDecimal.ZERO) >= 0) {
                    // Payment amount can be applied
                    BigDecimal difference = remaining.subtract(invoice.amountRemaining());
                    AppliedPayment appliedPayment = new AppliedPayment();

                    appliedPayment.setPaymentTransaction(transaction);
                    appliedPayment.setInvoiceId(invoice.getId());

                    if (difference.compareTo(BigDecimal.ZERO) == 1) {
                        // Full amount
                        appliedPayment.setAmount(invoice.amountRemaining());
                        remaining = difference;
                    } else {
                        // Partial payment
                        appliedPayment.setAmount(remaining);
                        remaining = BigDecimal.ZERO;
                    }

                    invoice.applyPayment(appliedPayment);
                    updated = true;
                }

                if (updated) {
                    paidInvoices.add(invoice);
                }
            }

            // Publish events for paid invoices
            for (Invoice invoice : paidInvoices) {
                publishEvent(new InvoicePaidEvent(invoice, transaction, permissionChecker.getRole()));
                publishEvent(new SystemLogEvent(invoice, permissionChecker.getUser(), "invoice paid", null));
            }

            if (remaining.compareTo(BigDecimal.ZERO) == 1) {
                // There is money remaining from the payment
                Credit credit = new Credit();

                credit.setAmount(remaining);
                credit.setAmountRemaining(remaining);
                credit.setCustomerId(customer.getId());
                credit.setNote("Amount remaining after payment");

                objectBase.save(credit);
            }
        }

        if (payment != null && payment.getCreditCardPayment()) {
            applyCreditCard(payment, transaction, paid);
        }

        return paidInvoices.stream()
                .map(invoice -> getInvoiceView(invoice))
                .collect(Collectors.toList());
    }

    /**
     *
     */
    @Override
    public List<DeletedInvoice> deletedInvoices()
    {
        return objectBase.deletedInvoices(null);
    }

    private InvoiceView getInvoiceView(Invoice invoice)
    {
        return (new InvoiceView(invoice));
    }

    private InvoiceIndividualView getIndividualInvoiceView(Invoice invoice)
    {
        log.debug("TO GET THE MESSAGES FOR THE INVOICE " + invoice);
        invoice.setMessages(getInvoiceMessages(invoice));
        log.debug("TO HAVE THE MESSAGES FOR THE INVOICE " + invoice.getMessages());

        if (permissionChecker.isFreightcomOrAdmin()) {
            log.debug("TO GET THE ADMIN GROUPED CHARGES");
            return (new InvoiceIndividualView(invoice, permissionChecker.getRole()))
                    .setGroupedCharges(objectBase.groupedCharges(invoice))
                    .setGroupedTaxes(objectBase.groupedTaxes(invoice));
        } else {
            log.debug("TO GET THE CUSTOMER GROUPED CHARGES");
            InvoiceIndividualView view = new InvoiceIndividualView(invoice, permissionChecker.getRole());

            view.setGroupedCharges(objectBase.groupedChargesCustomer(invoice));
            view.setGroupedTaxes(objectBase.groupedTaxes(invoice));

            return view;
        }
    }

    @Override
    public Object calculateCharges(Stream<Long> invoiceIds) throws Exception
    {
        Customer customer = null;
        List<Invoice> invoices = new ArrayList<Invoice>();

        for (Long id : (Iterable<Long>) invoiceIds::iterator) {
            Invoice invoice = findOne(id);

            if (!invoice.isPaid()) {
                if (customer == null) {
                    customer = invoice.getCustomer();
                } else if (customer != invoice.getCustomer()) {
                    throw new ReportableError("Please select only one customer's invoices.");
                }

                invoices.add(invoice);
            }
        }

        return new Charges(customer, invoices);
    }

    @Override
    public Object viewApplyCredit(Long id)
    {
        Map<String, Object> view = new HashMap<String, Object>();

        Invoice invoice = findOne(id);

        view.put("totalApplied", invoice.getPaidAmount()
                .add(invoice.getCreditedAmount()));
        view.put("amountRemaining", invoice.amountRemaining());
        view.put("amount", invoice.getAmount());

        BigDecimal creditAvailable = invoice.getCustomer()
                .getCreditAvailable(invoice.getCurrency());

        BigDecimal newAmountDue = invoice.getAmount()
                .subtract(creditAvailable);

        view.put("creditAvailable", creditAvailable);
        view.put("newAmountDue", newAmountDue.compareTo(BigDecimal.ZERO) <= 0 ? BigDecimal.ZERO : newAmountDue);

        return view;
    }
}
