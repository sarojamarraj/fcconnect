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
import com.freightcom.api.model.Agent;

public class AgentSpecification extends BaseSpecification implements Specification<Agent>
{
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private Map<String, Object> criteria;

    public AgentSpecification(Map<String, Object> criteria)
    {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<Agent> root, CriteriaQuery<?> query, CriteriaBuilder builder)
    {
        Predicate predicate = builder.conjunction();

        for (String key : criteria.keySet()) {
            log.debug("KEY IS " + key);

            switch (key.toLowerCase()) {

            case "id":
                predicate.getExpressions()
                        .add(builder.equal(root.get("id"), criteria.get(key)));
                break;

            case "parentid":
                predicate.getExpressions()
                        .add(builder.equal(root.get("parentSalesAgent").get("id"), criteria.get(key)));
                break;

            case "parentname":
                break;

            case "name":
                break;

            case "phone":
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
