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
package com.freightcom.api.repositories;

import java.util.List;

import javax.persistence.LockModeType;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.freightcom.api.model.CustomerOrder;
import com.freightcom.api.repositories.custom.OrderSpecification;

@RepositoryRestResource(exported=false)
public interface CustomerOrderRepository extends PagingAndSortingRepository<CustomerOrder, Long>, JpaSpecificationExecutor<CustomerOrder>  {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    CustomerOrder findOne(Long id);

    Page<CustomerOrder> findByCustomerId(
            @Param("id") Long id, Pageable pageable);

    List<CustomerOrder> findAllBy(OrderSpecification specification,
                                  Pageable pageable);
}
