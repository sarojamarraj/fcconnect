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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.freightcom.api.ApiSession;
import com.freightcom.api.model.Markup;
import com.freightcom.api.model.UserRole;
import com.freightcom.api.model.views.MarkupView;
import com.freightcom.api.repositories.custom.MarkupRepository;
import com.freightcom.api.repositories.custom.MarkupSpecification;
import com.freightcom.api.services.converters.MarkupConverter;

/**
 * @author bryan
 *
 */
@Component
public class MarkupService
{
    @SuppressWarnings("unused")
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final MarkupRepository markupRepository;

    private final PagedResourcesAssembler<Markup> pagedAssembler;
    private final PagedResourcesAssembler<MarkupView> markupAssembler;
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public MarkupService(final MarkupRepository markupRepository,
            final PagedResourcesAssembler<Markup> pagedAssembler,
            final PagedResourcesAssembler<MarkupView> markupAssembler,
            final ApiSession apiSession)
    {
        this.markupRepository = markupRepository;
        this.pagedAssembler = pagedAssembler;
        this.markupAssembler = markupAssembler;
    }

    public PagedResources<Resource<Markup>> getMarkups(Map<String,String> criteria, Pageable pageable)
    {
        Page<Markup> markups = markupRepository.findAll(new MarkupSpecification(criteria), pageable);

        return pagedAssembler.toResource(markups);
    }

    public PagedResources<Resource<MarkupView>> getMarkupsConverted(Map<String,String> criteria, Pageable pageable)
    {
        Page<MarkupView> markups = markupRepository.findAll(new MarkupSpecification(criteria), pageable)
                .map(new MarkupConverter());

        return markupAssembler.toResource(markups, new Link("/customerMarkup"));
    }

    @Transactional
    public Markup createOrUpdateMarkup(Markup markup) throws Exception
    {
        markupRepository.save(markup);

        return markup;
    }

    /**
     * @throws Exception
     *
     */
    public Markup createMarkup(final Markup markup) throws Exception
    {
        Markup newMarkup;

        if (markup.getId() != null) {
            newMarkup = new Markup();
            BeanUtils.copyProperties(markup, newMarkup);
        } else {
            newMarkup = markup;
        }

        return createOrUpdateMarkup(newMarkup);
    }

    /**
     *
     */
    @Transactional
    public Markup updateMarkup(Long id, Markup markup, Map<String,String> attributes, UserDetails loggedInUser, UserRole userRole) throws Exception
    {
        Markup existing = markupRepository.findOne(id);

        if (existing == null) {
            throw new ResourceNotFoundException("No such markup");
        }

        ConfigurablePropertyAccessor source = PropertyAccessorFactory.forDirectFieldAccess(markup);
        ConfigurablePropertyAccessor dest = PropertyAccessorFactory.forDirectFieldAccess(existing);

        // Only copy attributes supplied in the JSON input, leave others alone
        for (String key : attributes.keySet()) {
            if (dest.isWritableProperty(key)) {
                dest.setPropertyValue(key, source.getPropertyValue(key));
            }
        }

        return createOrUpdateMarkup(existing);
    }

    /**
     *
     */
    @Transactional
    public String deleteMarkup(Long markupId, UserDetails loggedInUser)
    {
       Markup markup = markupRepository.findOne(markupId);

       if (markup == null) {
           throw new ResourceNotFoundException("No such markup");
       }

       markupRepository.delete(markup);

       return "ok";
    }
}
