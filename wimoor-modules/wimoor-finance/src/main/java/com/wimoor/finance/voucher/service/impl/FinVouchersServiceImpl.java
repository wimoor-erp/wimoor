package com.wimoor.finance.voucher.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import cn.hutool.core.util.StrUtil;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.finance.ledger.domain.FinDetailLedger;
import com.wimoor.finance.ledger.domain.FinGeneralLedger;
import com.wimoor.finance.ledger.mapper.FinDetailLedgerMapper;
import com.wimoor.finance.ledger.mapper.FinGeneralLedgerMapper;
import com.wimoor.finance.ledger.service.IFinDetailLedgerService;
import com.wimoor.finance.ledger.service.IFinGeneralLedgerService;
import com.wimoor.finance.setting.domain.FinAccountingPeriods;
import com.wimoor.finance.setting.domain.FinAccountingSubjects;
import com.wimoor.finance.setting.service.IFinAccountingPeriodsService;
import com.wimoor.finance.setting.service.IFinAccountingSubjectsService;
import com.wimoor.finance.voucher.domain.*;
import com.wimoor.finance.voucher.domain.dto.FinVoucherDTO;
import com.wimoor.finance.voucher.mapper.FinVoucherEntriesMapper;
import com.wimoor.finance.voucher.mapper.FinVourchesFileMapper;
import com.wimoor.finance.voucher.service.IFinVoucherModifyLogService;
import com.wimoor.finance.voucher.service.IFinVourchesFileService;
import com.wimoor.finance.voucher.service.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.wimoor.finance.voucher.mapper.FinVouchersMapper;
import com.wimoor.finance.voucher.service.IFinVouchersService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 记账凭证Service业务层处理
 *
 * @author wimoor
 * @date 2025-11-04
 */
@Service
public class FinVouchersServiceImpl implements IFinVouchersService
{
    @Autowired
    private FinVouchersMapper finVouchersMapper;
    @Autowired
    private FinVoucherEntriesMapper finVoucherEntriesMapper;

    @Autowired
    private IFinDetailLedgerService finDetailLedgerService;

    @Autowired
    private IFinGeneralLedgerService finGeneralLedgerService;

    @Autowired
    private FinDetailLedgerMapper finDetailLedgerMapper;

    @Autowired
    private FinGeneralLedgerMapper finGeneralLedgerMapper;

    @Autowired
    private IFinAccountingSubjectsService accountingSubjectService;

    @Autowired
    IFinVoucherModifyLogService  finVoucherModifyLogService;

    @Autowired
    IFinAccountingPeriodsService finAccountingPeriodsService;
    @Autowired
    private IFinVourchesFileService finVourchesFileService;
    @Autowired
    private FinVourchesFileMapper finVourchesFileMapper;


    /**
     * 查询记账凭证
     *
     * @param voucherId 记账凭证主键
     * @return 记账凭证
     */
    @Override
    public FinVouchers selectFinVouchersByVoucherId(Long voucherId)
    {
        FinVouchers finVouchers = finVouchersMapper.selectFinVouchersByVoucherId(voucherId);
        List<FinVoucherEntries> entries = finVoucherEntriesMapper.selectByVoucherId(voucherId);
        finVouchers.setEntries(entries);
        // 4. 查询凭证附件
        List<FinVourchesFile> files = finVourchesFileService.selectFinVourchesFileByVoucherId(finVouchers);
        finVouchers.setFiles(files);
        return finVouchers;
    }

    /**
     * 查询记账凭证列表
     *
     * @param finVouchers 记账凭证
     * @return 记账凭证
     */
    @Override
    public List<FinVouchers> selectFinVouchersList(FinVoucherDTO finVouchers)
    {
        return finVouchersMapper.selectFinVouchersList(finVouchers);
    }

    @Override
    public String selectNextVoucherNo(FinVouchers finVouchers){
        return finVouchersMapper.selectNextVoucherNo(finVouchers);
    }

