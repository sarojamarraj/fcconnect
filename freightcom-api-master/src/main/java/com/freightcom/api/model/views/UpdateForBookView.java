package com.freightcom.api.model.views;


import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.freightcom.api.model.Customer.AutoInvoice;
import com.freightcom.api.model.CustomerOrder;
import com.freightcom.api.services.ServiceProvider;


public class UpdateForBookView implements View
{
    private final CustomerOrder order;
    @SuppressWarnings("unused")
    private final ServiceProvider provider;

    public static UpdateForBookView get(final CustomerOrder order, final ServiceProvider provider)
    {
        return new UpdateForBookView(order, provider);
    }

    protected UpdateForBookView(final CustomerOrder order, final ServiceProvider provider)
    {
        this.order = order;
        this.provider = provider;

        if (order == null) {
            throw new Error("Null order to view");
        }
    }

    public Long getId()
    {
        return order.getId();
    }

    public boolean getPayNow()
    {
        return order.getCustomer().getAutoInvoice() == AutoInvoice.ON_BOOKING;
    }

    public String getViewName()
    {
        return "UpdateForBookView";
    }

    public BigDecimal getCreditAvailable()
    {
        return order.getCustomer().getCreditAvailable(order.getSelectedQuote().getCurrency());
    }

    public BigDecimal getOrderTotal()
    {
        return order.getSelectedQuote().getTotalCharges();
    }

    public BigDecimal getAmountPayable()
    {
        if (getOrderTotal().compareTo(getCreditAvailable()) > 0) {
            return getOrderTotal().subtract(getCreditAvailable());
        } else {
            return BigDecimal.ZERO;
        }
    }
    
    public String getCurrency() 
    {
        return order.getSelectedQuote().getCurrency();        
    }
}
