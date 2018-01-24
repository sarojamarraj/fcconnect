/**
 * 
 */
package com.freightcom.api.services.payment;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.freightcom.api.model.PaymentTransaction;
import com.freightcom.api.model.StoredCreditCard;
import com.freightcom.api.services.dataobjects.InvoicePayment;

/**
 * @author bryan
 *
 */
public class PaymentProcessorImplTest
{
    PaymentProcessor paymentProcessor = new PaymentProcessorImpl();

    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception
    {
    }

    /**
     * @throws java.lang.Exception
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception
    {
    }

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception
    {
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception
    {
    }

    /**
     * Test method for {@link com.freightcom.api.services.payment.PaymentProcessorImpl#purchase(com.freightcom.api.services.dataobjects.InvoicePayment, com.freightcom.api.model.PaymentTransaction)}.
     */
    @Test
    public void testPurchase()
    {
        InvoicePayment payment = new InvoicePayment();
        PaymentTransaction transaction = new PaymentTransaction();
        
        Object purchase = paymentProcessor.purchase(new BigDecimal("5.00"), payment, transaction);
        
        assertNull(purchase);
    }

    /**
     * Test method for {@link com.freightcom.api.services.payment.PaymentProcessorImpl#storeCard()}.
     */
    @Test
    public void testStoreCard()
    {
        StoredCreditCard storedCard = paymentProcessor.storeCard(null);
        
        assertTrue(storedCard != null);
    }

    /**
     * Test method for {@link com.freightcom.api.services.payment.PaymentProcessorImpl#storedCardPurchase(java.lang.String)}.
     */
    @Test
    public void testStoredCardPurchase()
    {
        System.out.println("\n\nStoring Card\n\n");
        StoredCreditCard storedCard = paymentProcessor.storeCard(null);
        System.out.println("\n\nPurchasing\n\n");
        String result = paymentProcessor.storedCardPurchase(null, storedCard);
        
        assertNotNull(result);
    }

}
