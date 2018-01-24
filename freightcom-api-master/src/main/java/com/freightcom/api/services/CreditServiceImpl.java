package com.freightcom.api.services;

import java.math.BigDecimal;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.ConfigurablePropertyAccessor;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.freightcom.api.controllers.transfer.CreditRefund;
import com.freightcom.api.model.Credit;
import com.freightcom.api.model.Customer;
import com.freightcom.api.model.PaymentTransaction;
import com.freightcom.api.model.UserRole;
import com.freightcom.api.model.views.CreditView;
import com.freightcom.api.repositories.ObjectBase;
import com.freightcom.api.repositories.custom.CreditRepository;
import com.freightcom.api.repositories.custom.CreditSpecification;
import com.freightcom.api.services.converters.CreditConverter;
import com.freightcom.api.services.dataobjects.CreditPayment;

/**
 * @author bryan
 *
 */
@Component
public class CreditServiceImpl implements CreditService
{
    @SuppressWarnings("unused")
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final CreditRepository creditRepository;

    private final PagedResourcesAssembler<Credit> pagedAssembler;
    private final PagedResourcesAssembler<CreditView> creditAssembler;
    private final PermissionChecker permissionChecker;
    private final ObjectBase objectBase;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public CreditServiceImpl(final CreditRepository creditRepository,
            final PagedResourcesAssembler<Credit> pagedAssembler,
            final PagedResourcesAssembler<CreditView> creditAssembler, final PermissionChecker permissionChecker,
            final ObjectBase objectBase)
    {
        this.creditRepository = creditRepository;
        this.pagedAssembler = pagedAssembler;
        this.creditAssembler = creditAssembler;
        this.permissionChecker = permissionChecker;
        this.objectBase = objectBase;
    }

    @Override
    public PagedResources<Resource<Credit>> getCredits(Map<String, String> criteria, Pageable pageable)
    {
        Page<Credit> credits = creditRepository.findAll(new CreditSpecification(criteria), pageable);

        return pagedAssembler.toResource(credits);
    }

    @Override
    public PagedResources<Resource<CreditView>> getCreditsConverted(Map<String, String> criteria, Pageable pageable)
    {
        Page<CreditView> credits = creditRepository.findAll(new CreditSpecification(criteria), pageable)
                .map(new CreditConverter());

        return creditAssembler.toResource(credits, new Link("/customerCredit"));
    }

    @Override
    @Transactional
    public Credit createOrUpdateCredit(Credit credit) throws Exception
    {
        creditRepository.save(credit);

        return credit;
    }

    /**
     * @throws Exception
     *
     */
    @Override
    @Transactional
    public Credit createCredit(final Credit credit) throws Exception
    {
        Credit newCredit;

        if (credit.getId() != null) {
            newCredit = new Credit();
            BeanUtils.copyProperties(credit, newCredit);
        } else {
            newCredit = credit;
        }

        newCredit.setAmountRemaining(credit.getAmount());

        return createOrUpdateCredit(newCredit);
    }

    /**
     *
     */
    @Override
    @Transactional
    public Credit updateCredit(Long id, Credit credit, Map<String, String> attributes, UserDetails loggedInUser,
            UserRole userRole) throws Exception
    {
        Credit existing = creditRepository.findOne(id);

        if (existing == null) {
            throw new ResourceNotFoundException("No such credit");
        }

        ConfigurablePropertyAccessor source = PropertyAccessorFactory.forDirectFieldAccess(credit);
        ConfigurablePropertyAccessor dest = PropertyAccessorFactory.forDirectFieldAccess(existing);

        // Only copy attributes supplied in the JSON input, leave others alone
        for (String key : attributes.keySet()) {
            if (dest.isWritableProperty(key)) {
                dest.setPropertyValue(key, source.getPropertyValue(key));
            }
        }

        return createOrUpdateCredit(existing);
    }

    /**
     *
     */
    @Override
    @Transactional
    public String deleteCredit(Long creditId, UserDetails loggedInUser)
    {
        Credit credit = creditRepository.findOne(creditId);

        if (credit == null) {
            throw new ResourceNotFoundException("No such credit");
        }

        creditRepository.delete(credit);

        return "ok";
    }

    @Override
    public Credit addFromCard(CreditPayment payment) throws Exception
    {
        if (payment.getCustomerId() == null) {
            if (!permissionChecker.isCustomer()) {
                throw new AccessDeniedException("No customer for credit");
            }

            payment.setCustomerId(permissionChecker.getCustomerId());
        }

        Customer customer = objectBase.getCustomer(payment.getCustomerId());

        if (customer == null) {
            throw new ResourceNotFoundException("No customer");
        }


        if (permissionChecker.isCustomer()) {
            customer.updateCreditCards(payment);
        }

        PaymentTransaction transaction = new PaymentTransaction();
        Credit credit = new Credit();

        credit.setAmount(payment.getPayment());
        credit.setAmountRemaining(payment.getPayment());
        credit.setCustomerId(payment.getCustomerId());
        credit.setNote("Credit card payment");
        credit.setCurrency(customer.getInvoiceCurrency());
        credit.setCreatedByUserId(permissionChecker.getUserId());

        transaction.setAmount(payment.getPayment());
        transaction.setPaymentType("credit card");
        transaction.setCustomerId(payment.getCustomerId());
        transaction.setCreatedByUserId(permissionChecker.getUserId());
        transaction.setUserId(permissionChecker.getUserId());
        transaction.setCreditObject(credit);

        objectBase.save(credit);
        objectBase.save(transaction);

        return credit;
    }

    @Override
    public Credit refundCredit(CreditRefund creditRefund) throws Exception
    {
        Customer customer = objectBase.getCustomer(creditRefund.getCustomerId());

        Validation.get().test(customer != null, "customerId",
                                 "Customer may not be null").test(creditRefund.getAmount() != null && creditRefund.getAmount()
                .compareTo(BigDecimal.ZERO) > 0,
                                 "amount",
                                 "Amount must be greater than 0").test(creditRefund.getCcInfo() != null, "creditCard",
                                 "Credit card may not be null")
            .testNotEmpty(creditRefund.getCurrency(), "currency")
            .testNotEmpty(creditRefund.getNote(), "note")
            .throwIfFailed();


        Credit credit = new Credit();
        credit.setAmount(BigDecimal.ZERO.subtract(creditRefund.getAmount()));
        credit.setCurrency(creditRefund.getCurrency());
        credit.setNote("Refund to credit card - " + creditRefund.getNote());
        credit.setCustomerId(customer.getId());

        BigDecimal amountRemaining = creditRefund.getAmount();

        for (Credit positiveCredit: customer.getPositiveCredits(creditRefund.getCurrency())) {
            if (amountRemaining.compareTo(positiveCredit.getAmountRemaining()) >= 0) {
                positiveCredit.setAmountRemaining(BigDecimal.ZERO);
                amountRemaining = amountRemaining.subtract(positiveCredit.getAmountRemaining());
            } else {
                positiveCredit.setAmountRemaining(positiveCredit.getAmountRemaining().subtract(amountRemaining));
                amountRemaining = BigDecimal.ZERO;
                break;
            }
        }

        credit.setAmountRemaining(BigDecimal.ZERO.subtract(amountRemaining));

        objectBase.save(credit);

        return credit;
    }
}
