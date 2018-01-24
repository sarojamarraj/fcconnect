package com.freightcom.api.model.views;

import java.time.ZonedDateTime;
import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.freightcom.api.model.AppliedCredit;
import com.freightcom.api.model.AppliedPayment;
import com.freightcom.api.model.PaymentTransaction;

public class PaymentHistoryView
{
    private ZonedDateTime date;
    private BigDecimal amount;
    private String type;
    private String description;
    private Long id;

    public PaymentHistoryView(AppliedCredit credit)
    {
        date = credit.getCreatedAt();
        amount = credit.getAmount();
        type = "Credit";
        description = "Applied credit #" + credit.getId();
    }

    public PaymentHistoryView(AppliedPayment payment)
    {
        date = payment.getCreatedAt();
        amount = payment.getAmount();

        PaymentTransaction transaction = payment.getPaymentTransaction();

        if (transaction != null) {
            id = transaction.getId();

            if (transaction.getPaymentType() == null) {
                type = "";
                description = "";
            } else {
                switch (transaction.getPaymentType()) {
                case "credit card":
                    type = "Credit card";
                    break;

                case "cash":
                    type = "Cash";
                    break;

                case "check":
                    type = "Cheque";
                    description = "#" + transaction.getChequeNumber();
                    break;

                case "wireTransfer":
                    type = "Wire Transfer";
                    break;

                default:
                    type = "";
                    description = "";
                }
            }
        }
    }

    public Long getId()
    {
        return id;
    }

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm")
    public ZonedDateTime getPaymentDate()
    {
        // TODO - localize?
        return date;
    }

    public String getPaymentType()
    {
        return type;
    }

    public String getDescription()
    {
        return description;
    }

    public BigDecimal getPaymentAmount()
    {
        return amount.setScale(2);
    }
}
