package com.freightcom.api.repositories.custom;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;

import com.freightcom.api.ReportableError;
import com.freightcom.api.model.Customer.AutoInvoice;
import com.freightcom.api.model.CustomerOrderSummary;

public class OrderSummarySpecification extends BaseSpecification implements Specification<CustomerOrderSummary>
{
    public enum Mode {
        DRAFT_ONLY, SUBMITTED_ONLY, ALL
    };

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private Map<String, Object> criteria;

    public OrderSummarySpecification(Map<String, Object> criteria)
    {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<CustomerOrderSummary> root, CriteriaQuery<?> query, CriteriaBuilder builder)
    {
        Predicate predicate = builder.conjunction();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        boolean statusRestricted = false;
        boolean dateRestricted = false;

        for (String key : criteria.keySet()) {
            log.debug("KEY IS " + key);

            switch (key.toLowerCase()) {
            case "customerid":
                predicate.getExpressions()
                        .add(builder.equal(root.get("customer")
                                .get("id"), criteria.get(key)));
                break;

            case "agentid":
                log.debug("FILTERING BY AGENT ID " + criteria.get(key));
                dateRestricted = true;
                addLongId(predicate, "agent", criteria.get(key), root, builder);
                break;

            case "mode":
                switch ((Mode) criteria.get(key)) {
                case DRAFT_ONLY:
                    predicate.getExpressions()
                            .add(builder.equal(root.get("orderStatus")
                                    .get("id"), 16));
                    break;

                case SUBMITTED_ONLY:
                    predicate.getExpressions()
                            .add(builder.notEqual(root.get("orderStatus")
                                    .get("id"), 16));
                    break;

                case ALL:
                default:
                    break;

                }

                break;

            case "deliverydate":
                predicate.getExpressions()
                        .add(builder.equal(builder.function("DATE", Date.class, root.get("dateOfDelivery")), builder
                                .function("DATE", String.class, builder.literal(format.format(criteria.get(key))))));
                break;

            case "destination":
            case "shipto":
                Predicate disjunction = builder.disjunction();

                disjunction.getExpressions()
                        .add(builder.like(root.get("destinationCity"), '%' + criteria.get(key)
                                .toString() + '%'));
                disjunction.getExpressions()
                        .add(builder.like(root.get("destinationPostalCode"), '%' + criteria.get(key)
                                .toString() + '%'));
                disjunction.getExpressions()
                        .add(builder.like(root.get("destinationCompany"), '%' + criteria.get(key)
                                .toString() + '%'));

                predicate.getExpressions()
                        .add(disjunction);
                break;

            case "orderstatusname":
                Predicate disjunction1 = builder.disjunction();

                for (String name : criteria.get(key)
                        .toString()
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
                predicate.getExpressions()
                        .add(builder.equal(root.get("id"), criteria.get(key)));
                break;

            case "eshipperoid":
                Expression<String> condition = builder.function("IFNULL", String.class,
                        builder.function("RTRIM", String.class, root.get("eshipperOid")), builder.literal("draft"));
                predicate.getExpressions()
                        .add(builder.like(condition, '%' + criteria.get(key)
                                .toString() + '%'));
                break;

            case "statusid":
                try {
                    Predicate disjunction4 = builder.disjunction();

                    for (String value : criteria.get(key)
                            .toString()
                            .split("\\s*,\\s*")) {
                        disjunction4.getExpressions()
                                .add(builder.equal(root.get("orderStatus")
                                        .get("id"), Long.parseLong(value)));
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
                        .add(builder.like(root.get("bolNumber"), '%' + criteria.get(key)
                                .toString() + '%'));
                break;

            case "bolid":
                predicate.getExpressions()
                        .add(builder.like(root.get("bolId"), '%' + criteria.get(key)
                                .toString() + '%'));
                break;

            case "trackingnumber":
                if (criteria.get(key)
                        .toString()
                        .equalsIgnoreCase("@notnull")) {
                    predicate.getExpressions()
                            .add(builder.isNotNull(root.get("masterTrackingNum")));
                } else {
                    predicate.getExpressions()
                            .add(builder.equal(root.get("masterTrackingNum"), criteria.get(key)));
                }
                break;

            case "referencecode":
                predicate.getExpressions()
                        .add(builder.like(root.get("referenceCode"), '%' + criteria.get(key)
                                .toString() + '%'));
                break;

            case "carriername":
                Predicate disjunction5 = builder.disjunction();

                for (String value : criteria.get(key)
                        .toString()
                        .split("\\s*,\\s*")) {
                    disjunction5.getExpressions()
                            .add(builder.like(root.get("actualCarrierName"), "%" + value + "%"));
                }

                predicate.getExpressions()
                        .add(disjunction5);
                break;

            case "servicename":
                predicate.getExpressions()
                        .add(builder.like(root.get("serviceName"), '%' + criteria.get(key)
                                .toString() + '%'));
                break;

            case "origin":
            case "shipfrom":
                Predicate disjunction2 = builder.disjunction();

                disjunction2.getExpressions()
                        .add(builder.like(root.get("originCity"), '%' + criteria.get(key)
                                .toString() + '%'));
                disjunction2.getExpressions()
                        .add(builder.like(root.get("originPostalCode"), '%' + criteria.get(key)
                                .toString() + '%'));
                disjunction2.getExpressions()
                        .add(builder.like(root.get("originCompany"), '%' + criteria.get(key)
                                .toString() + '%'));

                predicate.getExpressions()
                        .add(disjunction2);
                break;

            case "autoinvoice":
                dateRestricted = true;

                predicate.getExpressions()
                        .add(builder.equal(root.get("customer")
                                .get("autoInvoice"), AutoInvoice.valueOf(criteria.get(key).toString())));

                break;

            case "scheduledshipdatefrom":
                dateRestricted = true;
                addDateFrom(predicate, "scheduledShipDate", criteria.get(key), root, builder);
                break;

            case "scheduledshipdateto":
                dateRestricted = true;
                addDateTo(predicate, "scheduledShipDate", criteria.get(key), root, builder);
                break;

            case "scheduledshipdate":
                dateRestricted = true;
                addDate(predicate, "scheduledShipDate", criteria.get(key), root, builder);
                break;

            case "invoicestatus":
                String matchStatus = criteria.get(key) == null ? null : criteria.get(key)
                        .toString()
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

        // Rule out orders with bad customer id
        predicate.getExpressions()
                .add(builder.notEqual(root.get("customerId"), 0));

        if (!statusRestricted) {
            // Rule out 999 unless explicitly requested
            predicate.getExpressions()
                    .add(builder.notEqual(root.get("orderStatus")
                            .get("id"), 999));
        }

        if (!dateRestricted) {
            // Just orders in the last year or so
            predicate.getExpressions()
                    .add(builder.lessThanOrEqualTo(
                            builder.diff(builder.function("YEAR", Integer.class, builder.function("NOW", Date.class)),
                                    builder.literal(1)),
                            builder.function("YEAR", Integer.class, root.get("createdAt"))));
        }

        return predicate;
    }

}
