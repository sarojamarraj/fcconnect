package com.freightcom.api.events;

import com.freightcom.api.model.Invoice;
import com.freightcom.api.model.UserRole;

public class InvoiceDeletedEvent extends InvoiceEvent
{
    private final Invoice invoice;
    private final UserRole loggedInRole;

    public InvoiceDeletedEvent(Invoice invoice, UserRole role)
    {
        this.invoice = invoice;
        this.loggedInRole = role;
    }

    public String toString()
    {
        return "Invoice created " + invoice + " R " + getLoggedInRole();
    }

    public UserRole getLoggedInRole()
    {
        return loggedInRole;
    }

    public Invoice getInvoice()
    {
        return invoice;
    }

}
