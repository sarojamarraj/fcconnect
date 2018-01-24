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

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.freightcom.api.model.LoggedEvent;
import com.freightcom.api.repositories.LoggedEventRepositoryBase;

public interface LoggedEventRepository extends LoggedEventRepositoryBase, JpaSpecificationExecutor<LoggedEvent>
{


}
