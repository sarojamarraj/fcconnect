package com.freightcom.api.carrier.ups.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.freightcom.api.SystemConfiguration;
import com.freightcom.api.carrier.ups.ship.ShipServiceStub;
import com.freightcom.api.model.Address;
import com.freightcom.api.model.CustomerOrder;
import com.freightcom.api.model.Pallet;
import com.freightcom.api.model.ShippingAddress;
import com.ups.www.xmlschema.xoltws.common.v1_0.RequestType;
import com.ups.www.xmlschema.xoltws.common.v1_0.TransactionReferenceType;
import com.ups.www.xmlschema.xoltws.ship.v1_0.PackageType;
import com.ups.www.xmlschema.xoltws.ship.v1_0.PaymentInfoType;
import com.ups.www.xmlschema.xoltws.ship.v1_0.ServiceType;
import com.ups.www.xmlschema.xoltws.ship.v1_0.ShipAddressType;
import com.ups.www.xmlschema.xoltws.ship.v1_0.ShipFromType;
import com.ups.www.xmlschema.xoltws.ship.v1_0.ShipPhoneType;
import com.ups.www.xmlschema.xoltws.ship.v1_0.ShipToAddressType;
import com.ups.www.xmlschema.xoltws.ship.v1_0.ShipToType;
import com.ups.www.xmlschema.xoltws.ship.v1_0.ShipmentChargeType;
import com.ups.www.xmlschema.xoltws.ship.v1_0.ShipmentRequestDocument;
import com.ups.www.xmlschema.xoltws.ship.v1_0.ShipmentRequestDocument.ShipmentRequest;
import com.ups.www.xmlschema.xoltws.ship.v1_0.ShipmentResponseDocument;
import com.ups.www.xmlschema.xoltws.ship.v1_0.ShipmentResponseDocument.ShipmentResponse;
import com.ups.www.xmlschema.xoltws.ship.v1_0.ShipmentType;
import com.ups.www.xmlschema.xoltws.ship.v1_0.ShipperType;
import com.ups.www.xmlschema.xoltws.upss.v1_0.UPSSecurityDocument;
import com.ups.www.xmlschema.xoltws.upss.v1_0.UPSSecurityDocument.UPSSecurity;
import com.ups.www.xmlschema.xoltws.upss.v1_0.UPSSecurityDocument.UPSSecurity.ServiceAccessToken;
import com.ups.www.xmlschema.xoltws.upss.v1_0.UPSSecurityDocument.UPSSecurity.UsernameToken;

@Component
@ConfigurationProperties(prefix = "ups")
public class UPSShipRequest
{
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Value("${ups.account.number}")
    private String accountNumber;

    @Value("${ups.shipping.url}")
    private String shippingURL;

    @Value("${ups.accesskey}")
    private String accessKey;

    @Value("${ups.username}")
    private String username;

    @Value("${ups.password}")
    private String password;

    private final SystemConfiguration systemConfiguration;

    @Autowired
    public UPSShipRequest(final SystemConfiguration systemConfiguration)
    {
        this.systemConfiguration = systemConfiguration;
    }

    public ShipmentResponse ship(CustomerOrder order) throws Exception
    {
        ShipmentResponse response = null;

        log.debug("UPS Ship Request " + shippingURL);

        ShipServiceStub shipServiceStub = new ShipServiceStub(shippingURL);

        ShipmentRequestDocument shipRequestDocument = ShipmentRequestDocument.Factory.newInstance();
        ShipmentRequest shipRequest = shipRequestDocument.addNewShipmentRequest();

        RequestType request = RequestType.Factory.newInstance();
        request.setRequestOptionArray(new String[] { "nonvalidate" });

        TransactionReferenceType transactionReference = TransactionReferenceType.Factory.newInstance();
        transactionReference.setCustomerContext(order.getReferenceCode() == null ? order.getId()
                .toString() : order.getReferenceCode());

        request.setTransactionReference(transactionReference);
        shipRequest.setRequest(request);

        ShipmentType shipment = shipRequest.addNewShipment();

        // TODO from customs invoice if cross border
        shipment.setDescription("A shipment");

        shipment.addNewReferenceNumber()
                .setCode("9");
        

        addShipper(order, shipment);
        getShipFromAddress(order, shipment);
        getShipToAddress(order, shipment);
        addPayment(order, shipment);
        addService(order, shipment);
        addPackages(order, shipment);
        addPickupDate(order, shipment);

        log.debug("SHIP REQUEST\n" + shipRequestDocument);

        try {
            ShipmentResponseDocument shipResponse = shipServiceStub.processShipment(shipRequestDocument, security());
            log.debug("SHIP RESPONSE\n" + shipResponse);
        } catch (Exception e) {
            log.debug("ERROR IN UPS SHIP");
            throw new Exception("Error in UPS Ship", e);
        } finally {
            log.debug("DONE UPS SHIP");
        }

        return response;
    }

    private void addPickupDate(CustomerOrder order, ShipmentType shipment) throws Exception
    {
       
    }

