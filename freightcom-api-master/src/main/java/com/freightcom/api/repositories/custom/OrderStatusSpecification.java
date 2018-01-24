package com.freightcom.api.repositories.custom;

import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.freightcom.api.model.OrderStatus;

public class OrderStatusSpecification implements Specification<OrderStatus>
{
    private final Map<String,String> criteria;

    public OrderStatusSpecification(final Map<String,String> criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<OrderStatus> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        Predicate predicate = builder.conjunction();
        
        if (criteria.containsKey("shipping")) {
            predicate.getExpressions()
            .add(builder.notLike(root.get("name"), "%invoic%"));
        }

        return predicate;
    }

}
