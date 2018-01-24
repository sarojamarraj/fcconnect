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

import com.freightcom.api.model.CurrencyExchange;
import com.freightcom.api.model.views.View;
import com.freightcom.api.repositories.ObjectBase;
import com.freightcom.api.repositories.custom.CurrencyExchangeRepository;
import com.freightcom.api.repositories.custom.CurrencyExchangeSpecification;
import com.freightcom.api.services.converters.CurrencyExchangeConverter;

/**
 * @author bryan
 *
 */
@Component
public class CurrencyExchangeService
{
    @SuppressWarnings("unused")
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final CurrencyExchangeRepository currencyExchangeRepository;
    private final PagedResourcesAssembler<View> currencyExchangeAssembler;
    private final PermissionChecker permissionChecker;
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public CurrencyExchangeService(final CurrencyExchangeRepository currencyExchangeRepository,
            final PagedResourcesAssembler<View> currencyExchangeAssembler,
            final PermissionChecker permissionChecker,
            final ObjectBase objectBase)
    {
        this.currencyExchangeRepository = currencyExchangeRepository;
        this.currencyExchangeAssembler = currencyExchangeAssembler;
        this.permissionChecker = permissionChecker;
    }

    public PagedResources<Resource<View>> getCurrencyExchangesConverted(Map<String,Object> criteria, Pageable pageable)
    {
        Page<View> currencyExchanges = null;

        if (permissionChecker.isFreightcom()) {
            currencyExchanges = currencyExchangeRepository.findAll(new CurrencyExchangeSpecification(criteria), pageable)
                .map(new CurrencyExchangeConverter(true));
        } else {
            currencyExchanges = currencyExchangeRepository.findAll(new CurrencyExchangeSpecification(criteria), pageable)
                .map(new CurrencyExchangeConverter());
        }

        return currencyExchangeAssembler.toResource(currencyExchanges, new Link("/currency-exchange"));
    }

    @Transactional
    public CurrencyExchange createOrUpdateCurrencyExchange(CurrencyExchange currencyExchange) throws Exception
    {
        permissionChecker.check(currencyExchange);
        permissionChecker.setUpdatedBy(currencyExchange);

        currencyExchangeRepository.save(currencyExchange);

        return currencyExchange;
    }

    /**
     * @param attributes
     * @throws Exception
     *
     */
    public CurrencyExchange createCurrencyExchange(final CurrencyExchange currencyExchange, Map<String, Object> attributes) throws Exception
    {
        CurrencyExchange newCurrencyExchange;

        if (currencyExchange.getId() != null) {
            newCurrencyExchange = new CurrencyExchange();
            BeanUtils.copyProperties(currencyExchange, newCurrencyExchange);
        } else {
            newCurrencyExchange = currencyExchange;
        }

        return createOrUpdateCurrencyExchange(newCurrencyExchange);
    }

    /**
     *
     */
    @Transactional
    public CurrencyExchange updateCurrencyExchange(Long id, CurrencyExchange currencyExchange, Map<String,String> attributes) throws Exception
    {
        CurrencyExchange existing = findOne(id);

        ConfigurablePropertyAccessor source = PropertyAccessorFactory.forDirectFieldAccess(currencyExchange);
        ConfigurablePropertyAccessor dest = PropertyAccessorFactory.forDirectFieldAccess(existing);

        // Only copy attributes supplied in the JSON input, leave others alone
        for (String key : attributes.keySet()) {
            if (dest.isWritableProperty(key)) {
                dest.setPropertyValue(key, source.getPropertyValue(key));
            }
        }

        return createOrUpdateCurrencyExchange(existing);
    }

    /**
     *
     */
    @Transactional
    public String deleteCurrencyExchange(Long currencyExchangeId)
    {
       CurrencyExchange currencyExchange = findOne(currencyExchangeId);

       currencyExchangeRepository.delete(currencyExchange);

       return "ok";
    }

    public CurrencyExchange findOne(Long id)
    {
        if (id == null) {
            throw new ResourceNotFoundException("No currencyExchange");
        }

        CurrencyExchange currencyExchange = currencyExchangeRepository.findOne(id);

        if (currencyExchange == null) {
            throw new ResourceNotFoundException("No currencyExchange");
        }

        permissionChecker.check(currencyExchange);

        return currencyExchange;
    }
}
