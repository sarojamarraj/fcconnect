package com.freightcom.api.repositories.custom;

import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.freightcom.api.ReportableError;
import com.freightcom.api.model.CurrencyExchange;

public class CurrencyExchangeSpecification extends BaseSpecification implements Specification<CurrencyExchange>
{
    private final Map<String,Object> criteria;

    public CurrencyExchangeSpecification(final Map<String,Object> criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<CurrencyExchange> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        Predicate predicate = builder.conjunction();

        for (String key : criteria.keySet()) {
            switch (key.toLowerCase()) {

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

        return predicate;
    }

}
