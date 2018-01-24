package com.freightcom.api.repositories.custom;


import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import com.freightcom.api.ReportableError;

import org.springframework.data.jpa.domain.Specification;

import com.freightcom.api.model.XTX;

public class XTXSpecification extends BaseSpecification implements Specification<XTX>
{
    private final Map<String,Object> criteria;

    public XTXSpecification(final Map<String,Object> criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<XTX> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
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
                throw new ReprotableError("Invalid parameter " + key);
            }
        }

        return predicate;
    }

}
