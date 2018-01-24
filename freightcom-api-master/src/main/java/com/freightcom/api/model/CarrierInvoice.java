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

import java.time.ZonedDateTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 *
 *
 * @author
 */
@Entity(name = "CarrierInvoice")
@Table(name = "carrier_invoice")
public class CarrierInvoice extends TransactionalEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "carrier_service_id", referencedColumnName = "id")
    private Service service;

    @Column(name="document_id")
    private String documentId;

    @Column(name="processed")
    private Boolean processed = false;

    @Column(name="file_name")
    private String fileName;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="created_at")
    private Date createdAt;

    public CarrierInvoice() {
        super();
    }

    public CarrierInvoice(Service service,
                          String documentId,
                          String fileName) {
        super();

        this.service = service;
        this.documentId = documentId;
        this.fileName = fileName;
    }


    public Long getId()
    {
         return id;
    }

    public void setId(Long id)
    {
         this.id = id;
    }


    public Service getService()
    {
         return service;
    }


    public Service getCarrier()
    {
         return service;
    }

    public void setService(Service service)
    {
         this.service = service;
    }


    public String getDocumentId()
    {
         return documentId;
    }

    public void setDocumentId(String documentId)
    {
         this.documentId = documentId;
    }


    public Boolean getProcessed()
    {
         return processed;
    }

    public void setProcessed(Boolean processed)
    {
         this.processed = processed;
    }


    public String getFileName()
    {
         return fileName;
    }

    public void setFileName(String fileName)
    {
         this.fileName = fileName;
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    public ZonedDateTime getCreatedAt()
    {
         return asDate(createdAt);
    }

    public void setCreatedAt(Date createdAt)
    {
         this.createdAt = createdAt;
    }

}
