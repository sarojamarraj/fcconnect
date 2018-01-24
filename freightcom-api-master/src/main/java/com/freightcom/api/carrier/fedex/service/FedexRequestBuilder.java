package com.freightcom.api.carrier.fedex.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.GregorianCalendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.fedex.ws.rate.v22.Address;
import com.fedex.ws.rate.v22.ClientDetail;
import com.fedex.ws.rate.v22.Dimensions;
import com.fedex.ws.rate.v22.DropoffType;
import com.fedex.ws.rate.v22.LinearUnits;
import com.fedex.ws.rate.v22.PackageSpecialServiceType;
import com.fedex.ws.rate.v22.PackageSpecialServicesRequested;
import com.fedex.ws.rate.v22.Party;
import com.fedex.ws.rate.v22.Payment;
import com.fedex.ws.rate.v22.PaymentType;
import com.fedex.ws.rate.v22.PhysicalPackagingType;
import com.fedex.ws.rate.v22.RateReply;
import com.fedex.ws.rate.v22.RateReplyDocument;
import com.fedex.ws.rate.v22.RateRequest;
import com.fedex.ws.rate.v22.RateRequestDocument;
import com.fedex.ws.rate.v22.RequestedPackageLineItem;
import com.fedex.ws.rate.v22.RequestedShipment;
import com.fedex.ws.rate.v22.SignatureOptionDetail;
import com.fedex.ws.rate.v22.SignatureOptionType;
import com.fedex.ws.rate.v22.SmartPostAncillaryEndorsementType;
import com.fedex.ws.rate.v22.SmartPostIndiciaType;
import com.fedex.ws.rate.v22.SmartPostShipmentDetail;
import com.fedex.ws.rate.v22.TransactionDetail;
import com.fedex.ws.rate.v22.VersionId;
import com.fedex.ws.rate.v22.WebAuthenticationCredential;
import com.fedex.ws.rate.v22.WebAuthenticationDetail;
import com.fedex.ws.rate.v22.Weight;
import com.fedex.ws.rate.v22.WeightUnits;
import com.freightcom.api.carrier.fedex.rate.RateServiceStub;
import com.freightcom.api.model.CustomerOrder;
import com.freightcom.api.model.OrderAccessorials;
import com.freightcom.api.model.Pallet;
import com.freightcom.api.model.ShippingAddress;
import com.freightcom.api.util.Empty;

@Component
@ConfigurationProperties(prefix = "fedex")
public class FedexRequestBuilder
{
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Value("${fedex.courier.ratings.url}")
    private String ratingsURL;

    @Value("${fedex.courier.meter.num}")
    private String meterNumber;

    @Value("${fedex.courier.account.num}")
    private String accountNumber;

    @Value("${fedex.courier.key}")
    private String key;

    @Value("${fedex.courier.password}")
    private String password;

    @Value("${fedex.courier.hub.id}")
    private String hubID;

    public FedexRequestBuilder()
    {
    }

    public RateReply createAndSendRateRequest(CustomerOrder order) throws Exception
    {
        log.debug("START FEDEX REQUEST");

        RateServiceStub connection = new RateServiceStub(ratingsURL);
        RateRequestDocument document = RateRequestDocument.Factory.newInstance();

        RateRequest request = document.addNewRateRequest();

        request.setWebAuthenticationDetail(createWebAuthenticationDetail());
        request.setClientDetail(createClientDetail());

        //
        TransactionDetail transactionDetail = TransactionDetail.Factory.newInstance();

        // The client will get the same value back in the response
        if (! Empty.check(order.getReferenceCode())) {
            transactionDetail.setCustomerTransactionId(order.getReferenceCode());
        } else {
            transactionDetail.setCustomerTransactionId(order.getId()
                    .toString());
        }

        request.setTransactionDetail(transactionDetail);

        //
        setVersion(request);

        //
        RequestedShipment requestedShipment = RequestedShipment.Factory.newInstance();

        requestedShipment.setDropoffType(DropoffType.REGULAR_PICKUP);
        requestedShipment.setShipTimestamp(GregorianCalendar.from(order.getScheduledShipDate()));

        //
        Party shipper = Party.Factory.newInstance();
        shipper.setAddress(getAddress(order.getShipFrom(), order.findAccessorial("Residential Pickup")));
        requestedShipment.setShipper(shipper);

        //
        Party recipient = Party.Factory.newInstance();
        recipient.setAddress(getAddress(order.getShipTo(), order.findAccessorial("Residential Delivery")));
        requestedShipment.setRecipient(recipient);

        //
        Payment shippingChargesPayment = Payment.Factory.newInstance();
        shippingChargesPayment.setPaymentType(PaymentType.SENDER);
        requestedShipment.setShippingChargesPayment(shippingChargesPayment);

        addRatingPackages(order, requestedShipment);

        // PackageSpecialServicesRequested pssr =
        // PackageSpecialServicesRequested.Factory.newInstance();
        // rp.setSpecialServicesRequested(pssr);

        // SmartPost Details
        SmartPostShipmentDetail smartPost = SmartPostShipmentDetail.Factory.newInstance();
        smartPost.setAncillaryEndorsement(SmartPostAncillaryEndorsementType.ADDRESS_CORRECTION);
        smartPost.setCustomerManifestId("XXX");
        // smartPost.setHubId(getHub());
        smartPost.setIndicia(SmartPostIndiciaType.PRESORTED_STANDARD);
        requestedShipment.setSmartPostDetail(smartPost);

        request.setRequestedShipment(requestedShipment);

        request.setReturnTransitAndCommit(true);

        log.debug("Fedex: FEDEX REQUEST " + document);

        // Initialize the service
        // This is the call to the web service passing in a RateRequest and
        // returning a RateReply
        RateReplyDocument replyDocument = connection.getRates(document);
        log.debug("Fedex: FEDEX DONE " + replyDocument);

        RateReply reply = replyDocument.getRateReply();

        log.debug("Notifications\n" + reply.getNotificationsArray());
        log.debug("Details\n" + reply.getRateReplyDetailsArray());

        return reply;
    }

