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
package com.freightcom.api.model;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.core.annotation.RestResource;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.freightcom.api.ReportableError;
import com.freightcom.api.services.converters.CreditCardConverter;
import com.freightcom.api.services.dataobjects.Payment;
import com.freightcom.api.util.Conversions;
import javax.persistence.Cacheable;

/**
 *
 *
 * @author
 */
@Entity(name = "Customer")
@Table(name = "customer")
@SQLDelete(sql = "update customer SET deleted_at = UTC_TIMESTAMP() where id=?")
@Where(clause = "deleted_at is null")
@Cacheable(value = true)
public class Customer extends TransactionalEntity
{
    @Transient
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private static final long serialVersionUID = 1L;

    public enum AutoInvoice {
        ON_BOOKING, ON_DELIVERY, DAILY, WEEKLY, BIWEEKLY, MONTHLY
    };

    public enum AutoCharge {
        NEVER, IMMEDIATELY, ON_DUE_DATE, DAILY, BIWEEKLY, WEEKLY, MONTHLY
    };

    public enum PastDueAction {
        DO_NOTHING, DISALLOW
    };

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Long version;

    @Column(name = "country")
    private String country;

    @Column(name = "invoice_currency_id")
    private Integer invoiceCurrencyId;

    @Column(name = "single_shipment_invoicing")
    private Integer singleShipmentInvoicing;

    @Column(name = "is_charge_credit_card")
    private Boolean isChargeCreditCard;

    @Column(name = "province")
    private String province;

    @Column(name = "contact")
    private String contact;

    @Column(name = "apply_tax")
    private Boolean applyTax;

    @Column(name = "billing_address_id")
    private Long billingAddressId;

    @Column(name = "dba")
    private String dba;

    @Column(name = "invoice_email")
    private String invoiceEmail;

    @Column(name = "disable_if_unpaid_invoices")
    private Boolean disableIfUnpaidInvoices;

    @Column(name = "apply_currency_exchange")
    private Integer applyCurrencyExchange;

    @Column(name = "shipping_pod_required")
    private Boolean shippingPODRequired = false;

    @Column(name = "shipping_nmfc_required")
    private Boolean shippingNMFCRequired = false;

