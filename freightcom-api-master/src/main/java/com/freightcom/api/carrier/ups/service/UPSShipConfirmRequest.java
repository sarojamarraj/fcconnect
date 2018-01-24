package com.freightcom.api.carrier.ups.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.freightcom.api.SystemConfiguration;
import com.freightcom.api.carrier.ups.ship.ShipAcceptErrorMessage;
import com.freightcom.api.carrier.ups.ship.ShipConfirmErrorMessage;
import com.freightcom.api.carrier.ups.ship.ShipServiceStub;
import com.freightcom.api.model.CustomerOrder;
import com.freightcom.api.model.Pallet;
import com.freightcom.api.model.ShippingAddress;
import com.freightcom.api.util.Empty;
import com.ups.www.xmlschema.xoltws.common.v1_0.RequestType;
import com.ups.www.xmlschema.xoltws.common.v1_0.TransactionReferenceType;
import com.ups.www.xmlschema.xoltws.ship.v1_0.LabelImageFormatType;
import com.ups.www.xmlschema.xoltws.ship.v1_0.LabelSpecificationType;
import com.ups.www.xmlschema.xoltws.ship.v1_0.PackageType;
import com.ups.www.xmlschema.xoltws.ship.v1_0.PaymentInfoType;
import com.ups.www.xmlschema.xoltws.ship.v1_0.ReferenceNumberType;
import com.ups.www.xmlschema.xoltws.ship.v1_0.ServiceType;
import com.ups.www.xmlschema.xoltws.ship.v1_0.ShipAcceptRequestDocument;
import com.ups.www.xmlschema.xoltws.ship.v1_0.ShipAcceptRequestDocument.ShipAcceptRequest;
import com.ups.www.xmlschema.xoltws.ship.v1_0.ShipAcceptResponseDocument;
import com.ups.www.xmlschema.xoltws.ship.v1_0.ShipAcceptResponseDocument.ShipAcceptResponse;
import com.ups.www.xmlschema.xoltws.ship.v1_0.ShipAddressType;
import com.ups.www.xmlschema.xoltws.ship.v1_0.ShipConfirmRequestDocument;
import com.ups.www.xmlschema.xoltws.ship.v1_0.ShipConfirmRequestDocument.ShipConfirmRequest;
import com.ups.www.xmlschema.xoltws.ship.v1_0.ShipConfirmResponseDocument;
import com.ups.www.xmlschema.xoltws.ship.v1_0.ShipConfirmResponseDocument.ShipConfirmResponse;
import com.ups.www.xmlschema.xoltws.ship.v1_0.ShipFromType;
import com.ups.www.xmlschema.xoltws.ship.v1_0.ShipPhoneType;
import com.ups.www.xmlschema.xoltws.ship.v1_0.ShipToAddressType;
import com.ups.www.xmlschema.xoltws.ship.v1_0.ShipToType;
import com.ups.www.xmlschema.xoltws.ship.v1_0.ShipmentChargeType;
import com.ups.www.xmlschema.xoltws.ship.v1_0.ShipmentType;
import com.ups.www.xmlschema.xoltws.ship.v1_0.ShipperType;
import com.ups.www.xmlschema.xoltws.upss.v1_0.UPSSecurityDocument;
import com.ups.www.xmlschema.xoltws.upss.v1_0.UPSSecurityDocument.UPSSecurity;
import com.ups.www.xmlschema.xoltws.upss.v1_0.UPSSecurityDocument.UPSSecurity.ServiceAccessToken;
import com.ups.www.xmlschema.xoltws.upss.v1_0.UPSSecurityDocument.UPSSecurity.UsernameToken;

