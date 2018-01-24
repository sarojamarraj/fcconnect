package com.freightcom.api.repositories.custom;

import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.freightcom.api.model.Alert;

public class AlertSpecification implements Specification<Alert>
{
    private final Map<String,String> criteria;

    public AlertSpecification(final Map<String,String> criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<Alert> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        Predicate predicate = builder.conjunction();
        for (String key : criteria.keySet()) {
            switch (key) {
            case "userId":
                predicate.getExpressions()
                        .add(builder.equal(root.get("userId"), criteria.get(key)));
                break;

            case "userRoleId":
                predicate.getExpressions()
                        .add(builder.equal(root.get("userRoleId"), criteria.get(key)));
                break;

            case "objectId":
                predicate.getExpressions()
                        .add(builder.equal(root.get("objectId"), criteria.get(key)));
                break;
            }
        }

        return predicate;
    }

}
