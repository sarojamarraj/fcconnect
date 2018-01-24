package com.freightcom.api.repositories;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.freightcom.api.model.AccessorialServices;
import com.freightcom.api.model.Agent;
import com.freightcom.api.model.ApplicableTax;
import com.freightcom.api.model.Carrier;
import com.freightcom.api.model.Charge;
import com.freightcom.api.model.CommissionPayable;
import com.freightcom.api.model.Credit;
import com.freightcom.api.model.CreditCard;
import com.freightcom.api.model.Currency;
import com.freightcom.api.model.Customer;
import com.freightcom.api.model.CustomerOrder;
import com.freightcom.api.model.CustomerStaff;
import com.freightcom.api.model.Customer_;
import com.freightcom.api.model.DeletedInvoice;
import com.freightcom.api.model.GroupedCharge;
import com.freightcom.api.model.GroupedTax;
import com.freightcom.api.model.Invoice;
import com.freightcom.api.model.LoggedEvent;
import com.freightcom.api.model.OrderAccessorials;
import com.freightcom.api.model.OrderDocument;
import com.freightcom.api.model.OrderStatus;
import com.freightcom.api.model.PackagePreference;
import com.freightcom.api.model.PalletTemplate;
import com.freightcom.api.model.PalletType;
import com.freightcom.api.model.Payable;
import com.freightcom.api.model.Service;
import com.freightcom.api.model.TransactionalEntity;
import com.freightcom.api.model.User;
import com.freightcom.api.model.UserRole;
import com.freightcom.api.model.UserRole_;
import com.freightcom.api.model.views.CustomerOption;
import com.freightcom.api.model.views.View;
import com.freightcom.api.repositories.custom.CustomerSpecification;
import com.freightcom.api.repositories.custom.OrderSpecification;
import com.freightcom.api.repositories.custom.UserRoleSpecification;

@Repository
public class ObjectBaseImpl implements ObjectBase
{
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Carrier getCarrier(Object id)
    {
        return lookup(Carrier.class, id);
    }

    @Override
    public CustomerOrder getOrder(Object id)
    {
        return lookup(CustomerOrder.class, id);
    }

    @Override
    public void delete(TransactionalEntity object)
    {
        entityManager.remove(object);
    }

    @Override
    public void refresh(TransactionalEntity object)
    {
        entityManager.refresh(object);
    }

    @Override
    public Customer getCustomer(Object id)
    {
        return lookup(Customer.class, id);
    }

    @Override
    public User getUser(Object id)
    {
        return lookup(User.class, id);
    }

    @Override
    public Currency lookupCurrency(String name)
    {
        TypedQuery<Currency> query = entityManager.createQuery("select C from Currency as C where C.name=:name",
                Currency.class);

        try {
            return query.setParameter("name", name)
                    .getSingleResult();
        } catch (Exception e) {
            // Ignore exception, assume invalid currency
            return null;
        }
    }

    @Override
    public List<Currency> listCurrencies()
    {
        TypedQuery<Currency> query = entityManager.createQuery("select C from Currency as C", Currency.class);

        return query.getResultList();
    }

    @Override
    public User getUserByName(String name)
    {
        TypedQuery<User> query = entityManager
                .createQuery("select U from User U where login=:name and U.deletedAt is null", User.class);

        List<User> users = query.setParameter("name", name)
                .getResultList();

        if (users.size() == 1) {
            return users.get(0);
        } else {
            return null;
        }
    }

    @Override
    public List<User> getUsersByCustomer(Customer customer)
    {
        TypedQuery<User> query = entityManager.createQuery(
                "select U from User U left join UserRole UR.user where (exists (select CA from CustomerAdmin CA where CA.id=UR.id and CA.customer.id=:customerId) or exists (select CS from CustomerStaff CS where CS.id=UR.id and CA.customer.id=:customerId)) and U.deletedAt is null order by concat(U.lastname, ',', U.firstname)",
                User.class);

        return query.setParameter("customerId", customer.getId())
                .getResultList();
    }