    /**
     * 新增记账凭证 - 实时过账模式
     *
     * @param finVouchers 记账凭证
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertFinVouchers(FinVouchers finVouchers)
    {
        UserInfo userinfo = UserInfoContext.get();
        int result=0;
        if(finVouchers.getTotalAmount()==null||finVouchers.getTotalAmount().doubleValue()==0){
            throw new IllegalArgumentException("凭证金额不能为空");
        }
        finVouchers.setPreparerBy(userinfo.getUserName());
        finVouchers.setCreatedTime(new Date());
        finVouchers.setUpdatedTime(new Date());

        // 1. 保存凭证主表
        result = finVouchersMapper.insertFinVouchers(finVouchers);

        if(result>0&&finVouchers.getEntries()!=null){
            // 2. 保存凭证分录
            List<FinVoucherEntries> details=finVouchers.getEntries();
            for(FinVoucherEntries detail:details){
                if(detail.getCreditAmount()!=null||detail.getDebitAmount()!=null){
                    detail.setVoucherId(finVouchers.getVoucherId());
                    detail.setGroupid(finVouchers.getGroupid());
                    detail.setCreatedTime(new Date());
                    finVoucherEntriesMapper.insertFinVoucherEntries(detail);
                }
            }

            // 3. 实时过账到账簿（无论是否审核）
            repostToLedgers(finVouchers);
        }
        if(result>0){
            // 4. 保存凭证附件
            if(finVouchers.getFiles()!=null){
                for(FinVourchesFile file:finVouchers.getFiles()){
                    file.setVoucherId(finVouchers.getVoucherId());
                    file.setGroupid(finVouchers.getGroupid());
                    file.setOpttime(new Date());
                    finVourchesFileService.insertFinVourchesFile(file);
                }
            }
        }
        return result;
    }

    /**
     * 修改记账凭证 - 实时过账模式
     *
     * @param finVouchers 记账凭证
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateFinVouchers(FinVouchers finVouchers)
    {
        // 1. 修改前的预处理和检查
        VoucherModifyPreCheckResult preCheckResult = preModifyCheck(finVouchers.getVoucherId());
        if (!preCheckResult.isSuccess()) {
            throw new IllegalArgumentException(preCheckResult.getMessage());
        }

        // 2. 获取备份数据
        VoucherBackup backup = preCheckResult.getBackup();
        UserInfo user=UserInfoContext.get();

        try {
            // 3. 从账簿中移除原凭证的影响
            removeOriginalVoucherImpact(backup);

            // 4. 更新凭证数据
            finVouchers.setUpdatedTime(new Date());
            finVouchers.setUpdateBy(UserInfoContext.get().getUserName());
            finVouchers.setPreparerBy(UserInfoContext.get().getUserName());
            int result = finVouchersMapper.updateFinVouchers(finVouchers);

            // 5. 更新凭证分录
            if(result>0&&finVouchers.getEntries()!=null){
                finVoucherEntriesMapper.deleteFinVoucherEntriesByVoucherId(finVouchers);
                List<FinVoucherEntries> details=finVouchers.getEntries();
                for(FinVoucherEntries detail:details){
                    if((detail.getCreditAmount()!=null&&detail.getCreditAmount().doubleValue()!=0)||(detail.getDebitAmount()!=null&&detail.getDebitAmount().doubleValue()!=0)){
                        detail.setVoucherId(finVouchers.getVoucherId());
                        detail.setGroupid(finVouchers.getGroupid());
                        finVoucherEntriesMapper.insertFinVoucherEntries(detail);
                    }
                }

                // 6. 重新实时过账到账簿
                repostToLedgers(finVouchers);

                // 7. 记录修改日志
                finVoucherModifyLogService.logModification(backup,finVouchers,user.getUserName(),finVouchers.getReason());
            }
             // 8. 更新凭证附件
//             if(result>0){
//                finVourchesFileService.deleteFinVourchesFileByVoucherId(finVouchers);
//                if(finVouchers.getFiles()!=null){
//                    for(FinVourchesFile file:finVouchers.getFiles()){
//                        file.setVoucherId(finVouchers.getVoucherId());
//                        file.setGroupid(finVouchers.getGroupid());
//                        file.setOpttime(new Date());
//                        finVourchesFileService.insertFinVourchesFile(file);
//                    }
//                }
//             }

            return result;

        } catch (Exception e) {
            // 8. 失败时恢复备份
            restoreFromBackup(backup);
            throw new BizException("凭证修改失败，已恢复原状", e);
        }
    }

    /**
     * 批量删除记账凭证
     *
     * @param voucherIds 需要删除的记账凭证主键
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteFinVouchersByVoucherIds(Long[] voucherIds)
    {
        int result = 0;
        for (Long voucherId : voucherIds) {
            // 删除前先检查凭证状态
            VoucherModifyPreCheckResult preCheckResult = preModifyCheck(voucherId);
            if (!preCheckResult.isSuccess()) {
                throw new IllegalArgumentException("凭证ID: " + voucherId + " - " + preCheckResult.getMessage());
            }

            // 获取备份数据（用于异常恢复）
            VoucherBackup backup = preCheckResult.getBackup();

            try {
                // 从账簿中移除原凭证的影响
                removeOriginalVoucherImpact(backup);

                // 删除凭证分录
                FinVouchers param = new FinVouchers();
                param.setVoucherId(voucherId);
                finVoucherEntriesMapper.deleteFinVoucherEntriesByVoucherId(param);
                finVourchesFileService.deleteFinVourchesFileByVoucherId(param);
                // 删除凭证
                result += finVouchersMapper.deleteFinVouchersByVoucherId(voucherId);
            } catch (Exception e) {
                // 失败时恢复备份
                restoreFromBackup(backup);
                throw new BizException("删除凭证失败，已恢复原状", e);
            }
        }
        return result;
    }

    /**
     * 删除记账凭证信息
     *
     * @param voucherId 记账凭证主键
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteFinVouchersByVoucherId(Long voucherId)
    {
        // 删除前先检查凭证状态
        VoucherModifyPreCheckResult preCheckResult = preModifyCheck(voucherId);
        if (!preCheckResult.isSuccess()) {
            throw new IllegalArgumentException(preCheckResult.getMessage());
        }

        // 获取备份数据（用于异常恢复）
        VoucherBackup backup = preCheckResult.getBackup();

        try {
            // 从账簿中移除原凭证的影响
            removeOriginalVoucherImpact(backup);

            // 删除凭证分录
            FinVouchers param = new FinVouchers();
            param.setVoucherId(voucherId);
            finVoucherEntriesMapper.deleteFinVoucherEntriesByVoucherId(param);

            // 删除凭证
            return finVouchersMapper.deleteFinVouchersByVoucherId(voucherId);
        } catch (Exception e) {
            // 失败时恢复备份
            restoreFromBackup(backup);
            throw new BizException("删除凭证失败，已恢复原状", e);
        }
    }

    /**
     * 审核记账凭证 - 仅修改状态，账簿数据在保存时已生成
     *
     * @param finVoucherIds 记账凭证IDS
     * @return 结果
     */
    @Override
    public int approveFinVouchers(List<Long> finVoucherIds) {
        //批量审核通过
        UserInfo userinfo = UserInfoContext.get();
        for(Long finVoucherId:finVoucherIds){
            FinVouchers voucher = this.selectFinVouchersByVoucherId(finVoucherId);
            if(voucher!=null && voucher.getVoucherStatus()==1){
                voucher.setVoucherStatus(2); // 已审核
                voucher.setUpdatedTime(new Date());
                voucher.setUpdateBy(userinfo.getUserName());
                finVouchersMapper.updateFinVouchers(voucher);
                // 不需要再次更新账簿！
            }
        }
        return 1;
    }

