package com.freightcom.api.repositories.custom;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;

import com.freightcom.api.model.AddressBook;
import com.freightcom.api.util.Empty;

public class AddressBookSpecification implements Specification<AddressBook>
{
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private AddressBookSearchCriteria criteria;

    public AddressBookSpecification(AddressBookSearchCriteria criteria)
    {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<AddressBook> root, CriteriaQuery<?> query, CriteriaBuilder builder)
    {
        Predicate predicate = builder.conjunction();

        if (criteria.getCustomerId() != null && ! criteria.getCustomerId().equals(0L)) {
            log.debug("SETTING CUSTOMER ID " + root.get("customerId") + " " + criteria.getCustomerId());
            predicate.getExpressions()
                    .add(builder.equal(root.get("customerId"), criteria.getCustomerId()));
        } else {
            predicate.getExpressions()
                    .add(builder.notEqual(root.get("customerId"), 0));
        }

        if (! Empty.check(criteria.getQuery())) {
            predicate.getExpressions()
            .add(builder.like(root.get("consigneeName"), "%" + criteria.getQuery() + "%"));
        }

        if (criteria.getCompany() != null) {
            predicate.getExpressions()
            .add(builder.like(root.get("consigneeName"), "%" + criteria.getCompany() + "%"));
        }

        if (criteria.getAddress() != null) {
            predicate.getExpressions()
            .add(builder.like(root.get("address1"), "%" + criteria.getAddress() + "%"));
        }

        if (criteria.getCity() != null) {
            predicate.getExpressions()
            .add(builder.like(root.get("city"), "%" + criteria.getCity() + "%"));
        }

        if (criteria.getProvince() != null) {
            predicate.getExpressions()
            .add(builder.like(root.get("province"), "%" + criteria.getProvince() + "%"));
        }

        if (criteria.getCountry() != null) {
            predicate.getExpressions()
            .add(builder.like(root.get("country"), "%" + criteria.getCountry() + "%"));
        }

        if (criteria.getPostalCode() != null) {
            predicate.getExpressions()
            .add(builder.like(root.get("postalCode"), "%" + criteria.getPostalCode() + "%"));
        }

        if (criteria.getContactName() != null) {
            predicate.getExpressions()
            .add(builder.like(root.get("contactName"), "%" + criteria.getContactName() + "%"));
        }

        if (criteria.getContactEmail() != null) {
            predicate.getExpressions()
            .add(builder.like(root.get("contactEmail"), "%" + criteria.getContactEmail() + "%"));
        }

        if (criteria.getPhone() != null) {
            predicate.getExpressions()
            .add(builder.like(root.get("phone"), "%" + criteria.getPhone() + "%"));
        }

        return predicate;
    }

}
