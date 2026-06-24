package com.wimoor.finance.closing.service.strategy.impl;

import cn.hutool.core.util.StrUtil;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.user.UserInfo;
import com.wimoor.finance.closing.domain.FinClosingTemplate;
import com.wimoor.finance.closing.domain.FinClosingTemplateProfitLoss;
import com.wimoor.finance.closing.domain.FinClosingTemplateVouchers;
import com.wimoor.finance.closing.service.IFinClosingTemplateProfitLossService;
import com.wimoor.finance.closing.service.IFinClosingTemplateService;
import com.wimoor.finance.closing.service.IFinClosingTemplateVouchersService;
import com.wimoor.finance.closing.service.strategy.IFinClosingTemplateStrategy;
import com.wimoor.finance.ledger.domain.FinGeneralLedger;
import com.wimoor.finance.ledger.service.IFinGeneralLedgerService;
import com.wimoor.finance.setting.domain.FinAccountingPeriods;
import com.wimoor.finance.setting.domain.FinAccountingSubjects;
import com.wimoor.finance.setting.service.IFinAccountingPeriodsService;
import com.wimoor.finance.setting.service.IFinAccountingSubjectsService;
import com.wimoor.finance.voucher.domain.FinVoucherEntries;
import com.wimoor.finance.voucher.domain.FinVouchers;
import com.wimoor.finance.voucher.service.IFinVouchersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Slf4j
public class ProfitLossTemplateStrategy implements IFinClosingTemplateStrategy {

    private static final String FTYPE = "loss";

    @Resource
    IFinClosingTemplateService finClosingTemplateService;
    @Resource
    IFinClosingTemplateProfitLossService finClosingTemplateProfitLossService;
    @Resource
    IFinVouchersService iFinVouchersService;
    @Resource
    IFinClosingTemplateVouchersService finClosingTemplateVouchersService;
    @Resource
    IFinAccountingPeriodsService iFinAccountingPeriodsService;
    @Resource
    IFinAccountingSubjectsService finAccountingSubjectsService;
    @Resource
    IFinGeneralLedgerService finGeneralLedgerService;

    @Override
    public String getFtype() {
        return FTYPE;
    }