    /**
     * 修改凭证前的预处理
     */
    private VoucherModifyPreCheckResult preModifyCheck(Long voucherId) {
        FinVouchers voucher = finVouchersMapper.selectFinVouchersByVoucherId(voucherId);

        // 1. 检查凭证状态
        if (voucher == null) {
            return VoucherModifyPreCheckResult.fail("凭证不存在");
        }

        // 3. 检查是否已结账
        if (isPeriodClosed(voucher.getGroupid(), voucher.getVoucherDate())) {
            return VoucherModifyPreCheckResult.fail("会计期间已结账，不能修改");
        }

        // 4. 备份当前凭证和账簿数据（用于回滚）
        VoucherBackup backup = backupVoucherAndLedgerData(voucherId);

        return VoucherModifyPreCheckResult.success(backup);
    }

    /**
     * 检查会计期间是否已结账
     */
    private boolean isPeriodClosed(String groupid, Date voucherDate) {
        // 这里应该实现检查会计期间是否已结账的逻辑
        // 暂时返回false，表示未结账
        String period=FinUtil.getAccountingPeriod(voucherDate);
        FinAccountingPeriods finPeriod = finAccountingPeriodsService.selectByPeriod(groupid, period);
        return finPeriod.getPeriodStatus()==3;
    }

