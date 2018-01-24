package com.freightcom.api.services.converters;

import java.util.Map;

import org.springframework.core.convert.converter.Converter;

import com.freightcom.api.model.CustomerOrder;
import com.freightcom.api.model.views.ClaimOrderView;
import com.freightcom.api.model.views.DisputedOrderView;
import com.freightcom.api.model.views.JobBoardView;
import com.freightcom.api.model.views.SubmittedOrderView;
import com.freightcom.api.model.views.View;
import com.freightcom.api.repositories.custom.OrderSpecification;
import com.freightcom.api.services.ServiceProvider;


public class OrderListConverter implements Converter<CustomerOrder, View>
{
    private final Map<String,Object> criteria;
    private final ServiceProvider provider;

    public OrderListConverter(final Map<String,Object> criteria,
                              final ServiceProvider provider)
    {
        this.criteria = criteria;
        this.provider = provider;
    }

    @Override
    public View convert(CustomerOrder order)
    {
        Object mode = criteria.get("mode");

        return mode == OrderSpecification.Mode.JOB_BOARD ? new JobBoardView(order, criteria, provider)
            : (mode == OrderSpecification.Mode.DISPUTED ? new DisputedOrderView(order, criteria, provider)
               : (mode == OrderSpecification.Mode.HAS_CLAIM ? new ClaimOrderView(order, criteria, provider)
                  : new SubmittedOrderView(order, criteria, provider)));
    }

}
