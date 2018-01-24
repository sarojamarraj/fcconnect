package com.freightcom.api.services;

import java.util.Map;

import org.springframework.data.domain.Pageable;

import com.freightcom.api.model.Charge;
import com.freightcom.api.model.views.View;

public interface ChargeService
{
    View reconcileCharge(Long id, Charge chargeData, Map<String, Object> attributes);

    Object getCharges(Map<String, Object> criteria, Pageable pageable);

    Object findByInvoiceId(Long invoice_id, Pageable pageable) throws Exception;

    Object findByOrderId(Long order_id, Pageable pageable);

    void autoChargeMonthly();

    void autoChargeBiWeekly();

    void autoChargeWeekly();

    void autoChargeDaily();

    void autoChargeOnDueDate();
}
