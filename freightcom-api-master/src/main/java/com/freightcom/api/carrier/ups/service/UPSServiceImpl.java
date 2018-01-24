package com.freightcom.api.carrier.ups.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;

import com.freightcom.api.ReportableError;
import com.freightcom.api.carrier.RateInfo;
import com.freightcom.api.carrier.ups.UPSAPI;
import com.freightcom.api.carrier.ups.wsdl.rate.response.RatedShipmentType;
import com.freightcom.api.carrier.ups.wsdl.rate.response.RatingServiceSelectionResponse;
import com.freightcom.api.carrier.ups.wsdl.timeintransit.response.ServiceSummaryType;
import com.freightcom.api.carrier.ups.wsdl.timeintransit.response.TimeInTransitResponse;
import com.freightcom.api.carrier.ups.wsdl.timeintransit.response.TransitResponseType;
import com.freightcom.api.model.ApplicableTax;
import com.freightcom.api.model.Carrier;
import com.freightcom.api.model.CustomerOrder;
import com.freightcom.api.model.OrderRateQuote;
import com.freightcom.api.model.TaxDefinition;
import com.freightcom.api.model.UpsRateCodes;
import com.freightcom.api.model.UpsTransitCodes;
import com.freightcom.api.repositories.ObjectBase;
import com.freightcom.api.services.orders.RateRequestResult;
import com.freightcom.api.util.Empty;
import com.freightcom.api.util.ShippingConstants;
import com.freightcom.api.util.Utilities;
import com.ups.www.xmlschema.xoltws.ship.v1_0.PackageResultsType;
import com.ups.www.xmlschema.xoltws.ship.v1_0.ShipAcceptResponseDocument.ShipAcceptResponse;
import com.ups.www.xmlschema.xoltws.ship.v1_0.ShipChargeType;

/**
 * @author bryan
 *
 */

@Service
public class UPSServiceImpl extends UPSAPI implements UPSService
{
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final static Map<Long, Object> locks = new HashMap<Long, Object>();

    private final UPSRequestBuilder requestBuilder;
    private final UPSShipRequest shipBuilder;
    private final UPSShipConfirmRequest shipConfirmRequest;

    @Autowired
    public UPSServiceImpl(final UPSRequestBuilder requestBuilder, final UPSShipRequest shipBuilder,
            final UPSShipConfirmRequest shipConfirmRequest) throws Exception
    {
        this.requestBuilder = requestBuilder;
        this.shipBuilder = shipBuilder;
        this.shipConfirmRequest = shipConfirmRequest;
    }

    @Override
    public Object rateShipment(CustomerOrder order) throws Exception
    {
        return rateShipmentByService(order, null, false);
    }

    @Override
    public Object rateShipment(CustomerOrder order, boolean getTimes) throws Exception
    {
        return rateShipmentByService(order, null, getTimes);
    }

    @Override
    public Object rateShipment(CustomerOrder order, long serviceId) throws Exception
    {

        return rateShipmentByService(order, serviceId, false);
    }

    private Object rateShipmentByService(CustomerOrder order, Long serviceId, boolean getTimes) throws Exception
    {
        return requestBuilder.createAndSendRateRequest(order, getTimes);
    }

    @Override
    public Object timeInTransit(CustomerOrder order) throws Exception
    {

        return requestBuilder.createAndSendTimeInTransitRequest(order);
    }

