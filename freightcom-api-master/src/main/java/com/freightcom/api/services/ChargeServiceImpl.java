package com.freightcom.api.services;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.freightcom.api.model.Charge;
import com.freightcom.api.model.Customer;
import com.freightcom.api.model.views.ChargeView;
import com.freightcom.api.model.views.View;
import com.freightcom.api.repositories.ChargeRepository;
import com.freightcom.api.repositories.ObjectBase;
import com.freightcom.api.repositories.custom.CustomerRepository;
import com.freightcom.api.repositories.custom.CustomerSpecification;

/**
 * @author bryan
 *
 */
@Component
public class ChargeServiceImpl extends ServicesCommon implements ChargeService
{
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final ObjectBase objectBase;
    private final ChargeRepository chargeRepository;
    private final PermissionChecker permissionChecker;
    private final CustomerRepository customerRepository;

    @Autowired
    public ChargeServiceImpl(final PagedResourcesAssembler<View> pagedAssembler,
            final ChargeRepository chargeRepository, final CustomerRepository customerRepository,
            final PermissionChecker permissionChecker, final ObjectBase objectBase)
    {
        super(null);
        this.permissionChecker = permissionChecker;
        this.objectBase = objectBase;
        this.chargeRepository = chargeRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public ChargeView reconcileCharge(Long id, Charge chargeData, Map<String, Object> attributes)
    {
        if (!permissionChecker.isFreightcomOrAdmin()) {
            throw new AccessDeniedException("Not authorized");
        }

        Charge charge = objectBase.getCharge(id);

        charge.setReconciled(true);

        if (attributes.get("cost") != null) {
            charge.setCost(chargeData.getCost());
        }

        return ChargeView.get(apiSession.getRole(), charge);
    }

    @Override
    public Object getCharges(Map<String, Object> criteria, Pageable pageable)
    {
        if (!permissionChecker.isFreightcomOrAdmin()) {
            throw new AccessDeniedException("Not authorized");
        }

        return chargeRepository.findAll(pageable);
    }

    @Override
    public Object findByInvoiceId(Long invoice_id, Pageable pageable) throws Exception
    {
        if (invoice_id == null) {
            throw new Exception("Missing invoice id");
        }

        if (!permissionChecker.isFreightcomOrAdmin()) {
            throw new AccessDeniedException("Not authorized");
        }

        log.debug("LOOKUP CHARGES '" + invoice_id + "'");

        return chargeRepository.findByInvoiceId(invoice_id, pageable);
    }

    @Override
    public Object findByOrderId(Long order_id, Pageable pageable)
    {
        if (!permissionChecker.isFreightcomOrAdmin()) {
            throw new AccessDeniedException("Not authorized");
        }
        return chargeRepository.findByOrderId(order_id, pageable);
    }

    /**
     * Update last auto charge run date in customer for the run paramters
     */
    @Transactional
    private void updateBillingRun(Customer.AutoCharge period, int days)
    {
        Map<String, Object> criteria = new HashMap<String, Object>();

        criteria.put("autocharge", period.toString());

        if (days > 0) {
            criteria.put("lastchargerun", new Integer(days));
        }

        for (Customer customer : customerRepository.findAll(new CustomerSpecification(criteria))) {
            customer.getCustomerBilling()
                    .setLastChargeRun(ZonedDateTime.now(ZoneId.of("UTC")));
        }
    }

    private void updateBillingRun(Customer.AutoCharge period)
    {
        updateBillingRun(period, 0);
    }

    @Override
    public void autoChargeOnDueDate()
    {
        updateBillingRun(Customer.AutoCharge.ON_DUE_DATE);
    }

    @Override
    public void autoChargeDaily()
    {
        updateBillingRun(Customer.AutoCharge.DAILY);
    }

    @Override
    public void autoChargeWeekly()
    {
        updateBillingRun(Customer.AutoCharge.WEEKLY);
    }

    @Override
    public void autoChargeBiWeekly()
    {
        updateBillingRun(Customer.AutoCharge.BIWEEKLY, 8);
    }

    @Override
    public void autoChargeMonthly()
    {
        updateBillingRun(Customer.AutoCharge.MONTHLY);
    }

    @Transactional
    private void autoCharge()
    {

    }
}
