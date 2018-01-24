package com.freightcom.api.repositories.custom;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.springframework.data.jpa.domain.Specification;

import com.freightcom.api.ReportableError;
import com.freightcom.api.model.Carrier;
import com.freightcom.api.model.Service;

public class CarrierSpecification extends BaseSpecification implements Specification<Carrier>
{
    private final Map<String,String> criteria;

    public CarrierSpecification(final Map<String,String> criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<Carrier> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        Predicate predicate = builder.conjunction();
        boolean hasServicePredicate = false;

        for (String key : criteria.keySet()) {
            String value = criteria.get(key);

            switch (key.toLowerCase()) {

            case "id":
                addLong(predicate, "id", value, root, builder);
                break;

            case "serviceid":
                hasServicePredicate = true;
                hasService(value, root, query, builder);
                break;

            case "name":
                addLike(predicate, "name", value, root, builder);
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

        if (! hasServicePredicate) {
            predicate.getExpressions().add(hasService(null, root, query, builder));
        }

        return predicate;
    }

    /**
     *
     *
     */
    private Predicate hasService(String serviceId, Root<Carrier> root, CriteriaQuery<?> query, CriteriaBuilder builder)
    {
        Predicate predicate = builder.disjunction();
        Subquery<Service> serviceQuery = query.subquery(Service.class);
        Root<Service> service = serviceQuery.from(Service.class);

        serviceQuery.select(service);

        List<Predicate> predicates = new ArrayList<Predicate>();
        predicates.add(builder.equal(service.get("carrier").get("id"), root.get("id")));

        if (serviceId != null) {
            predicates.add(builder.equal(service.get("id"), Long.parseLong(serviceId)));
        }

        serviceQuery.where(predicates.toArray(new Predicate[] {}));

        predicate.getExpressions()
                .add(builder.exists(serviceQuery));

        return predicate;
    }


}
