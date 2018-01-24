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
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;

/**
 *
 *
 * @author
 */
@Entity(name = "Properties")
@Table(name = "properties")
public class Properties extends TransactionalEntity
{

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "property_type")
    private String propertyType;

    @Column(name = "property_value")
    private String propertyValue;

    @Column(name = "property_name")
    private String propertyName;

    public Properties()
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

    public String getPropertyType()
    {
        return propertyType;
    }

    public void setPropertyType(String propertyType)
    {
        this.propertyType = propertyType;
    }

    public String getPropertyValue()
    {
        return propertyValue;
    }

    public void setPropertyValue(String propertyValue)
    {
        this.propertyValue = propertyValue;
    }

    public String getPropertyName()
    {
        return propertyName;
    }

    public void setPropertyName(String propertyName)
    {
        this.propertyName = propertyName;
    }

}
