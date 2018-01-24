package com.freightcom.api.repositories.custom;

import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;

import com.freightcom.api.ReportableError;
import com.freightcom.api.model.Customer;
import com.freightcom.api.model.CustomerBilling_;
import com.freightcom.api.model.Customer_;

public class CustomerSpecification extends BaseSpecification implements Specification<Customer>
{
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final Map<String, Object> criteria;

    public CustomerSpecification(final Map<String, Object> criteria)
    {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<Customer> root, CriteriaQuery<?> query, CriteriaBuilder builder)
    {
        Predicate predicate = builder.conjunction();
        for (String key : criteria.keySet()) {
            log.debug("KEY IS " + key);

            switch (key.toLowerCase()) {
            case "name":
                predicate.getExpressions()
                        .add(builder.like(root.get("name"), "%" + criteria.get(key) + "%"));
                break;

            case "id":
            case "customerid":
                predicate.getExpressions()
                        .add(builder.equal(root.get("id"), criteria.get(key)));
                break;

            case "agentid":
            case "salesAgentId":
                predicate.getExpressions()
                        .add(builder.equal(root.get("salesAgent")
                                .get("id"), criteria.get(key)));
                break;

            case "active":
                if ("all".equalsIgnoreCase(criteria.get(key)
                        .toString())) {
                    // no restriction
                } else if (yesValue(criteria.get(key))) {
                    predicate.getExpressions()
                            .add(builder.isNotNull(root.get("activatedAt")));
                    predicate.getExpressions()
                            .add(builder.isNull(root.get("suspendedAt")));
                } else {
                    Predicate disjunctionActive = builder.disjunction();

                    disjunctionActive.getExpressions()
                        .add(builder.isNull(root.get("activatedAt")));
                    disjunctionActive.getExpressions()
                        .add(builder.isNotNull(root.get("suspendedAt")));

                    predicate.getExpressions()
                        .add(disjunctionActive);
                }
                break;

            case "contact":
                predicate.getExpressions()
                        .add(builder.like(root.get("contact"), "%" + criteria.get(key) + "%"));
                break;

            case "phone":
                predicate.getExpressions()
                        .add(builder.like(root.get("phone"), "%" + criteria.get(key) + "%"));
                break;

            case "email":
                predicate.getExpressions()
                        .add(builder.like(root.get("email"), "%" + criteria.get(key) + "%"));
                break;

            case "address":
                Predicate disjunction = builder.disjunction();
                disjunction.getExpressions()
                        .add(builder.like(root.get("address"), "%" + criteria.get(key) + "%"));
                disjunction.getExpressions()
                        .add(builder.like(root.get("city"), "%" + criteria.get(key) + "%"));

                predicate.getExpressions()
                        .add(disjunction);
                break;

            case "lastinvoicerun":
                try {
                    Predicate disjunction4 = builder.disjunction();
                    disjunction4.getExpressions()
                            .add(builder.isNull(root.get(Customer_.customerBilling)
                                    .get(CustomerBilling_.lastInvoiceRun)));
                    dateLessThan(disjunction4, root.get(Customer_.customerBilling)
                            .get(CustomerBilling_.lastInvoiceRun), criteria.get(key), root, builder);
                    predicate.getExpressions()
                            .add(disjunction4);
                } catch (NumberFormatException e) {
                    throw new ReportableError("Invoice billing days");
                }
                break;

            case "autoinvoice":
                predicate.getExpressions()
                        .add(builder.equal(root.get(Customer_.autoInvoice),
                                Customer.AutoInvoice.valueOf(criteria.get(key)
                                        .toString())));

                break;

            case "lastchargerun":
                try {
                    Predicate disjunction5 = builder.disjunction();
                    disjunction5.getExpressions()
                            .add(builder.isNull(root.get(Customer_.customerBilling)
                                    .get(CustomerBilling_.lastChargeRun)));
                    dateLessThan(disjunction5, root.get(Customer_.customerBilling)
                            .get(CustomerBilling_.lastChargeRun), criteria.get(key), root, builder);
                    predicate.getExpressions()
                            .add(disjunction5);
                } catch (NumberFormatException e) {
                    throw new ReportableError("Charge billing days");
                }
                break;

            case "autocharge":
                predicate.getExpressions()
                        .add(builder.equal(root.get(Customer_.autoCharge),
                                Customer.AutoCharge.valueOf(criteria.get(key)
                                        .toString())));

                break;

            }
        }

        return predicate;
    }

}
