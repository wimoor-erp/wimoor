package com.wimoor.finance.closing.service.strategy.impl;

import cn.hutool.core.util.StrUtil;
import com.wimoor.common.mvc.BizException;
import com.wimoor.common.user.UserInfo;
import com.wimoor.finance.closing.domain.FinClosingTemplate;
import com.wimoor.finance.closing.domain.FinClosingTemplateItem;
import com.wimoor.finance.closing.domain.FinClosingTemplateVouchers;
import com.wimoor.finance.closing.service.IFinClosingTemplateItemService;
import com.wimoor.finance.closing.service.IFinClosingTemplateService;
import com.wimoor.finance.closing.service.IFinClosingTemplateVouchersService;
import com.wimoor.finance.closing.service.strategy.IFinClosingTemplateStrategy;
import com.wimoor.finance.ledger.domain.FinGeneralLedger;
import com.wimoor.finance.ledger.service.IFinGeneralLedgerService;
import com.wimoor.finance.setting.domain.FinAccountingPeriods;
import com.wimoor.finance.setting.domain.FinAccountingSubjects;
import com.wimoor.finance.setting.domain.FinCurrency;
import com.wimoor.finance.setting.service.IFinAccountingPeriodsService;
import com.wimoor.finance.setting.service.IFinAccountingSubjectsService;
import com.wimoor.finance.setting.service.IFinCurrencyService;
import com.wimoor.finance.voucher.domain.FinVoucherEntries;
import com.wimoor.finance.voucher.domain.FinVouchers;
import com.wimoor.finance.voucher.service.IFinVoucherEntriesService;
import com.wimoor.finance.voucher.service.IFinVouchersService;
import com.wimoor.finance.api.AmazonClientOneFeignManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Slf4j
public class ExchangeTemplateStrategy implements IFinClosingTemplateStrategy {

    private static final String FTYPE = "fct";
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Resource
    IFinClosingTemplateService finClosingTemplateService;
    @Resource
    IFinClosingTemplateItemService finClosingTemplateItemService;
    @Resource
    IFinVouchersService iFinVouchersService;
    @Resource
    IFinVoucherEntriesService iFinVoucherEntriesService;
    @Resource
    IFinCurrencyService iFinCurrencyService;
    @Resource
    AmazonClientOneFeignManager amazonClientOneFeignManager;
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

    /**
     * 获取会计期间（参照 ProfitLossTemplateStrategy）
     */
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
     * 获取模板科目配置，按 amountField 分组
     */
    private List<FinClosingTemplateItem> getTemplateItems(String templateId) {
        FinClosingTemplateItem query = new FinClosingTemplateItem();
        query.setClosingTemplateId(templateId);
        return finClosingTemplateItemService.selectFinClosingTemplateItemList(query);
    }

