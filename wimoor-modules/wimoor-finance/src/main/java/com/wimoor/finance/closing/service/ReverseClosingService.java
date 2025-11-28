package com.wimoor.finance.closing.service;

import com.wimoor.finance.closing.domain.*;
import com.wimoor.finance.ledger.domain.FinGeneralLedger;
import com.wimoor.finance.ledger.service.IFinDetailLedgerService;
import com.wimoor.finance.ledger.service.IFinGeneralLedgerService;
import com.wimoor.finance.setting.domain.FinAccountingPeriods;
import com.wimoor.finance.setting.domain.FinAccountingSubjects;
import com.wimoor.finance.setting.domain.PeriodStatus;
import com.wimoor.finance.setting.service.IFinAccountingPeriodsService;
import com.wimoor.finance.setting.service.IFinAccountingSubjectsService;
import com.wimoor.finance.voucher.domain.FinVouchers;
import com.wimoor.finance.voucher.domain.SubjectType;
import com.wimoor.finance.voucher.service.IFinVoucherEntriesService;
import com.wimoor.finance.voucher.service.IFinVouchersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class ReverseClosingService {

    @Autowired
    private IFinAccountingPeriodsService iFinAccountingPeriodsService;

    @Autowired
    private IFinVouchersService finVouchersService;

    @Autowired
    private IFinVoucherEntriesService iFinVoucherEntriesService;

    @Autowired
    private IFinGeneralLedgerService iFinGeneralLedgerService;

    @Autowired
    private IFinDetailLedgerService iFinDetailLedgerService;
    @Autowired
    private IFinAccountingSubjectsService accountingSubjectService;

    /**
     * 执行反结账操作
     */
    public ReverseClosingResult executeReverseClosing(String groupid, String period, String operator, String reason) {
        log.info("开始执行反结账: groupid={}, period={}, operator={}", groupid, period, operator);

        ReverseClosingResult result = new ReverseClosingResult(groupid, period, operator, reason);

        try {
            // 1. 权限验证
            if (!validateReverseClosingPermission(groupid, operator)) {
                return result.fail("无反结账权限");
            }

            // 2. 前置检查
            PreReverseCheckResult preCheck = performPreReverseCheck(groupid, period);
            if (!preCheck.isPassed()) {
                return result.fail("反结账前检查失败: " + preCheck.getErrorMessage());
            }

            // 3. 锁定期间（防止并发操作）
            lockAccountingPeriod(groupid, period);

            // 4. 执行反结账核心操作
            executeReverseClosingCore(groupid, period, result);

            // 5. 更新期间状态
            updatePeriodStatus(groupid, period, PeriodStatus.CLOSED.getValue(), operator);

            result.setSuccess(true);
            log.info("反结账完成: groupid={}, period={}", groupid, period);

        } catch (ReverseClosingException e) {
            log.error("反结账业务异常: groupid={}, period={}", groupid, period, e);
            handleReverseClosingFailure(groupid, period, result, e);
        } catch (Exception e) {
            log.error("反结账系统异常: tenantId={}, period={}", groupid, period, e);
            result.fail("反结账系统异常: " + e.getMessage());
        }

        return result;
    }

    /**
     * 反结账前置检查
     */
    private PreReverseCheckResult performPreReverseCheck(String groupid, String period) {
        PreReverseCheckResult checkResult = new PreReverseCheckResult();

        // 1. 检查期间是否存在且已结账
        FinAccountingPeriods accountingPeriod = iFinAccountingPeriodsService.selectByPeriod(groupid, period);
        if (accountingPeriod == null) {
            return checkResult.fail("会计期间不存在: " + period);
        }

        if (!PeriodStatus.CLOSED.getValue().equals(accountingPeriod.getPeriodStatus())) {
            return checkResult.fail("会计期间未结账，无需反结账: " + period);
        }

        checkResult.setAccountingPeriod(accountingPeriod);

        // 2. 检查是否有后续期间已结账
        List<FinAccountingPeriods> subsequentClosedPeriods =
                iFinAccountingPeriodsService.selectSubsequentClosedPeriods(groupid, period);
        if (!subsequentClosedPeriods.isEmpty()) {
            String subsequentPeriods = subsequentClosedPeriods.stream()
                    .map(FinAccountingPeriods::getPeriodCode)
                    .collect(Collectors.joining(", "));
            return checkResult.fail("存在已结账的后续期间，请先反结账后续期间: " + subsequentPeriods);
        }

        // 3. 检查是否有凭证在结账后新增或修改
        List<FinVouchers> modifiedVouchers = finVouchersService.selectVouchersModifiedAfterClosing(
                groupid, period, accountingPeriod.getCloseTime());
        if (!modifiedVouchers.isEmpty()) {
            checkResult.addWarning("结账后有凭证被修改，反结账可能会影响数据一致性");
            checkResult.setModifiedVouchers(modifiedVouchers);
        }

        // 4. 检查是否有依赖数据
        if (hasDependentData(groupid, period)) {
            return checkResult.fail("存在依赖数据，不能直接反结账");
        }

        return checkResult.success();
    }

    /**
     * 执行反结账核心操作
     */
    private void executeReverseClosingCore(String groupid, String period, ReverseClosingResult result) {
        // 1. 删除损益结转凭证
        deleteProfitTransferVouchers(groupid, period, result);

        // 2. 恢复损益科目余额
       // restoreProfitLossSubjectBalances(groupid, period, result);

        // 3. 删除下期期初余额
        deleteNextPeriodOpeningBalance(groupid, period, result);

        // 4. 清理结账生成的账簿数据
        cleanClosingGeneratedData(groupid, period, result);

        // 5. 恢复凭证状态（如有需要）
        restoreVoucherStatus(groupid, period, result);
    }

    /**
     * 删除损益结转凭证
     */
    private void deleteProfitTransferVouchers(String groupid, String period, ReverseClosingResult result) {
        log.info("开始删除损益结转凭证: groupid={}, period={}", groupid, period);

        // 查找损益结转凭证
        List<FinVouchers> transferVouchers = finVouchersService.selectProfitTransferVouchers(groupid, period);

        int deletedVoucherCount = 0;
        int deletedEntryCount = 0;

        for (FinVouchers voucher : transferVouchers) {
            // 删除凭证分录
            finVouchersService.reversePosting(voucher.getVoucherId());
            int entryCount = iFinVoucherEntriesService.deleteByVoucherId(voucher.getVoucherId());
            deletedEntryCount += entryCount;

            // 删除凭证主表
            int voucherCount = finVouchersService.deleteFinVouchersByVoucherId(voucher.getVoucherId());
            deletedVoucherCount += voucherCount;

            // 记录删除的凭证信息
            result.addDeletedVoucher(new DeletedVoucherInfo(
                    voucher.getVoucherId(), voucher.getVoucherNo(), voucher.getVoucherType(), entryCount
            ));
        }

        result.setDeletedVoucherCount(deletedVoucherCount);
        result.setDeletedEntryCount(deletedEntryCount);

        log.info("删除损益结转凭证完成: 凭证{}张, 分录{}条", deletedVoucherCount, deletedEntryCount);
    }

    /**
     * 恢复损益科目余额
     */
    private void restoreProfitLossSubjectBalances(String groupid, String period, ReverseClosingResult result) {
        log.info("开始恢复损益科目余额: groupid={}, period={}", groupid, period);

        // 获取所有损益类科目
        List<FinAccountingSubjects> profitLossSubjects = accountingSubjectService.getProfitLossSubjects(groupid);

        int restoredSubjectCount = 0;

        for (FinAccountingSubjects subject : profitLossSubjects) {
            FinGeneralLedger ledger = iFinGeneralLedgerService.selectBySubjectAndPeriod(groupid, subject.getSubjectId().toString(), period);
            if (ledger != null) {
                // 计算结转前的余额（将结转金额加回去）
                BigDecimal originalBalance = calculateOriginalBalance(ledger, subject);

                // 更新总账余额
                ledger.setEndBalance(originalBalance);
                ledger.setEndDirection(calculateOriginalDirection(originalBalance, subject));
                ledger.setUpdatedTime(new Date());

                iFinGeneralLedgerService.updateFinGeneralLedger(ledger);
                restoredSubjectCount++;

                result.addRestoredSubject(new RestoredSubjectInfo(
                        subject.getSubjectId(), subject.getSubjectCode(), subject.getSubjectName(), originalBalance
                ));
            }
        }

        result.setRestoredSubjectCount(restoredSubjectCount);
        log.info("恢复损益科目余额完成: 恢复{}个科目", restoredSubjectCount);
    }

    /**
     * 计算原始余额（反结转计算）
     */
    private BigDecimal calculateOriginalBalance(FinGeneralLedger ledger, FinAccountingSubjects subject) {
        // 这里需要根据具体的结转逻辑来反向计算
        // 假设结转时：余额 = 0，发生额 = 结转金额
        // 反结转：余额 = 结转金额，发生额保持不变

        SubjectType subjectType = SubjectType.valueOf(subject);

        if (subjectType == SubjectType.INCOME) {
            // 收入类科目：结转时从贷方转出，反结转应该加回贷方
            return ledger.getCreditTotal(); // 贷方发生额就是结转金额
        } else if (subjectType == SubjectType.EXPENSE) {
            // 费用类科目：结转时从借方转出，反结转应该加回借方
            return ledger.getDebitTotal(); // 借方发生额就是结转金额
        }

        return ledger.getEndBalance();
    }

    /**
     * 计算原始余额方向
     */
    private Integer calculateOriginalDirection(BigDecimal balance, FinAccountingSubjects  subject) {
        if (balance.compareTo(BigDecimal.ZERO) == 0) {
            SubjectType subjectType = SubjectType.valueOf(subject);
            return subjectType == SubjectType.INCOME ? 2 : 1; // 默认方向
        }
        return balance.compareTo(BigDecimal.ZERO) >= 0 ? 1 : 2;
    }

    /**
     * 删除下期期初余额
     */
    private void deleteNextPeriodOpeningBalance(String  groupid, String period, ReverseClosingResult result) {
        String nextPeriod = getNextPeriod(period);
        if (nextPeriod == null) {
            return;
        }

        log.info("开始删除下期期初余额: tenantId={}, nextPeriod={}", groupid, nextPeriod);

        // 删除下期总账记录
        int deletedLedgerCount = iFinGeneralLedgerService.deleteByPeriod(groupid, nextPeriod);

        // 删除下期明细账记录（如果有）
        //int deletedDetailCount = iFinDetailLedgerService.deleteByPeriod(groupid, nextPeriod);

        result.setDeletedNextPeriodLedgerCount(deletedLedgerCount);
        //result.setDeletedNextPeriodDetailCount(deletedDetailCount);

        log.info("删除下期期初余额完成: 总账{}条", deletedLedgerCount);
    }

    /**
     * 清理结账生成的账簿数据
     */
    private void cleanClosingGeneratedData(String groupid, String period, ReverseClosingResult result) {
//        log.info("开始清理结账生成的账簿数据: groupid={}, period={}", groupid, period);

        // 清理可能由结账过程生成的临时数据
        // 这里根据实际业务需求实现

        log.info("清理结账生成数据完成");
    }

    /**
     * 恢复凭证状态
     */
    private void restoreVoucherStatus(String groupid, String period, ReverseClosingResult result) {
        // 如果需要，可以将凭证状态从"已结账"恢复为"已审核"
        // 这取决于业务需求

        log.info("凭证状态恢复完成");
    }

    /**
     * 更新期间状态
     */
    private void updatePeriodStatus(String groupid, String period, Integer status, String operator) {
        FinAccountingPeriods periodEntity = new FinAccountingPeriods();
        periodEntity.setGroupid(groupid);
        periodEntity.setPeriodCode(period);
        List<FinAccountingPeriods> list = iFinAccountingPeriodsService.selectFinAccountingPeriodsList(periodEntity);
        if(list.size()>0){
            periodEntity = list.get(0);
        }
        periodEntity.setPeriodStatus(status);
        iFinAccountingPeriodsService.updateFinAccountingPeriods(periodEntity);
    }


    // 辅助方法
    private boolean validateReverseClosingPermission(String groupid, String operator) {
        // 实现权限验证逻辑
        // 通常只有管理员或财务主管有反结账权限
        return true; // 简化实现
    }

    private void lockAccountingPeriod(String groupid, String period) {
        // 实现期间锁定逻辑，防止并发操作
    }

    private boolean hasDependentData(String groupid, String period) {
        // 检查是否存在依赖数据，如报表、分析数据等
        return false; // 简化实现
    }

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
            return null;
        }
    }

    private void handleReverseClosingFailure(String groupid, String period,
                                             ReverseClosingResult result, ReverseClosingException e) {
        // 失败处理：恢复期间状态等
        try {
            updatePeriodStatus(groupid, period, PeriodStatus.CLOSED.getValue(), "system");
        } catch (Exception ex) {
            log.error("恢复期间状态失败: groupid={}, period={}", groupid, period, ex);
        }

        result.fail(e.getMessage());
    }

    private String getClientIp() {
        // 获取客户端IP
        return "127.0.0.1"; // 简化实现
    }
}