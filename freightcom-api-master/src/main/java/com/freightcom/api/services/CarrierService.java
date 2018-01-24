package com.freightcom.api.services;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;

import com.freightcom.api.model.CarrierInvoice;
import com.freightcom.api.model.Service;
import com.freightcom.api.model.UserRole;
import com.freightcom.api.model.views.View;

/**
 * @author bryan
 *
 */

public interface CarrierService
{

    Page<CarrierInvoice> listInvoices(Long serviceId, Map<String, String> criteria, Pageable pageable);

    Page<CarrierInvoice> listAllInvoices(Map<String, String> criteria, Pageable pageable);

    String deleteService(Long serviceId, UserDetails loggedInUser);

    Object uploadInvoice(Long serviceId, MultipartFile file) throws Exception;

    Page<Service> getServices(Map<String, String> criteria, Pageable pageable);

    Page<View> getServicesConverted(Map<String, String> criteria, Pageable pageable);

    Service createService(Service service) throws Exception;

    Service updateService(Long id, Service service, Map<String, String> attributes, UserDetails loggedInUser,
            UserRole userRole) throws Exception;

    Object downloadInvoice(Long serviceId, Long carrierInvoiceId) throws Exception;

    Service getService(Long serviceId) throws Exception;

    Object getDropdownService();

}
