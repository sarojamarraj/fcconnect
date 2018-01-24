package com.freightcom.api.services.payment;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.freightcom.api.model.PaymentTransaction;
import com.freightcom.api.model.StoredCreditCard;
import com.freightcom.api.services.dataobjects.InvoicePayment;

import JavaAPI.AvsInfo;
import JavaAPI.HttpsPostRequest;
import JavaAPI.Purchase;
import JavaAPI.Receipt;
import JavaAPI.ResAddCC;
import JavaAPI.ResPurchaseCC;

/**
 * @author bryan
 *
 *         Moneris (documentation available here)
 *         https://github.com/Moneris/eCommerce-Unified-API-Java
 *
 *         MasterCard 5454545454545454
 *
 *         Visa 4242424242424242
 *
 *         Amex 373599005095005
 *
 *         JCB 3566007770015365
 *
 *         Diners 36462462742008
 *
 *         Penny amounts for testing responses: https://developer.moneris.com/More/Testing/Penny%20Value%20Simulator
 *
 */
@Component
public class PaymentProcessorImpl implements PaymentProcessor
{
    @Value("${freightcom.moneris.storeid:store5}")
    private String store_id;

    @Value("${freightcom.moneris.api_token:yesguy}")
    private String api_token;

    @Value("${freightcom.moneris.test_mode:true}")
    private boolean test_mode;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public Object purchase(BigDecimal amount, InvoicePayment payment, PaymentTransaction transaction)
    {
        java.util.Date createDate = new java.util.Date();
        String order_id = "Test" + createDate.getTime();
        String pan = "4242424242424242";
        String expdate = "1901"; // YYMM format
        String crypt = "7";
        String processing_country_code = "CA";
        boolean status_check = false;

        amount = new BigDecimal("5.00");

        Purchase purchase = new Purchase();
        purchase.setOrderId(order_id);
        purchase.setAmount(amount.toString());
        purchase.setPan(pan);
        purchase.setExpdate(expdate);
        purchase.setCryptType(crypt);
        purchase.setDynamicDescriptor("123456");
        // purchase.setWalletIndicator(""); //Refer documentation for possible
        // values

        // Optional - Set for Multi-Currency only
        // setAmount must be 0.00 when using multi-currency
        // purchase.setMCPAmount("500"); //penny value amount 1.25 = 125
        // purchase.setMCPCurrencyCode("840"); //ISO-4217 country currency
        // number

        HttpsPostRequest mpgReq = new HttpsPostRequest();
        mpgReq.setProcCountryCode(processing_country_code);

        // false if not testing
        mpgReq.setTestMode(test_mode);
        mpgReq.setStoreId(store_id);
        mpgReq.setApiToken(api_token);
        mpgReq.setTransaction(purchase);
        mpgReq.setStatusCheck(status_check);

        // Optional - Proxy
        // true to use proxy
        mpgReq.setProxy(false);
        mpgReq.setProxyHost("proxyURL");
        mpgReq.setProxyPort("proxyPort");
        mpgReq.setProxyUser("proxyUser"); // optional - domainName\User
        mpgReq.setProxyPassword("proxyPassword"); // optional

        mpgReq.send();

        Receipt receipt = mpgReq.getReceipt();

        log.debug("CardType = " + receipt.getCardType());
        log.debug("TransAmount = " + receipt.getTransAmount());
        log.debug("TxnNumber = " + receipt.getTxnNumber());
        log.debug("ReceiptId = " + receipt.getReceiptId());
        log.debug("TransType = " + receipt.getTransType());
        log.debug("ReferenceNum = " + receipt.getReferenceNum());
        log.debug("ResponseCode = " + receipt.getResponseCode());
        log.debug("ISO = " + receipt.getISO());
        log.debug("BankTotals = " + receipt.getBankTotals());
        log.debug("Message = " + receipt.getMessage());
        log.debug("AuthCode = " + receipt.getAuthCode());
        log.debug("Complete = " + receipt.getComplete());
        log.debug("TransDate = " + receipt.getTransDate());
        log.debug("TransTime = " + receipt.getTransTime());
        log.debug("Ticket = " + receipt.getTicket());
        log.debug("TimedOut = " + receipt.getTimedOut());
        log.debug("IsVisaDebit = " + receipt.getIsVisaDebit());
        log.debug("MCPAmount = " + receipt.getMCPAmount());
        log.debug("MCPCurrencyCode = " + receipt.getMCPCurrencyCode());

        return null;
    }