    @Override
    @Transactional
    public <T extends TransactionalEntity> T save(T object)
    {
        try {
            log.debug("SAVING OBJECT " + object);

            entityManager.persist(object);

            return object;
        } finally {
            log.debug("DONE SAVING OBJECT " + object);
        }
    }

    @Override
    @Transactional
    public void lock(TransactionalEntity object)
    {
        entityManager.lock(object, LockModeType.WRITE);
    }

    @Override
    @Transactional
    public void flush()
    {
        entityManager.flush();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveInSubTransaction(TransactionalEntity object)
    {
        entityManager.persist(object);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.freightcom.api.repositories.ObjectBase#getInvoice(java.lang.Object)
     */
    @Override
    public Invoice getInvoice(Object id)
    {
        return lookup(Invoice.class, id);
    }

    @Override
    public Credit getCredit(Object id)
    {
        return lookup(Credit.class, id);
    }

    @Override
    public CreditCard getCreditCard(Object id)
    {
        return lookup(CreditCard.class, id);
    }

    /**
     * @param entityClass
     * @param id
     * @return
     */
    private <T> T lookup(Class<T> entityClass, Object id)
    {
        T entity = null;

        if (id == null) {
            // Returning null
        } else if (id instanceof Long) {
            entity = entityManager.find(entityClass, (Long) id);
        } else {
            entity = entityManager.find(entityClass, new Long(id.toString()));
        }

        return entity;
    }

    @Override
    public Agent getAgent(Object subAgentId)
    {
        UserRole role = lookup(UserRole.class, subAgentId);

        return role == null ? null : role.asAgent();
    }

    @Override
    public List<UserRole> getAgents()
    {
        TypedQuery<UserRole> query = entityManager
                .createQuery("select A from UserRole A where TYPE(A)='AGENT' and  A.deletedAt is null", UserRole.class);

        return query.getResultList();
    }

    @Override
    public OrderStatus getOrderStatus(Object statusId)
    {
        return lookup(OrderStatus.class, statusId);
    }

    @Override
    public OrderAccessorials getOrderAccessorial(Object accessorialId)
    {
        return lookup(OrderAccessorials.class, accessorialId);
    }

    @Override
    public AccessorialServices getAccessorialServices(Object accessorialId)
    {
        return lookup(AccessorialServices.class, accessorialId);
    }

    @Override
    public List<LoggedEvent> getOrderLoggedEvents(CustomerOrder order)
    {
        TypedQuery<LoggedEvent> query = entityManager.createQuery(
                "select LOG from LoggedEvent as LOG where LOG.entityId=:id and LOG.entityType='order' order by createdAt",
                LoggedEvent.class);

        return query.setParameter("id", order.getId()
                .toString())
                .getResultList();
    }

    @Override
    public List<LoggedEvent> getInvoiceLoggedEvents(Invoice invoice, boolean showPrivate)
    {
        List<Long> orderIds = invoice.getOrders()
                .stream()
                .map(order -> order.getId())
                .collect(Collectors.toList());

        TypedQuery<LoggedEvent> query;

        if (orderIds.size() < 1) {
            return new ArrayList<LoggedEvent>(0);
        } else {
            if (showPrivate) {
                query = entityManager.createQuery(
                        "select LOG from LoggedEvent as LOG where LOG.messageType = 'invoice' and LOG.entityId in (:ids) and LOG.entityType='order' order by createdAt",
                        LoggedEvent.class);
            } else {
                query = entityManager.createQuery(
                        "select LOG from LoggedEvent as LOG where LOG.messageType = 'invoice' and LOG.entityId in (:ids) and LOG.entityType='order' and LOG.nPrivate=0 order by createdAt",
                        LoggedEvent.class);
            }

            return query.setParameter("ids", orderIds)
                    .getResultList();
        }
    }

    @Override
    public LoggedEvent getLatestLoggedEvent(CustomerOrder order, boolean includePrivate)
    {
        TypedQuery<LoggedEvent> query;

        if (includePrivate) {
            query = entityManager.createQuery(
                    "select LOG from LoggedEvent as LOG where LOG.entityId=:id and LOG.entityType='order' order by createdAt desc",
                    LoggedEvent.class);

        } else {
            query = entityManager.createQuery(
                    "select LOG from LoggedEvent as LOG where LOG.nPrivate = false and LOG.entityId=:id and LOG.entityType='order' order by createdAt desc",
                    LoggedEvent.class);
        }

        List<LoggedEvent> results = query.setParameter("id", order.getId())
                .setMaxResults(1)
                .getResultList();

        if (results.size() == 1) {
            return results.get(0);
        } else {
            return null;
        }

    }

    @Override
    public List<Credit> getPositiveCredits(Long customerId)
    {
        TypedQuery<Credit> query = entityManager.createQuery(
                "select C from Credit as C where C.customerId=:id and C.amountRemaining > 0 order by createdAt",
                Credit.class);

        return query.setParameter("id", customerId)
                .getResultList();
    }

    @Override
    public Service getService(Object serviceId)
    {
        return lookup(Service.class, serviceId);
    }

    @Override
    public List<Service> getCarrierServices(Carrier carrier)
    {
        TypedQuery<Service> query = entityManager.createQuery(
                "select S from Service as S where S.carrier_id=:carrierId and discontinued=0", Service.class);

        return query.setParameter("carrierId", carrier.getId()
                .toString())
                .getResultList();
    }

    @Override
    public List<Service> getOrderedServices()
    {
        TypedQuery<Service> query = entityManager
                .createQuery("select S from Service as S where discontinued=0 order by name", Service.class);

        return query.getResultList();
    }

    @Override
    public User getUserByLogin(String login)
    {
        TypedQuery<User> query = entityManager
                .createQuery("select U from User as U where U.login = :login and deleted_at is null", User.class);

        try {
            return query.setParameter("login", login)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<CustomerStaff> getStaff(Customer customer)
    {
        TypedQuery<CustomerStaff> query = entityManager
                .createQuery("select C from CustomerStaff as C where C.staffCustomerId=:id", CustomerStaff.class);

        return query.setParameter("id", customer.getId())
                .getResultList();
    }

    @Override
    public List<DeletedInvoice> deletedInvoices(Customer customer)
    {
        log.debug("DELETED INVOICES");
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<DeletedInvoice> builderQuery = cb.createQuery(DeletedInvoice.class);
        Root<DeletedInvoice> deletedInvoice = builderQuery.from(DeletedInvoice.class);
        builderQuery.select(deletedInvoice)
                .where(cb.isNotNull(deletedInvoice.get("deletedAt")));

        TypedQuery<DeletedInvoice> query = entityManager.createQuery(builderQuery)
                .setMaxResults(10);

        return query.getResultList();
    }

    @Override
    public List<GroupedCharge> groupedCharges(Invoice invoice)
    {
        TypedQuery<GroupedCharge> query = entityManager.createQuery(
                "select new com.freightcom.api.model.GroupedCharge(1, AC.description, sum(IFNULL(AC.quantity,0) * IFNULL(AC.charge, 0)) as total, (select ROUND(sum(CT.amount),2) from ChargeTax CT where CT.charge.id=AC.id) as totalTax, sum(AC.cost) as cost) from Charge AC where AC.invoiceId = :invoiceId group by description",
                GroupedCharge.class);

        return query.setParameter("invoiceId", invoice.getId())
                .getResultList();
    }

    @Override
    public List<GroupedCharge> groupedChargesCustomer(Invoice invoice)
    {
        try {
            TypedQuery<GroupedCharge> query = entityManager.createQuery(
                    "select new com.freightcom.api.model.GroupedCharge(1, AC.description, sum(IFNULL(AC.quantity,0) * IFNULL(AC.charge, 0)) as total, (select ROUND(sum(CT.amount),2) from ChargeTax CT where CT.charge.id=AC.id) as totalTax, 0 as cost) from Charge AC where AC.invoiceId = :invoiceId group by description",
                    GroupedCharge.class);

            return query.setParameter("invoiceId", invoice.getId())
                    .getResultList();
        } catch (Exception e) {
            log.debug("GROUPED CHARGES CUSTOMER EXCEPTION " + e);
            throw e;
        }
    }

    @Override
    public List<GroupedTax> groupedTaxes(Invoice invoice)
    {
        TypedQuery<GroupedTax> query = entityManager.createQuery(
                "select new com.freightcom.api.model.GroupedTax(TX.name, sum(TX.amount) as total) from Charge AC left join AC.taxes TX where AC.invoiceId = :invoiceId  group by TX.name",
                GroupedTax.class);

        return query.setParameter("invoiceId", invoice.getId())
                .getResultList();
    }

    @Override
    public Charge getCharge(Long id)
    {
        return lookup(Charge.class, id);
    }

    private static final String UNREPORTED_COMMISSION = "select CH from Charge CH where not exists (select CC from ChargeCommission CC where CC.charge.id = CH.id and not exists (select CP from CommissionPayable CP where CP.id = CC.commissionPayable.id and CP.agent.id = :agentId)) and exists (select O from CustomerOrder O where O.id = CH.order.id and 1 = check_subagent(:agentId, O.agent.id))";

    @Override
    public List<Charge> chargesWithUnreportedCommission(UserRole agent)
    {
        TypedQuery<Charge> query = entityManager.createQuery(UNREPORTED_COMMISSION, Charge.class);

        return query.setParameter("agentId", agent.getId())
                .getResultList();

    }

    private static final String SERVICE_WITH_UNBILLED_CHARGES = "select SERV from Service SERV where SERV.deletedAt is null and SERV.term=:term and exists (select CH from Charge CH where CH.service=SERV and CH.reportedAt is null and CH.createdAt >= :startDate)";
    private static final String SERVICE_WITH_UNBILLED_CHARGES_NO_TERM = "select SERV from Service SERV where SERV.deletedAt is null and exists (select CH from Charge CH where CH.service=SERV and CH.reportedAt is null and CH.createdAt >= :startDate)";

    @Override
    public List<Service> servicesWithUnbilledCharges()
    {
        Date threeMonthsAgo = Date.from(ZonedDateTime.now()
                .minus(3, ChronoUnit.MONTHS)
                .toInstant());

        TypedQuery<Service> query = entityManager.createQuery(SERVICE_WITH_UNBILLED_CHARGES_NO_TERM, Service.class);

        return query.setParameter("startDate", threeMonthsAgo)
                .getResultList();
    }

    @Override
    public List<Service> servicesWithUnbilledCharges(Service.Term term)
    {
        Date threeMonthsAgo = Date.from(ZonedDateTime.now()
                .minus(3, ChronoUnit.MONTHS)
                .toInstant());

        TypedQuery<Service> query = entityManager.createQuery(SERVICE_WITH_UNBILLED_CHARGES, Service.class);

        return query.setParameter("startDate", threeMonthsAgo)
                .setParameter("term", term)
                .getResultList();
    }

    private static final String SERVICE_UNBILLED_CHARGES = "select CH from Charge CH where CH.service.id=:serviceId and CH.reportedAt is null";

    @Override
    public List<Charge> serviceUnbilledCharges(Service service)
    {
        TypedQuery<Charge> query = entityManager.createQuery(SERVICE_UNBILLED_CHARGES, Charge.class);

        return query.setParameter("serviceId", service.getId())
                .getResultList();
    }

    @Override
    public List<GroupedCharge> groupedCharges(Payable payable)
    {
        TypedQuery<GroupedCharge> query = entityManager.createQuery(
                "select new com.freightcom.api.model.GroupedCharge(1, AC.description, sum(IFNULL(AC.quantity,0) * IFNULL(AC.charge, 0)) as total, (select ROUND(sum(CT.amount),2) from ChargeTax CT where CT.charge.id=AC.id) as totalTax, sum(AC.cost) as cost) from Charge AC where AC.payable = :payable group by  description",
                GroupedCharge.class);

        return query.setParameter("payable", payable)
                .getResultList();
    }

    private static final String COMMISSION_PAYABLE_CHARGES = "select CH from Charge CH where CH.commissionCalculatedAt is null and CH.invoiceId is not null and CH.agent.id is not null order by root_agent_id(CH.agent.id)";

    @Override
    public List<Charge> commissionPayableCharges()
    {
        TypedQuery<Charge> query = entityManager.createQuery(COMMISSION_PAYABLE_CHARGES, Charge.class);

        return query.getResultList();
    }

    private static final String COMMISSION_PAYABLE_CHARGES_TERM = "select CH from Charge CH where CH.commissionCalculatedAt is null and CH.invoiceId is not null and CH.agent.id is not null and CH.agent.term=:term order by root_agent_id(CH.agent.id)";

    @Override
    public List<Charge> commissionPayableCharges(Agent.Term term)
    {
        TypedQuery<Charge> query = entityManager.createQuery(COMMISSION_PAYABLE_CHARGES_TERM, Charge.class);

        return query.setParameter("term", term)
                .getResultList();
    }

    private static final String COMMISSION_PAYABLE_CHARGES_AGENT = "select CH from Charge CH where (CH.agent.id=:agentId or 1 = check_subagent(CH.agent.parentSalesAgent.id, :agentId)) and CH.commissionCalculatedAt is null and CH.invoiceId is not null and CH.agent.id is not null order by CH.agent.id";

    @Override
    public List<Charge> commissionPayableCharges(Long agentId)
    {
        if (agentId == null) {
            return commissionPayableCharges();
        }

        TypedQuery<Charge> query = entityManager.createQuery(COMMISSION_PAYABLE_CHARGES_AGENT, Charge.class);

        return query.setParameter("agentId", agentId)
                .getResultList();
    }

    @Override
    public Long orderQueryCounts(OrderSpecification specification)
    {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> builderQuery = builder.createQuery(Long.class);
        Root<CustomerOrder> order = builderQuery.from(CustomerOrder.class);

        builderQuery.select(builder.count(order));
        builderQuery.where(specification.toPredicate(order, builderQuery, builder));

        TypedQuery<Long> query = entityManager.createQuery(builderQuery);

        return query.getSingleResult();
    }

    @Override
    public List<CustomerOrder> orderQuery(OrderSpecification specification, Pageable page)
    {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<CustomerOrder> builderQuery = builder.createQuery(CustomerOrder.class);
        Root<CustomerOrder> order = builderQuery.from(CustomerOrder.class);

        builderQuery.select(order);
        builderQuery.where(specification.toPredicate(order, builderQuery, builder));

        if (page.getSort() != null) {
            List<Order> orderList = new ArrayList<Order>();

            for (Sort.Order sorder : page.getSort()) {
                log.debug("SORT ORDER " + sorder.getDirection() + " " + sorder.getProperty() + " "
                        + sorder.isAscending());
                Path<Object> propertyExpression;

                switch (sorder.getProperty()) {
                case "statusId":
                case "status":
                case "statusName":
                    propertyExpression = order.get("orderStatus")
                            .get("id");
                    break;

                default:
                    propertyExpression = splitProperty(sorder.getProperty(), order);
                    break;
                }

                if (sorder.getDirection() == Sort.Direction.ASC) {
                    orderList.add(builder.asc(propertyExpression));
                } else {
                    orderList.add(builder.desc(propertyExpression));
                }
            }

            builderQuery.orderBy(orderList);
        }

        TypedQuery<CustomerOrder> query = entityManager.createQuery(builderQuery);

        query.setMaxResults(page.getPageSize() + 1);
        query.setFirstResult(page.getOffset());

        return query.getResultList();
    }

    private Path<Object> splitProperty(String path, Root<?> root)
    {
        Path<Object> result = null;

        for (String name : path.split("\\.")) {
            if (result == null) {
                result = root.get(name);
            } else {
                result = result.get(name);
            }
        }

        return result;
    }

    @Override
    public Page<UserRole> findAgents(UserRoleSpecification specification, Pageable page)
    {
        specification.put("type", "agent");
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
        Root<UserRole> userRole = countQuery.from(UserRole.class);
        countQuery.select(builder.countDistinct(userRole.get(UserRole_.id)));
        countQuery.where(specification.toPredicate(userRole, countQuery, builder));

        Long count = entityManager.createQuery(countQuery)
                .getSingleResult();

        log.debug("AGENT ENTITY COUNT " + count);

        CriteriaQuery<UserRole> builderQuery = builder.createQuery(UserRole.class);
        Root<UserRole> userRoleRoot = builderQuery.from(UserRole.class);

        builderQuery.select(userRoleRoot);
        builderQuery.where(specification.toPredicate(userRoleRoot, builderQuery, builder));

        if (page.getSort() != null) {
            List<Order> orderList = new ArrayList<Order>();

            for (Sort.Order sorder : page.getSort()) {
                log.debug("SORT ORDER " + sorder.getDirection() + " " + sorder.getProperty() + " "
                        + sorder.isAscending());
                Path<Object> propertyExpression;

                switch (sorder.getProperty()) {
                case "parentSalesAgentName":
                case "unpaidCommission":
                case "paidCommission":
                case "commissionPercent":
                case "customerCount":
                    propertyExpression = builder.treat(userRoleRoot, Agent.class)
                            .get(sorder.getProperty());
                    break;

                default:
                    propertyExpression = userRoleRoot.get(sorder.getProperty());
                    break;
                }

                if (sorder.getDirection() == Sort.Direction.ASC) {
                    orderList.add(builder.asc(propertyExpression));
                } else {
                    orderList.add(builder.desc(propertyExpression));
                }
            }

            builderQuery.orderBy(orderList);
        }

        TypedQuery<UserRole> query = entityManager.createQuery(builderQuery);

        query.setMaxResults(page.getPageSize());
        query.setFirstResult(page.getOffset());

        return new PageImpl<UserRole>(query.getResultList(), page, count);
    }

    @Override
    public List<GroupedCharge> groupedCharges(CommissionPayable commissionPayable)
    {
        TypedQuery<GroupedCharge> query = entityManager.createQuery(
                "select new com.freightcom.api.model.GroupedCharge(1, CC.charge.description, sum(IFNULL(CC.charge.quantity,0) * IFNULL(CC.charge.charge, 0)) as total, (select ROUND(sum(CT.amount),2) from ChargeTax CT where CT.charge.id=CC.charge.id) as totalTax, sum(CC.charge.cost) as cost) from ChargeCommission CC where CC.commissionPayable.id=:commissionPayable group by description",
                GroupedCharge.class);

        return query.setParameter("commissionPayable", commissionPayable)
                .getResultList();
    }

    @Override
    public Service getService(Long serviceId)
    {
        return lookup(Service.class, serviceId);
    }

    @Override
    public OrderDocument getOrderDocument(Long documentId)
    {
        return lookup(OrderDocument.class, documentId);
    }

    @Override
    public PackagePreference getPackagePreference(Object id)
    {
        return lookup(PackagePreference.class, id);
    }

    @Override
    public UserRole getSystemRole()
    {
        // TODO - real system account
        User user = getUserByName("admin");
        Collection<UserRole> roles = user.getAuthorities();
        return roles.iterator()
                .next();

    }

    @Override
    public UserRole lookupRole(Object id)
    {
        return lookup(UserRole.class, id);
    }

    public int customerBillingRun(Customer.AutoInvoice period, int days)
    {
        Query query = entityManager
                .createNativeQuery("update customer_billing set last_invoice_run=now() where customer_id in (select *");

        return query.executeUpdate();
    }

    @Override
    public Page<View> findCustomerOptions(CustomerSpecification specification, Pageable page)
    {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
        countQuery.select(builder.countDistinct(countQuery.from(Agent.class)));
        countQuery.where(specification.toPredicate(countQuery.from(Customer.class), countQuery, builder));

        Long count = entityManager.createQuery(countQuery)
                .getSingleResult();

        CriteriaQuery<CustomerOption> builderQuery = builder.createQuery(CustomerOption.class);
        Root<Customer> customerRoot = builderQuery.from(Customer.class);

        builderQuery.multiselect(customerRoot.get(Customer_.id), customerRoot.get(Customer_.name));
        builderQuery.where(specification.toPredicate(customerRoot, builderQuery, builder));

        if (page.getSort() != null) {
            List<Order> orderList = new ArrayList<Order>();

            for (Sort.Order sorder : page.getSort()) {
                Path<Object> propertyExpression = customerRoot.get(sorder.getProperty());

                if (sorder.getDirection() == Sort.Direction.ASC) {
                    orderList.add(builder.asc(propertyExpression));
                } else {
                    orderList.add(builder.desc(propertyExpression));
                }
            }

            builderQuery.orderBy(orderList);
        }

        TypedQuery<CustomerOption> query = entityManager.createQuery(builderQuery);

        query.setMaxResults(page.getPageSize());
        query.setFirstResult(page.getOffset());

        List<View> results = new ArrayList<View>();

        for (CustomerOption option : query.getResultList()) {
            results.add(option);
        }

        return new PageImpl<View>(results, page, count);
    }

    @Override
    @Cacheable(cacheNames = "functions")
    public Carrier getCarrierByName(String name)
    {
        TypedQuery<Carrier> query = entityManager
                .createQuery("select A from Carrier A where A.name=:name and  A.deletedAt is null", Carrier.class);

        return query.setParameter("name", name)
                .getSingleResult();
    }

    @Override
    public Carrier getCarrierByNameNoCache(String name)
    {
        TypedQuery<Carrier> query = entityManager
                .createQuery("select A from Carrier A where A.name=:name and  A.deletedAt is null", Carrier.class);

        return query.setParameter("name", name)
                .getSingleResult();
    }

    @Override
    public EntityManager getEntityManager()
    {
        return entityManager;
    }

    @Override
    public Boolean hasAddressBook()
    {
        return entityManager.createQuery("select id from AddressBook where deletedAt is null")
                .setMaxResults(1)
                .getResultList()
                .size() > 0;
    }

    @Override
    public Boolean hasAddressBook(Long customerId)
    {
        if (customerId == null) {
            return false;
        }

        return entityManager
                .createQuery("select id from AddressBook where deletedAt is null and customerId=:customerId")
                .setMaxResults(1)
                .setParameter("customerId", customerId)
                .getResultList()
                .size() > 0;
    }

    @Override
    public List<PalletTemplate> getPalletTemplates(Customer customer)
    {
        TypedQuery<PalletTemplate> query = entityManager.createQuery(
                "select PT from PalletTemplate PT where deletedAt is null and customer=:customer order by name",
                PalletTemplate.class);

        return query.setParameter("customer", customer)
                .getResultList();

    }

    @Override
    public PalletTemplate getPalletTemplate(Object id)
    {
        return lookup(PalletTemplate.class, id);
    }

    @Override
    public PalletType getPalletType(Object id)
    {
        return lookup(PalletType.class, id);
    }

    @Override
    public List<PalletType> getPalletTypes()
    {
        TypedQuery<PalletType> query = entityManager
                .createQuery("select PT from PalletType PT where deletedAt is null order by id", PalletType.class);

        return query.getResultList();

    }

    @Override
    public CommissionPayable getCommissionPayable(Object id)
    {
        return lookup(CommissionPayable.class, id);
    }

    @Override
    public Payable getPayable(Object id)
    {
        return lookup(Payable.class, id);
    }

    @Override
    public ApplicableTax getApplicableTax(Object id)
    {
        return lookup(ApplicableTax.class, id);
    }
}
