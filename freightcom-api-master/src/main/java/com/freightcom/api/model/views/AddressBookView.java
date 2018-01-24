package com.freightcom.api.model.views;

import java.time.ZonedDateTime;

import org.springframework.hateoas.core.Relation;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.freightcom.api.model.AddressBook;

@Relation(collectionRelation = "addressBook")
public class AddressBookView
{
    private final AddressBook addressBook;

    public AddressBookView(AddressBook addressBook)
    {
        this.addressBook = addressBook;
    }

    public Long getId() {
        return addressBook.getId();
    }

    public String getCountry() {
        return addressBook.getCountry();
    }

    public String getCity() {
        return addressBook.getCity();
    }

    public String getConsigneeName() {
        return addressBook.getConsigneeName();
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    public ZonedDateTime getCreatedAt() {
        return addressBook.getCreatedAt();
    }

    public Integer getDefaultTo() {
        return addressBook.getDefaultTo();
    }

    public Integer getNotify() {
        return addressBook.getNotify();
    }

    public String getContactEmail() {
        return addressBook.getContactEmail();
    }

    public String getProvince() {
        return addressBook.getProvince();
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    public ZonedDateTime getUpdatedAt() {
        return addressBook.getUpdatedAt();
    }

    public String getCountryName() {
        return addressBook.getCountryName();
    }

    public Integer getDefaultFrom() {
        return addressBook.getDefaultFrom();
    }

    public String getConsigneeId() {
        return addressBook.getConsigneeId();
    }

    public String getContactName() {
        return addressBook.getContactName();
    }

    public String getDistributionListName() {
        return addressBook.getDistributionListName();
    }

    public String getAddress2() {
        return addressBook.getAddress2();
    }

    public String getAddress1() {
        return addressBook.getAddress1();
    }

    public Long getAddressId() {
        return addressBook.getAddressId();
    }

    public ZonedDateTime getDeletedAt() {
        return addressBook.getDeletedAt();
    }

    public String getTaxId() {
        return addressBook.getTaxId();
    }

    public Boolean getResidential() {
        return addressBook.getResidential();
    }

    public String getPhone() {
        return addressBook.getPhone();
    }

    public String getInstruction() {
        return addressBook.getInstruction();
    }

    public Long getCustomerId() {
        return addressBook.getCustomerId();
    }

    public String getPostalCode() {
        return addressBook.getPostalCode();
    }

}