    @Override
    public StoredCreditCard storeCard(InvoicePayment payment)
    {
        String store_id = "store5";
        String api_token = "yesguy";
        String pan = "5454545454545454";
        String expdate = "1912";
        String phone = "0000000000";
        String email = "bob@smith.com";
        String note = "my note";
        String cust_id = "customer1";
        String crypt_type = "7";
        String processing_country_code = "CA";
        boolean status_check = false;

        AvsInfo avsCheck = new AvsInfo();
        avsCheck.setAvsStreetNumber("212");
        avsCheck.setAvsStreetName("Payton Street");
        avsCheck.setAvsZipCode("M1M1M1");

        ResAddCC resaddcc = new ResAddCC();
        resaddcc.setPan(pan);
        resaddcc.setExpdate(expdate);
        resaddcc.setCryptType(crypt_type);
        resaddcc.setCustId(cust_id);
        resaddcc.setPhone(phone);
        resaddcc.setEmail(email);
        resaddcc.setNote(note);
        resaddcc.setAvsInfo(avsCheck);

        HttpsPostRequest mpgReq = new HttpsPostRequest();
        mpgReq.setProcCountryCode(processing_country_code);

        //false or comment out this line for production transactions
        mpgReq.setTestMode(true);
        mpgReq.setStoreId(store_id);
        mpgReq.setApiToken(api_token);
        mpgReq.setTransaction(resaddcc);
        mpgReq.setStatusCheck(status_check);
        mpgReq.send();

        Receipt receipt = mpgReq.getReceipt();

        log.debug("DataKey = " + receipt.getDataKey());
        log.debug("ResponseCode = " + receipt.getResponseCode());
        log.debug("Message = " + receipt.getMessage());
        log.debug("TransDate = " + receipt.getTransDate());
        log.debug("TransTime = " + receipt.getTransTime());
        log.debug("Complete = " + receipt.getComplete());
        log.debug("TimedOut = " + receipt.getTimedOut());
        log.debug("ResSuccess = " + receipt.getResSuccess());
        log.debug("PaymentType = " + receipt.getPaymentType());
        log.debug("Cust ID = " + receipt.getResCustId());
        log.debug("Phone = " + receipt.getResPhone());
        log.debug("Email = " + receipt.getResEmail());
        log.debug("Note = " + receipt.getResNote());
        log.debug("MaskedPan = " + receipt.getResMaskedPan());
        log.debug("Exp Date = " + receipt.getResExpdate());
        log.debug("Crypt Type = " + receipt.getResCryptType());
        log.debug("Avs Street Number = " + receipt.getResAvsStreetNumber());
        log.debug("Avs Street Name = " + receipt.getResAvsStreetName());
        log.debug("Avs Zipcode = " + receipt.getResAvsZipcode());

        StoredCreditCard storedCreditCard = new StoredCreditCard();
        
        
        storedCreditCard.setDataKey(receipt.getDataKey());
        storedCreditCard.setResponseCode(receipt.getResponseCode());
        storedCreditCard.setMessage(receipt.getMessage());
        storedCreditCard.setTransactionDate(receipt.getTransDate());
        storedCreditCard.setTransactionTime(receipt.getTransTime());
        storedCreditCard.setComplete("true".equalsIgnoreCase(receipt.getComplete()));
        storedCreditCard.setTimedOut("true".equalsIgnoreCase(receipt.getTimedOut()));
        storedCreditCard.setSuccess("true".equalsIgnoreCase(receipt.getResSuccess()));
        storedCreditCard.setPaymentType(receipt.getPaymentType());
        storedCreditCard.setCustomerId(receipt.getResCustId());
        storedCreditCard.setPhone(receipt.getResPhone());
        storedCreditCard.setEmail(receipt.getResEmail());
        storedCreditCard.setNote(receipt.getResNote());
        storedCreditCard.setMaskedCard(receipt.getResMaskedPan());
        storedCreditCard.setExpiryDate(receipt.getResExpdate());
        storedCreditCard.setCryptType(receipt.getResCryptType());
        storedCreditCard.setStreetNumber(receipt.getResAvsStreetNumber());
        storedCreditCard.setStreetName(receipt.getResAvsStreetName());
        storedCreditCard.setPostalCode(receipt.getResAvsZipcode());
        storedCreditCard.setCardType(receipt.getCardType());

        return storedCreditCard;
    }

