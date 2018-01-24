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
import com.freightcom.api.model.Markup;

public class MarkupSpecification extends BaseSpecification implements Specification<Markup>
{
    private final Map<String,String> criteria;

    private final Logger log = LoggerFactory.getLogger(this.getClass());


    public MarkupSpecification(final Map<String,String> criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<Markup> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        Predicate predicate = builder.conjunction();
        for (String key : criteria.keySet()) {
            log.debug("MARKUP KEY " + key);

            switch (key.toLowerCase()) {

            case "id":
                addLong(predicate, "id", criteria.get(key), root, builder);
                break;

            case "customerid":
                addLong(predicate, "customerId", criteria.get(key), root, builder);
                break;

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
