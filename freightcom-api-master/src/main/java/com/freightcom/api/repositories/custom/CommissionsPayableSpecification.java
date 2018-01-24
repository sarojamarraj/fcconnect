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
import com.freightcom.api.model.CommissionPayable;

public class CommissionsPayableSpecification extends BaseSpecification implements Specification<CommissionPayable>
{
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private Map<String, Object> criteria;

    public CommissionsPayableSpecification(Map<String, Object> criteria)
    {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<CommissionPayable> root, CriteriaQuery<?> query, CriteriaBuilder builder)
    {
        Predicate predicate = builder.conjunction();

        // In case agent id has wierd case, use the forced one by
        // login role if specified
        Object agentId = criteria.get("agentid");

        for (String key : criteria.keySet()) {
            log.debug("KEY IS " + key);
            Object value = criteria.get(key);

            switch (key.toLowerCase()) {

            case "id":
                predicate.getExpressions()
                    .add(builder.equal(root.get("id"), value));
                break;

            case "agentid":
            case "agent.id":
                predicate.getExpressions()
                .add(builder.equal(root.get("agent").get("id"), agentId == null ? value : agentId));
            break;

            case "dueat":
                addDate(predicate, "dueAt", value, root, builder);
                break;

            case "agentname":
                addLike(root.get("agent").get("name"), value, builder);
                break;

            case "createdat":
                addDate(predicate, "createdAt", value, root, builder);
                break;

            case "paidat":
                addDate(predicate, "paidAt", value, root, builder);
                break;

            case "totalamount":
                addNumericRange(predicate, "totalAmount", value, root, builder);
                break;

            case "status":
                addString(predicate, "status", value, root, builder);
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