@Component
@ConfigurationProperties(prefix = "ups")
public class UPSShipConfirmRequest
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
    public UPSShipConfirmRequest(final SystemConfiguration systemConfiguration)
    {
        this.systemConfiguration = systemConfiguration;
    }

    public ShipAcceptResponse ship(CustomerOrder order) throws Exception
    {
        return shipAccept(order, shipRequest(order));
    }

    public ShipAcceptResponse shipAccept(CustomerOrder order, ShipConfirmResponse confirmResponse) throws Exception
    {
        log.debug("UPS Ship Accept " + shippingURL);
        ShipServiceStub shipServiceStub = new ShipServiceStub(shippingURL);
        ShipAcceptResponse acceptResponse = null;

        ShipAcceptRequestDocument document = ShipAcceptRequestDocument.Factory.newInstance();
        ShipAcceptRequest acceptRequest = document.addNewShipAcceptRequest();

        RequestType request = acceptRequest.addNewRequest();
        request.addNewRequestOption()
                .setStringValue("nonvalidate");
        request.addNewTransactionReference()
                .setCustomerContext(Empty.ifNull(order.getReferenceCode(), order.getId())
                        .toString());
        acceptRequest.setShipmentDigest(confirmResponse.getShipmentResults()
                .getShipmentDigest());

        log.debug("SHIP ACCEPT\n" + document);

        try {
            ShipAcceptResponseDocument acceptResponseDocument = shipServiceStub.processShipAccept(document, security());
            acceptResponse = acceptResponseDocument.getShipAcceptResponse();
            log.debug("SHIP RESPONSE\n" + acceptResponse);
        } catch (ShipAcceptErrorMessage e) {
            log.debug("ERROR IN UPS SHIP " + e.getFaultMessage());
            throw new Exception("Error in UPS Ship", e);
        } catch (Exception e) {
            log.debug("ERROR IN UPS SHIP");
            throw new Exception("Error in UPS Ship", e);
        } finally {
            log.debug("DONE UPS SHIP");
        }

        return acceptResponse;
    }

    public ShipConfirmResponse shipRequest(CustomerOrder order) throws Exception
    {
        ShipConfirmResponse response = null;

        log.debug("UPS Ship Request " + shippingURL);

        ShipServiceStub shipServiceStub = new ShipServiceStub(shippingURL);

        ShipConfirmRequestDocument document = ShipConfirmRequestDocument.Factory.newInstance();
        ShipConfirmRequest shipRequest = document.addNewShipConfirmRequest();

        LabelSpecificationType labelSpecification = shipRequest.addNewLabelSpecification();
        LabelImageFormatType labelFormat = labelSpecification.addNewLabelImageFormat();
        labelFormat.setCode("GIF");

        RequestType request = shipRequest.addNewRequest();

        request.setRequestOptionArray(new String[] { "validate" });

        TransactionReferenceType transactionReference = TransactionReferenceType.Factory.newInstance();
        transactionReference.setCustomerContext(Empty.check(order.getReferenceCode()) ? order.getId()
                .toString() : order.getReferenceCode());

        request.setTransactionReference(transactionReference);
        shipRequest.setRequest(request);

        ShipmentType shipment = shipRequest.addNewShipment();
        // TODO returns

        ReferenceNumberType x = shipment.addNewReferenceNumber();
        x.setValue(order.getId()
                .toString());

        // TODO from customs invoice if cross border
        shipment.setDescription("A shipment");

        addShipper(order, shipment);
        getShipFromAddress(order, shipment);
        getShipToAddress(order, shipment);
        addPayment(order, shipment);
        addService(order, shipment);
        addPackages(order, shipment);
        addPickupDate(order, shipment);

        log.debug("SHIP REQUEST\n" + document);

        try {
            ShipConfirmResponseDocument shipResponse = shipServiceStub.processShipConfirm(document, security());
            response = shipResponse.getShipConfirmResponse();
            log.debug("SHIP RESPONSE\n" + shipResponse);
        } catch (ShipConfirmErrorMessage e) {
            log.debug("ERROR IN UPS SHIP " + e.getFaultMessage());
            throw new Exception("Error in UPS Ship", e);
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
        item.addNewPackaging()
                .setCode("01");
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
        if (order.getCustomsInvoice() != null && "shipper".equalsIgnoreCase(order.getCustomsInvoice().getBill())) {
            addCustomsInvoice(order, shipment);
        } else {
            PaymentInfoType paymentInfo = shipment.addNewPaymentInformation();
            ShipmentChargeType charge = paymentInfo.addNewShipmentCharge();

            charge.setType("01");
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
        ShippingAddress orderAddress = order.getShipFrom();
        ShipperType shipper = shipment.addNewShipper();
        log.debug("SHIPPER  COMPANY " + order.getShipFrom()
                .getConsigneeName());
        shipper.setName(orderAddress                .getConsigneeName());
        ShipAddressType shipperAddress = shipper.addNewAddress();

        shipper.setShipperNumber(accountNumber);

        shipperAddress.addAddressLine(orderAddress.getAddress1());
        shipperAddress.setCity(orderAddress.getCity());
        shipperAddress.setStateProvinceCode(orderAddress.getProvince());
        shipperAddress.setPostalCode(orderAddress.getPostalCode()
                .replaceAll(" ", ""));
        shipperAddress.setCountryCode(orderAddress.getCountry());

        ShipPhoneType phone = shipper.addNewPhone();
        phone.setNumber(orderAddress.getPhone());
    }

    private void getShipFromAddress(CustomerOrder order, ShipmentType shipment)
    {
        ShippingAddress orderAddress = order.getShipFrom();
        ShipFromType shipFrom = shipment.addNewShipFrom();
        ShipAddressType address = shipFrom.addNewAddress();
        ShipPhoneType phone = shipFrom.addNewPhone();

        shipFrom.setName(orderAddress.getConsigneeName());

        address.addAddressLine(orderAddress.getAddress1());
        address.setCity(orderAddress.getCity());
        address.setStateProvinceCode(orderAddress.getProvince());
        address.setPostalCode(orderAddress.getPostalCode()
                .replaceAll(" ", ""));
        address.setCountryCode(orderAddress.getCountry());

        phone.setNumber(orderAddress.getPhone());

    }

    private void getShipToAddress(CustomerOrder order, ShipmentType shipment)
    {
        ShippingAddress orderAddress = order.getShipTo();
        ShipToType shipTo = shipment.addNewShipTo();
        ShipToAddressType address = shipTo.addNewAddress();
        ShipPhoneType phone = shipTo.addNewPhone();

        shipTo.setName(orderAddress.getConsigneeName());
        shipTo.setAttentionName(orderAddress.getContactName());

        address.addAddressLine(orderAddress.getAddress1());
        address.setCity(orderAddress.getCity());
        address.setStateProvinceCode(orderAddress.getProvince());
        address.setPostalCode(orderAddress.getPostalCode()
                .replaceAll(" ", ""));
        address.setCountryCode(orderAddress.getCountry());
        
        if (orderAddress.isResidential()) {
            address.setResidentialAddressIndicator("1");
        }

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
