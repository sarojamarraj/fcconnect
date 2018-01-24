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
@Entity(name = "UpsDasZipsCan")
@Table(name = "ups_das_zips_can")
public class UpsDasZipsCan extends TransactionalEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id") 
    private Long id;

    @Column(name="from_postal_code") 
    private String fromPostalCode;

    @Column(name="to_postal_code") 
    private String toPostalCode;

    @Column(name="del_area_charge") 
    private BigDecimal delAreaCharge;

    public UpsDasZipsCan() {
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


    public String getFromPostalCode()
    {
         return fromPostalCode; 
    }

    public void setFromPostalCode(String fromPostalCode)
    {
         this.fromPostalCode = fromPostalCode; 
    }


    public String getToPostalCode()
    {
         return toPostalCode; 
    }

    public void setToPostalCode(String toPostalCode)
    {
         this.toPostalCode = toPostalCode; 
    }


    public BigDecimal getDelAreaCharge()
    {
         return delAreaCharge; 
    }

    public void setDelAreaCharge(BigDecimal delAreaCharge)
    {
         this.delAreaCharge = delAreaCharge; 
    }

}