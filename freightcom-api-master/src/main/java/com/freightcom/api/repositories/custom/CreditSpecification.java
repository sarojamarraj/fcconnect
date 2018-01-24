package com.freightcom.api.repositories.custom;

import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.freightcom.api.ReportableError;
import com.freightcom.api.model.Credit;

public class CreditSpecification extends BaseSpecification implements Specification<Credit>
{
    private final Map<String,String> criteria;

    public CreditSpecification(final Map<String,String> criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<Credit> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        Predicate predicate = builder.conjunction();

        for (String key : criteria.keySet()) {

            switch (key.toLowerCase()) {

            case "id":
                addLong(predicate, "id", criteria.get(key), root, builder);
                break;

            case "customerid":
                addLong(predicate, "customerId", criteria.get(key), root, builder);
                break;

            case "accessorialtypeid":
                addLong(predicate, "customerid", criteria.get(key), root, builder);
                break;

            case "packagetypeid":
                addLong(predicate, "packageTypeId", criteria.get(key), root, builder);
                break;

            case "serviceid":
                addLong(predicate, "packagetypeid", criteria.get(key), root, builder);
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
