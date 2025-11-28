package com.wimoor.finance.closing.domain;

public enum TransferType {

    INCOME(1, "收入结转"),
    EXPENSE(2, "费用结转"),
    PROFIT(3, "利润结转"),
    LOSS(4, "亏损结转");

    private final Integer code;
    private final String description;

    TransferType(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public Integer getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}