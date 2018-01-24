package com.freightcom.api.repositories.custom;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;

import com.freightcom.api.ReportableError;
import com.freightcom.api.model.Claim;
import com.freightcom.api.model.Claim_;
import com.freightcom.api.model.Customer.AutoInvoice;
import com.freightcom.api.model.CustomerBilling_;
import com.freightcom.api.model.CustomerOrder;
import com.freightcom.api.model.CustomerOrder_;
import com.freightcom.api.model.Customer_;
import com.freightcom.api.model.DisputeInformation;
import com.freightcom.api.model.DisputeInformation_;
import com.freightcom.api.model.Invoice;
import com.freightcom.api.model.Invoice_;
import com.freightcom.api.model.OrderStatus_;
import com.freightcom.api.model.PackageType_;
import com.freightcom.api.model.Pickup_;
import com.freightcom.api.model.support.OrderStatusCode;
import com.freightcom.api.util.Empty;

public class OrderSpecification extends BaseSpecification implements Specification<CustomerOrder>
{
    public enum Mode {
        DRAFT_ONLY, SUBMITTED_ONLY, JOB_BOARD, DISPUTED, HAS_CLAIM, ALL
    };

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private Map<String, Object> criteria;

    public OrderSpecification(Map<String, Object> criteria)
    {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<CustomerOrder> root, CriteriaQuery<?> query, CriteriaBuilder builder)
    {
        Predicate predicate = builder.conjunction();

        boolean statusRestricted = false;

        for (String key : criteria.keySet()) {
            Object value = criteria.get(key);
            log.debug("KEY IS " + key);

            switch (key.toLowerCase()) {
            case "customerid":
                predicate.getExpressions()
                        .add(builder.equal(root.get("customer")
                                .get("id"), value));
                break;

            case "agentid":
                log.debug("FILTERING BY AGENT ID " + value);
                addLongId(predicate, "agent", value, root, builder);
                break;

            case "mode":
                switch ((Mode) value) {
                case DRAFT_ONLY:
                    Predicate draftDisjunction = builder.disjunction();
                    statusRestricted = true;

                    draftDisjunction.getExpressions()
                            .add(builder.equal(root.get("orderStatus")
                                    .get("id"), OrderStatusCode.DRAFT.getValue()));

                    draftDisjunction.getExpressions()
                            .add(builder.equal(root.get("orderStatus")
                                    .get("id"), OrderStatusCode.QUOTED.getValue()));

                    predicate.getExpressions()
                        .add(draftDisjunction);
                    break;

                case SUBMITTED_ONLY:
                    predicate.getExpressions()
                            .add(builder.notEqual(root.get("orderStatus")
                                    .get("id"), OrderStatusCode.DRAFT.getValue()));
                    predicate.getExpressions()
                            .add(builder.notEqual(root.get("orderStatus")
                                    .get("id"), OrderStatusCode.QUOTED.getValue()));
                    break;

                case DISPUTED:
                    log.debug("SHOW DISPUTED ORDERS");
                    predicate.getExpressions()
                            .add(disputed(root, query, builder));
                    break;

                case JOB_BOARD:
                    log.debug("SHOW JOB BOARD");
                    predicate.getExpressions()
                            .add(builder.notEqual(root.get("orderStatus")
                                    .get("id"), OrderStatusCode.DRAFT.getValue()));
                    break;

                case HAS_CLAIM:
                    predicate.getExpressions()
                            .add(claimed(root, query, builder));
                    break;

                case ALL:
                default:
                    break;

                }

                break;

            case "hasclaim":
                if (!Empty.check(value)) {
                    if (yesValue(value)) {
                        predicate.getExpressions()
                                .add(builder.isNotNull(root.get(CustomerOrder_.claim)));
                        predicate.getExpressions()
                                .add(builder.notEqual(root.get(CustomerOrder_.claim)
                                        .get(Claim_.status), Claim.Status.SETTLED));
                    } else {
                        Predicate claimDisjunction = builder.disjunction();
                        claimDisjunction.getExpressions()
                                .add(builder.isNull(root.get(CustomerOrder_.claim)));
                        claimDisjunction.getExpressions()
                                .add(builder.equal(root.get(CustomerOrder_.claim)
                                        .get(Claim_.status), Claim.Status.SETTLED));
                        predicate.getExpressions()
                                .add(claimDisjunction);
                    }
                }
                break;

            case "deliverydate":
                log.debug("DELIVERY DATE VALUE " + value + " " + value.getClass());
                predicate.getExpressions()
                        .add(builder.equal(builder.function("DATE", Date.class, root.get("dateOfDelivery")),
                                builder.function("DATE", String.class, builder.literal(value.toString()))));
                break;

            case "destination":
            case "shipto":
                Predicate disjunction = builder.disjunction();

                disjunction.getExpressions()
                        .add(builder.like(root.get("destinationCity"), '%' + value.toString() + '%'));
                disjunction.getExpressions()
                        .add(builder.like(root.get("destinationPostalCode"), '%' + value.toString() + '%'));
                disjunction.getExpressions()
                        .add(builder.like(root.get("destinationCompany"), '%' + value.toString() + '%'));

                predicate.getExpressions()
                        .add(disjunction);
                break;

            case "status":
                predicate.getExpressions()
                        .add(builder.equal(root.get(CustomerOrder_.orderStatus)
                                .get(OrderStatus_.id), value));
                break;

            case "orderstatusname":
                Predicate disjunction1 = builder.disjunction();

                for (String name : value.toString()
                        .split("\\s*,\\s*")) {
                    if (name.matches("DELIVERED")) {
                        disjunction1.getExpressions()
                                .add(builder.equal(root.get("orderStatus")
                                        .get("name"), "delivered"));
                        disjunction1.getExpressions()
                                .add(builder.like(root.get("orderStatus")
                                        .get("name"), "invoice"));
                    } else {
                        disjunction1.getExpressions()
                                .add(builder.like(root.get("orderStatus")
                                        .get("name"), '%' + name + '%'));
                    }
                }

                predicate.getExpressions()
                        .add(disjunction1);
                statusRestricted = true;

                break;

            case "id":
            case "eshipperoid":
                predicate.getExpressions()
                        .add(builder.equal(root.get("id"), value));
                break;

            case "xeshipperoid":
                Expression<String> condition = builder.function("IFNULL", String.class,
                        builder.function("RTRIM", String.class, root.get("eshipperOid")), builder.literal("draft"));
                predicate.getExpressions()
                        .add(builder.like(condition, '%' + value.toString() + '%'));
                break;

            case "statusid":
                try {
                    Predicate disjunction4 = builder.disjunction();

                    for (String item : value.toString()
                            .split("\\s*,\\s*")) {
                        disjunction4.getExpressions()
                                .add(builder.equal(root.get("orderStatus")
                                        .get("id"), Long.parseLong(item)));
                    }

                    predicate.getExpressions()
                            .add(disjunction4);
                    statusRestricted = true;
                } catch (NumberFormatException e) {
                    throw new ReportableError("Invalid order status id");
                }
                break;

            case "bolnumber":
                predicate.getExpressions()
                        .add(builder.like(root.get("bolNumber"), '%' + value.toString() + '%'));
                break;

            case "bolid":
                predicate.getExpressions()
                        .add(builder.like(root.get("bolId"), '%' + value.toString() + '%'));
                break;

            case "trackingnumber":
            case "trackingnum":
                predicate.getExpressions()
                        .add(hasTrackingNum(value, root, query, builder));
                break;

            case "referencecode":
            case "referencenum":
            case "referencenumber":
                predicate.getExpressions()
                        .add(hasReferenceCode(value, root, query, builder));
                break;

            case "carriername":
                Predicate disjunction5 = builder.disjunction();

                for (String item : value.toString()
                        .split("\\s*,\\s*")) {
                    disjunction5.getExpressions()
                            .add(builder.like(root.get("actualCarrierName"), "%" + item + "%"));
                }

                predicate.getExpressions()
                        .add(disjunction5);
                break;

            case "carrierid":
            case "carrier.id":
                Predicate disjunction6 = builder.disjunction();

                for (String item : value.toString()
                        .split("\\s*,\\s*")) {
                    disjunction6.getExpressions()
                            .add(builder.equal(root.get("service")
                                    .get("id"), asLong(item)));
                }

                predicate.getExpressions()
                        .add(disjunction6);
                break;

            case "servicename":
                predicate.getExpressions()
                        .add(builder.like(root.get("serviceName"), '%' + value.toString() + '%'));
                break;

            case "origin":
            case "shipfrom":
                Predicate disjunction2 = builder.disjunction();

                disjunction2.getExpressions()
                        .add(builder.like(root.get("originCity"), '%' + value.toString() + '%'));
                disjunction2.getExpressions()
                        .add(builder.like(root.get("originPostalCode"), '%' + value.toString() + '%'));
                disjunction2.getExpressions()
                        .add(builder.like(root.get("originCompany"), '%' + value.toString() + '%'));

                predicate.getExpressions()
                        .add(disjunction2);
                break;

            case "autoinvoice":
                predicate.getExpressions()
                        .add(builder.equal(root.get("customer")
                                .get("autoInvoice"), AutoInvoice.valueOf(value.toString())));

                break;

            case "customworkorder":
                predicate.getExpressions()
                        .add(builder.equal(root.get("customWorkOrder"), yesValue(value)));

                break;

            case "shipdatefrom":
            case "scheduledshipdatefrom":
                addDateFrom(predicate, "shipDate", value, root, builder);
                break;

            case "shipdateto":
            case "scheduledshipdateto":
                addDateTo(predicate, "shipDate", value, root, builder);
                break;

            case "shipdate":
            case "scheduledshipdate":
                addDate(predicate, "shipDate", value, root, builder);
                break;

            case "packagetypeid":
                try {
                    Predicate disjunction3 = builder.disjunction();

                    for (String item : value.toString()
                            .split("\\s*,\\s*")) {
                        disjunction3.getExpressions()
                                .add(builder.equal(root.get(CustomerOrder_.packageType)
                                        .get(PackageType_.id), Long.parseLong(item)));
                    }

                    predicate.getExpressions()
                            .add(disjunction3);
                } catch (NumberFormatException e) {
                    throw new ReportableError("Invalid package type id");
                }
                break;

            case "invoicestatus":
                String matchStatus = value == null ? null : value.toString()
                        .toLowerCase();

                if (matchStatus == null || matchStatus.equals("")) {
                    // No constraint
                    log.debug("NO INVOICE STATUS MATCH");
                } else if ("fully invoiced".equals(matchStatus) || "unbilled charges".equals(matchStatus)) {
                    log.debug("INVOICE STATUS MATCH " + matchStatus);
                    predicate.getExpressions()
                            .add(builder.equal(root.get("invoiceStatus"), matchStatus));
                } else {
                    throw new ReportableError("Invalid invoice status '" + matchStatus + "'");
                }

                break;

            case "lastinvoicerun":
                try {
                    Predicate disjunction4 = builder.disjunction();
                    dateLessThan(disjunction4, root.get(CustomerOrder_.customer)
                            .get(Customer_.customerBilling)
                            .get(CustomerBilling_.lastInvoiceRun), value, root, builder);
                    predicate.getExpressions()
                            .add(disjunction4);
                } catch (NumberFormatException e) {
                    throw new ReportableError("Invoice billing days");
                }
                break;

            case "latestcomment":
                predicate.getExpressions()
                        .add(latestComment(value, root, query, builder));
                break;

            case "currency":
                predicate.getExpressions()
                        .add(hasCurrency(value, root, query, builder));
                break;

            case "packagetypename":
                predicate.getExpressions()
                        .add(hasPackageTypeName(value, root, query, builder));
                break;

            case "cctransactionnumber":
            case "creditcardtransactionnumber":
                predicate.getExpressions()
                        .add(hasCCTransaction(value, root, query, builder));
                break;

            case "invoiceid":
                predicate.getExpressions()
                        .add(hasInvoice(value, root, query, builder));
                break;

            case "createdatfrom":
            case "createdatefrom":
                addDateFrom(predicate, "createdAt", value, root, builder);
                break;

            case "pickupdate":
                addDate(predicate, root.get(CustomerOrder_.scheduledPickup)
                        .get(Pickup_.pickupDate), value, root, builder);
                break;

            case "colorcode":
            case "colorcodes":
                predicate.getExpressions()
                        .add(colorCode(value, root, query, builder));
                break;

            case "sort":
            case "size":
            case "page":
                // Ok
                break;

            default:
                throw new ReportableError("Invalid parameter " + key);
            }
        }

        // Rule out orders with bad customer id
        predicate.getExpressions()
                .add(builder.notEqual(root.get("customerId"), 0));

        if (!statusRestricted) {
            // Rule out DELETE unless explicitly requested
            predicate.getExpressions()
                    .add(builder.notEqual(root.get("orderStatus")
                            .get("id"), OrderStatusCode.DELETED.getValue()));
        }

        return predicate;
    }

