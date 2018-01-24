package com.freightcom.api;

import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.freightcom.api.services.InvoiceService;
import com.freightcom.api.services.OrderService;
import com.freightcom.api.services.PayablesService;
import com.freightcom.api.services.ChargeService;
import com.freightcom.api.services.CommissionsService;


import com.freightcom.api.model.Service;
import com.freightcom.api.model.Agent;

@Component
public class ScheduledTasks
{
    @SuppressWarnings("unused")
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    OrderService orderService;

    @Autowired
    InvoiceService invoiceService;

    @Autowired
    ChargeService chargeService;

    @Autowired
    PayablesService payablesService;

    @Autowired
    CommissionsService commissionsService;

    // Every Day at 12:01 AM Eastern Time
    @Scheduled(cron="0 1 0 * * ?", zone="America/Toronto")
    public void updateReadyForShipping() throws Exception
    {
        orderService.updateReadyForShipping();
    }

    // Every Day at 5:00 AM Eastern Time
    @Scheduled(cron="0 0 5 * * ?", zone="America/Toronto")
    public void autoInvoiceDaily() throws Exception
    {
        invoiceService.autoInvoiceDaily();
    }

    // Every Friday at 5:00 AM Eastern Time
    @Scheduled(cron="0 0 5 ? * FRI", zone="America/Toronto")
    public void autoInvoicWeekly() throws Exception
    {
        invoiceService.autoInvoicWeekly();
    }

    // Every other Friday at 5:00 AM Eastern Time
    @Scheduled(cron="0 0 5 ? * FRI", zone="America/Toronto")
    public void autoInvoiceBiWeekly() throws Exception
    {
        invoiceService.autoInvoiceBiWeekly();
    }

    // Every last day of month at 5:00 AM Eastern time
    @Scheduled(cron="0 0 5 28-31 * ?", zone="America/Toronto")
    public void autoInvoiceMonthly() throws Exception
    {
        // Runs on 28, 29, 30, 31, so check if we are acutally on the last day of the month first
        final Calendar c = Calendar.getInstance();

        if (c.get(Calendar.DATE) == c.getActualMaximum(Calendar.DATE)) {
            invoiceService.autoInvoiceMonthly();
        }
    }

    // Payables

    // Every Day at 3:00 AM Eastern Time
    @Scheduled(cron = "0 0 3 * * ?", zone="America/Toronto")
    public void runGeneratePayablesDaily()
    {
        payablesService.payablesBackgroundProcess(Service.Term.DAILY);
    }

    // Every Friday at 8:00 AM Eastern Time
    @Scheduled(cron = "0 0 8 ? * FRI", zone="America/Toronto")
    public void runGeneratePayablesWeekly()
    {
        payablesService.payablesBackgroundProcess(Service.Term.WEEKLY);
    }

    // Every Friday at 8:00 AM Eastern Time
    @Scheduled(cron = "0 0 8 ? * FRI", zone="America/Toronto")
    public void runGeneratePayablesBiweekly()
    {
        payablesService.payablesBackgroundProcess(Service.Term.BIWEEKLY);
    }

    // First of month, 3:00am
    @Scheduled(cron = "0 0 3 1 * ?", zone="America/Toronto")
    public void runGeneratePayablesMonthly()
    {
        payablesService.payablesBackgroundProcess(Service.Term.MONTHLY);
    }


    // Commissions

    // Every Friday at 8:00 AM Eastern Time
    @Scheduled(cron = "0 0 8 ? * FRI", zone="America/Toronto")
    public void runGenerateCommissionsWeekly()
    {
        commissionsService.commissionsBackgroundProcess(Agent.Term.WEEKLY);
    }

    // Every Friday at 8:00 AM Eastern Time
    @Scheduled(cron = "0 0 8 ? * FRI", zone="America/Toronto")
    public void runGenerateCommissionsBiweekly()
    {
        commissionsService.commissionsBackgroundProcess(Agent.Term.BIWEEKLY);
    }

    // First of month, 3:00am
    @Scheduled(cron = "0 0 3 1 * ?", zone="America/Toronto")
    public void runGenerateCommissionsMonthly()
    {
        commissionsService.commissionsBackgroundProcess(Agent.Term.MONTHLY);
    }

    // Auto charge

    // Every Day at 5:00 AM Eastern Time
    @Scheduled(cron="0 0 5 * * ?", zone="America/Toronto")
    public void autoChargeOnDueDate() throws Exception
    {
        chargeService.autoChargeOnDueDate();
    }

    // Every Day at 5:00 AM Eastern Time
    @Scheduled(cron="0 0 5 * * ?", zone="America/Toronto")
    public void autoChargeDaily() throws Exception
    {
        chargeService.autoChargeDaily();
    }

    // Every Friday at 5:00 AM Eastern Time
    @Scheduled(cron="0 0 5 ? * FRI", zone="America/Toronto")
    public void autoChargeWeekly() throws Exception
    {
        chargeService.autoChargeWeekly();
    }

    // Every other Friday at 5:00 AM Eastern Time
    @Scheduled(cron="0 0 5 ? * FRI", zone="America/Toronto")
    public void autoChargeBiWeekly() throws Exception
    {
        chargeService.autoChargeBiWeekly();
    }

    // Every last day of month at 5:00 AM Eastern time
    @Scheduled(cron="0 0 5 28-31 * ?", zone="America/Toronto")
    public void autoChargeMonthly() throws Exception
    {
        // Runs on 28, 29, 30, 31, so check if we are acutally on the last day of the month first
        final Calendar c = Calendar.getInstance();

        if (c.get(Calendar.DATE) == c.getActualMaximum(Calendar.DATE)) {
            chargeService.autoChargeMonthly();
        }
    }

}
