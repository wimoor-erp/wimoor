package com.wimoor.finance.closing.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

@Data
@NoArgsConstructor
public class CheckStatistics {

    /**
     * 总检查项数量
     */
    private Integer totalCount = 0;

    /**
     * 通过数量
     */
    private Integer passedCount = 0;

    /**
     * 失败数量
     */
    private Integer failedCount = 0;

    /**
     * 警告数量
     */
    private Integer warningCount = 0;

    /**
     * 错误数量
     */
    private Integer errorCount = 0;

    /**
     * 检查开始时间
     */
    private Date startTime = new Date();

    /**
     * 检查结束时间
     */
    private Date endTime;

    /**
     * 检查耗时（毫秒）
     */
    private Long durationMs = 0L;

    // 业务方法

    public void incrementTotalCount() {
        this.totalCount++;
    }

    public void incrementPassedCount() {
        this.passedCount++;
    }

    public void incrementFailedCount() {
        this.failedCount++;
    }

    public void incrementWarningCount() {
        this.warningCount++;
    }

    public void incrementErrorCount() {
        this.errorCount++;
    }

    /**
     * 完成统计
     */
    public void complete() {
        this.endTime = new Date();
        this.durationMs = endTime.getTime() - startTime.getTime();
    }

    /**
     * 获取通过率
     */
    public BigDecimal getPassRate() {
        if (totalCount == 0) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf(passedCount)
                .divide(BigDecimal.valueOf(totalCount), 4, RoundingMode.HALF_UP)
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
        return String.format("总计: %d项, 通过: %d项, 失败: %d项, 警告: %d项, 错误: %d项, 通过率: %.2f%%, 耗时: %s",
                totalCount, passedCount, failedCount, warningCount, errorCount,
                getPassRate().doubleValue(), getFormattedDuration());
    }
}
