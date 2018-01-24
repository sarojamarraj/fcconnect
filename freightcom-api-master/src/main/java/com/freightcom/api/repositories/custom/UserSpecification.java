package com.freightcom.api.repositories.custom;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;

import com.freightcom.api.model.Admin;
import com.freightcom.api.model.Agent;
import com.freightcom.api.model.CustomerAdmin;
import com.freightcom.api.model.CustomerStaff;
import com.freightcom.api.model.FreightcomStaff;
import com.freightcom.api.model.User;
import com.freightcom.api.model.UserRole;

public class UserSpecification extends BaseSpecification implements Specification<User>
{
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @PersistenceContext
    private EntityManager entityManager;

    private Map<String, Object> criteria;

    public UserSpecification(Map<String, Object> criteria)
    {
        this.criteria = criteria;
    }

    /**
     * login, name, email, phone, enabled, and role
     */
    @Override
    public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder builder)
    {
        Predicate predicate = builder.conjunction();
        new SimpleDateFormat("yyyy-MM-dd");

        for (Map.Entry<String, Object> entry : criteria.entrySet()) {
            log.debug("USER SPECIFICATION " + entry.getKey() + " " + entry.getValue());

            switch (entry.getKey()
                    .toLowerCase()) {
            case "customer_id":
            case "customerid":
                predicate.getExpressions()
                        .add(customerId(entry.getValue(), root, query, builder));
                break;

            case "customer_staff_id":
            case "customerstaffid":
                predicate.getExpressions()
                        .add(customerStaffId(entry.getValue(), root, query, builder));
                break;

            case "customer_admin_id":
            case "customeradminid":
                predicate.getExpressions()
                        .add(customerAdminId(entry.getValue(), root, query, builder));
                break;

            case "name":
                Predicate disjunction = builder.disjunction();

                disjunction.getExpressions()
                        .add(builder.like(root.get("firstname"), likePattern(entry)));
                disjunction.getExpressions()
                        .add(builder.like(root.get("lastname"), likePattern(entry)));
                disjunction.getExpressions()
                        .add(builder.like(builder.function("CONCAT", String.class, root.get("firstname"),
                                builder.literal(" "), root.get("lastname")), likePattern(entry)));

                predicate.getExpressions()
                        .add(disjunction);
                break;

            case "namefields":
                Predicate names = builder.disjunction();

                names.getExpressions()
                        .add(builder.like(root.get("login"), likePattern(entry)));
                names.getExpressions()
                        .add(builder.like(root.get("email"), likePattern(entry)));
                names.getExpressions()
                        .add(builder.like(root.get("firstname"), likePattern(entry)));
                names.getExpressions()
                        .add(builder.like(root.get("lastname"), likePattern(entry)));
                names.getExpressions()
                        .add(builder.like(builder.function("CONCAT", String.class, root.get("firstname"),
                                builder.literal(" "), root.get("lastname")), likePattern(entry)));

                predicate.getExpressions()
                        .add(names);
                break;

            case "firstname":
                predicate.getExpressions()
                        .add(builder.like(root.get("firstname"), likePattern(entry)));
                break;

            case "lastname":
                predicate.getExpressions()
                        .add(builder.like(root.get("lastname"), likePattern(entry)));
                break;

            case "login":
                predicate.getExpressions()
                        .add(builder.like(root.get("login"), likePattern(entry)));
                break;

            case "role":
                predicate.getExpressions()
                        .add(userRole(entry.getValue(), root, query, builder));
                break;

            case "parentsalesagentid":
                predicate.getExpressions()
                        .add(hasParentAgent(entry.getValue(), root, query, builder));
                break;

            case "enabled":
                if (entry.getValue()
                        .toString()
                        .equalsIgnoreCase("active") || yesValue(entry.getValue())) {
                    predicate.getExpressions()
                            .add(builder.isTrue(root.get("enabled")));
                } else if (entry.getValue()
                        .toString()
                        .equalsIgnoreCase("inactive") || !yesValue(entry.getValue())) {
                    predicate.getExpressions()
                            .add(builder.isFalse(root.get("enabled")));
                }
                break;

            case "phone":
                predicate.getExpressions()
                        .add(builder.like(
                                builder.function("replace", String.class, root.get("phone"), builder.literal("-"),
                                        builder.literal("")),
                                "%" + ((String) entry.getValue()).replaceAll("-", "") + "%"));
                break;

            case "email":
                predicate.getExpressions()
                        .add(builder.like(root.get("email"), likePattern(entry)));
                break;
            }
        }

        return predicate;
    }

    /**
     *
     *
     */
    private Predicate userRole(Object roleSelector, Root<User> root, CriteriaQuery<?> query, CriteriaBuilder builder)
    {
        Predicate predicate = builder.disjunction();

        //
        Object roleName;

        if (roleSelector.toString()
                .equalsIgnoreCase("staff")
                || roleSelector.toString()
                        .equalsIgnoreCase("customer_staff")) {
            roleName = CustomerStaff.class;
        } else if (roleSelector.toString()
                .equalsIgnoreCase("customer_admin")) {
            roleName = CustomerAdmin.class;
        } else if (roleSelector.toString()
                .equalsIgnoreCase("freightcom_staff")) {
            roleName = FreightcomStaff.class;
        } else if (roleSelector.toString()
                .equalsIgnoreCase("agent")) {
            roleName = Agent.class;
        } else {
            roleName = Admin.class;
        }

        log.debug("ROLE NAME IS '" + roleName + "'");

        Subquery<UserRole> userRoleQuery = query.subquery(UserRole.class);
        Root<UserRole> userRole = userRoleQuery.from(UserRole.class);

        userRoleQuery.select(userRole);

        List<Predicate> predicates = new ArrayList<Predicate>();
        predicates.add(builder.equal(userRole.type(), roleName));
        predicates.add(builder.equal(userRole.get("user")
                .get("id"), root.get("id")));

        userRoleQuery.where(predicates.toArray(new Predicate[] {}));

        predicate.getExpressions()
                .add(builder.exists(userRoleQuery));

        return predicate;
    }

    /**
     *
     *
     */
    private Subquery<UserRole> customerIdQuery(Class<? extends UserRole> classObject, Object customerId,
            Root<User> root, CriteriaQuery<?> query, CriteriaBuilder builder)
    {
        Subquery<UserRole> userRoleQuery = query.subquery(UserRole.class);
        Root<UserRole> userRole = userRoleQuery.from(UserRole.class);

        userRoleQuery.select(userRole);

        List<Predicate> predicates = new ArrayList<Predicate>();
        predicates.add(builder.equal(userRole.type(), classObject));
        predicates.add(builder.equal(userRole.get("user")
                .get("id"), root.get("id")));

        if (classObject == CustomerStaff.class) {
            predicates.add(builder.equal(builder.treat(userRole, classObject)
                    .get("staffCustomerId"), customerId));
        } else {
            predicates.add(builder.equal(builder.treat(userRole, classObject)
                    .get("customerId"), customerId));
        }

        userRoleQuery.where(predicates.toArray(new Predicate[] {}));

        return userRoleQuery;
    }

    /**
     *
     *
     */
    private Predicate customerStaffId(Object customerId, Root<User> root, CriteriaQuery<?> query,
            CriteriaBuilder builder)
    {
        Predicate predicate = builder.disjunction();

        predicate.getExpressions()
                .add(builder.exists(customerIdQuery(CustomerStaff.class, customerId, root, query, builder)));

        return predicate;
    }

    /**
     *
     *
     */
    private Predicate customerAdminId(Object customerId, Root<User> root, CriteriaQuery<?> query,
            CriteriaBuilder builder)
    {
        Predicate predicate = builder.disjunction();

        predicate.getExpressions()
                .add(builder.exists(customerIdQuery(CustomerAdmin.class, customerId, root, query, builder)));

        return predicate;
    }

    /**
     *
     *
     */
    private Predicate customerId(Object customerId, Root<User> root, CriteriaQuery<?> query, CriteriaBuilder builder)
    {
        Predicate predicate = builder.disjunction();

        predicate.getExpressions()
                .add(builder.exists(customerIdQuery(CustomerStaff.class, customerId, root, query, builder)));
        predicate.getExpressions()
                .add(builder.exists(customerIdQuery(CustomerAdmin.class, customerId, root, query, builder)));

        return predicate;
    }

    /**
     *
     *
     */
    private Predicate hasParentAgent(Object parentId, Root<User> root, CriteriaQuery<?> query, CriteriaBuilder builder)
    {
        Predicate predicate = builder.disjunction();
        Subquery<UserRole> userRoleQuery = query.subquery(UserRole.class);
        Root<UserRole> userRole = userRoleQuery.from(UserRole.class);

        userRoleQuery.select(userRole);

        List<Predicate> predicates = new ArrayList<Predicate>();
        predicates.add(builder.equal(userRole.type(), Agent.class));
        predicates.add(builder.equal(userRole.get("user")
                .get("id"), root.get("id")));
        predicates.add(
                builder.isTrue(builder.function("check_subagent", Boolean.class, builder.treat(userRole, Agent.class)
                        .get("parentSalesAgent")
                        .get("id"), builder.literal(parentId))));

        userRoleQuery.where(predicates.toArray(new Predicate[] {}));

        predicate.getExpressions()
                .add(builder.exists(userRoleQuery));

        return predicate;
    }

}
