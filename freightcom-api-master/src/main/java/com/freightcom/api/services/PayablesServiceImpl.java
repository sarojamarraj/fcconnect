package com.freightcom.api.services;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.freightcom.api.ApiSession;
import com.freightcom.api.SystemConfiguration;
import com.freightcom.api.controllers.transfer.PrintChequeRequest;
import com.freightcom.api.events.PayablesCreatedEvent;
import com.freightcom.api.events.SystemLogEvent;
import com.freightcom.api.model.Charge;
import com.freightcom.api.model.Payable;
import com.freightcom.api.model.PayableEntity;
import com.freightcom.api.model.Service;
import com.freightcom.api.model.UserRole;
import com.freightcom.api.model.views.PayableView;
import com.freightcom.api.model.views.View;
import com.freightcom.api.repositories.ObjectBase;
import com.freightcom.api.repositories.PayableRepository;
import com.freightcom.api.repositories.custom.PayablesSpecification;
import com.freightcom.api.services.converters.PayablesConverter;
import com.freightcom.api.services.dataobjects.GenerateChequeRequest;

@Component
public class PayablesServiceImpl extends ServicesCommon implements PayablesService
{
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final PagedResourcesAssembler<View> pagedAssembler;
    private final PayableRepository payableRepository;
    private final SystemConfiguration systemConfiguration;
    private final ObjectBase objectBase;
    private final DocumentManager documentManager;

    /**
     *
     */
    @Autowired
    public PayablesServiceImpl(final ApiSession apiSession, final PagedResourcesAssembler<View> pagedAssembler,
            final PayableRepository payableRepository, final SystemConfiguration systemConfiguration,
            final ObjectBase objectBase, final DocumentManager documentManager)
    {
        super(apiSession);

        this.pagedAssembler = pagedAssembler;
        this.payableRepository = payableRepository;
        this.systemConfiguration = systemConfiguration;
        this.objectBase = objectBase;
        this.documentManager = documentManager;
    }

    @Override
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('FREIGHTCOM_STAFF')")
    public Object listPayables(Map<String, Object> criteria, Pageable pageable)
    {
        Page<View> orders = payableRepository.findAll(new PayablesSpecification(criteria), pageable)
                .map(new PayablesConverter());

        return pagedAssembler.toResource(orders);
    }

    @Override
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('FREIGHTCOM_STAFF')")
    public Object getPayable(Long payableId)
    {
        Payable payable = payableRepository.findOne(payableId);

        if (payable == null) {
            throw new ResourceNotFoundException("Not found");
        }

        payable.setGroupedCharges(objectBase.groupedCharges(payable));
        payable.setFromAddress(systemConfiguration.getFromAddress());

        return new PayableView(payable);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('FREIGHTCOM_STAFF')")
    public List<Payable> generatePayables()
    {
        List<Payable> newPayables = new ArrayList<Payable>();

        for (Service service : objectBase.servicesWithUnbilledCharges()) {
            Payable payable = generateServicePayable(service, getRole());

            if (payable != null) {
                newPayables.add(payable);
            }
        }

        return newPayables;
    }

    @Override
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('FREIGHTCOM_STAFF')")
    @Transactional
    public boolean deletePayable(Long payableId)
    {
        Payable payable = payableRepository.findOne(payableId);

        if (payable == null) {
            throw new ResourceNotFoundException("Not found");
        }

        objectBase.delete(payable);

        return true;
    }

    @Override
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('FREIGHTCOM_STAFF')")
    @Transactional
    public Payable markPaid(Long payableId)
    {
        Payable payable = payableRepository.findOne(payableId);

        if (payable == null) {
            throw new ResourceNotFoundException("Not found");
        }

        payable.markPaid();

        return payable;
    }

    @Override
    public void runAllPayables()
    {
        payablesBackgroundProcess(Service.Term.DAILY);
        payablesBackgroundProcess(Service.Term.WEEKLY);
        payablesBackgroundProcess(Service.Term.BIWEEKLY);
        payablesBackgroundProcess(Service.Term.MONTHLY);
    }

    /**
     * Runs with different terms according to cron
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void payablesBackgroundProcess(Service.Term term)
    {
        // Check if this is a second Friday
        ZonedDateTime timestamp = ZonedDateTime.now(ZoneId.of("UTC"));
        // January 6, 2017 was a Friday
        ZonedDateTime biweekly_start = ZonedDateTime.of(2017, 1, 6, 0, 0, 0, 0, ZoneId.of("America/Toronto"));
        long daysSinceStart = ChronoUnit.DAYS.between(biweekly_start, timestamp);

        // crons control processing time, except biweekly needs to check if it
        // is a second Friday
        boolean process = term != Service.Term.BIWEEKLY || daysSinceStart % 14 == 0;

        if (process) {

            UserRole systemRole = objectBase.getSystemRole();

            log.debug("PROCESSING SERVICES CHARGES " + term);

            for (Service service : objectBase.servicesWithUnbilledCharges(term)) {
                generateServicePayable(service, systemRole);
            }
        }
    }

    /**
     *
     */
    private Payable generateServicePayable(Service service, UserRole role)
    {
        Payable payable = null;

        List<Charge> charges = objectBase.serviceUnbilledCharges(service);

        if (charges.size() > 0) {
            ZonedDateTime timestamp = ZonedDateTime.now();
            payable = new Payable();
            payable.setService(service);
            payable.setStatus("unpaid");
            payable.setDueAt(timestamp.plusDays(30L));

            for (Charge charge : charges) {
                payable.addCharge(charge);
                charge.setReportedAt(timestamp);
            }

            payableRepository.save(payable);
            objectBase.refresh(payable);
            payable.setGroupedCharges(objectBase.groupedCharges(payable));

            publishEvent(new PayablesCreatedEvent(payable, role));
            publishEvent(new SystemLogEvent(payable, role.getUser(), "payable generated", null));
        }

        return payable;
    }

    @Override
    @Transactional
    public Object printCheques(PrintChequeRequest request) throws Exception
    {
        if (!getApiSession().isAdmin()) {
            throw new AccessDeniedException("Not authorized");
        }

        Validation.get()
                .test(request.getIds() != null && request.getIds()
                        .size() > 0, "ids", "No ids specified")
                .testNotEmpty(request.getStartingChequeNumber(), "startingChequeNumber")
                .throwIfFailed();

        List<PayableEntity> toPay;

        if (request.isPayables()) {
            toPay = request.getIds()
                    .stream()
                    .map(id -> getObjectBase().getPayable(id))
                    .collect(Collectors.toList());
        } else {
            toPay = request.getIds()
                    .stream()
                    .map(id -> getObjectBase().getCommissionPayable(id))
                    .collect(Collectors.toList());
        }

        ZonedDateTime transactionDate = ZonedDateTime.now(ZoneId.of("UTC"));
        int chequeNumber = request.getStartingChequeNumber();

        for (PayableEntity item : toPay) {
            Validation.get()
                    .test(!item.isPaid(), "id", "Item already paid")
                    .throwIfFailed();

            item.markPaid(chequeNumber, transactionDate);

            chequeNumber++;

            documentManager.executeCommand(GenerateChequeRequest.get()
                                           .setDate(item.getPaidAt().toString())
                                           .setName(item.getName())
                                           .setNumericAmount(item.getPaidAmount().toString()));
        }

        return UUID.randomUUID();
    }
}
