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

import com.freightcom.api.model.LoggedEvent;

@RepositoryRestResource()
public interface LoggedEventRepositoryBase extends PagingAndSortingRepository<LoggedEvent, Long>
{
    Page<LoggedEvent> findByEntityIdAndEntityType(@Param("entity_id") String entity_id, @Param("type") String type,
            Pageable pageable);

    Page<LoggedEvent> findByEntityIdAndEntityTypeAndMessageType(@Param("entity_id") String entity_id, @Param("type") String type,
                                                                @Param("message_type") String message_type, Pageable pageable);

}
