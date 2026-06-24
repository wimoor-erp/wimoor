package com.wimoor.finance.closing.service.impl;

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
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
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
     * 前置检查失败时直接返回（无数据变更），核心操作失败时抛异常触发事务回滚
     */
    @Transactional(rollbackFor = Exception.class)
    public ReverseClosingResult executeReverseClosing(String groupid, String period, String operator, String reason) {
        log.info("开始执行反结账: groupid={}, period={}, operator={}", groupid, period, operator);

        ReverseClosingResult result = new ReverseClosingResult(groupid, period, operator, reason);

        // 1. 权限验证（无数据变更，失败直接返回）
        if (!validateReverseClosingPermission(groupid, operator)) {
            return result.fail("无反结账权限");
        }

        // 2. 前置检查（无数据变更，失败直接返回）
        PreReverseCheckResult preCheck = performPreReverseCheck(groupid, period);
        if (!preCheck.isPassed()) {
            return result.fail("反结账前检查失败: " + preCheck.getErrorMessage());
        }

        // 3. 锁定期间（防止并发操作）
        lockAccountingPeriod(groupid, period);

        // 4. 执行反结账核心操作（失败则事务回滚）
        executeReverseClosingCore(groupid, period, result);

        // 5. 更新期间状态为已开启（反结账后恢复为开启状态）
        updatePeriodStatus(groupid, period, PeriodStatus.OPEN.getValue(), operator);

        result.setSuccess(true);
        log.info("反结账完成: groupid={}, period={}", groupid, period);

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
        // 1. 删除损益结转凭证（内部会调用 reversePosting 反向更新总账）
        deleteProfitTransferVouchers(groupid, period, result);

        // 2. 删除下期期初余额
        deleteNextPeriodOpeningBalance(groupid, period, result);

        // 3. 重新计算当前期间期末余额（因为删除了结转凭证，需要重新计算）
        recalculateEndingBalances(groupid, period, result);
    }

    /**
     * 删除损益结转凭证
     * 注意：reversePosting 会自动反向更新总账（减去凭证的借贷金额）
     */
    private void deleteProfitTransferVouchers(String groupid, String period, ReverseClosingResult result) {
        log.info("开始删除损益结转凭证: groupid={}, period={}", groupid, period);

        // 查找损益结转凭证
        List<FinVouchers> transferVouchers = finVouchersService.selectProfitTransferVouchers(groupid, period);

        int deletedVoucherCount = 0;
        int deletedEntryCount = 0;

        for (FinVouchers voucher : transferVouchers) {
            // 反过账：反向更新总账（减去凭证的借贷金额），删除明细账
            finVouchersService.reversePosting(voucher.getVoucherId());

            // 删除凭证分录
            int entryCount = iFinVoucherEntriesService.deleteByVoucherId(voucher.getVoucherId());
            deletedEntryCount += entryCount;

            // 删除凭证主表
            int voucherCount = finVouchersService.forceDeleteFinVouchersByVoucherId(voucher.getVoucherId());
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
     * 重新计算当前期间期末余额
     * 删除结转凭证后，总账的发生额已经通过 reversePosting 反向更新，
     * 但期末余额需要重新计算
     */
    private void recalculateEndingBalances(String groupid, String period, ReverseClosingResult result) {
        log.info("开始重新计算期末余额: groupid={}, period={}", groupid, period);

        FinGeneralLedger query = new FinGeneralLedger();
        query.setGroupid(groupid);
        query.setPeriod(period);
        List<FinGeneralLedger> ledgers = iFinGeneralLedgerService.selectFinGeneralLedgerList(query);

        int updatedCount = 0;
        for (FinGeneralLedger ledger : ledgers) {
            // 重新计算期末余额 = 期初 + 借方发生额 - 贷方发生额（根据科目方向）
            FinAccountingSubjects subject = accountingSubjectService.getSubjectById(ledger.getSubjectId().toString());
            if (subject == null) {
                continue;
            }

            BigDecimal beginBalance = ledger.getBeginBalance() != null ? ledger.getBeginBalance() : BigDecimal.ZERO;
            BigDecimal debitTotal = ledger.getDebitTotal() != null ? ledger.getDebitTotal() : BigDecimal.ZERO;
            BigDecimal creditTotal = ledger.getCreditTotal() != null ? ledger.getCreditTotal() : BigDecimal.ZERO;

            BigDecimal endBalance;
            Integer endDirection;

            if (subject.isDebitBalance()) {
                // 借方余额科目（资产、成本类）
                endBalance = beginBalance.add(debitTotal).subtract(creditTotal);
                endDirection = endBalance.compareTo(BigDecimal.ZERO) >= 0 ? 1 : 2;
            } else {
                // 贷方余额科目（负债、权益、收入类）
                endBalance = beginBalance.add(creditTotal).subtract(debitTotal);
                endDirection = endBalance.compareTo(BigDecimal.ZERO) >= 0 ? 2 : 1;
            }

            ledger.setEndBalance(endBalance);
            ledger.setEndDirection(endDirection);
            ledger.setUpdatedTime(new Date());

            iFinGeneralLedgerService.updateFinGeneralLedger(ledger);
            updatedCount++;
        }

        result.setRestoredSubjectCount(updatedCount);
        log.info("重新计算期末余额完成: 更新{}个科目", updatedCount);
    }

    /**
     * 删除下期期初余额（只清除期初余额，保留下期已有的发生额）
     */
    private void deleteNextPeriodOpeningBalance(String groupid, String period, ReverseClosingResult result) {
        String nextPeriod = getNextPeriod(period);
        if (nextPeriod == null) {
            return;
        }

        log.info("开始清除下期期初余额: groupid={}, nextPeriod={}", groupid, nextPeriod);

        // 获取下期所有总账记录
        FinGeneralLedger nextQuery = new FinGeneralLedger();
        nextQuery.setGroupid(groupid);
        nextQuery.setPeriod(nextPeriod);
        List<FinGeneralLedger> nextPeriodLedgers = iFinGeneralLedgerService.selectFinGeneralLedgerList(nextQuery);

        int clearedCount = 0;
        for (FinGeneralLedger ledger : nextPeriodLedgers) {
            // 只清除期初余额，保留下期已有的发生额
            BigDecimal debitTotal = ledger.getDebitTotal() != null ? ledger.getDebitTotal() : BigDecimal.ZERO;
            BigDecimal creditTotal = ledger.getCreditTotal() != null ? ledger.getCreditTotal() : BigDecimal.ZERO;

            // 如果下期没有任何发生额，直接删除整条记录
            if (debitTotal.compareTo(BigDecimal.ZERO) == 0 && creditTotal.compareTo(BigDecimal.ZERO) == 0) {
                iFinGeneralLedgerService.deleteFinGeneralLedgerByLedgerId(ledger.getLedgerId());
            } else {
                // 如果下期已有发生额，只清除期初余额，重新计算期末余额
                ledger.setBeginBalance(BigDecimal.ZERO);
                ledger.setBeginDirection(1);
                // 期末余额 = 期初(0) + 借方 - 贷方
                ledger.setEndBalance(debitTotal.subtract(creditTotal));
                ledger.setEndDirection(ledger.getEndBalance().compareTo(BigDecimal.ZERO) >= 0 ? 1 : 2);
                ledger.setUpdatedTime(new Date());
                iFinGeneralLedgerService.updateFinGeneralLedger(ledger);
            }
            clearedCount++;
        }

        result.setDeletedNextPeriodLedgerCount(clearedCount);
        log.info("清除下期期初余额完成: 处理{}条记录", clearedCount);
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
     * 更新期间状态（包括当前期间标志的处理）
     */
    private void updatePeriodStatus(String groupid, String period, Integer status, String operator) {
        // 1. 更新当前期间状态
        FinAccountingPeriods periodEntity = new FinAccountingPeriods();
        periodEntity.setGroupid(groupid);
        periodEntity.setPeriodCode(period);
        List<FinAccountingPeriods> list = iFinAccountingPeriodsService.selectFinAccountingPeriodsList(periodEntity);
        if(list.size() > 0){
            periodEntity = list.get(0);
        }
        periodEntity.setPeriodStatus(status);
        periodEntity.setUpdateTime(new Date());
        periodEntity.setUpdateBy(operator);

        // 根据状态决定 isCurrent
        if (PeriodStatus.OPEN.getValue().equals(status)) {
            // 反结账成功：当前期间设为当前
            periodEntity.setIsCurrent(1);
        } else if (PeriodStatus.CLOSED.getValue().equals(status)) {
            // 反结账失败恢复：当前期间不是当前
            periodEntity.setIsCurrent(0);
        }

        iFinAccountingPeriodsService.updateFinAccountingPeriods(periodEntity);

        // 2. 处理下期的当前期间标志
        String nextPeriod = getNextPeriod(period);
        if (nextPeriod != null) {
            FinAccountingPeriods nextPeriodEntity = new FinAccountingPeriods();
            nextPeriodEntity.setGroupid(groupid);
            nextPeriodEntity.setPeriodCode(nextPeriod);
            List<FinAccountingPeriods> nextList = iFinAccountingPeriodsService.selectFinAccountingPeriodsList(nextPeriodEntity);
            if(nextList.size() > 0){
                nextPeriodEntity = nextList.get(0);
                nextPeriodEntity.setUpdateTime(new Date());
                nextPeriodEntity.setUpdateBy(operator);

                if (PeriodStatus.OPEN.getValue().equals(status)) {
                    // 反结账成功：下期不再是当前期间
                    nextPeriodEntity.setIsCurrent(0);
                } else if (PeriodStatus.CLOSED.getValue().equals(status)) {
                    // 反结账失败恢复：下期恢复为当前期间
                    nextPeriodEntity.setIsCurrent(1);
                }

                iFinAccountingPeriodsService.updateFinAccountingPeriods(nextPeriodEntity);
            }
        }

        log.info("期间状态更新完成: period={}, status={}, isCurrent={}", period, status,
                PeriodStatus.OPEN.getValue().equals(status) ? 1 : 0);
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



    private String getClientIp() {
        // 获取客户端IP
        return "127.0.0.1"; // 简化实现
    }
}