package com.freightcom.api.model.views;

import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.freightcom.api.model.OrderDocument;
import com.freightcom.api.services.ServiceProvider;

public class OrderDocumentView implements View
{
    private final OrderDocument document;
    private final ServiceProvider provider;

    public static OrderDocumentView get(final OrderDocument document, final ServiceProvider provider)
    {
        if (document == null) {
            return null;
        }

        return new OrderDocumentView(document, provider);
    }

    public OrderDocumentView(final OrderDocument document, ServiceProvider provider)
    {
        this.document = document;
        this.provider = provider;
    }

    public Long getId()
    {
        return document.getId();
    }

    public String getFileName()
    {
        return document.getName();
    }

    public Boolean getCanDelete()
    {
        return provider != null && document.canDelete(provider.getApiSession().getRole());
    }

    public String getUploadedByName()
    {
        return provider == null ? "" : document.getUploadedByName(provider.getApiSession().getRole());
    }

    public String getDescription()
    {
        return document.getDescription();
    }

    public String getFileType()
    {
        return document.getType();
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    public ZonedDateTime getDateUploaded()
    {
        return document.getCreatedAt();
    }

}