    private void addEnvelope(CustomerOrder order, ShipmentType shipment) throws Exception
    {
        PackageType item = shipment.addNewPackage();
        // TODO - check
       item.addNewPackaging().setCode("01");
    }

    private void addPak(CustomerOrder order, ShipmentType shipment) throws Exception
    {
        PackageType item = shipment.addNewPackage();
    }

    private void addShipmentPackages(CustomerOrder order, ShipmentType shipment) throws Exception
    {
        for (com.freightcom.api.model.Package orderPackage : order.getPackages()) {
            PackageType item = shipment.addNewPackage();
        }
    }

    private void addShipmentPallets(CustomerOrder order, ShipmentType shipment) throws Exception
    {
        for (Pallet pallet : order.getPallets()) {
            PackageType item = shipment.addNewPackage();
        }
    }

    private void addPackages(CustomerOrder order, ShipmentType shipment) throws Exception
    {
        switch (order.getPackageTypeCode()) {
        case PACKAGE_ENV:
            addEnvelope(order, shipment);
            break;

        case PACKAGE_PAK:
            addPak(order, shipment);
            break;

        case PACKAGE_PACKAGE:
            addShipmentPackages(order, shipment);
            break;

        case PACKAGE_PALLET:
            addShipmentPallets(order, shipment);
            break;

        default:
            throw new Exception("BAD PACKAGE TYPE");
        }
    }

    private void addService(CustomerOrder order, ShipmentType shipment)
    {
        ServiceType service = shipment.addNewService();

        service.setCode(order.getSelectedQuote()
                .getService()
                .getCode());
    }

    private void addPayment(CustomerOrder order, ShipmentType shipment)
    {
        if (false) {
            addCustomsInvoice(order, shipment);
        } else {
            PaymentInfoType paymentInfo = shipment.addNewPaymentInformation();
            ShipmentChargeType charge = paymentInfo.addNewShipmentCharge();

            charge.addNewBillShipper()
                    .setAccountNumber(accountNumber);
        }
    }

    private void addCustomsInvoice(CustomerOrder order, ShipmentType shipment)
    {
        // TODO
    }

    private void addShipper(CustomerOrder order, ShipmentType shipment)
    {
        ShipperType shipper = shipment.addNewShipper();
        shipper.setName(order.getShipFrom()
                .getConsigneeName());
        ShipAddressType shipperAddress = shipper.addNewAddress();
        Address freightcomAddress = systemConfiguration.getFromAddress();

        // TODO system configuration proper address
        shipperAddress.addAddressLine(freightcomAddress.getAddress1());
        shipperAddress.setCity(freightcomAddress.getCity());
        shipperAddress.setStateProvinceCode(freightcomAddress.getProvince().getName());
        shipperAddress.setPostalCode(freightcomAddress.getPostalCode());
        shipperAddress.setCountryCode(freightcomAddress.getCountry().getName());

        // TODO phone number
        ShipPhoneType phone = shipper.addNewPhone();
        phone.setNumber("416-555-3432");
    }

    private void getShipFromAddress(CustomerOrder order, ShipmentType shipment)
    {
        ShippingAddress orderAddress = order.getShipFrom();
        ShipFromType shipFrom = shipment.addNewShipFrom();
        ShipAddressType address = shipFrom.addNewAddress();
        ShipPhoneType phone = shipFrom.addNewPhone();

        address.addAddressLine(orderAddress.getAddress1());
        address.setCity(orderAddress.getCity());
        address.setStateProvinceCode(orderAddress.getProvince());
        address.setPostalCode(orderAddress.getPostalCode());
        address.setCountryCode(orderAddress.getCountry());

        phone.setNumber(orderAddress.getPhone());

    }

    private void getShipToAddress(CustomerOrder order, ShipmentType shipment)
    {
        ShippingAddress orderAddress = order.getShipTo();
        ShipToType shipTo = shipment.addNewShipTo();
        ShipToAddressType address = shipTo.addNewAddress();
        ShipPhoneType phone = shipTo.addNewPhone();

        address.addAddressLine(orderAddress.getAddress1());
        address.setCity(orderAddress.getCity());
        address.setStateProvinceCode(orderAddress.getProvince());
        address.setPostalCode(orderAddress.getPostalCode());
        address.setCountryCode(orderAddress.getCountry());

        phone.setNumber(orderAddress.getPhone());

    }

    private UPSSecurityDocument security()
    {
        UPSSecurity upsSecurity = UPSSecurity.Factory.newInstance();
        ServiceAccessToken upsSvcToken = ServiceAccessToken.Factory.newInstance();
        upsSvcToken.setAccessLicenseNumber(accessKey);
        upsSecurity.setServiceAccessToken(upsSvcToken);

        UsernameToken upsSecUsrnameToken = UsernameToken.Factory.newInstance();
        upsSecUsrnameToken.setUsername(username);
        upsSecUsrnameToken.setPassword(password);
        upsSecurity.setUsernameToken(upsSecUsrnameToken);

        UPSSecurityDocument securityDocument = UPSSecurityDocument.Factory.newInstance();
        securityDocument.setUPSSecurity(upsSecurity);

        return securityDocument;
    }
}
