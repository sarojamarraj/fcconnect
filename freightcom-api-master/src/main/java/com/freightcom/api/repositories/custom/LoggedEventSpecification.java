package com.freightcom.api.repositories.custom;

import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.freightcom.api.model.LoggedEvent;

public class LoggedEventSpecification implements Specification<LoggedEvent>
{
    private final Map<String, Object> criteria;

    public LoggedEventSpecification(final Map<String, Object> criteria)
    {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<LoggedEvent> root, CriteriaQuery<?> query, CriteriaBuilder builder)
    {
        Predicate predicate = builder.conjunction();

        for (Map.Entry<String, Object> entry : criteria.entrySet()) {
            switch (entry.getKey()) {
            case "entity_id":
            case "entityId":
                predicate.getExpressions()
                        .add(builder.equal(root.get("entityId"), entry.getValue()));
                break;

            case "type":
            case "entityType":
                predicate.getExpressions()
                        .add(builder.equal(root.get("entityType"), entry.getValue()));
                break;

            case "message_type":
            case "messageType":
                predicate.getExpressions()
                        .add(builder.equal(root.get("messageType"), entry.getValue()));
                break;

            case "userId":
                predicate.getExpressions()
                        .add(builder.equal(root.get("userId"), entry.getValue()));
                break;

            case "private":
            case "nPrivate":
                predicate.getExpressions()
                        .add(builder.equal(root.get("nPrivate"), entry.getValue()));
                break;

            case "restrictPrivate":
                // User can only see not private and those they've added
                Predicate disjunctionPrivate = builder.disjunction();

                disjunctionPrivate.getExpressions()
                        .add(builder.equal(root.get("userId"), entry.getValue()));
                disjunctionPrivate.getExpressions()
                        .add(builder.notEqual(root.get("nPrivate"), entry.getValue()));

                predicate.getExpressions()
                        .add(disjunctionPrivate);

                break;
            }
        }

        return predicate;
    }

}
