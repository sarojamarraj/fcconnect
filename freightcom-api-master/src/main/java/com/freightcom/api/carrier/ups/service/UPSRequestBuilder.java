package com.freightcom.api.carrier.ups.service;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.freightcom.api.carrier.ups.wsdl.rate.request.AddressType;
import com.freightcom.api.carrier.ups.wsdl.rate.request.CodeDescriptionType;
import com.freightcom.api.carrier.ups.wsdl.rate.request.DeliveryConfirmationType;
import com.freightcom.api.carrier.ups.wsdl.rate.request.DimensionsType;
import com.freightcom.api.carrier.ups.wsdl.rate.request.HazMatType;
import com.freightcom.api.carrier.ups.wsdl.rate.request.PackageServiceOptionsType;
import com.freightcom.api.carrier.ups.wsdl.rate.request.PackageType;
import com.freightcom.api.carrier.ups.wsdl.rate.request.RatingServiceSelectionRequest;
import com.freightcom.api.carrier.ups.wsdl.rate.request.RequestType;
import com.freightcom.api.carrier.ups.wsdl.rate.request.ShipFromType;
import com.freightcom.api.carrier.ups.wsdl.rate.request.ShipToType;
import com.freightcom.api.carrier.ups.wsdl.rate.request.ShipmentType;
import com.freightcom.api.carrier.ups.wsdl.rate.request.ShipperType;
import com.freightcom.api.carrier.ups.wsdl.rate.request.UnitOfMeasurementType;
import com.freightcom.api.carrier.ups.wsdl.rate.request.WeightType;
import com.freightcom.api.carrier.ups.wsdl.rate.response.RatingServiceSelectionResponse;
import com.freightcom.api.carrier.ups.wsdl.timeintransit.request.CodeType;
import com.freightcom.api.carrier.ups.wsdl.timeintransit.request.MonetaryType;
import com.freightcom.api.carrier.ups.wsdl.timeintransit.request.TimeInTransitRequest;
import com.freightcom.api.carrier.ups.wsdl.timeintransit.request.TransitFromType;
import com.freightcom.api.carrier.ups.wsdl.timeintransit.request.TransitToAddressArtifactFormatType;
import com.freightcom.api.carrier.ups.wsdl.timeintransit.request.TransitToType;
import com.freightcom.api.carrier.ups.wsdl.timeintransit.response.TimeInTransitResponse;
import com.freightcom.api.carrier.ups.wsdl.tools.AccessRequest;
import com.freightcom.api.carrier.ups.wsdl.tools.ObjectFactory;
import com.freightcom.api.model.Customer;
import com.freightcom.api.model.CustomerOrder;
import com.freightcom.api.model.OrderAccessorials;
import com.freightcom.api.model.Pallet;
import com.freightcom.api.model.ShippingAddress;

@Component
@ConfigurationProperties(prefix = "ups")
public class UPSRequestBuilder
{
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private static final String UNIT_INCHES_STRING = "IN";
    private static final String UNIT_LBS_STRING = "LBS";
    private static final String UNIT_KGS_STRING = "KGS";
    private final RestTemplate connection;

    @Value("${ups.account.number}")
    private String accountNumber;

    @Value("${ups.ratings.url}")
    private String ratingsURL;

    @Value("${ups.time.in.service.url}")
    private String timeInServiceURL;

    @Value("${ups.accesskey}")
    private String accessKey;

    @Value("${ups.username}")
    private String username;

    @Value("${ups.password}")
    private String password;

    @Autowired
    public UPSRequestBuilder(final RestTemplate connection)
    {
        this.connection = connection;
    }

    public RatingServiceSelectionResponse createAndSendRateRequest(CustomerOrder order, boolean getTimes)
            throws Exception
    {
        return createAndSendRateRequest(order, null, getTimes);
    }