    /**
     * 备份凭证和账簿数据
     */
    private VoucherBackup backupVoucherAndLedgerData(Long voucherId) {
        VoucherBackup backup = new VoucherBackup();

        // 备份凭证主表
        backup.setOriginalVoucher(finVouchersMapper.selectFinVouchersByVoucherId(voucherId));

        // 备份凭证分录
        List<FinVoucherEntries> entries = finVoucherEntriesMapper.selectByVoucherId(voucherId);
        backup.setOriginalEntries(entries);

        // 备份相关的账簿记录
        backup.setOriginalLedgerEntries(finDetailLedgerService.selectByVoucherId(voucherId));

        // 备份总账影响

        backup.setGeneralLedger(calculateGeneralLedger(entries,backup.getOriginalVoucher()));

        return backup;
    }

    private List<FinGeneralLedger> calculateGeneralLedger(List<FinVoucherEntries> entries,FinVouchers vouchers) {
        List<FinGeneralLedger> result=new ArrayList<FinGeneralLedger>();
        String groupid=null;
        String period=null;
        Set<String> subjectIds=new HashSet<String>();
        for(FinVoucherEntries entry:entries){
            //凭证时间获取会计期间 AI
            groupid=entry.getGroupid();
            period= FinUtil.getAccountingPeriod(vouchers.getVoucherDate());
            subjectIds.add(entry.getSubjectId());
        }
        for (Iterator<String> it = subjectIds.iterator(); it.hasNext(); ) {
            String subjectid = it.next();
            FinGeneralLedger generalLedger = finGeneralLedgerMapper.selectBySubjectAndPeriod(
                    groupid, subjectid, period);
            result.add(generalLedger);
        }

       return result;
    }


    /**
     * 从账簿中移除原凭证影响
     */
    private void removeOriginalVoucherImpact(VoucherBackup backup) {
        // 删除明细账记录
        finDetailLedgerMapper.deleteByVoucherId(backup.getOriginalVoucher().getVoucherId());

        // 回滚总账发生额
        rollbackGeneralLedgerImpact(backup);
    }

    /**
     * 回滚总账影响
     */
    private void rollbackGeneralLedgerImpact(VoucherBackup backup) {
        List<FinVoucherEntries> entries = backup.getOriginalEntries();
        if (entries != null) {
            for (FinVoucherEntries entry : entries) {
                FinGeneralLedger ledger = finGeneralLedgerMapper.selectBySubjectAndPeriod(
                        backup.getOriginalVoucher().getGroupid(),
                        entry.getSubjectId(),
                        FinUtil.getAccountingPeriod(backup.getOriginalVoucher().getVoucherDate())
                );

                if (ledger != null) {
                    // 减去原分录的金额
                    BigDecimal newDebit = ledger.getDebitTotal().subtract(entry.getDebitAmount() != null ? entry.getDebitAmount() : BigDecimal.ZERO);
                    BigDecimal newCredit = ledger.getCreditTotal().subtract(entry.getCreditAmount() != null ? entry.getCreditAmount() : BigDecimal.ZERO);

                    ledger.setDebitTotal(newDebit);
                    ledger.setCreditTotal(newCredit);
                    ledger.setUpdatedTime(new Date());

                    finGeneralLedgerMapper.updateFinGeneralLedger(ledger);
                }
            }
        }
    }

