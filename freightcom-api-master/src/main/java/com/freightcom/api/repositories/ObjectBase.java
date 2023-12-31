package com.freightcom.api.repositories;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.freightcom.api.model.AccessorialServices;
import com.freightcom.api.model.Agent;
import com.freightcom.api.model.ApplicableTax;
import com.freightcom.api.model.Carrier;
import com.freightcom.api.model.Charge;
import com.freightcom.api.model.CommissionPayable;
import com.freightcom.api.model.Credit;
import com.freightcom.api.model.CreditCard;
import com.freightcom.api.model.Currency;
import com.freightcom.api.model.Customer;
import com.freightcom.api.model.Customer.AutoInvoice;
import com.freightcom.api.model.CustomerOrder;
import com.freightcom.api.model.CustomerStaff;
import com.freightcom.api.model.DeletedInvoice;
import com.freightcom.api.model.GroupedCharge;
import com.freightcom.api.model.GroupedTax;
import com.freightcom.api.model.Invoice;
import com.freightcom.api.model.LoggedEvent;
import com.freightcom.api.model.OrderAccessorials;
import com.freightcom.api.model.OrderDocument;
import com.freightcom.api.model.OrderStatus;
import com.freightcom.api.model.PackagePreference;
import com.freightcom.api.model.PalletTemplate;
import com.freightcom.api.model.PalletType;
import com.freightcom.api.model.Payable;
import com.freightcom.api.model.Service;
import com.freightcom.api.model.TransactionalEntity;
import com.freightcom.api.model.User;
import com.freightcom.api.model.UserRole;
import com.freightcom.api.model.views.View;
import com.freightcom.api.repositories.custom.CustomerSpecification;
import com.freightcom.api.repositories.custom.OrderSpecification;
import com.freightcom.api.repositories.custom.UserRoleSpecification;

public interface ObjectBase
{
    CustomerOrder getOrder(Object id);
    Customer getCustomer(Object object);
    Invoice getInvoice(Object id);
    Credit getCredit(Object id);
    User getUser(Object id);
    User getUserByLogin(String login);
    void delete(TransactionalEntity object);
    void saveInSubTransaction(TransactionalEntity object);
    Agent getAgent(Object subAgentId);
    OrderStatus getOrderStatus(Object statusId);
    List<LoggedEvent> getOrderLoggedEvents(CustomerOrder order);
    List<Credit> getPositiveCredits(Long customerId);
    Service getService(Object serviceId);
    List<CustomerStaff> getStaff(Customer customer);
    List<UserRole> getAgents();
    List<DeletedInvoice> deletedInvoices(Customer customer);
    List<GroupedCharge> groupedCharges(Invoice invoice);
    Charge getCharge(Long id);
    List<GroupedTax> groupedTaxes(Invoice invoice);
    List<Charge> chargesWithUnreportedCommission(UserRole agent);
    List<GroupedCharge> groupedChargesCustomer(Invoice invoice);
    List<Service> servicesWithUnbilledCharges(Service.Term term);
    List<Service> servicesWithUnbilledCharges();
    List<Charge> serviceUnbilledCharges(Service service);
    List<GroupedCharge> groupedCharges(Payable payable);
    void refresh(TransactionalEntity object);
    List<Charge> commissionPayableCharges();
    List<Charge> commissionPayableCharges(Agent.Term term);
    OrderAccessorials getOrderAccessorial(Object accessorialId);
    AccessorialServices getAccessorialServices(Object accessorialId);
    List<CustomerOrder> orderQuery(OrderSpecification specification, Pageable page);
    User getUserByName(String name);
    List<User> getUsersByCustomer(Customer customer);
    void flush();
    List<Charge> commissionPayableCharges(Long agentId);
    Page<UserRole> findAgents(UserRoleSpecification specification, Pageable page);
    Currency lookupCurrency(String name);
    Carrier getCarrier(Object id);
    List<Service> getCarrierServices(Carrier carrier);
    List<GroupedCharge> groupedCharges(CommissionPayable commissionPayable);
    Service getService(Long serviceId);
    PackagePreference getPackagePreference(Object id);
    CreditCard getCreditCard(Object id);
    UserRole getSystemRole();
    LoggedEvent getLatestLoggedEvent(CustomerOrder order, boolean includePrivate);
    int customerBillingRun(AutoInvoice period, int days);
    OrderDocument getOrderDocument(Long documentId);
    void lock(TransactionalEntity object);
    Page<View> findCustomerOptions(CustomerSpecification specification, Pageable page);
    Carrier getCarrierByName(String string);
    Carrier getCarrierByNameNoCache(String string);
    <T extends TransactionalEntity> T save(T object);
    EntityManager getEntityManager();
    Boolean hasAddressBook();
    Boolean hasAddressBook(Long customerId);
    List<LoggedEvent> getInvoiceLoggedEvents(Invoice invoice, boolean showPrivate);
    UserRole lookupRole(Object object);
    List<Currency> listCurrencies();
    Long orderQueryCounts(OrderSpecification orderSpecification);
    List<Service> getOrderedServices();
    List<PalletTemplate> getPalletTemplates(Customer customer);
    PalletTemplate getPalletTemplate(Object id);
    List<PalletType> getPalletTypes();
    PalletType getPalletType(Object id);
    CommissionPayable getCommissionPayable(Object id);
    Payable getPayable(Object id);
    ApplicableTax getApplicableTax(Object id);

}
