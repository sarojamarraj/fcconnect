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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 *
 * @author
 */
@Entity(name = "UpsAccChargesUs")
@Table(name = "ups_acc_charges_us")
public class UpsAccChargesUs extends TransactionalEntity
{

    private static final long serialVersionUID = 1L;

    @Column(name = "franchise_id")
    private Long franchiseId;

    @Id
    @Column(name = "charge_name")
    private String chargeName;

    @Column(name = "cost")
    private Float cost;

    @Column(name = "charge")
    private Float charge;

    @Column(name = "per_package")
    private Boolean perPackage;

    @Column(name = "max_charge")
    private Float maxCharge;

    public UpsAccChargesUs()
    {
        super();
    }

    public Long getFranchiseId()
    {
        return franchiseId;
    }

    public void setFranchiseId(Long franchiseId)
    {
        this.franchiseId = franchiseId;
    }

    public String getChargeName()
    {
        return chargeName;
    }

    public void setChargeName(String chargeName)
    {
        this.chargeName = chargeName;
    }

    public Float getCost()
    {
        return cost;
    }

    public void setCost(Float cost)
    {
        this.cost = cost;
    }

    public Float getCharge()
    {
        return charge;
    }

    public void setCharge(Float charge)
    {
        this.charge = charge;
    }

    public Boolean getPerPackage()
    {
        return perPackage;
    }

    public void setPerPackage(Boolean perPackage)
    {
        this.perPackage = perPackage;
    }

    public Float getMaxCharge()
    {
        return maxCharge;
    }

    public void setMaxCharge(Float maxCharge)
    {
        this.maxCharge = maxCharge;
    }

    @Override
    public Long getId()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setId(Long id)
    {
        // TODO Auto-generated method stub

    }

}