    /**
     * 重新过账到账簿
     */
    private void repostToLedgers(FinVouchers voucher) {
        if (voucher == null || voucher.getEntries() == null) {
            return;
        }

        List<FinVoucherEntries> entries = voucher.getEntries();
        String period = FinUtil.getAccountingPeriod(voucher.getVoucherDate());

        for (FinVoucherEntries entry : entries) {
            // 更新总账
            if(StrUtil.isNotBlank(entry.getSubjectId())&&(entry.getCreditAmount() != null || entry.getDebitAmount() != null)) {
                updateGeneralLedgerRealtime(voucher.getGroupid(), entry, period);

                // 插入明细账
                insertDetailLedgerRealtime(voucher, entry, period);
            }
        }
    }

    /**
     * 实时更新总账
     */
    private void updateGeneralLedgerRealtime(String groupid, FinVoucherEntries entry, String period) {
        // 查询总账记录
        FinGeneralLedger generalLedger = finGeneralLedgerMapper.selectBySubjectAndPeriod(
                groupid, entry.getSubjectId(), period);

        if (generalLedger == null) {
            // 创建新的总账记录
            generalLedger = new FinGeneralLedger();
            generalLedger.setGroupid(groupid);
            generalLedger.setSubjectId(Long.parseLong(entry.getSubjectId()));
            generalLedger.setPeriod(period);
            generalLedger.setBeginBalance(BigDecimal.ZERO);
            generalLedger.setBeginDirection(1); // 默认借方
            generalLedger.setDebitTotal(BigDecimal.ZERO);
            generalLedger.setCreditTotal(BigDecimal.ZERO);
            generalLedger.setEndBalance(BigDecimal.ZERO);
            generalLedger.setEndDirection(1); // 默认借方
            generalLedger.setCreatedTime(new Date());
            finGeneralLedgerMapper.insertFinGeneralLedger(generalLedger);
        } else {
            // 更新借贷金额
            BigDecimal newDebitTotal = generalLedger.getDebitTotal().add(
                    entry.getDebitAmount() != null ? entry.getDebitAmount() : BigDecimal.ZERO);
            BigDecimal newCreditTotal = generalLedger.getCreditTotal().add(
                    entry.getCreditAmount() != null ? entry.getCreditAmount() : BigDecimal.ZERO);

            // 计算期末余额和方向
            BalanceResult balanceResult = calculateEndBalance(
                    generalLedger.getBeginBalance(),
                    generalLedger.getBeginDirection().intValue(),
                    newDebitTotal,
                    newCreditTotal,
                    entry.getSubjectId()
            );

            // 更新总账
            generalLedger.setDebitTotal(newDebitTotal);
            generalLedger.setCreditTotal(newCreditTotal);
            generalLedger.setEndBalance(balanceResult.getBalance());
            generalLedger.setEndDirection(balanceResult.getDirection());
            generalLedger.setUpdatedTime(new Date());
            finGeneralLedgerMapper.updateFinGeneralLedger(generalLedger);
        }
    }

