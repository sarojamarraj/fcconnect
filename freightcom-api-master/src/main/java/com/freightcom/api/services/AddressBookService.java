package com.freightcom.api.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.freightcom.api.ApiSession;
import com.freightcom.api.model.AddressBook;
import com.freightcom.api.model.UserRole;
import com.freightcom.api.repositories.custom.CustomAddressBookRepository;
import com.freightcom.api.repositories.ObjectBase;
import com.freightcom.api.repositories.custom.AddressBookSearchCriteria;
import com.freightcom.api.repositories.custom.AddressBookSpecification;

@Component
public class AddressBookService
{
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final CustomAddressBookRepository addressBookRepository;
    private final PagedResourcesAssembler<AddressBook> pagedAssembler;
    private final ApiSession apiSession;
    private final ObjectBase objectBase;

    @Autowired
    public AddressBookService(final CustomAddressBookRepository addressBookRepository,
            final ApiSession apiSession, final PagedResourcesAssembler<AddressBook> pagedAssembler, final ObjectBase objectBase)
    {
        this.addressBookRepository = addressBookRepository;
        this.pagedAssembler = pagedAssembler;
        this.apiSession = apiSession;
        this.objectBase = objectBase;
    }


    public boolean hasAddressBook()
    {
        if (apiSession.isFreightcom()) {
            return objectBase.hasAddressBook();
        } else {
            return objectBase.hasAddressBook(apiSession.getCustomerId());
        }
    }


    public boolean hasAddressBook(Long customerId)
    {
        return objectBase.hasAddressBook(customerId);
    }


    public PagedResources<Resource<AddressBook>> getAddresses(AddressBookSearchCriteria criteria, Pageable pageable)
    {
        log.debug("HERE THERE THERE THERE");

        Page<AddressBook> addresses = addressBookRepository.findAll(new AddressBookSpecification(criteria), pageable);

        return pagedAssembler.toResource(addresses);
    }

    @Transactional
    public AddressBook createOrUpdateAddressBook(AddressBook addressBook) throws Exception
    {
        addressBookRepository.save(addressBook);

        return addressBook;
    }

    /**
     * @throws Exception
     *
     */
    public AddressBook createAddressBook(final AddressBook addressBook) throws Exception
    {
        AddressBook newAddressBook;

        if (addressBook.getId() != null) {
            newAddressBook = new AddressBook();
            BeanUtils.copyProperties(addressBook, newAddressBook);
        } else {
            newAddressBook = addressBook;
        }

        if (apiSession.getRole().isCustomer()) {
            if (newAddressBook.getCustomerId() == null) {
                newAddressBook.setCustomerId(apiSession.getRole().getCustomerId());
            } else if (! newAddressBook.getCustomerId().equals(apiSession.getRole().getCustomerId())) {
                throw new AccessDeniedException("Not authorized");
            }
        }

        return createOrUpdateAddressBook(newAddressBook);
    }

    /**
     *
     */
    public AddressBook updateAddressBook(Long id, AddressBook addressBook, UserDetails loggedInUser, UserRole userRole) throws Exception
    {
        return createOrUpdateAddressBook(addressBook);
    }

    public String delete(Long id, UserDetailsImpl loggedInUser)
    {
        String result = "ok";

        AddressBook addressBook = addressBookRepository.findOne(id);

        if (addressBook == null) {
            throw new ResourceNotFoundException("No addressbook");
        }

        addressBookRepository.delete(addressBook);


        return result;
    }

}