    /**
     *
     *
     */
    private Predicate disputed(Root<CustomerOrder> root, CriteriaQuery<?> query, CriteriaBuilder builder)
    {
        Predicate terms = builder.conjunction();

        terms.getExpressions()
                .add(builder.isNotNull(root.get(CustomerOrder_.disputeInformation)));
        terms.getExpressions()
                .add(builder.notEqual(root.get(CustomerOrder_.disputeInformation)
                        .get(DisputeInformation_.status), DisputeInformation.Status.RESOLVED));

        return terms;
    }

    /**
     *
     *
     */
    private Predicate claimed(Root<CustomerOrder> root, CriteriaQuery<?> query, CriteriaBuilder builder)
    {
        Predicate terms = builder.conjunction();

        terms.getExpressions()
                .add(builder.isNotNull(root.get(CustomerOrder_.claim)));
        terms.getExpressions()
                .add(builder.notEqual(root.get(CustomerOrder_.claim)
                        .get(Claim_.status), Claim.Status.SETTLED));

        return terms;
    }

    /**
     *
     *
     */
    private Predicate latestComment(Object value, Root<CustomerOrder> root, CriteriaQuery<?> query,
            CriteriaBuilder builder)
    {
        return builder.like(builder.function("last_comment", String.class, root.get("id")), likePattern(value));
    }