    @Override
    public void generateVoucher(UserInfo userInfo, String templateId, String periodCode) {
        // 第一步：获取模板对象
        FinClosingTemplate template = finClosingTemplateService.selectFinClosingTemplateById(templateId);
        if (template == null) {
            throw new BizException("模板不存在");
        }
        log.info("期末调汇模板信息: id={}, groupid={}, carryoverType={}, voucherClass={}",
                template.getId(), template.getGroupid(), template.getCarryoverType(), template.getVoucherClass());

        // 第二步：获取会计期间
        FinAccountingPeriods period = getPeriod(template.getGroupid(), periodCode);
        String groupid = template.getGroupid();

        // 第三步：获取模板科目配置（fin_closing_template_item）
        List<FinClosingTemplateItem> items = getTemplateItems(templateId);
        if (items == null || items.isEmpty()) {
            throw new BizException("未配置调汇科目，请先进行设置");
        }

        // 按 amountField 分类：income（汇兑收益科目）、loss（汇兑损失科目）
        String incomeSubjectId = null;
        String lossSubjectId = null;
        String summary = null;
        Integer incomeDirection = null;
        Integer lossDirection = null;

        for (FinClosingTemplateItem item : items) {
            if ("income".equals(item.getAmountField())) {
                incomeSubjectId = item.getSubjectId();
                summary = item.getSummary();
                incomeDirection = item.getDirection();
            } else if ("loss".equals(item.getAmountField())) {
                lossSubjectId = item.getSubjectId();
                summary = item.getSummary();
                lossDirection = item.getDirection();
            }
        }

        if (incomeSubjectId == null && lossSubjectId == null) {
            throw new BizException("未配置汇兑收益或损失科目，请先进行设置");
        }

        // 第四步：获取所有需要调汇的科目，计算汇兑损益
        Map<String, List<Map<String, Object>>> exchangeData = getExchangeAdjustments(groupid, period);

        List<Map<String, Object>> gainList = exchangeData.getOrDefault("gain", new ArrayList<>());
        List<Map<String, Object>> lossList = exchangeData.getOrDefault("loss", new ArrayList<>());

        if (gainList.isEmpty() && lossList.isEmpty()) {
            throw new BizException("所有调汇科目余额为零或无外币余额，无需调汇");
        }

        // 第五步：确定凭证分类方式（voucher_class: 1=分开生成，0=合并生成）
        Integer voucherClass = template.getVoucherClass();
        boolean separateVoucher = voucherClass != null && voucherClass == 1;

        // 第六步：生成凭证
        if (separateVoucher) {
            // 汇兑收益和汇兑损失分开生成凭证
            if (!gainList.isEmpty() && incomeSubjectId != null) {
                generateExchangeVoucher(userInfo, template, period, groupid,
                        incomeSubjectId, incomeDirection, gainList, summary, "汇兑收益");
            }
            if (!lossList.isEmpty() && lossSubjectId != null) {
                generateExchangeVoucher(userInfo, template, period, groupid,
                        lossSubjectId, lossDirection, lossList, summary, "汇兑损失");
            }
        } else {
            // 汇兑收益和汇兑损失合并生成一张凭证
            List<Map<String, Object>> allAdjustments = new ArrayList<>();
            allAdjustments.addAll(gainList);
            allAdjustments.addAll(lossList);

            // 合并时使用收益科目或损失科目（根据 carryoverType 决定）
            String targetSubjectId = incomeSubjectId != null ? incomeSubjectId : lossSubjectId;
            Integer targetDirection = incomeSubjectId != null ? incomeDirection : lossDirection;
            generateExchangeVoucher(userInfo, template, period, groupid,
                    targetSubjectId, targetDirection, allAdjustments, summary, null);
        }
    }

