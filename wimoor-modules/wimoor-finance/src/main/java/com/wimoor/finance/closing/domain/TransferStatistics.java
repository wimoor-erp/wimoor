package com.wimoor.finance.closing.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

@Data
@NoArgsConstructor
public class TransferStatistics {

    /**
     * 开始时间
     */
    private Date startTime = new Date();

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 结转耗时（毫秒）
     */
    private Long durationMs = 0L;

    /**
     * 结转科目总数
     */
    private Integer totalSubjectCount = 0;

    /**
     * 成功结转科目数
     */
    private Integer successSubjectCount = 0;

    /**
     * 失败结转科目数
     */
    private Integer failedSubjectCount = 0;

    /**
     * 生成凭证数量
     */
    private Integer voucherCount = 0;

    /**
     * 结转总金额
     */
    private BigDecimal totalTransferAmount = BigDecimal.ZERO;

    /**
     * 凭证总金额
     */
    private BigDecimal totalVoucherAmount = BigDecimal.ZERO;

    // 业务方法

    public void incrementSubjectCount() {
        this.totalSubjectCount++;
    }

    public void incrementSuccessSubjectCount() {
        this.successSubjectCount++;
    }

    public void incrementFailedSubjectCount() {
        this.failedSubjectCount++;
    }

    public void incrementVoucherCount() {
        this.voucherCount++;
    }

    public void addTotalTransferAmount(BigDecimal amount) {
        this.totalTransferAmount = this.totalTransferAmount.add(amount);
    }

    public void addTotalAmount(BigDecimal amount) {
        this.totalVoucherAmount = this.totalVoucherAmount.add(amount);
    }

    /**
     * 完成统计
     */
    public void complete() {
        this.endTime = new Date();
        this.durationMs = endTime.getTime() - startTime.getTime();
    }

    /**
     * 获取成功率
     */
    public BigDecimal getSuccessRate() {
        if (totalSubjectCount == 0) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf(successSubjectCount)
                .divide(BigDecimal.valueOf(totalSubjectCount), 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
    }

    /**
     * 获取格式化耗时
     */
    public String getFormattedDuration() {
        if (durationMs < 1000) {
            return durationMs + "ms";
        } else {
            return (durationMs / 1000) + "s " + (durationMs % 1000) + "ms";
        }
    }

    /**
     * 获取统计摘要
     */
    public String getSummary() {
        return String.format("科目: %d成功/%d失败, 凭证: %d张, 金额: %.2f, 耗时: %s, 成功率: %.2f%%",
                successSubjectCount, failedSubjectCount, voucherCount,
                totalTransferAmount, getFormattedDuration(), getSuccessRate().doubleValue());
    }

    public void setTotalSubjects(Integer subjectCount) {
        this.totalSubjectCount = subjectCount;
    }
}