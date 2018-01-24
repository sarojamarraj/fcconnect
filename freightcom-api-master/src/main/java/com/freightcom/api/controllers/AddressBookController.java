package com.freightcom.api.controllers;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.freightcom.api.ApiSession;
import com.freightcom.api.NoRoleSelectedException;
import com.freightcom.api.model.AddressBook;
import com.freightcom.api.model.views.AddressBookView;
import com.freightcom.api.repositories.AddressBookRepository;
import com.freightcom.api.repositories.custom.AddressBookSearchCriteria;
import com.freightcom.api.services.AddressBookService;
import com.freightcom.api.services.UserDetailsImpl;
import com.freightcom.api.util.MapBuilder;

@RestController
@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('CUSTOMER_STAFF') or hasAuthority('CUSTOMER_ADMIN')")
public class AddressBookController extends BaseController
{
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final AddressBookService addressBookService;
    private final AddressBookRepository addressBookRepository;
    private final ApiSession apiSession;

    public AddressBookController(final AddressBookService addressBookService,
            final AddressBookRepository addressBookRepository, final ApiSession apiSession)
    {
        this.addressBookService = addressBookService;
        this.apiSession = apiSession;
        this.addressBookRepository = addressBookRepository;
    }

    @RequestMapping(value = "/hasAddressBook", method = RequestMethod.GET)
    @ResponseBody
    public Object hasAddressBook()
    {
        if (addressBookService.hasAddressBook()) {
            return MapBuilder.ok("hasAddressBook", "yes").toMap();
        } else {
            return MapBuilder.ok().toMap();
        }
    }

    @RequestMapping(value = "/hasAddressBook/{customerId:\\d+}", method = RequestMethod.GET)
    @ResponseBody
    public Object hasAddressBook(@PathVariable(value = "customerId") Long customerId)
    {
        if (addressBookService.hasAddressBook(customerId)) {
            return MapBuilder.ok("hasAddressBook", "yes").toMap();
        } else {
            return MapBuilder.ok().toMap();
        }
    }

    @RequestMapping(value = "/addressbook", method = RequestMethod.GET)
    @ResponseBody
    public PagedResources<Resource<AddressBook>> getAddressBooksAlt(
            @RequestParam(value = "customer_id", required = false) Long customerId,
            @RequestParam(value = "consigneeName", required = false) String company,
            @RequestParam(value = "address", required = false) String address,
            @RequestParam(value = "city", required = false) String city,
            @RequestParam(value = "province", required = false) String province,
            @RequestParam(value = "country", required = false) String country,
            @RequestParam(value = "postalCode", required = false) String postalCode,
            @RequestParam(value = "contactName", required = false) String contactName,
            @RequestParam(value = "contactEmail", required = false) String contactEmail,
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "q", required = false) String query, Principal principal,
            HttpServletResponse response, Pageable pageable) throws IOException, NoRoleSelectedException
    {
        return getAddressBooks(customerId, company, address, city, province, country, postalCode, contactName,
                contactEmail, phone, query, principal, response, pageable);
    }

    @RequestMapping(value = "/addressBook", method = RequestMethod.GET)
    @ResponseBody
    public PagedResources<Resource<AddressBook>> getAddressBooksAlt2(
            @RequestParam(value = "customer_id", required = false) Long customerId,
            @RequestParam(value = "consigneeName", required = false) String company,
            @RequestParam(value = "address", required = false) String address,
            @RequestParam(value = "city", required = false) String city,
            @RequestParam(value = "province", required = false) String province,
            @RequestParam(value = "country", required = false) String country,
            @RequestParam(value = "postalCode", required = false) String postalCode,
            @RequestParam(value = "contactName", required = false) String contactName,
            @RequestParam(value = "contactEmail", required = false) String contactEmail,
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "q", required = false) String query, Principal principal,
            HttpServletResponse response, Pageable pageable) throws IOException, NoRoleSelectedException
    {
        return getAddressBooks(customerId, company, address, city, province, country, postalCode, contactName,
                contactEmail, phone, query, principal, response, pageable);
    }

    @RequestMapping(value = "/address_book", method = RequestMethod.GET)
    @ResponseBody
    public PagedResources<Resource<AddressBook>> getAddressBooks(
            @RequestParam(value = "customer_id", required = false) Long customerId,
            @RequestParam(value = "consigneeName", required = false) String company,
            @RequestParam(value = "address", required = false) String address,
            @RequestParam(value = "city", required = false) String city,
            @RequestParam(value = "province", required = false) String province,
            @RequestParam(value = "country", required = false) String country,
            @RequestParam(value = "postalCode", required = false) String postalCode,
            @RequestParam(value = "contactName", required = false) String contactName,
            @RequestParam(value = "contactEmail", required = false) String contactEmail,
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "q", required = false) String query, Principal principal,
            HttpServletResponse response, Pageable pageable) throws IOException, NoRoleSelectedException
    {
        AddressBookSearchCriteria criteria = new AddressBookSearchCriteria();

        apiSession.check();

        if (apiSession.isCustomer()) {
            if (customerId == null) {
                customerId = apiSession.getCustomerId();
            } else if (!customerId.equals(apiSession.getCustomerId())) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            }
        }

        log.debug("CUSTOMER ID " + customerId);

        if (query != null) {
            criteria.setQuery(query);
        }

        if (customerId != null) {
            criteria.setCustomerId(customerId);
        }

        if (company != null) {
            criteria.setCompany(company);
        }

        if (address != null) {
            criteria.setAddress(address);
        }

        if (city != null) {
            criteria.setCity(city);
        }

        if (province != null) {
            criteria.setProvince(province);
        }

        if (country != null) {
            criteria.setCountry(country);
        }

        if (postalCode != null) {
            criteria.setPostalCode(postalCode);
        }

        if (contactName != null) {
            criteria.setContactName(contactName);
        }

        if (contactEmail != null) {
            criteria.setContactEmail(contactEmail);
        }

        if (phone != null) {
            criteria.setPhone(phone);
        }

        return addressBookService.getAddresses(criteria, pageable);
    }

    @RequestMapping(value = "/address_book/{id:\\d+}", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<AddressBookView> updateAddressBook(@PathVariable(value = "id") Long id,
            @RequestBody AddressBook addressBook, Principal principal) throws Exception
    {
        UserDetailsImpl userDetails = getLoggedInUser(principal);

        AddressBook entry = addressBookRepository.findOne(id);

        if (entry == null) {
            throw new ResourceNotFoundException("No such address book entry");
        }

        addressBook.setCustomerId(entry.getCustomerId());
        addressBookService.updateAddressBook(id, addressBook, userDetails, apiSession.getRole());

        return new ResponseEntity<AddressBookView>(new AddressBookView(addressBook), HttpStatus.OK);
    }

    @RequestMapping(value = "/address_book", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<AddressBookView> create_addressBook(@RequestBody AddressBook addressBookData) throws Exception
    {
        AddressBook addressBook = addressBookService.createAddressBook(addressBookData);
        log.debug("create addressBook " + addressBookData);

        return new ResponseEntity<AddressBookView>(new AddressBookView(addressBook), HttpStatus.OK);
    }

    /**
     *
     */
    @RequestMapping(value = "/address_book/{id:\\d+}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<String> realDeleteOrder(@PathVariable("id") Long id, Principal principal) throws Exception {
        List<String> result = new ArrayList<String>(1);

        result.add(addressBookService.delete(id, getLoggedInUser(principal)));

        return result;
    }


}
