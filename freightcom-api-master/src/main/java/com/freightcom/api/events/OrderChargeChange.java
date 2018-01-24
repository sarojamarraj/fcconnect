package com.freightcom.api.events;

import java.math.BigDecimal;

import com.freightcom.api.model.Charge;
import com.freightcom.api.model.CustomerOrder;
import com.freightcom.api.model.User;
import com.freightcom.api.model.UserRole;

public class OrderChargeChange extends OrderEvent
{
    private final CustomerOrder order;
    private final UserRole loggedInRole;
    private final Charge charge;
    private final Charge previous;
    private final boolean notifyCustomer;

    public OrderChargeChange(final CustomerOrder order,
                             final Charge previous,
                             final Charge charge,
                             final boolean notifyCustomer,
                             final UserRole loggedInRole)
    {
        this.order = order;
        this.loggedInRole = loggedInRole;
        this.charge = charge;
        this.previous = previous;
        this.notifyCustomer = notifyCustomer;
    }

    public boolean getNotiyCustomer()
    {
        return notifyCustomer;
    }

    public UserRole getLoggedInRole()
    {
        return loggedInRole;
    }

    @Override
    public User getUser()
    {
        return loggedInRole.getUser();
    }

    public CustomerOrder getOrder()
    {
        return order;
    }

    public Charge getCharge()
    {
        return charge;
    }

    public Charge getPrevious()
    {
        return previous;
    }

    public BigDecimal getFromCharge()
    {
        return previous.getCharge();
    }

    public BigDecimal getFromCost()
    {
        return previous.getCost();
    }

    public String getMessage()
    {
        String message;

        if (previous == null) {
            message = "Charge added";
        } else if (charge == null) {
            message = "Charge deleted";
        } else {
            message = "Charge modified";
        }

        return message;
    }

    public String getComment()
    {
        String comment;

        if (previous == null) {
            comment = String.format("Charge %s $%.2f/$%.2f added",
                                    charge.getName(),
                                    charge.getCharge(),
                                    charge.getCost());
        } else if (charge == null) {
            comment = String.format("Charge %s $%.2f/$%.2f deleted",
                                    previous.getName(),
                                    previous.getCharge(),
                                    previous.getCost());
        } else {
            comment = String.format("Charge %s was modified to $%.2f/$%.2f from $%.2f/$%.2f",
                                    charge.getName(),
                                    charge.getCharge(),
                                    charge.getCost(),
                                    previous.getCharge(),
                                    previous.getCost());
        }

        return comment;
    }

    public String getCustomerComment()
    {
        String comment;

        if (previous == null) {
            comment = String.format("Charge %s $%.2f added",
                                    charge.getName(),
                                    charge.getCharge());
        } else if (charge == null) {
            comment = String.format("Charge %s $%.2f deleted",
                                    previous.getName(),
                                    previous.getCharge());
        } else {
            comment = String.format("Charge %s was modified to $%.2f from $%.2f",
                                    charge.getName(),
                                    charge.getCharge(),
                                    previous.getCharge());
        }

        return comment;
    }

    public String toString()
    {
        return "Order charge change";
    }
}
