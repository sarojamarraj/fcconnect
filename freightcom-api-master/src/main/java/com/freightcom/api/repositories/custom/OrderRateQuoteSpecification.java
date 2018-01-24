package com.freightcom.api.repositories.custom;

import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.freightcom.api.model.OrderRateQuote;

public class OrderRateQuoteSpecification implements Specification<OrderRateQuote>
{
    public OrderRateQuoteSpecification(final Map<String,String> criteria) {
    }

    @Override
    public Predicate toPredicate(Root<OrderRateQuote> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        Predicate predicate = builder.conjunction();
        return predicate;
    }

}