    /**
     * 获取所有调汇科目的汇兑损益数据
     * 计算逻辑：根据总账期末余额和当前汇率，计算汇兑损益
     *
     * @return Map: "gain" -> 收益列表, "loss" -> 损失列表
     *         每个元素包含: subjectId, amount, currency, direction
     */
    private Map<String, List<Map<String, Object>>> getExchangeAdjustments(String groupid, FinAccountingPeriods period) {
        Map<String, List<Map<String, Object>>> result = new HashMap<>();
        List<Map<String, Object>> gainList = new ArrayList<>();
        List<Map<String, Object>> lossList = new ArrayList<>();

        // 预先获取自定义汇率列表（避免在循环中重复调用）
        List<Map<String, Object>> customRates = null;
        String endDateStr = null;
        if (period.getEndDate() != null) {
            LocalDate endDate = period.getEndDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            endDateStr = endDate.format(DATE_FMT);
            try {
                customRates = amazonClientOneFeignManager.getMyCurrencyRate(endDateStr);
            } catch (Exception e) {
                log.warn("获取自定义汇率失败，将使用默认汇率: {}", e.getMessage());
            }
        }

        // 获取本位币代码（用于过滤）
        String localCurrencyCode = getLocalCurrencyCode(groupid);

        // 获取所有需要调汇的科目
        FinAccountingSubjects subjectQuery = new FinAccountingSubjects();
        subjectQuery.setGroupid(groupid);
        subjectQuery.setStatus(1);
        subjectQuery.setIsExchange(true);
        List<FinAccountingSubjects> allSubjects = finAccountingSubjectsService.selectFinAccountingSubjectsListAll(subjectQuery);

        if (allSubjects == null || allSubjects.isEmpty()) {
            return result;
        }

        String periodCode = period.getPeriodCode();

        for (FinAccountingSubjects subject : allSubjects) {
            // 获取该科目的总账期末余额
            FinGeneralLedger ledger = finGeneralLedgerService.selectBySubjectAndPeriod(
                    groupid, subject.getSubjectId().toString(), periodCode);

            if (ledger == null || ledger.getEndBalance() == null
                    || ledger.getEndBalance().compareTo(BigDecimal.ZERO) == 0) {
                continue;
            }

            BigDecimal endBalance = ledger.getEndBalance();

            // 获取该科目的外币币种（用"/"分隔的多币种字符串，如 "USD/CAD"）
            String foreignCurrencys = subject.getForeignCurrencys();
            if (StrUtil.isBlank(foreignCurrencys)) {
                log.warn("调汇科目[{}]未配置外币币种，跳过", subject.getSubjectCode());
                continue;
            }

            // 解析币种数组，过滤掉本位币
            String[] currencyArray = foreignCurrencys.split("/");
            List<String> validCurrencies = new ArrayList<>();
            for (String currencyCode : currencyArray) {
                if (StrUtil.isNotBlank(currencyCode) && StrUtil.isNotBlank(currencyCode.trim())) {
                    currencyCode = currencyCode.trim();
                    // 跳过本位币（本位币汇率为1，不需要调汇）
                    if (currencyCode.equals(localCurrencyCode)) {
                        log.debug("跳过本位币: {}", currencyCode);
                        continue;
                    }
                    validCurrencies.add(currencyCode);
                }
            }
            if (validCurrencies.isEmpty()) {
                log.warn("调汇科目[{}]外币币种配置为空，跳过", subject.getSubjectCode());
                continue;
            }

            // 收集各币种的外币余额和汇率，计算新本位币余额总和
            List<Map<String, Object>> currencyDataList = new ArrayList<>();
            BigDecimal totalNewLocalBalance = BigDecimal.ZERO;

            for (String currencyCode : validCurrencies) {
                // 获取该币种的当前期末汇率
                BigDecimal currentRate = getExchangeRateByPeriod(groupid, currencyCode, customRates, endDateStr);
                if (currentRate == null || currentRate.compareTo(BigDecimal.ZERO) == 0) {
                    log.warn("币种[{}]未配置汇率或汇率为零，跳过", currencyCode);
                    continue;
                }

                // 获取该科目的外币余额
                BigDecimal foreignBalance = calculateForeignBalance(groupid, subject.getSubjectId().toString(), currencyCode, period);
                if (foreignBalance.compareTo(BigDecimal.ZERO) == 0) {
                    continue;
                }

                // 按当前汇率计算本位币余额
                BigDecimal newLocalBalance = foreignBalance.multiply(currentRate).setScale(2, RoundingMode.HALF_UP);
                totalNewLocalBalance = totalNewLocalBalance.add(newLocalBalance);

                Map<String, Object> currencyData = new HashMap<>();
                currencyData.put("currency", currencyCode);
                currencyData.put("foreignBalance", foreignBalance);
                currencyData.put("rate", currentRate);
                currencyData.put("newLocalBalance", newLocalBalance);
                currencyDataList.add(currencyData);
            }

            if (currencyDataList.isEmpty()) {
                continue;
            }

            // 计算总汇兑损益 = 所有币种新本位币余额总和 - 原期末余额
            BigDecimal totalAdjustment = totalNewLocalBalance.subtract(endBalance);
            if (totalAdjustment.compareTo(BigDecimal.ZERO) == 0) {
                continue;
            }

            // 按各币种的贡献比例分配总调整额
            for (Map<String, Object> currencyData : currencyDataList) {
                BigDecimal newLocalBalance = (BigDecimal) currencyData.get("newLocalBalance");
                String currencyCode = (String) currencyData.get("currency");

                // 按比例分配调整额
                BigDecimal ratio = newLocalBalance.divide(totalNewLocalBalance, 10, RoundingMode.HALF_UP);
                BigDecimal adjustment = totalAdjustment.multiply(ratio).setScale(2, RoundingMode.HALF_UP);

                if (adjustment.compareTo(BigDecimal.ZERO) == 0) {
                    continue;
                }

                BigDecimal foreignBalance = (BigDecimal) currencyData.get("foreignBalance");
                BigDecimal rate = (BigDecimal) currencyData.get("rate");

                Map<String, Object> entry = new HashMap<>();
                entry.put("subjectId", subject.getSubjectId().toString());
                entry.put("amount", adjustment.abs());
                entry.put("currency", currencyCode);
                entry.put("direction", subject.getDirection());
                entry.put("foreignBalance", foreignBalance);
                entry.put("exchangeRate", rate);

                if (adjustment.compareTo(BigDecimal.ZERO) > 0) {
                    // 汇兑收益
                    gainList.add(entry);
                    log.info("科目[{}] {} 汇兑收益: {} {}",
                            subject.getSubjectCode(), subject.getSubjectName(),
                            adjustment.abs(), currencyCode);
                } else {
                    // 汇兑损失
                    lossList.add(entry);
                    log.info("科目[{}] {} 汇兑损失: {} {}",
                            subject.getSubjectCode(), subject.getSubjectName(),
                            adjustment.abs(), currencyCode);
                }
            }
        }

        result.put("gain", gainList);
        result.put("loss", lossList);
        return result;
    }

