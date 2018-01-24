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

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.security.access.prepost.PreAuthorize;

import com.freightcom.api.model.AppliedCredit;

@PreAuthorize("hasAuthority('ADMIN')")
@RepositoryRestResource(collectionResourceRel = "appliedcredit", path = "appliedcredit")
@CrossOrigin(maxAge = 3600)
public interface AppliedCreditRepository extends PagingAndSortingRepository<AppliedCredit, Long>
{
    Page<AppliedCredit> findByInvoiceId(@Param("invoice_id") Long invoice_id, Pageable pageable);
    Page<AppliedCredit> findByCreditId(@Param("credit_id") Long credit_id, Pageable pageable);
}
