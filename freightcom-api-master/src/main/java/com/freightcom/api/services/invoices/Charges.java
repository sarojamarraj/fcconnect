package com.freightcom.api.services.invoices;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import com.freightcom.api.model.Customer;
import com.freightcom.api.model.Invoice;

/**
 * @author bryan
 *
 */

public class Charges
{

    private final Customer customer;
    private final List<Invoice> invoices;
    private BigDecimal subTotal = BigDecimal.ZERO;
    private BigDecimal credit = BigDecimal.ZERO;
    private BigDecimal balance = BigDecimal.ZERO;

    public Charges(final Customer customer, final List<Invoice> invoices) throws Exception
    {
        this.customer = customer;
        this.invoices = invoices;

        if (customer == null) {
            throw new Exception("Charges has no customer");
        }

        if (invoices.isEmpty()) {
            throw new Exception("No invoices specified");
        }

        computeCharges();
    }

    private void computeCharges()
    {
        subTotal = invoices.stream()
                .map(invoice -> invoice.amountRemaining())
                .reduce(BigDecimal.ZERO, (total, amount) -> total.add(amount))
                .setScale(2);

        credit = customer.getCreditAvailable(invoices.get(0).getCurrency());

        if (credit.compareTo(subTotal) >= 0) {
            balance = BigDecimal.ZERO;
            credit = subTotal;
        } else {
            balance = subTotal.subtract(credit)
                    .setScale(2);
        }
    }

    public BigDecimal getSubTotal()
    {
        return subTotal;
    }

    public BigDecimal getCredit()
    {
        return credit;
    }

    public BigDecimal getBalance()
    {
        return balance;
    }

    public ChargesCustomerView getCustomer()
    {
        return new ChargesCustomerView(customer);
    }

    public List<ChargesInvoiceView> getInvoices()
    {
        return invoices.stream()
                .map(invoice -> new ChargesInvoiceView(invoice))
                .collect(Collectors.toList());
    }

}
