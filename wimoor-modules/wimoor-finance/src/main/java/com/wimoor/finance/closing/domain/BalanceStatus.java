package com.wimoor.finance.closing.domain;

/**
 * 余额状态枚举
 */
public enum BalanceStatus {

    ZERO(1, "零余额"),
    DEBIT_BALANCE(2, "借方余额"),
    CREDIT_BALANCE(3, "贷方余额"),
    ABNORMAL(4, "异常余额");

    private final Integer code;
    private final String description;

    BalanceStatus(Integer code, String description) {
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
