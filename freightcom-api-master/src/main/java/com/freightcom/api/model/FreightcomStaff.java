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

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Cacheable;
import javax.persistence.Column;

/**
 *
 *
 * @author
 */
@Entity(name = "FreightcomStaff")
@Table(name = "freightcom_staff")
@DiscriminatorValue("FREIGHTCOM_STAFF")
@Cacheable(value = true)
public class FreightcomStaff extends UserRole
{

    private static final long serialVersionUID = 1L;

    @Column(name = "can_manage_disputes")
    private Boolean canManageDisputes = Boolean.FALSE;

    @Column(name = "can_manage_claims")
    private Boolean canManageClaims = Boolean.FALSE;

    @Column(name = "can_generate_invoices")
    private Boolean canGenerateInvoices = Boolean.FALSE;

    @Column(name = "can_enter_payments")
    private Boolean canEnterPayments = Boolean.FALSE;

    public FreightcomStaff()
    {
        super();
    }

    @Override
    public boolean isFreightcomStaff()
    {
        return true;
    }

    @Override
    public String getRoleName()
    {
        return ROLE_FREIGHTCOM_STAFF;
    }

    @Override
    public String getPrettyRoleName()
    {
        return "Staff";
    }

    @Override
    public String toString()
    {
        return getId() + ": " + getRoleName() + " " + getUserId();
    }

    @Override
    public boolean canManageClaims()
    {
        return getCanManageClaims();
    }

    @Override
    public boolean canManageDisputes()
    {
        return getCanManageDisputes();
    }

    public Boolean getCanManageDisputes()
    {
        return canManageDisputes;
    }

    public void setCanManageDisputes(Boolean canManageDisputes)
    {
        this.canManageDisputes = canManageDisputes;
    }

    public Boolean getCanManageClaims()
    {
        return canManageClaims;
    }

    public void setCanManageClaims(Boolean canManageClaims)
    {
        this.canManageClaims = canManageClaims;
    }

    @Override
    public boolean canViewCost()
    {
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public  <T extends UserRole> T  updateFrom(T role) throws Exception
    {
        if (! (role instanceof FreightcomStaff)) {
            throw new Exception("Cannot update");
        }

        canManageClaims = ((FreightcomStaff) role).getCanManageClaims();
        canManageDisputes = ((FreightcomStaff) role).getCanManageDisputes();
        canEnterPayments = ((FreightcomStaff) role).getCanEnterPayments();
        canGenerateInvoices = ((FreightcomStaff) role).getCanGenerateInvoices();

        return (T) this;
    }

    @Override
    public Boolean canEnterPayments()
    {
        return canEnterPayments != null && canEnterPayments;
    }

    @Override
    public Boolean canGenerateInvoices()
    {
        return canGenerateInvoices != null && canGenerateInvoices;
    }

    public Boolean getCanGenerateInvoices()
    {
        return canGenerateInvoices;
    }

    public void setCanGenerateInvoices(Boolean canGenerateInvoices)
    {
        this.canGenerateInvoices = canGenerateInvoices;
    }

    public Boolean getCanEnterPayments()
    {
        return canEnterPayments;
    }

    public void setCanEnterPayments(Boolean canEnterPayments)
    {
        this.canEnterPayments = canEnterPayments;
    }

    @Override
    public boolean canManageCredits()
    {
        return true;
    }

}
