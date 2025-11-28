package com.wimoor.finance.closing.domain;

import com.wimoor.finance.voucher.domain.SubjectType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfitLossTransferResult {

    /**
     * 结转是否成功
     */
    private Boolean success = false;

    /**
     * 结转结果消息
     */
    private String message;

    /**
     * 结转的会计期间
     */
    private String period;

    /**
     * 租户ID
     */
    private String groupid;

    /**
     * 操作人
     */
    private String operator;

    /**
     * 结转时间
     */
    private Date transferTime = new Date();

    /**
     * 本期净利润（收入-费用）
     */
    private BigDecimal netProfit = BigDecimal.ZERO;

    /**
     * 收入结转汇总
     */
    private TransferSummary incomeSummary = new TransferSummary();

    /**
     * 费用结转汇总
     */
    private TransferSummary expenseSummary = new TransferSummary();

    /**
     * 生成的结转凭证列表
     */
    private List<TransferVoucherInfo> generatedVouchers = new ArrayList<TransferVoucherInfo>();

    /**
     * 结转详情列表
     */
    private List<TransferDetail> transferDetails = new ArrayList<>();

    /**
     * 结转统计信息
     */
    private TransferStatistics statistics = new TransferStatistics();

    /**
     * 错误信息列表
     */
    private List<String> errors = new ArrayList<>();

    /**
     * 警告信息列表
     */
    private List<String> warnings = new ArrayList<>();

    // 构造方法
    public ProfitLossTransferResult(String groupid, String period, String operator) {
        this.groupid = groupid;
        this.period = period;
        this.operator = operator;
    }

    // 业务方法

    /**
     * 添加生成的结转凭证
     */
    public void addGeneratedVoucher(TransferVoucherInfo voucherInfo) {
        this.generatedVouchers.add(voucherInfo);
        this.statistics.incrementVoucherCount();
        this.statistics.addTotalAmount(voucherInfo.getAmount());
    }

    /**
     * 添加上述结转详情
     */
    public void addTransferDetail(TransferDetail detail) {
        this.transferDetails.add(detail);

        // 更新汇总信息
        if (detail.getSubjectType() == SubjectType.INCOME) {
            incomeSummary.addTransferDetail(detail);
        } else if (detail.getSubjectType() == SubjectType.EXPENSE) {
            expenseSummary.addTransferDetail(detail);
        }

        statistics.incrementSubjectCount();
        statistics.addTotalTransferAmount(detail.getTransferAmount());
    }

    /**
     * 添加错误信息
     */
    public void addError(String error) {
        this.errors.add(error);
        this.success = false;
        if (this.message == null) {
            this.message = error;
        } else {
            this.message += "; " + error;
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
        this.message = "损益结转完成";
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
     * 计算净利润
     */
    public void calculateNetProfit() {
        BigDecimal totalIncome = incomeSummary.getTotalAmount();
        BigDecimal totalExpense = expenseSummary.getTotalAmount();
        this.netProfit = totalIncome.subtract(totalExpense);
    }

    /**
     * 是否有生成的凭证
     */
    public boolean hasGeneratedVouchers() {
        return !generatedVouchers.isEmpty();
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
            return String.format("损益结转失败: %s", message);
        }

        return String.format("损益结转成功: 收入科目%d个(%.2f), 费用科目%d个(%.2f), 净利润%.2f, 生成凭证%d张",
                incomeSummary.getSubjectCount(), incomeSummary.getTotalAmount(),
                expenseSummary.getSubjectCount(), expenseSummary.getTotalAmount(),
                netProfit, generatedVouchers.size());
    }

    /**
     * 获取盈利状态
     */
    public ProfitStatus getProfitStatus() {
        if (netProfit.compareTo(BigDecimal.ZERO) > 0) {
            return ProfitStatus.PROFIT;
        } else if (netProfit.compareTo(BigDecimal.ZERO) < 0) {
            return ProfitStatus.LOSS;
        } else {
            return ProfitStatus.BREAK_EVEN;
        }
    }

    public Boolean isSuccess() {
        return success;
    }
}