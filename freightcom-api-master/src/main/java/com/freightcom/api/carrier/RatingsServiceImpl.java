package com.freightcom.api.carrier;

import java.util.ArrayList;
import java.util.List;

import org.apache.axis2.AxisFault;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.freightcom.api.ReportableError;
import com.freightcom.api.carrier.eshipper.service.EshipperService;
import com.freightcom.api.carrier.fedex.service.FedexService;
import com.freightcom.api.carrier.ups.service.ServiceChecker;
import com.freightcom.api.carrier.ups.service.UPSService;
import com.freightcom.api.model.CustomerOrder;
import com.freightcom.api.model.OrderRateQuote;
import com.freightcom.api.repositories.ObjectBase;
import com.freightcom.api.services.ValidationException;
import com.freightcom.api.services.orders.RateRequestResult;
import com.freightcom.api.util.Utilities;

@Component
public class RatingsServiceImpl implements RatingsService
{
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private FedexService fedex;

    @Autowired
    private UPSService ups;

    @Autowired
    private EshipperService eshipper;

    public RatingsServiceImpl()
    {

    }

    @Override
    public RateRequestResult getRates(CustomerOrder order, ServiceChecker serviceChecker, ObjectBase objectBase)
            throws Exception
    {
        validateShipment(order);

        List<OrderRateQuote> quotes = new ArrayList<OrderRateQuote>();
        RateRequestResult result = new RateRequestResult(quotes);

        if (order.isLTL()) {
            try {
                result.addQuotes(fedex.getRates(order, objectBase.getCarrierByNameNoCache("Fedex Freight"),
                        serviceChecker, objectBase));
            } catch (AxisFault e) {
                log.error(String.format("\n\nEXCEPTION IN FEDEX FAULT\n%s\n%s\n%s\n%s\n%s\n\n", e.getMessage(),
                        e.getDetails()
                                .toString(),
                        e.getFaultAction(), e.getReason(), e.getFaultDetailElement()
                                .toString()),
                        Utilities.getStackTrace(e));

                result.addMessage("Fedex: " + e.getMessage());
            } catch (Exception e) {
                log.error("EXCEPTION IN FEDEX " + e.getMessage());
                log.error("EXCEPTION IN FEDEX " + e);
                log.error("EXCEPTION IN FEDEX " + Utilities.getStackTrace(e));

                result.addMessage("Fedex: " + e.getMessage());
            } catch (ReportableError e) {
                log.error("EXCEPTION IN FEDEX B " + e.getMessage());
                log.error("EXCEPTION IN FEDEX B " + e);
                log.error("EXCEPTION IN FEDEX B " + Utilities.getStackTrace(e));

                result.addMessage("Fedex: " + e.getMessage());
            }
        } else {
            try {
                result.addQuotes(ups.getRates(order, objectBase.getCarrierByNameNoCache("UPS Courier"), serviceChecker,
                        objectBase));
                log.debug("FIRST QUOTE QUOTES " + quotes + " " + quotes.size());
            } catch (Exception e) {
                log.error("EXCEPTION IN UPS " + e.getMessage());
                log.error("EXCEPTION IN UPS " + e);
                log.error("EXCEPTION IN UPS " + Utilities.getStackTrace(e));

                result.addMessage("UPS: " + e.getMessage());
            } catch (ReportableError e) {
                log.error("EXCEPTION IN UPS B " + e.getMessage());
                log.error("EXCEPTION IN UPS B " + e);
                log.error("EXCEPTION IN UPS B " + Utilities.getStackTrace(e));

                result.addMessage("UPS: " + e.getMessage());
            }
        }

        if (!order.isLTL()) {
            try {
                // Carrier with Eshipper implementing class
                log.debug("ESHIPPER CARRIER " + objectBase.getCarrier(new Long(22)));
                result.addQuotes(
                        eshipper.getRates(order, objectBase.getCarrier(new Long(22)), serviceChecker, objectBase));
            } catch (Exception e) {
                log.error("EXCEPTION IN ESHIPPER " + e.getMessage());
                log.error("EXCEPTION IN ESHIPPER " + e);
                log.error("EXCEPTION IN ESHIPPER " + Utilities.getStackTrace(e));
            }
        }

        log.debug("NOW QUOTE QUOTES " + quotes + " " + quotes.size());

        return result;
    }

    private void validateShipment(CustomerOrder order) throws Exception
    {
        ValidationException problems = null;

        if (order == null) {
            problems = ValidationException.get()
                    .add("order", "Null order");
        }

        if (order.getShipFrom() == null) {
            if (problems == null) {
                problems = ValidationException.get();
            }

            problems.add("shipFrom", "Missing ship from");
        }

        if (order.getShipTo() == null) {
            if (problems == null) {
                problems = ValidationException.get();
            }

            problems.add("shipFrom", "Missing ship to");
        }

        if (problems != null) {
            problems.doThrow();
        }
    }
}