    @Override
    public String storedCardPurchase(BigDecimal amount, StoredCreditCard storedCard)
    {
        java.util.Date createDate = new java.util.Date();

        String order_id = "Test"+createDate.getTime();
        String store_id = "store5";
        String api_token = "yesguy";
        String data_key = storedCard.getDataKey();
        String cust_id = "customer1"; //if sent will be submitted, otherwise cust_id from profile will be used
        String crypt_type = "1";
        String descriptor = "my descriptor";
        String processing_country_code = "CA";
        boolean status_check = false;

        amount = new BigDecimal("1.00");

        ResPurchaseCC resPurchaseCC = new ResPurchaseCC();
        resPurchaseCC.setData(data_key);
        resPurchaseCC.setOrderId(order_id);
        resPurchaseCC.setCustId(cust_id);
        resPurchaseCC.setAmount(amount.toString());
        resPurchaseCC.setCryptType(crypt_type);
        resPurchaseCC.setDynamicDescriptor(descriptor);


        HttpsPostRequest mpgReq = new HttpsPostRequest();
        mpgReq.setProcCountryCode(processing_country_code);

        //false or comment out this line for production transactions
        mpgReq.setTestMode(true);
        mpgReq.setStoreId(store_id);
        mpgReq.setApiToken(api_token);
        mpgReq.setTransaction(resPurchaseCC);
        mpgReq.setStatusCheck(status_check);
        mpgReq.send();

        Receipt receipt = mpgReq.getReceipt();

        log.debug("DataKey = " + receipt.getDataKey());
        log.debug("ReceiptId = " + receipt.getReceiptId());
        log.debug("ReferenceNum = " + receipt.getReferenceNum());
        log.debug("ResponseCode = " + receipt.getResponseCode());
        log.debug("AuthCode = " + receipt.getAuthCode());
        log.debug("Message = " + receipt.getMessage());
        log.debug("TransDate = " + receipt.getTransDate());
        log.debug("TransTime = " + receipt.getTransTime());
        log.debug("TransType = " + receipt.getTransType());
        log.debug("Complete = " + receipt.getComplete());
        log.debug("TransAmount = " + receipt.getTransAmount());
        log.debug("CardType = " + receipt.getCardType());
        log.debug("TxnNumber = " + receipt.getTxnNumber());
        log.debug("TimedOut = " + receipt.getTimedOut());
        log.debug("ResSuccess = " + receipt.getResSuccess());
        log.debug("PaymentType = " + receipt.getPaymentType());
        log.debug("IsVisaDebit = " + receipt.getIsVisaDebit());
        log.debug("Cust ID = " + receipt.getResCustId());
        log.debug("Phone = " + receipt.getResPhone());
        log.debug("Email = " + receipt.getResEmail());
        log.debug("Note = " + receipt.getResNote());
        log.debug("Masked Pan = " + receipt.getResMaskedPan());
        log.debug("Exp Date = " + receipt.getResExpdate());
        log.debug("Crypt Type = " + receipt.getResCryptType());
        log.debug("Avs Street Number = " + receipt.getResAvsStreetNumber());
        log.debug("Avs Street Name = " + receipt.getResAvsStreetName());
        log.debug("Avs Zipcode = " + receipt.getResAvsZipcode());

        return receipt.getReceiptId();
    }
}
