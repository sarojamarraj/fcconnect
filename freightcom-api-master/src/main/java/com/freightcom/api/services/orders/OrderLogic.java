package com.freightcom.api.services.orders;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.freightcom.api.model.AddressBook;
import com.freightcom.api.model.Charge;
import com.freightcom.api.model.Customer;
import com.freightcom.api.model.CustomerOrder;
import com.freightcom.api.model.LoggedEvent;
import com.freightcom.api.model.ShippingAddress;
import com.freightcom.api.services.ServiceProvider;

public class OrderLogic
{
    @SuppressWarnings("unused")
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final CustomerOrder order;
    private final ServiceProvider provider;

    private List<Charge> disputedCharges = null;

    /**
     *
     */
    public static OrderLogic get(final CustomerOrder order, final ServiceProvider provider)
    {
        return new OrderLogic(order, provider);
    }

    /**
     *
     */
    private OrderLogic(final CustomerOrder order, final ServiceProvider provider)
    {
        this.order = order;
        this.provider = provider;
    }

    public Boolean getCustomsSurveyFormRequired()
    {
        return order.isTransborder();
    }

    public String getLatestComment()
    {
        LoggedEvent latestEvent = provider.getObjectBase()
                .getLatestLoggedEvent(order, false);

        return latestEvent == null ? null : latestEvent.getComment();
    }

    public Long getDisputeAgeInDays()
    {
        Charge earliestDispute = getDisputedCharges().min((a, b) -> a.getDisputedAt()
                .compareTo(b.getDisputedAt()))
                .orElse(null);

        return earliestDispute == null ? 0L
                : Duration.between(earliestDispute.getDisputedAt(), ZonedDateTime.now(ZoneId.of("UTC")))
                        .toDays();
    }

    public BigDecimal getDisputeAmount()
    {
        return getDisputedCharges().map(charge -> charge.getCost())
                .reduce(BigDecimal.ZERO, (a, b) -> a.add(b));
    }

    public String getDisputeLatestComment()
    {
        // TODO dispute

        LoggedEvent latestEvent = provider.getObjectBase()
                .getLatestLoggedEvent(order, false);

        return latestEvent == null ? null : latestEvent.getComment();
    }

    public Stream<Charge> getDisputedCharges()
    {
        if (disputedCharges == null) {
            disputedCharges = order.getCharges();
        }

        return disputedCharges.stream()
                .filter(charge -> charge.isDisputed() && charge.getDisputeResolvedAt() == null);
    }

    public void possiblySaveToAddressBook()
    {
        possiblySaveToAddressBook(order.getShipTo(), order.getCustomer());
        possiblySaveToAddressBook(order.getShipFrom(), order.getCustomer());
    }

    private void possiblySaveToAddressBook(ShippingAddress address, Customer customer)
    {
        if (address != null && address.getSaveToAddressBook()) {
           AddressBook newEntry = provider.getObjectBase().save(new AddressBook(address, customer));
           address.setAddressBookId(newEntry.getId());
        }
    }
}
