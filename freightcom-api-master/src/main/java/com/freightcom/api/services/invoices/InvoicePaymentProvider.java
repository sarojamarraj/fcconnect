/******************************************************************
              WebPal Product Suite Framework Libraries
-------------------------------------------------------------------
(c) 2002-present: all copyrights are with Palomino System Innovations Inc.
(Palomino Inc.) of Toronto, Canada

Unauthorized reproduction, licensing or disclosure of this source
code will be prosecuted. WebPal is a registered trademark of
Palomino System Innovations Inc. To report misuse please contact
info@palominosys.com or call +1 416 964 7333.
*******************************************************************/

package com.freightcom.api.services.invoices;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.access.AccessDeniedException;

import com.freightcom.api.events.InvoicePaidEvent;
import com.freightcom.api.events.SystemLogEvent;
import com.freightcom.api.model.AppliedPayment;
import com.freightcom.api.model.Credit;
import com.freightcom.api.model.CreditCard;
import com.freightcom.api.model.Customer;
import com.freightcom.api.model.Invoice;
import com.freightcom.api.model.PaymentTransaction;
import com.freightcom.api.model.StoredCreditCard;
import com.freightcom.api.model.UserRole;
import com.freightcom.api.services.ServiceProvider;
import com.freightcom.api.services.dataobjects.InvoicePayment;
import com.freightcom.api.services.payment.PaymentProcessor;

/**
 * @author bryan
 *
 */

public class InvoicePaymentProvider
{
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final ServiceProvider serviceProvider;
    private final PaymentProcessor paymentProcessor;

    private InvoicePaymentProvider(final ServiceProvider serviceProvider, final PaymentProcessor paymentProcessor)
    {
        this.serviceProvider = serviceProvider;
        this.paymentProcessor = paymentProcessor;
    }

    public static InvoicePaymentProvider get(final ServiceProvider serviceProvider,
            final PaymentProcessor paymentProcessor)
    {
        return new InvoicePaymentProvider(serviceProvider, paymentProcessor);
    }

    /**
     * @param invoices
     * @param credits
     * @param remaining
     * @param remainingCredit
     * @return
     * @throws AccessDeniedException
     */
    public Object payInvoiceNow(UserRole role, Customer loggedInCustomer, InvoicePayment payment, List<Invoice> invoices) throws AccessDeniedException
    {
        PaymentTransaction transaction = new PaymentTransaction();
        BigDecimal transactionAmount = BigDecimal.ZERO;
        List<Invoice> paidInvoices = new ArrayList<Invoice>();

        transaction.setCreatedByUserId(role.getUser()
                .getId());
        transaction.setUserId(role.getUser()
                .getId());
        transaction.setPaymentType(payment.getPaymentType());
        transaction.setReference(payment.getReference());
        transaction.setAmount(BigDecimal.ZERO);

        serviceProvider.getObjectBase()
                .save(transaction);

        for (Invoice invoice : invoices) {
            Customer customer = invoice.getCustomer();
            boolean fullyPaid = false;

            for (Credit customerCredit : customer.getPositiveCredits(invoice.getCurrency())) {
                if (!invoice.fullyPaid()) {
                    if (customerCredit.positiveAmountRemaining()) {
                        // Apply credit
                        invoice.applyCredit(customerCredit, null);

                        fullyPaid = invoice.fullyPaid();

                        log.debug("PAYNOW APPLIED CREDIT INVOICE " + invoice.debugPaymentStatus());

                    }
                } else {
                    break;
                }
            }

            if (!invoice.fullyPaid()) {
                // Pay remaining amount

                log.debug("PAYNOW PAYING INVOICE " + invoice.debugPaymentStatus());

                // Payment amount can be applied
                AppliedPayment appliedPayment = new AppliedPayment();

                appliedPayment.setPaymentTransaction(transaction);
                appliedPayment.setInvoiceId(invoice.getId());
                appliedPayment.setAmount(invoice.amountRemaining());

                transactionAmount = transactionAmount.add(invoice.amountRemaining());

                invoice.applyPayment(appliedPayment);

                fullyPaid = true;
            }

            if (fullyPaid) {
                serviceProvider.publishEvent(new InvoicePaidEvent(invoice, transaction, role));
                serviceProvider.publishEvent(new SystemLogEvent(invoice, role.getUser(), "invoice paid", null));
            }

            paidInvoices.add(invoice);
        }

        Throwable result = null;

        if (! payment.isCreditOnly()) {
            // Apply credits only
            result = applyCreditCard(role, loggedInCustomer, payment, transaction, transactionAmount);
        }

        transaction.setAmount(transactionAmount);
        transaction.setPayment(transactionAmount);

        return result == null ? paidInvoices : result;
    }

    public Throwable applyCreditCard(final UserRole role, final Customer customer, final InvoicePayment payment,
            final PaymentTransaction transaction, final BigDecimal amount)
    {
        Throwable result = null;

        log.debug("ENTER APPLY CREDIT CARD " + payment);

        try {
            // Catch any failures so transaction not rolled back
            // so that this attempt is recorded

            if (customer != null) {
                StoredCreditCard storedCard = null;

                // Check if vault, if not store in vault and use
                if (payment.getCreditCardId() == null) {
                    // A new credit card, store in value
                    storedCard = paymentProcessor.storeCard(payment);

                    CreditCard card = customer.updateCreditCards(payment);

                    if (card == null) {
                        throw new ResourceNotFoundException("Unable to update credit card");
                    }

                    card.setStoredCreditCard(storedCard);
                } else {
                    // A stored credit card referenced
                    CreditCard card = serviceProvider.getObjectBase()
                            .getCreditCard(payment.getCreditCardId());

                    if (card == null) {
                        throw new ResourceNotFoundException("Invalid credit card id");
                    } else if (card.getCustomer() != customer) {
                        throw new AccessDeniedException("Not logged in user's credit card");
                    } else if (card.getStoredCreditCard() == null) {
                        throw new ResourceNotFoundException("Stored card information is missing");
                    }

                    storedCard = card.getStoredCreditCard();
                }

                paymentProcessor.storedCardPurchase(amount, storedCard);
            } else {
                // Apply credit card once only
                paymentProcessor.purchase(amount, payment, transaction);
            }
        } catch (Throwable e) {
            log.error("PROBLEM WITH PAYMENT " + e.getMessage());
            result = e;
        }

        log.debug("DONE APPLY PAYMENT " + result);

        return result;
    }
}
