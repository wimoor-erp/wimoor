package com.wimoor.finance.closing.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferResult {

    /**
     * 结转是否成功
     */
    private Boolean success = false;

    /**
     * 结转结果消息
     */
    private String message;

    /**
     * 结转类型（收入/费用）
     */
    private TransferType transferType;

    /**
     * 结转的会计期间
     */
    private String period;

    /**
     * 租户ID
     */
    private Long tenantId;

    /**
     * 操作人
     */
    private String operator;

    /**
     * 结转时间
     */
    private Date transferTime = new Date();

    /**
     * 结转总金额
     */
    private BigDecimal totalAmount = BigDecimal.ZERO;

    /**
     * 结转科目数量
     */
    private Integer subjectCount = 0;

    /**
     * 成功结转科目数
     */
    private Integer successCount = 0;

    /**
     * 失败结转科目数
     */
    private Integer failedCount = 0;

    /**
     * 跳过的科目数（余额为0）
     */
    private Integer skippedCount = 0;

    /**
     * 生成的凭证ID列表
     */
    private List<Long> generatedVoucherIds = new ArrayList<>();

    /**
     * 结转详情列表
     */
    private List<TransferDetail> transferDetails = new ArrayList<>();

    /**
     * 错误信息列表
     */
    private List<TransferError> errors = new ArrayList<>();

    /**
     * 警告信息列表
     */
    private List<String> warnings = new ArrayList<>();

    /**
     * 结转统计信息
     */
    private TransferStatistics statistics = new TransferStatistics();

    // 构造方法
    public TransferResult(Long tenantId, String period, TransferType transferType, String operator) {
        this.tenantId = tenantId;
        this.period = period;
        this.transferType = transferType;
        this.operator = operator;
    }

    // 业务方法

    /**
     * 添加结转详情
     */
    public void addTransferDetail(TransferDetail detail) {
        this.transferDetails.add(detail);
        this.subjectCount++;

        if (detail.getSuccess()) {
            this.successCount++;
            this.totalAmount = this.totalAmount.add(detail.getTransferAmount());

            // 记录生成的凭证
            if (detail.getVoucherId() != null && !generatedVoucherIds.contains(detail.getVoucherId())) {
                this.generatedVoucherIds.add(detail.getVoucherId());
            }
        } else {
            this.failedCount++;
            addError(detail.getSubjectId(), detail.getSubjectName(), detail.getErrorMessage());
        }

        // 更新统计信息
        updateStatistics();
    }


    /**
     * 添加错误信息
     */
    public void addError(Long subjectId, String subjectName, String errorMessage) {
        TransferError error = new TransferError();
        error.setSubjectId(subjectId);
        error.setSubjectName(subjectName);
        error.setErrorMessage(errorMessage);
        error.setErrorTime(new Date());

        this.errors.add(error);

        // 更新消息
        if (this.message == null) {
            this.message = errorMessage;
        } else {
            this.message += "; " + errorMessage;
        }
    }

    /**
     * 添加警告信息
     */
    public void addWarning(String warning) {
        this.warnings.add(warning);
    }

    /**
     * 设置结转成功
     */
    public void setSuccess() {
        this.success = true;
        if (this.message == null) {
            this.message = String.format("%s结转完成", transferType.getDescription());
        }
        this.statistics.complete();
    }

    /**
     * 设置结转失败
     */
    public void setFailure(String errorMessage) {
        this.success = false;
        this.message = errorMessage;
        this.statistics.complete();
    }

    /**
     * 更新统计信息
     */
    private void updateStatistics() {
        this.statistics.setTotalSubjects(this.subjectCount);
        this.statistics.setSuccessSubjectCount(this.successCount);
        this.statistics.setFailedSubjectCount(this.failedCount);
        this.statistics.setTotalTransferAmount(this.totalAmount);

        // 自动判断成功状态
        if (this.failedCount == 0) {
            this.success = true;
        } else {
            this.success = false;
        }
    }

    /**
     * 获取成功率
     */
    public BigDecimal getSuccessRate() {
        if (subjectCount == 0) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf(successCount + skippedCount)
                .divide(BigDecimal.valueOf(subjectCount), 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
    }

    /**
     * 是否有错误
     */
    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    /**
     * 是否有警告
     */
    public boolean hasWarnings() {
        return !warnings.isEmpty();
    }

    /**
     * 获取结转摘要
     */
    public String getSummary() {
        if (!success) {
            return String.format("%s结转失败: %s (成功%d个，失败%d个)",
                    transferType.getDescription(), message, successCount, failedCount);
        }

        return String.format("%s结转成功: 总共%d个科目，成功%d个，跳过%d个，结转金额%.2f",
                transferType.getDescription(), subjectCount, successCount, skippedCount, totalAmount);
    }

    /**
     * 获取成功结转的科目列表
     */
    public List<TransferDetail> getSuccessfulTransfers() {
        return transferDetails.stream()
                .filter(TransferDetail::getSuccess)
                .filter(detail -> !detail.getSkipped())
                .collect(Collectors.toList());
    }

    /**
     * 获取失败的结转列表
     */
    public List<TransferDetail> getFailedTransfers() {
        return transferDetails.stream()
                .filter(detail -> !detail.getSuccess())
                .collect(Collectors.toList());
    }

    /**
     * 获取跳过的科目列表
     */
    public List<TransferDetail> getSkippedTransfers() {
        return transferDetails.stream()
                .filter(TransferDetail::getSkipped)
                .collect(Collectors.toList());
    }

    /**
     * 验证结转结果
     */
    public ValidationResult validate() {
        List<String> validationErrors = new ArrayList<>();
        List<String> validationWarnings = new ArrayList<>();

        // 检查金额一致性
        BigDecimal calculatedTotal = transferDetails.stream()
                .filter(detail -> detail.getSuccess() && !detail.getSkipped())
                .map(TransferDetail::getTransferAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (calculatedTotal.compareTo(totalAmount) != 0) {
            validationErrors.add(String.format("金额不一致: 统计金额%.2f ≠ 明细合计金额%.2f",
                    totalAmount, calculatedTotal));
        }

        // 检查计数一致性
        int actualSuccessCount = (int) transferDetails.stream()
                .filter(TransferDetail::getSuccess)
                .count();

        if (actualSuccessCount != successCount) {
            validationErrors.add(String.format("成功计数不一致: %d ≠ %d", successCount, actualSuccessCount));
        }

        // 检查是否有成功但金额为0的结转（可能有问题）
        long zeroAmountSuccess = transferDetails.stream()
                .filter(TransferDetail::getSuccess)
                .filter(detail -> !detail.getSkipped())
                .filter(detail -> detail.getTransferAmount().compareTo(BigDecimal.ZERO) == 0)
                .count();

        if (zeroAmountSuccess > 0) {
            validationWarnings.add(String.format("有%d个成功结转的科目金额为0", zeroAmountSuccess));
        }

        return new ValidationResult(validationErrors.isEmpty(),
                validationErrors.isEmpty() ? null : String.join("; ", validationErrors),
                validationWarnings);
    }

    /**
     * 合并另一个结转结果（用于批量操作）
     */
    public void merge(TransferResult other) {
        if (!this.tenantId.equals(other.getTenantId()) || !this.period.equals(other.getPeriod())) {
            throw new IllegalArgumentException("只能合并相同租户和期间的结转结果");
        }

        this.transferDetails.addAll(other.getTransferDetails());
        this.generatedVoucherIds.addAll(other.getGeneratedVoucherIds());
        this.errors.addAll(other.getErrors());
        this.warnings.addAll(other.getWarnings());

        // 重新计算统计
        this.subjectCount += other.getSubjectCount();
        this.successCount += other.getSuccessCount();
        this.failedCount += other.getFailedCount();
        this.skippedCount += other.getSkippedCount();
        this.totalAmount = this.totalAmount.add(other.getTotalAmount());

        updateStatistics();

        // 更新消息
        if (this.message == null) {
            this.message = other.getMessage();
        } else if (other.getMessage() != null) {
            this.message += "; " + other.getMessage();
        }
    }


}