    /**
     * 根据会计期间获取汇率
     * 优先使用预获取的自定义汇率，如果没有则使用默认汇率
     *
     * @param groupid      组id
     * @param currencyCode 币种代码
     * @param customRates  预获取的自定义汇率列表
     * @param endDateStr   会计期间结束日期字符串
     * @return 汇率
     */
    private BigDecimal getExchangeRateByPeriod(String groupid, String currencyCode,
                                               List<Map<String, Object>> customRates, String endDateStr) {
        // 优先使用预获取的自定义汇率
        if (customRates != null && !customRates.isEmpty()) {
            for (Map<String, Object> rateMap : customRates) {
                String name = (String) rateMap.get("name");
                if (currencyCode.equals(name)) {
                    Object priceObj = rateMap.get("price");
                    if (priceObj != null) {
                        try {
                            BigDecimal customRate = new BigDecimal(priceObj.toString());
                            // getMyCurrencyRate 返回的是以100为基准的汇率，需要除以100
                            if (customRate.compareTo(BigDecimal.ZERO) > 0) {
                                log.info("使用自定义汇率: 币种={}, 日期={}, 汇率={}", currencyCode, endDateStr, customRate);
                                return customRate.divide(new BigDecimal("100"), 6, RoundingMode.HALF_UP);
                            }
                        } catch (NumberFormatException e) {
                            log.warn("自定义汇率格式错误: 币种={}, price={}", currencyCode, priceObj);
                        }
                    }
                    break;
                }
            }
        }

        // 回退使用 selectFinCurrencyByCode 获取默认汇率
        FinCurrency currency = iFinCurrencyService.selectFinCurrencyByCode(currencyCode, groupid);
        if (currency != null && currency.getRate() != null && currency.getRate().compareTo(BigDecimal.ZERO) > 0) {
            log.info("使用默认汇率: 币种={}, 汇率={}", currencyCode, currency.getRate());
            return currency.getRate();
        }

        return null;
    }

    /**
     * 获取本位币代码
     *
     * @param groupid 组id
     * @return 本位币代码，如果未找到返回 null
     */
    private String getLocalCurrencyCode(String groupid) {
        FinCurrency query = new FinCurrency();
        query.setGroupid(groupid);
        query.setIslocal(1);
        List<FinCurrency> currencyList = iFinCurrencyService.selectFinCurrencyList(query);
        if (currencyList != null && !currencyList.isEmpty()) {
            return currencyList.get(0).getCode();
        }
        return null;
    }

    /**
     * 计算某科目在指定会计期间的外币余额
     * 通过累计该科目在期间内所有凭证分录的原始外币金额（借方 - 贷方）
     */
    private BigDecimal calculateForeignBalance(String groupid, String subjectId, String currencyCode, FinAccountingPeriods period) {
        // 查询该科目及所有子科目的ID
        List<String> subjectIds = new ArrayList<>();
        subjectIds.add(subjectId);
        
        FinAccountingSubjects childQuery = new FinAccountingSubjects();
        childQuery.setGroupid(groupid);
        childQuery.setStatus(1);
        childQuery.setParentId(Long.parseLong(subjectId));
        List<FinAccountingSubjects> children = finAccountingSubjectsService.selectFinAccountingSubjectsListAll(childQuery);
        if (children != null) {
            for (FinAccountingSubjects child : children) {
                subjectIds.add(child.getSubjectId().toString());
            }
        }
        
        BigDecimal foreignDebit = BigDecimal.ZERO;
        BigDecimal foreignCredit = BigDecimal.ZERO;
        
        for (String sid : subjectIds) {
            FinVoucherEntries query = new FinVoucherEntries();
            query.setGroupid(groupid);
            query.setSubjectId(sid);
            query.setCurrency(currencyCode);

            List<FinVoucherEntries> entries = iFinVoucherEntriesService.selectFinVoucherEntriesList(query);

            if (entries == null || entries.isEmpty()) {
                continue;
            }

            for (FinVoucherEntries entry : entries) {
                // 使用原始外币金额计算
                if (entry.getOriginalAmount() != null && entry.getOriginalAmount().compareTo(BigDecimal.ZERO) != 0) {
                    if (entry.getDebitAmount() != null && entry.getDebitAmount().compareTo(BigDecimal.ZERO) > 0) {
                        foreignDebit = foreignDebit.add(entry.getOriginalAmount().abs());
                    } else if (entry.getCreditAmount() != null && entry.getCreditAmount().compareTo(BigDecimal.ZERO) > 0) {
                        foreignCredit = foreignCredit.add(entry.getOriginalAmount().abs());
                    }
                }
            }
        }

        return foreignDebit.subtract(foreignCredit);
    }

