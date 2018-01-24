package com.freightcom.api.services.payment;

import java.math.BigDecimal;

import com.freightcom.api.model.PaymentTransaction;
import com.freightcom.api.model.StoredCreditCard;
import com.freightcom.api.services.dataobjects.InvoicePayment;

public interface PaymentProcessor
{

    Object purchase(BigDecimal amount, InvoicePayment payment, PaymentTransaction transaction);

    StoredCreditCard storeCard(InvoicePayment payment);

    String storedCardPurchase(BigDecimal amount, StoredCreditCard storedCard);

}
