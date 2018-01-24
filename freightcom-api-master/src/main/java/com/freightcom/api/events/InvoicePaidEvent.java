package com.freightcom.api.events;

import com.freightcom.api.model.Invoice;
import com.freightcom.api.model.PaymentTransaction;
import com.freightcom.api.model.UserRole;

public class InvoicePaidEvent extends InvoiceEvent
{
    private final Invoice invoice;
    private final UserRole loggedInRole;
    private final PaymentTransaction payment;

    public InvoicePaidEvent(Invoice invoice, PaymentTransaction payment, UserRole role)
    {
        this.invoice = invoice;
        this.loggedInRole = role;
        this.payment = payment;
    }

    public String toString()
    {
        return "Invoice created " + invoice + " R " + getLoggedInRole();
    }

    public UserRole getLoggedInRole()
    {
        return loggedInRole;
    }

    @Override
    public UserRole getRole() {
        return loggedInRole;
    }

    public Invoice getInvoice()
    {
        return invoice;
    }

    public PaymentTransaction getPayment()
    {
        return payment;
    }

}
