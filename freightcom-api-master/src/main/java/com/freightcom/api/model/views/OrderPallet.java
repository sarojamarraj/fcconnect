package com.freightcom.api.model.views;

import com.freightcom.api.model.Pallet;


public class OrderPallet implements View
{
    private final Pallet pallet;
    private final boolean nullId;

    public OrderPallet(final Pallet pallet, final boolean nullId)
    {
        this.pallet = pallet;
        this.nullId = nullId;
    }

    public OrderPallet(final Pallet pallet)
    {
        this.pallet = pallet;
        this.nullId = false;
    }

    public Object getId()
    {
        return nullId ? null : pallet.getId();
    }

    public String getHeight()
    {
        return asString(pallet.getHeight());
    }

    public String getWidth()
    {
        return asString(pallet.getWidth());
    }

    public String getLength()
    {
        return asString(pallet.getLength());
    }

    public String getWeight()
    {
        return pallet.getWeight();
    }

    public Object getDescription()
    {
        return pallet.getDescription();
    }

    public String getInsurance()
    {
        return asString(pallet.getInsurance());
    }

    public String getPieces()
    {
        return asString(pallet.getPieces());
    }

    public String getNmfcCode()
    {
        return pallet.getNmfcCode();
    }

    public String getFreightclass()
    {
        return pallet.getFreightclass();
    }

    public String getPalletType()
    {
        return pallet.getPalletType();
    }

    private String asString(Object o)
    {
        if (o == null) {
            return null;
        } else {
            return o.toString();
        }
    }
}
