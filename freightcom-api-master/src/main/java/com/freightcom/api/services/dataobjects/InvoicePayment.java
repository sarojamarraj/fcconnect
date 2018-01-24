package com.freightcom.api.services.dataobjects;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.freightcom.api.model.Credit;
import com.freightcom.api.model.Invoice;
import com.freightcom.api.model.PaymentTransaction;
import com.freightcom.api.repositories.ObjectBase;
import com.freightcom.api.services.PermissionChecker;
import com.freightcom.api.util.StringUtil;

public class InvoicePayment extends Payment
{
    private List<Long> credits;
    private List<Long> invoices;
    private String paymentType;
    private String chequeNumber;
    private Boolean creditCardPayment;
    private BigDecimal applyCredit;
    private String reference;
    private boolean creditOnly = false;

    public BigDecimal getApplyCredit()
    {
        return applyCredit;
    }

    public void setApplyCredit(BigDecimal applyCredit)
    {
        this.applyCredit = applyCredit;
    }

    public List<Long> getCredits()
    {
        return credits;
    }

    public void setCredits(List<Long> credits)
    {
        this.credits = credits;
    }

    public List<Long> getInvoices()
    {
        return invoices;
    }

    public void setInvoices(List<Long> invoices)
    {
        this.invoices = invoices;
    }

    public List<Invoice> getInvoiceObjects(ObjectBase objectBase, PermissionChecker permissionChecker)
    {
        List<Invoice> invoices = new ArrayList<Invoice>();

        if (getInvoices() != null) {
            for (Long id : getInvoices()) {
                Invoice invoice = objectBase.getInvoice(id);

                permissionChecker.check(invoice);

                invoices.add(invoice);
            }
        }

        return invoices;
    }

    public List<Credit> getCreditObjects(ObjectBase objectBase, PermissionChecker permissionChecker)
    {
        List<Credit> credits = new ArrayList<Credit>();

        if (getCredits() != null) {
            for (Long id : getCredits()) {
                Credit credit = objectBase.getCredit(id);

                permissionChecker.check(credit);

                credits.add(credit);
            }
        }

        return credits;
    }

    public String getCheckNumber()
    {
        return chequeNumber;
    }

    public void setCheckNumber(String chequeNumber)
    {
        this.chequeNumber = chequeNumber;
    }

    public String getReference()
    {
        return reference;
    }

    public void setReference(String reference)
    {
        this.reference = reference;
    }

    public String getChequeNumber()
    {
        return chequeNumber;
    }

    public void setChequeNumber(String chequeNumber)
    {
        this.chequeNumber = chequeNumber;
    }

    public boolean getCreditCardPayment()
    {
        return getCreditCardNumber() != null || (creditCardPayment != null && creditCardPayment);
    }

    public void setCreditCardPayment(Boolean creditCardPayment)
    {
        this.creditCardPayment = creditCardPayment;
    }

    public void updateTransaction(PaymentTransaction transaction)
    {
        transaction.setChequeNumber(this.getChequeNumber());
        transaction.setPaymentType(this.getPaymentType());
        transaction.setCredit(this.getApplyCredit());
        transaction.setPayment(this.getPayment());
    }

    public String getPaymentType()
    {
        return paymentType;
    }

    public void setPaymentType(String paymentType)
    {
        this.paymentType = paymentType;
    }

    public String toString()
    {
        if (getCreditCardId() != null) {
            return "INVOICE PAYMENT " + getCreditCardId();
        } else if (getCreditCardNumber() != null) {
            return "INVOICE PAYMENT ************" + StringUtil.substring(getCreditCardNumber(), -4);
        } else {
            return "INVOICE PAYMENT no cc";
        }
    }

    public boolean isCreditOnly()
    {
        return creditOnly;
    }

    public void setCreditOnly(boolean creditOnly)
    {
        this.creditOnly = creditOnly;
    }
}
