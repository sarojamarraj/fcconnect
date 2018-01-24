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
package com.freightcom.api.model;


import java.math.BigDecimal;
import java.time.ZonedDateTime;

public interface PayableEntity
{
    boolean isPaid();
    void markPaid(Integer chequeNumber, ZonedDateTime transactionDate);
    String getName();
    ZonedDateTime getPaidAt();
    String getChequeNumber();
    BigDecimal getPaidAmount();
}
