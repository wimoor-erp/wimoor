package com.wimoor.finance.closing.domain;

/**
 * 计算方式枚举
 */
public enum CalculationMethod {

    STANDARD(1, "标准计算"),
    QUICK(2, "快速计算"),
    MANUAL(3, "手动输入"),
    ADJUSTED(4, "调整后计算");

    private final Integer code;
    private final String description;

    CalculationMethod(Integer code, String description) {
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

