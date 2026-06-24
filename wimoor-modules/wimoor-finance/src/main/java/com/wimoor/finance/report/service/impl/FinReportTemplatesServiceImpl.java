package com.wimoor.finance.report.service.impl;

import com.googlecode.aviator.AviatorEvaluator;
import com.wimoor.finance.ledger.service.IFinGeneralLedgerService;
import com.wimoor.finance.ledger.service.ISubjectBalanceService;
import com.wimoor.finance.report.domain.FinReportItems;
import com.wimoor.finance.report.domain.FinReportMappingRules;
import com.wimoor.finance.report.domain.FinReportTemplates;
import com.wimoor.finance.report.domain.dto.ReportGenerateRequest;
import com.wimoor.finance.report.domain.dto.ReportGenerateResponse;
import com.wimoor.finance.report.domain.dto.ReportItemValueDTO;
import com.wimoor.finance.report.mapper.FinReportMappingRulesMapper;
import com.wimoor.finance.report.mapper.FinReportTemplatesMapper;
import com.wimoor.finance.report.service.IFinReportItemsService;
import com.wimoor.finance.report.service.IFinReportMappingRulesService;
import com.wimoor.finance.report.service.IFinReportTemplatesService;
import com.wimoor.finance.setting.domain.FinAccountingSubjects;
import com.wimoor.finance.setting.mapper.FinAccountingSubjectsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
/**
 * 财务报表模板Service业务层处理
 * 
 * @author wimoor
 * @date 2025-11-04
 */
@Service
public class FinReportTemplatesServiceImpl implements IFinReportTemplatesService 
{
    @Autowired
    private FinReportTemplatesMapper finReportTemplatesMapper;
    @Autowired
    private IFinReportItemsService finReportItemsService;
    @Autowired
    ISubjectBalanceService subjectBalanceService;
    @Autowired
    FinReportCalcCustomAmountService finReportCalcCustomAmountService;
    @Autowired
    IFinGeneralLedgerService finGeneralLedgerService;
    @Autowired
    IFinReportMappingRulesService finReportMappingRulesService;
    @Autowired
    FinAccountingSubjectsMapper finAccountingSubjectsMapper;
    @Autowired
    FinReportMappingRulesMapper finReportMappingRulesMapper;
    // 缓存计算结果
    private final Map<String, Map<String, BigDecimal>> calculationCache = new ConcurrentHashMap<>();

    private static final DateTimeFormatter PERIOD_FORMATTER = DateTimeFormatter.ofPattern("yyyy年MM月");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    /**
     * 查询财务报表模板
     * 
     * @param templateId 财务报表模板主键
     * @return 财务报表模板
     */
    @Override
    public FinReportTemplates selectFinReportTemplatesByTemplateId(Long templateId)
    {
        return finReportTemplatesMapper.selectFinReportTemplatesByTemplateId(templateId);
    }

    /**
     * 查询财务报表模板列表
     * 
     * @param finReportTemplates 财务报表模板
     * @return 财务报表模板
     */
    @Override
    public List<FinReportTemplates> selectFinReportTemplatesList(FinReportTemplates finReportTemplates)
    {
        return finReportTemplatesMapper.selectFinReportTemplatesList(finReportTemplates);
    }

    /**
     * 新增财务报表模板
     * 
     * @param finReportTemplates 财务报表模板
     * @return 结果
     */
    @Override
    public int insertFinReportTemplates(FinReportTemplates finReportTemplates)
    {
        return finReportTemplatesMapper.insertFinReportTemplates(finReportTemplates);
    }

    /**
     * 修改财务报表模板
     * 
     * @param finReportTemplates 财务报表模板
     * @return 结果
     */
    @Override
    public int updateFinReportTemplates(FinReportTemplates finReportTemplates)
    {
        return finReportTemplatesMapper.updateFinReportTemplates(finReportTemplates);
    }

    /**
     * 批量删除财务报表模板
     * 
     * @param templateIds 需要删除的财务报表模板主键
     * @return 结果
     */
    @Override
    public int deleteFinReportTemplatesByTemplateIds(Long[] templateIds)
    {
        return finReportTemplatesMapper.deleteFinReportTemplatesByTemplateIds(templateIds);
    }

    /**
     * 删除财务报表模板信息
     * 
     * @param templateId 财务报表模板主键
     * @return 结果
     */
    @Override
    public int deleteFinReportTemplatesByTemplateId(Long templateId)
    {
        return finReportTemplatesMapper.deleteFinReportTemplatesByTemplateId(templateId);
    }

    @Override
    public ReportGenerateResponse generateReport(ReportGenerateRequest request) {
        ReportGenerateResponse response = new ReportGenerateResponse();

        try {
            String groupid = request.getGroupid();
            String templateCode = request.getTemplateCode();
            String period = request.getPeriod();
            String comparePeriod = request.getComparePeriod();
            Integer amountUnit = request.getAmountUnit();
             Boolean includeComparison = request.getIncludeComparison();
             Boolean includeChartData = request.getIncludeChartData();
             FinReportTemplates finReportTemplates = new FinReportTemplates();
            finReportTemplates.setGroupid(groupid);
            finReportTemplates.setTemplateCode(templateCode);
            // 1. 获取报表模板
            List<FinReportTemplates> templates = this.finReportTemplatesMapper.selectFinReportTemplatesList(finReportTemplates);
            if (templates.isEmpty()) {
                throw new RuntimeException("报表模板不存在");
            }
            FinReportTemplates template = templates.get(0);
            String tplType = template.getTemplateType();


            // 2. 获取报表项目结构
            FinReportItems itemsquery=new FinReportItems();
            itemsquery.setGroupid(groupid);
            itemsquery.setTemplateId(template.getTemplateId());
            List<FinReportItems> reportItems = finReportItemsService.selectFinReportItemsList(itemsquery);

            if (reportItems.isEmpty()) {
                throw new RuntimeException("报表项目未配置");
            }

            // 3. 计算当前期间数据
            Map<String, BigDecimal> currentAmounts = calculateReportAmounts(groupid, period, reportItems, template);

            // 4. 计算比较期间数据（如果需要）
            Map<String, BigDecimal> compareAmounts = new HashMap<>();
            if (request.getIncludeComparison() && comparePeriod != null && !comparePeriod.isEmpty()) {
                compareAmounts = calculateReportAmounts(groupid, comparePeriod, reportItems, template);
            }

            // 5. 计算本月发生额（利润表和现金流量表需要）
            Map<String, BigDecimal> monthAmounts = new HashMap<>();
            if ("INCOME_STATEMENT".equals(tplType) || "CASH_FLOW".equals(tplType)) {
                monthAmounts = calculateReportMonthAmounts(groupid, period, reportItems, template);
            }

            // 6. 构建响应数据
            List<ReportItemValueDTO> itemDTOs = buildReportItems(reportItems, currentAmounts, compareAmounts, monthAmounts, tplType, amountUnit);

            // 6. 计算财务指标（仅资产负债表和利润表）
            Map<String, Object> financialRatios = new HashMap<>();
            if ("BALANCE_SHEET".equals(template.getTemplateType()) || "INCOME_STATEMENT".equals(template.getTemplateType())) {
                financialRatios = calculateFinancialRatios(currentAmounts, template.getTemplateType());
            }

            // 7. 构建图表数据
            Map<String, Object> chartData = new HashMap<>();
//            if (request.getIncludeChartData()) {
//                chartData = buildChartData(currentAmounts, template.getTemplateType());
//            }

            // 8. 构建汇总信息
            Map<String, Object> summary =null;// buildReportSummary(currentAmounts, template.getTemplateType());

            // 9. 验证报表数据（仅警告，不阻断报表生成）
            List<String> warnings = new ArrayList<>();
            if ("BALANCE_SHEET".equals(template.getTemplateType())) {
                try {
                    validateBalanceSheet(currentAmounts);
                } catch (RuntimeException e) {
                    System.out.println("资产负债表验证警告: " + e.getMessage());
                    warnings.add(e.getMessage());
                }
            }

            response.setSuccess(true);
            response.setMessage("报表生成成功");
            response.setTemplateCode(templateCode);
            response.setTemplateName(template.getTemplateName());
            response.setPeriod(period);
            response.setReportDate(getReportDate(period, template.getTemplateType()));
            response.setItems(itemDTOs);
            response.setFinancialRatios(financialRatios);
            response.setChartData(chartData);
            response.setSummary(summary);
            response.setWarnings(warnings);

        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("报表生成失败: " + e.getMessage());
        }

        return response;
    }

