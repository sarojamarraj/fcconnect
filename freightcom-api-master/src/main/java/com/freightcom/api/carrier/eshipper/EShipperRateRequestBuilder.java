package com.freightcom.api.carrier.eshipper;

import static com.freightcom.api.util.Empty.asString;
import static com.freightcom.api.util.Empty.ifNull;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

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

import com.freightcom.api.ReportableError;
import com.freightcom.api.carrier.eshipper.schema.Address;
import com.freightcom.api.carrier.eshipper.schema.EshipperQuoteReply;
import com.freightcom.api.carrier.eshipper.schema.EshipperQuoteRequest;
import com.freightcom.api.carrier.eshipper.schema.IndividualPackage;
import com.freightcom.api.carrier.eshipper.schema.QuoteRequest;
import com.freightcom.api.carrier.eshipper.schema.ShipPackage;
import com.freightcom.api.model.CustomerOrder;
import com.freightcom.api.model.Pallet;
import com.freightcom.api.model.ShippingAddress;

@Component
@ConfigurationProperties
public class EShipperRateRequestBuilder
{
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final RestTemplate connection;

    @Value("${eshipper.ratings.url}")
    private String ratingsURL;

    @Value("${eshipper.username}")
    private String username;

    @Value("${eshipper.password}")
    private String password;

    @Autowired
    public EShipperRateRequestBuilder(final RestTemplate connection)
    {
        this.connection = connection;
    }

    public EshipperQuoteReply createAndSendRateRequest(CustomerOrder order) throws Exception
    {
        EshipperQuoteRequest request = new EshipperQuoteRequest(username, password);
        QuoteRequest quoteRequest = request.addQuoteRequest();

        fillAddress(order.getShipFrom(), quoteRequest.addFrom(), false);
        fillAddress(order.getShipTo(), quoteRequest.addTo(), true);

        addPackages(order, quoteRequest);

        log.debug("CONTACT ESHIPPER '" + ratingsURL + "' '" + username + "' '" + password + "'");

        return contactService(marshall(request), ratingsURL);
    }

    private ShipPackage addPackages(CustomerOrder order, QuoteRequest quoteRequest) throws Exception
    {
        // Envelope,Courier Pak, Package and Pallet]

        switch (order.getPackageTypeCode()) {
        case PACKAGE_ENV:
            return addEnvelope(order, quoteRequest);

        case PACKAGE_PAK:
            return addPak(order, quoteRequest);

        case PACKAGE_PACKAGE:
            return addPackage(order, quoteRequest);

        case PACKAGE_PALLET:
            return addPallet(order, quoteRequest);
        }

        throw new ReportableError("No package type");
    }

    private ShipPackage addEnvelope(CustomerOrder order, QuoteRequest quoteRequest)
    {
        ShipPackage shipPackage = quoteRequest.addPackages("Envelope");
        shipPackage.addPackage(1, 1, 1, 1.0);

        return shipPackage;
    }

    private ShipPackage addPak(CustomerOrder order, QuoteRequest quoteRequest)
    {
        ShipPackage shipPackage = quoteRequest.addPackages("Courier Pak");
        shipPackage.addPackage(1, 1, 1, ifNull(order.getWeight(), 0.0).doubleValue());

        return shipPackage;
    }

    private ShipPackage addPackage(CustomerOrder order, QuoteRequest quoteRequest)
    {
        ShipPackage shipPackage = quoteRequest.addPackages("Package");

        for (com.freightcom.api.model.Package item : order.getPackages()) {
            IndividualPackage individualPackage = shipPackage.addPackage(item.getLength(), item.getWidth(),
                    item.getHeight(), new Double(ifNull(item.getWeight(), "1")));

            individualPackage.setInsuranceAmount(item.getInsuranceAmount());
            individualPackage.setDescription(item.getDescription());
        }

        return shipPackage;
    }

    private ShipPackage addPallet(CustomerOrder order, QuoteRequest quoteRequest)
    {
        ShipPackage shipPackage = quoteRequest.addPackages("Pallet");

        for (Pallet item : order.getPallets()) {
            IndividualPackage individualPallet = shipPackage.addPackage(new Integer(ifNull(item.getLength(), "0")),
                    new Integer(ifNull(item.getWidth(), "0")), new Integer(ifNull(item.getHeight(), "0")),
                    new Double(ifNull(item.getWeight(), "0.0")));
            
            log.debug("ESHIPPER PALLET " + item + " " + item.getPalletType());

            individualPallet.setInsuranceAmount(item.getInsurance());
            individualPallet.setDescription(item.getDescription());
            individualPallet.setType(asString(item.getPalletType(), (String) null));
            individualPallet.setFreightClass(item.getFreightClass());
            individualPallet.setNmfcCode(item.getNmfcCode());
        }

        return shipPackage;
    }

    private void fillAddress(ShippingAddress modelAddress, Address address, boolean isTo)
    {
        address.setId(modelAddress.getId()
                .toString());
        address.setCompany(modelAddress.getCompany());
        address.setAddress1(modelAddress.getAddress1());
        address.setAddress2(modelAddress.getAddress2());
        address.setCity(modelAddress.getCity());
        address.setState(modelAddress.getProvince());
        address.setZip(modelAddress.getPostalCode());
        address.setCountry(modelAddress.getCountry());
    }

    /**
     * @param requestStr
     * @param url
     * @return
     * @throws Exception
     */
    private EshipperQuoteReply contactService(String requestStr, String url) throws Exception
    {
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.TEXT_PLAIN);
        headers.set("Referer", "Freightcom Inc.");
        HttpEntity<String> entity = new HttpEntity<String>(requestStr, headers);

        log.debug("CONNECT ESHIPPER URL '" + url + "'");
        log.debug("RATINGS ENITTY '" + entity + "'");

        ResponseEntity<String> response = connection.postForEntity(url, entity, String.class);

        log.debug("RESPONSE '" + response + "'");

        if (response.getStatusCode() != HttpStatus.OK) {
            log.error("Method failed: " + response.getStatusCode());
        }

        return unmarshall(response.getBody(), EshipperQuoteReply.class);
    }

    private <T> T unmarshall(String text, Class<T> clazz) throws Exception
    {
        JAXBContext jaxbContext = JAXBContext.newInstance(clazz);

        XMLInputFactory xif = XMLInputFactory.newFactory();
        xif.setProperty(XMLInputFactory.SUPPORT_DTD, false);
        XMLStreamReader xsr = xif.createXMLStreamReader(new StringReader(text));
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        JAXBElement<T> x = unmarshaller.unmarshal(xsr, clazz);

        return x.getValue();
    }

    private String marshall(Object object) throws Exception
    {
        JAXBContext jaxbContext = JAXBContext.newInstance(EshipperQuoteRequest.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
        StringWriter writer = new StringWriter();

        jaxbMarshaller.marshal(object, writer);

        return writer.toString();
    }
}
