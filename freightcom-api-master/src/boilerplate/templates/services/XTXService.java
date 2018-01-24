package com.freightcom.api.services;

import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.ConfigurablePropertyAccessor;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.freightcom.api.ApiSession;
import com.freightcom.api.model.XTX;
import com.freightcom.api.model.views.View;
import com.freightcom.api.repositories.custom.XTXRepository;
import com.freightcom.api.repositories.custom.XTXSpecification;
import com.freightcom.api.services.converters.XTXConverter;

/**
 * @author bryan
 *
 */
@Component
public class XTXService
{
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final XTXRepository xtxRepository;
    private final PagedResourcesAssembler<View> xtxAssembler;
    private final ApiSession apiSession;
    private final PermissionChecker permissionChecker;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public XTXService(final XTXRepository xtxRepository,
            final PagedResourcesAssembler<View> xtxAssembler,
            final PermissionChecker permissionChecker,
            final ApiSession apiSession)
    {
        this.xtxRepository = xtxRepository;
        this.xtxAssembler = xtxAssembler;
        this.permissionChecker = permissionChecker;
        this.apiSession = apiSession;
    }

    public PagedResources<Resource<View>> getXTXsConverted(Map<String,Object> criteria, Pageable pageable)
    {
        permissionChecker.checkCriteria(criteria, apiSession);
        Page<View> xtxs = xtxRepository.findAll(new XTXSpecification(criteria), pageable)
                .map(new XTXConverter());

        return xtxAssembler.toResource(xtxs, new Link("/xtx"));
    }

    @Transactional
    public XTX createOrUpdateXTX(XTX xtx) throws Exception
    {
        permissionChecker.check(xtx, apiSession);

        xtxRepository.save(xtx);

        return xtx;
    }

    /**
     * @throws Exception
     *
     */
    public XTX createXTX(final XTX xtx, final Map<String,Object> attributes) throws Exception
    {
        XTX newXTX;

        if (xtx.getId() != null) {
            newXTX = new XTX();
            BeanUtils.copyProperties(xtx, newXTX);
        } else {
            newXTX = xtx;
        }

        return createOrUpdateXTX(newXTX);
    }

    /**
     *
     */
    @Transactional
    public XTX updateXTX(Long id, XTX xtx, Map<String,String> attributes) throws Exception
    {
        XTX existing = findOne(id);

        ConfigurablePropertyAccessor source = PropertyAccessorFactory.forDirectFieldAccess(xtx);
        ConfigurablePropertyAccessor dest = PropertyAccessorFactory.forDirectFieldAccess(existing);

        // Only copy attributes supplied in the JSON input, leave others alone
        for (String key : attributes.keySet()) {
            if (dest.isWritableProperty(key)) {
                dest.setPropertyValue(key, source.getPropertyValue(key));
            }
        }

        return createOrUpdateXTX(existing);
    }

    /**
     *
     */
    @Transactional
    public String deleteXTX(Long xtxId)
    {
       XTX xtx = findOne(xtxId);

       xtxRepository.delete(xtx);

       return "ok";
    }

    public XTX findOne(Long id)
    {
        if (id == null) {
            throw new ResourceNotFoundException("No xtx");
        }

        XTX xtx = xtxRepository.findOne(id);

        if (xtx == null) {
            throw new ResourceNotFoundException("No xtx");
        }

        permissionChecker.check(xtx, apiSession);

        return xtx;
    }
}
