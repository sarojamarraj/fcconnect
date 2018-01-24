package com.freightcom.api.repositories.custom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.springframework.data.jpa.domain.Specification;

import com.freightcom.api.ReportableError;
import com.freightcom.api.model.Customer;
import com.freightcom.api.model.CustomerOrder;
import com.freightcom.api.model.CustomerOrder_;
import com.freightcom.api.model.Invoice;
import com.freightcom.api.model.Invoice_;

public class InvoiceSpecification extends BaseSpecification implements Specification<Invoice>
{
    private final Map<String, Object> criteria;

    public InvoiceSpecification(final Map<String, Object> criteria2)
    {
        this.criteria = criteria2;
    }

    /**
     * Need a back end support for the following filters paymentStatus: '', 1, 0
     *
     * dueDate: today, custom dueDateFrom(Y-m-d), dueDateTo(Y-m-d)
     * dateGenerated: today, custom dateGeneratedFrom(Y-m-d),
     * dateGeneratedTo(Y-m-d)
     */

    @Override
    public Predicate toPredicate(Root<Invoice> root, CriteriaQuery<?> query, CriteriaBuilder builder)
    {
        Predicate predicate = builder.conjunction();
        for (String key : criteria.keySet()) {

            switch (key.toLowerCase()) {

            case "id":
                addLong(predicate, "id", criteria.get(key), root, builder);
                break;

            case "paymentstatus":
                if (yesValue(criteria.get(key))) {
                    predicate.getExpressions()
                            .add(builder.equal(root.get("paymentStatus"), builder.literal(1)));
                } else {
                    predicate.getExpressions()
                            .add(builder.equal(root.get("paymentStatus"), builder.literal(0)));
                }
                break;

            case "deleted_at":
                addLong(predicate, "deletedAt", criteria.get(key), root, builder);
                break;

            case "status":
                addString(predicate, "status", criteria.get(key), root, builder);
                break;

            case "notcancelled":
                predicate.getExpressions()
                        .add(notCancelled(root, query, builder));
                break;

            case "duedate":
            case "dueat":
                addDate(predicate, "dueAt", criteria.get(key), root, builder);
                break;

            case "duedatefrom":
            case "dueatfrom":
                addDateFrom(predicate, "dueAt", criteria.get(key), root, builder);
                break;

            case "duedateto":
            case "dueatto":
                addDateTo(predicate, "dueAt", criteria.get(key), root, builder);
                break;

            case "dategenerated":
            case "createdat":
            case "datecreated":
                addDate(predicate, "createdAt", criteria.get(key), root, builder);
                break;

            case "dategeneratedfrom":
            case "createdatfrom":
            case "createdatefrom":
            case "datecreatedfrom":
                addDateFrom(predicate, "createdAt", criteria.get(key), root, builder);
                break;

            case "dategeneratedto":
            case "createdatto":
            case "datecreatedto":
                addDateTo(predicate, "createdAt", criteria.get(key), root, builder);
                break;

            case "customerid":
                addLongId(predicate, "customer", criteria.get(key), root, builder);
                break;

            case "orderid":
            case "workorderid":
                predicate.getExpressions()
                        .add(hasOrderId(criteria.get(key), root, query, builder));
                break;

            case "agentid":
                predicate.getExpressions()
                        .add(hasAgentId(criteria.get(key), root, query, builder));
                break;

            case "bol":
            case "bolid":
                predicate.getExpressions()
                        .add(hasBOL(criteria.get(key), root, query, builder));
                break;

            case "trackingnumber":
                predicate.getExpressions()
                        .add(hasTrackingNumber(criteria.get(key), root, query, builder));
                break;

            case "referencecode":
                predicate.getExpressions()
                        .add(hasReferenceCode(criteria.get(key), root, query, builder));
                break;

            case "cctransactionnumber":
            case "creditcardtransactionnumber":
                predicate.getExpressions().add(hasCCTransaction(criteria.get(key), root, query, builder));
                break;

            case "sort":
            case "size":
            case "packagetypename":
            case "page":
                // Ok
                break;

            default:
                throw new ReportableError("Invalid parameter " + key);
            }
        }

        return predicate;
    }

    /**
     *
     *
     */
    private Predicate notCancelled(Root<Invoice> root, CriteriaQuery<?> query, CriteriaBuilder builder)
    {
        Predicate predicate = builder.disjunction();

        predicate.getExpressions()
            .add(builder.isNull(root.get(Invoice_.status)));

        predicate.getExpressions()
            .add(builder.notEqual(root.get(Invoice_.status), builder.literal("cancelled")));

        return predicate;
    }

