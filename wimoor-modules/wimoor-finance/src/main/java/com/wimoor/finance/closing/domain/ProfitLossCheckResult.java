package com.wimoor.finance.closing.domain;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class ProfitLossCheckResult {

    /**
     * 损益科目总数
     */
    private Integer totalSubjects = 0;

    /**
     * 有余额的科目数量
     */
    private Integer subjectsWithBalance = 0;

    /**
     * 收入总额
     */
    private BigDecimal totalIncome = BigDecimal.ZERO;

    /**
     * 费用总额
     */
    private BigDecimal totalExpense = BigDecimal.ZERO;

    /**
     * 净利润
     */
    private BigDecimal netProfit = BigDecimal.ZERO;

    /**
     * 所有科目余额信息
     */
    private List<SubjectBalanceInfo> balanceInfos = new ArrayList<>();

    /**
     * 有余额的收入类科目
     */
    private List<SubjectBalanceInfo> incomeSubjects = new ArrayList<>();

    /**
     * 有余额的费用类科目
     */
    private List<SubjectBalanceInfo> expenseSubjects = new ArrayList<>();

    /**
     * 余额方向错误的科目
     */
    private List<SubjectBalanceInfo> subjectsWithWrongDirection = new ArrayList<>();

    /**
     * 有发生额但余额为0的科目
     */
    private List<SubjectBalanceInfo> subjectsWithActivityButZeroBalance = new ArrayList<>();

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
}