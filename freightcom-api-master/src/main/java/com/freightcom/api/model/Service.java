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
import java.time.ZonedDateTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *
 *
 * @author
 */
@Entity(name = "Service")
@Table(name = "service")
public class Service extends TransactionalEntity
{
    private static final long serialVersionUID = 1L;

    public enum Term { DAILY, WEEKLY, BIWEEKLY, MONTHLY };

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "end_at")
    private Date endAt;

    @Column(name = "code")
    private String code;

    @Column(name = "color")
    private String color;

    @Column(name = "transit_code_intl")
    private String transitCodeIntl;

    @Column(name = "contract_id")
    private String contractId;

    @Column(name = "carrier_service_name")
    private String carrierServiceName;

    @Column(name = "display_order")
    private Integer displayOrder;

    @Column(name = "description")
    private String description;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "start_at")
    private Date startAt;

    @ManyToOne
    @JoinColumn(name = "carrier_id")
    private Carrier carrier;

    @Column(name = "mode")
    private Integer mode;

    @Column(name = "code_us")
    private String codeUs;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(name = "provider")
    private String provider;

    @Column(name = "logo")
    private String logo;

    @Column(name = "distribution_id")
    private Long distributionId;

    @Column(name = "terminal_info_link")
    private String terminalInfoLink;

    @Column(name = "code_intl")
    private String codeIntl;

    @Column(name = "markup_id")
    private Long markupId;

    @Column(name = "service_time_mins")
    private Integer serviceTimeMins;

    @Column(name = "guaranteed")
    private Boolean guaranteed;

    @Column(name = "discontinued")
    private Integer discontinued = 0;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "deleted_at")
    private Date deletedAt;

    @Column(name = "rate_adjustments_id")
    private Long rateAdjustmentsId;

    @Column(name = "name")
    private String name;

    @Column(name = "service_name_us")
    private String serviceNameUs;

    @Column(name = "tran_fee")
    private BigDecimal tranFee;

    @Column(name = "transit_code_us")
    private String transitCodeUs;

    @Column(name = "transit_code_ca")
    private String transitCodeCa;

    @Column(name = "term")
    @Enumerated(EnumType.STRING)
    private Term term;

    @ManyToOne
    @JoinColumn(name = "billing_address_id")
    private Address address;

    public Service()
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

    public ZonedDateTime getEndAt()
    {
        return asDate(endAt);
    }

    public void setEndAt(Date endAt)
    {
        this.endAt = endAt;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public String getColor()
    {
        return color;
    }

    public void setColor(String color)
    {
        this.color = color;
    }

    public String getTransitCodeIntl()
    {
        return transitCodeIntl;
    }

    public void setTransitCodeIntl(String transitCodeIntl)
    {
        this.transitCodeIntl = transitCodeIntl;
    }

    public String getContractId()
    {
        return contractId;
    }

    public void setContractId(String contractId)
    {
        this.contractId = contractId;
    }

    public String getCarrierServiceName()
    {
        return carrierServiceName;
    }

    public void setCarrierServiceName(String carrierServiceName)
    {
        this.carrierServiceName = carrierServiceName;
    }

    public Integer getDisplayOrder()
    {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder)
    {
        this.displayOrder = displayOrder;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public ZonedDateTime getCreatedAt()
    {
        return asDate(createdAt);
    }

    public void setCreatedAt(Date createdAt)
    {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getStartAt()
    {
        return asDate(startAt);
    }

    public void setStartAt(Date startAt)
    {
        this.startAt = startAt;
    }

    public Integer getMode()
    {
        return mode;
    }

    public void setMode(Integer mode)
    {
        this.mode = mode;
    }

    public String getCodeUs()
    {
        return codeUs;
    }

    public void setCodeUs(String codeUs)
    {
        this.codeUs = codeUs;
    }

    public ZonedDateTime getUpdatedAt()
    {
        return asDate(updatedAt);
    }

    public void setUpdatedAt(Date updatedAt)
    {
        this.updatedAt = updatedAt;
    }

    public String getProvider()
    {
        return provider;
    }

    public void setProvider(String provider)
    {
        this.provider = provider;
    }

    public String getLogo()
    {
        return logo;
    }

    public void setLogo(String logo)
    {
        this.logo = logo;
    }

    public Long getDistributionId()
    {
        return distributionId;
    }

    public void setDistributionId(Long distributionId)
    {
        this.distributionId = distributionId;
    }

    public String getTerminalInfoLink()
    {
        return terminalInfoLink;
    }

    public void setTerminalInfoLink(String terminalInfoLink)
    {
        this.terminalInfoLink = terminalInfoLink;
    }

    public String getCodeIntl()
    {
        return codeIntl;
    }

    public void setCodeIntl(String codeIntl)
    {
        this.codeIntl = codeIntl;
    }

    public Long getMarkupId()
    {
        return markupId;
    }

    public void setMarkupId(Long markupId)
    {
        this.markupId = markupId;
    }

    public Integer getServiceTimeMins()
    {
        return serviceTimeMins;
    }

    public void setServiceTimeMins(Integer serviceTimeMins)
    {
        this.serviceTimeMins = serviceTimeMins;
    }

    public Boolean getGuaranteed()
    {
        return guaranteed;
    }

    public void setGuaranteed(Boolean guaranteed)
    {
        this.guaranteed = guaranteed;
    }

    public Integer getDiscontinued()
    {
        return discontinued == null ? 0 : discontinued;
    }

    public void setDiscontinued(Integer discontinued)
    {
        this.discontinued = discontinued;
    }

    public ZonedDateTime getDeletedAt()
    {
        return asDate(deletedAt);
    }

    public void setDeletedAt(Date deletedAt)
    {
        this.deletedAt = deletedAt;
    }

    public Long getRateAdjustmentsId()
    {
        return rateAdjustmentsId;
    }

    public void setRateAdjustmentsId(Long rateAdjustmentsId)
    {
        this.rateAdjustmentsId = rateAdjustmentsId;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getServiceNameUs()
    {
        return serviceNameUs;
    }

    public void setServiceNameUs(String serviceNameUs)
    {
        this.serviceNameUs = serviceNameUs;
    }

    public BigDecimal getTranFee()
    {
        return tranFee;
    }

    public void setTranFee(BigDecimal tranFee)
    {
        this.tranFee = tranFee;
    }

    public String getTransitCodeUs()
    {
        return transitCodeUs;
    }

    public void setTransitCodeUs(String transitCodeUs)
    {
        this.transitCodeUs = transitCodeUs;
    }

    public String getTransitCodeCa()
    {
        return transitCodeCa;
    }

    public void setTransitCodeCa(String transitCodeCa)
    {
        this.transitCodeCa = transitCodeCa;
    }

    @JsonIgnore
    public Carrier getCarrier()
    {
        return carrier;
    }

    public void setCarrier(Carrier carrier)
    {
        this.carrier = carrier;
    }

    @JsonIgnore
    public Address getAddress()
    {
        return address;
    }

    public void setAddress(Address address)
    {
        this.address = address;
    }

    public Term getTerm()
    {
        return term;
    }

    public void setTerm(Term term)
    {
        this.term = term;
    }

    public String getCodeUS()
    {
        // TODO
        return "A";
    }

    public String toString()
    {
        return "<SERVICE " + id + " " + description + ">";
    }
}
