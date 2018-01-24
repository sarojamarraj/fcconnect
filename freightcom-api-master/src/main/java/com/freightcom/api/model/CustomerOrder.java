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

import static com.freightcom.api.model.support.OrderStatusCode.BOOKED;
import static com.freightcom.api.model.support.OrderStatusCode.CANCELLED;
import static com.freightcom.api.model.support.OrderStatusCode.DELETED;
import static com.freightcom.api.model.support.OrderStatusCode.DELIVERED;
import static com.freightcom.api.model.support.OrderStatusCode.DRAFT;
import static com.freightcom.api.model.support.OrderStatusCode.QUOTED;
import static com.freightcom.api.model.support.OrderStatusCode.READY_FOR_SHIPPING;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Formula;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.core.annotation.RestResource;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.freightcom.api.model.support.OrderStatusCode;
import com.freightcom.api.services.converters.PickupConverter;
import com.freightcom.api.util.AccessorialDeserializer;
import com.freightcom.api.util.InsuranceTypeDeserializer;

/**
 *
 *
 * @author
 */
@Entity(name = "CustomerOrder")
@Table(name = "customer_order")
@SQLDelete(sql = "update customer_order SET deleted_at = UTC_TIMESTAMP() where id=?")
@Where(clause = "deleted_at is null")
public class CustomerOrder extends TransactionalEntity implements OwnedByCustomer
{

    @Transient
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private static final long serialVersionUID = 1L;

