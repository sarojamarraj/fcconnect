package com.freightcom.api.services;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.freightcom.api.ApiSession;
import com.freightcom.api.model.UserRole;
import com.freightcom.api.model.views.UserRoleView;
import com.freightcom.api.repositories.custom.UserRoleRepository;
import com.freightcom.api.repositories.custom.UserRoleSpecification;
import com.freightcom.api.services.converters.UserRoleConverter;

/**
 * @author bryan
 *
 */
@Component
public class UserRoleService extends ServicesCommon
{
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final UserRoleRepository userRoleRepository;

    private final PagedResourcesAssembler<UserRole> pagedAssembler;
    private final PagedResourcesAssembler<UserRoleView> userRoleAssembler;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public UserRoleService(final UserRoleRepository userRoleRepository,
            final PagedResourcesAssembler<UserRole> pagedAssembler,
            final PagedResourcesAssembler<UserRoleView> userRoleAssembler,
            final ApiSession apiSession)
    {
        super(apiSession);

        this.userRoleRepository = userRoleRepository;
        this.pagedAssembler = pagedAssembler;
        this.userRoleAssembler = userRoleAssembler;
    }

    public PagedResources<Resource<UserRole>> getUserRoles(Map<String,String> criteria, Pageable pageable)
    {
        Page<UserRole> userRoles = userRoleRepository.findAll(new UserRoleSpecification(criteria), pageable);

        return pagedAssembler.toResource(userRoles);
    }

    public PagedResources<Resource<UserRoleView>> getUserRolesConverted(Map<String,String> criteria, Pageable pageable)
    {
        Page<UserRoleView> userRoles = userRoleRepository.findAll(new UserRoleSpecification(criteria), pageable)
                .map(new UserRoleConverter());

        return userRoleAssembler.toResource(userRoles, new Link("/customerUserRole"));
    }

    @Transactional
    public UserRole createOrUpdateUserRole(UserRole userRole) throws Exception
    {
        userRoleRepository.save(userRole);

        return userRole;
    }

    /**
     *
     */
    @Transactional
    public String deleteUserRole(Long userRoleId, UserDetails loggedInUser)
    {
       UserRole userRole = userRoleRepository.findOne(userRoleId);

       if (userRole == null) {
           throw new ResourceNotFoundException("No such userRole");
       }

       userRoleRepository.delete(userRole);

       return "ok";
    }

    public List<UserRole> findByUserIdAndRoleName(Long userId, String roleName) {
        List<UserRole> roles = userRoleRepository.findByUser_Id(userId);

        log.debug("FIND BY USER ID AND ROLE NAME A " + roleName + " " + roles.size());

        if (roles.size() > 0) {
            log.debug("FOUND " + roles.get(0) + " " + roles.get(0).getRoleName());
        }

        roles.removeIf((UserRole role) -> ! role.getRoleName().equalsIgnoreCase(roleName));

        log.debug("FIND BY USER ID AND ROLE NAME B " + roleName + " " + roles.size());

        return roles;
    }

    public Object findByRoleName(String roleName) {
        // TODO Auto-generated method stub
        return null;
    }
}
