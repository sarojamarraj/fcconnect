/******************************************************************
              WebPal Product Suite Framework Libraries
-------------------------------------------------------------------
(c) 2002-present: all copyrights are with Palomino System Innovations Inc.
(Palomino Inc.) of Toronto, Canada

Unauthorized reproduction, licensing or disclosure of this source
code will be prosecuted. WebPal is a registered trademark of
Palomino System Innovations Inc. To report misuse please contact
info@palominosys.com or call +1 416 964 7333.
*******************************************************************/
package com.freightcom.api.model.support;

public enum OrderStatusCode
{
    DRAFT(16L, "DRAFT"),
    QUOTED(10L, "QUOTED"),
    BOOKED(17L, "BOOKED"),
    SCHEDULED(18L, "SCHEDULED"),
    READY_FOR_SHIPPING(1L, "READY FOR SHIPPING"),
    IN_TRANSIT(2L, "IN TRANSIT"),
    DELIVERED(3L, "DELIVERED"),
    CANCELLED(4L, "CANCELLED"),
    PREDISPATCHED(6L, "PREDISPATCHED"),
    READY_FOR_DISPATCH(8L, "READY FOR DISPATCH"),
    MISSED_PICKUP(9L, "MISSED PICKUP"),
    WAITING_BORDER(19L, "WAITING BORDER"),
    EXCEPTION(5L, "EXCEPTION"),
    DELETED(999L, "DELETED");

    private Long value;
    private String label;

    private OrderStatusCode(Long value, String label)
    {
        this.value = value;
        this.label = label;
    }

    public String getLabel()
    {
        return label;
    }

    public Long getValue()
    {
        return value;
    }

    public static OrderStatusCode get(String label) throws Exception
    {
        for (OrderStatusCode status: OrderStatusCode.values()) {
            if (status.getLabel().equals(label)) {
                return status;
            }
        }

        throw new Exception("BAD VALUE");
    }

    public static OrderStatusCode get(long value) throws Exception
    {
        for (OrderStatusCode status: OrderStatusCode.values()) {
            if (status.getValue() == value) {
                return status;
            }
        }

        throw new Exception("BAD VALUE");
    }

    public String toString()
    {
        return label;
    }
}
