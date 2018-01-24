package com.freightcom.api.services.orders;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;

import com.freightcom.api.events.ChargeDisputed;
import com.freightcom.api.events.ChargeDisputeResolved;
import com.freightcom.api.events.ChargeDisputeUpdate;
import com.freightcom.api.model.Charge;
import com.freightcom.api.model.CustomerOrder;
import com.freightcom.api.model.DisputeInformation;
import com.freightcom.api.model.LoggedEvent;
import com.freightcom.api.model.MessageAction;
import com.freightcom.api.model.UserRole;
import com.freightcom.api.repositories.ObjectBase;
import com.freightcom.api.services.ValidationException;
import com.freightcom.api.util.Empty;
import com.freightcom.api.util.YesValue;

public class Dispute
{
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private static String DISPUTE_COMMENT = "Disputed";
    private static String DISPUTE_RESPONSE = "Dispute Response";

    private final UserRole role;
    private final ApplicationEventPublisher publisher;
    private final ObjectBase objectBase;

    /**
     *
     */
    public Dispute(final UserRole role, final ApplicationEventPublisher publisher, final ObjectBase objectBase)
    {
        this.role = role;
        this.publisher = publisher;
        this.objectBase = objectBase;
    }

    /**
     * @throws ValidationException
     *
     */
    @Transactional
    public DisputeInformation disputeCharge(Charge charge, Map<String, Object> attributes) throws ValidationException
    {
        if (Empty.check(attributes.get("comment"))) {
            throw ValidationException.get("comment", "Comment may not be empty");
        }

        log.debug("DISPUTING CHARGE " + charge + " " + charge.getDisputedAt());

        if (charge.getDisputedAt() == null) {
            charge.dispute();
        }

        DisputeInformation dispute = charge.getOrder()
                .getDisputeInformation();

        if (dispute == null) {
            dispute = new DisputeInformation();
            dispute.setUser(role.getUser());
            dispute.setStatus(DisputeInformation.Status.OPEN);
            objectBase.save(dispute);
            charge.getOrder()
                    .setDisputeInformation(dispute);
        } else {
            dispute.setStatus(DisputeInformation.Status.REOPENED);
        }

        LoggedEvent statusMessage = LoggedEvent.orderStatusMessage(role.getUser(), charge.getOrder(),
                attributes.get("comment")
                        .toString(),
                DISPUTE_COMMENT, MessageAction.DISPUTE);

        objectBase.save(statusMessage);

        charge.addDisputeComment(statusMessage);

        publisher.publishEvent(new ChargeDisputed(charge, statusMessage, role));

        log.debug("DONE DISPUTING CHARGE " + charge + " " + charge.getDisputedAt() + " " + statusMessage);

        return dispute;
    }

    /**
     * @throws ValidationException
     *
     */
    @Transactional
    public DisputeInformation respondToDispute(CustomerOrder order, Map<String, Object> attributes) throws ValidationException
    {
        if (Empty.check(attributes.get("comment"))) {
            throw ValidationException.get("comment", "Comment may not be empty");
        }

        boolean resolveDispute = attributes.get("resolveDispute") == null ? false
                : YesValue.parseStrict(attributes.get("resolveDispute"));

        LoggedEvent statusMessage = LoggedEvent.orderStatusMessage(role.getUser(), order, attributes.get("comment")
                .toString(), DISPUTE_RESPONSE, resolveDispute ? MessageAction.DISPUTE_RESOLVED : MessageAction.DISPUTE);

        objectBase.save(statusMessage);

        if (resolveDispute && order.getDisputeInformation() != null) {
            order.getDisputeInformation()
                    .setStatus(DisputeInformation.Status.RESOLVED);
        }

        for (Charge charge : order.getCharges()) {
            if (charge.isDisputed()) {
                charge.addDisputeComment(statusMessage);

                if (resolveDispute) {
                    charge.resolveDispute();
                }
            }
        }

        if (resolveDispute) {
            publisher.publishEvent(new ChargeDisputeResolved(order, statusMessage, role));
        } else {
            publisher.publishEvent(new ChargeDisputeUpdate(order, statusMessage, role));
        }

        return order.getDisputeInformation();
    }

}