    /**
     *
     *
     */
    private Predicate hasAgentId(Object agentId, Root<Invoice> root, CriteriaQuery<?> query, CriteriaBuilder builder)
    {
        Predicate predicate = builder.disjunction();
        Subquery<Customer> customerQuery = query.subquery(Customer.class);
        Root<Customer> customer = customerQuery.from(Customer.class);

        customerQuery.select(customer);

        List<Predicate> predicates = new ArrayList<Predicate>();
        predicates.add(builder.equal(customer.get("id"), root.get("customer")
                .get("id")));
        predicates.add(builder.isTrue(builder.function("check_subagent", Boolean.class, customer.get("salesAgent").get("id"),
                builder.literal(agentId))));

        customerQuery.where(predicates.toArray(new Predicate[] {}));

        predicate.getExpressions()
                .add(builder.exists(customerQuery));

        return predicate;
    }

    /**
     * exists (select o from Order o JOIN o.invoices i where o.trackingNumber =
     * :track)
     *
     */
    private Predicate hasReferenceCode(Object codes, Root<Invoice> root, CriteriaQuery<?> query,
                                       CriteriaBuilder builder)
    {
        Predicate predicate = builder.disjunction();
        Join<Invoice,CustomerOrder> orders = root.join(Invoice_.orders);

        predicate.getExpressions()
            .add(orders.get(CustomerOrder_.referenceCode).in((Object[]) ((String) codes).split("\\s*,\\s*")));

        return predicate;
    }

    /**
     * exists (select o from Order o JOIN o.invoices i where o.trackingNumber =
     * :track)
     *
     */
    private Predicate hasTrackingNumber(Object trackingNumber, Root<Invoice> root, CriteriaQuery<?> query,
            CriteriaBuilder builder)
    {
        Predicate predicate = builder.disjunction();
        Subquery<CustomerOrder> orderQuery = query.subquery(CustomerOrder.class);

        Root<CustomerOrder> customerOrder = orderQuery.from(CustomerOrder.class);
        Join<Invoice, CustomerOrder> invoices = customerOrder.join("invoices");
        orderQuery.select(customerOrder);

        List<Predicate> predicates = new ArrayList<Predicate>();
        predicates.add(builder.equal(invoices.get("id"), root.get("id")));
        predicates.add(builder.in(customerOrder.get("masterTrackingNum"))
                .value(Arrays.asList(trackingNumber.toString()
                        .split("\\s*,\\s*"))));

        orderQuery.where(predicates.toArray(new Predicate[] {}));

        predicate.getExpressions()
                .add(builder.exists(orderQuery));

        return predicate;
    }

    /**
     *
     *
     */
    private Predicate hasBOL(Object bol, Root<Invoice> root, CriteriaQuery<?> query, CriteriaBuilder builder)
    {
        Predicate predicate = builder.disjunction();
        Subquery<CustomerOrder> orderQuery = query.subquery(CustomerOrder.class);

        Root<CustomerOrder> customerOrder = orderQuery.from(CustomerOrder.class);
        Join<Invoice, CustomerOrder> invoices = customerOrder.join("invoices");
        orderQuery.select(customerOrder);

        List<Predicate> predicates = new ArrayList<Predicate>();
        predicates.add(builder.equal(invoices.get("id"), root.get("id")));
        predicates.add(builder.in(customerOrder.get("bolId"))
                .value(Arrays.asList(bol.toString()
                        .split("\\s*,\\s*"))));

        orderQuery.where(predicates.toArray(new Predicate[] {}));

        predicate.getExpressions()
                .add(builder.exists(orderQuery));

        return predicate;
    }

    /**
     *
     *
     */
    private Predicate hasOrderId(Object orderId, Root<Invoice> root, CriteriaQuery<?> query, CriteriaBuilder builder)
    {
        Predicate predicate = builder.disjunction();
        Subquery<CustomerOrder> orderQuery = query.subquery(CustomerOrder.class);

        Root<CustomerOrder> customerOrder = orderQuery.from(CustomerOrder.class);
        Join<Invoice, CustomerOrder> invoices = customerOrder.join("invoices");
        orderQuery.select(customerOrder);

        List<Long> values = Arrays.stream(orderId.toString()
                .split("\\s*,\\s*"))
                .map((s) -> Long.parseLong(s))
                .collect(Collectors.toList());

        List<Predicate> predicates = new ArrayList<Predicate>();
        predicates.add(builder.equal(invoices.get("id"), root.get("id")));
        predicates.add(builder.in(customerOrder.get("id"))
                .value(values));

        orderQuery.where(predicates.toArray(new Predicate[] {}));

        predicate.getExpressions()
                .add(builder.exists(orderQuery));

        return predicate;
    }

    /**
     *
     *
     */
    private Predicate hasCCTransaction(Object transactionNumber, Root<Invoice> root, CriteriaQuery<?> query, CriteriaBuilder builder)
    {
        Predicate predicate = builder.disjunction();

        predicate.getExpressions()
            .add(root.get(Invoice_.terms).in((Object[]) ((String) transactionNumber).split("\\s*,\\s*")));

        return predicate;
    }
}
