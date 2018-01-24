package com.freightcom.api.services.invoices;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.freightcom.api.events.InvoiceCreatedEvent;
import com.freightcom.api.model.Customer;
import com.freightcom.api.model.CustomerOrder;
import com.freightcom.api.model.Invoice;
import com.freightcom.api.services.ServiceProvider;

/**
 * @author bryan
 *
 */

public class InvoiceWrapper
{
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final Invoice invoice;
    private final ServiceProvider serviceProvider;
    private boolean deleted = false;

    public static InvoiceWrapper get(ServiceProvider serviceProvider)
    {
        return new InvoiceWrapper(serviceProvider);
    }

    public static InvoiceWrapper get(ServiceProvider serviceProvider, Invoice invoice)
    {
        return new InvoiceWrapper(serviceProvider, invoice);
    }

    private InvoiceWrapper(final ServiceProvider serviceProvider)
    {
        invoice = new Invoice();
        this.serviceProvider = serviceProvider;
    }

    private InvoiceWrapper(final ServiceProvider serviceProvider, final Invoice invoice)
    {
        this.invoice = invoice;
        this.serviceProvider = serviceProvider;
    }

    public void delete() throws Exception
    {
        synchronized (invoice) {

            if (deleted) {
                throw new Exception("Invoice already deleted");
            }

            serviceProvider.getObjectBase()
                    .delete(invoice);

            deleted = true;
        }
    }

    public Invoice getInvoice()
    {
        return invoice;
    }

    public InvoiceWrapper done() throws Exception
    {
        if (invoice == null) {
            throw new Exception("Unexpected null invoice");
        }

        if (!deleted) {
            log.debug("FINALIZING INVOICE " + invoice);

            invoice.markRates();
            invoice.setAmount(invoice.sumRates());
            invoice.setTax(invoice.sumTaxes());
            invoice.computeDueDateIfNotSet();

            serviceProvider.getObjectBase().save(invoice);

            serviceProvider.publishEvent(new InvoiceCreatedEvent(invoice, serviceProvider.getObjectBase()
                    .getSystemRole()));
        }

        return this;
    }

    public InvoiceWrapper addOrder(CustomerOrder order)
    {
        if (invoice.getId() == null) {
            // not saved yet
            serviceProvider.getObjectBase().save(invoice);
        }

        invoice.addOrder(order);

        return this;
    }

    public InvoiceWrapper setCustomer(Customer customer)
    {
        if (customer == null) {
            throw new Error("Null customer to invoice wapper");
        }
        
        if (customer.getCustomerBilling() == null) {
            throw new Error("Null customer billing to invoice wapper");
        }
        
        invoice.setCustomer(customer);

        customer.getCustomerBilling().setLastInvoiced(ZonedDateTime.now(ZoneId.of("UTC")));

        return this;
    }

}
