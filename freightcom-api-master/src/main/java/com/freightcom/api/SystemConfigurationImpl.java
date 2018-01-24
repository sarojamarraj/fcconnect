package com.freightcom.api;

import org.springframework.stereotype.Component;

import com.freightcom.api.model.Address;

@Component
public class SystemConfigurationImpl implements  SystemConfiguration
{
    public Address getFromAddress()
    {
        Address fromAddress =  new Address();

        fromAddress.setAddress1("7000 Pine Valley Road");
        fromAddress.setPhoneNo("1-877-335-8740");
        fromAddress.setCity("Woodbridge");
        fromAddress.setPostalCode("L4L 4Y8");
        fromAddress.setCompanyName("Freightcom");

        return fromAddress;
    }
}
