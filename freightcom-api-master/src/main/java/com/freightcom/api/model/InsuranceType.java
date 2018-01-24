package com.freightcom.api.model;

public enum InsuranceType {
    FREIGHTCOM(1), CARRIER(2), DECLINED(0);

    private int value;

    InsuranceType(int value)
    {
        this.value = value;
    }

    public int getValue()
    {
        return value;
    }

    public String toString()
    {
        return Integer.toString(value);
    }

    public static InsuranceType valueOf(Integer insuranceType)
    {
        if (insuranceType == null) {
            return DECLINED;
        }

        switch(insuranceType.intValue()) {
        case 1:
            return FREIGHTCOM;

        case 2:
            return CARRIER;

        default:
            return DECLINED;
        }
    }

    public static InsuranceType getType(Integer insuranceType)
    {
        if (insuranceType == null) {
            return DECLINED;
        }

        switch(insuranceType.intValue()) {
        case 1:
            return FREIGHTCOM;

        case 2:
            return CARRIER;

        default:
            return DECLINED;
        }
    }

    public static InsuranceType getType(String insuranceType)
    {
        if (insuranceType == null) {
            return DECLINED;
        }

        switch(insuranceType) {
        case "1":
            return FREIGHTCOM;

        case "2":
            return CARRIER;

        default:
            return DECLINED;
        }
    }
}