    /**
     * @param order
     * @param serviceId
     * @param getTimes
     * @return
     * @throws Exception
     */
    public RatingServiceSelectionResponse createAndSendRateRequest(CustomerOrder order, String serviceId,
            boolean getTimes) throws Exception
    {
        StringWriter stringWriter = null;
        RatingServiceSelectionResponse result = null;

        try {
            // Create JAXBContext and marshaller for AccessRequest object
            JAXBContext accessRequestJAXBC = JAXBContext.newInstance(AccessRequest.class.getPackage()
                    .getName());
            Marshaller accessRequestMarshaller = accessRequestJAXBC.createMarshaller();
            ObjectFactory accessRequestObjectFactory = new ObjectFactory();
            AccessRequest accessRequest = accessRequestObjectFactory.createAccessRequest();
            populateAccessRequest(accessRequest);

            // Create JAXBContext and marshaller for
            // RatingServiceSelectionRequest object
            JAXBContext rateRequestJAXBC = JAXBContext.newInstance(RatingServiceSelectionRequest.class.getPackage()
                    .getName());
            Marshaller rateRequestMarshaller = rateRequestJAXBC.createMarshaller();
            com.freightcom.api.carrier.ups.wsdl.rate.request.ObjectFactory requestObjectFactory = new com.freightcom.api.carrier.ups.wsdl.rate.request.ObjectFactory();
            RatingServiceSelectionRequest rateRequest = requestObjectFactory.createRatingServiceSelectionRequest();
            populateRatingServiceSelectionRequest(rateRequest, order, getTimes ? "Shoptimeintransit" : "Shop");

            // Get String out of access request and rate request objects.
            stringWriter = new StringWriter();
            accessRequestMarshaller.marshal(accessRequest, stringWriter);
            rateRequestMarshaller.marshal(rateRequest, stringWriter);
            stringWriter.flush();
            stringWriter.close();

            String payload = stringWriter.getBuffer()
                    .toString();
            log.debug("UPS Request:\n" + payload);

            String strResults = contactService(payload, ratingsURL);

            log.debug("UPS Result\n" + strResults + "\n===\n");

            // ------ Parse Response ---
            JAXBContext jc = JAXBContext.newInstance("com.freightcom.api.carrier.ups.wsdl.rate.response");
            Unmarshaller rateResponseUnmarshaller = jc.createUnmarshaller();
            ByteArrayInputStream input = new ByteArrayInputStream(strResults.getBytes());
            result = (RatingServiceSelectionResponse) rateResponseUnmarshaller.unmarshal(input);
        } finally {
            try {
                if (stringWriter != null) {
                    stringWriter.close();
                    stringWriter = null;
                }
            } catch (Exception e) {
                log.error(e.toString());
                // ignore cleanup errors
            }
        }

        return result;
    }

    /**
     * @param requestStr
     * @param url
     * @return
     * @throws Exception
     */
    private String contactService(String requestStr, String url) throws Exception
    {
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.TEXT_PLAIN);
        headers.set("Referer", "Freightcom Inc.");
        HttpEntity<String> entity = new HttpEntity<String>(requestStr, headers);

        log.debug("CONNECT UPS URL '" + url + "'");
        log.debug("RATINGS ENITTY '" + entity + "'");

        ResponseEntity<String> response = connection.postForEntity(url, entity, String.class);

        log.debug("RESPONSE '" + response + "'");

        if (response.getStatusCode() != HttpStatus.OK) {
            log.error("Method failed: " + response.getStatusCode());
        }

