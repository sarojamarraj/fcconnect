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

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;

import com.freightcom.api.model.OrderRateQuote;
import com.freightcom.api.repositories.OrderRateQuoteRepositoryBase;

public interface OrderRateQuoteRepository extends OrderRateQuoteRepositoryBase, JpaSpecificationExecutor<OrderRateQuote>
{

    Page<OrderRateQuote> findByOrderId(
            @Param("orderId") Long orderId, Pageable pageable);

    List<OrderRateQuote> findByOrderId(Long id);

}