    /**
     * 生成汇兑凭证
     * 参照 ProfitLossTemplateStrategy 的凭证创建和更新逻辑
     */
    private void generateExchangeVoucher(UserInfo userInfo, FinClosingTemplate template,
                                         FinAccountingPeriods period, String groupid,
                                         String targetSubjectId, Integer targetDirection,
                                         List<Map<String, Object>> adjustments,
                                         String summary, String voucherSuffix) {
        // 参数校验
        if (adjustments == null || adjustments.isEmpty()) {
            log.warn("无调汇数据，跳过凭证生成");
            return;
        }
        if (StrUtil.isBlank(targetSubjectId)) {
            throw new BizException("对方科目ID不能为空");
        }

        // 创建凭证对象
        FinVouchers finVouchers = new FinVouchers();
        finVouchers.setVoucherType(template.getVoucherType());
        finVouchers.setGroupid(groupid);
        finVouchers.setVoucherDate(period.getEndDate());
        finVouchers.setPreparerBy(userInfo.getUserName());
        finVouchers.setPeriodId(period.getPeriodId());
        finVouchers.setVoucherStatus(3);
        finVouchers.setDataSource(3); // 结账模版生成
        finVouchers.setVoucherNo(iFinVouchersService.selectNextVoucherNo(finVouchers));

        // 创建凭证分录
        List<FinVoucherEntries> entryList = new ArrayList<>();
        BigDecimal debitTotal = BigDecimal.ZERO;
        BigDecimal creditTotal = BigDecimal.ZERO;
        long entryNo = 1;
        StringBuilder datalog = new StringBuilder();

        String entrySummary = summary;
        if (StrUtil.isNotBlank(voucherSuffix)) {
            entrySummary = summary + "（" + voucherSuffix + "）";
        }

        for (Map<String, Object> adjustment : adjustments) {
            BigDecimal amount = (BigDecimal) adjustment.get("amount");
            Integer subjectDirection = (Integer) adjustment.get("direction");
            String subjectId = (String) adjustment.get("subjectId");
            String currency = (String) adjustment.get("currency");

            // 获取外币余额和汇率用于追溯
            BigDecimal foreignBalance = (BigDecimal) adjustment.get("foreignBalance");
            BigDecimal exchangeRate = (BigDecimal) adjustment.get("exchangeRate");

            // 调汇科目分录
            FinVoucherEntries subjectEntry = new FinVoucherEntries();
            subjectEntry.setGroupid(groupid);
            subjectEntry.setEntryNo(entryNo++);
            subjectEntry.setSubjectId(subjectId);
            subjectEntry.setSummary(entrySummary);
            subjectEntry.setCurrency(currency);
            subjectEntry.setOriginalAmount(foreignBalance);
            subjectEntry.setExchangeRate(exchangeRate);

            // 对方科目分录（模板配置的汇兑收益/损失科目）
            FinVoucherEntries counterEntry = new FinVoucherEntries();
            counterEntry.setGroupid(groupid);
            counterEntry.setEntryNo(entryNo++);
            counterEntry.setSubjectId(targetSubjectId);
            counterEntry.setSummary(entrySummary);
            counterEntry.setCurrency(currency);
            counterEntry.setOriginalAmount(foreignBalance);
            counterEntry.setExchangeRate(exchangeRate);

            // 按科目方向设置借贷（carryoverType=1 按科目方向调汇）
            if (subjectDirection != null && subjectDirection == 1) {
                // 调汇科目为借方：汇兑收益时借方增加，汇兑损失时借方减少
                subjectEntry.setDebitAmount(amount);
                subjectEntry.setCreditAmount(BigDecimal.ZERO);
                counterEntry.setDebitAmount(BigDecimal.ZERO);
                counterEntry.setCreditAmount(amount);
            } else {
                // 调汇科目为贷方：汇兑收益时贷方增加，汇兑损失时贷方减少
                subjectEntry.setDebitAmount(BigDecimal.ZERO);
                subjectEntry.setCreditAmount(amount);
                counterEntry.setDebitAmount(amount);
                counterEntry.setCreditAmount(BigDecimal.ZERO);
            }

            entryList.add(subjectEntry);
            entryList.add(counterEntry);

            // 累加借贷金额
            debitTotal = debitTotal.add(subjectEntry.getDebitAmount()).add(counterEntry.getDebitAmount());
            creditTotal = creditTotal.add(subjectEntry.getCreditAmount()).add(counterEntry.getCreditAmount());

            datalog.append(subjectEntry.getEntryNo()).append("-").append(subjectId).append("-").append(amount).append(";");
            datalog.append(counterEntry.getEntryNo()).append("-").append(targetSubjectId).append("-").append(amount).append(";");
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
        queryVouchers.setGroupid(groupid);
        queryVouchers.setVoucherDate(period.getEndDate());
        FinClosingTemplateVouchers existingTemplateVouchers = finClosingTemplateVouchersService.selectByTemplate(queryVouchers);

        if (existingTemplateVouchers != null) {
            // 更新现有凭证
            Long existingVoucherId = Long.valueOf(existingTemplateVouchers.getVourchesId());
            FinVouchers oldFinVouchers = iFinVouchersService.selectFinVouchersByVoucherId(existingVoucherId);
            if (oldFinVouchers == null) {
                log.warn("关联的原凭证[voucherId={}]不存在，将创建新凭证", existingVoucherId);
            } else {
                finVouchers.setVoucherNo(oldFinVouchers.getVoucherNo());
            }
            finVouchers.setUpdateBy(userInfo.getUserName());
            finVouchers.setUpdatedTime(new Date());
            finVouchers.setVoucherId(existingVoucherId);

            // 删除旧分录，插入新分录
            iFinVoucherEntriesService.deleteByVoucherId(existingVoucherId);
            for (FinVoucherEntries entry : finVouchers.getEntries()) {
                entry.setVoucherId(existingVoucherId);
            }

            iFinVouchersService.updateFinVouchers(finVouchers);

            // 更新模板凭证关联
            existingTemplateVouchers.setDatalog(datalog.toString());
            existingTemplateVouchers.setUpdateBy(userInfo.getUserName());
            existingTemplateVouchers.setVoucherDate(period.getEndDate());
            existingTemplateVouchers.setUpdatedTime(new Date());
            finClosingTemplateVouchersService.updateFinClosingTemplateVouchers(existingTemplateVouchers);

            log.info("更新期末调汇凭证: voucherId={}", existingVoucherId);
        } else {
            // 创建新凭证
            finVouchers.setCreateBy(userInfo.getUserName());
            finVouchers.setUpdateBy(userInfo.getUserName());
            finVouchers.setCreatedTime(new Date());
            finVouchers.setUpdatedTime(new Date());
            iFinVouchersService.insertFinVouchers(finVouchers);

            // 创建模板凭证关联
            FinClosingTemplateVouchers templateVouchers = new FinClosingTemplateVouchers();
            templateVouchers.setTemplateId(template.getId());
            templateVouchers.setGroupid(groupid);
            templateVouchers.setDatalog(datalog.toString());
            templateVouchers.setVourchesId(finVouchers.getVoucherId().toString());
            templateVouchers.setVoucherDate(period.getEndDate());
            templateVouchers.setCreatedTime(new Date());
            templateVouchers.setUpdatedTime(new Date());
            templateVouchers.setCreateBy(userInfo.getUserName());
            templateVouchers.setUpdateBy(userInfo.getUserName());
            finClosingTemplateVouchersService.insertFinClosingTemplateVouchers(templateVouchers);

            log.info("创建期末调汇凭证: voucherId={}", finVouchers.getVoucherId());
        }
    }

    @Override
    public void initTemplateItem(FinClosingTemplate template) {
        // 期末调汇模板的科目配置由用户手动维护，无需系统初始化
    }
    
    @Override
    public Map<String, Object> getCalculationDetail(String templateId, String periodCode) {
        Map<String, Object> result = new HashMap<>();
        
        FinClosingTemplate template = finClosingTemplateService.selectFinClosingTemplateById(templateId);
        if (template == null) {
            return result;
        }
        
        String groupid = template.getGroupid();
        FinAccountingPeriods period = getPeriod(groupid, periodCode);
        FinAccountingSubjects querySubject = new FinAccountingSubjects();
        querySubject.setGroupid(groupid);
        querySubject.setStatus(1);
        List<FinAccountingSubjects> subjects = finAccountingSubjectsService.selectFinAccountingSubjectsList(querySubject);
        Map<String, FinAccountingSubjects> codeMap = new HashMap<>();
        for (FinAccountingSubjects subject : subjects) {
            codeMap.put(subject.getSubjectCode(), subject);
        }
        result.put("templateName", template.getName());
        result.put("periodName", period.getPeriodName());
        result.put("formula", "汇兑损益 = Σ(外币余额 × 期末汇率) - 原本位币余额");
        
        // 预先获取自定义汇率列表
        List<Map<String, Object>> customRates = null;
        String endDateStr = null;
        if (period.getEndDate() != null) {
            LocalDate endDate = period.getEndDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            endDateStr = endDate.format(DATE_FMT);
            try {
                customRates = amazonClientOneFeignManager.getMyCurrencyRate(endDateStr);
            } catch (Exception e) {
                log.warn("获取自定义汇率失败，将使用默认汇率: {}", e.getMessage());
            }
        }
        
        // 获取本位币代码
        String localCurrencyCode = getLocalCurrencyCode(groupid);
        
        // 获取所有需要调汇的科目
        FinAccountingSubjects subjectQuery = new FinAccountingSubjects();
        subjectQuery.setGroupid(groupid);
        subjectQuery.setStatus(1);
        subjectQuery.setIsExchange(true);
        List<FinAccountingSubjects> exchangeSubjects = finAccountingSubjectsService.selectFinAccountingSubjectsListAll(subjectQuery);
        
        // 如果没有配置isExchange的科目，则查询有外币凭证分录的科目
        if (exchangeSubjects == null || exchangeSubjects.isEmpty()) {
            log.info("未找到isExchange=true的科目，尝试查询有外币凭证分录的科目");
            exchangeSubjects = new ArrayList<>();
            List<FinAccountingSubjects> allSubjects = finAccountingSubjectsService.selectFinAccountingSubjectsList(subjectQuery);
            if (allSubjects != null) {
                for (FinAccountingSubjects subject : allSubjects) {
                    if (subject.getForeignCurrencys() != null && !subject.getForeignCurrencys().isEmpty()) {
                        exchangeSubjects.add(subject);
                    }
                }
            }
            log.info("找到{}个有外币配置的科目", exchangeSubjects.size());
        } else {
            log.info("找到{}个isExchange=true的科目", exchangeSubjects.size());
        }
        
        // 构建明细数据
        List<Map<String, Object>> details = new ArrayList<>();
        BigDecimal totalGain = BigDecimal.ZERO;
        BigDecimal totalLoss = BigDecimal.ZERO;
        
        if (exchangeSubjects != null && !exchangeSubjects.isEmpty()) {
            String periodCodeVal = period.getPeriodCode();
            
            for (FinAccountingSubjects subject : exchangeSubjects) {
                log.info("处理科目: {} {}", subject.getSubjectCode(), subject.getSubjectName());
                
                // 获取该科目的总账期末余额
                FinGeneralLedger ledger = finGeneralLedgerService.selectBySubjectAndPeriod(
                        groupid, subject.getSubjectId().toString(), periodCodeVal);
                
                if (ledger == null || ledger.getEndBalance() == null) {
                    log.info("科目 {} 总账记录为空", subject.getSubjectCode());
                    continue;
                }
                
                if (ledger.getEndBalance().compareTo(BigDecimal.ZERO) == 0) {
                    log.info("科目 {} 期末余额为0", subject.getSubjectCode());
                    continue;
                }
                
                BigDecimal endBalance = ledger.getEndBalance();
                log.info("科目 {} 期末余额: {}", subject.getSubjectCode(), endBalance);
                
                // 获取该科目的外币币种
                String foreignCurrencys = subject.getForeignCurrencys();
                if (StrUtil.isBlank(foreignCurrencys)) {
                    log.info("科目 {} 未配置外币币种", subject.getSubjectCode());
                    continue;
                }
                log.info("科目 {} 外币币种配置: {}", subject.getSubjectCode(), foreignCurrencys);
                
                // 解析币种数组，过滤掉本位币
                String[] currencyArray = foreignCurrencys.split("/");
                List<String> validCurrencies = new ArrayList<>();
                for (String currencyCode : currencyArray) {
                    if (StrUtil.isNotBlank(currencyCode) && StrUtil.isNotBlank(currencyCode.trim())) {
                        currencyCode = currencyCode.trim();
                        if (currencyCode.equals(localCurrencyCode)) {
                            continue;
                        }
                        validCurrencies.add(currencyCode);
                    }
                }
                if (validCurrencies.isEmpty()) {
                    continue;
                }
                
                // 收集各币种的外币余额和汇率
                List<Map<String, Object>> currencyDataList = new ArrayList<>();
                BigDecimal totalNewLocalBalance = BigDecimal.ZERO;
                
                for (String currencyCode : validCurrencies) {
                    BigDecimal currentRate = getExchangeRateByPeriod(groupid, currencyCode, customRates, endDateStr);
                    if (currentRate == null || currentRate.compareTo(BigDecimal.ZERO) == 0) {
                        log.info("币种 {} 汇率为0或空，跳过", currencyCode);
                        continue;
                    }
                    
                    BigDecimal foreignBalance = calculateForeignBalance(groupid, subject.getSubjectId().toString(), currencyCode, period);
                    log.info("科目 {} 币种 {} 外币余额: {} 汇率: {}", subject.getSubjectCode(), currencyCode, foreignBalance, currentRate);
                    
                    if (foreignBalance.compareTo(BigDecimal.ZERO) == 0) {
                        log.info("科目 {} 币种 {} 外币余额为0，跳过", subject.getSubjectCode(), currencyCode);
                        continue;
                    }
                    
                    BigDecimal newLocalBalance = foreignBalance.multiply(currentRate).setScale(2, RoundingMode.HALF_UP);
                    totalNewLocalBalance = totalNewLocalBalance.add(newLocalBalance);
                    
                    Map<String, Object> currencyData = new HashMap<>();
                    currencyData.put("currency", currencyCode);
                    currencyData.put("foreignBalance", foreignBalance);
                    currencyData.put("rate", currentRate);
                    currencyData.put("newLocalBalance", newLocalBalance);
                    currencyDataList.add(currencyData);
                }
                
                if (currencyDataList.isEmpty()) {
                    continue;
                }
                
                // 计算总汇兑损益
                BigDecimal totalAdjustment = totalNewLocalBalance.subtract(endBalance);
                if (totalAdjustment.compareTo(BigDecimal.ZERO) == 0) {
                    continue;
                }
                
                // 按各币种的贡献比例分配总调整额
                for (Map<String, Object> currencyData : currencyDataList) {
                    BigDecimal newLocalBalance = (BigDecimal) currencyData.get("newLocalBalance");
                    String currencyCode = (String) currencyData.get("currency");
                    
                    BigDecimal ratio = newLocalBalance.divide(totalNewLocalBalance, 10, RoundingMode.HALF_UP);
                    BigDecimal adjustment = totalAdjustment.multiply(ratio).setScale(2, RoundingMode.HALF_UP);
                    
                    if (adjustment.compareTo(BigDecimal.ZERO) == 0) {
                        continue;
                    }
                    
                    BigDecimal foreignBalance = (BigDecimal) currencyData.get("foreignBalance");
                    BigDecimal rate = (BigDecimal) currencyData.get("rate");
                    
                    Map<String, Object> item = new HashMap<>();
                    item.put("subjectId", subject.getSubjectId().toString());
                    item.put("subjectName", subject.getSubjectCode() + " " + finAccountingSubjectsService.buildFullSubjectName(subject, codeMap));
                    item.put("currency", currencyCode);
                    item.put("foreignBalance", foreignBalance);
                    item.put("exchangeRate", rate);
                    item.put("localAmount", adjustment.abs());
                    item.put("type", adjustment.compareTo(BigDecimal.ZERO) > 0 ? "收益" : "损失");
                    
                    details.add(item);
                    
                    if (adjustment.compareTo(BigDecimal.ZERO) > 0) {
                        totalGain = totalGain.add(adjustment.abs());
                    } else {
                        totalLoss = totalLoss.add(adjustment.abs());
                    }
                }
            }
        }
        
        result.put("details", details);
        result.put("totalGain", totalGain);
        result.put("totalLoss", totalLoss);
        
        return result;
    }
}