    @Override
    public RateRequestResult getRates(CustomerOrder order, Carrier carrier, ServiceChecker serviceChecker,
            ObjectBase objectBase) throws Exception
    {
        RateRequestResult result = new RateRequestResult();

        synchronized (getLock(order)) {
            RatingServiceSelectionResponse rates = requestBuilder.createAndSendRateRequest(order, false);
            TimeInTransitResponse times = requestBuilder.createAndSendTimeInTransitRequest(order);

            if (rates.getResponse()
                    .getResponseStatusCode()
                    .equals("0")) {
                throw new ReportableError(rates.getResponse()
                        .getError()
                        .get(0)
                        .getErrorCode() + ": "
                        + rates.getResponse()
                                .getError()
                                .get(0)
                                .getErrorDescription());
            }

            List<OrderRateQuote> quotes = new ArrayList<OrderRateQuote>();
            UPSResolveService resolver = new UPSResolveService(objectBase);

            result.addMessage("UPS: " + rates.getResponse()
                    .getResponseStatusDescription());

            for (RatedShipmentType shipment : rates.getRatedShipment()) {
                OrderRateQuote quote = new OrderRateQuote();

                BigDecimal charge = new BigDecimal(shipment.getTotalCharges()
                        .getMonetaryValue()).multiply(new BigDecimal(1.25))
                                .setScale(2, RoundingMode.HALF_UP);

                quote.setBaseFee(charge);
                quote.setTotalCharges(charge);

                for (ApplicableTax applicableTax : order.getCustomer()
                        .getApplicableTaxes()) {
                    TaxDefinition definition = applicableTax.getTaxDefinition();
                    quote.addTax(definition.getName(), definition.getTaxId(), definition.getRate());
                }

                quote.setCarrier(carrier);

                UpsRateCodes rateCode = resolver.resolveRatingService(order.getFromCountry(), order.getToCountry(),
                        shipment.getService()
                                .getCode());

                if (rateCode != null) {
                    quote.setService(serviceChecker.get(rateCode.getRatingCode(), rateCode.getServiceName(), carrier,
                            rateCode.getCategory()));
                } else {
                    quote.setService(serviceChecker.get(shipment.getService()
                            .getCode(),
                            shipment.getService()
                                    .getDescription(),
                            carrier, null));
                }

                if (Empty.check(quote.getService()
                        .getDescription())) {
                    quote.getService()
                            .setDescription(serviceDescription(shipment.getService()
                                    .getCode()));
                }

                if (!Empty.check(shipment.getGuaranteedDaysToDelivery())) {
                    quote.setTransitDays(Integer.parseInt(shipment.getGuaranteedDaysToDelivery(), 10));
                    quote.setGuaranteedDelivery(true);
                }

                if (!Empty.check(shipment.getScheduledDeliveryTime())) {
                    quote.setScheduledDeliveryTime(shipment.getScheduledDeliveryTime());
                    quote.setGuaranteedDelivery(true);
                }

                if (!Empty.check(shipment.getTotalCharges()
                        .getCurrencyCode())) {
                    quote.setCurrency(shipment.getTotalCharges()
                            .getCurrencyCode());
                    quote.setGuaranteedDelivery(true);
                }

                order.addQuote(quote);

                quotes.add(quote);
            }

            if (times == null) {
                throw new ReportableError("Unable to get time in transit");
            }

            for (TransitResponseType transitResponse : times.getTransitResponse()) {
                for (ServiceSummaryType serviceSummary : transitResponse.getServiceSummary()) {
                    String code = serviceSummary.getService()
                            .getCode();
                    String description = serviceSummary.getService()
                            .getDescription();
                    String days = serviceSummary.getEstimatedArrival()
                            .getTotalTransitDays();
                    OrderRateQuote matched = null;

                    UpsTransitCodes transitCode = resolver.resolveTransitService(order.getFromCountry(),
                            order.getToCountry(), code);

                    for (OrderRateQuote quote : quotes) {
                        if (transitCode != null) {
                            if (quote.getService() != null && quote.getService()
                                    .getName()
                                    .equals(transitCode.getServiceName())) {
                                matched = quote;
                                break;
                            }
                        } else if (quote.getService() != null && quote.getService()
                                .getCode()
                                .equals(code)) {
                            matched = quote;
                            break;
                        }
                    }

                    if (matched != null) {
                        if (matched.getService()
                                .getDescription() == null) {
                            matched.getService()
                                    .setDescription(description);
                        }

                        if (days != null) {
                            matched.setTransitDays(Integer.parseInt(days, 10));
                        }
                    }
                }
            }

            result.addRates(quotes);

            return result;
        }
    }

    private boolean isValidOrder(CustomerOrder order)
    {
        if (order.getPackageTypeId() == ShippingConstants.PACKAGE_TYPE_PALLET) {
            log.debug("Pallet package type is not supported");
            return false;
        }

        if (order.isHoldForPickupRequired()) {
            log.info("UPS not serving the hold For Pickup Required.");
            return false;
        }

        if (order.isSaturdayPickup()) {
            log.info("UPS not serving Saturday Pick up.");
            return false;
        }

        if (order.getDangerousGoods() != null && !order.getDangerousGoods()
                .equalsIgnoreCase("None")
                && !order.getDangerousGoods()
                        .equalsIgnoreCase("")) {
            log.debug("Dangerous Goods is not supported");
            return false;
        }

        if (!(order.getShipFrom()
                .getCountry()
                .equals(ShippingConstants.CANADA)
                || order.getShipFrom()
                        .getCountry()
                        .equals(ShippingConstants.US))) {
            log.info("ship from country needs to be Canada or US");
            return false;
        }

        if (order.getShipFrom()
                .getCountry()
                .equals(ShippingConstants.CANADA) && order.getCodValue() > 0) {
            log.info("COD is not supported if origin country Canada");
            return false;
        }

        // TODO COD
        // TODOsmall package service
        /*
         * if(order.getCodValue() > 0 && order.getSignatureRequired() != 1 ) {
         * return false; }
         *
         * String upsService =
         * pinBlockManager.getPropertyValue(UPSConstants.UPS_COURIER_SERVICE);
         * String upsCostPuro = pinBlockManager.getPropertyValue(UPSConstants.
         * FCCOST_UPS_COURIER_SERVICE);
         *
         * if (upsCostPuro.equals("1") && order.getCustomer() .getId() ==
         * ShippingConstants.FC_COST_CUSTOMER_ID) {
         * log.info("UPS courier SMALL PKG SERVICE FOR FC_COST ONLY."); return
         * true; }
         *
         * if (!upsService.equals("1")) {
         * log.info("UPS courier SMALL PKG SERVICE is not enabled"); return
         * false; }
         */

        return true;
    }

    private void breakdownRates(List<RateInfo> rates, CustomerOrder order)
    {
    }

