package com.freightcom.api.controllers.transfer;

import java.util.List;



public class PrintChequeRequest
{
    public enum Type { PAYABLES, COMMISSIONS };

    private Type type;
    private List<Long> ids;
    private Integer startingChequeNumber;

    public PrintChequeRequest()
    {
    }

    public Type getType()
    {
        return type;
    }

    public void setType(Type type)
    {
        this.type = type;
    }
    
    public boolean isPayables() 
    {
        return type == Type.PAYABLES;
    }
    
    public boolean isCommissions() 
    {
        return type == Type.COMMISSIONS;
    }

    public List<Long> getIds()
    {
        return ids;
    }

    public void setIds(List<Long> ids)
    {
        this.ids = ids;
    }

    public Integer getStartingChequeNumber()
    {
        return startingChequeNumber;
    }

    public void setStartingChequeNumber(Integer startingChequeNumber)
    {
        this.startingChequeNumber = startingChequeNumber;
    }

    public String toString()
    {
        return "PrintChequeRequest " + type;
    }
}
