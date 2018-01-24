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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 *
 * @author
 */
@Entity(name = "UpsRateCodes")
@Table(name = "ups_rate_codes")
public class UpsRateCodes extends TransactionalEntity
{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "service_name")
    private String serviceName;

    @Column(name = "from_country")
    private String fromCountry;

    @Column(name = "to_country")
    private String toCountry;

    @Column(name = "category")
    private String category;

    @Column(name = "shipping_code")
    private String shippingCode;

    @Column(name = "rating_code")
    private String ratingCode;

    public UpsRateCodes()
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

    public String getServiceName()
    {
        return serviceName;
    }

    public void setServiceName(String serviceName)
    {
        this.serviceName = serviceName;
    }

    public String getFromCountry()
    {
        return fromCountry;
    }

    public void setFromCountry(String fromCountry)
    {
        this.fromCountry = fromCountry;
    }

    public String getToCountry()
    {
        return toCountry;
    }

    public void setToCountry(String toCountry)
    {
        this.toCountry = toCountry;
    }

    public String getCategory()
    {
        return category;
    }

    public void setCategory(String category)
    {
        this.category = category;
    }

    public String getShippingCode()
    {
        return shippingCode;
    }

    public void setShippingCode(String shippingCode)
    {
        this.shippingCode = shippingCode;
    }

    public String getRatingCode()
    {
        return ratingCode;
    }

    public void setRatingCode(String ratingCode)
    {
        this.ratingCode = ratingCode;
    }


    @Override
    public String toString()
    {
        return "RateCode " + ratingCode + " " + serviceName + " " + fromCountry + " " + toCountry;
    }
}