    /**
     * 实时插入明细账
     */
    private void insertDetailLedgerRealtime(FinVouchers voucher, FinVoucherEntries entry, String period) {
        FinDetailLedger detailLedger = new FinDetailLedger();
        detailLedger.setGroupid(voucher.getGroupid());
        detailLedger.setSubjectId(entry.getSubjectId());
        detailLedger.setVoucherId(voucher.getVoucherId());
        detailLedger.setEntryId(entry.getEntryId());
        detailLedger.setVoucherDate(voucher.getVoucherDate());
        detailLedger.setVoucherNo(voucher.getVoucherNo());
        detailLedger.setSummary(entry.getSummary());
        detailLedger.setDebitAmount(entry.getDebitAmount());
        detailLedger.setCreditAmount(entry.getCreditAmount());
        detailLedger.setCreatedTime(new Date());

        // 计算余额和方向
        BalanceDetailResult balanceDetail = calculateDetailBalance(
                voucher.getGroupid(), entry.getSubjectId(), voucher.getVoucherDate(),
                entry.getDebitAmount(), entry.getCreditAmount(), entry.getSubjectId()
        );

        detailLedger.setBalance(balanceDetail.getBalance());
        detailLedger.setBalanceDirection(balanceDetail.getDirection());

        finDetailLedgerMapper.insertFinDetailLedger(detailLedger);
    }

    /**
     * 恢复备份数据
     */
    private void restoreFromBackup(VoucherBackup backup) {
        // 恢复凭证数据
        finVouchersMapper.updateFinVouchers(backup.getOriginalVoucher());

        // 删除修改后的分录，恢复原分录
        finVoucherEntriesMapper.deleteFinVoucherEntriesByVoucherId(backup.getOriginalVoucher());
        if (backup.getOriginalEntries() != null) {
            for (FinVoucherEntries entry : backup.getOriginalEntries()) {
                finVoucherEntriesMapper.insertFinVoucherEntries(entry);
            }
        }

        // 删除修改后的账簿记录，恢复原账簿记录
        finDetailLedgerMapper.deleteByVoucherId(backup.getOriginalVoucher().getVoucherId());
        if (backup.getOriginalLedgerEntries() != null) {
            for (FinDetailLedger ledger : backup.getOriginalLedgerEntries()) {
                finDetailLedgerMapper.insertFinDetailLedger(ledger);
            }
        }

        // 恢复总账数据
        restoreGeneralLedgerFromBackup(backup);
    }

    /**
     * 恢复总账数据
     */
    private void restoreGeneralLedgerFromBackup(VoucherBackup backup) {
        // 如果有预先计算的总账影响，则直接恢复
        List<FinGeneralLedger> impacts = backup.getGeneralLedger();
        if (impacts != null) {
            for (FinGeneralLedger impact : impacts) {
                FinGeneralLedger ledger = finGeneralLedgerMapper.selectBySubjectAndPeriod(
                        impact.getGroupid(), impact.getSubjectId().toString(), impact.getPeriod());

                if (ledger != null) {
                    ledger.setDebitTotal(impact.getDebitTotal());
                    ledger.setCreditTotal(impact.getCreditTotal());
                    ledger.setEndBalance(impact.getEndBalance());
                    ledger.setEndDirection(impact.getEndDirection());
                    ledger.setUpdatedTime(new Date());
                    finGeneralLedgerMapper.updateFinGeneralLedger(ledger);
                }
            }
        } else {
            // 否则重新过账原凭证
            repostToLedgers(backup.getOriginalVoucher());
        }
    }

    /**
     * 凭证过账到账簿（保留原方法以兼容现有功能）
     */
    @Transactional(rollbackFor = Exception.class)
    public PostingResult postVoucherToLedger(Long voucherId) {
        try {
            // 查询凭证及分录
            FinVouchers voucher = finVouchersMapper.selectVoucherWithEntries(voucherId);
            if (voucher == null) {
                return PostingResult.fail("凭证不存在");
            }

            // 校验凭证状态
            if (!validateVoucherStatus(voucher)) {
                return PostingResult.fail("凭证状态不符合过账要求");
            }

            // 校验借贷平衡
            if (!validateDebitCreditBalance(voucher.getEntries())) {
                return PostingResult.fail("凭证借贷不平衡");
            }

            // 更新凭证状态为已过账
            updateVoucherStatus(voucherId, 3); // 3-已过账

            return PostingResult.success("过账成功");

        } catch (Exception e) {
            throw new RuntimeException("过账失败：" + e.getMessage());
        }
    }

