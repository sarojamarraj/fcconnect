package com.freightcom.api.repositories.custom;

import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.freightcom.api.ReportableError;
import com.freightcom.api.model.Carrier_;
import com.freightcom.api.model.Service;
import com.freightcom.api.model.Service_;

public class ServiceSpecification extends BaseSpecification implements Specification<Service>
{
    private final Map<String, ?> criteria;

    public ServiceSpecification(final Map<String, ?> criteria)
    {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<Service> root, CriteriaQuery<?> query, CriteriaBuilder builder)
    {
        Predicate predicate = builder.conjunction();

        for (String key : criteria.keySet()) {
            Object value = criteria.get(key);

            switch (key.toLowerCase()) {

            case "id":
                addLong(predicate, "id", value, root, builder);
                break;

            case "name":
                addLike(predicate, "name", value, root, builder);
                break;

            case "provider":
                addLike(predicate, "provider", value, root, builder);
                break;

            case "implementingclass":
                addLike(predicate, root.get(Service_.carrier).get(Carrier_.implementingClass), value, root, builder);
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

        predicate.getExpressions()
            .add(builder.equal(root.get("discontinued"), 0));



        return predicate;
    }

}