    public static final Integer DIM_TYPE_CA = 2;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "submitted_at")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonProperty(access = Access.READ_ONLY)
    private Date submittedAt;

    @Column(name = "quoted_fuel_cost")
    private BigDecimal quotedFuelCost;

    @Column(name = "package_id")
    private Long packageId;

    @Column(name = "origin_close_time_m")
    private String originCloseTimeM;

    @Column(name = "markup_perc")
    private Float markupPerc;

    @Column(name = "comm_status")
    private Integer commStatus;

    @Column(name = "bol_id")
    private String bolId;

    @Column(name = "operating_cost_perc")
    private Double operatingCostPerc;

    @Column(name = "distribution_quote")
    private Float distributionQuote;

    @Column(name = "is_from_mf_to_f")
    private Integer isFromMfToF;

    @Column(name = "inside_pickup")
    private Integer insidePickup;

    @Column(name = "job_number")
    private String jobNumber;

    @Column(name = "quoted_total_charge")
    private BigDecimal quotedTotalCharge;

    @Column(name = "homeland_security")
    private Integer homelandSecurity;

    @Column(name = "frozen")
    private Integer frozen;

    @Column(name = "pod_name")
    private String podName;

    @Column(name = "pak_weight")
    private Float pakWeight;

    @Column(name = "master_tracking_num_1")
    private String masterTrackingNum1;

    @Column(name = "excess_length")
    private Integer excessLength;

    @Column(name = "quoted_total_cost")
    private BigDecimal quotedTotalCost;

    @Column(name = "cod_value")
    private Float codValue;

    @Column(name = "shipment_density")
    private Integer shipmentDensity;

    @Column(name = "tender_error_message")
    private String tenderErrorMessage;

    @Column(name = "tran_fee")
    private BigDecimal tranFee;

    @Column(name = "reference_name")
    private String referenceName;

    @Column(name = "partner_markup")
    private Float partnerMarkup;

    @Column(name = "freight_tariff")
    private BigDecimal freightTariff;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "invoice_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonProperty(access = Access.READ_ONLY)
    private Date invoiceDate;

    @Column(name = "fuel_tariff")
    private BigDecimal fuelTariff;

    @Column(name = "currency_rate")
    private Float currencyRate;

    @Column(name = "dangerous_goods")
    private String dangerousGoods;

    @Column(name = "internal_notes")
    private String internalNotes;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonProperty(access = Access.READ_ONLY)
    private Date updatedAt;

    @Column(name = "tot_act_wgt")
    private Float totActWgt;

    @Column(name = "currency")
    private Integer currency;

    @Column(name = "tracking_url")
    private String trackingUrl;

    @Column(name = "sort_segregate")
    private Integer sortSegregate;

    @Column(name = "mode_transport")
    private String modeTransport;

    @Column(name = "mark_and_tag_freight")
    private Integer markAndTagFreight;

    @Column(name = "mfw_booking_key")
    private String mfwBookingKey;

    @Column(name = "carrier_pick_up_conf")
    private String carrierPickUpConf;

    @Column(name = "comm_driver_id")
    private Long commDriverId;

    @Column(name = "is_e_manifested")
    private Boolean isEManifested;

    @Column(name = "proof_of_delivery")
    private String proofOfDelivery;

    @Column(name = "customes_freight")
    private Integer customesFreight;

    @Column(name = "gross_profit")
    private BigDecimal grossProfit;

    @Column(name = "destination_terminal")
    private Long destinationTerminal;

    @Column(name = "pod_file_name")
    private String podFileName;

    @Column(name = "tot_act_quantity")
    private Integer totActQuantity;

    @Column(name = "hold")
    private Integer hold;

    @Column(name = "cod_payment")
    private String codPayment;

    @Column(name = "commissionable_amount")
    private BigDecimal commissionableAmount;

    @Column(name = "insurance_value_3rd")
    private Float insuranceValue3rd;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "package_type_id")
    private PackageType packageType;

    @Column(name = "wait_time")
    private Integer waitTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "pod_timestamp")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonProperty(access = Access.READ_ONLY)
    private Date podTimestamp;

    @Column(name = "service_name")
    private String serviceName;

    @Column(name = "fuel_surcharge")
    private BigDecimal fuelSurcharge;

    @Column(name = "edi_verified")
    private Boolean ediVerified;

    @Column(name = "insurance_type")
    private Integer insuranceType;

    @Column(name = "total_tariff")
    private BigDecimal totalTariff;

    @Column(name = "original_markdown_type")
    private Integer originalMarkdownType;

    @Column(name = "eshipper_order_id")
    private Long eshipperOrderId;

    @Column(name = "per_cubic_feet")
    private String perCubicFeet;

    @Column(name = "adjustment")
    private BigDecimal adjustment;

    @Column(name = "transit_days")
    private Integer transitDays;

    @Column(name = "reference3")
    private String reference3;

    @Column(name = "distribution_service_id")
    private Long distributionServiceId;

    @Column(name = "param_service_id")
    private Long paramServiceId;

    @Column(name = "expected_delivery_date")
    private String expectedDeliveryDate;

    @Column(name = "reference2")
    private String reference2;

    @Column(name = "cod_pin_number")
    private String codPinNumber;

    @Column(name = "return_service")
    private Integer returnService;

    @Column(name = "has_been_edited")
    private Boolean hasBeenEdited;

    @Column(name = "canadian_customs")
    private Integer canadianCustoms;

    @Column(name = "reference_code")
    private String referenceCode;

    @Column(name = "notes_1")
    private String notes1;

    @Column(name = "notes_2")
    private String notes2;

    @Column(name = "inbond_fee")
    private Integer inbondFee;

    @Column(name = "load_freight_at_pickup")
    private Integer loadFreightAtPickup;

    @Column(name = "reference2_name")
    private String reference2Name;

    @Column(name = "merged")
    private Boolean merged;

    @Column(name = "protective_pallet_cover")
    private Integer protectivePalletCover;

    @Column(name = "quoted_base_cost")
    private BigDecimal quotedBaseCost;

    @Column(name = "from_airline_name")
    private String fromAirlineName;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ship_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date shipDate;

    @Column(name = "quoted_base_charge")
    private BigDecimal quotedBaseCharge;

    @Column(name = "fuel_cost")
    private BigDecimal fuelCost;

    @Column(name = "load_id")
    private Long loadId;

    @ManyToOne
    @JoinColumn(name = "agent_id")
    private Agent agent;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "scheduled_ship_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date scheduledShipDate;

    @Column(name = "freeze_protect")
    private Integer freezeProtect;

    @Column(name = "type")
    private Integer type;

    @Column(name = "pier_charge")
    private Integer pierCharge;

    @Column(name = "guarantee_charge")
    private Integer guaranteeCharge;

    @Column(name = "inside_delivery")
    private Integer insideDelivery;

    @Column(name = "payment_type_id")
    private Integer paymentTypeId;

    @Column(name = "military_base_delivery")
    private Integer militaryBaseDelivery;

    @Column(name = "invoice_number")
    private String invoiceNumber;

    @Column(name = "manifest_number")
    private String manifestNumber;

    @Column(name = "mfw_connection_key")
    private String mfwConnectionKey;

    @Column(name = "insured_amount")
    private Float insuredAmount;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "deleted_at")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonProperty(access = Access.READ_ONLY)
    private Date deletedAt;

    @Column(name = "protective_drum_cover")
    private Integer protectiveDrumCover;

    @Column(name = "reference3_name")
    private String reference3Name;

    @Column(name = "limited_access")
    private Integer limitedAccess;

    @Column(name = "vehicle_full")
    private Boolean vehicleFull;

    @Column(name = "heated_service")
    private Integer heatedService;

    @Column(name = "dim_type")
    private Integer dimType;

    @Column(name = "affiliate_fee")
    private BigDecimal affiliateFee;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonProperty(access = Access.READ_ONLY)
    private Date createdAt;

    @Column(name = "tender_status")
    private String tenderStatus;

    @Column(name = "pod_file")
    private byte[] podFile;

    @Column(name = "expected_transit")
    private Integer expectedTransit;

    @Column(name = "master_tracking_num")
    private String masterTrackingNum;

    @Column(name = "billing_status")
    private Integer billingStatus;

    @Column(name = "actual_base_charge")
    private BigDecimal actualBaseCharge;

    @Column(name = "unload_freight_at_del")
    private Integer unloadFreightAtDel;

    @Column(name = "creator")
    private Long creator;

    @Column(name = "reconcil_adjustment")
    private BigDecimal reconcilAdjustment;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "date_of_delivery")
    @JsonProperty(access = Access.READ_ONLY)
    private Date dateOfDelivery;

    @Column(name = "vehicle_type")
    private Integer vehicleType;

    @Column(name = "destination_close_time_h")
    private String destinationCloseTimeH;

    @Column(name = "destination_close_time_m")
    private String destinationCloseTimeM;

    @Column(name = "delivery_appt")
    private Integer deliveryAppt;

    @Column(name = "parent_ediwo_id")
    private Long parentEdiwoId;

    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "assign_driver_id")
    private Long assignDriverId;

    @Column(name = "quoted_fuel_surcharge")
    private BigDecimal quotedFuelSurcharge;

    @Column(name = "charged_weight")
    private Float chargedWeight;

    @Column(name = "prev_invoice_number")
    private Long prevInvoiceNumber;

    @Column(name = "paper_manifest_status")
    private Integer paperManifestStatus;

    @Column(name = "comm_amount")
    private BigDecimal commAmount;

    @Column(name = "exibition_site")
    private Integer exibitionSite;

    @Column(name = "is_insurance_manifested")
    private Boolean isInsuranceManifested;

    @Column(name = "eshipper_oid")
    private Long eshipperOid;

    @Column(name = "insurance_currency")
    private Integer insuranceCurrency;

    @Column(name = "cur_code")
    private Integer curCode;

    @Column(name = "after_hours")
    private Boolean afterHours;

    @Column(name = "operating_cost")
    private BigDecimal operatingCost;

    @Column(name = "pickup_num")
    private Integer pickupNum;

    @Column(name = "to_airline_name")
    private String toAirlineName;

    @Column(name = "base_cost")
    private BigDecimal baseCost;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "quoted_at")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonProperty(access = Access.READ_ONLY)
    private Date quotedAt;

    @Column(name = "cross_border_fee")
    private Integer crossBorderFee;

    @Column(name = "signature_required")
    private Integer signatureRequired;

    @Column(name = "quote_status")
    private Integer quoteStatus;

    @Column(name = "insurance_value")
    private Float insuranceValue;

    @Column(name = "single_shipment")
    private Integer singleShipment;

    @Column(name = "sub_type")
    private Integer subType;

    @Column(name = "base_charge")
    private BigDecimal baseCharge;

    @Column(name = "customer_markdown")
    private Float customerMarkdown;

    @Column(name = "quantity")
    private Long quantity;

    @Column(name = "pickup_time_h")
    private String pickupTimeH;

    @Column(name = "pickup_time_m")
    private String pickupTimeM;

    @Column(name = "editable")
    private Integer editable;

    @Column(name = "customer_markup")
    private Float customerMarkup;

    @Column(name = "customer_markup_type")
    private Integer customerMarkupType;

    @Column(name = "special_equipment")
    private String specialEquipment;

    @Column(name = "actual_weight")
    private Float actualWeight;

    @Column(name = "update_status")
    private Boolean updateStatus;

    @Column(name = "return_order_id")
    private Long returnOrderId;

    @Column(name = "distribution_list_id")
    private Long distributionGroupId;

    @Column(name = "custom_work_order")
    private Boolean customWorkOrder = false;

    @OneToOne(cascade = { CascadeType.ALL })
    @JoinColumn(name = "scheduled_pick_up_id")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonSerialize(converter = PickupConverter.class)
    private Pickup scheduledPickup;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private CustomsInvoice customsInvoice = null;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private ShippingLabels shippingLabel = null;

    @OneToOne(cascade = { CascadeType.ALL })
    @JoinColumn(name = "selected_quote_id", referencedColumnName = "id", nullable = true)
    private SelectedQuote selectedQuote;

    @OneToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id", updatable = false, insertable = false, nullable = true)
    private Customer customer;

    @OneToOne(cascade = { CascadeType.ALL })
    @JoinColumn(name = "ship_to_id", referencedColumnName = "id", nullable = true)
    private ShippingAddress shipTo;

    @OneToOne(cascade = { CascadeType.ALL })
    @JoinColumn(name = "ship_from_id", referencedColumnName = "id", nullable = true)
    private ShippingAddress shipFrom;

    @OneToMany(targetEntity = Package.class, mappedBy = "order", cascade = { CascadeType.ALL })
    @Where(clause = "deleted_at is null")
    private List<Package> packages = new ArrayList<Package>();

    @OneToMany(targetEntity = OrderAccessorials.class, mappedBy = "order", cascade = { CascadeType.ALL })
    @JsonDeserialize(using = AccessorialDeserializer.class)
    private List<OrderAccessorials> accessorialServices = new ArrayList<OrderAccessorials>();

    @OneToMany(targetEntity = Pallet.class, mappedBy = "order", cascade = { CascadeType.ALL })
    private List<Pallet> pallets = new ArrayList<Pallet>();

    @OneToOne
    @JoinColumn(name = "status_id")
    private OrderStatus orderStatus;

    @OneToOne
    private Carrier carrier;

    @OneToOne
    private Service service;

    @OneToMany(cascade = { CascadeType.ALL }, orphanRemoval = true, mappedBy = "order")
    @OrderBy("id asc")
    private List<Charge> charges = new ArrayList<Charge>();

    @OneToMany(cascade = { CascadeType.ALL }, orphanRemoval = true, mappedBy = "order")
    @OrderBy("id asc")
    private List<OrderDocument> files = new ArrayList<OrderDocument>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "order")
    private List<OrderRateQuote> quotes = new ArrayList<OrderRateQuote>();

    @ManyToMany
    @JoinTable(name = "order_invoice_workorder", joinColumns = @JoinColumn(name = "elt", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "order_id", referencedColumnName = "id"))
    @OrderColumn(name = "position")
    private List<Invoice> invoices;

    @OneToOne(cascade = { CascadeType.ALL })
    @JoinColumn(name = "claim_id", referencedColumnName = "id", nullable = true)
    private Claim claim;

    @OneToOne(cascade = { CascadeType.ALL })
    @JoinColumn(name = "dispute_information_id", referencedColumnName = "id", nullable = true)
    private DisputeInformation disputeInformation;

    @JsonIgnore
    @Transient
    private List<LoggedEvent> loggedEvents;

    @Formula("(select PT.name from package_type PT where PT.id=package_type_id)")
    private String packageTypeName;

    @Transient
    private String orderStatusName = null;

    @Formula("(select TD.city from shipping_address TD where TD.id=ship_to_id)")
    private String destinationCity;

    @Formula("(select TA.city from shipping_address TA where TA.id=ship_from_id)")
    private String originCity;

    @Formula("(select TD1.postal_code from shipping_address TD1 where TD1.id=ship_to_id)")
    private String destinationPostalCode;

    @Formula("(select TA1.postal_code from shipping_address TA1 where TA1.id=ship_from_id)")
    private String originPostalCode;

    @Formula("(select TD2.company from shipping_address TD2 where TD2.id=ship_to_id)")
    private String destinationCompany;

    @Formula("(select TA2.company from shipping_address TA2 where TA2.id=ship_from_id)")
    private String originCompany;

    @Formula("(select CR.name from carrier CR where CR.id=carrier_id)")
    private String carrierName;

    @Formula("(select count(*) from distribution_address DA where DA.distribution_group_id=distribution_list_id)")
    private Integer destinationCount;

    @Formula("(SELECT IF(SERVICE.carrier_id=40,(select CA1.name from carrier CA1 where CA1.id=SERVICE.carrier_id),IF((select CA2.name from carrier CA2 where CA2.id=SERVICE.carrier_id)='freightcom', SERVICE.name, SERVICE.provider)) FROM selected_quote SQ left join service SERVICE on SERVICE.id=SQ.service_id where SQ.id=selected_quote_id)")
    private String actualCarrierName;

    @Formula("(IF(exists (select * from charge CRZ where CRZ.order_id=id and CRZ.invoice_id is null and IFNULL(IFNULL(CRZ.quantity,0) * IFNULL(CRZ.charge, 0), 0) > 0), 'Unbilled charges', 'Fully invoiced'))")
    private String invoiceStatus;

    @Formula("(select IFNULL(sum(IFNULL(C.quantity,0) * IFNULL(C.charge, 0)),0) from charge C where C.order_id=id and C.invoice_id is null and C.charge > 0)")
    private BigDecimal unbilledCharges = BigDecimal.ZERO;

    @Formula("(select IFNULL(sum(IFNULL(C.quantity,0) * IFNULL(C.charge, 0)),0) from charge C where C.order_id=id)")
    private BigDecimal totalCharge = BigDecimal.ZERO;

    @Formula("(exists (select * from charge C where C.order_id=id and C.disputed_at is not null))")
    private Boolean disputed = false;

    @Formula("(case status_id when 1 then 1 when 2 then case when DATEDIFF(expected_delivery_date, current_date()) < 0 then 4 when DATEDIFF(expected_delivery_date, current_date()) = 0 then 3  else 2 end else 0 end)")
    private Integer colorCode = new Integer(0);

    @Formula("(status_id)")
    private Integer statusId;

    public String getActualCarrierName()
    {
        if (actualCarrierName != null) {
            return actualCarrierName;
        } else if (service != null && service.getCarrier() != null) {
            return service.getCarrier()
                    .getName();
        } else {
            return null;
        }
    }

    public CustomerOrder()
    {
        super();
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public ZonedDateTime getSubmittedAt()
    {
        return asDate(submittedAt);
    }

    public void setSubmittedAt(Date submittedAt)
    {
        this.submittedAt = submittedAt;
    }

    public BigDecimal getQuotedFuelCost()
    {
        return quotedFuelCost;
    }

    public void setQuotedFuelCost(BigDecimal quotedFuelCost)
    {
        this.quotedFuelCost = quotedFuelCost;
    }

    public Long getPackageId()
    {
        return packageId;
    }

    public void setPackageId(Long packageId)
    {
        this.packageId = packageId;
    }

    public String getOriginCloseTimeM()
    {
        return originCloseTimeM;
    }

    public void setOriginCloseTimeM(String originCloseTimeM)
    {
        this.originCloseTimeM = originCloseTimeM;
    }

    public Long getShipToId()
    {
        return getShipTo() == null ? null : getShipTo().getId();
    }

    public Float getMarkupPerc()
    {
        return markupPerc;
    }

    public void setMarkupPerc(Float markupPerc)
    {
        this.markupPerc = markupPerc;
    }

    public Integer getCommStatus()
    {
        return commStatus;
    }

    public void setCommStatus(Integer commStatus)
    {
        this.commStatus = commStatus;
    }

    public String getBolId()
    {
        return bolId;
    }

    public void setBolId(String bolId)
    {
        this.bolId = bolId;
    }

    public Long getStatusId()
    {
        if (orderStatus == null) {
            return null;
        } else {
            return orderStatus.getId();
        }
    }

    public Double getOperatingCostPerc()
    {
        return operatingCostPerc;
    }

    public void setOperatingCostPerc(Double operatingCostPerc)
    {
        this.operatingCostPerc = operatingCostPerc;
    }

    public Float getDistributionQuote()
    {
        return distributionQuote;
    }

    public void setDistributionQuote(Float distributionQuote)
    {
        this.distributionQuote = distributionQuote;
    }

    public Integer getIsFromMfToF()
    {
        return isFromMfToF;
    }

    public void setIsFromMfToF(Integer isFromMfToF)
    {
        this.isFromMfToF = isFromMfToF;
    }

    public Integer getInsidePickup()
    {
        return insidePickup;
    }

    public void setInsidePickup(Integer insidePickup)
    {
        this.insidePickup = insidePickup;
    }

    public String getJobNumber()
    {
        return jobNumber;
    }

    public void setJobNumber(String jobNumber)
    {
        this.jobNumber = jobNumber;
    }

    public BigDecimal getQuotedTotalCharge()
    {
        return quotedTotalCharge;
    }

    public void setQuotedTotalCharge(BigDecimal quotedTotalCharge)
    {
        this.quotedTotalCharge = quotedTotalCharge;
    }

    public Integer getHomelandSecurity()
    {
        return homelandSecurity;
    }

    public void setHomelandSecurity(Integer homelandSecurity)
    {
        this.homelandSecurity = homelandSecurity;
    }

    public Long getShipFromId()
    {
        return getShipFrom() == null ? null : getShipFrom().getId();
    }

    public Integer getFrozen()
    {
        return frozen;
    }

    public void setFrozen(Integer frozen)
    {
        this.frozen = frozen;
    }

    public String getPodName()
    {
        return podName;
    }

    public void setPodName(String podName)
    {
        this.podName = podName;
    }

    public String getMasterTrackingNum1()
    {
        return masterTrackingNum1;
    }

    public void setMasterTrackingNum1(String masterTrackingNum1)
    {
        this.masterTrackingNum1 = masterTrackingNum1;
    }

    public Integer getExcessLength()
    {
        return excessLength;
    }

    public void setExcessLength(Integer excessLength)
    {
        this.excessLength = excessLength;
    }

    public BigDecimal getQuotedTotalCost()
    {
        return quotedTotalCost;
    }

    public void setQuotedTotalCost(BigDecimal quotedTotalCost)
    {
        this.quotedTotalCost = quotedTotalCost;
    }

    public Float getCodValue()
    {
        return new Float(0);
    }

    public void setCodValue(Float codValue)
    {
        this.codValue = codValue;
    }

    public Integer getShipmentDensity()
    {
        return shipmentDensity;
    }

    public void setShipmentDensity(Integer shipmentDensity)
    {
        this.shipmentDensity = shipmentDensity;
    }

    public String getTenderErrorMessage()
    {
        return tenderErrorMessage;
    }

    public void setTenderErrorMessage(String tenderErrorMessage)
    {
        this.tenderErrorMessage = tenderErrorMessage;
    }

    public BigDecimal getTranFee()
    {
        return tranFee;
    }

    public void setTranFee(BigDecimal tranFee)
    {
        this.tranFee = tranFee;
    }

    public String getReferenceName()
    {
        return referenceName;
    }

    public void setReferenceName(String referenceName)
    {
        this.referenceName = referenceName;
    }

    public Float getPartnerMarkup()
    {
        return partnerMarkup;
    }

    public void setPartnerMarkup(Float partnerMarkup)
    {
        this.partnerMarkup = partnerMarkup;
    }

    public BigDecimal getFreightTariff()
    {
        return freightTariff;
    }

    public void setFreightTariff(BigDecimal freightTariff)
    {
        this.freightTariff = freightTariff;
    }

    public ZonedDateTime getInvoiceDate()
    {
        return asDate(invoiceDate);
    }

    public void setInvoiceDate(Date invoiceDate)
    {
        this.invoiceDate = invoiceDate;
    }

    public BigDecimal getFuelTariff()
    {
        return fuelTariff;
    }

    public void setFuelTariff(BigDecimal fuelTariff)
    {
        this.fuelTariff = fuelTariff;
    }

    public Float getCurrencyRate()
    {
        return currencyRate;
    }

    public void setCurrencyRate(Float currencyRate)
    {
        this.currencyRate = currencyRate;
    }

    public String getDangerousGoods()
    {
        return dangerousGoods;
    }

    public void setDangerousGoods(String dangerousGoods)
    {
        this.dangerousGoods = dangerousGoods;
    }

    public String getInternalNotes()
    {
        return internalNotes;
    }

    public void setInternalNotes(String internalNotes)
    {
        this.internalNotes = internalNotes;
    }

    @Override
    public ZonedDateTime getUpdatedAt()
    {
        return asDate(updatedAt);
    }

    @Override
    public void setUpdatedAt(Date updatedAt)
    {
        this.updatedAt = updatedAt;
    }

    public Float getTotActWgt()
    {
        return totActWgt;
    }

    public void setTotActWgt(Float totActWgt)
    {
        this.totActWgt = totActWgt;
    }

    public Integer getCurrency()
    {
        return currency;
    }

    public void setCurrency(Integer currency)
    {
        this.currency = currency;
    }

    public String getTrackingUrl()
    {
        return trackingUrl;
    }

    public void setTrackingUrl(String trackingUrl)
    {
        this.trackingUrl = trackingUrl;
    }

    public Integer getSortSegregate()
    {
        return sortSegregate;
    }

    public void setSortSegregate(Integer sortSegregate)
    {
        this.sortSegregate = sortSegregate;
    }

    public String getModeTransport()
    {
        return modeTransport;
    }

    public void setModeTransport(String modeTransport)
    {
        this.modeTransport = modeTransport;
    }

    public Integer getMarkAndTagFreight()
    {
        return markAndTagFreight;
    }

    public void setMarkAndTagFreight(Integer markAndTagFreight)
    {
        this.markAndTagFreight = markAndTagFreight;
    }

    public String getMfwBookingKey()
    {
        return mfwBookingKey;
    }

    public void setMfwBookingKey(String mfwBookingKey)
    {
        this.mfwBookingKey = mfwBookingKey;
    }

    public String getCarrierPickUpConf()
    {
        return carrierPickUpConf;
    }

    public void setCarrierPickUpConf(String carrierPickUpConf)
    {
        this.carrierPickUpConf = carrierPickUpConf;
    }

    public Long getCommDriverId()
    {
        return commDriverId;
    }

    public void setCommDriverId(Long commDriverId)
    {
        this.commDriverId = commDriverId;
    }

    public Boolean getIsEManifested()
    {
        return isEManifested;
    }

    public void setIsEManifested(Boolean isEManifested)
    {
        this.isEManifested = isEManifested;
    }

    public String getProofOfDelivery()
    {
        return proofOfDelivery;
    }

    public void setProofOfDelivery(String proofOfDelivery)
    {
        this.proofOfDelivery = proofOfDelivery;
    }

    public Integer getCustomesFreight()
    {
        return customesFreight;
    }

    public void setCustomesFreight(Integer customesFreight)
    {
        this.customesFreight = customesFreight;
    }

    public BigDecimal getGrossProfit()
    {
        return grossProfit;
    }

    public void setGrossProfit(BigDecimal grossProfit)
    {
        this.grossProfit = grossProfit;
    }

    public Long getDestinationTerminal()
    {
        return destinationTerminal;
    }

    public void setDestinationTerminal(Long destinationTerminal)
    {
        this.destinationTerminal = destinationTerminal;
    }

    public String getPodFileName()
    {
        return podFileName;
    }

    public void setPodFileName(String podFileName)
    {
        this.podFileName = podFileName;
    }

    public Integer getTotActQuantity()
    {
        return totActQuantity;
    }

    public void setTotActQuantity(Integer totActQuantity)
    {
        this.totActQuantity = totActQuantity;
    }

    public Integer getHold()
    {
        return hold;
    }

    public void setHold(Integer hold)
    {
        this.hold = hold;
    }

    public String getCodPayment()
    {
        return codPayment;
    }

    public void setCodPayment(String codPayment)
    {
        this.codPayment = codPayment;
    }

    public BigDecimal getCommissionableAmount()
    {
        return commissionableAmount;
    }

    public void setCommissionableAmount(BigDecimal commissionableAmount)
    {
        this.commissionableAmount = commissionableAmount;
    }

    public Float getInsuranceValue3rd()
    {
        return insuranceValue3rd;
    }

    public void setInsuranceValue3rd(Float insuranceValue3rd)
    {
        this.insuranceValue3rd = insuranceValue3rd;
    }

    public Long getPackageTypeId()
    {
        return packageType == null ? null : packageType.getId();
    }

    @JsonIgnore
    public PackageType.Type getPackageTypeCode() throws Exception
    {
        return packageType == null ? PackageType.Type.PACKAGE_ENV : packageType.getTypeCode();
    }

    @JsonIgnore
    public boolean isLTL()
    {
        try {
            return getPackageTypeCode() == PackageType.Type.PACKAGE_PALLET;
        } catch (Exception e) {
            return false;
        }
    }

    public PackageType getPackageType()
    {
        return packageType;
    }

    public void setPackageType(PackageType packageType)
    {
        this.packageType = packageType;
    }

    public Integer getWaitTime()
    {
        return waitTime;
    }

    public void setWaitTime(Integer waitTime)
    {
        this.waitTime = waitTime;
    }

    public ZonedDateTime getPodTimestamp()
    {
        return asDate(podTimestamp);
    }

    public void setPodTimestamp(Date podTimestamp)
    {
        this.podTimestamp = podTimestamp;
    }

    public String getServiceName()
    {
        return serviceName == null ? (service == null ? null : service.getName()) : serviceName;
    }

    public void setServiceName(String serviceName)
    {
        this.serviceName = serviceName;
    }

    public BigDecimal getFuelSurcharge()
    {
        return fuelSurcharge;
    }

    public void setFuelSurcharge(BigDecimal fuelSurcharge)
    {
        this.fuelSurcharge = fuelSurcharge;
    }

    public Boolean getEdiVerified()
    {
        return ediVerified;
    }

    public void setEdiVerified(Boolean ediVerified)
    {
        this.ediVerified = ediVerified;
    }

    public InsuranceType getInsuranceType()
    {
        return InsuranceType.getType(insuranceType);
    }

    @JsonDeserialize(using = InsuranceTypeDeserializer.class)
    public void setInsuranceType(InsuranceType insuranceType)
    {
        this.insuranceType = insuranceType.getValue();
    }

    public BigDecimal getTotalTariff()
    {
        return totalTariff;
    }

    public void setTotalTariff(BigDecimal totalTariff)
    {
        this.totalTariff = totalTariff;
    }

    public Integer getOriginalMarkdownType()
    {
        return originalMarkdownType;
    }

    public void setOriginalMarkdownType(Integer originalMarkdownType)
    {
        this.originalMarkdownType = originalMarkdownType;
    }

    public Long getEshipperOrderId()
    {
        return eshipperOrderId;
    }

    public void setEshipperOrderId(Long eshipperOrderId)
    {
        this.eshipperOrderId = eshipperOrderId;
    }

    public String getPerCubicFeet()
    {
        return perCubicFeet;
    }

    public void setPerCubicFeet(String perCubicFeet)
    {
        this.perCubicFeet = perCubicFeet;
    }

    public BigDecimal getAdjustment()
    {
        return adjustment;
    }

    public void setAdjustment(BigDecimal adjustment)
    {
        this.adjustment = adjustment;
    }

    public String getInvoiceStatus()
    {
        return invoiceStatus;
    }

    public Integer getTransitDays()
    {
        return transitDays;
    }

    public void setTransitDays(Integer transitDays)
    {
        this.transitDays = transitDays;
    }

    public String getReference3()
    {
        return reference3;
    }

    public void setReference3(String reference3)
    {
        this.reference3 = reference3;
    }

    public Long getDistributionServiceId()
    {
        return distributionServiceId;
    }

    public void setDistributionServiceId(Long distributionServiceId)
    {
        this.distributionServiceId = distributionServiceId;
    }

    public Long getParamServiceId()
    {
        return paramServiceId;
    }

    public void setParamServiceId(Long paramServiceId)
    {
        this.paramServiceId = paramServiceId;
    }

    public String getExpectedDeliveryDate()
    {
        return expectedDeliveryDate;
    }

    public void setExpectedDeliveryDate(String expectedDeliveryDate)
    {
        this.expectedDeliveryDate = expectedDeliveryDate;
    }

    public String getReference2()
    {
        return reference2;
    }

    public void setReference2(String reference2)
    {
        this.reference2 = reference2;
    }

    public String getCodPinNumber()
    {
        return codPinNumber;
    }

    public void setCodPinNumber(String codPinNumber)
    {
        this.codPinNumber = codPinNumber;
    }

    public Integer getReturnService()
    {
        return returnService;
    }

    public void setReturnService(Integer returnService)
    {
        this.returnService = returnService;
    }

    public Boolean getHasBeenEdited()
    {
        return hasBeenEdited;
    }

    public void setHasBeenEdited(Boolean hasBeenEdited)
    {
        this.hasBeenEdited = hasBeenEdited;
    }

    public Integer getCanadianCustoms()
    {
        return canadianCustoms;
    }

    public void setCanadianCustoms(Integer canadianCustoms)
    {
        this.canadianCustoms = canadianCustoms;
    }

    public String getReferenceCode()
    {
        return referenceCode;
    }

    public void setReferenceCode(String referenceCode)
    {
        this.referenceCode = referenceCode;
    }

    public String getNotes1()
    {
        return notes1;
    }

    public void setNotes1(String notes1)
    {
        this.notes1 = notes1;
    }

    public String getNotes2()
    {
        return notes2;
    }

    public void setNotes2(String notes2)
    {
        this.notes2 = notes2;
    }

    public Integer getInbondFee()
    {
        return inbondFee;
    }

    public void setInbondFee(Integer inbondFee)
    {
        this.inbondFee = inbondFee;
    }

    public Integer getLoadFreightAtPickup()
    {
        return loadFreightAtPickup;
    }

    public void setLoadFreightAtPickup(Integer loadFreightAtPickup)
    {
        this.loadFreightAtPickup = loadFreightAtPickup;
    }

    public String getReference2Name()
    {
        return reference2Name;
    }

    public void setReference2Name(String reference2Name)
    {
        this.reference2Name = reference2Name;
    }

    public Boolean getMerged()
    {
        return merged;
    }

    public void setMerged(Boolean merged)
    {
        this.merged = merged;
    }

    public Integer getProtectivePalletCover()
    {
        return protectivePalletCover;
    }

    public void setProtectivePalletCover(Integer protectivePalletCover)
    {
        this.protectivePalletCover = protectivePalletCover;
    }

    public BigDecimal getQuotedBaseCost()
    {
        return quotedBaseCost;
    }

    public void setQuotedBaseCost(BigDecimal quotedBaseCost)
    {
        this.quotedBaseCost = quotedBaseCost;
    }

    public String getFromAirlineName()
    {
        return fromAirlineName;
    }

    public void setFromAirlineName(String fromAirlineName)
    {
        this.fromAirlineName = fromAirlineName;
    }

    public ZonedDateTime getShipDate()
    {
        return asDate(shipDate);
    }

    public void setShipDate(Date shipDate)
    {
        this.shipDate = shipDate;
    }

    public BigDecimal getQuotedBaseCharge()
    {
        return quotedBaseCharge;
    }

    public void setQuotedBaseCharge(BigDecimal quotedBaseCharge)
    {
        this.quotedBaseCharge = quotedBaseCharge;
    }

    public BigDecimal getFuelCost()
    {
        return fuelCost;
    }

    public void setFuelCost(BigDecimal fuelCost)
    {
        this.fuelCost = fuelCost;
    }

    public Long getLoadId()
    {
        return loadId;
    }

    public void setLoadId(Long loadId)
    {
        this.loadId = loadId;
    }

    public Long getAgentId()
    {
        if (getAgent() != null) {
            return getAgent().getId();
        } else {
            return null;
        }
    }

    public ZonedDateTime getScheduledShipDate()
    {
        return scheduledShipDate == null ? asDate(shipDate) : asDate(scheduledShipDate);
    }

    public void setScheduledShipDate(Date scheduledShipDate)
    {
        this.shipDate = scheduledShipDate;
    }

    public String getPackageTypeName()
    {
        return packageTypeName;
    }

    public void setPackageTypeName(String packageTypeName)
    {
        this.packageTypeName = packageTypeName;
    }

    public Integer getFreezeProtect()
    {
        return freezeProtect;
    }

    public void setFreezeProtect(Integer freezeProtect)
    {
        this.freezeProtect = freezeProtect;
    }

    public Integer getType()
    {
        return type;
    }

    public void setType(Integer type)
    {
        this.type = type;
    }

    public Integer getPierCharge()
    {
        return pierCharge;
    }

    public void setPierCharge(Integer pierCharge)
    {
        this.pierCharge = pierCharge;
    }

    public Integer getGuaranteeCharge()
    {
        return guaranteeCharge;
    }

    public void setGuaranteeCharge(Integer guaranteeCharge)
    {
        this.guaranteeCharge = guaranteeCharge;
    }

    public Integer getInsideDelivery()
    {
        return insideDelivery;
    }

    public void setInsideDelivery(Integer insideDelivery)
    {
        this.insideDelivery = insideDelivery;
    }

    public Integer getPaymentTypeId()
    {
        return paymentTypeId;
    }

    public void setPaymentTypeId(Integer paymentTypeId)
    {
        this.paymentTypeId = paymentTypeId;
    }

    public Integer getMilitaryBaseDelivery()
    {
        return militaryBaseDelivery;
    }

    public void setMilitaryBaseDelivery(Integer militaryBaseDelivery)
    {
        this.militaryBaseDelivery = militaryBaseDelivery;
    }

    public String getInvoiceNumber()
    {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber)
    {
        this.invoiceNumber = invoiceNumber;
    }

    public String getManifestNumber()
    {
        return manifestNumber;
    }

    public void setManifestNumber(String manifestNumber)
    {
        this.manifestNumber = manifestNumber;
    }

    public String getMfwConnectionKey()
    {
        return mfwConnectionKey;
    }

    public void setMfwConnectionKey(String mfwConnectionKey)
    {
        this.mfwConnectionKey = mfwConnectionKey;
    }

    public Float getInsuredAmount()
    {
        return insuredAmount;
    }

    public void setInsuredAmount(Float insuredAmount)
    {
        this.insuredAmount = insuredAmount;
    }

    public ZonedDateTime getDeletedAt()
    {
        return asDate(deletedAt);
    }

    public void setDeletedAt(Date deletedAt)
    {
        this.deletedAt = deletedAt;
    }

    public Integer getProtectiveDrumCover()
    {
        return protectiveDrumCover;
    }

    public void setProtectiveDrumCover(Integer protectiveDrumCover)
    {
        this.protectiveDrumCover = protectiveDrumCover;
    }

    public String getReference3Name()
    {
        return reference3Name;
    }

    public void setReference3Name(String reference3Name)
    {
        this.reference3Name = reference3Name;
    }

    public Integer getLimitedAccess()
    {
        return limitedAccess;
    }

    public void setLimitedAccess(Integer limitedAccess)
    {
        this.limitedAccess = limitedAccess;
    }

    public Boolean getVehicleFull()
    {
        return vehicleFull;
    }

    public void setVehicleFull(Boolean vehicleFull)
    {
        this.vehicleFull = vehicleFull;
    }

    public Integer getHeatedService()
    {
        return heatedService;
    }

    public void setHeatedService(Integer heatedService)
    {
        this.heatedService = heatedService;
    }

    public Integer getDimType()
    {
        return dimType;
    }

    public void setDimType(Integer dimType)
    {
        this.dimType = dimType;
    }

    public BigDecimal getAffiliateFee()
    {
        return affiliateFee;
    }

    public void setAffiliateFee(BigDecimal affiliateFee)
    {
        this.affiliateFee = affiliateFee;
    }

    public Claim getClaim()
    {
        return claim;
    }

    @Override
    public ZonedDateTime getCreatedAt()
    {
        return asDate(createdAt);
    }

    @Override
    public void setCreatedAt(final Date createdAt)
    {
        this.createdAt = createdAt;
    }

    public String getTenderStatus()
    {
        return tenderStatus;
    }

    public void setTenderStatus(String tenderStatus)
    {
        this.tenderStatus = tenderStatus;
    }

    public byte[] getPodFile()
    {
        return podFile;
    }

    public void setPodFile(byte[] podFile)
    {
        this.podFile = podFile;
    }

    public Integer getExpectedTransit()
    {
        return expectedTransit;
    }

    public void setExpectedTransit(Integer expectedTransit)
    {
        this.expectedTransit = expectedTransit;
    }

    public String getMasterTrackingNum()
    {
        return masterTrackingNum;
    }

    public void setMasterTrackingNum(String masterTrackingNum)
    {
        this.masterTrackingNum = masterTrackingNum;
    }

    public Integer getBillingStatus()
    {
        return billingStatus;
    }

    public void setBillingStatus(Integer billingStatus)
    {
        this.billingStatus = billingStatus;
    }

    public BigDecimal getActualBaseCharge()
    {
        return actualBaseCharge;
    }

    public void setActualBaseCharge(BigDecimal actualBaseCharge)
    {
        this.actualBaseCharge = actualBaseCharge;
    }

    public Integer getUnloadFreightAtDel()
    {
        return unloadFreightAtDel;
    }

    public void setUnloadFreightAtDel(Integer unloadFreightAtDel)
    {
        this.unloadFreightAtDel = unloadFreightAtDel;
    }

    public Long getCreator()
    {
        return creator;
    }

    public void setCreator(Long creator)
    {
        this.creator = creator;
    }

    public BigDecimal getReconcilAdjustment()
    {
        return reconcilAdjustment;
    }

    public void setReconcilAdjustment(BigDecimal reconcilAdjustment)
    {
        this.reconcilAdjustment = reconcilAdjustment;
    }

    public ZonedDateTime getDateOfDelivery()
    {
        return asDate(dateOfDelivery);
    }

    public void setDateOfDelivery(Date dateOfDelivery)
    {
        this.dateOfDelivery = dateOfDelivery;
    }

    public Integer getVehicleType()
    {
        return vehicleType;
    }

    public void setVehicleType(Integer vehicleType)
    {
        this.vehicleType = vehicleType;
    }

    public String getDestinationCloseTimeH()
    {
        return destinationCloseTimeH;
    }

    public void setDestinationCloseTimeH(String destinationCloseTimeH)
    {
        this.destinationCloseTimeH = destinationCloseTimeH;
    }

    public String getDestinationCloseTimeM()
    {
        return destinationCloseTimeM;
    }

    public void setDestinationCloseTimeM(String destinationCloseTimeM)
    {
        this.destinationCloseTimeM = destinationCloseTimeM;
    }

    public Integer getDeliveryAppt()
    {
        return deliveryAppt;
    }

    public void setDeliveryAppt(Integer deliveryAppt)
    {
        this.deliveryAppt = deliveryAppt;
    }

    public Long getParentEdiwoId()
    {
        return parentEdiwoId;
    }

    public void setParentEdiwoId(Long parentEdiwoId)
    {
        this.parentEdiwoId = parentEdiwoId;
    }

    public Long getCustomerId()
    {
        Long result = customerId;

        if (result == null && getCustomer() != null) {
            result = getCustomer().getId();
        }

        return result;
    }

    public void setCustomerId(Long customerId)
    {
        this.customerId = customerId;
    }

    public Long getAssignDriverId()
    {
        return assignDriverId;
    }

    public void setAssignDriverId(Long assignDriverId)
    {
        this.assignDriverId = assignDriverId;
    }

    public BigDecimal getQuotedFuelSurcharge()
    {
        return quotedFuelSurcharge;
    }

    public void setQuotedFuelSurcharge(BigDecimal quotedFuelSurcharge)
    {
        this.quotedFuelSurcharge = quotedFuelSurcharge;
    }

    public Float getChargedWeight()
    {
        return chargedWeight;
    }

    public void setChargedWeight(Float chargedWeight)
    {
        this.chargedWeight = chargedWeight;
    }

    public Long getPrevInvoiceNumber()
    {
        return prevInvoiceNumber;
    }

    public void setPrevInvoiceNumber(Long prevInvoiceNumber)
    {
        this.prevInvoiceNumber = prevInvoiceNumber;
    }

    public Integer getPaperManifestStatus()
    {
        return paperManifestStatus;
    }

    public void setPaperManifestStatus(Integer paperManifestStatus)
    {
        this.paperManifestStatus = paperManifestStatus;
    }

    // @JsonSerialize(converter = PickupConverter.class)
    public Pickup getScheduledPickup()
    {
        return scheduledPickup;
    }

    public void setScheduledPickup(Pickup scheduledPickup)
    {
        if (this.scheduledPickup == null) {
            this.scheduledPickup = scheduledPickup;
        } else {
            this.scheduledPickup.updateFrom(scheduledPickup);
        }

        if (scheduledPickup.getPickupDate() == null) {
            setScheduledShipDate(null);
        } else {
            setScheduledShipDate(Date.from(scheduledPickup.getPickupDate()
                    .toInstant()));
        }
    }

    public boolean hasScheduledPickupDate()
    {
        return scheduledPickup != null && scheduledPickup.getPickupDate() != null;
    }

    public BigDecimal getCommAmount()
    {
        return commAmount;
    }

    public void setCommAmount(BigDecimal commAmount)
    {
        this.commAmount = commAmount;
    }

    public Integer getExibitionSite()
    {
        return exibitionSite;
    }

    public void setExibitionSite(Integer exibitionSite)
    {
        this.exibitionSite = exibitionSite;
    }

    public Boolean getIsInsuranceManifested()
    {
        return isInsuranceManifested;
    }

    public void setIsInsuranceManifested(Boolean isInsuranceManifested)
    {
        this.isInsuranceManifested = isInsuranceManifested;
    }

    public Long getEshipperOid()
    {
        return eshipperOid;
    }

    public void setEshipperOid(Long eshipperOid)
    {
        this.eshipperOid = eshipperOid;
    }

    public Integer getInsuranceCurrency()
    {
        return insuranceCurrency;
    }

    public void setInsuranceCurrency(Integer insuranceCurrency)
    {
        this.insuranceCurrency = insuranceCurrency;
    }

    public Integer getCurCode()
    {
        return curCode;
    }

    public void setCurCode(Integer curCode)
    {
        this.curCode = curCode;
    }

    public Boolean getAfterHours()
    {
        return afterHours;
    }

    public void setAfterHours(Boolean afterHours)
    {
        this.afterHours = afterHours;
    }

    public BigDecimal getOperatingCost()
    {
        return operatingCost;
    }

    public void setOperatingCost(BigDecimal operatingCost)
    {
        this.operatingCost = operatingCost;
    }

    public Integer getPickupNum()
    {
        return pickupNum;
    }

    public void setPickupNum(Integer pickupNum)
    {
        this.pickupNum = pickupNum;
    }

    public String getToAirlineName()
    {
        return toAirlineName;
    }

    public void setToAirlineName(String toAirlineName)
    {
        this.toAirlineName = toAirlineName;
    }

    public BigDecimal getBaseCost()
    {
        return baseCost;
    }

    public void setBaseCost(BigDecimal baseCost)
    {
        this.baseCost = baseCost;
    }

    public ZonedDateTime getQuotedAt()
    {
        return asDate(quotedAt);
    }

    public void setQuotedAt(Date quotedAt)
    {
        this.quotedAt = quotedAt;
    }

    public Integer getCrossBorderFee()
    {
        return crossBorderFee;
    }

    public void setCrossBorderFee(Integer crossBorderFee)
    {
        this.crossBorderFee = crossBorderFee;
    }

    public String getSignatureRequired()
    {
        return (new Integer(1)).equals(signatureRequired) ? "Yes" : "No";
    }

    public void setSignatureRequired(String signatureRequired)
    {
        this.signatureRequired = "yes".equalsIgnoreCase(signatureRequired) ? 1 : 0;
    }

    public Integer getQuoteStatus()
    {
        return quoteStatus;
    }

    public void setQuoteStatus(Integer quoteStatus)
    {
        this.quoteStatus = quoteStatus;
    }

    public Long getCarrierId()
    {
        return carrier == null ? null : carrier.getId();
    }

    public Float getInsuranceValue()
    {
        return insuranceValue;
    }

    public void setInsuranceValue(Float insuranceValue)
    {
        this.insuranceValue = insuranceValue;
    }

    public Integer getSingleShipment()
    {
        return singleShipment;
    }

    public void setSingleShipment(Integer singleShipment)
    {
        this.singleShipment = singleShipment;
    }

    public Integer getSubType()
    {
        return subType;
    }

    public void setSubType(Integer subType)
    {
        this.subType = subType;
    }

    public Long getServiceId()
    {
        return service == null ? null : service.getId();
    }

    public BigDecimal getBaseCharge()
    {
        return baseCharge;
    }

    public void setBaseCharge(BigDecimal baseCharge)
    {
        this.baseCharge = baseCharge;
    }

    public Float getCustomerMarkdown()
    {
        return customerMarkdown;
    }

    public void setCustomerMarkdown(Float customerMarkdown)
    {
        this.customerMarkdown = customerMarkdown;
    }

    public Long getQuantity()
    {
        return quantity;
    }

    public void setQuantity(Long quantity)
    {
        this.quantity = quantity;
    }

    public String getPickupTimeH()
    {
        return pickupTimeH;
    }

    public void setPickupTimeH(String pickupTimeH)
    {
        this.pickupTimeH = pickupTimeH;
    }

    public String getPickupTimeM()
    {
        return pickupTimeM;
    }

    public void setPickupTimeM(String pickupTimeM)
    {
        this.pickupTimeM = pickupTimeM;
    }

    public Integer getEditable()
    {
        return editable;
    }

    public void setEditable(Integer editable)
    {
        this.editable = editable;
    }

    public Float getCustomerMarkup()
    {
        return customerMarkup;
    }

    public void setCustomerMarkup(Float customerMarkup)
    {
        this.customerMarkup = customerMarkup;
    }

    public Integer getCustomerMarkupType()
    {
        return customerMarkupType;
    }

    public void setCustomerMarkupType(Integer customerMarkupType)
    {
        this.customerMarkupType = customerMarkupType;
    }

    public String getSpecialEquipment()
    {
        return specialEquipment;
    }

    public void setSpecialEquipment(String specialEquipment)
    {
        this.specialEquipment = specialEquipment;
    }

    public Float getActualWeight()
    {
        return actualWeight;
    }

    public void setActualWeight(Float actualWeight)
    {
        this.actualWeight = actualWeight;
    }

    public Boolean getUpdateStatus()
    {
        return updateStatus;
    }

    public void setUpdateStatus(Boolean updateStatus)
    {
        this.updateStatus = updateStatus;
    }

    public Long getReturnOrderId()
    {
        return returnOrderId;
    }

    public void setReturnOrderId(Long returnOrderId)
    {
        this.returnOrderId = returnOrderId;
    }

    @RestResource(exported = false)
    @JsonIgnore
    public Customer getCustomer()
    {
        return customer;
    }

    public void setCustomer(Customer customer)
    {
        this.customer = customer;
    }

    @RestResource(exported = false)
    public ShippingAddress getShipTo()
    {
        return shipTo;
    }

    @RestResource(exported = false)
    public String getShipToPostalCode()
    {
        return shipTo == null ? null : shipTo.getPostalCode();
    }

    @RestResource(exported = false)
    public ShippingAddress getShipFrom()
    {
        return shipFrom;
    }

    @RestResource(exported = false)
    public String getShipFromPostalCode()
    {
        return shipFrom == null ? null : shipFrom.getPostalCode();
    }

    @RestResource(exported = false)
    public List<Package> getPackages()
    {
        return packages;
    }

    public void setPackages(List<Package> packages)
    {
        this.packages = packages;
    }

    @RestResource(exported = false)
    public List<Pallet> getPallets()
    {
        return pallets;
    }

    public void setPallets(List<Pallet> pallets)
    {
        this.pallets = pallets;
    }

    @RestResource(exported = false)
    public OrderStatus getOrderStatus()
    {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus)
    {
        this.orderStatus = orderStatus;

        if (orderStatus != null) {
            this.orderStatusName = orderStatus.getName();
        }
    }

    public String getOrderStatusName()
    {
        if (orderStatusName == null && getOrderStatus() != null) {
            orderStatusName = getOrderStatus().getName();
        }

        return orderStatusName;
    }

    // Alternate name for status name
    public String getStatusName()
    {
        return orderStatusName;
    }

    public void setStatusName(String orderStatusName)
    {
        this.orderStatusName = orderStatusName;
    }

    public String getDestinationCity()
    {
        return destinationCity;
    }

    public String getOriginCity()
    {
        return originCity;
    }

    public String getDestinationPostalCode()
    {
        return destinationPostalCode;
    }

    public String getOriginPostalCode()
    {
        return originPostalCode;
    }

    public String getDestinationCompany()
    {
        return destinationCompany;
    }

    public String getOriginCompany()
    {
        return originCompany;
    }

    public List<LoggedEvent> getLoggedEvents()
    {
        return loggedEvents;
    }

    public void setLoggedEvents(List<LoggedEvent> loggedEvents)
    {
        this.loggedEvents = loggedEvents;
    }

    public String getCarrierName()
    {
        return carrierName;
    }

    public Carrier getCarrier()
    {
        return carrier;
    }

    public void setCarrier(Carrier carrier)
    {
        this.carrier = carrier;
    }

    public Service getService()
    {
        return service;
    }

    public void setService(Service service)
    {
        this.service = service;
    }

    public Long getDistributionGroupId()
    {
        return distributionGroupId;
    }

    public void setDistributionGroupId(Long distributionGroupId)
    {
        this.distributionGroupId = distributionGroupId;
    }

    public Integer getDestinationCount()
    {
        return destinationCount;
    }

    public List<OrderAccessorials> getAccessorials()
    {
        return accessorialServices;
    }

    public void setAccessorials(List<OrderAccessorials> accessorialServices)
    {
        this.accessorialServices.clear();

        for (OrderAccessorials item : accessorialServices) {
            this.accessorialServices.add(item);
        }
    }

    @RestResource(exported = false)
    public List<String> getAccessorialServices()
    {
        List<String> services = new ArrayList<String>();

        if (getAccessorials() != null) {
            for (OrderAccessorials service : getAccessorials()) {
                log.debug("ADDING SERVICE NAME TO LIST " + service);

                if (service.getService() != null) {
                    services.add(service.getService()
                            .getName());
                }
            }
        }

        return services;
    }

    public void setShipTo(ShippingAddress address)
    {
        shipTo = address;
    }

    public String toString()
    {
        return "O " + getId() + " " + getOrderStatusName();
    }

    public SelectedQuote getSelectedQuote()
    {
        return selectedQuote;
    }

    public void setSelectedQuote(SelectedQuote selectedQuote)
    {
        this.selectedQuote = selectedQuote;

        if (selectedQuote != null) {
            this.setService(selectedQuote.getService());
            this.setCarrier(selectedQuote.getService()
                    .getCarrier());
        }
    }

    public List<Charge> getCharges()
    {
        return charges;
    }

    public void clearCharges()
    {
        this.charges.clear();
    }

    public Agent getAgent()
    {
        return agent;
    }

    public void setAgent(Agent agent)
    {
        this.agent = agent;
    }

    public Charge newCharge(String description, BigDecimal rate, String currency)
    {
        return newCharge(description, rate, currency, null);
    }

    public Charge newCharge(String description, BigDecimal rate, String currency, OrderAccessorials accessorial)
    {
        Charge charge = new Charge();

        charge.setDescription(description);

        if (rate == null) {
            charge.setCost(BigDecimal.ZERO);
            charge.setCharge(BigDecimal.ZERO);
        } else {
            charge.setCost(rate.multiply(new BigDecimal(.8)));
            charge.setCharge(rate);
        }

        charge.setQuantity(1);
        charge.setCurrency(currency);
        charge.setAgent(agent);
        charge.setOrder(this);
        charge.setTaxable(!isTransborder());

        if (accessorial != null) {
            charge.setAccessorial(accessorial);
        }

        charge.setService(getSelectedQuote() == null ? null : getSelectedQuote().getService());

        // taxes
        // TODO some services are not taxable
        if (charge.getTaxable()) {
            for (ApplicableTax applicableTax : getCustomer().getApplicableTaxes()) {
                TaxDefinition definition = applicableTax.getTaxDefinition();
                charge.addTax(definition.getName(), definition.getTaxId(), definition.getRate());
            }
        }

        // TODO markup?

        log.debug("NEW CHARGE " + charge);

        if (charges.size() >= 0) {
            // Charges seem to be doubled if we don't ask for size first
            charges.add(charge);
        }

        return charge;
    }

    public void defaultAccessorialCharges()
    {
        log.debug("SETTING DEFAULT ACCESSORIAL CHARGES " + this);

        String currency = "CAN";

        for (OrderAccessorials accessorial : getAccessorials()) {
            AccessorialServices service = accessorial.getService();
            String description = service == null ? ""
                    : ((service.getDescription() == null || "".equals(service.getDescription())) ? service.getName()
                            : service.getDescription());

            newCharge(description, service.getRate(), currency, accessorial);
        }

        if (selectedQuote != null) {
            newCharge("Fuel Fee", selectedQuote.getFuelFee(), currency);
            newCharge("Insurance", selectedQuote.getInsurance(), currency);
            newCharge("Base Fee", selectedQuote.getBaseFee(), currency);
        }
    }

    public BigDecimal getUnbilledCharges()
    {
        return unbilledCharges;
    }

    public void setUnbilledCharges(BigDecimal unbilledCharges)
    {
        this.unbilledCharges = unbilledCharges;
    }

    public boolean hasBilledCharges()
    {
        return charges == null || charges.stream()
                .anyMatch(charge -> charge.isBilled());
    }

    @JsonIgnore
    public List<Invoice> getInvoices()
    {
        return invoices;
    }

    public void setInvoices(List<Invoice> invoices)
    {
        this.invoices = invoices;
    }

    public boolean isReadyForShipping()
    {
        return orderStatus != null && READY_FOR_SHIPPING == orderStatus.code();
    }

    public boolean isDraft()
    {
        return orderStatus != null && DRAFT == orderStatus.code();
    }

    public boolean isQuoted()
    {
        return orderStatus != null && QUOTED == orderStatus.code();
    }

    public boolean isBooked()
    {
        return orderStatus != null && BOOKED == orderStatus.code();
    }

    public boolean isCancelled()
    {
        return orderStatus != null && CANCELLED == orderStatus.code();
    }

    public boolean isDelivered()
    {
        return orderStatus != null && DELIVERED == orderStatus.code();
    }

    public boolean isPreDelivery()
    {
        return orderStatus != null && orderStatus.code() != DELIVERED && orderStatus.code() != DELETED
                && orderStatus.code() != CANCELLED;

    }

    public OrderStatusCode getStatusCode()
    {
        return orderStatus == null ? null : orderStatus.code();
    }

    public CustomerOrder reconcileCharges()
    {
        for (Charge charge : charges) {
            charge.setReconciled(true);
        }

        return this;
    }

    public Boolean getCustomWorkOrder()
    {
        return customWorkOrder;
    }

    public void setCustomWorkOrder(Boolean customWorkOrder)
    {
        this.customWorkOrder = customWorkOrder;
    }

    public BigDecimal getTotalCharge()
    {
        if (totalCharge == null) {
            return BigDecimal.ZERO;
        } else {
            return totalCharge;
        }
    }

    public void setTotalCharge(BigDecimal totalCharges)
    {
        this.totalCharge = totalCharges;
    }

    public String getInvoiceCurrency()
    {
        if (getCustomer() == null) {
            return null;
        } else {
            return getCustomer().getInvoiceCurrency();
        }
    }

    public BigDecimal getTotalCost()
    {
        return charges.stream()
                .map(charge -> charge.getCost())
                .reduce(BigDecimal.ZERO, (total, cost) -> total.add(cost));
    }

    public List<User> shipToNotifyUsers()
    {
        if (shipTo != null) {
            return shipTo.getNotifyUsers();
        } else {
            return new ArrayList<User>();
        }
    }

    public List<User> shipFromNotifyUsers()
    {
        if (shipFrom != null) {
            return shipFrom.getNotifyUsers();
        } else {
            return new ArrayList<User>();
        }
    }

    public Boolean isDisputed()
    {
        return disputed;
    }

    public CustomsInvoice getCustomsInvoice()
    {
        return customsInvoice;
    }

    public void setCustomsInvoice(CustomsInvoice customsInvoice)
    {
        if (customsInvoice != null) {
            customsInvoice.setOrder(this);
        }

        if (this.customsInvoice == null) {
            this.customsInvoice = customsInvoice;
        } else {
            this.customsInvoice.updateFrom(customsInvoice);
        }
    }

    public List<OrderDocument> getFiles()
    {
        return files;
    }

    public List<OrderDocument> removeFile(OrderDocument document) throws Exception
    {
        if (files != null) {
            int index = files.indexOf(document);

            if (index < 0) {
                throw new Exception("Document not in order list");
            }

            log.debug("ORDER DELETING DOCUMENT " + index + " " + document + " from " + files.size());

            files.remove(index);

            log.debug("ORDER DELETING DOCUMENT DONE " + files.size());
        } else {
            throw new Exception("No such document " + document.getId());
        }

        return files;
    }

    public boolean containsFile(OrderDocument document)
    {
        if (files == null) {
            return false;
        } else {
            return files.contains(document);
        }
    }

    public void addFile(OrderDocument file)
    {
        file.setOrder(this);
        files.add(file);
    }

    public void setFiles(ArrayList<OrderDocument> files)
    {
        files.clear();

        for (OrderDocument file : files) {
            addFile(file);
        }
    }

    public Claim submitClaim(UserRole role)
    {
        claim = new Claim();
        claim.setSubmittedBy(role);

        return claim;
    }

    public Claim submitClaim(UserRole role, BigDecimal amount, String currency, String reason)
    {
        claim = submitClaim(role);
        claim.setAmount(amount);
        claim.setReason(reason);
        claim.setCurrency(currency);

        return claim;
    }

    public void setColorCode()
    {
        // no op
    }

    public String getColorCode()
    {
        log.debug("GET COLOR CODE " + colorCode + " " + orderStatus);

        if (colorCode == null) {
            return null;
        }

        switch (colorCode) {
        case 1:
            // Dispatched
            return "orange";

        case 2:
            // in transit > 24 hours before delivery date - before today
            return "green";

        case 3:
            // in transit, < 24 hours before delivery date - today
            return "yellow";

        case 4:
            // in transit, past delivery date - overdue
            return "red";

        default:
            return null;
        }
    }

    public Boolean isTransborder()
    {
        ShippingAddress to = getShipTo();
        ShippingAddress from = getShipFrom();

        return to != null && from != null && to.getCountry() != null && !to.getCountry()
                .equals(from.getCountry());
    }

    public boolean getSaturdayDeliveryRequired()
    {
        // TODO Auto-generated method stub
        return false;
    }

    public double getTotalWeight()
    {
        // TODO Auto-generated method stub
        return 1;
    }

    public boolean isSaturdayPickup()
    {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean isHoldForPickupRequired()
    {
        // TODO Auto-generated method stub
        return false;
    }

    @JsonIgnore
    public List<OrderRateQuote> getQuotes()
    {
        return quotes;
    }

    public void addQuote(OrderRateQuote quote)
    {
        quote.setOrder(this);
        quotes.add(quote);
    }

    public Float getWeight()
    {
        try {
            if (getPackageTypeCode() == PackageType.Type.PACKAGE_PAK && pakWeight == null) {
                return (float) 1.0;
            }
        } catch (Exception e) {
            return (float) 1.0;
        }

        return pakWeight;
    }

    public void setWeight(Float weight)
    {
        log.debug("SETTING WEIGHT " + weight);
        this.pakWeight = weight;
    }

    public void updatePackages(List<Package> updates)
    {
        int n = packages.size();
        int m = updates.size();
        int i = 0;

        for (i = 0; i < n && i < m; i++) {
            log.debug("UPDATING PACKAGE " + packages.get(i) + " from " + updates.get(i));
            packages.get(i)
                    .update(updates.get(i));
        }

        for (; i < n; i++) {
            log.debug("REMOVING PACKAGE " + packages.get(i));
            packages.remove(i);
        }

        for (; i < m; i++) {
            log.debug("ADDING PACKAGE " + updates.get(i));
            packages.add(updates.get(i));
        }
    }

    public void updatePallets(List<Pallet> updates)
    {
        int n = pallets.size();
        int m = updates.size();
        int i = 0;

        for (i = 0; i < n && i < m; i++) {
            pallets.get(i)
                    .update(updates.get(i));
        }

        for (; i < n; i++) {
            pallets.remove(i);
        }

        for (; i < m; i++) {
            pallets.add(updates.get(i));
        }
    }

    public String getFromCountry()
    {
        if (shipFrom == null) {
            return null;
        } else {
            return shipFrom.getCountry();
        }
    }

    public String getToCountry()
    {
        if (shipTo == null) {
            return null;
        } else {
            return shipTo.getCountry();
        }
    }

    public BigDecimal getProfit()
    {
        BigDecimal profit = BigDecimal.ZERO;

        for (Charge charge : charges) {
            profit = profit.add(charge.getProfit());
        }

        return profit;
    }

    public Integer getNumberOfPieces() throws Exception
    {
        Integer pieces = 0;

        if (getPackageTypeCode() != null) {
            switch (getPackageTypeCode()) {
            case PACKAGE_ENV:
                pieces = 1;
                break;

            case PACKAGE_PAK:
                pieces = 1;
                break;

            case PACKAGE_PACKAGE:
                pieces = packages == null ? 0 : packages.size();
                break;

            case PACKAGE_PALLET:
                pieces = pallets == null ? 0 : pallets.size();
                break;
            }
        }

        return pieces;
    }

    public OrderAccessorials findAccessorial(String name)
    {
        return getAccessorials().stream()
                .filter(accessorial -> accessorial.getService()
                        .getName()
                        .equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    public DisputeInformation getDisputeInformation()
    {
        return disputeInformation;
    }

    public void setDisputeInformation(DisputeInformation disputeInformation)
    {
        this.disputeInformation = disputeInformation;
    }

    /**
     * Return true if the order is in a state where it may be cancelled.
     */
    public Boolean getCancellable()
    {
        switch (getStatusCode()) {
        case DELIVERED:
        case CANCELLED:
        case IN_TRANSIT:
        case WAITING_BORDER:
            return false;

        default:
            return true;

        }
    }

    /**
     * Return true if a pickup may be scheduled for the order
     */
    public Boolean canSchedulePickup()
    {
        // TODO - should check for existence of tracking url (number?)
        switch (getStatusCode()) {
        case READY_FOR_SHIPPING:
        case MISSED_PICKUP:
            return true;

        default:
            return false;

        }
    }

    /**
     * Return true if the order is in a state where manual status change is
     * allowed
     */
    public Boolean statusChangeable()
    {
        // TODO - should check for existence of tracking url (number?)
        switch (getStatusCode()) {
        case READY_FOR_SHIPPING:
        case SCHEDULED:
        case IN_TRANSIT:
        case WAITING_BORDER:
            return true;

        default:
            return false;

        }
    }

    /**
     * Return true if the order is in a state where it can be tracked.
     */
    public Boolean getTrackable()
    {
        // TODO - should check for existence of tracking url (number?)
        switch (getStatusCode()) {
        case DELIVERED:
        case CANCELLED:
        case IN_TRANSIT:
        case WAITING_BORDER:
            return true;

        default:
            return false;

        }
    }

    /**
     * Return true if a claim can be filed
     */
    public Boolean getClaimCanBeFiled()
    {
        if (this.claim != null) {
            // Can't file if we have a claim
            return false;
        }
        switch (getStatusCode()) {
        case IN_TRANSIT:
        case DELIVERED:
        case WAITING_BORDER:
        case MISSED_PICKUP:
        case EXCEPTION:
            return true;

        default:
            return false;

        }
    }

    /**
     * Return true if a claim can be filed
     */
    public Boolean isSchedulable()
    {
        switch (getStatusCode()) {
        case DRAFT:
        case SCHEDULED:
        case QUOTED:
        case BOOKED:
        case PREDISPATCHED:
        case READY_FOR_DISPATCH:
            return true;

        default:
            log.debug("NOT SCHEDULABLE " + this + " " + getStatusCode());
            return false;

        }
    }

    public ShippingLabels getShippingLabel()
    {
        return shippingLabel;
    }

    public void setShippingLabel(ShippingLabels shippingLabel)
    {
        this.shippingLabel = shippingLabel;
        shippingLabel.setOrder(this);
    }

    public void addShippingLabel(byte[] image)
    {
        if (image == null) {
            shippingLabel = null;
        } else {
            shippingLabel = new ShippingLabels();
            shippingLabel.setOrder(this);
            shippingLabel.setLabel(image);
        }
    }

    public boolean hasShippingLabel()
    {
        return shippingLabel != null && shippingLabel.getLabel() != null;
    }
}
