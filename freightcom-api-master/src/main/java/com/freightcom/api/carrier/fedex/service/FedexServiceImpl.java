package com.freightcom.api.carrier.fedex.service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fedex.ws.rate.v22.CommitDetail;
import com.fedex.ws.rate.v22.Money;
import com.fedex.ws.rate.v22.Notification;
import com.fedex.ws.rate.v22.RateReply;
import com.fedex.ws.rate.v22.RateReplyDetail;
import com.fedex.ws.rate.v22.RatedShipmentDetail;
import com.fedex.ws.rate.v22.ShipmentRateDetail;
import com.fedex.ws.rate.v22.Surcharge;
import com.fedex.ws.rate.v22.SurchargeType;
import com.fedex.ws.rate.v22.TransitTimeType;
import com.freightcom.api.carrier.fedex.FedexAPI;
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
public class FedexServiceImpl extends FedexAPI implements FedexService
{
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final static Map<Long, Object> locks = new HashMap<Long, Object>();

    private final FedexRequestBuilder requestBuilder;

    @Autowired
    public FedexServiceImpl(final FedexRequestBuilder requestBuilder) throws Exception
    {
        this.requestBuilder = requestBuilder;
    }

    @Override
    public RateRequestResult getRates(CustomerOrder order, Carrier carrier, ServiceChecker serviceChecker,
            ObjectBase objectBase) throws Exception
    {
        RateRequestResult result = new RateRequestResult();



        synchronized (getLock(order)) {
            RateReply reply = requestBuilder.createAndSendRateRequest(order);

            for (Notification notification: reply.getNotificationsArray()) {
                log.debug("NOTIFICATION " + notification + " " + notification.getMessage());
                result.addMessage("Fedex: " + notification.getMessage());
            }

            for (RateReplyDetail detail : reply.getRateReplyDetailsArray()) {
                RatedShipmentDetail[] shipmentDetails = detail.getRatedShipmentDetailsArray();

                if (detail.getServiceType().toString().matches("(?i)^.*PRIORITY_FREIGHT$")) {
                    if (shipmentDetails.length > 1) {
                        throw new Exception("Unexpected more than one shipment detail");
                    } else if (shipmentDetails.length == 1) {

                        OrderRateQuote quote = new OrderRateQuote();
                        ShipmentRateDetail rateDetail = shipmentDetails[0].getShipmentRateDetail();

                        Money totalNetCharge = rateDetail.getTotalBaseCharge();
                        BigDecimal charge = totalNetCharge.getAmount();

                        quote.setBaseFee(charge);
                        quote.setTotalCharges(rateDetail.getTotalNetFedExCharge().getAmount());
                        quote.setCurrency(totalNetCharge.getCurrency());

                        for (Surcharge surcharge: rateDetail.getSurchargesArray()) {
                            if (SurchargeType.FUEL == surcharge.getSurchargeType()) {
                                quote.setFuelFee(surcharge.getAmount().getAmount());
                            } else if (SurchargeType.RESIDENTIAL_DELIVERY == surcharge.getSurchargeType()) {
                                quote.setResidentialDeliveryFee(surcharge.getAmount().getAmount());
                            }
                        }

                        quote.setService(serviceChecker.get("FXFC",
                                                            detail.getServiceType()
                                                            .toString(),
                                                            carrier, null));

                        for (ApplicableTax applicableTax : order.getCustomer()
                                 .getApplicableTaxes()) {
                            TaxDefinition definition = applicableTax.getTaxDefinition();
                            quote.addTax(definition.getName(), definition.getTaxId(), definition.getRate());
                        }

                        CommitDetail[] commitments = detail.getCommitDetailsArray();

                        log.debug("FEDEX RATE COMMITMENTS " + commitments + " L " + commitments.length);

                        if (detail.getTransitTime() != null) {
                            Integer transitDays = transitTime(detail.getTransitTime());
                            log.debug("TRANSIT TIME " + transitDays);
                            quote.setTransitDays(transitDays);
                        } else if (commitments.length > 0) {
                            if (commitments[0].getCommitTimestamp() != null) {
                                ZonedDateTime shipDate = order.getScheduledShipDate();
                                ZonedDateTime commitDate = ((GregorianCalendar) commitments[0].getCommitTimestamp()).toZonedDateTime();

                                quote.setTransitDays((int) Duration.between(shipDate, commitDate)
                                                     .toDays());
                            } else {
                                quote.setTransitDays(1);
                            }
                            quote.setGuaranteedDelivery(true);
                        }

                        quote.setCarrier(carrier);

                        order.addQuote(quote);
                        result.addRate(quote);
                    }
                }
            }
        }

        return result;
    }

    private Integer transitTime(TransitTimeType.Enum representation)
    {
        int time = 99;

        if (representation == TransitTimeType.TWO_DAYS) {
            time = 2;
        } else if (representation == TransitTimeType.THREE_DAYS) {
            time = 3;
        } else if (representation == TransitTimeType.FOUR_DAYS) {
            time = 4;
        } else if (representation == TransitTimeType.FIVE_DAYS) {
            time = 5;
        } else if (representation == TransitTimeType.SIX_DAYS) {
            time = 6;
        } else if (representation == TransitTimeType.SEVEN_DAYS) {
            time = 7;
        } else if (representation == TransitTimeType.EIGHT_DAYS) {
            time = 8;
        } else if (representation == TransitTimeType.NINE_DAYS) {
            time = 9;
        } else if (representation == TransitTimeType.TEN_DAYS) {
            time = 10;
        } else if (representation == TransitTimeType.ELEVEN_DAYS) {
            time = 11;
        } else if (representation == TransitTimeType.ONE_DAY) {
            time = 1;
        }

        return new Integer(time);
    }

    private static Object getLock(CustomerOrder order)
    {
        Object lock = locks.get(order.getId());

        if (lock == null) {
            lock = new Object();
            locks.put(order.getId(), lock);
        }

        return lock;
    }
}
