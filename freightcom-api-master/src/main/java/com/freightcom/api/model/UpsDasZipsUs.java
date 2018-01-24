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
@Entity(name = "UpsDasZipsUs")
@Table(name = "ups_das_zips_us")
public class UpsDasZipsUs extends TransactionalEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="zip_code") 
    private Long zipCode;

    @Column(name="is_das") 
    private Integer isDas;

    @Column(name="is_das_extended") 
    private Integer isDasExtended;

    @Column(name="is_remote_hi") 
    private Integer isRemoteHi;

    @Column(name="is_remote_ak") 
    private Integer isRemoteAk;

    public UpsDasZipsUs() {
        super();
    }


    public Long getZipCode()
    {
         return zipCode; 
    }

    public void setZipCode(Long zipCode)
    {
         this.zipCode = zipCode; 
    }


    public Integer getIsDas()
    {
         return isDas; 
    }

    public void setIsDas(Integer isDas)
    {
         this.isDas = isDas; 
    }


    public Integer getIsDasExtended()
    {
         return isDasExtended; 
    }

    public void setIsDasExtended(Integer isDasExtended)
    {
         this.isDasExtended = isDasExtended; 
    }


    public Integer getIsRemoteHi()
    {
         return isRemoteHi; 
    }

    public void setIsRemoteHi(Integer isRemoteHi)
    {
         this.isRemoteHi = isRemoteHi; 
    }


    public Integer getIsRemoteAk()
    {
         return isRemoteAk; 
    }

    public void setIsRemoteAk(Integer isRemoteAk)
    {
         this.isRemoteAk = isRemoteAk; 
    }

    public Long getId() {
         return zipCode; 
    }

    public void setId(Long zipCode) {
         this.zipCode = zipCode; 
    }

}