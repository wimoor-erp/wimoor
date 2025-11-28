package com.wimoor.finance.closing.domain;

public enum ErrorType {

    SUBJECT_NOT_FOUND(1, "科目不存在"),
    BALANCE_ZERO(2, "余额为零"),
    BALANCE_NEGATIVE(3, "余额为负"),
    VOUCHER_GENERATION_FAILED(4, "凭证生成失败"),
    DATABASE_ERROR(5, "数据库错误"),
    VALIDATION_ERROR(6, "数据验证错误"),
    UNKNOWN(99, "未知错误");

    private final Integer code;
    private final String description;

    ErrorType(Integer code, String description) {
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