    /**
     * 批量过账（保留原方法以兼容现有功能）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public BatchPostingResult batchPostVouchers(List<Long> voucherIds) {
        BatchPostingResult result = new BatchPostingResult();

        for (Long voucherId : voucherIds) {
            try {
                PostingResult postingResult = postVoucherToLedger(voucherId);
                if (postingResult.isSuccess()) {
                    result.addSuccess(voucherId);
                } else {
                    result.addFailure(voucherId, postingResult.getMessage());
                }
            } catch (Exception e) {
                result.addFailure(voucherId, e.getMessage());
            }
        }

        return result;
    }

    @Override
    public Integer countProfitTransferVouchers(String groupid, String period) {
        return this.finVouchersMapper.countProfitTransferVouchers(groupid, period);
    }

    @Override
    public List<FinVouchers> selectVouchersModifiedAfterClosing(String groupid, String period, Date closeTime) {
        return this.finVouchersMapper.selectVouchersModifiedAfterClosing(groupid, period, closeTime);
    }

    @Override
    public List<FinVouchers> selectProfitTransferVouchers(String groupid, String period) {
        return this.finVouchersMapper.selectProfitTransferVouchers(groupid, period);
    }

    /**
     * 校验凭证状态
     */
    private boolean validateVoucherStatus(FinVouchers voucher) {
        // 只有已审核状态(2)的凭证才能过账
        return voucher.getVoucherStatus() != null && voucher.getVoucherStatus() == 2;
    }

    /**
     * 校验借贷平衡
     */
    private boolean validateDebitCreditBalance(List<FinVoucherEntries> entries) {
        BigDecimal totalDebit = BigDecimal.ZERO;
        BigDecimal totalCredit = BigDecimal.ZERO;

        for (FinVoucherEntries entry : entries) {
            totalDebit = totalDebit.add(entry.getDebitAmount() != null ? entry.getDebitAmount() : BigDecimal.ZERO);
            totalCredit = totalCredit.add(entry.getCreditAmount() != null ? entry.getCreditAmount() : BigDecimal.ZERO);
        }
        return totalDebit.compareTo(totalCredit) == 0;
    }



    /**
     * 更新总账（兼容原有方法）
     */
    private void updateGeneralLedger(FinVoucherEntries entry, String groupid, String period) {
        updateGeneralLedgerRealtime(groupid, entry, period);
    }

    /**
     * 计算期末余额
     */
    private BalanceResult calculateEndBalance(BigDecimal beginBalance, Integer beginDirection,
                                              BigDecimal debitTotal, BigDecimal creditTotal,
                                              String subjectId) {
        // 获取科目信息（这里需要根据科目类型判断余额方向）
        FinAccountingSubjects subject = accountingSubjectService.getSubjectById(subjectId);

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

        return new BalanceResult(endBalance, endDirection);
    }

    /**
     * 插入明细账（兼容原有方法）
     */
    private void insertDetailLedger(FinVoucherEntries entry, FinVouchers voucher, String period) {
        insertDetailLedgerRealtime(voucher, entry, period);
    }

    /**
     * 计算明细账余额
     */
    private BalanceDetailResult calculateDetailBalance(String groupid, String subjectId, Date voucherDate,
                                                       BigDecimal debitAmount, BigDecimal creditAmount,
                                                       String currentSubjectId) {
        // 查询上一条明细账记录
        FinDetailLedger lastDetail = finDetailLedgerService.selectLastDetailLedger(
                groupid, subjectId, voucherDate);
        creditAmount=creditAmount!=null?creditAmount:BigDecimal.ZERO;
        debitAmount=debitAmount!=null?debitAmount:BigDecimal.ZERO;
        BigDecimal previousBalance = BigDecimal.ZERO;
        Integer previousDirection = 1;

        if (lastDetail != null) {
            previousBalance = lastDetail.getBalance();
            if(lastDetail.getBalanceDirection()!=null){
                previousDirection = lastDetail.getBalanceDirection().intValue();
            }else{
                previousDirection = 2;
            }
        }

        // 获取科目信息
        FinAccountingSubjects subject = accountingSubjectService.getSubjectById(currentSubjectId);

        BigDecimal currentBalance;
        Integer currentDirection;

        if (subject.isDebitBalance()) {
            // 借方余额科目
            currentBalance = previousBalance.add(debitAmount).subtract(creditAmount);
            currentDirection = currentBalance.compareTo(BigDecimal.ZERO) >= 0 ? 1 : 2;
        } else {
            // 贷方余额科目
            currentBalance = previousBalance.add(creditAmount).subtract(debitAmount);
            currentDirection = currentBalance.compareTo(BigDecimal.ZERO) >= 0 ? 2 : 1;
        }

        return new BalanceDetailResult(currentBalance, currentDirection);
    }

