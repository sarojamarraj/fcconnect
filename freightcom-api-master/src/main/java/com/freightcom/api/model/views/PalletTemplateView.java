package com.freightcom.api.model.views;

import java.math.BigDecimal;

import com.freightcom.api.model.PalletTemplate;
import com.freightcom.api.util.MapBuilder;

public class PalletTemplateView
{
    private final PalletTemplate palletTemplate;

    public static PalletTemplateView get(final PalletTemplate palletTemplate)
    {
        return new PalletTemplateView(palletTemplate);
    }

    private PalletTemplateView(final PalletTemplate palletTemplate)
    {
        this.palletTemplate = palletTemplate;
    }

    public String getName()
    {
        return palletTemplate.getName();
    }

    public Object getPalletType()
    {
        if (palletTemplate.getPalletType() != null) {
            return MapBuilder.getNew("id", palletTemplate.getPalletType()
                    .getId())
                    .set("name", palletTemplate.getPalletType()
                            .getName())
                    .toMap();
        }

        return null;
    }

    public Long getPalletTypeId()
    {
        if (palletTemplate.getPalletType() != null) {
            return palletTemplate.getPalletType().getId();
        }

        return 1L;
    }

    public Integer getHeight()
    {
        return palletTemplate.getHeight();
    }

    public Integer getLength()
    {
        return palletTemplate.getLength();
    }

    public Integer getWidth()
    {
        return palletTemplate.getWidth();
    }

    public String getDescription()
    {
        return palletTemplate.getDescription();
    }

    public BigDecimal getInsurance()
    {
        return palletTemplate.getInsurance();
    }

    public String getNmfcCode()
    {
        return palletTemplate.getNmfcCode();
    }

    public String getFreightclass()
    {
        return palletTemplate.getFreightclass();
    }

    public Integer getPieces()
    {
        return palletTemplate.getPieces();
    }

    public Double getWeight()
    {
        return palletTemplate.getWeight();
    }
}
