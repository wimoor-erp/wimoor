package com.wimoor.finance.closing.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PreClosingCheckResult {

    /**
     * 总账是否平衡
     */
    private Boolean balanced = false;

    /**
     * 检查是否通过
     */
    private Boolean passed = false;

    /**
     * 错误消息（如果有）
     */
    private String errorMessage;

    /**
     * 检查项列表
     */
    private List<ClosingCheckItem> checkItems = new ArrayList<>();

    /**
     * 警告信息列表
     */
    private List<String> warnings = new ArrayList<>();

    /**
     * 错误信息列表
     */
    private List<String> errors = new ArrayList<>();

    /**
     * 检查统计信息
     */
    private CheckStatistics statistics = new CheckStatistics();

    /**
     * 检查时间
     */
    private Date checkTime = new Date();

    /**
     * 检查人
     */
    private String checkedBy;

    /**
     * 检查的会计期间
     */
    private String period;

    /**
     * 租户ID
     */
    private String groupid;

    // 构造方法
    public PreClosingCheckResult(String groupid, String period, String checkedBy) {
        this.groupid = groupid;
        this.period = period;
        this.checkedBy = checkedBy;
    }

    // 业务方法

    /**
     * 添加检查项
     */
    public void addCheckItem(ClosingCheckItem checkItem) {
        this.checkItems.add(checkItem);

        // 更新统计信息
        if (checkItem.getPassed()) {
            statistics.incrementPassedCount();
        } else {
            statistics.incrementFailedCount();

        }

        statistics.incrementTotalCount();
        updatePassedStatus();
    }

    /**
     * 添加检查项（简化版）
     */
    public void addCheckItem(String checkItem, Boolean passed, String message) {
        addCheckItem(new ClosingCheckItem(checkItem, passed, message));
    }

    /**
     * 添加检查项（带检查级别）
     */
    public void addCheckItem(String checkItem, Boolean passed, String message, CheckLevel checkLevel) {
        addCheckItem(new ClosingCheckItem(checkItem, passed, message, checkLevel));
    }

    /**
     * 添加警告信息
     */
    public void addWarning(String warning) {
        this.warnings.add(warning);
        statistics.incrementWarningCount();
    }

    /**
     * 添加错误信息
     */
    public void addError(String error) {
        this.errors.add(error);
        statistics.incrementErrorCount();
        this.passed = false;
        this.errorMessage = error;
    }

    /**
     * 检查是否有错误
     */
    public boolean hasErrors() {
        return !this.errors.isEmpty();
    }

    /**
     * 检查是否有警告
     */
    public boolean hasWarnings() {
        return !this.warnings.isEmpty();
    }

    /**
     * 获取通过率
     */
    public BigDecimal getPassRate() {
        if (statistics.getTotalCount() == 0) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf(statistics.getPassedCount())
                .divide(BigDecimal.valueOf(statistics.getTotalCount()), 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
    }

    /**
     * 更新通过状态
     */
    private void updatePassedStatus() {
        // 如果有错误，则不通过
        if (hasErrors()) {
            this.passed = false;
            return;
        }

        // 所有检查项都通过才算通过
        this.passed = statistics.getFailedCount() == 0;
    }

    public Boolean isPassed() {
        return passed;
    }
    /**
     * 获取检查摘要
     */
    public String getSummary() {
        if (!passed) {
            return String.format("检查未通过: 总共%d项检查，%d项失败，%d项警告",
                    statistics.getTotalCount(), statistics.getFailedCount(), statistics.getWarningCount());
        } else if (hasWarnings()) {
            return String.format("检查通过(有警告): 总共%d项检查，%d项警告",
                    statistics.getTotalCount(), statistics.getWarningCount());
        } else {
            return String.format("检查通过: 总共%d项检查，全部通过", statistics.getTotalCount());
        }
    }

}
