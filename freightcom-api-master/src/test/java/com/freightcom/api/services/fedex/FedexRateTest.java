package com.freightcom.api.services.fedex;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Properties;

import org.apache.axis2.databinding.types.NonNegativeInteger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import com.fedex.ws.rate.v22.Address;
import com.fedex.ws.rate.v22.ClientDetail;
import com.fedex.ws.rate.v22.Dimensions;
import com.fedex.ws.rate.v22.DropoffType;
import com.fedex.ws.rate.v22.LinearUnits;
import com.fedex.ws.rate.v22.PackagingType;
import com.fedex.ws.rate.v22.Party;
import com.fedex.ws.rate.v22.Payment;
import com.fedex.ws.rate.v22.PaymentType;
import com.fedex.ws.rate.v22.RateReply;
import com.fedex.ws.rate.v22.RateReplyDocument;
import com.fedex.ws.rate.v22.RateRequest;
import com.fedex.ws.rate.v22.RateRequestDocument;
import com.fedex.ws.rate.v22.RequestedPackageLineItem;
import com.fedex.ws.rate.v22.RequestedShipment;
import com.fedex.ws.rate.v22.ServiceType;
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

/**
 * @author bryan
 *
 */
public class FedexRateTest
{
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private static Properties properties;
    public static String url = "https://wsbeta.fedex.com:443/web-services/rate";

    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception
    {
        properties = PropertiesLoaderUtils
                .loadProperties(new FileSystemResource("c:/users/bryan/.spring-boot-devtools.properties"));
    }

    /**
     * @throws java.lang.Exception
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception
    {
    }

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception
    {
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception
    {
    }

    /**
     * @throws Exception
     *
     */
    @Test
    public void testPurchase() throws Exception
    {
        boolean getAllRatesFlag = true;

        try {
            log.debug(properties.toString());

            RateServiceStub connection = new RateServiceStub(url);
            RateRequestDocument document = RateRequestDocument.Factory.newInstance();


            RateRequest request = document.addNewRateRequest();

            request.setWebAuthenticationDetail(createWebAuthenticationDetail());
            request.setClientDetail(createClientDetail());

            //
            TransactionDetail transactionDetail = TransactionDetail.Factory.newInstance();

            // The client will get the same value back in the response
            transactionDetail.setCustomerTransactionId("java sample - Rate Request");
            request.setTransactionDetail(transactionDetail);

            //
            VersionId versionId = VersionId.Factory.newInstance();
            versionId.setServiceId("crs");
            versionId.setMajor(22);
            versionId.setIntermediate(0);
            versionId.setMinor(0);
            request.setVersion(versionId);

            //
            RequestedShipment requestedShipment = RequestedShipment.Factory.newInstance();

            requestedShipment.setShipTimestamp(Calendar.getInstance());
            requestedShipment.setDropoffType(DropoffType.REGULAR_PICKUP);
            
            if (!getAllRatesFlag) {
                requestedShipment.setServiceType(ServiceType.SMART_POST);
                requestedShipment.setPackagingType(PackagingType.YOUR_PACKAGING);
            }

            //
            Party shipper = Party.Factory.newInstance();
            Address shipperAddress = Address.Factory.newInstance(); // Origin
                                                                    // information
            shipperAddress.addStreetLines("Address Line 1");
            shipperAddress.setCity("City Name");
            shipperAddress.setStateOrProvinceCode("TN");
            shipperAddress.setPostalCode("38115");
            shipperAddress.setCountryCode("US");
            shipper.setAddress(shipperAddress);

            requestedShipment.setShipper(shipper);

            //
            Party recipient = Party.Factory.newInstance();
            Address recipientAddress = Address.Factory.newInstance(); // Destination
                                                                      // information
            recipientAddress.addStreetLines("Address Line 1");
            recipientAddress.setCity("City Name");
            recipientAddress.setStateOrProvinceCode("CA");
            recipientAddress.setPostalCode("91765");
            recipientAddress.setCountryCode("US");

            recipient.setAddress(recipientAddress);
            requestedShipment.setRecipient(recipient);

            //
            Payment shippingChargesPayment = Payment.Factory.newInstance();
            shippingChargesPayment.setPaymentType(PaymentType.SENDER);
            requestedShipment.setShippingChargesPayment(shippingChargesPayment);

            RequestedPackageLineItem rp = requestedShipment.addNewRequestedPackageLineItems();
            rp.setGroupPackageCount(new NonNegativeInteger("1"));
            Weight weight = Weight.Factory.newInstance();
            weight.setUnits(WeightUnits.LB);
            weight.setValue(new BigDecimal(0.5));
            //
            Dimensions dimensions = Dimensions.Factory.newInstance();
            dimensions.setUnits(LinearUnits.IN);
            dimensions.setHeight(new BigInteger("6"));
            dimensions.setLength(new BigInteger("6"));
            dimensions.setWidth(new BigInteger("6"));
            
            rp.setDimensions(dimensions);
            rp.setWeight(weight);

            //PackageSpecialServicesRequested pssr = PackageSpecialServicesRequested.Factory.newInstance();
            //rp.setSpecialServicesRequested(pssr);
            

            // SmartPost Details
            SmartPostShipmentDetail smartPost = SmartPostShipmentDetail.Factory.newInstance();
            smartPost.setAncillaryEndorsement(SmartPostAncillaryEndorsementType.ADDRESS_CORRECTION);
            smartPost.setCustomerManifestId("XXX");
            // smartPost.setHubId(getHub());
            smartPost.setIndicia(SmartPostIndiciaType.PRESORTED_STANDARD);
            requestedShipment.setSmartPostDetail(smartPost);

            requestedShipment.setPackageCount(new NonNegativeInteger("1"));
            request.setRequestedShipment(requestedShipment);
            
            request.setReturnTransitAndCommit(true);

            // Initialize the service
            // This is the call to the web service passing in a RateRequest and
            // returning a RateReply
            RateReplyDocument replyDocument = connection.getRates(document);
            log.debug("DONE " + replyDocument);
            
            RateReply reply = replyDocument.getRateReply();
            
            log.debug("Notifications\n" + reply.getNotificationsArray());
            log.debug("Details\n" + reply.getRateReplyDetailsArray());
        } catch (Exception e) {
            log.debug("RATE " + e);
            log.debug("RATE " + e.getMessage());
            throw e;
        }
    }

    private ClientDetail createClientDetail()
    {
        ClientDetail clientDetail = ClientDetail.Factory.newInstance();

        String accountNumber = properties.getProperty("fedex.courier.test.account.num");
        String meterNumber = properties.getProperty("fedex.courier.test.meter.num");

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
        String key = properties.getProperty("fedex.courier.test.key");
        String password = properties.getProperty("fedex.courier.test.password");

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

        //detail.setParentCredential(parentCredential);
        detail.setUserCredential(userCredential);

        return detail;
    }

    private String getHub()
    {
        String hubID = properties.getProperty("fedex.courier.test.hub.id");

        if (hubID != null) {
            return hubID;
        } else {
            return null;
        }
    }
}
