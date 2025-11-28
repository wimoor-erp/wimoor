package com.wimoor.finance.voucher.domain;

/**
 * 修改结果类
 */
public  class   ModifyResult {
    private boolean success;
    private String message;

    private ModifyResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public static ModifyResult success(String message) {
        return new ModifyResult(true, message);
    }

    public static ModifyResult fail(String message) {
        return new ModifyResult(false, message);
    }

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
}