package com.freightcom.api.model;

public enum MessageAction {
    NONE(""), UPDATE, MANUAL_UPDATE("MANUAL UPDATE"), NOTE, PRIVATE_NOTE("PRIVATE NOTE"), CUSTOMER_COMMENT(
            "CUSTOMER_COMMENT"), DISPUTE,
    DISPUTE_RESOLVED("DISPUTE RESOLVED"),
    CLAIM("CLAIM"), UPLOAD("UPLOAD"), NEW_ORDER("NEW"), NEW_CHARGE("NEW CHARGE"), CHARGE_UPDATED("CHARGE UPDATED"),
    NEW_CLAIM("NEW CLAIM"),
    CLAIM_REVIEWED("CLAIM REVIEWED"),
    CLAIM_IN_PROCESS("CLAIM IN PROCESS"),
    CLAIM_DENIED("CLAIM DENIED"),
    CLAIM_SETTLED("CLAIM SETTLED");

    private String value;

    MessageAction(String value)
    {
        this.value = value;
    }

    MessageAction()
    {
        this.value = name();
    }

    public String getValue()
    {
        return value;
    }

    public String toString()
    {
        return value;
    }
}