    private Map<String, BigDecimal> calculateReportAmounts(String groupid, String period,
                                                           List<FinReportItems> reportItems,
                                                           FinReportTemplates template) {
        String cacheKey = groupid + "_" + template.getTemplateCode() + "_" + period;

        // 检查缓存
//        if (calculationCache.containsKey(cacheKey)) {
//            return calculationCache.get(cacheKey);
//        }


        Map<String, BigDecimal> amounts = subjectBalanceService.getAllSubjectBalance(groupid, period);

        // 第一轮：按层级从高到低排序计算
        List<FinReportItems> sortedItems = reportItems.stream()
                .sorted(Comparator.comparing(FinReportItems::getItemLevel).reversed())
                .collect(Collectors.toList());

        for (FinReportItems item : sortedItems) {
            if (!item.getIsShow() ) {
                continue; // 跳过不显示的项目
            }

            BigDecimal amount = calculateItemAmount(groupid, period, item, amounts, reportItems);
            amounts.put(item.getItemCode(), amount);
        }

        // 第二轮：重新计算 CALCULATED 类型的项目（确保同级子项金额已存在）
        for (FinReportItems item : sortedItems) {
            if (!item.getIsShow()) {
                continue;
            }
            if ("CALCULATED".equals(item.getFormulaType())) {
                BigDecimal amount = calculateAutoAmount(item, amounts, reportItems);
                amounts.put(item.getItemCode(), amount);
            }
        }

        // 第三轮：重新计算 FORMULA 和 CUSTOM 类型的项目（确保引用的项目金额已存在）
        for (FinReportItems item : sortedItems) {
            if (!item.getIsShow()) {
                continue;
            }
            if ("FORMULA".equals(item.getFormulaType()) || "CUSTOM".equals(item.getFormulaType())) {
                BigDecimal amount = calculateItemAmount(groupid, period, item, amounts, reportItems);
                amounts.put(item.getItemCode(), amount);
            }
        }

        // 存入缓存
        calculationCache.put(cacheKey, amounts);

        return amounts;
    }

    /**
     * 计算报表项目的本月发生额
     * 对于利润表，本月金额 = 本期借方发生额（费用类）或 本期贷方发生额（收入类）
     */
    private Map<String, BigDecimal> calculateReportMonthAmounts(String groupid, String period,
                                                                 List<FinReportItems> reportItems,
                                                                 FinReportTemplates template) {
        Map<String, BigDecimal> monthAmounts = new HashMap<>();

        // 初始化所有科目的本月发生额
        Map<String, BigDecimal> allDebits = subjectBalanceService.getAllSubjectDebitTotal(groupid, period);
        Map<String, BigDecimal> allCredits = subjectBalanceService.getAllSubjectCreditTotal(groupid, period);

        // 按层级从高到低排序计算
        List<FinReportItems> sortedItems = reportItems.stream()
                .sorted(Comparator.comparing(FinReportItems::getItemLevel).reversed())
                .collect(Collectors.toList());

        for (FinReportItems item : sortedItems) {
            if (!item.getIsShow()) {
                continue;
            }

            BigDecimal monthAmount = calculateMonthItemAmount(groupid, period, item, monthAmounts, reportItems, allDebits, allCredits);
            monthAmounts.put(item.getItemCode(), monthAmount);
        }

        // 第二轮：重新计算 CALCULATED 类型的项目
        for (FinReportItems item : sortedItems) {
            if (!item.getIsShow()) {
                continue;
            }
            if ("CALCULATED".equals(item.getFormulaType())) {
                BigDecimal monthAmount = calculateAutoAmount(item, monthAmounts, reportItems);
                monthAmounts.put(item.getItemCode(), monthAmount);
            }
        }

        // 第三轮：重新计算 FORMULA 和 CUSTOM 类型的项目
        for (FinReportItems item : sortedItems) {
            if (!item.getIsShow()) {
                continue;
            }
            if ("FORMULA".equals(item.getFormulaType()) || "CUSTOM".equals(item.getFormulaType())) {
                BigDecimal monthAmount = calculateMonthItemAmount(groupid, period, item, monthAmounts, reportItems, allDebits, allCredits);
                monthAmounts.put(item.getItemCode(), monthAmount);
            }
        }

        return monthAmounts;
    }

    /**
     * 计算单个项目的本月发生额
     */
    private BigDecimal calculateMonthItemAmount(String groupid, String period, FinReportItems item,
                                                 Map<String, BigDecimal> monthAmounts, List<FinReportItems> allItems,
                                                 Map<String, BigDecimal> allDebits, Map<String, BigDecimal> allCredits) {
        String formulaType = item.getFormulaType();
        String calculationRule = item.getCalculationRule();
        String dataSource = item.getDataSource();

        try {
            switch (formulaType) {
                case "DIRECT":
                    return calculateMonthDirectAmount(groupid, period, item, dataSource, allDebits, allCredits);

                case "FORMULA":
                    return evaluateFormula(calculationRule, monthAmounts);

                case "CUSTOM":
                    return calculateMonthCustomAmount(groupid, period, calculationRule, monthAmounts, allItems);

                case "CALCULATED":
                    return calculateAutoAmount(item, monthAmounts, allItems);

                default:
                    return BigDecimal.ZERO;
            }
        } catch (Exception e) {
            System.err.println("计算项目本月金额失败: " + item.getItemCode() + " - " + e.getMessage());
            return BigDecimal.ZERO;
        }
    }

    /**
     * 计算DIRECT类型项目的本月发生额
     */
    private BigDecimal calculateMonthDirectAmount(String groupid, String period, FinReportItems item,
                                                   String dataSource, Map<String, BigDecimal> allDebits,
                                                   Map<String, BigDecimal> allCredits) {
        if (dataSource == null || dataSource.isEmpty()) {
            dataSource = "SUBJECT";
        }

        switch (dataSource) {
            case "SUBJECT":
                String codes = item.getSubjectCodes();
                if (codes == null || codes.isEmpty()) {
                    codes = item.getCalculationRule();
                }
                if (codes != null && !codes.isEmpty()) {
                    // 对于借方科目，返回借方发生额；对于贷方科目，返回贷方发生额
                    String accCode = "ACC_" + codes;
                    if (item.getDirection() != null && "2".equals(item.getDirection())) {
                        return allCredits.getOrDefault(accCode, BigDecimal.ZERO);
                    } else {
                        return allDebits.getOrDefault(accCode, BigDecimal.ZERO);
                    }
                }
                break;

            case "CONSTANT":
                if (item.getCalculationRule() != null) {
                    try {
                        return new BigDecimal(item.getCalculationRule());
                    } catch (NumberFormatException e) {
                        return BigDecimal.ZERO;
                    }
                }
                break;
        }

        return BigDecimal.ZERO;
    }

    private BigDecimal calculateItemAmount(String groupid, String period, FinReportItems item,
                                           Map<String, BigDecimal> amounts, List<FinReportItems> allItems) {
        String formulaType = item.getFormulaType();
        String calculationRule = item.getCalculationRule();
        String dataSource = item.getDataSource();

        try {
            switch (formulaType) {
                case "DIRECT":
                    return calculateDirectAmount(groupid, period, item, dataSource);

                case "FORMULA":
                    return calculateFormulaAmount(calculationRule, amounts, item.getFormulaContent());

                case "CUSTOM":
                    return calculateCustomAmount(groupid, period, calculationRule, amounts, allItems);

                case "CALCULATED":
                    return calculateAutoAmount(item, amounts, allItems);

                default:
                    return BigDecimal.ZERO;
            }
        } catch (Exception e) {
            // 记录错误但继续计算其他项目
            System.err.println("计算项目失败: " + item.getItemCode() + " - " + e.getMessage());
            return BigDecimal.ZERO;
        }
    }

