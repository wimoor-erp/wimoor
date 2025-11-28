package com.wimoor.finance.closing.service;

import com.wimoor.finance.closing.domain.ProfitLossTransferResult;
import com.wimoor.finance.closing.domain.TransferResult;
import com.wimoor.finance.closing.domain.TransferVoucherInfo;
import com.wimoor.finance.ledger.domain.FinGeneralLedger;
import com.wimoor.finance.ledger.mapper.FinGeneralLedgerMapper;
import com.wimoor.finance.ledger.service.IFinGeneralLedgerService;
import com.wimoor.finance.setting.domain.FinAccountingSubjects;
import com.wimoor.finance.setting.service.IFinAccountingSubjectsService;
import com.wimoor.finance.voucher.domain.FinVouchers;
import com.wimoor.finance.voucher.domain.FinVoucherEntries;
import com.wimoor.finance.voucher.mapper.FinVouchersMapper;
import com.wimoor.finance.voucher.mapper.FinVoucherEntriesMapper;
import com.wimoor.finance.voucher.service.IFinVoucherEntriesService;
import com.wimoor.finance.voucher.service.IFinVouchersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class ProfitLossTransferService {
    @Autowired
    IFinAccountingSubjectsService finAccountingSubjectsService;
    
    @Autowired
    IFinGeneralLedgerService finGeneralLedgerService;
    
    @Autowired
    FinVouchersMapper finVouchersMapper;
    
    @Autowired
    IFinVoucherEntriesService FinVoucherEntriesService;
    
    @Autowired
    IFinVouchersService voucherService;
    


    /**
     * 结转损益类科目到本年利润
     */
    // 1. 定义一个内部类来保存待创建的分录信息
    private static class EntryInfo {
        String subjectId;
        BigDecimal debitAmount;
        BigDecimal creditAmount;
        String summary;
        
        public EntryInfo(String subjectId, BigDecimal debitAmount, BigDecimal creditAmount, String summary) {
            this.subjectId = subjectId;
            this.debitAmount = debitAmount;
            this.creditAmount = creditAmount;
            this.summary = summary;
        }
    }
    
    // 2. 修改transferProfitAndLoss方法，实现单个凭证的创建逻辑
    public ProfitLossTransferResult transferProfitAndLoss(String groupid, String period) {
        ProfitLossTransferResult result = new ProfitLossTransferResult(groupid, period, "system");
        
        try {
            log.info("开始执行损益结转: groupid={}, period={}", groupid, period);
            
            // 获取本年利润科目
            FinAccountingSubjects profitSubject = finAccountingSubjectsService.getProfitSubject(groupid);
            if (profitSubject == null) {
                result.addError("未找到本年利润科目");
                return result;
            }
            
            // 创建一个凭证
            FinVouchers voucher = new FinVouchers();
            voucher.setGroupid(groupid);
            voucher.setVoucherType("转");
            voucher.setVoucherNo(generateTransferVoucherNo(groupid, period));
            voucher.setVoucherDate(getPeriodEndDate(period));
            voucher.setVoucherStatus(1); // 待审核
            voucher.setPreparerBy("system");
            voucher.setCreatedTime(new Date());
            
            // 收集所有分录信息
            List<EntryInfo> allEntries = new ArrayList<>();
            BigDecimal totalDebit = BigDecimal.ZERO;
            BigDecimal totalCredit = BigDecimal.ZERO;
            
            // 1. 处理收入科目结转
            List<FinAccountingSubjects> incomeSubjects = finAccountingSubjectsService.getIncomeSubjects(groupid);
            for (FinAccountingSubjects subject : incomeSubjects) {
                FinGeneralLedger ledger = finGeneralLedgerService.selectBySubjectAndPeriod(
                        groupid, subject.getSubjectId().toString(), period);
                
                if (ledger != null && ledger.getEndBalance().compareTo(BigDecimal.ZERO) != 0) {
                    BigDecimal amount = ledger.getEndBalance();
                    BigDecimal absAmount = amount.abs();
                    
                    if (amount.compareTo(BigDecimal.ZERO) > 0) {
                        // 收入科目有贷方余额，结转时做借方分录
                        allEntries.add(new EntryInfo(
                                subject.getSubjectId().toString(), 
                                absAmount, 
                                BigDecimal.ZERO, 
                                "结转" + subject.getSubjectName()
                        ));
                        totalDebit = totalDebit.add(absAmount);
                    } else {
                        // 收入科目有借方余额（负数收入），结转时做贷方分录
                        allEntries.add(new EntryInfo(
                                subject.getSubjectId().toString(), 
                                BigDecimal.ZERO, 
                                absAmount, 
                                "结转" + subject.getSubjectName()
                        ));
                        totalCredit = totalCredit.add(absAmount);
                    }
                }
            }
            
            // 2. 处理费用科目结转
            List<FinAccountingSubjects> expenseSubjects = finAccountingSubjectsService.getExpenseSubjects(groupid);
            for (FinAccountingSubjects subject : expenseSubjects) {
                FinGeneralLedger ledger = finGeneralLedgerService.selectBySubjectAndPeriod(
                        groupid, subject.getSubjectId().toString(), period);
                
                if (ledger != null && ledger.getEndBalance().compareTo(BigDecimal.ZERO) != 0) {
                    BigDecimal amount = ledger.getEndBalance();
                    BigDecimal absAmount = amount.abs();
                    
                    if (amount.compareTo(BigDecimal.ZERO) > 0) {
                        // 费用科目有借方余额，结转时做贷方分录
                        allEntries.add(new EntryInfo(
                                subject.getSubjectId().toString(), 
                                BigDecimal.ZERO, 
                                absAmount, 
                                "结转" + subject.getSubjectName()
                        ));
                        totalCredit = totalCredit.add(absAmount);
                    } else {
                        // 费用科目有贷方余额（负数费用），结转时做借方分录
                        allEntries.add(new EntryInfo(
                                subject.getSubjectId().toString(), 
                                absAmount, 
                                BigDecimal.ZERO, 
                                "结转" + subject.getSubjectName()
                        ));
                        totalDebit = totalDebit.add(absAmount);
                    }
                }
            }
            
            // 3. 计算并添加本年利润科目分录
            BigDecimal profitAmount = totalCredit.subtract(totalDebit);
            if (profitAmount.compareTo(BigDecimal.ZERO) > 0) {
                // 盈利：本年利润科目贷记
                allEntries.add(new EntryInfo(
                        profitSubject.getSubjectId().toString(), 
                        BigDecimal.ZERO, 
                        profitAmount, 
                        "结转本期利润"
                ));
                totalCredit = totalCredit.add(profitAmount);
            } else if (profitAmount.compareTo(BigDecimal.ZERO) < 0) {
                // 亏损：本年利润科目借记
                allEntries.add(new EntryInfo(
                        profitSubject.getSubjectId().toString(), 
                        profitAmount.abs(), 
                        BigDecimal.ZERO, 
                        "结转本期亏损"
                ));
                totalDebit = totalDebit.add(profitAmount.abs());
            }
            
            // 设置凭证总金额
            voucher.setTotalAmount(totalDebit); // 借贷相等，用哪一方都可以
            
            // 插入凭证主表
            finVouchersMapper.insertFinVouchers(voucher);
            
            // 批量插入所有分录
            for (EntryInfo entryInfo : allEntries) {
                FinVoucherEntries entry = new FinVoucherEntries();
                entry.setVoucherId(voucher.getVoucherId());
                entry.setGroupid(groupid);
                entry.setSubjectId(entryInfo.subjectId);
                entry.setDebitAmount(entryInfo.debitAmount);
                entry.setCreditAmount(entryInfo.creditAmount);
                entry.setSummary(entryInfo.summary);
                FinVoucherEntriesService.insertFinVoucherEntries(entry);
            }
            
            // 自动过账损益结转凭证
            voucherService.batchPostVouchers(Arrays.asList(voucher.getVoucherId()));
            
            // 构建成功响应
            TransferVoucherInfo voucherInfo = new TransferVoucherInfo();
            voucherInfo.setVoucherId(voucher.getVoucherId());
            voucherInfo.setAmount(profitAmount.abs());
            voucherInfo.setDescription("损益结转凭证");
            result.addGeneratedVoucher(voucherInfo);
            result.setSuccess();
            log.info("损益结转完成: groupid={}, period={}, profit={}", groupid, period, profitAmount);
            
        } catch (Exception e) {
            log.error("损益结转失败: groupid={}, period={}", groupid, period, e);
            result.addError("损益结转失败: " + e.getMessage());
        }

        return result;
    }

    /*
     * 以下方法已被整合到新的transferProfitAndLoss方法中，不再使用
     */
    /*
        // 结转收入类科目
        // 修改transferIncomeAccounts方法，处理所有非零余额
        private TransferResult transferIncomeAccounts(String groupid, String period) {
            TransferResult result = new TransferResult();
            try {
                List<FinAccountingSubjects> incomeSubjects = finAccountingSubjectsService.getIncomeSubjects(groupid);
                BigDecimal totalIncome = BigDecimal.ZERO;
        
                for (FinAccountingSubjects subject : incomeSubjects) {
                    FinGeneralLedger ledger = finGeneralLedgerService.selectBySubjectAndPeriod(
                            groupid, subject.getSubjectId().toString(), period);
        
                    // 处理所有非零余额，无论正负
                    if (ledger != null && ledger.getEndBalance().compareTo(BigDecimal.ZERO) != 0) {
                        // 根据余额方向调整结转方向
                        generateIncomeTransferEntry(groupid, period, subject, ledger.getEndBalance());
                        totalIncome = totalIncome.add(ledger.getEndBalance());
                    }
                }
                
                result.setSuccess(true);
                result.setMessage("收入科目结转成功");
                result.setTotalAmount(totalIncome);
                
            } catch (Exception e) {
                result.setSuccess(false);
                result.setMessage("收入科目结转失败: " + e.getMessage());
            }
            
            return result;
        }
    
         //结转费用类科目
        // 修改transferExpenseAccounts方法，处理所有非零余额
        private TransferResult transferExpenseAccounts(String groupid, String period) {
            TransferResult result = new TransferResult();
            try {
                List<FinAccountingSubjects> expenseSubjects = finAccountingSubjectsService.getExpenseSubjects(groupid);
                BigDecimal totalExpense = BigDecimal.ZERO;
        
                for (FinAccountingSubjects subject : expenseSubjects) {
                    FinGeneralLedger ledger = finGeneralLedgerService.selectBySubjectAndPeriod(
                            groupid, subject.getSubjectId().toString(), period);
        
                    // 处理所有非零余额，无论正负
                    if (ledger != null && ledger.getEndBalance().compareTo(BigDecimal.ZERO) != 0) {
                        // 根据余额方向调整结转方向
                        generateExpenseTransferEntry(groupid, period, subject, ledger.getEndBalance());
                        totalExpense = totalExpense.add(ledger.getEndBalance());
                    }
                }
                
                result.setSuccess(true);
                result.setMessage("费用科目结转成功");
                result.setTotalAmount(totalExpense);
                
            } catch (Exception e) {
                result.setSuccess(false);
                result.setMessage("费用科目结转失败: " + e.getMessage());
            }
            
            return result;
        }
    
        //计算本期利润
        private BigDecimal calculateCurrentPeriodProfit(String groupid, String period) {
            // 1. 获取本年利润科目
            FinAccountingSubjects profitSubjectId = finAccountingSubjectsService.getProfitSubject(groupid);
            
            // 2. 查询本年利润科目余额
            FinGeneralLedger profitLedger = null;
            if (profitSubjectId != null) {
                profitLedger = finGeneralLedgerService.selectBySubjectAndPeriod(
                        groupid, profitSubjectId.getSubjectId().toString(), period);
            }
            if (profitLedger != null) {
                // 3. 根据余额方向计算实际利润
                if (profitLedger.getEndDirection() == 1) { // 借方
                    return profitLedger.getEndBalance().negate();
                } else { // 贷方
                    return profitLedger.getEndBalance();
                }
            }
            
            return BigDecimal.ZERO;
        }
    
         // 生成收入结转分录
         // 将收入类科目的余额结转到本年利润科目
        private void generateIncomeTransferEntry(String groupid, String period, FinAccountingSubjects subject, BigDecimal amount) {
            // 获取本年利润科目
            FinAccountingSubjects profitSubject = finAccountingSubjectsService.getProfitSubject(groupid);
            if (profitSubject == null) {
                log.error("未找到本年利润科目: groupid={}", groupid);
                return;
            }
            
            // 创建凭证
            FinVouchers voucher = new FinVouchers();
            voucher.setGroupid(groupid);
            voucher.setVoucherType("转");
            voucher.setVoucherNo(generateTransferVoucherNo(groupid, period));
            voucher.setVoucherDate(getPeriodEndDate(period));
            voucher.setVoucherStatus(1); // 待审核
            voucher.setPreparerBy("system");
            voucher.setTotalAmount(amount);
            voucher.setCreatedTime(new Date());
            
            finVouchersMapper.insertFinVouchers(voucher);
            
            // 创建收入科目借方分录（减少收入）
            FinVoucherEntries incomeEntry = new FinVoucherEntries();
            incomeEntry.setVoucherId(voucher.getVoucherId());
            incomeEntry.setGroupid(groupid);
            incomeEntry.setSubjectId(subject.getSubjectId().toString());
            incomeEntry.setDebitAmount(amount);
            incomeEntry.setCreditAmount(BigDecimal.ZERO);
            incomeEntry.setSummary("结转" + subject.getSubjectName());
            incomeEntry.setVoucherDate(voucher.getVoucherDate());
        
            FinVoucherEntriesService.insertFinVoucherEntries(incomeEntry);
            
            // 创建本年利润科目贷方分录（增加利润）
            FinVoucherEntries profitEntry = new FinVoucherEntries();
            profitEntry.setVoucherId(voucher.getVoucherId());
            profitEntry.setGroupid(groupid);
            profitEntry.setSubjectId(profitSubject.getSubjectId().toString());
            profitEntry.setDebitAmount(BigDecimal.ZERO);
            profitEntry.setCreditAmount(amount);
            profitEntry.setSummary("结转" + subject.getSubjectName());
            profitEntry.setVoucherDate(voucher.getVoucherDate());
        
            FinVoucherEntriesService.insertFinVoucherEntries(profitEntry);
            
            log.info("收入结转分录生成成功: subjectId={}, amount={}, voucherId={}", 
                    subject.getSubjectId(), amount, voucher.getVoucherId());
        }
        

         // 生成费用结转分录
         //将费用类科目的余额结转到本年利润科目
        private void generateExpenseTransferEntry(String groupid, String period, FinAccountingSubjects subject, BigDecimal amount) {
            // 获取本年利润科目
            FinAccountingSubjects profitSubject = finAccountingSubjectsService.getProfitSubject(groupid);
            if (profitSubject == null) {
                log.error("未找到本年利润科目: groupid={}", groupid);
                return;
            }
            
            // 创建凭证
            FinVouchers voucher = new FinVouchers();
            voucher.setGroupid(groupid);
            voucher.setVoucherType("转");
            voucher.setVoucherNo(generateTransferVoucherNo(groupid, period));
            voucher.setVoucherDate(getPeriodEndDate(period));
            voucher.setVoucherStatus(1); // 待审核
            voucher.setPreparerBy("system");
            voucher.setTotalAmount(amount);
            voucher.setCreatedTime(new Date());
            
            finVouchersMapper.insertFinVouchers(voucher);
            
            // 创建本年利润科目借方分录（减少利润）
            FinVoucherEntries profitEntry = new FinVoucherEntries();
            profitEntry.setVoucherId(voucher.getVoucherId());
            profitEntry.setGroupid(groupid);
            profitEntry.setSubjectId(profitSubject.getSubjectId().toString());
            profitEntry.setDebitAmount(amount);
            profitEntry.setCreditAmount(BigDecimal.ZERO);
            profitEntry.setSummary("结转" + subject.getSubjectName());
            profitEntry.setVoucherDate(voucher.getVoucherDate());
        
            FinVoucherEntriesService.insertFinVoucherEntries(profitEntry);
            
            // 创建费用科目贷方分录（减少费用）
            FinVoucherEntries expenseEntry = new FinVoucherEntries();
            expenseEntry.setVoucherId(voucher.getVoucherId());
            expenseEntry.setGroupid(groupid);
            expenseEntry.setSubjectId(subject.getSubjectId().toString());
            expenseEntry.setDebitAmount(BigDecimal.ZERO);
            expenseEntry.setCreditAmount(amount);
            expenseEntry.setSummary("结转" + subject.getSubjectName());
            expenseEntry.setVoucherDate(voucher.getVoucherDate());
        
            FinVoucherEntriesService.insertFinVoucherEntries(expenseEntry);
            
            log.info("费用结转分录生成成功: subjectId={}, amount={}, voucherId={}", 
                    subject.getSubjectId(), amount, voucher.getVoucherId());
        }


         // 生成盈利时的凭证分录
         // 将本年利润科目的余额结转到利润分配科目
        private void generateProfitEntries(FinVouchers voucher, BigDecimal profit) {
            // 获取本年利润科目和利润分配科目
            FinAccountingSubjects profitSubject = finAccountingSubjectsService.getProfitSubject(voucher.getGroupid());
            FinAccountingSubjects profitDistributionSubject = finAccountingSubjectsService.getProfitDistributionSubject(voucher.getGroupid());
            
            if (profitSubject == null || profitDistributionSubject == null) {
                log.error("未找到必要的会计科目: groupid={}", voucher.getGroupid());
                return;
            }
            
            // 创建本年利润科目借方分录（减少本年利润）
            FinVoucherEntries profitEntry = new FinVoucherEntries();
            profitEntry.setVoucherId(voucher.getVoucherId());
            profitEntry.setGroupid(voucher.getGroupid());
            profitEntry.setSubjectId(profitSubject.getSubjectId().toString());
            profitEntry.setDebitAmount(profit);
            profitEntry.setCreditAmount(BigDecimal.ZERO);
            profitEntry.setSummary("本年利润结转");
            profitEntry.setVoucherDate(voucher.getVoucherDate());
        
            FinVoucherEntriesService.insertFinVoucherEntries(profitEntry);
            
            // 创建利润分配科目贷方分录（增加未分配利润）
            FinVoucherEntries distributionEntry = new FinVoucherEntries();
            distributionEntry.setVoucherId(voucher.getVoucherId());
            distributionEntry.setGroupid(voucher.getGroupid());
            distributionEntry.setSubjectId(profitDistributionSubject.getSubjectId().toString());
            distributionEntry.setDebitAmount(BigDecimal.ZERO);
            distributionEntry.setCreditAmount(profit);
            distributionEntry.setSummary("本年利润结转");
            distributionEntry.setVoucherDate(voucher.getVoucherDate());
        
            FinVoucherEntriesService.insertFinVoucherEntries(distributionEntry);
            
            log.info("盈利凭证分录生成成功: voucherId={}, profit={}", voucher.getVoucherId(), profit);
        }
        

         // 生成亏损时的凭证分录
         // 将本年利润科目的亏损结转到利润分配科目
        private void generateLossEntries(FinVouchers voucher, BigDecimal loss) {
            // 获取本年利润科目和利润分配科目
            FinAccountingSubjects profitSubject = finAccountingSubjectsService.getProfitSubject(voucher.getGroupid());
            FinAccountingSubjects profitDistributionSubject = finAccountingSubjectsService.getProfitDistributionSubject(voucher.getGroupid());
            
            if (profitSubject == null || profitDistributionSubject == null) {
                log.error("未找到必要的会计科目: groupid={}", voucher.getGroupid());
                return;
            }
            
            // 创建利润分配科目借方分录（减少未分配利润）
            FinVoucherEntries distributionEntry = new FinVoucherEntries();
            distributionEntry.setVoucherId(voucher.getVoucherId());
            distributionEntry.setGroupid(voucher.getGroupid());
            distributionEntry.setSubjectId(profitDistributionSubject.getSubjectId().toString());
            distributionEntry.setDebitAmount(loss);
            distributionEntry.setCreditAmount(BigDecimal.ZERO);
            distributionEntry.setSummary("本年亏损结转");
            distributionEntry.setVoucherDate(voucher.getVoucherDate());
        
            FinVoucherEntriesService.insertFinVoucherEntries(distributionEntry);
            
            // 创建本年利润科目贷方分录（减少本年亏损）
            FinVoucherEntries profitEntry = new FinVoucherEntries();
            profitEntry.setVoucherId(voucher.getVoucherId());
            profitEntry.setGroupid(voucher.getGroupid());
            profitEntry.setSubjectId(profitSubject.getSubjectId().toString());
            profitEntry.setDebitAmount(BigDecimal.ZERO);
            profitEntry.setCreditAmount(loss);
            profitEntry.setSummary("本年亏损结转");
            profitEntry.setVoucherDate(voucher.getVoucherDate());
        
            FinVoucherEntriesService.insertFinVoucherEntries(profitEntry);
            
            log.info("亏损凭证分录生成成功: voucherId={}, loss={}", voucher.getVoucherId(), loss);
        }

// 在文件中找到现有位置并替换或添加以下方法实现


     // 生成损益结转凭证
     // @param groupid 租户ID
    //  @param period 会计期间（YYYYMM格式）
     // @param profit 本期利润金额（正数表示盈利，负数表示亏损）
     // @return 生成的凭证ID

    private Long generateProfitTransferVoucher(String groupid, String period, BigDecimal profit) {
        try {
            // 1. 创建损益结转凭证主表记录
            FinVouchers voucher = new FinVouchers();
            voucher.setGroupid(groupid);
            voucher.setVoucherType("转");
            voucher.setVoucherNo(generateTransferVoucherNo(groupid, period));
            voucher.setVoucherDate(getPeriodEndDate(period));
            voucher.setVoucherStatus(1); // 1-待审核
            voucher.setPreparerBy("system");
            voucher.setTotalAmount(profit.abs()); // 绝对值作为总金额
            voucher.setCreatedTime(new Date());
            voucher.setAttachmentCount(0);

            // 2. 插入凭证主表
            voucherService.insertFinVouchers(voucher);
            log.info("创建损益结转凭证: voucherId={}, period={}, amount={}",
                    voucher.getVoucherId(), period, profit.abs());

            // 3. 根据盈利或亏损生成不同的凭证分录
            if (profit.compareTo(BigDecimal.ZERO) > 0) {
                // 盈利：将本年利润结转到利润分配
                log.info("生成盈利凭证分录: profit={}", profit);
                generateProfitEntries(voucher, profit);
            } else if (profit.compareTo(BigDecimal.ZERO) < 0) {
                // 亏损：将本年亏损结转到利润分配
                log.info("生成亏损凭证分录: loss={}", profit.abs());
                generateLossEntries(voucher, profit.abs());
            } else {
                // 无利润也无亏损，生成零金额的结转凭证
                log.info("本期无利润无亏损，生成零金额结转凭证");
                generateZeroProfitEntries(voucher);
            }

            // 4. 自动审核凭证
            voucherService.approveFinVouchers(Arrays.asList(voucher.getVoucherId()));
            log.info("损益结转凭证已审核: voucherId={}", voucher.getVoucherId());

            return voucher.getVoucherId();
        } catch (Exception e) {
            log.error("生成损益结转凭证失败: groupid={}, period={}, profit={}",
                    groupid, period, profit, e);
            throw new RuntimeException("生成损益结转凭证失败: " + e.getMessage(), e);
        }
    }
*/
     // 生成零利润时的凭证分录（平衡分录）
     // 当本期无利润也无亏损时，生成一个特殊的平衡分录
    private void generateZeroProfitEntries(FinVouchers voucher) {
        // 获取本年利润科目和利润分配科目
        FinAccountingSubjects profitSubject = finAccountingSubjectsService.getProfitSubject(voucher.getGroupid());
        FinAccountingSubjects profitDistributionSubject = finAccountingSubjectsService.getProfitDistributionSubject(voucher.getGroupid());

        if (profitSubject == null || profitDistributionSubject == null) {
            log.error("未找到必要的会计科目: groupid={}", voucher.getGroupid());
            return;
        }

        // 创建平衡的零金额分录
        // 借：本年利润 0
        FinVoucherEntries debitEntry = new FinVoucherEntries();
        debitEntry.setVoucherId(voucher.getVoucherId());
        debitEntry.setGroupid(voucher.getGroupid());
        debitEntry.setSubjectId(profitSubject.getSubjectId().toString());
        debitEntry.setDebitAmount(BigDecimal.ZERO);
        debitEntry.setCreditAmount(BigDecimal.ZERO);
        debitEntry.setSummary("本期无利润无亏损");
        debitEntry.setEntryNo(1L);
        FinVoucherEntriesService.insertFinVoucherEntries(debitEntry);

        // 贷：利润分配 0
        FinVoucherEntries creditEntry = new FinVoucherEntries();
        creditEntry.setVoucherId(voucher.getVoucherId());
        creditEntry.setGroupid(voucher.getGroupid());
        creditEntry.setSubjectId(profitDistributionSubject.getSubjectId().toString());
        creditEntry.setDebitAmount(BigDecimal.ZERO);
        creditEntry.setCreditAmount(BigDecimal.ZERO);
        creditEntry.setSummary("本期无利润无亏损");
        creditEntry.setEntryNo(2L);
        FinVoucherEntriesService.insertFinVoucherEntries(creditEntry);
    }
    /**
     * 生成结转凭证号
     */
    private String generateTransferVoucherNo(String groupid, String period) {
        // 实现凭证号生成逻辑
        FinVouchers voucher = new FinVouchers();
        voucher.setGroupid(groupid);
        voucher.setVoucherType("转");
        return finVouchersMapper.selectNextVoucherNo(voucher);
    }
    
    /**
     * 获取期间结束日期
     */
    private Date getPeriodEndDate(String period) {
        try {
            // 假设期间格式为YYYYMM
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
            Date periodStart = sdf.parse(period);
            
            // 计算该月最后一天
            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.setTime(periodStart);
            cal.add(java.util.Calendar.MONTH, 1);
            cal.add(java.util.Calendar.DAY_OF_MONTH, -1);
            
            return cal.getTime();
        } catch (Exception e) {
            log.error("解析期间格式失败: {}", period, e);
            return new Date();
        }
    }
    

    
    // 内部类定义
    public static class IncomeSubject {
        private Long subjectId;
        private String subjectCode;
        private String subjectName;
        
        // getter和setter方法
        public Long getSubjectId() { return subjectId; }
        public void setSubjectId(Long subjectId) { this.subjectId = subjectId; }
        public String getSubjectCode() { return subjectCode; }
        public void setSubjectCode(String subjectCode) { this.subjectCode = subjectCode; }
        public String getSubjectName() { return subjectName; }
        public void setSubjectName(String subjectName) { this.subjectName = subjectName; }
    }
    
    public static class ExpenseSubject {
        private Long subjectId;
        private String subjectCode;
        private String subjectName;
        
        // getter和setter方法
        public Long getSubjectId() { return subjectId; }
        public void setSubjectId(Long subjectId) { this.subjectId = subjectId; }
        public String getSubjectCode() { return subjectCode; }
        public void setSubjectCode(String subjectCode) { this.subjectCode = subjectCode; }
        public String getSubjectName() { return subjectName; }
        public void setSubjectName(String subjectName) { this.subjectName = subjectName; }
    }

}