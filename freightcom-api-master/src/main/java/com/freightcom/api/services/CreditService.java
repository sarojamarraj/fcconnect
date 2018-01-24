package com.freightcom.api.services;

import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.freightcom.api.controllers.transfer.CreditRefund;
import com.freightcom.api.model.Credit;
import com.freightcom.api.model.UserRole;
import com.freightcom.api.model.views.CreditView;
import com.freightcom.api.services.dataobjects.CreditPayment;

/**
 * @author bryan
 *
 */
@Component
public interface CreditService
{

    Credit addFromCard(CreditPayment payment) throws Exception;

    PagedResources<Resource<Credit>> getCredits(Map<String, String> criteria, Pageable pageable);

    PagedResources<Resource<CreditView>> getCreditsConverted(Map<String, String> criteria, Pageable pageable);

    Credit createOrUpdateCredit(Credit credit) throws Exception;

    Credit createCredit(Credit credit) throws Exception;

    Credit updateCredit(Long id, Credit credit, Map<String, String> attributes, UserDetails loggedInUser,
            UserRole userRole) throws Exception;

    String deleteCredit(Long creditId, UserDetails loggedInUser);

    Credit refundCredit(CreditRefund creditRefund) throws Exception;

}
