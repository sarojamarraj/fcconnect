package com.freightcom.api.model.views;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.freightcom.api.model.CustomerOrder;


public abstract class CommissionCommon extends BaseView
{
    // bol, customer name, ship date and amount.
    protected class OrderView
    {
        private final CustomerOrder order;

        public OrderView(final CustomerOrder order)
        {
            this.order = order;
        }

        public Object getId()
        {
            return order.getId();
        }

        public Object getOrderStatus()
        {
            if (order.getOrderStatus() != null) {
                return order.getOrderStatus().getName();
            } else {
                return null;
            }
        }

        public Object getBolId()
        {
            return order.getBolId();
        }

        public Object getCustomerName()
        {
            if (order.getCustomer() == null) {
                return null;
            } else {
                return order.getCustomer().getName();
            }
        }

        @JsonFormat(pattern = "yyyy-MM-dd")
        public Object getShipDate()
        {
            return order.getShipDate();
        }
    }
}