    // for now, all rates for UPS shipments will be CAD/USA currency as per From
    // or To country
    private void setCurrency(List<RateInfo> rates, CustomerOrder order)
    {
        if (rates == null)
            return;
        for (RateInfo rate : rates) {
            setCurrency(rate, order);
        }
    }

    // for now, all rates for UPS shipments will be CAD/USA currency as per From
    // or To country
    private void setCurrency(RateInfo rate, CustomerOrder order)
    {
        if (rate != null) {
            if (order.getShipFrom()
                    .getCountry()
                    .equalsIgnoreCase(ShippingConstants.CANADA))
                rate.setCurrencyType(ShippingConstants.CURRENCY_CA);
            else if (order.getShipFrom()
                    .getCountry()
                    .equalsIgnoreCase(ShippingConstants.US))
                rate.setCurrencyType(ShippingConstants.CURRENCY_US);
            else if (order.getShipTo()
                    .getCountry()
                    .equalsIgnoreCase(ShippingConstants.US))
                rate.setCurrencyType(ShippingConstants.CURRENCY_US);
            else if (order.getShipTo()
                    .getCountry()
                    .equalsIgnoreCase(ShippingConstants.CANADA))
                rate.setCurrencyType(ShippingConstants.CURRENCY_CA);
        }
    }

    @SuppressWarnings("unused")
    private double getDiscountedBaseCharge(double baseCharge, long serviceId)
    {
        double discountedBaseCharge = baseCharge;
        double disc_perc = 0.0;

        // Next Day Air Services
        if (serviceId == 600 || serviceId == 605) {
            disc_perc = 64;
        } else if (serviceId == 609 || serviceId == 607) {
            disc_perc = 65;
        } else if (serviceId == 601 || serviceId == 611) {
            disc_perc = 60;
        } else if (serviceId == 606) {
            disc_perc = 50;
        } else if (serviceId == 602 || serviceId == 603 || serviceId == 610) {
            disc_perc = 15;
        } else if (serviceId == 608) {
            disc_perc = 50;
        }

        if (disc_perc == 0)
            return discountedBaseCharge;

        discountedBaseCharge *= (100 - disc_perc) / 100;

        return discountedBaseCharge;

    }

    @SuppressWarnings("unused")
    private int getTransitDays(Map<String, String> transitMap, CustomerOrder order, Service service)
    {
        // TODO transit days calculation
        /*
         * if (transitMap != null && transitMap.size() > 0) { String transitDays
         * = null; if (order.getShipFrom() .getCountry()
         * .equals(ShippingConstants.US) && order.getShipTo() .getCountry()
         * .equals(ShippingConstants.US)) { transitDays =
         * transitMap.get(service.getTransitCodeUS()); } else if
         * (order.getShipFrom() .getCountry() .equals(ShippingConstants.CANADA)
         * && order.getShipTo() .getCountry() .equals(ShippingConstants.CANADA))
         * { transitDays = transitMap.get(service.getTransitCodeCA()); } else {
         * transitDays = transitMap.get(service.getTransitCodeIntl()); } if
         * (transitDays != null) { return Integer.parseInt(transitDays); } }
         */
        return 0;
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

    public void bookOrder(CustomerOrder order) throws Exception
    {

        synchronized (getLock(order)) {
            try {
                ShipAcceptResponse response = shipConfirmRequest.ship(order);

                if (response.getResponse()
                        .getResponseStatus()
                        .getCode()
                        .equals("0")) {
                    throw new ReportableError("UPS Accept Error");
                }

                log.debug("UPS RESPONsE " + response.getShipmentResults()
                        .getShipmentCharges()
                        .getTotalCharges());

                ShipChargeType[] y = response.getShipmentResults()
                        .getShipmentCharges()
                        .getItemizedChargesArray();

                for (ShipChargeType x : y) {
                    log.debug("SCT " + x);
                }

                ShipChargeType charge = response.getShipmentResults()
                        .getShipmentCharges()
                        .getTotalCharges();

                log.debug("UPS ADDING CHARGE " + new BigDecimal(charge.getMonetaryValue()) + " "
                        + charge.getCurrencyCode());
                order.newCharge("Base Fee", new BigDecimal(charge.getMonetaryValue()), charge.getCurrencyCode());
                
                for (PackageResultsType packageResult : response.getShipmentResults().getPackageResultsArray()) {
                    log.debug("PACKAGE" + packageResult.getTrackingNumber());
                    log.debug("PACKAGE" + packageResult.getShippingLabel().getImageFormat().getCode());
                    order.addShippingLabel(Base64Utils.decode(packageResult.getShippingLabel().getGraphicImage().getBytes()));
                    order.setMasterTrackingNum(packageResult.getTrackingNumber());
                }

            } catch (Exception e) {
                log.error("Unable to book " + order + " " + e.getMessage() + "\n" + Utilities.getStackTrace(e));
                throw e;
            } catch (Throwable e) {
                log.error("Unable to book " + order + " " + e.getMessage() + "\n" + Utilities.getStackTrace(e));
                throw e;
            }
        }
    }
}
