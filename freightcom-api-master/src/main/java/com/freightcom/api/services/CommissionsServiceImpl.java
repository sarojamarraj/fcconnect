package com.freightcom.api.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.freightcom.api.ApiSession;
import com.freightcom.api.ReportableError;
import com.freightcom.api.SystemConfiguration;
import com.freightcom.api.events.CommissionReportCreatedEvent;
import com.freightcom.api.events.SystemLogEvent;
import com.freightcom.api.model.Agent;
import com.freightcom.api.model.Charge;
import com.freightcom.api.model.ChargeCommission;
import com.freightcom.api.model.CommissionPayable;
import com.freightcom.api.model.UserRole;
import com.freightcom.api.model.views.CommissionPayableView;
import com.freightcom.api.model.views.View;
import com.freightcom.api.repositories.CommissionPayableRepository;
import com.freightcom.api.repositories.ObjectBase;
import com.freightcom.api.repositories.custom.CommissionsPayableSpecification;
import com.freightcom.api.services.converters.CommissionPayableConverter;

@Component
public class CommissionsServiceImpl extends ServicesCommon implements CommissionsService
{
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final PagedResourcesAssembler<View> pagedAssembler;
    private final CommissionPayableRepository commissionPayableRepository;
    private final ObjectBase objectBase;
    private final SystemConfiguration systemConfiguration;
    private final Boolean commissionsLock = Boolean.TRUE;

    /**
     *
     */
    @Autowired
    public CommissionsServiceImpl(final ApiSession apiSession, final PagedResourcesAssembler<View> pagedAssembler,
            final CommissionPayableRepository commissionPayableRepository,
                                  final SystemConfiguration systemConfiguration,
                                  final ObjectBase objectBase)
    {
        super(apiSession);

        this.pagedAssembler = pagedAssembler;
        this.commissionPayableRepository = commissionPayableRepository;
        this.systemConfiguration = systemConfiguration;
        this.objectBase = objectBase;
    }

    @Override
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('FREIGHTCOM_STAFF') or hasAuthority('AGENT')")
    public Object listCommissions(Map<String, Object> criteria, Pageable pageable)
    {
        if (apiSession.isAgent()) {
            criteria.put("agentid", apiSession.getRole().getId());
        }

        Page<View> orders = commissionPayableRepository.findAll(new CommissionsPayableSpecification(criteria), pageable)
                .map(new CommissionPayableConverter());

        return pagedAssembler.toResource(orders);
    }

    @Override
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('FREIGHTCOM_STAFF')")
    public Object getCommission(Long commissionPayableId)
    {
        CommissionPayable commissionPayable = commissionPayableRepository.findOne(commissionPayableId);

        if (commissionPayable == null) {
            throw new ResourceNotFoundException("Commission payable " + commissionPayableId);
        }

        log.debug("COMMISSION PAYABLE SINGLE VIEW");

        for (Charge charge: commissionPayable.getCharges()) {
            log.debug("HAVE A CHARGE " + charge + " " + charge.getOrder());
        }

        commissionPayable.setFromAddress(systemConfiguration.getFromAddress());

        return new CommissionPayableView(commissionPayable);
    }

    @Override
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('FREIGHTCOM_STAFF')")
    @Transactional
    public Object deleteCommission(Long commissionPayableId)
    {
        CommissionPayable commissionPayable = commissionPayableRepository.findOne(commissionPayableId);

        if (commissionPayable == null) {
            throw new ResourceNotFoundException("Commission payable " + commissionPayableId);
        }

        objectBase.delete(commissionPayable);

        return "{ \"status\": \"ok\" }";
    }

    @Override
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('FREIGHTCOM_STAFF')")
    @Transactional
    public CommissionPayable markPaid(Long commissionPayableId)
    {
        CommissionPayable commissionPayable = commissionPayableRepository.findOne(commissionPayableId);

        if (commissionPayable == null) {
            throw new ResourceNotFoundException("Commission payable " + commissionPayableId);
        }

        commissionPayable.markPaid();

        return commissionPayable;
    }

    public List<CommissionPayable> calculateCommissions()
    {
        return calculateCommissions(null);
    }