    /**
     *
     *
     */
    private Predicate hasInvoice(Object invoiceIds, Root<CustomerOrder> root, CriteriaQuery<?> query,
            CriteriaBuilder builder)
    {
        Predicate predicate = builder.disjunction();
        Join<CustomerOrder, Invoice> invoices = root.join(CustomerOrder_.invoices);

        predicate.getExpressions()
                .add(invoices.get(Invoice_.id)
                        .in(splitIDs(invoiceIds)));

        return predicate;
    }

    private Integer mapColor(String color)
    {
        switch (color.toLowerCase()) {
        case "orange":
            return new Integer(1);

        case "green":
            return new Integer(2);

        case "yellow":
            return new Integer(3);

        case "red":
            return new Integer(4);

        default:
            throw new ReportableError("Invalid color code " + color);
        }
    }

    /**
     *
     *
     */
    private Predicate colorCode(Object codes, Root<CustomerOrder> root, CriteriaQuery<?> query, CriteriaBuilder builder)
    {
        Predicate predicate = builder.disjunction();

        List<Integer> numericCodes = Arrays.stream(((String) codes).split(SPLIT_PATTERN))
                .map(color -> mapColor(color))
                .collect(Collectors.toList());

        predicate.getExpressions()
                .add(root.get(CustomerOrder_.colorCode)
                        .in(numericCodes));

        return predicate;
    }

