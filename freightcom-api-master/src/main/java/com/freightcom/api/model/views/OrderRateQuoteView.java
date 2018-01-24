package com.freightcom.api.model.views;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.hateoas.core.Relation;
import com.freightcom.api.model.OrderRateQuote;
import com.freightcom.api.util.Empty;
import static com.freightcom.api.util.Empty.ifNull;

@Relation(collectionRelation = "orderRateQuote")
public class OrderRateQuoteView
{
    private final OrderRateQuote orderRateQuote;

    public OrderRateQuoteView(OrderRateQuote orderRateQuote)
    {
        this.orderRateQuote = orderRateQuote;
    }

    public Long getId()
    {
        return orderRateQuote.getId();
    }

    public String getCarrierName()
    {
        if (orderRateQuote.getCarrier() != null) {
            return orderRateQuote.getCarrier()
                    .getName();
        } else {
            return null;
        }
    }

    public String getServiceName()
    {
        if (orderRateQuote.getService() != null) {
            if (Empty.check(orderRateQuote.getService()
                    .getName())) {
                return orderRateQuote.getService()
                        .getDescription();
            } else {
                return orderRateQuote.getService()
                        .getName();
            }
        } else {
            return null;
        }
    }

    public String getDescription()
    {
        // return orderRateQuote.getService() == null ? null :
        // orderRateQuote.getService().getDescription();
        return "Vivamus sagittis lacus vel augue laoreet rutrum faucibus dolor auctor. Duis mollis, est non commodo luctus.";
    }

    public Boolean getGuaranteedDelivery()
    {
        return orderRateQuote.getGuaranteedDelivery();
    }

    public String getRating()
    {
        // ToDo
        return "4.0";
    }

    public Long getServiceId()
    {
        if (orderRateQuote.getService() != null) {
            return orderRateQuote.getService()
                    .getId();
        } else {
            return null;
        }
    }

    public String getLogo()
    {
        // TODO - better logos
        if (orderRateQuote.getService() != null) {
            if ("UPS".equalsIgnoreCase(orderRateQuote.getService()
                    .getCarrier()
                    .getName())) {
                return "/api/public/carrier/UPS-large.png";
            } else if ("federal express".equalsIgnoreCase(orderRateQuote.getService()
                    .getCarrier()
                    .getName())
                    || ifNull(orderRateQuote.getService()
                            .getName(), "").matches("(?i).*fedex.*")) {
                return "/api/public/carrier/Fedex.png";
            } else if (orderRateQuote.getService()
                    .getName() != null) {
                return "/api/public/carrier/" + orderRateQuote.getService()
                        .getName()
                        .replace(":", "") + ".png";
            } else if (orderRateQuote.getService()
                    .getDescription() != null) {
                return "/api/public/carrier/" + orderRateQuote.getService()
                        .getDescription()
                        .replace(":", "") + ".png";
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public Integer getTransitDays()
    {
        return orderRateQuote.getTransitDays();
    }

    public BigDecimal getTotalCharges()
    {
        return orderRateQuote.getTotalCharges();
    }

    public Map<String, Object> getCharges()
    {
        Map<String, Object> values = new HashMap<String, Object>();

        values.put("base_fee", orderRateQuote.getBaseFee());
        values.put("fuel_fee", orderRateQuote.getFuelFee());
        values.put("insurance", orderRateQuote.getInsurance());
        values.put("cross_border_fee", orderRateQuote.getCrossBorderFee());

        if (!BigDecimal.ZERO.equals(orderRateQuote.getResidentialDeliveryFee())) {
            values.put("residential_delivery_fee", orderRateQuote.getResidentialDeliveryFee());
        }

        values.put("total_tax", orderRateQuote.getTotalTax());

        return values;
    }

    public String getCurrency()
    {
        return orderRateQuote.getCurrency();
    }

    public String getScheduledDeliveryTime()
    {
        return orderRateQuote.getScheduledDeliveryTime();
    }

    public Object getTaxes()
    {
        return orderRateQuote.getTaxes();
    }
}
