package com.wimoor.finance.closing.domain;

import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class ReverseClosingResult {
    private boolean success = false;
    private String message;
    private String groupid;
    private String period;
    private String operator;
    private String reason;
    private Date operationTime = new Date();

    // 操作统计
    private Integer deletedVoucherCount = 0;
    private Integer deletedEntryCount = 0;
    private Integer restoredSubjectCount = 0;
    private Integer deletedNextPeriodLedgerCount = 0;
    private Integer deletedNextPeriodDetailCount = 0;

    // 详细操作记录
    private List<DeletedVoucherInfo> deletedVouchers = new ArrayList<>();
    private List<RestoredSubjectInfo> restoredSubjects = new ArrayList<>();

    // 构造方法
    public ReverseClosingResult(String groupid, String period, String operator, String reason) {
        this.groupid = groupid;
        this.period = period;
        this.operator = operator;
        this.reason = reason;
    }

    // 业务方法
    public ReverseClosingResult success(String message) {
        this.success = true;
        this.message = message;
        return this;
    }

    public ReverseClosingResult fail(String message) {
        this.success = false;
        this.message = message;
        return this;
    }

    public void addDeletedVoucher(DeletedVoucherInfo voucherInfo) {
        this.deletedVouchers.add(voucherInfo);
    }

    public void addRestoredSubject(RestoredSubjectInfo subjectInfo) {
        this.restoredSubjects.add(subjectInfo);
    }

    /**
     * 获取操作摘要
     */
    public String getOperationSummary() {
        if (!success) {
            return "反结账失败: " + message;
        }

        return String.format(
                "反结账成功: 删除凭证%d张(%d条分录), 恢复%d个科目余额, 清理下期%d条总账记录",
                deletedVoucherCount, deletedEntryCount, restoredSubjectCount, deletedNextPeriodLedgerCount
        );
    }
}