    /**
     * 更新凭证状态
     */
    private void updateVoucherStatus(Long voucherId, Integer status) {
        FinVouchers voucher = new FinVouchers();
        voucher.setVoucherId(voucherId);
        voucher.setVoucherStatus(status);
        voucher.setPostTime(new Date());
        finVouchersMapper.updateFinVouchers(voucher);
    }

    /**
     * 凭证反过账
     */
    public void reversePosting(Long voucherId) {
        // 1. 检查凭证状态
        FinVouchers voucher = finVouchersMapper.selectFinVouchersByVoucherId(voucherId);
        if (voucher.getVoucherStatus() != 3) { // 3-已过账
            throw new BizException("凭证未过账，不能反过账");
        }

        // 2. 获取会计期间
        String period = FinUtil.getAccountingPeriod(voucher.getVoucherDate());

        // 3. 反向更新总账和明细账
        List<FinVoucherEntries> entries = voucher.getEntries();
        if (entries != null) {
            for (FinVoucherEntries entry : entries) {
                reverseGeneralLedger(entry, period);
            }
        }

        // 4. 删除明细账记录
        finDetailLedgerService.deleteByVoucherId(voucherId);

        // 5. 更新凭证状态
        voucher.setVoucherStatus(2);
        voucher.setPostTime(null);
        finVouchersMapper.updateFinVouchers(voucher);
    }

    @Override
    public int filesFinVouchers(FinVouchers finVouchers) {
        int count=0;
        count=count+finVourchesFileService.deleteFinVourchesFileByVoucherId(finVouchers);

            if(finVouchers.getFiles()!=null) {
                for (FinVourchesFile file : finVouchers.getFiles()) {
                    file.setVoucherId(finVouchers.getVoucherId());
                    file.setGroupid(finVouchers.getGroupid());
                    file.setOpttime(new Date());
                    count=count+finVourchesFileService.insertFinVourchesFile(file);
                }
            }
            return count;
    }

    /**
     * 反向更新总账
     */
    private void reverseGeneralLedger(FinVoucherEntries entry, String period) {
        FinGeneralLedger generalLedger = finGeneralLedgerService.selectBySubjectAndPeriod(entry.getGroupid(), entry.getSubjectId(), period);
        if (generalLedger != null) {
            // 减去相应的借贷金额
            BigDecimal newDebit = generalLedger.getDebitTotal().subtract(entry.getDebitAmount());
            BigDecimal newCredit = generalLedger.getCreditTotal().subtract(entry.getCreditAmount());

            generalLedger.setDebitTotal(newDebit);
            generalLedger.setCreditTotal(newCredit);

            // 重新计算期末余额
            BalanceResult endBalance = calculateEndBalance(
                    generalLedger.getBeginBalance(),
                    generalLedger.getBeginDirection().intValue(),
                    newDebit,
                    newCredit,
                    entry.getSubjectId()
            );
            generalLedger.setEndBalance(endBalance.getBalance());
            generalLedger.setEndDirection(endBalance.getDirection());
            generalLedger.setUpdatedTime(new Date());
            finGeneralLedgerService.updateFinGeneralLedger(generalLedger);
        }
    }





}