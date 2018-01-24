package com.freightcom.api.services;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;

import com.freightcom.api.model.Charge;
import com.freightcom.api.model.Claim;
import com.freightcom.api.model.CustomerOrder;
import com.freightcom.api.model.CustomsInvoice;
import com.freightcom.api.model.DisputeInformation;
import com.freightcom.api.model.LoggedEvent;
import com.freightcom.api.model.PalletTemplate;
import com.freightcom.api.model.PalletType;
import com.freightcom.api.model.UserRole;
import com.freightcom.api.model.views.View;
import com.freightcom.api.repositories.custom.OrderSpecification.Mode;
import com.freightcom.api.services.dataobjects.PickupData;
import com.freightcom.api.services.orders.RateRequestResult;

/**
 * @author bryan
 *
 */
public interface OrderService extends ServiceProvider
{

    PagedResources<Resource<View>> getCustomerOrders(Map<String, Object> criteria, Pageable pageable);

    CustomerOrder bookOrder(Long orderId, CustomerOrder orderData, Map<String, Object> attributes,
            UserDetailsImpl loggedInUser) throws Exception;

    String cancelOrder(Long orderId) throws ValidationException, Exception;

    CustomerOrder createOrder(CustomerOrder order) throws Exception;

    String deleteOrder(Long orderId, UserDetails loggedInUser) throws Exception;

    CustomerOrder deliverOrder(Long orderId, String comment) throws Exception;

    CustomerOrder markIntransit(Long orderId, String comment) throws Exception;

    ResponseEntity<InputStreamResource> downloadLegacyPod(Long orderId) throws Exception;

    Object getInvoices(Long orderId);

    Object downloadPod(Long orderId) throws Exception;

    Object uploadPOD(Long orderId, MultipartFile file) throws Exception;

    Object schedulePickup(Long orderId, PickupData pickupData);

    List<Map<String, String>> getByDay(Date from, Date to, Long customerId, Long agentId);

    CustomerOrder testRate(Long orderId, Long serviceId, Map<String, BigDecimal> data) throws Exception;

    List<Map<String, String>> getByWeek(Date from, Date to, Long customerId, Long agentId);

    List<Map<String, String>> getByMonth(Date from, Date to, Long customerId, Long agentId);

    PagedResources<Resource<View>> getOrders(Map<String, Object> criteria, Pageable pageable);

    PagedResources<Resource<View>> getOrders(Map<String, Object> criteria, Pageable pageable, Mode mode);

    Page<View> getOrdersQuick(Map<String, Object> criteria, Pageable pageable);

    CustomerOrder getOrder(Long id, UserDetails loggedInUser);

    Page<LoggedEvent> loggedEvents(Long orderId, UserDetailsImpl user, Pageable pageable);

    String realDeleteOrder(Long orderId, UserDetails loggedInUser);

    CustomerOrder selectRate(Long orderId, Long rateId, CustomerOrder orderData, Map<String, Object> attributes,
            UserDetailsImpl loggedInUser) throws Exception;

    Map<String, Object> storeNoOrder(MultipartFile file, UserDetails loggedInUser, UserRole userRole) throws Exception;

    Map<String, Object> storeDistributionList(Long orderId, MultipartFile file, UserDetails loggedInUser,
            UserRole userRole) throws Exception;

    CustomerOrder updateOrder(Long id, CustomerOrder order, Map<String, Object> attributes, UserDetails loggedInUser,
            UserRole userRole) throws Exception;

    Charge reconcileCharge(Long chargeId) throws Exception;

    CustomerOrder reconcileOrderCharges(Long orderId) throws Exception;

    Object deleteCharge(Long chargeId);

    Charge updateCharge(Long chargeId, Map<String, Object> attributes) throws ValidationException;

    Object addStatusUpdate(Long orderId, Map<String, Object> attributes) throws Exception;

    Charge addCharge(Long orderId, Map<String, Object> chargeData) throws ValidationException;

    Object getStatusMessages(Long orderId, Map<String, Object> criteria, Pageable pageable) throws Exception;

    CustomerOrder sendQuote(Long orderId) throws Exception;

    Object statsPerType();

    DisputeInformation disputeCharge(Long chargeId, Map<String, Object> attributes) throws Exception;

    DisputeInformation respondToDispute(Long orderId, Map<String, Object> attributes) throws Exception;

    CustomsInvoice updateCustomsForm(Long orderId, CustomsInvoice invoiceData, Map<String, Object> attributes)
            throws Exception;

    Object upload(Long orderId, MultipartFile file) throws Exception;

    Object deleteOrderDocument(Long documentId) throws Exception;

    Object downloadOrderDocument(Long documentId) throws Exception;

    Claim submitClaim(Long orderId, Map<String, Object> attributes) throws Exception;

    Object updateClaim(Long orderId, Map<String, Object> attributes) throws Exception;

    void updateReadyForShipping() throws Exception;

    Object testUPS(Long orderId) throws Exception;

    RateRequestResult getRates(CustomerOrder order) throws Exception;

    Object duplicate(Long orderId) throws Exception;

    CustomerOrder schedulePickup(Long orderId, CustomerOrder data) throws Exception;

    CustomerOrder savePickupAndCustoms(Long orderId, CustomerOrder data) throws Exception;

    Object upload(Long orderId, MultipartFile file, String description) throws Exception;

    Object uploadPOD(Long orderId, MultipartFile file, String description) throws Exception;

    Object customerUsers(Long orderId) throws Exception;

    Object getOrderCounts(Map<String, Object> criteria);

    Object shippingLabel(Long orderId) throws Exception;

    List<PalletTemplate> getPalletTemplates(Long customerId) throws Exception;

    PalletTemplate createPalletTemplate(PalletTemplate template, Long customerId) throws Exception;

    boolean removePalletTemplate(Long templateId) throws Exception;

    List<PalletType> getPalletTypes() throws Exception;
}
