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

/**
 *
 *
 * @author
 */


public class GroupedChargeCommission
{
    private Long accessorialId;

    private String description;

    private BigDecimal total;

    private BigDecimal totalTax;

    private BigDecimal cost;

    private BigDecimal commission;

    public GroupedChargeCommission(Long accessorialId,
                                   String description,
                                   BigDecimal total,
                                   BigDecimal totalTax,
                                   BigDecimal cost,
                                   BigDecimal commission)
    {
     this.accessorialId = accessorialId;
     this.description = description;
     this.total = total;
     this.totalTax = totalTax;
     this.cost = cost;
     this.commission = commission;
    }

    public Long getAccessorialId()
    {
        return accessorialId;
    }

    public void setAccessorialId(Long accessorialId)
    {
        this.accessorialId = accessorialId;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public BigDecimal getTotalTax()
    {
        return totalTax;
    }

    public void setTotalTax(BigDecimal totalTax)
    {
        this.totalTax = totalTax;
    }

    public BigDecimal getCost()
    {
        return cost;
    }

    public void setCost(BigDecimal cost)
    {
        this.cost = cost;
    }

    public BigDecimal getTotal()
    {
        return total;
    }

    public void setTotal(BigDecimal total)
    {
        this.total = total;
    }

    public BigDecimal getCommission()
    {
        return commission;
    }

    public void setCommission(BigDecimal commission)
    {
        this.commission = commission;
    }
}
