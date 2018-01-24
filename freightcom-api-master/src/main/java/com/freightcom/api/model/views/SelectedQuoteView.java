package com.freightcom.api.model.views;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.hateoas.core.Relation;

import com.freightcom.api.model.Carrier;
import com.freightcom.api.model.SelectedQuote;

@Relation(collectionRelation = "selectedQuote")
public class SelectedQuoteView
{
    private final SelectedQuote selectedQuote;

    public SelectedQuoteView(SelectedQuote selectedQuote)
    {
        this.selectedQuote = selectedQuote;
    }

    public Long getId()
    {
        return selectedQuote.getId();
    }

    public Carrier getCarrier()
    {
        if (selectedQuote.getService() != null) {
            return selectedQuote.getService()
                    .getCarrier();
        } else {
            return null;
        }
    }

    public String getServiceName()
    {
        if (selectedQuote.getService() != null) {
            return selectedQuote.getService()
                    .getName();
        } else {
            return null;
        }
    }

    public Long getServiceId()
    {
        if (selectedQuote.getService() != null) {
            return selectedQuote.getService()
                    .getId();
        } else {
            return null;
        }
    }

    public String getLogo()
    {
        // TODO - better logos
        if (selectedQuote.getService() != null) {
            if ("UPS".equalsIgnoreCase(selectedQuote.getService()
                    .getCarrier()
                    .getName())) {
                return "/api/public/carrier/UPS-large.png";
            } else if ("federal express".equalsIgnoreCase(selectedQuote.getService()
                                              .getCarrier()
                                              .getName())) {
                return "/api/public/carrier/Fedex.png";
            } else if (selectedQuote.getService()
                    .getName() != null) {
                return "/api/public/carrier/" + selectedQuote.getService()
                        .getName()
                        .replace(":", "") + ".png";
            } else if (selectedQuote.getService()
                    .getDescription() != null) {
                return "/api/public/carrier/" + selectedQuote.getService()
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
        return selectedQuote.getTransitDays();
    }

    public BigDecimal getTotalCharges()
    {
        return selectedQuote.getTotalCharges();
    }

    public Object getTaxes()
    {
        return selectedQuote.getTaxes();
    }

    public String getCurrency()
    {
        return selectedQuote.getCurrency();
    }

    public Map<String, Object> getCharges()
    {
        Map<String, Object> values = new HashMap<String, Object>();

        values.put("base_fee", selectedQuote.getBaseFee());
        values.put("fuel_fee", selectedQuote.getFuelFee());
        values.put("insurance", selectedQuote.getInsurance());
        values.put("cross_border_fee", selectedQuote.getCrossBorderFee());

        return values;
    }

    public String getDescription()
    {
        //return selectedQuote.getService() == null ? null : selectedQuote.getService().getDescription();
        return "Vivamus sagittis lacus vel augue laoreet rutrum faucibus dolor auctor. Duis mollis, est non commodo luctus.";
    }
}