    private BigDecimal calculateDirectAmount(String groupid, String period, FinReportItems item, String dataSource) {
        // 如果dataSource为空，根据formulaType推断
        if (dataSource == null || dataSource.isEmpty()) {
            dataSource = "SUBJECT";
        }
        
        switch (dataSource) {
            case "SUBJECT":
                // 从科目余额获取数据
                // 优先使用subjectCodes，如果没有则使用calculationRule
                String codes = item.getSubjectCodes();
                if (codes == null || codes.isEmpty()) {
                    codes = item.getCalculationRule();
                }
                if (codes != null && !codes.isEmpty()) {
                    return getSubjectBalance(groupid, period, codes, item.getAmountType());
                }
                break;

            case "CONSTANT":
                // 常量值
                if (item.getCalculationRule() != null) {
                    try {
                        return new BigDecimal(item.getCalculationRule());
                    } catch (NumberFormatException e) {
                        return BigDecimal.ZERO;
                    }
                }
                break;

            case "CUSTOM":
                // 自定义数据源
                return getCustomAmount(groupid, period, item.getCalculationRule());
        }

        return BigDecimal.ZERO;
    }

    private BigDecimal calculateFormulaAmount(String calculationRule, Map<String, BigDecimal> amounts, String formulaContent) {
        if (formulaContent != null && !formulaContent.isEmpty()) {
            // 使用公式内容
            return evaluateFormula(formulaContent, amounts);
        } else if (calculationRule != null && !calculationRule.isEmpty()) {
            // 使用计算规则
            return evaluateFormula(calculationRule, amounts);
        }

        return BigDecimal.ZERO;
    }

    private BigDecimal calculateCustomAmount(String groupid, String period, String customRule,
                                             Map<String, BigDecimal> amounts, List<FinReportItems> allItems) {
        // customRule 格式: "CUSTOM:ITEM_CODE"，提取 itemCode
        String itemCode = customRule;
        if (customRule.startsWith("CUSTOM:")) {
            itemCode = customRule.substring(7);
        }

        // 从数据库查询该报表项目的映射规则
        List<FinReportMappingRules> rules = finReportMappingRulesService.selectRulesByItemCode(groupid, itemCode);

        if (rules.isEmpty()) {
            // 如果没有配置规则，尝试使用子项汇总
            return sumChildrenAmounts(itemCode, allItems, amounts);
        }

        BigDecimal total = BigDecimal.ZERO;

        for (FinReportMappingRules rule : rules) {
            if (rule.getStatus() != 1) {
                continue; // 跳过停用的规则
            }

            String matchType = rule.getMatchType();
            String matchValue = rule.getMatchValue();
            String operator = rule.getOperator();

            BigDecimal ruleAmount = BigDecimal.ZERO;

            if ("ITEM".equals(matchType)) {
                // 汇总其他报表项目
                ruleAmount = amounts.getOrDefault(matchValue, BigDecimal.ZERO);
            } else if ("SUBJECT".equals(matchType) || "PREFIX".equals(matchType)) {
                // 科目前缀匹配
                ruleAmount = calculateSubjectBalanceByPrefix(groupid, period, matchValue, rule);
            }

            // 根据操作符累加或累减
            if ("ADD".equals(operator)) {
                total = total.add(ruleAmount);
            } else if ("SUBTRACT".equals(operator)) {
                total = total.subtract(ruleAmount);
            }
        }

        return total;
    }

    /**
     * 计算CUSTOM类型项目的本月发生额
     */
    private BigDecimal calculateMonthCustomAmount(String groupid, String period, String customRule,
                                                   Map<String, BigDecimal> monthAmounts, List<FinReportItems> allItems) {
        String itemCode = customRule;
        if (customRule.startsWith("CUSTOM:")) {
            itemCode = customRule.substring(7);
        }

        List<FinReportMappingRules> rules = finReportMappingRulesService.selectRulesByItemCode(groupid, itemCode);

        if (rules.isEmpty()) {
            return sumChildrenAmounts(itemCode, allItems, monthAmounts);
        }

        BigDecimal total = BigDecimal.ZERO;

        for (FinReportMappingRules rule : rules) {
            if (rule.getStatus() != 1) {
                continue;
            }

            String matchType = rule.getMatchType();
            String matchValue = rule.getMatchValue();
            String operator = rule.getOperator();

            BigDecimal ruleAmount = BigDecimal.ZERO;

            if ("ITEM".equals(matchType)) {
                ruleAmount = monthAmounts.getOrDefault(matchValue, BigDecimal.ZERO);
            } else if ("SUBJECT".equals(matchType) || "PREFIX".equals(matchType)) {
                ruleAmount = calculateMonthSubjectBalanceByPrefix(groupid, period, matchValue, rule);
            }

            if ("ADD".equals(operator)) {
                total = total.add(ruleAmount);
            } else if ("SUBTRACT".equals(operator)) {
                total = total.subtract(ruleAmount);
            }
        }

        return total;
    }

    /**
     * 根据科目前缀计算余额
     */
    private BigDecimal calculateSubjectBalanceByPrefix(String groupid, String period, String prefix, FinReportMappingRules rule) {
        BigDecimal total = BigDecimal.ZERO;
        Map<String, BigDecimal> allBalances = subjectBalanceService.getAllSubjectBalance(groupid, period);

        for (Map.Entry<String, BigDecimal> entry : allBalances.entrySet()) {
            String subjectCode = entry.getKey();
            // 去掉前缀 "ACC_"
            String code = subjectCode.startsWith("ACC_") ? subjectCode.substring(4) : subjectCode;

            if (code.startsWith(prefix)) {
                BigDecimal balance = entry.getValue();
                if (balance != null) {
                    // getAllSubjectBalance 返回: 借方余额为正数，贷方余额为负数
                    // 对于贷方科目(负债/权益): 取反使其变为正数表示
                    // 对于借方科目(资产): 保持原值
                    if (rule.getDirection() != null && rule.getDirection() == 2) {
                        balance = balance.negate();
                    }
                    total = total.add(balance);
                }
            }
        }

        return total;
    }

    /**
     * 根据科目前缀计算本月发生额
     * 对于借方科目(direction=1)，返回借方发生额
     * 对于贷方科目(direction=2)，返回贷方发生额
     */
    private BigDecimal calculateMonthSubjectBalanceByPrefix(String groupid, String period, String prefix, FinReportMappingRules rule) {
        BigDecimal total = BigDecimal.ZERO;
        Map<String, BigDecimal> allDebits = subjectBalanceService.getAllSubjectDebitTotal(groupid, period);
        Map<String, BigDecimal> allCredits = subjectBalanceService.getAllSubjectCreditTotal(groupid, period);

        for (Map.Entry<String, BigDecimal> entry : allDebits.entrySet()) {
            String subjectCode = entry.getKey();
            String code = subjectCode.startsWith("ACC_") ? subjectCode.substring(4) : subjectCode;

            if (code.startsWith(prefix)) {
                BigDecimal monthAmount;
                // 对于贷方科目(direction=2)，使用贷方发生额
                // 对于借方科目(direction=1)，使用借方发生额
                if (rule.getDirection() != null && rule.getDirection() == 2) {
                    monthAmount = allCredits.getOrDefault(subjectCode, BigDecimal.ZERO);
                } else {
                    monthAmount = allDebits.getOrDefault(subjectCode, BigDecimal.ZERO);
                }
                total = total.add(monthAmount);
            }
        }

        return total;
    }

    private BigDecimal calculateAutoAmount(FinReportItems item, Map<String, BigDecimal> amounts, List<FinReportItems> allItems) {
        if ("SUM(CHILDREN)".equals(item.getCalculationRule())) {
            // 检查是否有子项目
            boolean hasChildren = allItems.stream()
                    .anyMatch(child -> item.getItemCode().equals(child.getParentCode()) && child.getIsShow());
            if (hasChildren) {
                return sumChildrenAmounts(item.getItemCode(), allItems, amounts);
            } else {
                // 没有子项目时，找同级别的前一个有子项目的兄弟项目（如"资产总计"找"资产"）
                return findNearestPrecedingAmount(item, allItems, amounts);
            }
        }
        // 其他自动计算规则...

        return BigDecimal.ZERO;
    }

    private BigDecimal findNearestPrecedingAmount(FinReportItems item, List<FinReportItems> allItems, Map<String, BigDecimal> amounts) {
        // 先在同级别找有子项的兄弟项目
        FinReportItems bestMatch = findNearestPrecedingWithChildrenAtLevel(item, allItems, item.getItemLevel());
        if (bestMatch != null) {
            return amounts.getOrDefault(bestMatch.getItemCode(), BigDecimal.ZERO);
        }
        // 同级别没找到，向上一级找（如"流动资产合计"level3找"流动资产"level2）
        if (item.getItemLevel() > 1) {
            bestMatch = findNearestPrecedingWithChildrenAtLevel(item, allItems, item.getItemLevel() - 1);
            if (bestMatch != null) {
                return amounts.getOrDefault(bestMatch.getItemCode(), BigDecimal.ZERO);
            }
        }
        return BigDecimal.ZERO;
    }

