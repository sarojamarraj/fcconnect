package com.freightcom.api.repositories.custom;

import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.freightcom.api.ReportableError;
import com.freightcom.api.model.SystemProperty;

public class SystemPropertySpecification extends BaseSpecification implements Specification<SystemProperty>
{
    private final Map<String, ?> criteria;

    public SystemPropertySpecification(final Map<String, ?> criteria)
    {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<SystemProperty> root, CriteriaQuery<?> query, CriteriaBuilder builder)
    {
        Predicate predicate = builder.conjunction();

        for (Map.Entry<String, ?> entry : criteria.entrySet()) {

            switch (entry.getKey()
                    .toLowerCase()) {

            case "sort":
            case "size":
            case "page":
                // Ok
                break;

            default:
                throw new ReportableError("Invalid parameter " + entry.getKey());
            }

        }

        return predicate;
    }

}
