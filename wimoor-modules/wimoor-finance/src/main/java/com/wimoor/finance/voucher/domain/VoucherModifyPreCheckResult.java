package com.wimoor.finance.voucher.domain;


/**
 * 修改凭证前的检查结果类
 */
public class VoucherModifyPreCheckResult {
    private boolean success;
    private String message;
    private VoucherBackup backup;

    private VoucherModifyPreCheckResult(boolean success, String message, VoucherBackup backup) {
        this.success = success;
        this.message = message;
        this.backup = backup;
    }

    public static VoucherModifyPreCheckResult success(VoucherBackup backup) {
        return new VoucherModifyPreCheckResult(true, "", backup);
    }

    public static VoucherModifyPreCheckResult fail(String message) {
        return new VoucherModifyPreCheckResult(false, message, null);
    }

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public VoucherBackup getBackup() { return backup; }
}