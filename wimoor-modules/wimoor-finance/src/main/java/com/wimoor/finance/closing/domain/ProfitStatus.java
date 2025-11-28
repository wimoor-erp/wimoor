package com.wimoor.finance.closing.domain;


/**
 * 盈利状态枚举
 */
public enum ProfitStatus {

    /**
     * 盈利
     */
    PROFIT(1, "盈利"),

    /**
     * 亏损
     */
    LOSS(2, "亏损"),

    /**
     * 持平
     */
    BREAK_EVEN(3, "持平");

    private final Integer code;
    private final String description;

    ProfitStatus(Integer code, String description) {
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