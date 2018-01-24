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

/**
 *
 *
 * @author
 */
@Entity(name = "SuperAdmin")
@Table(name = "super_admin")
@DiscriminatorValue("ROLE_ADMIN")
@Cacheable(value = true)
public class SuperAdmin extends UserRole
{

    private static final long serialVersionUID = 1L;

    public SuperAdmin()
    {
        super();
    }

    @Override
    public boolean isSuperAdmin()
    {
        return true;
    }

    @Override
    public boolean isAdmin()
    {
        return true;
    }

    @Override
    public boolean isFreightcomStaff()
    {
        return true;
    }

    @Override
    public String getRoleName()
    {
        return ROLE_SUPER_ADMIN;
    }

    @Override
    public boolean canManageClaims()
    {
        return true;
    }

    @Override
    public boolean canManageDisputes()
    {
        return true;
    }

    @Override
    public Boolean canEnterPayments()
    {
        return true;
    }

    @Override
    public Boolean canGenerateInvoices()
    {
        return true;
    }

    @Override
    public String toString()
    {
        return getId() + ": " + getRoleName() + " " + getUserId();
    }

    @Override
    public String getPrettyRoleName()
    {
        return "Super Admin";
    }

}
