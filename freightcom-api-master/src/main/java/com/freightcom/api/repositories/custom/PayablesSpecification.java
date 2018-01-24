package com.freightcom.api.repositories.custom;

import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.freightcom.api.ReportableError;
import com.freightcom.api.model.Payable;

public class PayablesSpecification extends BaseSpecification implements Specification<Payable>
{
    private Map<String, Object> criteria;

    public PayablesSpecification(Map<String, Object> criteria)
    {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<Payable> root, CriteriaQuery<?> query, CriteriaBuilder builder)
    {
        Predicate predicate = builder.conjunction();

        for (String key : criteria.keySet()) {
            Object value = criteria.get(key);

            switch (key.toLowerCase()) {

            case "id":
                predicate.getExpressions()
                        .add(builder.equal(root.get("id"), criteria.get(key)));
                break;

            case "dueat":
                addDate(predicate, "dueAt", value, root, builder);
                break;

            case "createdat":
                addDate(predicate, "createdAt", value, root, builder);
                break;

            case "status":
                addString(predicate, "status", value, root, builder);
                break;

            case "paidat":
                addDate(predicate, "paidAt", value, root, builder);
                break;

            case "totalamount":
                addNumericRange(predicate, "totalAmount", value, root, builder);
                break;

            case "paidamount":
                addNumericRange(predicate, "paidAmount", value, root, builder);
                break;

            case "carrierid":
            case "carrier.id":
                try {
                    Predicate disjunction1 = builder.disjunction();

                    for (String item : value.toString()
                            .split("\\s*,\\s*")) {
                        disjunction1.getExpressions()
                                .add(builder.equal(root.get("service")
                                        .get("id"), Long.parseLong(item)));
                    }

                    predicate.getExpressions()
                            .add(disjunction1);
                } catch (NumberFormatException e) {
                    throw new ReportableError("Invalid order status id");
                }
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

        return predicate;
    }

}
