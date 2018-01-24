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

import javax.persistence.Entity;

import java.time.ZonedDateTime;
import java.util.Date;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 *
 * @author
 */
@Entity(name = "CarrierData")
@Table(name = "carrier_data")
public class CarrierData extends TransactionalEntity
{

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "fedex_can_meter_num")
    private String fedexCanMeterNum;

    @Column(name = "ups_usa_username")
    private String upsUsaUsername;

    @Column(name = "use_custom_ups_can_acct")
    private Integer useCustomUpsCanAcct;

    @Column(name = "ups_can_acct_num")
    private String upsCanAcctNum;

    @Column(name = "fedex_acct_num_us")
    private String fedexAcctNumUs;

    @Column(name = "use_custom_fedex_usa_acct")
    private Integer useCustomFedexUsaAcct;

    @Column(name = "use_custom_fedex_can_acct")
    private Integer useCustomFedexCanAcct;

    @Column(name = "ups_usa_password")
    private String upsUsaPassword;

    @Column(name = "fedex_meter_num")
    private String fedexMeterNum;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "fedex_meter_num_us")
    private String fedexMeterNumUs;

    @Column(name = "ups_can_username")
    private String upsCanUsername;

    @Column(name = "sfw_user_id")
    private String sfwUserId;

    @Column(name = "ups_usa_key")
    private String upsUsaKey;

    @Column(name = "ups_can_password")
    private String upsCanPassword;

    @Column(name = "ups_can_key")
    private String upsCanKey;

    @Column(name = "ups_usa_acct_num")
    private String upsUsaAcctNum;

    @Column(name = "use_custom_ups_usa_acct")
    private Integer useCustomUpsUsaAcct;

    @Column(name = "fedex_acct_num")
    private String fedexAcctNum;

    @Column(name = "ups_acct_num")
    private String upsAcctNum;

    @Column(name = "fedex_usa_acct_num")
    private String fedexUsaAcctNum;

    @Column(name = "fedex_usa_meter_num")
    private String fedexUsaMeterNum;

    @Column(name = "fedex_can_acct_num")
    private String fedexCanAcctNum;

    public CarrierData()
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

    public String getFedexCanMeterNum()
    {
        return fedexCanMeterNum;
    }

    public void setFedexCanMeterNum(String fedexCanMeterNum)
    {
        this.fedexCanMeterNum = fedexCanMeterNum;
    }

    public String getUpsUsaUsername()
    {
        return upsUsaUsername;
    }

    public void setUpsUsaUsername(String upsUsaUsername)
    {
        this.upsUsaUsername = upsUsaUsername;
    }

    public Integer getUseCustomUpsCanAcct()
    {
        return useCustomUpsCanAcct;
    }

    public void setUseCustomUpsCanAcct(Integer useCustomUpsCanAcct)
    {
        this.useCustomUpsCanAcct = useCustomUpsCanAcct;
    }

    public String getUpsCanAcctNum()
    {
        return upsCanAcctNum;
    }

    public void setUpsCanAcctNum(String upsCanAcctNum)
    {
        this.upsCanAcctNum = upsCanAcctNum;
    }

    public String getFedexAcctNumUs()
    {
        return fedexAcctNumUs;
    }

    public void setFedexAcctNumUs(String fedexAcctNumUs)
    {
        this.fedexAcctNumUs = fedexAcctNumUs;
    }

    public Integer getUseCustomFedexUsaAcct()
    {
        return useCustomFedexUsaAcct;
    }

    public void setUseCustomFedexUsaAcct(Integer useCustomFedexUsaAcct)
    {
        this.useCustomFedexUsaAcct = useCustomFedexUsaAcct;
    }

    public Integer getUseCustomFedexCanAcct()
    {
        return useCustomFedexCanAcct;
    }

    public void setUseCustomFedexCanAcct(Integer useCustomFedexCanAcct)
    {
        this.useCustomFedexCanAcct = useCustomFedexCanAcct;
    }

    public String getUpsUsaPassword()
    {
        return upsUsaPassword;
    }

    public void setUpsUsaPassword(String upsUsaPassword)
    {
        this.upsUsaPassword = upsUsaPassword;
    }

    public String getFedexMeterNum()
    {
        return fedexMeterNum;
    }

    public void setFedexMeterNum(String fedexMeterNum)
    {
        this.fedexMeterNum = fedexMeterNum;
    }

    public ZonedDateTime getCreatedAt()
    {
        return asDate(createdAt);
    }

    public void setCreatedAt(Date createdAt)
    {
        this.createdAt = createdAt;
    }

    public String getFedexMeterNumUs()
    {
        return fedexMeterNumUs;
    }

    public void setFedexMeterNumUs(String fedexMeterNumUs)
    {
        this.fedexMeterNumUs = fedexMeterNumUs;
    }

    public String getUpsCanUsername()
    {
        return upsCanUsername;
    }

    public void setUpsCanUsername(String upsCanUsername)
    {
        this.upsCanUsername = upsCanUsername;
    }

    public String getSfwUserId()
    {
        return sfwUserId;
    }

    public void setSfwUserId(String sfwUserId)
    {
        this.sfwUserId = sfwUserId;
    }

    public String getUpsUsaKey()
    {
        return upsUsaKey;
    }

    public void setUpsUsaKey(String upsUsaKey)
    {
        this.upsUsaKey = upsUsaKey;
    }

    public String getUpsCanPassword()
    {
        return upsCanPassword;
    }

    public void setUpsCanPassword(String upsCanPassword)
    {
        this.upsCanPassword = upsCanPassword;
    }

    public String getUpsCanKey()
    {
        return upsCanKey;
    }

    public void setUpsCanKey(String upsCanKey)
    {
        this.upsCanKey = upsCanKey;
    }

    public String getUpsUsaAcctNum()
    {
        return upsUsaAcctNum;
    }

    public void setUpsUsaAcctNum(String upsUsaAcctNum)
    {
        this.upsUsaAcctNum = upsUsaAcctNum;
    }

    public Integer getUseCustomUpsUsaAcct()
    {
        return useCustomUpsUsaAcct;
    }

    public void setUseCustomUpsUsaAcct(Integer useCustomUpsUsaAcct)
    {
        this.useCustomUpsUsaAcct = useCustomUpsUsaAcct;
    }

    public String getFedexAcctNum()
    {
        return fedexAcctNum;
    }

    public void setFedexAcctNum(String fedexAcctNum)
    {
        this.fedexAcctNum = fedexAcctNum;
    }

    public String getUpsAcctNum()
    {
        return upsAcctNum;
    }

    public void setUpsAcctNum(String upsAcctNum)
    {
        this.upsAcctNum = upsAcctNum;
    }

    public String getFedexUsaAcctNum()
    {
        return fedexUsaAcctNum;
    }

    public void setFedexUsaAcctNum(String fedexUsaAcctNum)
    {
        this.fedexUsaAcctNum = fedexUsaAcctNum;
    }

    public String getFedexUsaMeterNum()
    {
        return fedexUsaMeterNum;
    }

    public void setFedexUsaMeterNum(String fedexUsaMeterNum)
    {
        this.fedexUsaMeterNum = fedexUsaMeterNum;
    }

    public String getFedexCanAcctNum()
    {
        return fedexCanAcctNum;
    }

    public void setFedexCanAcctNum(String fedexCanAcctNum)
    {
        this.fedexCanAcctNum = fedexCanAcctNum;
    }

}
