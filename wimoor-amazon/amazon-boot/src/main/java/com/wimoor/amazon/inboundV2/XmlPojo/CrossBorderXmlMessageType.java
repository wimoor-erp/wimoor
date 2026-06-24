package com.wimoor.amazon.inboundV2.XmlPojo;

public enum CrossBorderXmlMessageType {
    ORDER("CEB303"),
    INVENTORY("CEB603"),
    RECEIPT("CEB403"),
    DECLARATION("DEC001"),
    DECLARATION_DRAFT("DEC002");
    private final String value;
    CrossBorderXmlMessageType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
