package com.wimoor.finance.closing.domain;

/**
 * 连续性問題类型枚举
 */
public enum ContinuityIssueType {

    /**
     * 缺失序列号
     */
    MISSING_SEQUENCE(1, "缺失序列号"),

    /**
     * 重复序列号
     */
    DUPLICATE_SEQUENCE(2, "重复序列号"),
    /**
     * 编号间隙
     */
    NUMBER_GAP(4, "编号间隙"),

    /**
     * 无效格式
     */
    INVALID_FORMAT(3, "无效格式");


    private final Integer code;
    private final String description;

    ContinuityIssueType(Integer code, String description) {
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