    /**
     *
     *
     */
    private Predicate hasCurrency(Object currencies, Root<CustomerOrder> root, CriteriaQuery<?> query,
            CriteriaBuilder builder)
    {
        Predicate predicate = builder.disjunction();

        predicate.getExpressions()
                .add(root.get(CustomerOrder_.customer)
                        .get(Customer_.invoiceCurrency)
                        .in((Object[]) ((String) currencies).split(SPLIT_PATTERN)));

        return predicate;
    }

    /**
     *
     *
     */
    private Predicate hasPackageTypeName(Object typeNames, Root<CustomerOrder> root, CriteriaQuery<?> query,
            CriteriaBuilder builder)
    {
        Predicate predicate = builder.disjunction();
        String[] names = ((String) typeNames).replaceAll("(?i)pallet", "pallet,ltl")
                .split("\\s*,\\s*");

        if (Arrays.stream(names)
                .anyMatch(name -> name.matches("(?i)^work-?order$"))) {
            // Have work order in list, add check for customWorkOrder to
            // disjunction
            predicate.getExpressions()
                    .add(builder.isTrue(root.get(CustomerOrder_.customWorkOrder)));
        }

        predicate.getExpressions()
                .add(root.get(CustomerOrder_.packageTypeName)
                        .in((Object[]) names));

        return predicate;
    }

