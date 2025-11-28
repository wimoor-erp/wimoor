package com.wimoor.finance.closing.domain;


/**
 * 问题严重程度枚举
 */
public enum IssueSeverity {

    /**
     * 信息级别
     */
    INFO(1, "信息"),

    /**
     * 警告级别
     */
    WARNING(2, "警告"),

    /**
     * 错误级别
     */
    ERROR(3, "错误");

    private final Integer code;
    private final String description;

    IssueSeverity(Integer code, String description) {
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