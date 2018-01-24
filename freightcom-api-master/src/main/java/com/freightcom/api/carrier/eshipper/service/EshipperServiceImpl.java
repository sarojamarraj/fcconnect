package com.freightcom.api.carrier.eshipper.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.freightcom.api.carrier.eshipper.EShipperRateRequestBuilder;
import com.freightcom.api.carrier.eshipper.schema.EshipperQuoteReply;
import com.freightcom.api.carrier.eshipper.schema.Quote;
import com.freightcom.api.carrier.ups.service.ServiceChecker;
import com.freightcom.api.model.ApplicableTax;
import com.freightcom.api.model.Carrier;
import com.freightcom.api.model.CustomerOrder;
import com.freightcom.api.model.OrderRateQuote;
import com.freightcom.api.model.TaxDefinition;
import com.freightcom.api.repositories.ObjectBase;
import com.freightcom.api.services.orders.RateRequestResult;

/**
 * @author bryan
 *
 */

@Service
public class EshipperServiceImpl implements EshipperService
{
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final static Map<Long, Object> locks = new HashMap<Long, Object>();

    private final EShipperRateRequestBuilder requestBuilder;

    @Autowired
    public EshipperServiceImpl(final EShipperRateRequestBuilder requestBuilder) throws Exception
    {
        this.requestBuilder = requestBuilder;
    }

    @Override
    public RateRequestResult getRates(CustomerOrder order, Carrier carrier, ServiceChecker serviceChecker,
            ObjectBase objectBase) throws Exception
    {
        RateRequestResult result = new RateRequestResult();
        EshipperQuoteReply rates = requestBuilder.createAndSendRateRequest(order);

        if (rates.getErrorReply() != null) {
            for (com.freightcom.api.carrier.eshipper.schema.Error error : rates.getErrorReply()
                    .getErrors()) {
                log.debug("ESHIPPER ERROR " + error.getMessage());
                result.addMessage("Eshipper: " + error.getMessage());
            }
        } else {
            List<OrderRateQuote> quotes = new ArrayList<OrderRateQuote>();

            result.addMessage("Eshipper: success");

            for (Quote quoteReply : rates.getQuoteReply()
                    .getQuotes()) {
                com.freightcom.api.model.Service service = carrier.findService(quoteReply.getServiceId(),
                        quoteReply.getServiceName(), quoteReply.getServiceName());

                log.debug("ESHIPPER SERVICE " + quoteReply.getServiceId() + " " + service);

                if (service != null) {
                    OrderRateQuote quote = new OrderRateQuote();

                    quote.setOrder(order);
                    quote.setCarrier(carrier);
                    quote.setService(service);
                    quote.setCurrency(quoteReply.getCurrency());
                    quote.setBaseFee(quoteReply.getBaseCharge());
                    quote.setTotalCharges(quoteReply.getTotalCharge());
                    quote.setFuelFee(quoteReply.getFuelSurcharge());
                    quote.setTransitDays(quoteReply.getTransitDays());

                    for (ApplicableTax applicableTax : order.getCustomer()
                            .getApplicableTaxes()) {
                        TaxDefinition definition = applicableTax.getTaxDefinition();
                        quote.addTax(definition.getName(), definition.getTaxId(), definition.getRate());
                    }

                    objectBase.save(quote);
                    quotes.add(quote);
                }
            }

            result.addRates(quotes);
        }

        return result;
    }

    @Override
    public void bookOrder(CustomerOrder order) throws Exception
    {

    }
}
