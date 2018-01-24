package com.freightcom.api.services.orders;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.freightcom.api.Messages;
import com.freightcom.api.SlackService;
import com.freightcom.api.events.OrderStatusChange;
import com.freightcom.api.model.CustomerOrder;
import com.freightcom.api.model.LoggedEvent;
import com.freightcom.api.model.User;
import com.freightcom.api.services.MailService;
import com.freightcom.api.util.MapBuilder;

public class StatusNotifier
{
    private final LoggedEvent loggedEvent;
    private final MailService mailService;
    private final OrderStatusChange event;
    private final CustomerOrder order;
    private final String emailFromAddress;
    private final String orderLink;
    private final Messages messages;
    private final SlackService slackService;

    public StatusNotifier(final CustomerOrder order, final OrderStatusChange event, final LoggedEvent loggedEvent,
            final String orderLink, final Messages messages, final MailService mailService,
            final String emailFromAddress, final SlackService slackService)
    {
        this.order = order;
        this.event = event;
        this.orderLink = orderLink;
        this.mailService = mailService;
        this.loggedEvent = loggedEvent;
        this.emailFromAddress = emailFromAddress;
        this.messages = messages;
        this.slackService = slackService;
    }

    public void notifyIfRequired() throws Exception
    {
        List<User> users = new ArrayList<User>();

        users.addAll(order.shipToNotifyUsers());
        users.addAll(order.shipFromNotifyUsers());

        MapBuilder templateVariables = MapBuilder.getNew()
                .put("orderId", order.getId())
                .put("link", orderLink)
                .put("status", loggedEvent.getMessage())
                .put("message", loggedEvent.getComment());

        for (User user : users) {
            if (user != null) {
                templateVariables.put("name", user.fullName());

                mailService.send(messages.get("freightcom.status-change.email.subject", new Object[] { order.getId() }),
                        user.getEmail(), "mail/html/order-status-change.html", templateVariables.toMap(),
                        emailFromAddress);
            }
        }

        instantMessagingNotifyStatusChange(order, event);
    }

    public void instantMessagingNotifyStatusChange(final CustomerOrder order, final OrderStatusChange event) throws IOException
    {
        if (order.isBooked()) {
            slackService.send("New Order " + order);
        }
    }
}