    @Column(name = "allow_new_orders")
    private Boolean allowNewOrders = true;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "deleted_at")
    @JsonProperty(access = Access.READ_ONLY)
    private Date deletedAt;

    @Column(name = "tax_id")
    private String taxId;

    @Column(name = "phone")
    private String phone;

    @Column(name = "discount_perc")
    private Float discountPerc;

    @Column(name = "name")
    private String name;

    @Column(name = "apply_credit_limit")
    private Integer applyCreditLimit;

    @Column(name = "freight_class")
    private Integer freightClass;

    @Column(name = "status")
    private Integer status;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "reference3")
    private String reference3;

    @Column(name = "city")
    private String city;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonProperty(access = Access.READ_ONLY)
    @Column(name = "created_at")
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonProperty(access = Access.READ_ONLY)
    @Column(name = "activated_at")
    private Date activatedAt;

    @Column(name = "reference1")
    private String reference1;

    @Column(name = "reference2")
    private String reference2;

    @Column(name = "nmfc_code")
    private Integer nmfcCode;

    @Column(name = "show_rates")
    private Integer showRates;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    @JsonProperty(access = Access.READ_ONLY)
    private Date updatedAt;

    @Column(name = "credit_limit")
    private BigDecimal creditLimit;

    @Column(name = "invoice_term")
    private Integer invoiceTerm;

    @Column(name = "email")
    private String email;

    @Column(name = "creator")
    private Long creator;

    @Column(name = "address")
    private String address;

    @Column(name = "broker_name")
    private String brokerName;

    @Column(name = "invoice_term_warning")
    private Integer invoiceTermWarning;

    @Column(name = "time_zone")
    private String timeZone;

    @Column(name = "broker")
    private Integer broker;

    @Column(name = "language_preference")
    private String languagePreference;

    @Column(name = "is_web_customer")
    private Boolean isWebCustomer;

    @Column(name = "deleted")
    private Boolean deleted;

    @Column(name = "small_package")
    private Integer smallPackage;

    @Column(name = "postal_code")
    private String postalCode;

    @Column(name = "invoice_currency")
    private String invoiceCurrency;

    @Column(name = "cc_receipt")
    private Boolean ccReceipt = true;

    @Column(name = "auto_invoice")
    @Enumerated(EnumType.STRING)
    private AutoInvoice autoInvoice = AutoInvoice.ON_BOOKING;

    @Enumerated(EnumType.STRING)
    @Column(name = "auto_charge")
    private AutoCharge autoCharge = AutoCharge.NEVER;

    @Column(name = "term")
    private String term;

    @Column(name = "past_due_action")
    @Enumerated(EnumType.STRING)
    private PastDueAction pastDueAction = PastDueAction.DO_NOTHING;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "suspended_at")
    @JsonProperty(access = Access.READ_ONLY)
    private Date suspendedAt;

    @OneToOne(orphanRemoval = true)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "sub_agent_id", referencedColumnName = "id")
    @RestResource(exported = false)
    private Agent salesAgent;

    @OneToMany(cascade = { CascadeType.ALL })
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    @OrderColumn(name = "position")
    private List<ApplicableTax> applicableTaxes = new ArrayList<ApplicableTax>();

    @OneToOne(cascade = { CascadeType.ALL }, optional = false, fetch = FetchType.LAZY, mappedBy = "customer")
    private CustomerBilling customerBilling = null;

    @OneToMany
    @JoinTable(name = "excluded_service", joinColumns = @JoinColumn(name = "customer_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "service_id", referencedColumnName = "id"))
    private List<Service> excludedServices = new ArrayList<Service>(0);

    @OneToOne(cascade = { CascadeType.ALL })
    @JoinColumn(name = "ship_to_address_id", referencedColumnName = "id", nullable = true)
    private ShippingAddress shipTo;

    @OneToOne(cascade = { CascadeType.ALL })
    @JoinColumn(name = "ship_from_address_id", referencedColumnName = "id", nullable = true)
    private ShippingAddress shipFrom;

    @OneToOne(cascade = { CascadeType.MERGE })
    @JoinColumn(name = "package_preference_id")
    @JsonProperty(value = "packagePreference")
    private PackagePreference packagePreference;

    @Formula("(select count(*) from customer_order where customer_order.customer_id=id)")
    private Long orderCount;

    @OneToMany(targetEntity = CreditCard.class, mappedBy = "customer", cascade = {
            CascadeType.ALL }, orphanRemoval = true)
    private List<CreditCard> creditCards = new ArrayList<CreditCard>();

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id")
    private List<CustomerAdmin> admins;

    @Formula("(exists(select * from credit as C where C.customer_id=id and C.amount_remaining > 0))")
    private boolean hasCredit = false;

    @Formula("(activated_at is not null)")
    private Boolean active = false;

    @OneToMany(cascade = { CascadeType.ALL }, orphanRemoval = true)
    @JoinColumn(name = "customer_id", nullable = true)
    private List<Credit> credits = new ArrayList<Credit>();

    public Customer()
    {
        super();
    }

    public Long getId()
    {
        return id;
    }

    @JsonProperty(access = Access.READ_ONLY)
    public void setId(Long id)
    {
        this.id = id;
    }

    public String getCountry()
    {
        return country;
    }

    public void setCountry(String country)
    {
        this.country = country;
    }

    public Integer getInvoiceCurrencyId()
    {
        return invoiceCurrencyId;
    }

    public void setInvoiceCurrencyId(Integer invoiceCurrencyId)
    {
        this.invoiceCurrencyId = invoiceCurrencyId;
    }

    public Integer getSingleShipmentInvoicing()
    {
        return singleShipmentInvoicing;
    }

    public void setSingleShipmentInvoicing(Integer singleShipmentInvoicing)
    {
        this.singleShipmentInvoicing = singleShipmentInvoicing;
    }

    public Boolean getIsChargeCreditCard()
    {
        return isChargeCreditCard;
    }

    public void setIsChargeCreditCard(Boolean isChargeCreditCard)
    {
        this.isChargeCreditCard = isChargeCreditCard;
    }

    public String getProvince()
    {
        return province;
    }

    public void setProvince(String province)
    {
        this.province = province;
    }

    public String getContact()
    {
        return contact;
    }

    public void setContact(String contact)
    {
        this.contact = contact;
    }

    public Boolean getApplyTax()
    {
        return applyTax;
    }

    public void setApplyTax(Boolean applyTax)
    {
        this.applyTax = applyTax;
    }

    public Long getBillingAddressId()
    {
        return billingAddressId;
    }

    public void setBillingAddressId(Long billingAddressId)
    {
        this.billingAddressId = billingAddressId;
    }

    public String getDba()
    {
        return dba;
    }

    public void setDba(String dba)
    {
        this.dba = dba;
    }

    public String getInvoiceEmail()
    {
        return invoiceEmail;
    }

    public void setInvoiceEmail(String invoiceEmail)
    {
        this.invoiceEmail = invoiceEmail;
    }

    public Boolean getDisableIfUnpaidInvoices()
    {
        return disableIfUnpaidInvoices;
    }

    public void setDisableIfUnpaidInvoices(Boolean disableIfUnpaidInvoices)
    {
        this.disableIfUnpaidInvoices = disableIfUnpaidInvoices;
    }

    public Boolean getActive()
    {
        return active;
    }

    public void setActive(Boolean active)
    {
        if (active) {
            if (activatedAt == null) {
                activatedAt = Date.from(ZonedDateTime.now(ZoneId.of("UTC"))
                        .toInstant());
            }
        } else {
            activatedAt = null;
        }
    }

    public Integer getApplyCurrencyExchange()
    {
        return applyCurrencyExchange;
    }

    public void setApplyCurrencyExchange(Integer applyCurrencyExchange)
    {
        this.applyCurrencyExchange = applyCurrencyExchange;
    }

    public Boolean getShippingPODRequired()
    {
        return shippingPODRequired == null ? false : shippingPODRequired;
    }

    public void setShippingPODRequired(Boolean shippingPODRequired)
    {
        this.shippingPODRequired = shippingPODRequired;
    }

    public Boolean getShippingNMFCRequired()
    {
        return shippingNMFCRequired == null ? false : shippingNMFCRequired;
    }

    public void setShippingNMFCRequired(Boolean shippingNMFCRequired)
    {
        this.shippingNMFCRequired = shippingNMFCRequired;
    }

    public ZonedDateTime getDeletedAt()
    {
        return asDate(deletedAt);
    }

    public void setDeletedAt(Date deletedAt)
    {
        this.deletedAt = deletedAt;
    }

    public String getTaxId()
    {
        return taxId;
    }

    public void setTaxId(String taxId)
    {
        this.taxId = taxId;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public Float getDiscountPerc()
    {
        return discountPerc;
    }

    public void setDiscountPerc(Float discountPerc)
    {
        this.discountPerc = discountPerc;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Integer getApplyCreditLimit()
    {
        return applyCreditLimit;
    }

    public void setApplyCreditLimit(Integer applyCreditLimit)
    {
        this.applyCreditLimit = applyCreditLimit;
    }

    public Integer getFreightClass()
    {
        return freightClass;
    }

    public void setFreightClass(Integer freightClass)
    {
        this.freightClass = freightClass;
    }

    public Integer getStatus()
    {
        return status;
    }

    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public String getAccountNumber()
    {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber)
    {
        this.accountNumber = accountNumber;
    }

    public String getReference3()
    {
        return reference3;
    }

    public void setReference3(String reference3)
    {
        this.reference3 = reference3;
    }

    public String getCity()
    {
        return city;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public ZonedDateTime getCreatedAt()
    {
        return asDate(createdAt);
    }

    public void setCreatedAt(Date createdAt)
    {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getActivatedAt()
    {
        return asDate(activatedAt);
    }

    public void setActivatedAt(Date activatedAt)
    {
        this.activatedAt = activatedAt;
    }

    public String getReference1()
    {
        return reference1;
    }

    public void setReference1(String reference1)
    {
        this.reference1 = reference1;
    }

    public String getReference2()
    {
        return reference2;
    }

    public void setReference2(String reference2)
    {
        this.reference2 = reference2;
    }

    public Integer getNmfcCode()
    {
        return nmfcCode;
    }

    public void setNmfcCode(Integer nmfcCode)
    {
        this.nmfcCode = nmfcCode;
    }

    public Integer getShowRates()
    {
        return showRates;
    }

    public void setShowRates(Integer showRates)
    {
        this.showRates = showRates;
    }

    public Long getSubAgentId()
    {
        if (getSalesAgent() != null) {
            return getSalesAgent().getId();
        } else {
            return null;
        }
    }

    public ZonedDateTime getUpdatedAt()
    {
        return asDate(updatedAt);
    }

    public void setUpdatedAt(Date updatedAt)
    {
        this.updatedAt = updatedAt;
    }

    public BigDecimal getCreditLimit()
    {
        return creditLimit;
    }

    public void setCreditLimit(BigDecimal creditLimit)
    {
        this.creditLimit = creditLimit;
    }

    public Integer getInvoiceTerm()
    {
        return invoiceTerm;
    }

    public void setInvoiceTerm(Integer invoiceTerm)
    {
        this.invoiceTerm = invoiceTerm;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public Long getCreator()
    {
        return creator;
    }

    public void setCreator(Long creator)
    {
        this.creator = creator;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getBrokerName()
    {
        return brokerName;
    }

    public void setBrokerName(String brokerName)
    {
        this.brokerName = brokerName;
    }

    public Integer getInvoiceTermWarning()
    {
        return invoiceTermWarning;
    }

    public void setInvoiceTermWarning(Integer invoiceTermWarning)
    {
        this.invoiceTermWarning = invoiceTermWarning;
    }

    public String getTimeZone()
    {
        return timeZone;
    }

    public void setTimeZone(String timeZone)
    {
        this.timeZone = timeZone;
    }

    public Integer getBroker()
    {
        return broker;
    }

    public void setBroker(Integer broker)
    {
        this.broker = broker;
    }

    public String getLanguagePreference()
    {
        return languagePreference;
    }

    public void setLanguagePreference(String languagePreference)
    {
        this.languagePreference = languagePreference;
    }

    public Boolean getIsWebCustomer()
    {
        return isWebCustomer;
    }

    public void setIsWebCustomer(Boolean isWebCustomer)
    {
        this.isWebCustomer = isWebCustomer;
    }

    public Boolean getDeleted()
    {
        return deleted;
    }

    public void setDeleted(Boolean deleted)
    {
        this.deleted = deleted;
    }

    public Integer getSmallPackage()
    {
        return smallPackage;
    }

    public void setSmallPackage(Integer smallPackage)
    {
        this.smallPackage = smallPackage;
    }

    public String getPostalCode()
    {
        return postalCode;
    }

    public void setPostalCode(String postalCode)
    {
        this.postalCode = postalCode;
    }

    public String getInvoiceCurrency()
    {
        return invoiceCurrency;
    }

    public void setInvoiceCurrency(String invoiceCurrency)
    {
        this.invoiceCurrency = invoiceCurrency;
    }

    public ZonedDateTime getSuspendedAt()
    {
        return asDate(suspendedAt);
    }

    public void setSuspendedAt(Date suspendedAt)
    {
        this.suspendedAt = suspendedAt;
    }

    public boolean getSuspended()
    {
        return activatedAt == null || suspendedAt != null;
    }

    public void setSuspended(Boolean suspended)
    {
        if (suspended == null || !suspended) {
            suspendedAt = null;

            if (activatedAt == null) {
                activatedAt = now();
            }
        } else {
            suspendedAt = now();
        }
    }

    public Agent getSalesAgent()
    {
        return salesAgent;
    }

    public void setSalesAgent(Agent salesAgent)
    {
        this.salesAgent = salesAgent;
    }

    public Long getOrderCount()
    {
        return orderCount;
    }

    public void setOrderCount(Long orderCount)
    {
        this.orderCount = orderCount;
    }

    public List<CustomerAdmin> getAdmins()
    {
        return admins;
    }

    public List<CustomerStaff> getStaff()
    {
        return new ArrayList<CustomerStaff>(0);
    }

    public String toString()
    {
        return "CUST " + id + " " + name;
    }

    public AutoInvoice getAutoInvoice()
    {
        return autoInvoice;
    }

    public void setAutoInvoice(AutoInvoice autoInvoice)
    {
        this.autoInvoice = autoInvoice;
    }

    @JsonSerialize(contentConverter = CreditCardConverter.class)
    public List<CreditCard> getCreditCards()
    {
        return creditCards;
    }

    public void setCreditCards(List<CreditCard> creditCards)
    {
        this.creditCards = creditCards;
    }

    public int countDefaultCards()
    {
        if (creditCards == null || creditCards.isEmpty()) {
            return 0;
        } else {
            return creditCards.stream()
                    .map((card) -> card.getIsDefault() ? 1 : 0)
                    .reduce(0, (value, result) -> value + result);
        }
    }

    public CreditCard getDefaultCreditCard()
    {
        if (creditCards == null || creditCards.isEmpty()) {
            return null;
        } else {
            return creditCards.stream()
                    .filter((card) -> card.getIsDefault())
                    .findFirst()
                    .orElse(null);
        }
    }

    public Customer updateCreditCards(List<CreditCard> creditCards)
    {
        if (creditCards == null || creditCards.isEmpty()) {
            this.creditCards.clear();
        } else {
            Map<Long, CreditCard> existing = new HashMap<Long, CreditCard>();

            for (CreditCard card : this.creditCards) {
                existing.put(card.getId(), card);
            }

            this.creditCards.clear();

            for (CreditCard card : creditCards) {
                if (card.getId() == null || !existing.containsKey(card.getId())) {
                    card.setCustomer(this);
                    this.creditCards.add(card);
                } else {
                    this.creditCards.add(existing.get(card.getId())
                            .update(card));
                }
            }
        }

        return this;
    }

    public CreditCard updateCreditCards(Payment payment) throws Exception
    {
        CreditCard card = null;

        log.debug("CUSTOMER UPDATE CREDIT CARDS " + payment);

        if (payment.getCreditCardId() != null) {
            // Existing credit card?
            CreditCard existing = creditCards.stream()
                    .filter(candidate -> candidate.getId() != null && candidate.getId()
                            .equals(payment.getCreditCardId()))
                    .findFirst()
                    .orElse(null);

            if (existing == null) {
                throw new ReportableError("Credit card no longer exists");
            }
        } else if (payment.getCreditCardNumber() != null) {
            // Request to add card

            card = new CreditCard();
            card.setCustomer(this);
            creditCards.add(card);

            if (payment.getNameOnCard() != null) {
                card.setName(payment.getNameOnCard());
            }

            if (payment.getExpiryMonth() != null) {
                card.setExpiryMonth(Conversions.toString(payment.getExpiryMonth()));
            }

            if (payment.getExpiryYear() != null) {
                card.setExpiryYear(Conversions.toString(payment.getExpiryYear()));
            }

            if (payment.getCardType() != null) {
                card.setType(payment.getCardType());
            }
        }

        return card;
    }

    public AutoCharge getAutoCharge()
    {
        return autoCharge;
    }

    public void setAutoCharge(AutoCharge autoCharge)
    {
        this.autoCharge = autoCharge;
    }

    public String getTerm()
    {
        return term;
    }

    public void setTerm(String term)
    {
        this.term = term;
    }

    public PastDueAction getPastDueAction()
    {
        return pastDueAction == null ? PastDueAction.DO_NOTHING : pastDueAction;
    }

    public void setPastDueAction(PastDueAction pastDueAction)
    {
        this.pastDueAction = pastDueAction;
    }

    public Boolean getAllowNewOrders()
    {
        return allowNewOrders;
    }

    public void setAllowNewOrders(Boolean allowNewOrders)
    {
        this.allowNewOrders = allowNewOrders;
    }

    public Boolean getCcReceipt()
    {
        return ccReceipt;
    }

    public void setCcReceipt(Boolean ccReceipt)
    {
        this.ccReceipt = ccReceipt;
    }

    public List<ApplicableTax> getApplicableTaxes()
    {
        return applicableTaxes;
    }

    public void setApplicableTaxes(List<ApplicableTax> applicableTaxes)
    {
        this.applicableTaxes = applicableTaxes;
    }

    public List<Service> getExcludedServices()
    {
        return excludedServices;
    }

    public void setExcludedServices(List<Service> excludedServices)
    {
        this.excludedServices = excludedServices;
    }

    public ShippingAddress getShipTo()
    {
        return shipTo;
    }

    public void setShipTo(ShippingAddress shipTo)
    {
        this.shipTo = shipTo;
    }

    public ShippingAddress getShipFrom()
    {
        return shipFrom;
    }

    public void setShipFrom(ShippingAddress shipFrom)
    {
        this.shipFrom = shipFrom;
    }

    @JsonProperty(value = "packagePreference")
    public void setPackagePreference(PackagePreference packagePreference)
    {
        this.packagePreference = packagePreference;
    }

    public PackagePreference getPackagePreference()
    {
        return packagePreference;
    }

    public CustomerBilling getCustomerBilling()
    {
        if (customerBilling == null) {
            customerBilling = new CustomerBilling();
            customerBilling.setCustomer(this);
        }

        return customerBilling;
    }

    public Long getVersion()
    {
        return version == null ? 0 : version;
    }

    public void setVersion(Long version)
    {
        this.version = version;
    }

    public boolean isActive()
    {
        return getActivatedAt() != null && getSuspendedAt() == null;
    }

    public Map<String, BigDecimal> getCreditAvailableMap()
    {
        Map<String, BigDecimal> available = new HashMap<String, BigDecimal>();

        for (Credit credit : credits) {
            if (BigDecimal.ZERO.compareTo(credit.getAmountRemaining()) != 0) {
                String currency = credit.getCurrency();

                if (currency == null) {
                    currency = "CAD";
                }

                BigDecimal amount = available.get(currency);

                if (amount == null) {
                    available.put(currency, credit.getAmountRemaining());
                } else {
                    available.put(currency, amount.add(credit.getAmountRemaining()));
                }
            }
        }

        return available;
    }

    public BigDecimal getCreditAvailable(String currency)
    {
        return credits.stream()
                .filter(credit -> currency.equals(credit.getCurrency()))
                .map(credit -> credit.getAmountRemaining())
                .reduce(BigDecimal.ZERO, (total, amount) -> total.add(amount))
                .setScale(2);
    }

    public List<Credit> getPositiveCredits(String currency)
    {
        return credits.stream()
                .filter(credit -> currency.equals(credit.getCurrency()) && credit.getAmountRemaining()
                        .compareTo(BigDecimal.ZERO) > 0)
                .collect(Collectors.toList());
    }

    public boolean hasCredit()
    {
        return hasCredit;
    }
}