    private FinReportItems findNearestPrecedingWithChildrenAtLevel(FinReportItems item, List<FinReportItems> allItems, int targetLevel) {
        FinReportItems bestMatch = null;
        for (FinReportItems other : allItems) {
            if (!other.getIsShow()) continue;
            if (other.getItemLevel() != targetLevel) continue;
            if (other.getLineNumber() == null || item.getLineNumber() == null) continue;
            if (other.getLineNumber() >= item.getLineNumber()) continue;

            boolean hasChildren = allItems.stream()
                    .anyMatch(child -> other.getItemCode().equals(child.getParentCode()) && child.getIsShow());

            if (hasChildren) {
                if (bestMatch == null || other.getLineNumber() > bestMatch.getLineNumber()) {
                    bestMatch = other;
                }
            }
        }
        return bestMatch;
    }

    private BigDecimal sumChildrenAmounts(String parentCode, List<FinReportItems> allItems, Map<String, BigDecimal> amounts) {
        return allItems.stream()
                .filter(item -> parentCode.equals(item.getParentCode()) && item.getIsShow())
                .map(item -> amounts.getOrDefault(item.getItemCode(), BigDecimal.ZERO))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal evaluateFormula(String formula, Map<String, BigDecimal> amounts) {
        Map<String, Object> env = null;
        try {
            // 预处理公式
            String processedFormula = processAccountFormula(formula);

            // 提取公式中的所有变量名（ACC_前缀的），如果 amounts 中缺失则补 0
            java.util.regex.Matcher varMatcher = java.util.regex.Pattern.compile("ACC_\\d+").matcher(processedFormula);
            while (varMatcher.find()) {
                String varName = varMatcher.group();
                if (!amounts.containsKey(varName)) {
                    amounts.put(varName, BigDecimal.ZERO);
                }
            }

            // 提取公式中引用的项目编码变量（非ACC_前缀的字母标识符），如果 amounts 中缺失则补 0
            // 这处理 FORMULA 类型中直接引用其他项目编码的情况，如 "PROFIT_NET"
            java.util.regex.Matcher itemMatcher = java.util.regex.Pattern.compile("[A-Z][A-Z_0-9]+").matcher(processedFormula);
            while (itemMatcher.find()) {
                String varName = itemMatcher.group();
                if (!varName.startsWith("ACC_") && !amounts.containsKey(varName)) {
                    amounts.put(varName, BigDecimal.ZERO);
                }
            }

            // 准备环境变量
            env = prepareEnvironment(amounts);

            // 执行计算
            Object result = AviatorEvaluator.execute(processedFormula, env);
            return convertToBigDecimal(result);

        } catch (Exception e) {
            // 公式计算失败时返回0，避免中断整个报表生成
            System.err.println("公式计算错误: " + formula + " - " + e.getMessage());
            return BigDecimal.ZERO;
        }
    }

    private String processAccountFormula(String formula) {
        // 清理空格
        String cleaned = formula.replaceAll("\\s+", "");

        // 使用正则匹配科目代码（4位数字），并添加前缀
        Pattern pattern = Pattern.compile("\\d{4,}"); // 匹配4位及以上数字
        Matcher matcher = pattern.matcher(cleaned);
        StringBuffer sb = new StringBuffer();

        while (matcher.find()) {
            String accountCode = matcher.group();
            matcher.appendReplacement(sb, "ACC_" + accountCode);
        }
        matcher.appendTail(sb);

        return sb.toString();
    }

    private Map<String, Object> prepareEnvironment(Map<String, BigDecimal> amounts) {
        Map<String, Object> env = new HashMap<>();
        env.putAll(amounts);
        return env;
    }

    private BigDecimal convertToBigDecimal(Object value) {
        if (value instanceof BigDecimal) {
            return (BigDecimal) value;
        } else if (value instanceof Number) {
            return new BigDecimal(value.toString());
        } else {
            throw new RuntimeException("无法转换的类型: " + value.getClass());
        }
    }

    private BigDecimal parseNumber(String str) {
        try {
            return new BigDecimal(str);
        } catch (NumberFormatException e) {
            // 尝试处理括号表达式
            if (str.startsWith("(") && str.endsWith(")")) {
                return parseNumber(str.substring(1, str.length() - 1));
            }
            return BigDecimal.ZERO;
        }
    }

    private BigDecimal getSubjectBalance(String groupid, String period, String subjectCodes, String amountType) {
        // 这里调用科目余额服务获取数据
        // 简化实现，返回模拟数据
        return subjectBalanceService.getSubjectBalance(groupid, period, subjectCodes, amountType);
    }

    private BigDecimal getCustomAmount(String groupid, String period, String customRule) {
        return finReportCalcCustomAmountService.getCustomAmount(groupid, period, customRule);
    }

    private List<ReportItemValueDTO> buildReportItems(List<FinReportItems> reportItems,
                                                      Map<String, BigDecimal> currentAmounts,
                                                      Map<String, BigDecimal> compareAmounts,
                                                      Map<String, BigDecimal> monthAmounts,
                                                      String templateType,
                                                      Integer amountUnit) {
        List<ReportItemValueDTO> result = new ArrayList<>();
        BigDecimal unitDivisor = new BigDecimal(amountUnit);
        // 按lineNumber排序，null值排到最后
        reportItems.sort(Comparator.comparing(FinReportItems::getLineNumber,
                Comparator.nullsLast(Comparator.naturalOrder())));
        for (FinReportItems item : reportItems) {
            if (!item.getIsShow()) {
                continue;
            }

            ReportItemValueDTO dto = new ReportItemValueDTO();
            dto.setItemCode(item.getItemCode());
            dto.setItemName(item.getItemName());
            dto.setItemLevel(item.getItemLevel());
            dto.setLineNumber(item.getLineNumber());
            dto.setItemType(item.getItemType());
            dto.setDisplayFormat(item.getDisplayFormat());
            dto.setIsLeaf(item.getIsLeaf() );

            // 当前期间金额（期末余额）
            BigDecimal currentAmount = currentAmounts.getOrDefault(item.getItemCode(), BigDecimal.ZERO);
            dto.setAmount(currentAmount.divide(unitDivisor, 2, RoundingMode.HALF_UP));

            // 年初余额
            if (!compareAmounts.isEmpty()) {
                BigDecimal yearBeginAmount = compareAmounts.getOrDefault(item.getItemCode(), BigDecimal.ZERO);
                dto.setYearBeginAmount(yearBeginAmount.divide(unitDivisor, 2, RoundingMode.HALF_UP));
            }

            // 利润表和现金流量表：设置本年累计金额和本月金额
            if ("INCOME_STATEMENT".equals(templateType) || "CASH_FLOW".equals(templateType)) {
                // 本年累计金额 = 当前期间的endBalance（即年初至今的累计）
                dto.setYearAmount(currentAmount.divide(unitDivisor, 2, RoundingMode.HALF_UP));
                // 本月金额 = 本期发生额
                BigDecimal monthAmount = monthAmounts.getOrDefault(item.getItemCode(), BigDecimal.ZERO);
                dto.setMonthAmount(monthAmount.divide(unitDivisor, 2, RoundingMode.HALF_UP));
            }

            result.add(dto);
        }

        return result;
    }

    private void validateBalanceSheet(Map<String, BigDecimal> amounts) {
        // 兼容新旧结构：优先取 ASSET，其次 ASSET_TOTAL
        BigDecimal assetTotal = amounts.getOrDefault("ASSET", BigDecimal.ZERO);
        if (assetTotal.compareTo(BigDecimal.ZERO) == 0) {
            assetTotal = amounts.getOrDefault("ASSET_TOTAL", BigDecimal.ZERO);
        }
        BigDecimal liabilityEquityTotal = amounts.getOrDefault("LIABILITY_EQUITY_TOTAL", BigDecimal.ZERO);

        BigDecimal difference = assetTotal.subtract(liabilityEquityTotal).abs();
        if (difference.compareTo(new BigDecimal("0.01")) > 0) {
            throw new RuntimeException(String.format(
                    "资产负债表不平衡! 资产总计: %s, 负债和权益总计: %s, 差额: %s",
                    assetTotal, liabilityEquityTotal, difference
            ));
        }
    }

    private Map<String, Object> calculateFinancialRatios(Map<String, BigDecimal> amounts, String templateType) {
        Map<String, Object> ratios = new HashMap<>();

        if ("BALANCE_SHEET".equals(templateType)) {
            // 资产负债率
            BigDecimal totalAssets = amounts.getOrDefault("ASSET_TOTAL", BigDecimal.ZERO);
            BigDecimal totalLiabilities = amounts.getOrDefault("LIABILITY_TOTAL", BigDecimal.ZERO);
            if (totalAssets.compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal debtRatio = totalLiabilities.divide(totalAssets, 4, RoundingMode.HALF_UP)
                        .multiply(new BigDecimal(100));
                ratios.put("debtRatio", debtRatio);
                ratios.put("debtRatioLevel", getRatioLevel(debtRatio, new BigDecimal("60"), new BigDecimal("80")));
            }

            // 流动比率
            BigDecimal currentAssets = amounts.getOrDefault("ASSET_CURRENT_TOTAL", BigDecimal.ZERO);
            BigDecimal currentLiabilities = amounts.getOrDefault("LIABILITY_CURRENT_TOTAL", BigDecimal.ZERO);
            if (currentLiabilities.compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal currentRatio = currentAssets.divide(currentLiabilities, 2, RoundingMode.HALF_UP);
                ratios.put("currentRatio", currentRatio);
                ratios.put("currentRatioLevel", getRatioLevel(currentRatio, new BigDecimal("1.2"), new BigDecimal("2.0")));
            }
        } else if ("INCOME_STATEMENT".equals(templateType)) {
            // 销售净利率
            BigDecimal netProfit = amounts.getOrDefault("PROFIT_NET", BigDecimal.ZERO);
            BigDecimal operatingIncome = amounts.getOrDefault("INCOME_TOTAL", BigDecimal.ZERO);
            if (operatingIncome.compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal netProfitMargin = netProfit.divide(operatingIncome, 4, RoundingMode.HALF_UP)
                        .multiply(new BigDecimal(100));
                ratios.put("netProfitMargin", netProfitMargin);
            }
        }

        return ratios;
    }

    private String getRatioLevel(BigDecimal ratio, BigDecimal warning, BigDecimal good) {
        if (ratio.compareTo(good) >= 0) {
            return "良好";
        } else if (ratio.compareTo(warning) >= 0) {
            return "一般";
        } else {
            return "警戒";
        }
    }

    private Map<String, Object> buildChartData(Map<String, BigDecimal> amounts, String templateType) {
        Map<String, Object> chartData = new HashMap<>();

        if ("BALANCE_SHEET".equals(templateType)) {
            // 资产结构图数据
            Map<String, BigDecimal> assetStructure = new HashMap<>();
            assetStructure.put("货币资金", amounts.getOrDefault("ASSET_CASH", BigDecimal.ZERO));
            assetStructure.put("应收账款", amounts.getOrDefault("ASSET_RECEIVABLE", BigDecimal.ZERO));
            assetStructure.put("存货", amounts.getOrDefault("ASSET_INVENTORY", BigDecimal.ZERO));
            assetStructure.put("固定资产", amounts.getOrDefault("ASSET_FIXED", BigDecimal.ZERO));
            assetStructure.put("其他资产", amounts.getOrDefault("ASSET_NON_CURRENT_OTHER", BigDecimal.ZERO));
            chartData.put("assetStructure", assetStructure);
        }

        return chartData;
    }

    private Map<String, Object> buildReportSummary(Map<String, BigDecimal> amounts, String templateType) {
        Map<String, Object> summary = new HashMap<>();
        summary.put("generateTime", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        summary.put("itemCount", amounts.size());

        if ("BALANCE_SHEET".equals(templateType)) {
            summary.put("totalAssets", amounts.getOrDefault("ASSET_TOTAL", BigDecimal.ZERO));
            summary.put("totalLiabilities", amounts.getOrDefault("LIABILITY_TOTAL", BigDecimal.ZERO));
            summary.put("totalEquity", amounts.getOrDefault("EQUITY_TOTAL", BigDecimal.ZERO));
        } else if ("INCOME_STATEMENT".equals(templateType)) {
            summary.put("netProfit", amounts.getOrDefault("PROFIT_NET", BigDecimal.ZERO));
            summary.put("operatingIncome", amounts.getOrDefault("INCOME_TOTAL", BigDecimal.ZERO));
        }

        return summary;
    }

    private String getReportDate(String period, String templateType) {
        if ("BALANCE_SHEET".equals(templateType)) {
            return period.substring(0, 4) + "年" + period.substring(4) + "月31日";
        } else if ("INCOME_STATEMENT".equals(templateType)) {
            return period.substring(0, 4) + "年" + period.substring(4) + "月";
        } else {
            return period.substring(0, 4) + "年度";
        }
    }

    @Override
    public Map<String, Object> validateReportData(String groupid, String templateCode, String period) {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, String>> errors = new ArrayList<>();
        List<Map<String, String>> warnings = new ArrayList<>();
        List<Map<String, String>> suggestions = new ArrayList<>();

        try {
            // 1. 验证模板是否存在
            FinReportTemplates query = new FinReportTemplates();
            query.setGroupid(groupid);
            query.setTemplateCode(templateCode);
            query.setStatus(1);
            List<FinReportTemplates> templates = this.finReportTemplatesMapper.selectFinReportTemplatesList(query);
            if (templates == null || templates.isEmpty()) {
                Map<String, String> error = new HashMap<>();
                error.put("type", "TEMPLATE_NOT_FOUND");
                error.put("message", "模板不存在或已禁用");
                error.put("suggestion", "请检查模板编码是否正确，或联系管理员启用该模板");
                error.put("helpUrl", "/help/template-setup");
                errors.add(error);
                result.put("valid", false);
                result.put("errors", errors);
                result.put("warnings", warnings);
                result.put("suggestions", suggestions);
                return result;
            }

            FinReportTemplates template = templates.get(0);
            String tplType = template.getTemplateType();

            // 2. 获取报表项目
            FinReportItems itemquery = new FinReportItems();
            itemquery.setGroupid(groupid);
            itemquery.setTemplateId(template.getTemplateId());
            itemquery.setStatus(1);
            List<FinReportItems> items = this.finReportItemsService.selectFinReportItemsList(itemquery);

            if (items == null || items.isEmpty()) {
                Map<String, String> error = new HashMap<>();
                error.put("type", "NO_ITEMS");
                error.put("message", "模板中没有配置报表项目");
                error.put("suggestion", "请点击[初始化模板]按钮创建默认项目，或手动添加报表项目");
                error.put("helpUrl", "/help/template-items");
                errors.add(error);
                result.put("valid", false);
                result.put("errors", errors);
                result.put("warnings", warnings);
                result.put("suggestions", suggestions);
                return result;
            }

            // 3. 验证会计科目是否存在
            FinAccountingSubjects subjectQuery = new FinAccountingSubjects();
            subjectQuery.setGroupid(groupid);
            subjectQuery.setStatus(1);
            List<FinAccountingSubjects> subjects = this.finAccountingSubjectsMapper.selectFinAccountingSubjectsList(subjectQuery);
            if (subjects == null || subjects.isEmpty()) {
                Map<String, String> warning = new HashMap<>();
                warning.put("type", "NO_SUBJECTS");
                warning.put("message", "未找到启用的会计科目");
                warning.put("suggestion", "请先在[会计科目]模块中添加或启用会计科目，否则报表数据可能不完整");
                warnings.add(warning);
            }

            // 4. 验证映射规则是否存在
            List<FinReportMappingRules> rules = this.finReportMappingRulesMapper.selectRulesByTemplateType(groupid, tplType);
            if (rules == null || rules.isEmpty()) {
                Map<String, String> warning = new HashMap<>();
                warning.put("type", "NO_MAPPING_RULES");
                warning.put("message", "未找到该报表类型的映射规则");
                warning.put("suggestion", "请点击[同步公式]按钮自动生成映射规则，或手动配置映射规则");
                warnings.add(warning);
            }

            // 5. 计算报表金额
            Map<String, BigDecimal> amounts = calculateReportAmounts(groupid, period, items, template);

            // 6. 根据报表类型进行特定验证
            switch (tplType) {
                case "BALANCE_SHEET":
                    validateBalanceSheetDetailed(amounts, errors, warnings, suggestions);
                    break;
                case "INCOME_STATEMENT":
                    validateIncomeStatement(amounts, errors, warnings, suggestions);
                    break;
                case "CASH_FLOW":
                    validateCashFlowStatement(amounts, errors, warnings, suggestions);
                    break;
                case "EQUITY_CHANGE":
                    validateEquityChangeStatement(amounts, errors, warnings, suggestions);
                    break;
            }

            // 7. 检查零值项目
            for (FinReportItems item : items) {
                if (item.getIsShow() && item.getIsShowZero()) {
                    BigDecimal amount = amounts.get(item.getItemCode());
                    if (amount != null && amount.compareTo(BigDecimal.ZERO) == 0) {
                        Map<String, String> warning = new HashMap<>();
                        warning.put("type", "ZERO_AMOUNT");
                        warning.put("itemCode", item.getItemCode());
                        warning.put("itemName", item.getItemName());
                        warning.put("message", "项目 \"" + item.getItemName() + "\" 金额为零");
                        warning.put("suggestion", "请检查该项目是否有对应的业务数据，或确认是否需要显示该项目");
                        warnings.add(warning);
                    }
                }
            }

            // 8. 检查异常大额变动（与上期对比，如果变动超过50%则提示）
            // 这里可以添加更复杂的逻辑

            // 9. 检查损益结转情况（仅资产负债表）
            if ("BALANCE_SHEET".equals(tplType)) {
                Map<String, Object> profitLossCheck = checkProfitLossTransfer(groupid, period);
                result.put("profitLossCheck", profitLossCheck);
            }

            // 10. 检查未设置报表项目的科目
            List<Map<String, Object>> unmappedSubjects = checkUnmappedSubjects(groupid, period, tplType, rules, subjects);
            if (!unmappedSubjects.isEmpty()) {
                result.put("unmappedSubjects", unmappedSubjects);
            }

            // 设置验证结果
            if (errors.isEmpty()) {
                result.put("valid", true);
                result.put("message", "数据验证通过");
            } else {
                result.put("valid", false);
                result.put("message", "数据验证失败，发现 " + errors.size() + " 个错误");
            }

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("type", "SYSTEM_ERROR");
            error.put("message", "系统错误: " + e.getMessage());
            error.put("suggestion", "请联系技术支持或稍后重试");
            errors.add(error);
            result.put("valid", false);
        }

        result.put("errors", errors);
        result.put("warnings", warnings);
        result.put("suggestions", suggestions);
        result.put("period", period);
        result.put("templateCode", templateCode);

        return result;
    }

    /**
     * 详细的资产负债表验证
     */
    private void validateBalanceSheetDetailed(Map<String, BigDecimal> amounts,
                                               List<Map<String, String>> errors,
                                               List<Map<String, String>> warnings,
                                               List<Map<String, String>> suggestions) {
        // 兼容新旧结构：优先取 ASSET，其次 ASSET_TOTAL
        BigDecimal assetTotal = amounts.getOrDefault("ASSET", BigDecimal.ZERO);
        if (assetTotal.compareTo(BigDecimal.ZERO) == 0) {
            assetTotal = amounts.getOrDefault("ASSET_TOTAL", BigDecimal.ZERO);
        }
        // 兼容新旧结构：优先取 LIABILITY，其次 LIABILITY_TOTAL
        BigDecimal liabilityTotal = amounts.getOrDefault("LIABILITY", BigDecimal.ZERO);
        if (liabilityTotal.compareTo(BigDecimal.ZERO) == 0) {
            liabilityTotal = amounts.getOrDefault("LIABILITY_TOTAL", BigDecimal.ZERO);
        }
        BigDecimal equityTotal = amounts.getOrDefault("EQUITY_TOTAL", BigDecimal.ZERO);
        BigDecimal liabilityEquityTotal = amounts.getOrDefault("LIABILITY_EQUITY_TOTAL", BigDecimal.ZERO);

        // 验证资产 = 负债 + 所有者权益
        BigDecimal difference = assetTotal.subtract(liabilityEquityTotal).abs();
        if (difference.compareTo(new BigDecimal("0.01")) > 0) {
            Map<String, String> error = new HashMap<>();
            error.put("type", "BALANCE_MISMATCH");
            error.put("message", String.format("资产负债表不平衡！资产总计: %s, 负债和权益总计: %s, 差额: %s",
                    formatAmount(assetTotal), formatAmount(liabilityEquityTotal), formatAmount(difference)));
            error.put("suggestion", "请检查以下可能的原因：\n" +
                    "1. 检查是否所有会计科目都已正确配置映射规则\n" +
                    "2. 检查是否存在未录入的业务数据\n" +
                    "3. 检查是否有重复录入或遗漏的凭证\n" +
                    "4. 点击[同步公式]按钮重新生成计算公式");
            errors.add(error);

            // 提供具体的调整建议
            if (assetTotal.compareTo(liabilityEquityTotal) > 0) {
                Map<String, String> suggestion = new HashMap<>();
                suggestion.put("type", "ADJUSTMENT");
                suggestion.put("message", "资产总计大于负债和权益总计");
                suggestion.put("action", "请检查是否有未记录的负债或所有者权益项目");
                suggestions.add(suggestion);
            } else {
                Map<String, String> suggestion = new HashMap<>();
                suggestion.put("type", "ADJUSTMENT");
                suggestion.put("message", "负债和权益总计大于资产总计");
                suggestion.put("action", "请检查是否有未记录的资产项目或重复记录的负债");
                suggestions.add(suggestion);
            }
        }

        // 验证资产分类合计
        BigDecimal currentAssets = amounts.getOrDefault("ASSET_CURRENT_TOTAL", BigDecimal.ZERO);
        BigDecimal nonCurrentAssets = amounts.getOrDefault("ASSET_NON_CURRENT_TOTAL", BigDecimal.ZERO);
        if (assetTotal.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal classifiedTotal = currentAssets.add(nonCurrentAssets);
            BigDecimal unclassifiedDiff = assetTotal.subtract(classifiedTotal).abs();
            if (unclassifiedDiff.compareTo(new BigDecimal("0.01")) > 0) {
                Map<String, String> warning = new HashMap<>();
                warning.put("type", "CLASSIFICATION_MISMATCH");
                warning.put("message", String.format("资产分类合计与总资产不一致。流动资产: %s, 非流动资产: %s, 分类合计: %s, 总资产: %s",
                        formatAmount(currentAssets), formatAmount(nonCurrentAssets), formatAmount(classifiedTotal), formatAmount(assetTotal)));
                warning.put("suggestion", "请检查资产项目的分类是否正确");
                warnings.add(warning);
            }
        }

        // 验证负债分类合计
        BigDecimal currentLiabilities = amounts.getOrDefault("LIABILITY_CURRENT_TOTAL", BigDecimal.ZERO);
        BigDecimal nonCurrentLiabilities = amounts.getOrDefault("LIABILITY_NON_CURRENT_TOTAL", BigDecimal.ZERO);
        if (liabilityTotal.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal classifiedTotal = currentLiabilities.add(nonCurrentLiabilities);
            BigDecimal unclassifiedDiff = liabilityTotal.subtract(classifiedTotal).abs();
            if (unclassifiedDiff.compareTo(new BigDecimal("0.01")) > 0) {
                Map<String, String> warning = new HashMap<>();
                warning.put("type", "CLASSIFICATION_MISMATCH");
                warning.put("message", String.format("负债分类合计与总负债不一致。流动负债: %s, 非流动负债: %s, 分类合计: %s, 总负债: %s",
                        formatAmount(currentLiabilities), formatAmount(nonCurrentLiabilities), formatAmount(classifiedTotal), formatAmount(liabilityTotal)));
                warning.put("suggestion", "请检查负债项目的分类是否正确");
                warnings.add(warning);
            }
        }

        // 检查货币资金
        BigDecimal cash = amounts.getOrDefault("ASSET_CASH", BigDecimal.ZERO);
        if (cash.compareTo(BigDecimal.ZERO) < 0) {
            Map<String, String> warning = new HashMap<>();
            warning.put("type", "NEGATIVE_CASH");
            warning.put("message", "货币资金为负数: " + formatAmount(cash));
            warning.put("suggestion", "货币资金不应为负数，请检查现金和银行存款科目的余额");
            warnings.add(warning);
        }

        // 检查应收账款
        BigDecimal receivable = amounts.getOrDefault("ASSET_RECEIVABLE", BigDecimal.ZERO);
        BigDecimal badDebtProvision = amounts.getOrDefault("ASSET_BAD_DEBT", BigDecimal.ZERO);
        if (receivable.compareTo(BigDecimal.ZERO) > 0 && badDebtProvision.compareTo(BigDecimal.ZERO) == 0) {
            Map<String, String> suggestion = new HashMap<>();
            suggestion.put("type", "BAD_DEBT_CHECK");
            suggestion.put("message", "应收账款余额为 " + formatAmount(receivable) + "，但未计提坏账准备");
            suggestion.put("action", "请根据公司的坏账政策计提相应的坏账准备");
            suggestions.add(suggestion);
        }
    }

    /**
     * 利润表验证
     */
    private void validateIncomeStatement(Map<String, BigDecimal> amounts,
                                          List<Map<String, String>> errors,
                                          List<Map<String, String>> warnings,
                                          List<Map<String, String>> suggestions) {
        BigDecimal revenue = amounts.getOrDefault("REVENUE_MAIN", BigDecimal.ZERO);
        BigDecimal cost = amounts.getOrDefault("COST_MAIN", BigDecimal.ZERO);
        BigDecimal grossProfit = amounts.getOrDefault("GROSS_PROFIT", BigDecimal.ZERO);
        BigDecimal netProfit = amounts.getOrDefault("PROFIT_NET", BigDecimal.ZERO);

        // 验证毛利计算
        BigDecimal calculatedGrossProfit = revenue.subtract(cost);
        if (grossProfit.subtract(calculatedGrossProfit).abs().compareTo(new BigDecimal("0.01")) > 0) {
            Map<String, String> warning = new HashMap<>();
            warning.put("type", "GROSS_PROFIT_MISMATCH");
            warning.put("message", String.format("毛利计算不一致。营业收入: %s, 营业成本: %s, 计算毛利: %s, 报表毛利: %s",
                    formatAmount(revenue), formatAmount(cost), formatAmount(calculatedGrossProfit), formatAmount(grossProfit)));
            warning.put("suggestion", "请检查收入和成本的确认是否正确");
            warnings.add(warning);
        }

        // 检查毛利率
        if (revenue.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal grossMargin = grossProfit.divide(revenue, 4, RoundingMode.HALF_UP).multiply(new BigDecimal(100));
            if (grossMargin.compareTo(new BigDecimal("-10")) < 0) {
                Map<String, String> warning = new HashMap<>();
                warning.put("type", "LOW_GROSS_MARGIN");
                warning.put("message", "毛利率异常低: " + formatAmount(grossMargin) + "%");
                warning.put("suggestion", "请检查收入和成本数据是否正确，或业务是否出现严重问题");
                warnings.add(warning);
            } else if (grossMargin.compareTo(new BigDecimal("100")) > 0) {
                Map<String, String> warning = new HashMap<>();
                warning.put("type", "HIGH_GROSS_MARGIN");
                warning.put("message", "毛利率异常高: " + formatAmount(grossMargin) + "%");
                warning.put("suggestion", "请检查收入和成本数据是否正确录入");
                warnings.add(warning);
            }
        }

        // 检查净利润
        if (revenue.compareTo(BigDecimal.ZERO) > 0 && netProfit.compareTo(BigDecimal.ZERO) < 0) {
            BigDecimal lossRate = netProfit.negate().divide(revenue, 4, RoundingMode.HALF_UP).multiply(new BigDecimal(100));
            if (lossRate.compareTo(new BigDecimal("50")) > 0) {
                Map<String, String> warning = new HashMap<>();
                warning.put("type", "HIGH_LOSS");
                warning.put("message", "净利润亏损严重，亏损率: " + formatAmount(lossRate) + "%");
                warning.put("suggestion", "请关注公司的经营状况，分析亏损原因并采取相应措施");
                warnings.add(warning);
            }
        }

        // 检查费用占比
        BigDecimal totalExpense = amounts.getOrDefault("EXPENSE_SELLING", BigDecimal.ZERO)
                .add(amounts.getOrDefault("EXPENSE_ADMIN", BigDecimal.ZERO))
                .add(amounts.getOrDefault("EXPENSE_FINANCIAL", BigDecimal.ZERO));
        if (revenue.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal expenseRatio = totalExpense.divide(revenue, 4, RoundingMode.HALF_UP).multiply(new BigDecimal(100));
            if (expenseRatio.compareTo(new BigDecimal("80")) > 0) {
                Map<String, String> suggestion = new HashMap<>();
                suggestion.put("type", "HIGH_EXPENSE_RATIO");
                suggestion.put("message", "期间费用占收入比例较高: " + formatAmount(expenseRatio) + "%");
                suggestion.put("action", "建议分析各项费用的构成，寻找降低成本的机会");
                suggestions.add(suggestion);
            }
        }
    }

    /**
     * 现金流量表验证
     */
    private void validateCashFlowStatement(Map<String, BigDecimal> amounts,
                                            List<Map<String, String>> errors,
                                            List<Map<String, String>> warnings,
                                            List<Map<String, String>> suggestions) {
        BigDecimal operatingNet = amounts.getOrDefault("CASH_OPERATING_NET", BigDecimal.ZERO);
        BigDecimal investingNet = amounts.getOrDefault("CASH_INVESTING_NET", BigDecimal.ZERO);
        BigDecimal financingNet = amounts.getOrDefault("CASH_FINANCING_NET", BigDecimal.ZERO);
        BigDecimal netIncrease = amounts.getOrDefault("CASH_NET_INCREASE", BigDecimal.ZERO);

        // 验证现金净增加额计算
        BigDecimal calculatedNetIncrease = operatingNet.add(investingNet).add(financingNet)
                .add(amounts.getOrDefault("CASH_EXCHANGE_RATE", BigDecimal.ZERO));
        if (netIncrease.subtract(calculatedNetIncrease).abs().compareTo(new BigDecimal("0.01")) > 0) {
            Map<String, String> warning = new HashMap<>();
            warning.put("type", "NET_INCREASE_MISMATCH");
            warning.put("message", "现金净增加额计算不一致");
            warning.put("suggestion", "请检查经营活动、投资活动和筹资活动的现金流量净额是否正确");
            warnings.add(warning);
        }

        // 检查经营活动现金流
        if (operatingNet.compareTo(BigDecimal.ZERO) < 0) {
            Map<String, String> warning = new HashMap<>();
            warning.put("type", "NEGATIVE_OPERATING_CASH");
            warning.put("message", "经营活动现金流量净额为负数: " + formatAmount(operatingNet));
            warning.put("suggestion", "经营活动现金流为负可能表示公司主营业务造血能力不足，请关注应收账款回收和存货管理");
            warnings.add(warning);
        }

        // 检查投资活动现金流
        if (investingNet.compareTo(BigDecimal.ZERO) > 0) {
            Map<String, String> suggestion = new HashMap<>();
            suggestion.put("type", "POSITIVE_INVESTING_CASH");
            suggestion.put("message", "投资活动现金流量净额为正数: " + formatAmount(investingNet));
            suggestion.put("action", "投资活动现金流为正可能表示公司在处置资产或收回投资，请确认是否符合公司战略");
            suggestions.add(suggestion);
        }

        // 检查筹资活动现金流
        if (financingNet.compareTo(BigDecimal.ZERO) > 0) {
            Map<String, String> suggestion = new HashMap<>();
            suggestion.put("type", "POSITIVE_FINANCING_CASH");
            suggestion.put("message", "筹资活动现金流量净额为正数: " + formatAmount(financingNet));
            suggestion.put("action", "筹资活动现金流为正表示公司在融入资金，请关注债务规模和偿债能力");
            suggestions.add(suggestion);
        }
    }

    /**
     * 所有者权益变动表验证
     */
    private void validateEquityChangeStatement(Map<String, BigDecimal> amounts,
                                                List<Map<String, String>> errors,
                                                List<Map<String, String>> warnings,
                                                List<Map<String, String>> suggestions) {
        BigDecimal beginningTotal = amounts.getOrDefault("BEGINNING_TOTAL", BigDecimal.ZERO);
        BigDecimal changesTotal = amounts.getOrDefault("CHANGES_TOTAL", BigDecimal.ZERO);
        BigDecimal endingTotal = amounts.getOrDefault("ENDING_TOTAL", BigDecimal.ZERO);

        // 验证期末余额计算
        BigDecimal calculatedEnding = beginningTotal.add(changesTotal);
        if (endingTotal.subtract(calculatedEnding).abs().compareTo(new BigDecimal("0.01")) > 0) {
            Map<String, String> error = new HashMap<>();
            error.put("type", "ENDING_MISMATCH");
            error.put("message", String.format("期末余额计算不正确。期初: %s, 本年变动: %s, 计算期末: %s, 报表期末: %s",
                    formatAmount(beginningTotal), formatAmount(changesTotal), formatAmount(calculatedEnding), formatAmount(endingTotal)));
            error.put("suggestion", "请检查各项所有者权益变动的计算是否正确");
            errors.add(error);
        }

        // 检查实收资本变动
        BigDecimal capitalBeginning = amounts.getOrDefault("BALANCE_CAPITAL_PRIOR", BigDecimal.ZERO);
        BigDecimal capitalEnding = amounts.getOrDefault("BALANCE_CAPITAL_ENDING", BigDecimal.ZERO);
        if (capitalEnding.compareTo(capitalBeginning) != 0) {
            Map<String, String> suggestion = new HashMap<>();
            suggestion.put("type", "CAPITAL_CHANGE");
            suggestion.put("message", String.format("实收资本发生变动，期初: %s, 期末: %s",
                    formatAmount(capitalBeginning), formatAmount(capitalEnding)));
            suggestion.put("action", "实收资本变动需要有相应的工商变更登记，请确认手续是否完备");
            suggestions.add(suggestion);
        }

        // 检查盈余公积提取
        BigDecimal surplusReserve = amounts.getOrDefault("BALANCE_RESERVE_SURPLUS_ENDING", BigDecimal.ZERO);
        BigDecimal retainedEarnings = amounts.getOrDefault("BALANCE_RETAINED_ENDING", BigDecimal.ZERO);
        if (retainedEarnings.compareTo(BigDecimal.ZERO) > 0 && surplusReserve.compareTo(BigDecimal.ZERO) == 0) {
            Map<String, String> warning = new HashMap<>();
            warning.put("type", "NO_SURPLUS_RESERVE");
            warning.put("message", "未分配利润为 " + formatAmount(retainedEarnings) + "，但未提取盈余公积");
            warning.put("suggestion", "根据公司法规定，公司应按净利润的10%提取法定盈余公积（累计达注册资本50%时可不再提取）");
            warnings.add(warning);
        }
    }

    /**
     * 格式化金额显示
     */
    private String formatAmount(BigDecimal amount) {
        if (amount == null) {
            return "0.00";
        }
        return String.format("%,.2f", amount);
    }

    @Override
    public Map<String, Object> getReportTemplates(String groupid) {
        Map<String, Object> result = new HashMap<>();

        FinReportTemplates tempquery = new FinReportTemplates();
        tempquery.setGroupid(groupid);
        tempquery.setStatus(1);
        List<FinReportTemplates> templates =this.finReportTemplatesMapper.selectFinReportTemplatesList(tempquery);

        List<Map<String, Object>> templateList = templates.stream()
                .map(template -> {
                    Map<String, Object> templateInfo = new HashMap<>();
                    templateInfo.put("templateCode", template.getTemplateCode());
                    templateInfo.put("templateName", template.getTemplateName());
                    templateInfo.put("templateType", template.getTemplateType());
                    templateInfo.put("description", template.getDescription());
                    return templateInfo;
                })
                .collect(Collectors.toList());

        result.put("templates", templateList);
        result.put("count", templateList.size());

        return result;
    }

    @Override
    public void clearReportCache(String groupid, String templateCode, String period) {
        String cacheKey = groupid + "_" + templateCode;
        if (period != null) {
            cacheKey += "_" + period;
        }

        // 清除匹配的缓存项
        String finalCacheKey = cacheKey;
        calculationCache.keySet().removeIf(key -> key.startsWith(finalCacheKey));
    }

    /**
     * 检查损益结转情况
     * @param groupid 租户ID
     * @param period 期间
     * @return 损益结转检查结果
     */
    private Map<String, Object> checkProfitLossTransfer(String groupid, String period) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 获取所有损益类科目（以6开头的科目）
            Map<String, BigDecimal> allBalances = subjectBalanceService.getAllSubjectBalance(groupid, period);
            
            int untransferredCount = 0;
            BigDecimal untransferredAmount = BigDecimal.ZERO;
            
            for (Map.Entry<String, BigDecimal> entry : allBalances.entrySet()) {
                String subjectCode = entry.getKey();
                // 去掉前缀 "ACC_"
                String code = subjectCode.startsWith("ACC_") ? subjectCode.substring(4) : subjectCode;
                
                // 检查是否是损益类科目（6开头的收入类，5开头的成本费用类）
                if (code.startsWith("6") || code.startsWith("5")) {
                    BigDecimal balance = entry.getValue();
                    if (balance != null && balance.compareTo(BigDecimal.ZERO) != 0) {
                        untransferredCount++;
                        untransferredAmount = untransferredAmount.add(balance.abs());
                    }
                }
            }
            
            boolean transferred = untransferredCount == 0;
            result.put("transferred", transferred);
            result.put("untransferredCount", untransferredCount);
            result.put("untransferredAmount", untransferredAmount);
            
        } catch (Exception e) {
            // 如果检查失败，默认为已结转
            result.put("transferred", true);
            result.put("untransferredCount", 0);
            result.put("untransferredAmount", BigDecimal.ZERO);
        }
        
        return result;
    }

