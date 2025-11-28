package com.wimoor.finance.closing.domain;

/**
 * 损益结转方向枚举
 */
public enum TransferDirection {

    /**
     * 借方结转
     */
    DEBIT(1, "借方结转"),

    /**
     * 贷方结转
     */
    CREDIT(2, "贷方结转");

    private final Integer code;
    private final String description;

    TransferDirection(Integer code, String description) {
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
