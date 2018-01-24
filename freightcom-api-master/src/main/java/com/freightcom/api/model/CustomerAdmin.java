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
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Cacheable;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Formula;

/**
 *
 *
 * @author
 */
@Entity(name = "CustomerAdmin")
@Table(name = "customer_admin")
@DiscriminatorValue("CUSTOMER_ADMIN")
@Cacheable(value = true)
public class CustomerAdmin extends UserRole
{

    private static final long serialVersionUID = 1L;

    @Column(name = "customer_id")
    @NotNull
    private Long customerId;

    @Formula("(select C.name from customer C where C.id=customer_id)")
    private String customerName;

    public CustomerAdmin()
    {
        super();
    }

    @Override
    public Long getCustomerId()
    {
        return customerId;
    }

    @Override
    public boolean sameCustomer(Customer customer)
    {
        return customerId != null && customer != null
            && customerId.equals(customer.getId());
    }

    @Override
    public boolean sameCustomer(UserRole role)
    {
        return customerId != null && role != null
            && customerId.equals(role.getCustomerId());
    }

    public void setCustomerId(Long customerId)
    {
        this.customerId = customerId;
    }

    @Override
    public String getRoleName()
    {
        return ROLE_CUSTOMER_ADMIN;
    }

    @Override
    public String getPrettyRoleName()
    {
        return "Admin - " + getCustomerName();
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
    public boolean isCustomerAdmin()
    {
        return true;
    }
}