    /**
     *
     *
     */
    @SuppressWarnings("unused")
    private Predicate hasAttributeValue(SingularAttribute<? super CustomerOrder, ?> attribute, Object codes,
            Root<CustomerOrder> root, CriteriaQuery<?> query, CriteriaBuilder builder)
    {
        Predicate predicate = builder.disjunction();

        if (codes != null && codes.toString()
                .equalsIgnoreCase("@notnull")) {
            predicate.getExpressions()
                    .add(builder.isNotNull(root.get(attribute)));
        } else {
            predicate.getExpressions()
                    .add(root.get(attribute)
                            .in((Object[]) ((String) codes).split("\\s*,\\s*")));
        }

        return predicate;
    }

    /**
     *
     *
     */
    private Predicate hasReferenceCode(Object value, Root<CustomerOrder> root, CriteriaQuery<?> query,
            CriteriaBuilder builder)
    {
        Predicate predicate = builder.disjunction();

        log.debug("ADDING REFERENCE CODE LOOKUP " + value);

        predicate.getExpressions()
                .add(root.get(CustomerOrder_.referenceCode)
                        .in((Object[]) ((String) value).split("\\s*,\\s*")));

        return predicate;
    }

    /**
     *
     *
     */
    private Predicate hasTrackingNum(Object value, Root<CustomerOrder> root, CriteriaQuery<?> query,
            CriteriaBuilder builder)
    {
        Predicate predicate = builder.disjunction();

        predicate.getExpressions()
                .add(root.get(CustomerOrder_.masterTrackingNum)
                        .in((Object[]) ((String) value).split("\\s*,\\s*")));

        return predicate;
    }

    /**
     *
     *
     */
    private Predicate hasCCTransaction(Object transactionNumber, Root<CustomerOrder> root, CriteriaQuery<?> query,
            CriteriaBuilder builder)
    {
        Predicate predicate = builder.disjunction();

        predicate.getExpressions()
                .add(root.get(CustomerOrder_.packageTypeName)
                        .in((Object[]) ((String) transactionNumber).split("\\s*,\\s*")));

        return predicate;
    }

}
