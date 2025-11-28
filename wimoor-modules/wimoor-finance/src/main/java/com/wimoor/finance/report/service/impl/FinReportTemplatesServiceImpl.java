package com.wimoor.finance.report.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;
import com.googlecode.aviator.Options;
import com.wimoor.common.mvc.BizException;
import com.wimoor.finance.ledger.service.IFinGeneralLedgerService;
import com.wimoor.finance.ledger.service.ISubjectBalanceService;
import com.wimoor.finance.report.domain.FinReportItems;
import com.wimoor.finance.report.domain.dto.ReportGenerateRequest;
import com.wimoor.finance.report.domain.dto.ReportGenerateResponse;
import com.wimoor.finance.report.domain.dto.ReportItemValueDTO;
import com.wimoor.finance.report.service.IFinReportItemsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.wimoor.finance.report.mapper.FinReportTemplatesMapper;
import com.wimoor.finance.report.domain.FinReportTemplates;
import com.wimoor.finance.report.service.IFinReportTemplatesService;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.*;
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

            // 5. 构建响应数据
            List<ReportItemValueDTO> itemDTOs = buildReportItems(reportItems, currentAmounts, compareAmounts, amountUnit);

            // 6. 计算财务指标（仅资产负债表和利润表）
            Map<String, Object> financialRatios = new HashMap<>();
            if ("BALANCE_SHEET".equals(template.getTemplateType()) || "INCOME_STATEMENT".equals(template.getTemplateType())) {
                financialRatios = calculateFinancialRatios(currentAmounts, template.getTemplateType());
            }

            // 7. 构建图表数据
            Map<String, Object> chartData = new HashMap<>();
            if (request.getIncludeChartData()) {
                chartData = buildChartData(currentAmounts, template.getTemplateType());
            }

            // 8. 构建汇总信息
            Map<String, Object> summary = buildReportSummary(currentAmounts, template.getTemplateType());

            // 9. 验证报表数据
            if ("BALANCE_SHEET".equals(template.getTemplateType())) {
                validateBalanceSheet(currentAmounts);
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

        // 按层级从高到低排序计算，确保父级项目在子级之后计算
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

        // 存入缓存
        calculationCache.put(cacheKey, amounts);

        return amounts;
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
        switch (dataSource) {
            case "SUBJECT":
                // 从科目余额获取数据
                if (item.getSubjectCodes() != null && !item.getSubjectCodes().isEmpty()) {
                    return getSubjectBalance(groupid, period, item.getSubjectCodes(), item.getAmountType());
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
        // 根据自定义规则计算
        switch (customRule) {
            case "ASSET_CURRENT":
                return sumChildrenAmounts("ASSET_CURRENT", allItems, amounts);

            case "ASSET_NON_CURRENT":
                return sumChildrenAmounts("ASSET_NON_CURRENT", allItems, amounts);

            case "LIABILITY_CURRENT":
                return sumChildrenAmounts("LIABILITY_CURRENT", allItems, amounts);

            case "LIABILITY_NON_CURRENT":
                return sumChildrenAmounts("LIABILITY_NON_CURRENT", allItems, amounts);

            case "EQUITY_TOTAL":
                return sumChildrenAmounts("EQUITY", allItems, amounts);

            default:
                // 其他自定义规则
                return getCustomAmount(groupid, period, customRule);
        }
    }

    private BigDecimal calculateAutoAmount(FinReportItems item, Map<String, BigDecimal> amounts, List<FinReportItems> allItems) {
        if ("SUM(CHILDREN)".equals(item.getCalculationRule())) {
            return sumChildrenAmounts(item.getItemCode(), allItems, amounts);
        }
        // 其他自动计算规则...

        return BigDecimal.ZERO;
    }

    private BigDecimal sumChildrenAmounts(String parentCode, List<FinReportItems> allItems, Map<String, BigDecimal> amounts) {
        return allItems.stream()
                .filter(item -> parentCode.equals(item.getParentCode()) && item.getIsShow())
                .map(item -> amounts.getOrDefault(item.getItemCode(), BigDecimal.ZERO))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal evaluateFormula(String formula, Map<String, BigDecimal> amounts) {
        Map<String, Object> env =null;
        try {
            System.out.println("原始公式: " + formula);
            System.out.println("金额数据: " + amounts);

            // 预处理公式
            String processedFormula = processAccountFormula(formula);
            System.out.println("处理后的公式: " + processedFormula);

            // 准备环境变量
            env = prepareEnvironment(amounts);

            // 执行计算
            AviatorEvaluator.setOption(Options.ALWAYS_USE_DOUBLE_AS_DECIMAL, false);
            Object result = AviatorEvaluator.execute(processedFormula, env);

            System.out.println("计算结果: " + result);
            return convertToBigDecimal(result);

        } catch (Exception e) {
            throw new RuntimeException("公式计算错误: " + formula, e);
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
                                                      Integer amountUnit) {
        List<ReportItemValueDTO> result = new ArrayList<>();
        BigDecimal unitDivisor = new BigDecimal(amountUnit);
          //帮我reportItems进行排序按lineNumber
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

            // 当前期间金额
            BigDecimal currentAmount = currentAmounts.getOrDefault(item.getItemCode(), BigDecimal.ZERO);
            dto.setAmount(currentAmount.divide(unitDivisor, 2, RoundingMode.HALF_UP));

            // 比较期间金额和变化率
            if (!compareAmounts.isEmpty()) {
                BigDecimal compareAmount = compareAmounts.getOrDefault(item.getItemCode(), BigDecimal.ZERO);
                dto.setComparisonAmount(compareAmount.divide(unitDivisor, 2, RoundingMode.HALF_UP));

                if (compareAmount.compareTo(BigDecimal.ZERO) != 0) {
                    BigDecimal changeAmount = currentAmount.subtract(compareAmount);
                    BigDecimal changeRate = changeAmount.divide(compareAmount.abs(), 4, RoundingMode.HALF_UP)
                            .multiply(new BigDecimal(100));
                    dto.setChangeAmount(changeAmount.divide(unitDivisor, 2, RoundingMode.HALF_UP));
                    dto.setChangeRate(changeRate);
                }
            }

            result.add(dto);
        }

        return result;
    }

    private void validateBalanceSheet(Map<String, BigDecimal> amounts) {
        BigDecimal assetTotal = amounts.getOrDefault("ASSET_TOTAL", BigDecimal.ZERO);
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
        List<String> errors = new ArrayList<>();
        List<String> warnings = new ArrayList<>();

        try {
            FinReportTemplates query = new FinReportTemplates();
            query.setGroupid(groupid);
            query.setTemplateCode(templateCode);
            query.setStatus(1);
            List<FinReportTemplates> templates =this.finReportTemplatesMapper.selectFinReportTemplatesList(query);
            if(templates==null||templates.isEmpty()) {
              throw new BizException("模板不存在");
            }
            FinReportTemplates template=templates.get(0);
            FinReportItems itemquery = new FinReportItems();
            itemquery.setGroupid(groupid);
            itemquery.setTemplateId(template.getTemplateId());
            itemquery.setStatus(1);
            List<FinReportItems> items = this.finReportItemsService.selectFinReportItemsList(itemquery);
                    //.findAllVisibleItems(tenantId, template.getTemplateId());

            Map<String, BigDecimal> amounts = calculateReportAmounts(groupid, period, items, template);

            // 验证关键数据
            if ("BALANCE_SHEET".equals(templateCode)) {
                validateBalanceSheet(amounts);
            }

            // 检查零值项目
            for (FinReportItems item : items) {
                if (item.getIsShowZero()) {
                    BigDecimal amount = amounts.get(item.getItemCode());
                    if (amount != null && amount.compareTo(BigDecimal.ZERO) == 0) {
                        warnings.add("项目 " + item.getItemName() + " 金额为零");
                    }
                }
            }

            result.put("valid", true);
            result.put("message", "数据验证通过");

        } catch (Exception e) {
            errors.add(e.getMessage());
            result.put("valid", false);
            result.put("message", "数据验证失败");
        }

        result.put("errors", errors);
        result.put("warnings", warnings);

        return result;
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


}
