package com.freightcom.api.services.orders;

import java.math.BigDecimal;
import java.util.Map;
import java.lang.NumberFormatException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;

import com.freightcom.api.events.ClaimFiledEvent;
import com.freightcom.api.events.ClaimUpdatedEvent;
import com.freightcom.api.model.Claim;
import com.freightcom.api.model.CustomerOrder;
import com.freightcom.api.model.LoggedEvent;
import com.freightcom.api.model.MessageAction;
import com.freightcom.api.model.UserRole;
import com.freightcom.api.model.Credit;
import com.freightcom.api.repositories.ObjectBase;
import com.freightcom.api.services.ServiceProvider;
import com.freightcom.api.services.ValidationException;
import com.freightcom.api.util.Empty;

public class ClaimActions
{
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final UserRole role;
    private final ApplicationEventPublisher publisher;
    private final ObjectBase objectBase;
    private final ServiceProvider provider;

    /**
     *
     */
    public ClaimActions(final UserRole role, final ApplicationEventPublisher publisher, final ServiceProvider provider)
    {
        this.role = role;
        this.publisher = publisher;
        this.provider = provider;
        this.objectBase = provider.getObjectBase();
    }

    /**
     * @throws ValidationException
     *
     */
    @Transactional
    public Claim submit(CustomerOrder order, Map<String, Object> attributes) throws ValidationException
    {
        if (Empty.check(attributes.get("comment"))) {
            throw ValidationException.get("comment", "Failed to submit claim - comment may not be empty");
        }

        if (Empty.check(attributes.get("currency"))) {
            throw ValidationException.get("currency", "Failed to submit claim - currency may not be empty");
        }

        if (Empty.check(attributes.get("reason"))) {
            throw ValidationException.get("reason", "Failed to submit claim - currency may not be empty");
        }

        if (!order.getClaimCanBeFiled()) {
            throw ValidationException.get("status", "Failed to submit claim - invalid order status");
        }

        if (order.getClaim() != null) {
            throw ValidationException.get("claim", "Failed to submit claim - there is already a claim for this order");
        }

        BigDecimal amount = BigDecimal.ZERO;

        if (!Empty.check(attributes.get("amount"))) {
            try {
                amount = new BigDecimal(attributes.get("amount")
                        .toString());
            } catch (Exception e) {
                throw ValidationException.get("claimAmount", "Failed to submit claim - invalid format for amount");
            }
        }

        UserRole submittingRole = role;

        if (role.isFreightcom()) {
            // Must submit on behalf of a user
            submittingRole = objectBase.lookupRole(attributes.get("userRoleId"));
        }

        if (submittingRole == null) {
            throw ValidationException.get("userRoleId", "Failed to submit claim - a submitting user must be specified");
        }

        LoggedEvent statusMessage = LoggedEvent.orderStatusMessage(submittingRole.getUser(), order, String
                .format("Claim filed for %s %.2f: %s", attributes.get("currency"), amount, attributes.get("comment")
                        .toString()),
                order.getOrderStatusName(), MessageAction.CLAIM);

        objectBase.save(statusMessage);

        Claim claim = order.submitClaim(submittingRole, amount, attributes.get("currency")
                .toString(),
                attributes.get("reason")
                        .toString());
        claim.setCreatedByRole(role);

        provider.publishEvent(new ClaimFiledEvent(order, role));
        log.debug("DONE SUBMITTING CLAIM " + claim);

        return claim;
    }

    /**
     * @throws ValidationException
     *
     */
    @Transactional
    public Object update(CustomerOrder order, Map<String, Object> attributes) throws ValidationException
    {
        Claim claim = order.getClaim();

        if (Empty.check(attributes.get("comment"))) {
            throw ValidationException.get("comment", "Failed to submit claim - comment may not be empty");
        }

        if (Empty.check(attributes.get("status"))) {
            throw ValidationException.get("status", "Failed to submit claim - status may not be empty");
        }

        if (!Empty.check(attributes.get("amount"))) {
            try {
                claim.setAmount(new BigDecimal(attributes.get("amount")
                        .toString()));
            } catch (NumberFormatException e) {
                throw ValidationException.get("claimAmount", "Failed to submit claim - invalid format for amount");
            }
        }

        Claim.Status status = Claim.Status.fromString(attributes.get("status")
                .toString());

        if (status == null) {
            throw ValidationException.get("status", "Invalid order claim status '" + attributes.get("status") + "'");
        }

        String comment = attributes.get("comment")
                .toString();
        String updateMessage = "";

        if (status == Claim.Status.SETTLED) {
            try {
                BigDecimal creditAmount = new BigDecimal(attributes.get("creditAmount")
                        .toString());

                Credit credit = creditClaim(claim, order, comment, creditAmount);

                updateMessage = String.format("Credit #%d for %s $ %.2f has been created. %s",
                        credit.getId(), claim.getCurrency(), creditAmount, comment);
            } catch (NumberFormatException e) {
                throw ValidationException.get("creditAmount", "Bad format for credit amount");
            }
        } else {
            updateMessage = comment;
        }

        LoggedEvent statusMessage = LoggedEvent.orderStatusMessage(role.getUser(), order, updateMessage,
                order.getOrderStatusName(), MessageAction.valueOf(status.getValue()));

        objectBase.save(statusMessage);

        publisher.publishEvent(new ClaimUpdatedEvent(order, status, statusMessage, role));

        claim.setStatus(status);

        log.debug("DONE UPDATING CLAIM");

        return claim;
    }

    /**
     * Add a credit for a settled claim if a credit amount is submitted
     *
     * @throws ValidationException
     */
    private Credit creditClaim(Claim claim, CustomerOrder order, String comment, BigDecimal creditAmount)
            throws ValidationException
    {
        Credit credit = new Credit();
        credit.setCustomerId(order.getCustomer()
                .getId());
        credit.setNote("Credit for order #" + order.getId());
        credit.setAmount(creditAmount);
        credit.setAmountRemaining(creditAmount);
        credit.setCreatedByUserId(role.getUser()
                .getId());
        credit.setCurrency(claim.getCurrency());

        objectBase.save(credit);

        return credit;
    }

}