    /**
     * UPS packaging types 00 = UNKNOWN 01 = UPS Letter 02 = Package 03 = Tube
     * 04 = Pak 21 = Express Box 24 = 25KG Box 25 = 10KG Box 30 = Pallet 2a = *
     * Small Express Box 2b = Medium Express Box 2c = Large Express Box
     *
     * @throws Exception
     */
    private void addRatingPackages(final CustomerOrder order, RequestedShipment requestedShipment) throws Exception
    {
        RequestedPackageLineItem packageLineItem;
        Weight weight;
        int count = 0;

        log.debug("FEDEX GET RATES " + order + " " + (order.getPackageTypeCode()));

        switch (order.getPackageTypeCode()) {
        case PACKAGE_ENV:
            packageLineItem = requestedShipment.addNewRequestedPackageLineItems();

            packageLineItem.setPhysicalPackaging(PhysicalPackagingType.ENVELOPE);
            packageLineItem.setGroupPackageCount(safeBigInteger(1));

            weight = Weight.Factory.newInstance();
            weight.setUnits(WeightUnits.LB);
            weight.setValue(BigDecimal.valueOf(1));
            packageLineItem.setWeight(weight);

            addSpecialServices(order, packageLineItem);
            count = 1;

            break;

        case PACKAGE_PAK:
            packageLineItem = requestedShipment.addNewRequestedPackageLineItems();

            packageLineItem.setPhysicalPackaging(PhysicalPackagingType.PIECE);
            packageLineItem.setGroupPackageCount(safeBigInteger(1));

            weight = Weight.Factory.newInstance();
            weight.setUnits(WeightUnits.LB);
            weight.setValue(BigDecimal.valueOf(order.getWeight()));
            packageLineItem.setWeight(weight);

            addSpecialServices(order, packageLineItem);
            count = 1;
            break;

        case PACKAGE_PACKAGE:
            for (com.freightcom.api.model.Package orderPackage : order.getPackages()) {
                packageLineItem = requestedShipment.addNewRequestedPackageLineItems();
                packageLineItem.setPhysicalPackaging(PhysicalPackagingType.PIECE);
                packageLineItem.setGroupPackageCount(safeBigInteger(1));

                weight = Weight.Factory.newInstance();
                weight.setUnits(WeightUnits.LB);
                weight.setValue(new BigDecimal(orderPackage.getWeight()));
                //
                Dimensions dimensions = Dimensions.Factory.newInstance();
                dimensions.setUnits(LinearUnits.IN);
                dimensions.setHeight(safeBigInteger(orderPackage.getHeight()));
                dimensions.setLength(safeBigInteger(orderPackage.getLength()));
                dimensions.setWidth(safeBigInteger(orderPackage.getWidth()));

                packageLineItem.setDimensions(dimensions);
                packageLineItem.setWeight(weight);
                log.debug("UPS RATES ADD PACKAGE " + orderPackage + " " + orderPackage.getWeight());

                addSpecialServices(order, packageLineItem);
                count += 1;
            }
            break;

        case PACKAGE_PALLET:
            for (Pallet pallet : order.getPallets()) {
                log.debug("UPS RATES ADD PALLET " + pallet);
                // TODO drum
                packageLineItem = requestedShipment.addNewRequestedPackageLineItems();
                packageLineItem.setPhysicalPackaging(PhysicalPackagingType.PALLET);
                packageLineItem.setGroupPackageCount(safeBigInteger(pallet.getPieces()));

                weight = Weight.Factory.newInstance();
                weight.setUnits(WeightUnits.LB);
                weight.setValue(new BigDecimal(pallet.getWeight()));
                //
                Dimensions dimensions = Dimensions.Factory.newInstance();
                dimensions.setUnits(LinearUnits.IN);
                dimensions.setHeight(safeBigInteger(pallet.getHeight()));
                dimensions.setLength(safeBigInteger(pallet.getLength()));
                dimensions.setWidth(safeBigInteger(pallet.getWidth()));

                packageLineItem.setDimensions(dimensions);
                packageLineItem.setWeight(weight);
                packageLineItem.setGroupPackageCount(BigInteger.valueOf(pallet.getPieces()));

                addSpecialServices(order, packageLineItem);
                count += pallet.getPieces();
            }
            break;
        }

        requestedShipment.setPackageCount(BigInteger.valueOf(count));
    }