    @Override
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('FREIGHTCOM_STAFF')")
    @Transactional
    public List<CommissionPayable> calculateCommissions(Long agentId)
    {
        synchronized (commissionsLock) {
            if (agentId != null) {
                Agent queryAgent = objectBase.getAgent(agentId);

                if (queryAgent == null) {
                    throw new ResourceNotFoundException("No such agent: " + agentId);
                }

                if (queryAgent.getParentSalesAgent() != null) {
                    throw new ReportableError("Cannot compute commission on sub agent");
                }
            }

            List<CommissionPayable> payables = new ArrayList<CommissionPayable>();

            Agent currentAgent = null;
            CommissionPayable commissionPayable = null;

            log.debug("QUERYING AGENT CHARGES " + agentId);

            for (Charge charge : objectBase.commissionPayableCharges(agentId)) {
                if (charge.getAgent()
                        .findRootAgent() != currentAgent) {
                    currentAgent = charge.getAgent()
                            .findRootAgent();
                    // This a statement of commissions payable to an agent
                    commissionPayable = new CommissionPayable();

                    commissionPayable.setTotalAmount(BigDecimal.ZERO);
                    commissionPayable.setAgent(currentAgent);
                    commissionPayable.setDueAt(ZonedDateTime.now()
                            .with(TemporalAdjusters.lastDayOfMonth()));

                    objectBase.save(commissionPayable);

                    payables.add(commissionPayable);
                }

                log.debug("COMMISSION PERCENT " + currentAgent.getCommissionPercent());
                log.debug("MARGIN " + charge.getMarkup());

                if (currentAgent.getCommissionPercent() != null && currentAgent.getCommissionPercent() > 0) {
                    BigDecimal amount = charge.getMarkup()
                            .multiply(new BigDecimal(currentAgent.getCommissionPercent() / 100.0));

                    ChargeCommission chargeCommission = new ChargeCommission();
                    chargeCommission.setCharge(charge);
                    chargeCommission.setCurrency(charge.getCurrency());
                    chargeCommission.setAmount(amount);
                    chargeCommission.setAgent(charge.getAgent());

                    commissionPayable.addChargeCommission(chargeCommission);

                    objectBase.save(chargeCommission);

                    commissionPayable.setTotalAmount(commissionPayable.getTotalAmount()
                            .add(amount));
                }

                charge.setCommissionCalculatedAt(ZonedDateTime.now());
            }

            return payables;
        }
    }

    @Override
    public void runAllCommissions()
    {
        commissionsBackgroundProcess(Agent.Term.WEEKLY);
        commissionsBackgroundProcess(Agent.Term.BIWEEKLY);
        commissionsBackgroundProcess(Agent.Term.MONTHLY);
    }

    /**
     * Runs with different terms according to cron schedules
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void commissionsBackgroundProcess(Agent.Term term)
    {
        // Check if this is a second Friday
        ZonedDateTime timestamp = ZonedDateTime.now(ZoneId.of("UTC"));
        // January 6, 2017 was a Friday
        ZonedDateTime biweekly_start = ZonedDateTime.of(2017, 1, 6, 0, 0, 0, 0, ZoneId.of("America/Toronto"));
        long daysSinceStart = ChronoUnit.DAYS.between(biweekly_start, timestamp);

        // crons control processing time, except biweekly needs to check if it is a second Friday
        boolean process = term != Agent.Term.BIWEEKLY || daysSinceStart % 14 == 0;

        if (process) {
            
            log.debug("PROCESSING AGENTS CHARGES " + term);
            
            synchronized (commissionsLock) {
                UserRole systemRole = objectBase.getSystemRole();
                Agent currentAgent = null;
                CommissionPayable commissionPayable = null;

                log.debug("QUERYING AGENT CHARGES ");

                for (Charge charge : objectBase.commissionPayableCharges(term)) {
                    if (charge.getAgent()
                        .findRootAgent() != currentAgent) {
                        currentAgent = charge.getAgent()
                            .findRootAgent();

                        // This a statement of commissions payable to an agent
                        commissionPayable = new CommissionPayable();

                        commissionPayable.setTotalAmount(BigDecimal.ZERO);
                        commissionPayable.setAgent(currentAgent);
                        commissionPayable.setDueAt(ZonedDateTime.now()
                                                   .with(TemporalAdjusters.lastDayOfMonth()));

                        objectBase.save(commissionPayable);

                        publishEvent(new CommissionReportCreatedEvent(commissionPayable, systemRole));
                        publishEvent(new SystemLogEvent(commissionPayable, systemRole.getUser(), "commission generated",
                                                        null));

                        // Compute commission if matches agent's term
                        log.debug("COMMISSION PERCENT " + currentAgent.getCommissionPercent());
                        log.debug("MARGIN " + charge.getMarkup());

                        if (currentAgent.getCommissionPercent() != null && currentAgent.getCommissionPercent() > 0) {
                            BigDecimal amount = charge.getMarkup()
                                .multiply(new BigDecimal(currentAgent.getCommissionPercent() / 100.0));

                            // Make sure rounded correctly
                            amount.setScale(2, RoundingMode.HALF_UP);
                            amount = new BigDecimal(String.format("%.2f", amount));

                            ChargeCommission chargeCommission = new ChargeCommission();
                            chargeCommission.setCharge(charge);
                            chargeCommission.setCurrency(charge.getCurrency());
                            chargeCommission.setAmount(amount);
                            chargeCommission.setAgent(charge.getAgent());

                            commissionPayable.addChargeCommission(chargeCommission);

                            objectBase.save(chargeCommission);

                            commissionPayable.setTotalAmount(commissionPayable.getTotalAmount()
                                                             .add(amount));
                        }

                        charge.setCommissionCalculatedAt(ZonedDateTime.now());
                    }
                }
            }
        }
    }
}
