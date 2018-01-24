package com.freightcom.api.model.views;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.core.Relation;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.freightcom.api.model.User;
import com.freightcom.api.model.UserRole;

@Relation(value = "user", collectionRelation = "user")
public class UserView implements View
{
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final User user;
    private final UserRole role;
    private final boolean isSelf;

    public UserView(User user, UserRole role)
    {
        this.user = user;
        this.role = role;
        this.isSelf = false;

        if (user == null) {
            log.debug("NULL USER TO USER VIEW");
            throw new Error("Null user to user view");
        }

        if (role == null) {
            log.debug("NULL ROLE TO USER VIEW");
            throw new Error("Null role to user view " + user);
        }
    }

    public UserView(User user, UserRole role, boolean isSelf)
    {
        this.user = user;
        this.role = role;
        this.isSelf = isSelf;

        if (user == null) {
            log.debug("NULL USER TO USER VIEW");
            throw new Error("Null user to user view");
        }

        if (role == null && !isSelf) {
            log.debug("NULL ROLE TO USER VIEW");
            throw new Error("Null role to user view " + user);
        }
    }

    public Long getId()
    {
        return user.getId();
    }

    public String getViewName()
    {
        return "User View R";
    }

    public Collection<UserRoleView> getAuthorities()
    {
        return user.getAuthorities()
                .stream()
                .sequential()
                .filter(userRole -> isSelf || role.canView(userRole))
                .map(userRole -> {
                    try {
                        return UserRoleViewFactory.get(userRole);
                    } catch (Exception e) {
                        return null;
                    }
                })
                .collect(Collectors.toList());
    }

    public String getEmail()
    {
        return user.getEmail();
    }

    public String getFax()
    {
        return user.getFax();
    }

    public Boolean getFirstLoginComplete()
    {
        return user.getFirstLoginComplete();
    }

    public String getSubType()
    {
        return user.getSubType();
    }

    public String getPhone()
    {
        return user.getPhone();
    }

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm")
    public ZonedDateTime getUpdatedAt()
    {
        return user.getUpdatedAt();
    }

    public Boolean getDeleted()
    {
        return user.getDeleted();
    }

    public String getLastname()
    {
        return user.getLastname();
    }

    public boolean getEnabled()
    {
        return user.getEnabled();
    }

    public String getCell()
    {
        return user.getCell();
    }

    public Boolean getFirstTimeLogin()
    {
        return user.getFirstTimeLogin();
    }

    public String getType()
    {
        return user.getType();
    }

    public String getRoles()
    {
        return user.getAuthorities()
                .stream()
                .sequential()
                .filter(userRole -> isSelf || role.canView(userRole))
                .map(role -> role.getPrettyRoleName())
                .filter(s -> s != null && !s.isEmpty())
                .collect(Collectors.joining(", "));
    }

    public String getLogin()
    {
        return user.getLogin();
    }

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm")
    public ZonedDateTime getPasswordExpiry()
    {
        return user.getPasswordExpiry();
    }

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm")
    public ZonedDateTime getCreatedAt()
    {
        return user.getCreatedAt();
    }

    public String getFirstname()
    {
        return user.getFirstname();
    }
}
