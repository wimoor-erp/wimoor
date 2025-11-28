package com.wimoor.finance.closing.service;

import com.wimoor.common.mvc.BizException;
import com.wimoor.finance.closing.domain.*;
import com.wimoor.finance.ledger.domain.FinGeneralLedger;
import com.wimoor.finance.ledger.mapper.FinGeneralLedgerMapper;
import com.wimoor.finance.setting.domain.FinAccountingPeriods;
import com.wimoor.finance.setting.domain.FinAccountingSubjects;
import com.wimoor.finance.setting.domain.PeriodStatus;
import com.wimoor.finance.setting.mapper.FinAccountingPeriodsMapper;
import com.wimoor.finance.setting.service.IFinAccountingSubjectsService;
import com.wimoor.finance.voucher.domain.FinVoucherEntries;
import com.wimoor.finance.voucher.domain.SubjectType;
import com.wimoor.finance.voucher.service.IFinVoucherEntriesService;
import com.wimoor.finance.voucher.service.IFinVouchersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class AccountingClosingService {

    @Autowired
    private FinAccountingPeriodsMapper accAccountingPeriodMapper;

    @Autowired
    private FinGeneralLedgerMapper finGeneralLedgerMapper;

    @Autowired
    private PreClosingCheckService preClosingCheckService;

    @Autowired
    private ProfitLossTransferService profitLossTransferService;

    @Autowired
    private ClosingReportService closingReportService;

    @Autowired
    private IFinVouchersService voucherService;
    @Autowired
    private IFinVoucherEntriesService finVoucherEntriesService;
    @Autowired
    private IFinAccountingSubjectsService finAccountingSubjectsService;

    /**
     * 执行完整结账流程
     */
    @Transactional(rollbackFor = Exception.class)
    public ClosingResult executeFullClosing(String groupid, String period, String operator) {
        log.info("开始执行结账流程: groupid={}, period={}, operator={}", groupid, period, operator);

        ClosingContext context = new ClosingContext(groupid, period, operator);

        try {
            // 1. 初始化检查
            initializeClosing(context);

            // 2. 结账前检查
            performPreClosingChecks(context);

            // 3. 结转损益
            transferProfitAndLoss(context);

            // 4. 计算期末余额
            calculateEndingBalances(context);

            // 5. 生成下期期初
            generateNextPeriodOpening(context);

            // 6. 更新期间状态
            updatePeriodStatus(context);

            // 7. 生成结账报告
            generateClosingReport(context);

            log.info("结账流程完成: groupid={}, period={}", groupid, period);
            return ClosingResult.success("结账成功", context.getReport());

        } catch (BizException e) {
            log.error("结账流程失败: groupid={}, period={}", groupid, period, e);
            handleClosingFailure(context, e);
            return ClosingResult.fail(e.getMessage());
        } catch (Exception e) {
            log.error("结账系统异常: groupid={}, period={}", groupid, period, e);
            // 添加状态恢复
            try {
                accAccountingPeriodMapper.updatePeriodStatus(
                        groupid, period, PeriodStatus.OPEN.getValue(), operator);
            } catch (Exception recoveryEx) {
                log.error("恢复期间状态失败", recoveryEx);
            }
            return ClosingResult.fail("系统异常: " + e.getMessage());
        }
    }

    /**
     * 初始化结账环境
     */
    private void initializeClosing(ClosingContext context) {
        // 检查期间是否存在
        FinAccountingPeriods accountingPeriod = accAccountingPeriodMapper.selectByPeriod(
                context.getGroupid(), context.getPeriod());

        if (accountingPeriod == null) {
            throw new BizException("会计期间不存在: " + context.getPeriod());
        }

        if (Objects.equals(PeriodStatus.CLOSED.getValue(), accountingPeriod.getPeriodStatus())) {
            throw new BizException("会计期间已结账: " + context.getPeriod());
        }

        if (Objects.equals(PeriodStatus.CLOSED.getValue(), accountingPeriod.getPeriodStatus())) {
            throw new BizException("会计期间正在结账中: " + context.getPeriod());
        }

        context.setAccountingPeriod(accountingPeriod);

        // 更新期间状态为结账中
        accAccountingPeriodMapper.updatePeriodStatus(
                context.getGroupid(),
                context.getPeriod(),
                PeriodStatus.CLOSED.getValue(),
                context.getOperator()
        );

        log.info("结账环境初始化完成: period={}", context.getPeriod());
    }

    /**
     * 执行结账前检查
     */
    private void performPreClosingChecks(ClosingContext context) {
        log.info("开始结账前检查: period={}", context.getPeriod());

        PreClosingCheckResult checkResult = preClosingCheckService.performComprehensiveCheck(
                context.getGroupid(), context.getPeriod());

        context.setPreCheckResult(checkResult);

        if (!checkResult.isPassed()) {
            throw new BizException("结账前检查失败: " + checkResult.getErrorMessage());
        }

        // 记录警告信息
        for (String warning : checkResult.getWarnings()) {
            context.addWarning(warning);
        }

        log.info("结账前检查完成: period={}, 检查项={}", context.getPeriod(), checkResult.getCheckItems().size());
    }

    /**
     * 结转损益
     */
    private void transferProfitAndLoss(ClosingContext context) {
        log.info("开始结转损益: period={}", context.getPeriod());

        ProfitLossTransferResult transferResult = profitLossTransferService.transferProfitAndLoss(
                context.getGroupid(), context.getPeriod());

        if (!transferResult.isSuccess()) {
            throw new BizException("损益结转失败: " + transferResult.getMessage());
        }

        context.setTransferResult(transferResult);
        // 直接创建 ClosingVoucherInfo
        for (TransferVoucherInfo transferVoucher : transferResult.getGeneratedVouchers()) {
            validateVoucherBalance(transferVoucher.getVoucherId());
            ClosingVoucherInfo closingVoucher = new ClosingVoucherInfo();
            closingVoucher.setVoucherId(transferVoucher.getVoucherId());
            closingVoucher.setVoucherNo(transferVoucher.getVoucherNo());
            closingVoucher.setVoucherType(transferVoucher.getVoucherType());
            closingVoucher.setAmount(transferVoucher.getAmount());
            closingVoucher.setDescription(transferVoucher.getDescription());
            context.getGeneratedVouchers().add(closingVoucher);
        }


        log.info("损益结转完成: period={}, 生成凭证={}张", context.getPeriod(),
                transferResult.getGeneratedVouchers().size());
    }

    // 实现validateVoucherBalance方法
    private void validateVoucherBalance(Long voucherId) {
        // 查询该凭证的所有分录
        FinVoucherEntries entryiesQuery=new FinVoucherEntries();
        entryiesQuery.setVoucherId(voucherId);
        List<FinVoucherEntries> entries = finVoucherEntriesService.selectFinVoucherEntriesList(entryiesQuery);

        if (entries == null || entries.isEmpty()) {
            throw new BizException("凭证不存在分录: " + voucherId);
        }

        // 计算借方总金额
        BigDecimal debitTotal = BigDecimal.ZERO;
        // 计算贷方总金额
        BigDecimal creditTotal = BigDecimal.ZERO;

        for (FinVoucherEntries entry : entries) {
            if (entry.getDebitAmount() != null) {
                debitTotal = debitTotal.add(entry.getDebitAmount());
            }
            if (entry.getCreditAmount() != null) {
                creditTotal = creditTotal.add(entry.getCreditAmount());
            }
        }

        // 比较借贷方金额是否相等
        if (!debitTotal.equals(creditTotal)) {
            throw new BizException("凭证借贷不平衡: 凭证ID=" + voucherId + ", 借方总额=" + debitTotal + ", 贷方总额=" + creditTotal);
        }

        log.info("凭证借贷平衡验证通过: 凭证ID={}, 借贷总额={}", voucherId, debitTotal);
    }
    /**
     * 计算期末余额
     */
    private void calculateEndingBalances(ClosingContext context) {
        log.info("开始计算期末余额: period={}", context.getPeriod());

        List<FinGeneralLedger> ledgers = finGeneralLedgerMapper.selectByPeriod(
                context.getGroupid(), context.getPeriod());

        int updatedCount = 0;
        for (FinGeneralLedger ledger : ledgers) {
            // 计算期末余额
            EndBalanceResult balanceResult = calculateSubjectEndBalance(ledger);

            // 更新总账
            ledger.setEndBalance(balanceResult.getBalance());
            ledger.setEndDirection(balanceResult.getDirection());
            ledger.setUpdatedTime(new Date());

            finGeneralLedgerMapper.updateFinGeneralLedger(ledger);
            updatedCount++;
        }

        log.info("期末余额计算完成: period={}, 更新科目={}个", context.getPeriod(), updatedCount);
    }

    /**
     * 生成下期期初余额
     */
    private void generateNextPeriodOpening(ClosingContext context) {
        String nextPeriod = getNextPeriod(context.getPeriod());
        log.info("开始生成下期期初: currentPeriod={}, nextPeriod={}", context.getPeriod(), nextPeriod);

        // 检查下期期间是否存在
        FinAccountingPeriods nextAccountingPeriod = accAccountingPeriodMapper.selectByPeriod(
                context.getGroupid(), nextPeriod);

        if (nextAccountingPeriod == null) {
            log.warn("下个会计期间不存在: {}", nextPeriod);
            context.addWarning("下个会计期间不存在，无法生成期初余额");
            return;
        }

        // 删除可能存在的期初数据（防止重复结账）
        finGeneralLedgerMapper.deleteByPeriod(context.getGroupid(), nextPeriod);

        // 从当前期间复制期末余额作为下期期初
        List<FinGeneralLedger> currentLedgers = finGeneralLedgerMapper.selectByPeriod(
                context.getGroupid(), context.getPeriod());

        int createdCount = 0;
        for (FinGeneralLedger current : currentLedgers) {
            FinGeneralLedger nextLedger = new FinGeneralLedger();
            nextLedger.setGroupid(context.getGroupid());
            nextLedger.setSubjectId(current.getSubjectId());
            nextLedger.setPeriod(nextPeriod);
            nextLedger.setBeginBalance(current.getEndBalance());
            nextLedger.setBeginDirection(current.getEndDirection());
            nextLedger.setDebitTotal(BigDecimal.ZERO);
            nextLedger.setCreditTotal(BigDecimal.ZERO);
            nextLedger.setEndBalance(current.getEndBalance());
            nextLedger.setEndDirection(current.getEndDirection());
            nextLedger.setCreatedTime(new Date());
            nextLedger.setUpdatedTime(new Date());

            finGeneralLedgerMapper.insertFinGeneralLedger(nextLedger);
            createdCount++;
        }

        log.info("下期期初生成完成: nextPeriod={}, 创建科目={}个", nextPeriod, createdCount);
    }

    /**
     * 更新期间状态
     */
    private void updatePeriodStatus(ClosingContext context) {
        accAccountingPeriodMapper.updatePeriodStatus(
                context.getGroupid(),
                context.getPeriod(),
                PeriodStatus.CLOSED.getValue(),
                context.getOperator()
        );

        // 更新期间结账时间
        FinAccountingPeriods period = context.getAccountingPeriod();
        period.setPeriodStatus(3);
        period.setIsCurrent(0);
        period.setUpdateTime(new Date());
        period.setUpdateBy(context.getOperator());
        accAccountingPeriodMapper.updateFinAccountingPeriods(period);

        log.info("期间状态更新完成: period={}, status=CLOSED", context.getPeriod());
    }

    /**
     * 生成结账报告
     */
    private void generateClosingReport(ClosingContext context) {
        ClosingReport report = closingReportService.generateClosingReport(
                context.getGroupid(), context.getPeriod(), context.getOperator());

        // 添加检查结果
        for (ClosingCheckItem checkItem : context.getPreCheckResult().getCheckItems()) {
            report.getCheckItems().add(checkItem);
        }

        // 添加生成的凭证
        report.getGeneratedVouchers().addAll(context.getGeneratedVouchers());

        // 添加警告信息
        for (String warning : context.getWarnings()) {
            report.getCheckItems().add(new ClosingCheckItem("系统警告", false, warning));
        }

        context.setReport(report);
        log.info("结账报告生成完成: period={}", context.getPeriod());
    }

    /**
     * 处理结账失败
     */
    private void handleClosingFailure(ClosingContext context, BizException e) {
        // 恢复期间状态为OPEN
        accAccountingPeriodMapper.updatePeriodStatus(
                context.getGroupid(),
                context.getPeriod(),
                PeriodStatus.OPEN.getValue(),
                context.getOperator()
        );

        log.error("结账失败处理完成: period={}, 状态已恢复为OPEN", context.getPeriod());
    }

    /**
     * 计算科目期末余额
     */
    private EndBalanceResult calculateSubjectEndBalance(FinGeneralLedger ledger) {
        FinAccountingSubjects subject = getSubjectById(String.valueOf(ledger.getSubjectId()));
        SubjectType subjectType = SubjectType.valueOf(subject);
        BigDecimal endBalance;
        Integer endDirection;

        if (subjectType.isDebitBalance()) {
            // 借方余额科目：期初借方 + 本期借方 - 本期贷方
            BigDecimal beginBalance = ledger.getBeginDirection() == 1 ?
                    ledger.getBeginBalance() : ledger.getBeginBalance().negate();
            endBalance = beginBalance.add(ledger.getDebitTotal()).subtract(ledger.getCreditTotal());
        } else {
            // 贷方余额科目：期初贷方 + 本期贷方 - 本期借方
            BigDecimal beginBalance = ledger.getBeginDirection() == 2 ?
                    ledger.getBeginBalance() : ledger.getBeginBalance().negate();
            endBalance = beginBalance.add(ledger.getCreditTotal()).subtract(ledger.getDebitTotal());
        }

        // 确定余额方向
        if (endBalance.compareTo(BigDecimal.ZERO) == 0) {
            endDirection = subjectType.getDefaultDirection();
        } else if (endBalance.compareTo(BigDecimal.ZERO) > 0) {
            endDirection = subjectType.isDebitBalance() ? 1 : 2;
        } else {
            endDirection = subjectType.isDebitBalance() ? 2 : 1;
            endBalance = endBalance.abs();
        }

        return new EndBalanceResult(endBalance, endDirection);
    }

    /**
     * 获取下个期间
     */
    private String getNextPeriod(String period) {
        try {
            int year = Integer.parseInt(period.substring(0, 4));
            int month = Integer.parseInt(period.substring(4, 6));

            if (month == 12) {
                year++;
                month = 1;
            } else {
                month++;
            }

            return String.format("%04d%02d", year, month);
        } catch (Exception e) {
            throw new BizException("期间格式错误: " + period);
        }
    }

    // 辅助方法
    private FinAccountingSubjects getSubjectById(String subjectId) {
        return finAccountingSubjectsService.selectFinAccountingSubjectsBySubjectId(subjectId.toString());
    }
}