    private BigInteger safeBigInteger(Integer value)
    {
        if (value == null) {
            return BigInteger.valueOf(0);
        } else {
            return BigInteger.valueOf(value);
        }
    }

    private BigInteger safeBigInteger(String value)
    {
        if (value == null) {
            return BigInteger.valueOf(0);
        } else {
            return new BigInteger(value);
        }
    }

    private void addSpecialServices(CustomerOrder order, RequestedPackageLineItem packageLineItem)
    {
        PackageSpecialServicesRequested service = null;

        if (order.getSignatureRequired()
                .equals("Yes")) {
            service = packageLineItem.addNewSpecialServicesRequested();

            SignatureOptionDetail signatureOption = service.addNewSignatureOptionDetail();
            signatureOption.setOptionType(SignatureOptionType.DIRECT);

            service.addSpecialServiceTypes(PackageSpecialServiceType.SIGNATURE_OPTION);
        }

        // if (order.getDangerousGoods() != null) {
        // if (service == null) {
        // service = packageLineItem.addNewSpecialServicesRequested();
        // }
        //
        // service.addSpecialServiceTypes(PackageSpecialServiceType.DANGEROUS_GOODS);
        // }
    }

    private Address getAddress(ShippingAddress modelAddress, OrderAccessorials residentialOption)
    {
        Address address = Address.Factory.newInstance();

        address.addStreetLines(modelAddress.getAddress1());

        if (!Empty.check(modelAddress.getAddress2())) {
            address.addStreetLines(modelAddress.getAddress2());
        }

        address.setCity(modelAddress.getCity());
        address.setStateOrProvinceCode(modelAddress.getProvince());
        address.setPostalCode(modelAddress.getPostalCode());
        address.setCountryCode(modelAddress.getCountry());
        address.setResidential(residentialOption != null);

        return address;
    }

    private void setVersion(RateRequest request)
    {
        VersionId versionId = VersionId.Factory.newInstance();
        versionId.setServiceId("crs");
        versionId.setMajor(22);
        versionId.setIntermediate(0);
        versionId.setMinor(0);
        request.setVersion(versionId);
    }

    private ClientDetail createClientDetail()
    {
        ClientDetail clientDetail = ClientDetail.Factory.newInstance();

        log.debug("AAA " + accountNumber + " " + meterNumber);
        //
        // See if the accountNumber and meterNumber properties are set,
        // if set use those values, otherwise default them to "XXX"
        //
        if (accountNumber == null) {
            accountNumber = "XXX"; // Replace "XXX" with clients account number
        }

        if (meterNumber == null) {
            meterNumber = "XXX"; // Replace "XXX" with clients meter number
        }
        clientDetail.setAccountNumber(accountNumber);
        clientDetail.setMeterNumber(meterNumber);

        return clientDetail;
    }

    private WebAuthenticationDetail createWebAuthenticationDetail()
    {
        WebAuthenticationCredential userCredential = WebAuthenticationCredential.Factory.newInstance();
        log.debug("BBBBB" + " " + key + " " + password);

        if (key == null) {
            key = "XXX"; // Replace "XXX" with clients key
        }
        if (password == null) {
            password = "XXX"; // Replace "XXX" with clients password
        }
        userCredential.setKey(key);
        userCredential.setPassword(password);

        WebAuthenticationCredential parentCredential = null;
        Boolean useParentCredential = false; // Set this value to true is using
                                             // a parent credential
        if (useParentCredential) {

            String parentKey = System.getProperty("parentkey");
            String parentPassword = System.getProperty("parentpassword");
            //
            // See if the parentkey and parentpassword properties are set,
            // if set use those values, otherwise default them to "XXX"
            //
            if (parentKey == null) {
                parentKey = "XXX"; // Replace "XXX" with clients parent key
            }
            if (parentPassword == null) {
                parentPassword = "XXX"; // Replace "XXX" with clients parent
                                        // password
            }
            parentCredential = WebAuthenticationCredential.Factory.newInstance();
            parentCredential.setKey(parentKey);
            parentCredential.setPassword(parentPassword);
        }

        WebAuthenticationDetail detail = WebAuthenticationDetail.Factory.newInstance();

        // detail.setParentCredential(parentCredential);
        detail.setUserCredential(userCredential);

        return detail;
    }

    private String getHub()
    {
        if (!Empty.check(hubID)) {
            return hubID;
        } else {
            return null;
        }
    }
}
