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
import com.freightcom.api.model.Admin;
import com.freightcom.api.model.Agent;
import com.freightcom.api.model.CustomerAdmin;
import com.freightcom.api.model.CustomerStaff;
import com.freightcom.api.model.FreightcomStaff;
import com.freightcom.api.model.UserRole;

public class UserRoleSpecification extends BaseSpecification implements Specification<UserRole>
{
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final Map<String, String> criteria;

    public UserRoleSpecification(final Map<String, String> criteria)
    {
        this.criteria = criteria;
    }
    
    public void put(String key,String value) 
    {
        criteria.put(key, value);
    }

    @Override
    public Predicate toPredicate(Root<UserRole> root, CriteriaQuery<?> query, CriteriaBuilder builder)
    {
        Predicate predicate = builder.conjunction();
        String ancestorId = criteria.get("ancestorid");

        predicate.getExpressions()
            .add(builder.isNull(root.get("user").get("deletedAt")));

        for (Map.Entry<String, String> entry : criteria.entrySet()) {
            String key = entry.getKey()
                    .toLowerCase();
            String value = entry.getValue();

            log.debug("USER ROLE MATCH " + key + " = " + value);

            switch (key) {

            case "id":
                predicate.getExpressions()
                        .add(builder.equal(root.get("id"), value));
                break;

            case "type":
                Object roleName;

                if (value
                        .equalsIgnoreCase("staff")
                        || value
                                .equalsIgnoreCase("customer_staff")) {
                    roleName = CustomerStaff.class;
                } else if (value
                        .equalsIgnoreCase("customer_admin")) {
                    roleName = CustomerAdmin.class;
                } else if (value
                        .equalsIgnoreCase("freightcom_staff")) {
                    roleName = FreightcomStaff.class;
                } else if (value
                        .equalsIgnoreCase("agent")) {
                    roleName = Agent.class;
                } else {
                    roleName = Admin.class;
                }

                predicate.getExpressions()
                        .add(builder.equal(root.type(), roleName));
                break;

            case "name":
                predicate.getExpressions()
                        .add(matchName(value, root, query, builder));
                break;

            case "parentname":
                addLike(predicate, builder.treat(root, Agent.class).get("parentSalesAgentName"), value, root, builder);
                break;

            case "phone":
                addLike(predicate, "phone", value, root, builder);
                break;

            case "parentid":
                predicate.getExpressions()
                        .add(builder.equal(builder.treat(root, Agent.class).get("parentSalesAgent").get("id"), value));
                break;

            case "ancestorid":
                predicate.getExpressions().add(builder.isTrue(builder.function("check_subagent", Boolean.class, root.get("id"),
                builder.literal(ancestorId))));
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

        return predicate;
    }

    /**
     *
     *
     */
    private Predicate matchName(Object name, Root<UserRole> root, CriteriaQuery<?> query, CriteriaBuilder builder)
    {
        Predicate disjunction = builder.disjunction();

        disjunction.getExpressions()
                .add(builder.like(builder.treat(root, Agent.class).get("agentName"), likePattern(name)));

        disjunction.getExpressions()
                .add(builder.like(root.get("user").get("firstname"), likePattern(name)));

        disjunction.getExpressions()
                .add(builder.like(root.get("user").get("lastname"), likePattern(name)));

        disjunction.getExpressions()
                        .add(builder.like(builder.function("CONCAT", String.class, root.get("user").get("firstname"),
                                builder.literal(" "), root.get("user").get("lastname")), likePattern(name)));

        return disjunction;
    }

}