    /**
     * 检查未设置报表项目的科目
     * @param groupid 租户ID
     * @param period 期间
     * @param templateCode 模板编码
     * @param rules 映射规则列表
     * @param subjects 会计科目列表
     * @return 未映射科目列表
     */
    private List<Map<String, Object>> checkUnmappedSubjects(String groupid, String period, 
                                                              String templateCode,
                                                              List<FinReportMappingRules> rules,
                                                              List<FinAccountingSubjects> subjects) {
        List<Map<String, Object>> unmappedList = new ArrayList<>();
        
        try {
            // 获取所有科目余额
            Map<String, BigDecimal> allBalances = subjectBalanceService.getAllSubjectBalance(groupid, period);
            
            // 收集已映射的科目前缀
            Set<String> mappedPrefixes = new HashSet<>();
            if (rules != null) {
                for (FinReportMappingRules rule : rules) {
                    if (rule.getStatus() == 1 && rule.getMatchValue() != null) {
                        mappedPrefixes.add(rule.getMatchValue());
                    }
                }
            }
            
            // 检查每个有余额的科目
            for (Map.Entry<String, BigDecimal> entry : allBalances.entrySet()) {
                String subjectCode = entry.getKey();
                BigDecimal balance = entry.getValue();
                
                // 跳过余额为0的科目
                if (balance == null || balance.compareTo(BigDecimal.ZERO) == 0) {
                    continue;
                }
                
                // 去掉前缀 "ACC_"
                String code = subjectCode.startsWith("ACC_") ? subjectCode.substring(4) : subjectCode;
                
                // 检查是否已被映射
                boolean isMapped = false;
                for (String prefix : mappedPrefixes) {
                    if (code.startsWith(prefix)) {
                        isMapped = true;
                        break;
                    }
                }
                
                // 如果未映射，添加到未映射列表
                if (!isMapped && subjects != null) {
                    // 查找科目名称
                    String subjectName = "";
                    for (FinAccountingSubjects subject : subjects) {
                        if (subject.getSubjectCode().equals(code)) {
                            subjectName = subject.getSubjectName();
                            break;
                        }
                    }
                    
                    // 只添加一级或二级科目，避免过于详细
                    if (code.length() <= 6) {
                        Map<String, Object> unmapped = new HashMap<>();
                        unmapped.put("subjectCode", code);
                        unmapped.put("subjectName", subjectName.isEmpty() ? "未知科目" : subjectName);
                        unmapped.put("balance", balance);
                        unmappedList.add(unmapped);
                    }
                }
            }
            
            // 限制返回数量，避免过多
            if (unmappedList.size() > 20) {
                unmappedList = unmappedList.subList(0, 20);
            }
            
        } catch (Exception e) {
            // 如果检查失败，返回空列表
            System.err.println("检查未映射科目失败: " + e.getMessage());
        }
        
        return unmappedList;
    }

}