        // Read the response body.
        return response.getBody();
    }

    /**
     * Populates the access request object.
     *
     * @param accessRequest
     */
    private void populateAccessRequest(AccessRequest accessRequest)
    {
        accessRequest.setAccessLicenseNumber(accessKey);
        accessRequest.setUserId(username);
        accessRequest.setPassword(password);
    }

    /**
     * Populate RatingServiceSelectionRequest object
     *
     * @param rateRequest
     * @throws Exception
     */
    private void populateRatingServiceSelectionRequest(RatingServiceSelectionRequest rateRequest,
            final CustomerOrder order, String command) throws Exception
    {
        RequestType request = new RequestType();
        request.setRequestOption(command == null ? "Shoptimeintransit" : command);
        rateRequest.setRequest(request);

        ShipmentType shipment = new ShipmentType();

        /*********************** Start Shipper ****************************/
        Customer customer = order.getCustomer();

        ShipperType shipper = new ShipperType();
        shipper.setName("Carol");
        shipper.setShipperNumber(accountNumber);
        AddressType shipperAddress = new AddressType();
        shipperAddress.setAddressLine1(customer.getAddress());
        shipperAddress.setCity(customer.getCity());
        shipperAddress.setStateProvinceCode(customer.getProvince());
        shipperAddress.setPostalCode(customer.getPostalCode());
        shipperAddress.setCountryCode(mapCountry(customer.getCountry()));
        shipper.setAddress(shipperAddress);
        shipment.setShipper(shipper);
        /************************* End Shipper ****************************/

        /***************** Start ShipTo *****************************/
        ShipToType shipTo = new ShipToType();
        shipTo.setCompanyName(order.getShipTo()
                .getCompanyName());
        shipTo.setAddress(getAddress(order.getShipTo(), order.findAccessorial("Residential Delivery")));
        shipment.setShipTo(shipTo);
        /***************** End ShipTo *****************************/

        /***************** Start ShipFrom *****************************/
        ShipFromType shipFrom = new ShipFromType();
        shipFrom.setCompanyName(order.getShipFrom()
                .getCompanyName());
        shipFrom.setAddress(getAddress(order.getShipFrom(), order.findAccessorial("Residential Pickup")));
        shipment.setShipFrom(shipFrom);
        /***************** End ShipFrom *****************************/

        addRatingPackages(order, shipment.getPackage());
        shipment.setNumOfPieces(order.getNumberOfPieces()
                .toString());

        rateRequest.setShipment(shipment);
    }

    /**
     * UPS packaging types 00 = UNKNOWN 01 = UPS Letter 02 = Package 03 = Tube
     * 04 = Pak 21 = Express Box 24 = 25KG Box 25 = 10KG Box 30 = Pallet 2a = *
     * Small Express Box 2b = Medium Express Box 2c = Large Express Box
     *
     * @throws Exception
     */
    private void addRatingPackages(final CustomerOrder order, List<PackageType> packages) throws Exception
    {
        int i;
        PackageType packageType;
        CodeDescriptionType code;
        WeightType pkgWeight;
        UnitOfMeasurementType UOMType;

        log.debug("UPS GET RATES " + order + " " + (order.getPackageTypeCode()));

        switch (order.getPackageTypeCode()) {
        case PACKAGE_ENV:
            packageType = new PackageType();
            code = new CodeDescriptionType();

            code.setCode("01");
            code.setDescription("UPS Letter");

            packageType.setPackagingType(code);
            addSignature(packageType, order);
            addDangerous(packageType, order);
            packages.add(packageType);
            break;

        case PACKAGE_PAK:
            packageType = new PackageType();
            code = new CodeDescriptionType();

            code.setCode("04");
            code.setDescription("Pak");

            pkgWeight = new WeightType();
            UOMType = new UnitOfMeasurementType();
            UOMType.setCode("lbs");
            UOMType.setDescription("Pounds");
            pkgWeight.setUnitOfMeasurement(UOMType);
            pkgWeight.setWeight(order.getWeight() == null ? "0" : order.getWeight()
                    .toString());

            packageType.setPackageWeight(pkgWeight);
            packageType.setPackagingType(code);
            addSignature(packageType, order);
            addDangerous(packageType, order);
            packages.add(packageType);
            break;

        case PACKAGE_PACKAGE:
            i = 0;

            for (com.freightcom.api.model.Package orderPackage : order.getPackages()) {
                log.debug("UPS RATES ADD PACKAGE " + orderPackage + " " + orderPackage.getWeight());

                packageType = new PackageType();
                code = new CodeDescriptionType();

                code.setCode("02");
                code.setDescription("package");

                pkgWeight = new WeightType();
                UOMType = new UnitOfMeasurementType();
                UOMType.setCode("lbs");
                UOMType.setDescription("Pounds");
                pkgWeight.setUnitOfMeasurement(UOMType);
                pkgWeight.setWeight(orderPackage.getWeight());

                packageType.setPackagingType(code);
                packageType.setPackageWeight(pkgWeight);
                addSignature(packageType, order);
                addDangerous(packageType, order, i);

                packages.add(packageType);
                i++;
            }
            break;

        case PACKAGE_PALLET:
            i = 0;

            for (Pallet pallet : order.getPallets()) {
                log.debug("UPS RATES ADD PALLET " + pallet);

                packageType = new PackageType();
                code = new CodeDescriptionType();

                code.setCode("30");
                code.setDescription("pallet");

                pkgWeight = new WeightType();
                UOMType = new UnitOfMeasurementType();
                UOMType.setCode("lbs");
                UOMType.setDescription("Pounds");
                pkgWeight.setUnitOfMeasurement(UOMType);
                pkgWeight.setWeight(pallet.getWeight());
                addDangerous(packageType, order, i);

                DimensionsType dimensions = new DimensionsType();

                UnitOfMeasurementType dimensionsUnit = new UnitOfMeasurementType();
                // IN OR CM
                dimensionsUnit.setCode("IN");
                dimensions.setUnitOfMeasurement(dimensionsUnit);
                dimensions.setHeight(pallet.getHeight());
                dimensions.setWidth(pallet.getWidth());
                dimensions.setLength(pallet.getLength());

                packageType.setDimensions(dimensions);

                packageType.setPackagingType(code);
                packageType.setPackageWeight(pkgWeight);
                packages.add(packageType);
                i++;
            }
            break;
        }
    }

    private void addSignature(PackageType packageObject, CustomerOrder order)
    {
        if (order.getSignatureRequired()
                .equals("Yes")) {
            PackageServiceOptionsType serviceOptions = new PackageServiceOptionsType();
            DeliveryConfirmationType deliveryConfirmation = new DeliveryConfirmationType();

            deliveryConfirmation.setDCISType("2");

            serviceOptions.setDeliveryConfirmation(deliveryConfirmation);
            packageObject.setPackageServiceOptions(serviceOptions);
        }
    }

    private void addDangerous(PackageType packageObject, CustomerOrder order)
    {
        addDangerous(packageObject, order, 1);
    }

    private void addDangerous(PackageType packageObject, CustomerOrder order, int i)
    {
        if (order.getDangerousGoods() != null) {
            PackageServiceOptionsType serviceOptions = packageObject.getPackageServiceOptions();

            if (serviceOptions == null) {
                serviceOptions = new PackageServiceOptionsType();
                packageObject.setPackageServiceOptions(serviceOptions);
            }

            HazMatType hazMat = new HazMatType();

            hazMat.setPackageIdentifier((new Integer(i)).toString());

            serviceOptions.setHazMat(hazMat);
        }
    }

    /********************
     * Package***************** if ("Shoptimeintransit".equals(command)) {
     * PackageType packageType = new PackageType(); CodeDescriptionType
     * packagingType = new CodeDescriptionType();
     *
     * packagingType.setCode("02");
     *
     * packagingType.setDescription("UPS");
     * packageType.setPackagingType(packagingType);
     *
     * WeightType pkgWeight = new WeightType(); UnitOfMeasurementType UOMType =
     * new UnitOfMeasurementType(); UOMType.setCode("lbs");
     * UOMType.setDescription("Pounds");
     * pkgWeight.setUnitOfMeasurement(UOMType); pkgWeight.setWeight("15");
     * packageType.setPackageWeight(pkgWeight); List<PackageType> packages =
     * shipment.getPackage();
     *
     * PackageServiceOptionsType serviceOptions = new
     * PackageServiceOptionsType(); ShipperDeclaredValueType declaredValue = new
     * ShipperDeclaredValueType(); declaredValue.setCurrencyCode("CAD");
     * declaredValue.setMonetaryValue("30");
     * serviceOptions.setShipperDeclaredValue(declaredValue);
     * packageType.setPackageServiceOptions(serviceOptions); DimensionsType
     * dimensions = new DimensionsType(); dimensions.setHeight("10");
     * dimensions.setWidth("20"); dimensions.setLength("30"); //
     * UnitOfMeasurementType unit = new UnitOfMeasurementType(); //
     * unit.setCode("IN"); // dimensions.setUnitOfMeasurement(unit);
     * packageType.setDimensions(dimensions); packages.add(packageType); }
     */

    private AddressType getAddress(ShippingAddress modelAddress, OrderAccessorials residentialOption)
    {
        AddressType requestAddress = new AddressType();

        requestAddress.setAddressLine1(modelAddress.getAddress1());
        requestAddress.setAddressLine2(modelAddress.getAddress2());
        requestAddress.setCity(modelAddress.getCity());
        requestAddress.setStateProvinceCode(modelAddress.getProvince());
        requestAddress.setPostalCode(modelAddress.getPostalCode());
        requestAddress.setCountryCode(modelAddress.getCountry());

        if (residentialOption != null) {
            requestAddress.setResidentialAddressIndicator("yes");
        }

        return requestAddress;
    }

    private String mapCountry(final String country)
    {
        if ("canada".equalsIgnoreCase(country)) {
            return "CA";
        } else {
            return "US";
        }
    }

    public TimeInTransitResponse createAndSendTimeInTransitRequest(CustomerOrder order) throws Exception
    {
        StringWriter stringWriter = null;
        TimeInTransitResponse result = null;

        try {
            // Create JAXBContext and marshaller for AccessRequest object
            JAXBContext accessRequestJAXBC = JAXBContext.newInstance(AccessRequest.class.getPackage()
                    .getName());
            Marshaller accessRequestMarshaller = accessRequestJAXBC.createMarshaller();
            ObjectFactory accessRequestObjectFactory = new ObjectFactory();
            AccessRequest accessRequest = accessRequestObjectFactory.createAccessRequest();
            populateAccessRequest(accessRequest);

            // Create JAXBContext and marshaller for
            // RatingServiceSelectionRequest object
            JAXBContext timeInTranistRequestJAXBC = JAXBContext.newInstance(TimeInTransitRequest.class.getPackage()
                    .getName());
            Marshaller timeInTranistRequestMarshaller = timeInTranistRequestJAXBC.createMarshaller();
            com.freightcom.api.carrier.ups.wsdl.timeintransit.request.ObjectFactory requestObjectFactory = new com.freightcom.api.carrier.ups.wsdl.timeintransit.request.ObjectFactory();
            TimeInTransitRequest timeInTranistRequest = requestObjectFactory.createTimeInTransitRequest();
            populateTimeInTransitRequest(timeInTranistRequest, order);

            // Get String out of access request and rate request objects.
            stringWriter = new StringWriter();
            accessRequestMarshaller.marshal(accessRequest, stringWriter);
            timeInTranistRequestMarshaller.marshal(timeInTranistRequest, stringWriter);
            stringWriter.flush();
            stringWriter.close();

            String payload = stringWriter.getBuffer()
                    .toString();
            System.out.println("Request: " + payload);

            String strResults = contactService(payload, timeInServiceURL);

            System.out.print("What is this:::::\n" + strResults + "\n===\n");

            // ------ Parse Response ---
            try {

                JAXBContext jc = JAXBContext.newInstance("com.freightcom.api.carrier.ups.wsdl.timeintransit.response");

                Unmarshaller timeInTransitResponseUnmarshaller = jc.createUnmarshaller();

                ByteArrayInputStream input = new ByteArrayInputStream(strResults.getBytes());

                Object jaxbObject = timeInTransitResponseUnmarshaller.unmarshal(input);

                result = (TimeInTransitResponse) jaxbObject;

            } catch (Exception e) {
                System.out.println("****See Error here");
                e.printStackTrace();
            }

        } catch (Exception e) {
            System.out.println(e.getClass());

            e.printStackTrace();

        } finally {
            try {
                if (stringWriter != null) {
                    stringWriter.close();
                    stringWriter = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    /**
     * PopulatetimeInTransitRequest object
     *
     * @param timeInTransitRequest
     */
    private void populateTimeInTransitRequest(TimeInTransitRequest timeInTransitRequest, final CustomerOrder order)
    {
        com.freightcom.api.carrier.ups.wsdl.timeintransit.request.Request request = new com.freightcom.api.carrier.ups.wsdl.timeintransit.request.Request();

        request.setRequestAction("TimeInTransit");
        timeInTransitRequest.setRequest(request);

        TransitFromType transitFrom = new TransitFromType();
        transitFrom.setAddressArtifactFormat(getTransitAddress(order.getShipFrom()));
        timeInTransitRequest.setTransitFrom(transitFrom);

        TransitToType transitTo = new TransitToType();
        transitTo.setAddressArtifactFormat(getTransitAddress(order.getShipTo()));
        timeInTransitRequest.setTransitTo(transitTo);

        if (order.getScheduledShipDate() != null) {
            timeInTransitRequest.setPickupDate(order.getScheduledShipDate()
                    .withZoneSameInstant(ZoneId.of("America/Toronto"))
                    .format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        }

        com.freightcom.api.carrier.ups.wsdl.timeintransit.request.WeightType weight = new com.freightcom.api.carrier.ups.wsdl.timeintransit.request.WeightType();
        CodeType weightUnit = new CodeType();
        weightUnit.setCode(UNIT_LBS_STRING);
        weight.setUnitOfMeasurement(weightUnit);
        weight.setWeight(order.getWeight() == null ? "1" : order.getWeight()
                .toString());
        timeInTransitRequest.setShipmentWeight(weight);

        // TODO - fix monetary value
        MonetaryType value = new MonetaryType();
        value.setCurrencyCode("CAD");
        value.setMonetaryValue("30");
        timeInTransitRequest.setInvoiceLineTotal(value);

    }

    private TransitToAddressArtifactFormatType getTransitAddress(ShippingAddress modelAddress)
    {
        TransitToAddressArtifactFormatType address = new TransitToAddressArtifactFormatType();

        address.setPostcodePrimaryLow(modelAddress.getPostalCode());
        address.setCountryCode(modelAddress.getCountry());

        return address;
    }
}
