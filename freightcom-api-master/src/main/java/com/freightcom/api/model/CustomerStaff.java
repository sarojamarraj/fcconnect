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

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Formula;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Cacheable;

/**
 *
 *
 * @author
 */
@Entity(name = "CustomerStaff")
@Table(name = "customer_staff")
@DiscriminatorValue("CUSTOMER_STAFF")
@Cacheable(value = true)
public class CustomerStaff extends UserRole
{

    private static final long serialVersionUID = 1L;

    @Column(name = "perm")
    private String perm;

    @Column(name = "customer_id")
    @NotNull
    private Long staffCustomerId;

    @Formula("(select C.name from customer C where C.id=customer_id)")
    private String customerName;

    public CustomerStaff()
    {
        super();
    }

    public String getPerm()
    {
        return perm;
    }

    public void setPerm(String perm)
    {
        this.perm = perm;
    }

    @Override
    public String getRoleName()
    {
        return ROLE_CUSTOMER_STAFF;
    }

    @Override
    public String getPrettyRoleName()
    {
        return "Staff - " + getCustomerName();
    }

    public Long getStaffCustomerId()
    {
        return staffCustomerId;
    }

    @Override
    public Long getCustomerId()
    {
        return staffCustomerId;
    }

    public void setCustomerId(Long customerId)
    {
        this.staffCustomerId = customerId;
    }

    @Override
    public boolean sameCustomer(Customer customer)
    {
        return staffCustomerId != null && customer != null
            && staffCustomerId.equals(customer.getId());
    }

    @Override
    public boolean sameCustomer(UserRole role)
    {
        return staffCustomerId != null && role != null
            && staffCustomerId.equals(role.getCustomerId());
    }

    @Override
    public String getCustomerName()
    {
        return customerName;
    }

    @Override
    public boolean isCustomer()
    {
        return true;
    }

    @Override
    public boolean isCustomerStaff()
    {
        return true;
    }

    public String toString()
    {
        return "UR-C " + getId() + " " + getRoleName();
    }

}
