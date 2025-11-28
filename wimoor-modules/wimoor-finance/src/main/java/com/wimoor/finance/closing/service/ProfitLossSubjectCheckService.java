package com.wimoor.finance.closing.service;

import com.wimoor.finance.closing.domain.*;
import com.wimoor.finance.ledger.domain.FinGeneralLedger;
import com.wimoor.finance.ledger.service.IFinGeneralLedgerService;
import com.wimoor.finance.setting.domain.FinAccountingSubjects;
import com.wimoor.finance.setting.service.IFinAccountingSubjectsService;
import com.wimoor.finance.voucher.domain.SubjectType;
import com.wimoor.finance.voucher.service.IFinVouchersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProfitLossSubjectCheckService {

    @Autowired
    private IFinGeneralLedgerService finGeneralLedgerService;

    @Autowired
    private IFinAccountingSubjectsService FinAccountingSubjectsService;

    @Autowired
    private IFinVouchersService finVouchersService;

    /**
     * 检查损益类科目
     */
    public void checkProfitLossSubjects(String groupid, String period, PreClosingCheckResult result) {
        log.info("开始检查损益类科目: groupid={}, period={}", groupid, period);

        ClosingCheckItem checkItem = new ClosingCheckItem();
        checkItem.setCheckItem("损益类科目检查");
        checkItem.setCheckTime(new Date());

        try {
            // 1. 获取所有损益类科目
            List<FinAccountingSubjects> profitLossSubjects = FinAccountingSubjectsService.getProfitLossSubjects(groupid);

            if (profitLossSubjects.isEmpty()) {
                checkItem.setPassed(true);
                checkItem.setMessage("未配置损益类科目");
                checkItem.setCheckLevel(CheckLevel.INFO);
                result.addCheckItem(checkItem);
                return;
            }

            // 2. 检查损益类科目余额
            ProfitLossCheckResult plResult = checkProfitLossBalances(groupid, period, profitLossSubjects);

            // 3. 检查损益结转状态
            checkProfitLossTransferStatus(groupid, period, plResult, checkItem);

            // 4. 检查收入费用配比
            checkIncomeExpenseMatching(groupid, period, plResult, checkItem);

            // 5. 检查异常余额
            checkAbnormalBalances(groupid, period, plResult, checkItem);

            // 汇总检查结果
            summarizeProfitLossCheck(plResult, checkItem);

            result.addCheckItem(checkItem);
            log.info("损益类科目检查完成: tenantId={}, period={}", groupid, period);

        } catch (Exception e) {
            log.error("损益类科目检查异常: tenantId={}, period={}", groupid, period, e);
            checkItem.setPassed(false);
            checkItem.setMessage("损益类科目检查异常: " + e.getMessage());
            checkItem.setCheckLevel(CheckLevel.ERROR);
            result.addCheckItem(checkItem);
        }
    }

    /**
     * 检查损益类科目余额
     */
    private ProfitLossCheckResult checkProfitLossBalances(String groupid, String period,
                                                          List<FinAccountingSubjects> profitLossSubjects) {
        ProfitLossCheckResult result = new ProfitLossCheckResult();
        result.setTotalSubjects(profitLossSubjects.size());

        List<SubjectBalanceInfo> balanceInfos = new ArrayList<>();
        BigDecimal totalIncome = BigDecimal.ZERO;
        BigDecimal totalExpense = BigDecimal.ZERO;

        for (FinAccountingSubjects subject : profitLossSubjects) {
            SubjectBalanceInfo balanceInfo = getSubjectBalanceInfo(groupid, period, subject);
            balanceInfos.add(balanceInfo);

            // 统计总额
            if (balanceInfo.getEndBalance().compareTo(BigDecimal.ZERO) != 0) {
                if (subject.getDirection() == 2) {
                    totalIncome = totalIncome.add(balanceInfo.getEndBalance());
                    result.getIncomeSubjects().add(balanceInfo);
                } else if (subject.getDirection() == 1) {
                    totalExpense = totalExpense.add(balanceInfo.getEndBalance());
                    result.getExpenseSubjects().add(balanceInfo);
                }
            }

            // 检查余额方向是否正确
            if (!isBalanceDirectionCorrect(subject, balanceInfo)) {
                result.getSubjectsWithWrongDirection().add(balanceInfo);
            }

            // 检查是否有发生额但余额为0（可能已结转）
            if (hasActivityButZeroBalance(balanceInfo)) {
                result.getSubjectsWithActivityButZeroBalance().add(balanceInfo);
            }
        }

        result.setBalanceInfos(balanceInfos);
        result.setTotalIncome(totalIncome);
        result.setTotalExpense(totalExpense);
        result.setNetProfit(totalIncome.subtract(totalExpense));
        result.setSubjectsWithBalance(
                (int) balanceInfos.stream()
                        .filter(info -> info.getEndBalance().compareTo(BigDecimal.ZERO) != 0)
                        .count()
        );

        return result;
    }

    /**
     * 获取科目余额信息
     */
    private SubjectBalanceInfo getSubjectBalanceInfo(String groupid, String period, FinAccountingSubjects subject) {
        SubjectBalanceInfo info = new SubjectBalanceInfo();
        info.setSubjectId(subject.getSubjectId());
        info.setSubjectCode(subject.getSubjectCode());
        info.setSubjectName(subject.getSubjectName());
        info.setSubjectType(SubjectType.valueOf(subject));

        // 查询总账余额
        FinGeneralLedger ledger = finGeneralLedgerService.selectBySubjectAndPeriod( groupid, subject.getSubjectId().toString(), period);
        if (ledger != null) {
            info.setBeginBalance(ledger.getBeginBalance());
            info.setBeginDirection(ledger.getBeginDirection());
            info.setDebitTotal(ledger.getDebitTotal());
            info.setCreditTotal(ledger.getCreditTotal());
            info.setEndBalance(ledger.getEndBalance());
            info.setEndDirection(ledger.getEndDirection());
        } else {
            // 无总账记录，余额为0
            info.setBeginBalance(BigDecimal.ZERO);
            info.setBeginDirection(1);
            info.setDebitTotal(BigDecimal.ZERO);
            info.setCreditTotal(BigDecimal.ZERO);
            info.setEndBalance(BigDecimal.ZERO);
            info.setEndDirection(1);
        }

        return info;
    }

    /**
     * 检查余额方向是否正确
     */
    private boolean isBalanceDirectionCorrect(FinAccountingSubjects subject, SubjectBalanceInfo balanceInfo) {
        if (balanceInfo.getEndBalance().compareTo(BigDecimal.ZERO) == 0) {
            return true; // 余额为0，方向无所谓
        }

        SubjectType subjectType = SubjectType.valueOf(subject);
        Integer actualDirection = balanceInfo.getEndDirection();
        // 收入类科目应该是贷方余额（正数）
        // 费用类科目应该是借方余额（正数）
        if (subjectType == SubjectType.INCOME) {
            return actualDirection == 2; // 贷方
        } else if (subjectType == SubjectType.EXPENSE) {
            return actualDirection == 1; // 借方
        }

        return true;
    }

    /**
     * 检查是否有发生额但余额为0
     */
    private boolean hasActivityButZeroBalance(SubjectBalanceInfo balanceInfo) {
        boolean hasDebitActivity = balanceInfo.getDebitTotal().compareTo(BigDecimal.ZERO) > 0;
        boolean hasCreditActivity = balanceInfo.getCreditTotal().compareTo(BigDecimal.ZERO) > 0;
        boolean hasZeroBalance = balanceInfo.getEndBalance().compareTo(BigDecimal.ZERO) == 0;

        return (hasDebitActivity || hasCreditActivity) && hasZeroBalance;
    }

    /**
     * 检查损益结转状态
     */
    private void checkProfitLossTransferStatus(String groupid, String period,
                                               ProfitLossCheckResult plResult, ClosingCheckItem checkItem) {
        // 检查是否有未结转的损益科目
        long unclosedSubjects = plResult.getBalanceInfos().stream()
                .filter(info -> info.getEndBalance().compareTo(BigDecimal.ZERO) != 0)
                .count();

        if (unclosedSubjects > 0) {
            checkItem.setMessage(String.format("发现%d个损益类科目有余额未结转", unclosedSubjects));
            checkItem.setCheckLevel(CheckLevel.WARNING);
            checkItem.addRelatedData("unclosedSubjects", unclosedSubjects);
            checkItem.addRelatedData("unclosedIncomeSubjects",
                    plResult.getIncomeSubjects().size());
            checkItem.addRelatedData("unclosedExpenseSubjects",
                    plResult.getExpenseSubjects().size());
        } else {
            checkItem.addRelatedData("profitLossTransfer", "所有损益科目余额已结转");
        }

        // 检查是否已生成损益结转凭证
        boolean hasTransferVoucher = checkProfitTransferVoucherExists(groupid, period);
        checkItem.addRelatedData("hasProfitTransferVoucher", hasTransferVoucher);

        if (unclosedSubjects > 0 && !hasTransferVoucher) {
            checkItem.setCheckLevel(CheckLevel.WARNING);
            if(checkItem.getMessage()!=null){
                  checkItem.setMessage(checkItem.getMessage()+"，存在未结转损益科目，但未找到损益结转凭证");
            }else{
                checkItem.setMessage("存在未结转损益科目，但未找到损益结转凭证");
            }
        }
    }

    /**
     * 检查收入费用配比
     */
    private void checkIncomeExpenseMatching(String groupid, String period,
                                            ProfitLossCheckResult plResult, ClosingCheckItem checkItem) {
        if (plResult.getTotalIncome().compareTo(BigDecimal.ZERO) == 0 &&
                plResult.getTotalExpense().compareTo(BigDecimal.ZERO) == 0) {
            checkItem.addRelatedData("incomeExpenseMatching", "无收入费用发生");
            return;
        }

        // 计算收入费用比率
        BigDecimal incomeExpenseRatio = BigDecimal.ZERO;
        if (plResult.getTotalIncome().compareTo(BigDecimal.ZERO) > 0) {
            incomeExpenseRatio = plResult.getTotalExpense()
                    .multiply(BigDecimal.valueOf(100))
                    .divide(plResult.getTotalIncome(), 2, RoundingMode.HALF_UP);
        }

        checkItem.addRelatedData("incomeExpenseRatio", incomeExpenseRatio);
        checkItem.addRelatedData("totalIncome", plResult.getTotalIncome());
        checkItem.addRelatedData("totalExpense", plResult.getTotalExpense());
        checkItem.addRelatedData("netProfit", plResult.getNetProfit());

        // 检查异常配比
        if (plResult.getTotalIncome().compareTo(BigDecimal.ZERO) > 0 &&
                incomeExpenseRatio.compareTo(BigDecimal.valueOf(90)) > 0) {
            checkItem.setMessage("费用占收入比例过高(" + incomeExpenseRatio + "%)");
            checkItem.setCheckLevel(CheckLevel.WARNING);
        }

        if (plResult.getTotalExpense().compareTo(BigDecimal.ZERO) > 0 &&
                plResult.getTotalIncome().compareTo(BigDecimal.ZERO) == 0) {
            checkItem.setMessage("有费用发生但无收入");
            checkItem.setCheckLevel(CheckLevel.WARNING);
        }
    }

    /**
     * 检查异常余额
     */
    private void checkAbnormalBalances(String groupid, String period,
                                       ProfitLossCheckResult plResult, ClosingCheckItem checkItem) {
        List<SubjectBalanceInfo> abnormalBalances = new ArrayList<>();

        for (SubjectBalanceInfo balanceInfo : plResult.getBalanceInfos()) {
            // 检查收入类科目借方余额（异常）
            if (balanceInfo.getSubjectType() == SubjectType.INCOME &&
                    balanceInfo.getEndDirection() == 1 &&
                    balanceInfo.getEndBalance().compareTo(BigDecimal.ZERO) > 0) {
                abnormalBalances.add(balanceInfo);
            }

            // 检查费用类科目贷方余额（异常）
            if (balanceInfo.getSubjectType() == SubjectType.EXPENSE &&
                    balanceInfo.getEndDirection() == 2 &&
                    balanceInfo.getEndBalance().compareTo(BigDecimal.ZERO) > 0) {
                abnormalBalances.add(balanceInfo);
            }

            // 检查异常大的余额
            if (isAbnormallyLargeBalance(balanceInfo)) {
                abnormalBalances.add(balanceInfo);
            }
        }

        if (!abnormalBalances.isEmpty()) {
            checkItem.setMessage("发现" + abnormalBalances.size() + "个科目余额异常");
            checkItem.setCheckLevel(CheckLevel.WARNING);
            checkItem.addRelatedData("abnormalBalances",
                    abnormalBalances.stream()
                            .map(info -> info.getSubjectName() + "(" + info.getEndBalance() + ")")
                            .collect(Collectors.toList()));
        }

        // 检查方向错误的科目
        if (!plResult.getSubjectsWithWrongDirection().isEmpty()) {
            checkItem.setMessage("发现" + plResult.getSubjectsWithWrongDirection().size() + "个科目余额方向错误");
            checkItem.setCheckLevel(CheckLevel.WARNING);
        }
    }

    /**
     * 汇总损益检查结果
     */
    private void summarizeProfitLossCheck(ProfitLossCheckResult plResult, ClosingCheckItem checkItem) {
        // 检查是否所有损益科目都已结转（余额为0）
        boolean allClosed = plResult.getSubjectsWithBalance() == 0;

        if (allClosed) {
            checkItem.setPassed(true);
            checkItem.setMessage("损益类科目检查通过，所有科目余额已结转");
            checkItem.setCheckLevel(CheckLevel.INFO);
        } else {
            checkItem.setPassed(true); // 不是错误，只是警告
            checkItem.setCheckLevel(CheckLevel.WARNING);
            checkItem.setMessage(String.format(
                    "发现%d个损益科目有余额未结转(收入%d个, 费用%d个), 净利润: %.2f",
                    plResult.getSubjectsWithBalance(),
                    plResult.getIncomeSubjects().size(),
                    plResult.getExpenseSubjects().size(),
                    plResult.getNetProfit().doubleValue()
            ));
            checkItem.setSuggestion("请执行损益结转操作");
        }

        // 添加详细统计信息
        checkItem.addRelatedData("totalProfitLossSubjects", plResult.getTotalSubjects());
        checkItem.addRelatedData("subjectsWithBalance", plResult.getSubjectsWithBalance());
        checkItem.addRelatedData("subjectsWithWrongDirection",
                plResult.getSubjectsWithWrongDirection().size());
        checkItem.addRelatedData("subjectsWithActivityButZeroBalance",
                plResult.getSubjectsWithActivityButZeroBalance().size());
    }

    /**
     * 检查是否已存在损益结转凭证
     */
    private boolean checkProfitTransferVoucherExists(String groupid, String period) {
        Integer count = finVouchersService.countProfitTransferVouchers(groupid, period);
        return count != null && count > 0;
    }

    /**
     * 检查异常大余额
     */
    private boolean isAbnormallyLargeBalance(SubjectBalanceInfo balanceInfo) {
        // 这里可以根据企业规模设置阈值
        BigDecimal threshold = new BigDecimal("1000000.00"); // 100万
        return balanceInfo.getEndBalance().compareTo(threshold) > 0;
    }
}