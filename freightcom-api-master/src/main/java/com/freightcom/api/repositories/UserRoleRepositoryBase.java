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

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.freightcom.api.model.UserRole;

@RepositoryRestResource(exported=false)
@CrossOrigin(maxAge = 3600)
public interface UserRoleRepositoryBase extends PagingAndSortingRepository<UserRole, Long> {
    List<UserRole> findByUser_Id(@Param("userId") Long userId);
    UserRole findById(@Param("id") Long id);
 }