    private FinAccountingPeriods getPeriod(String groupid, String periodCode) {
        FinAccountingPeriods period = null;
        if (StrUtil.isNotBlank(periodCode)) {
            period = iFinAccountingPeriodsService.selectByPeriod(groupid, periodCode);
            if (period == null) {
                String periodDate = periodCode + "01";
                SimpleDateFormat FMT_YMD = new SimpleDateFormat("yyyyMMdd");
                try {
                    period = iFinAccountingPeriodsService.selectFinAccountingPeriodsByDate(groupid, FMT_YMD.parse(periodDate));
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        } else {
            period = iFinAccountingPeriodsService.getCurrentPeriod(groupid);
        }
        if (period == null) {
            throw new BizException("未找到指定的会计期间");
        }
        if (period.getPeriodStatus() == 3) {
            throw new BizException("会计期间已关闭，无法生成凭证");
        }
        return period;
    }

    /**
     * 根据模板ID和租户ID获取结转损益配置
     */
    private FinClosingTemplateProfitLoss getProfitLossConfig(String templateId, String groupid) {
        FinClosingTemplateProfitLoss query = new FinClosingTemplateProfitLoss();
        query.setTemplateId(templateId);
        query.setGroupid(groupid);
        List<FinClosingTemplateProfitLoss> list = finClosingTemplateProfitLossService.selectFinClosingTemplateProfitLossList(query);
        if (list != null && !list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 根据科目编码查找科目
     */
    private FinAccountingSubjects getSubjectByCode(String groupid, String subjectCode) {
        if (StrUtil.isBlank(subjectCode)) {
            return null;
        }
        return finAccountingSubjectsService.selectByGroupCode(groupid, subjectCode);
    }

    @Override
    public void generateVoucher(UserInfo userInfo, String templateId, String periodCode) {
        // 第一步：获取模板对象
        FinClosingTemplate template = finClosingTemplateService.selectFinClosingTemplateById(templateId);
        if (template == null) {
            throw new BizException("模板不存在");
        }
        
        log.info("模板信息: id={}, groupid={}, ftype={}", template.getId(), template.getGroupid(), template.getFtype());

        // 第二步：获取结转损益配置
        FinClosingTemplateProfitLoss plConfig = getProfitLossConfig(templateId, template.getGroupid());
        if (plConfig == null) {
            log.error("未找到结转损益配置: templateId={}, groupid={}", templateId, template.getGroupid());
            throw new BizException("未找到结转损益配置，请先进行设置");
        }

        // 第三步：获取会计期间
        FinAccountingPeriods period = getPeriod(template.getGroupid(), periodCode);
        String periodCodeForLedger = period.getPeriodCode();

        // 第四步：确定凭证日期
        Date voucherDate= period.getEndDate();


        // 第五步：获取结转目标科目（损益类科目的结转科目，通常为"本年利润"）
        String targetSubjectCode = plConfig.getProfitLossSubjectCode();
        if (StrUtil.isBlank(targetSubjectCode)) {
            targetSubjectCode = "3103";
        }
        FinAccountingSubjects targetSubject = getSubjectByCode(template.getGroupid(), targetSubjectCode);
        if (targetSubject == null) {
            throw new BizException("未找到结转目标科目：" + targetSubjectCode + "，请检查配置");
        }

        // 第六步：获取所有损益类科目
        List<FinAccountingSubjects> profitLossSubjects = finAccountingSubjectsService.getProfitLossSubjects(template.getGroupid());
        if (profitLossSubjects == null || profitLossSubjects.isEmpty()) {
            throw new BizException("未配置损益类科目");
        }

        // 第七步：确定凭证分类方式
        Integer voucherClass = template.getVoucherClass();
        boolean separateVoucher = voucherClass != null && voucherClass == 1;

        // 第八步：确定摘要
        String summary = StrUtil.isNotBlank(plConfig.getSummary()) ? plConfig.getSummary() : "结转本期损益";

        // 第九步：确定方向处理方式
        boolean positiveAmount = plConfig.getDirectionHandling() != null && plConfig.getDirectionHandling() == 1;

        // 第十步：按科目分类收集需要结转的数据
        List<SubjectTransferInfo> incomeTransfers = new ArrayList<>();
        List<SubjectTransferInfo> expenseTransfers = new ArrayList<>();

        for (FinAccountingSubjects subject : profitLossSubjects) {
            FinGeneralLedger ledger = finGeneralLedgerService.selectBySubjectAndPeriod(
                    template.getGroupid(), subject.getSubjectId().toString(), periodCodeForLedger);

            if (ledger == null || ledger.getEndBalance() == null
                    || ledger.getEndBalance().compareTo(BigDecimal.ZERO) == 0) {
                continue;
            }

            BigDecimal balance = ledger.getEndBalance();
            Integer direction = ledger.getEndDirection();
            if (direction == null) {
                direction = subject.getDirection();
            }
            if (direction == null) {
                log.warn("科目[{}]方向为空，跳过", subject.getSubjectCode());
                continue;
            }

            SubjectTransferInfo info = new SubjectTransferInfo();
            info.subject = subject;
            info.balance = positiveAmount ? balance.abs() : balance;
            info.direction = direction;
            info.summary = summary;

            if (direction == 2) {
                // 贷方余额（收入类）
                incomeTransfers.add(info);
            } else {
                // 借方余额（费用类）
                expenseTransfers.add(info);
            }
        }

        if (incomeTransfers.isEmpty() && expenseTransfers.isEmpty()) {
            throw new BizException("所有损益类科目余额为零，无需结转");
        }

        // 第十一步：生成凭证
        if (separateVoucher) {
            // 收益与损益分开结转
            if (!incomeTransfers.isEmpty()) {
                createAndSaveVoucher(userInfo, template, period, voucherDate,
                        incomeTransfers, targetSubject, summary, "收益结转");
            }
            if (!expenseTransfers.isEmpty()) {
                createAndSaveVoucher(userInfo, template, period, voucherDate,
                        expenseTransfers, targetSubject, summary, "损益结转");
            }
        } else {
            // 收益与损益同时结转
            List<SubjectTransferInfo> allTransfers = new ArrayList<>();
            allTransfers.addAll(incomeTransfers);
            allTransfers.addAll(expenseTransfers);
            createAndSaveVoucher(userInfo, template, period, voucherDate,
                    allTransfers, targetSubject, summary, null);
        }
    }

    /**
     * 创建并保存凭证
     */
    private void createAndSaveVoucher(UserInfo userInfo, FinClosingTemplate template,
                                       FinAccountingPeriods period, Date voucherDate,
                                       List<SubjectTransferInfo> transfers,
                                       FinAccountingSubjects targetSubject,
                                       String summary, String voucherSuffix) {
        FinVouchers finVouchers = new FinVouchers();
        finVouchers.setVoucherType(template.getVoucherType());
        finVouchers.setGroupid(template.getGroupid());
        finVouchers.setVoucherDate(voucherDate);
        finVouchers.setVoucherNo(iFinVouchersService.selectNextVoucherNo(finVouchers));
        finVouchers.setVoucherStatus(3);
        finVouchers.setDataSource(3); // 结账模版生成

        List<FinVoucherEntries> entryList = new ArrayList<>();
        BigDecimal debitTotal = BigDecimal.ZERO;
        BigDecimal creditTotal = BigDecimal.ZERO;
        long entryNo = 1;
        StringBuilder datalog = new StringBuilder();

        String entrySummary = summary;
        if (StrUtil.isNotBlank(voucherSuffix)) {
            entrySummary = summary + "（" + voucherSuffix + "）";
        }

        for (SubjectTransferInfo transfer : transfers) {
            FinAccountingSubjects subject = transfer.subject;
            BigDecimal balance = transfer.balance;
            Integer direction = transfer.direction;

            if (direction == 2) {
                // 贷方余额（收入类）：借方记收入科目，贷方记目标科目
                FinVoucherEntries debitEntry = new FinVoucherEntries();
                debitEntry.setSubjectId(subject.getSubjectId().toString());
                debitEntry.setDebitAmount(balance.abs());
                debitEntry.setSummary(entrySummary);
                debitEntry.setEntryNo(entryNo++);
                entryList.add(debitEntry);
                debitTotal = debitTotal.add(balance.abs());
                datalog.append(debitEntry.getEntryNo()).append("-借-").append(subject.getSubjectName())
                        .append("-").append(balance.abs()).append(";");

                FinVoucherEntries creditEntry = new FinVoucherEntries();
                creditEntry.setSubjectId(targetSubject.getSubjectId().toString());
                creditEntry.setCreditAmount(balance.abs());
                creditEntry.setSummary(entrySummary);
                creditEntry.setEntryNo(entryNo++);
                entryList.add(creditEntry);
                creditTotal = creditTotal.add(balance.abs());
                datalog.append(creditEntry.getEntryNo()).append("-贷-").append(targetSubject.getSubjectName())
                        .append("-").append(balance.abs()).append(";");
            } else {
                // 借方余额（费用类）：借方记目标科目，贷方记费用科目
                FinVoucherEntries debitEntry = new FinVoucherEntries();
                debitEntry.setSubjectId(targetSubject.getSubjectId().toString());
                debitEntry.setDebitAmount(balance.abs());
                debitEntry.setSummary(entrySummary);
                debitEntry.setEntryNo(entryNo++);
                entryList.add(debitEntry);
                debitTotal = debitTotal.add(balance.abs());
                datalog.append(debitEntry.getEntryNo()).append("-借-").append(targetSubject.getSubjectName())
                        .append("-").append(balance.abs()).append(";");

                FinVoucherEntries creditEntry = new FinVoucherEntries();
                creditEntry.setSubjectId(subject.getSubjectId().toString());
                creditEntry.setCreditAmount(balance.abs());
                creditEntry.setSummary(entrySummary);
                creditEntry.setEntryNo(entryNo++);
                entryList.add(creditEntry);
                creditTotal = creditTotal.add(balance.abs());
                datalog.append(creditEntry.getEntryNo()).append("-贷-").append(subject.getSubjectName())
                        .append("-").append(balance.abs()).append(";");
            }
        }

        // 检查借贷平衡
        if (debitTotal.compareTo(creditTotal) != 0) {
            throw new BizException("凭证生成失败：借贷不平衡，借方金额：" + debitTotal + "，贷方金额：" + creditTotal);
        }

        finVouchers.setTotalAmount(debitTotal);
        finVouchers.setEntries(entryList);

        // 检查是否已存在该模板该期间的凭证
        FinClosingTemplateVouchers queryVouchers = new FinClosingTemplateVouchers();
        queryVouchers.setTemplateId(template.getId());
        queryVouchers.setGroupid(template.getGroupid());
        queryVouchers.setVoucherDate(voucherDate);
        FinClosingTemplateVouchers existingTemplateVouchers = finClosingTemplateVouchersService.selectByTemplate(queryVouchers);

        if (existingTemplateVouchers != null) {
            // 更新现有凭证
            FinVouchers oldFinVouchers = iFinVouchersService.selectFinVouchersByVoucherId(
                    Long.valueOf(existingTemplateVouchers.getVourchesId()));
            finVouchers.setVoucherNo(oldFinVouchers.getVoucherNo());
            finVouchers.setUpdateBy(userInfo.getUserName());
            finVouchers.setUpdatedTime(new Date());
            finVouchers.setVoucherId(Long.valueOf(existingTemplateVouchers.getVourchesId()));
            for (FinVoucherEntries entry : finVouchers.getEntries()) {
                entry.setVoucherId(Long.valueOf(existingTemplateVouchers.getVourchesId()));
            }
            iFinVouchersService.updateFinVouchers(finVouchers);

            existingTemplateVouchers.setDatalog(datalog.toString());
            existingTemplateVouchers.setUpdateBy(userInfo.getUserName());
            existingTemplateVouchers.setVoucherDate(voucherDate);
            existingTemplateVouchers.setUpdatedTime(new Date());
            finClosingTemplateVouchersService.updateFinClosingTemplateVouchers(existingTemplateVouchers);
        } else {
            finVouchers.setCreateBy(userInfo.getUserName());
            finVouchers.setUpdateBy(userInfo.getUserName());
            finVouchers.setCreatedTime(new Date());
            finVouchers.setUpdatedTime(new Date());
            iFinVouchersService.insertFinVouchers(finVouchers);

            FinClosingTemplateVouchers templateVouchers = new FinClosingTemplateVouchers();
            templateVouchers.setTemplateId(template.getId());
            templateVouchers.setGroupid(template.getGroupid());
            templateVouchers.setDatalog(datalog.toString());
            templateVouchers.setVourchesId(finVouchers.getVoucherId().toString());
            templateVouchers.setVoucherDate(voucherDate);
            templateVouchers.setCreatedTime(new Date());
            templateVouchers.setUpdatedTime(new Date());
            templateVouchers.setCreateBy(userInfo.getUserName());
            templateVouchers.setUpdateBy(userInfo.getUserName());
            finClosingTemplateVouchersService.insertFinClosingTemplateVouchers(templateVouchers);
        }
    }

    /**
     * 科目结转信息内部类
     */
    private static class SubjectTransferInfo {
        FinAccountingSubjects subject;
        BigDecimal balance;
        Integer direction;
        String summary;
    }

    @Override
    public void initTemplateItem(FinClosingTemplate template) {
        // 结转损益模板不需要初始化Item
    }
    
    @Override
    public Map<String, Object> getCalculationDetail(String templateId, String periodCode) {
        Map<String, Object> result = new java.util.HashMap<>();
        
        FinClosingTemplate template = finClosingTemplateService.selectFinClosingTemplateById(templateId);
        if (template == null) {
            return result;
        }
        FinAccountingSubjects querySubject=new FinAccountingSubjects();
        querySubject.setGroupid(template.getGroupid());
        querySubject.setStatus(1);
        List<FinAccountingSubjects> subjects = finAccountingSubjectsService.selectFinAccountingSubjectsList(querySubject);
        Map<String, FinAccountingSubjects> codeMap = new HashMap<>();
        for (FinAccountingSubjects subject : subjects) {
            codeMap.put(subject.getSubjectCode(), subject);
        }
        String groupid = template.getGroupid();
        FinClosingTemplateProfitLoss plConfig = getProfitLossConfig(templateId, groupid);
        FinAccountingPeriods period = getPeriod(groupid, periodCode);
        
        // 目标科目
        String targetSubjectCode = plConfig != null ? plConfig.getProfitLossSubjectCode() : "3103";
        if (StrUtil.isBlank(targetSubjectCode)) targetSubjectCode = "3103";
        FinAccountingSubjects targetSubject = getSubjectByCode(groupid, targetSubjectCode);
        result.put("templateName", template.getName());
        result.put("targetSubjectCode", targetSubjectCode);
        result.put("targetSubjectName", targetSubject != null ?finAccountingSubjectsService.buildFullSubjectName(targetSubject,codeMap) : "");
        result.put("formula", "损益类科目期末余额 → 结转至 " + targetSubjectCode + " " + (targetSubject != null ? targetSubject.getSubjectName() : ""));
        
        // 获取损益类科目明细
        List<FinAccountingSubjects> profitLossSubjects = finAccountingSubjectsService.getProfitLossSubjects(groupid);
        List<Map<String, Object>> incomeItems = new ArrayList<>();
        List<Map<String, Object>> expenseItems = new ArrayList<>();
        BigDecimal totalIncome = BigDecimal.ZERO;
        BigDecimal totalExpense = BigDecimal.ZERO;
        
        if (profitLossSubjects != null) {
            for (FinAccountingSubjects subject : profitLossSubjects) {
                FinGeneralLedger ledger = finGeneralLedgerService.selectBySubjectAndPeriod(
                        groupid, subject.getSubjectId().toString(), period.getPeriodCode());
                
                if (ledger == null || ledger.getEndBalance() == null
                        || ledger.getEndBalance().compareTo(BigDecimal.ZERO) == 0) {
                    continue;
                }
                
                Map<String, Object> item = new java.util.HashMap<>();
                item.put("subjectCode", subject.getSubjectCode());
                finAccountingSubjectsService.buildFullSubjectName(subject,codeMap);
                item.put("subjectName", subject.getSubjectCode() + " " + finAccountingSubjectsService.buildFullSubjectName(subject,codeMap));
                item.put("balance", ledger.getEndBalance());
                item.put("direction", ledger.getEndDirection() != null ? ledger.getEndDirection() : subject.getDirection());
                
                Integer direction = ledger.getEndDirection() != null ? ledger.getEndDirection() : subject.getDirection();
                if (direction == 2) {
                    incomeItems.add(item);
                    totalIncome = totalIncome.add(ledger.getEndBalance().abs());
                } else {
                    expenseItems.add(item);
                    totalExpense = totalExpense.add(ledger.getEndBalance().abs());
                }
            }
        }
        
        result.put("incomeItems", incomeItems);
        result.put("expenseItems", expenseItems);
        result.put("totalIncome", totalIncome);
        result.put("totalExpense", totalExpense);
        result.put("periodName", period.getPeriodName());
        
        return result;
    }
}
