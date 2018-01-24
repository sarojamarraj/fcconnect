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
package com.freightcom.api.repositories.custom;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;

import com.freightcom.api.model.Customer;
import com.freightcom.api.repositories.CustomerRepositoryBase;

public interface CustomerRepository extends CustomerRepositoryBase, JpaSpecificationExecutor<Customer>
{

    List<Customer> findBySalesAgent_Id(@Param("subAgentId") Long subAgentId);

}
