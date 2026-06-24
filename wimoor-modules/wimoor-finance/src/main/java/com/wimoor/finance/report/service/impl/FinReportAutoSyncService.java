package com.wimoor.finance.report.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wimoor.finance.report.domain.*;
import com.wimoor.finance.report.service.*;
import com.wimoor.finance.setting.domain.*;
import com.wimoor.finance.setting.service.*;

/**
 * 报表自动同步服务
 * 用于根据映射规则自动生成和更新报表项目的计算公式
 *
 * @author wimoor
 * @date 2025-06-08
 */
@Service
public class FinReportAutoSyncService
{
    private static final Logger log = LoggerFactory.getLogger(FinReportAutoSyncService.class);

    @Autowired
    private IFinReportMappingRulesService mappingRulesService;

    @Autowired
    private IFinReportItemsService reportItemsService;

    @Autowired
    private IFinAccountingSubjectsService accountingSubjectsService;
    
    @Autowired
    private IFinReportTemplatesService reportTemplatesService;

    /**
     * 同步报表模板公式
     * 根据映射规则和会计科目，自动更新报表项目的计算公式
     *
     * @param groupid 租户ID
     * @param templateId 模板ID
     * @param templateType 模板类型
     * @return 更新的项目数量
     */
    @Transactional
    public int syncReportFormulas(String groupid, Long templateId, String templateType)
    {
        log.info("开始同步报表公式: groupid={}, templateId={}, templateType={}", groupid, templateId, templateType);

        // 1. 获取该租户的所有会计科目
        FinAccountingSubjects subjectQuery = new FinAccountingSubjects();
        subjectQuery.setGroupid(groupid);
        subjectQuery.setStatus(1); // 只查询启用的科目
        List<FinAccountingSubjects> allSubjects = accountingSubjectsService.selectFinAccountingSubjectsList(subjectQuery);

        if (allSubjects == null || allSubjects.isEmpty())
        {
            log.warn("未找到会计科目，跳过同步");
            return 0;
        }
        log.info("找到会计科目: {}个", allSubjects.size());

        // 2. 获取映射规则
        List<FinReportMappingRules> rules = mappingRulesService.selectRulesByTemplateType(groupid, templateType);

        if (rules == null || rules.isEmpty())
        {
            log.warn("未找到映射规则，跳过同步");
            return 0;
        }
        log.info("找到映射规则: {}个", rules.size());
        // 打印规则的itemCode
        Set<String> ruleItemCodes = rules.stream()
            .map(FinReportMappingRules::getItemCode)
            .collect(Collectors.toSet());
        log.info("映射规则中的项目编码: {}", ruleItemCodes);

        // 3. 获取需要同步的模板ID列表
        List<Long> templateIds = new ArrayList<>();
        if (templateId != null && templateId > 0) {
            templateIds.add(templateId);
        } else {
            // 查询该类型的所有模板
            FinReportTemplates templateQuery = new FinReportTemplates();
            templateQuery.setGroupid(groupid);
            templateQuery.setTemplateType(templateType);
            List<FinReportTemplates> templates = reportTemplatesService.selectFinReportTemplatesList(templateQuery);
            if (templates != null && !templates.isEmpty()) {
                templateIds = templates.stream()
                    .map(FinReportTemplates::getTemplateId)
                    .collect(Collectors.toList());
            }
        }
        
        if (templateIds.isEmpty()) {
            log.warn("未找到模板，跳过同步");
            return 0;
        }
        
        // 4. 遍历模板，同步公式
        int totalUpdated = 0;
        for (Long tid : templateIds) {
            FinReportItems itemQuery = new FinReportItems();
            itemQuery.setTemplateId(tid);
            itemQuery.setGroupid(groupid);
            List<FinReportItems> reportItems = reportItemsService.selectFinReportItemsList(itemQuery);
            
            if (reportItems == null || reportItems.isEmpty()) {
                log.info("模板 {} 没有报表项目，跳过", tid);
                continue;
            }
            
            // 打印报表项目的itemCode
            Set<String> itemCodes = reportItems.stream()
                .map(FinReportItems::getItemCode)
                .collect(Collectors.toSet());
            log.info("模板 {} 的报表项目编码: {}", tid, itemCodes);
            
            // 按项目编码分组规则
            Map<String, List<FinReportMappingRules>> rulesByItemCode = rules.stream()
                .collect(Collectors.groupingBy(FinReportMappingRules::getItemCode));
            
            // 遍历报表项目，根据规则生成公式
            for (FinReportItems item : reportItems) {
                String itemCode = item.getItemCode();
                List<FinReportMappingRules> itemRules = rulesByItemCode.get(itemCode);
                
                if (itemRules == null || itemRules.isEmpty()) {
                    // 没有匹配的规则
                    continue;
                }
                log.info("项目 {} 匹配到 {} 条规则", itemCode, itemRules.size());
                
                // 根据规则匹配科目并生成公式
                String formula = generateFormulaFromRules(allSubjects, itemRules);
                
                if (formula != null && !formula.isEmpty()) {
                    // 更新报表项目
                    item.setCalculationRule(formula);
                    item.setFormulaType("FORMULA");
                    item.setSubjectCodes(extractSubjectCodes(formula));
                    reportItemsService.updateFinReportItems(item);
                    totalUpdated++;
                    
                    log.info("更新报表项目: templateId={}, itemCode={}, formula={}", tid, itemCode, formula);
                }
            }
        }
        
        log.info("报表公式同步完成: 更新了{}个项目", totalUpdated);
        return totalUpdated;
    }

    /**
     * 根据规则生成公式
     *
     * @param allSubjects 所有会计科目
     * @param rules 规则列表
     * @return 公式字符串
     */
    private String generateFormulaFromRules(List<FinAccountingSubjects> allSubjects, List<FinReportMappingRules> rules)
    {
        List<String> addParts = new ArrayList<>();
        List<String> subtractParts = new ArrayList<>();

        // 按优先级排序规则
        rules.sort((r1, r2) -> Integer.compare(
                r1.getPriority() != null ? r1.getPriority() : 100,
                r2.getPriority() != null ? r2.getPriority() : 100
        ));

        for (FinReportMappingRules rule : rules)
        {
            if (rule.getStatus() != null && rule.getStatus() != 1)
            {
                continue; // 跳过停用的规则
            }

            // 根据匹配类型匹配科目
            List<FinAccountingSubjects> matchedSubjects = matchSubjects(allSubjects, rule);

            for (FinAccountingSubjects subject : matchedSubjects)
            {
                String subjectCode = subject.getSubjectCode();

                if ("ADD".equals(rule.getOperator()))
                {
                    if (!addParts.contains(subjectCode))
                    {
                        addParts.add(subjectCode);
                    }
                }
                else if ("SUBTRACT".equals(rule.getOperator()))
                {
                    if (!subtractParts.contains(subjectCode))
                    {
                        subtractParts.add(subjectCode);
                    }
                }
                else if ("EXCLUDE".equals(rule.getOperator()))
                {
                    // 排除项，从加项中移除
                    addParts.remove(subjectCode);
                }
            }
        }

        // 生成公式
        StringBuilder formula = new StringBuilder();

        // 添加加项
        for (int i = 0; i < addParts.size(); i++)
        {
            if (i > 0)
            {
                formula.append("+");
            }
            formula.append(addParts.get(i));
        }

        // 添加减项
        for (String subtractPart : subtractParts)
        {
            formula.append("-");
            formula.append(subtractPart);
        }

        return formula.toString();
    }

    /**
     * 根据规则匹配会计科目
     *
     * @param allSubjects 所有会计科目
     * @param rule 映射规则
     * @return 匹配的科目列表
     */
    private List<FinAccountingSubjects> matchSubjects(List<FinAccountingSubjects> allSubjects, FinReportMappingRules rule)
    {
        List<FinAccountingSubjects> matched = new ArrayList<>();

        for (FinAccountingSubjects subject : allSubjects)
        {
            boolean isMatch = false;

            switch (rule.getMatchType())
            {
                case "PREFIX":
                    // 前缀匹配
                    if (subject.getSubjectCode() != null && subject.getSubjectCode().startsWith(rule.getMatchValue()))
                    {
                        isMatch = true;
                    }
                    break;

                case "EXACT":
                    // 精确匹配
                    if (subject.getSubjectCode() != null && subject.getSubjectCode().equals(rule.getMatchValue()))
                    {
                        isMatch = true;
                    }
                    break;

                case "RANGE":
                    // 范围匹配
                    if (subject.getSubjectCode() != null &&
                        subject.getSubjectCode().compareTo(rule.getMatchValue()) >= 0 &&
                        subject.getSubjectCode().compareTo(rule.getMatchValueEnd()) <= 0)
                    {
                        isMatch = true;
                    }
                    break;

                case "PARENT":
                    // 父级科目匹配
                    if (subject.getParentCode() != null && subject.getParentCode().equals(rule.getMatchValue()))
                    {
                        isMatch = true;
                    }
                    break;

                default:
                    break;
            }

            if (isMatch)
            {
                // 检查科目类型
                if (rule.getSubjectType() != null && subject.getSubjectType() != null)
                {
                    if (!rule.getSubjectType().equals(subject.getSubjectType()))
                    {
                        continue;
                    }
                }

                // 检查余额方向
                if (rule.getDirection() != null && subject.getDirection() != null)
                {
                    if (!rule.getDirection().equals(subject.getDirection()))
                    {
                        continue;
                    }
                }

                // 检查是否仅匹配末级科目
                if (rule.getIsLeafOnly() != null && rule.getIsLeafOnly() == 1)
                {
                    if (subject.getIsLeaf() == null || subject.getIsLeaf() != 1)
                    {
                        continue;
                    }
                }

                matched.add(subject);
            }
        }

        return matched;
    }

    /**
     * 从公式中提取科目编码
     *
     * @param formula 公式字符串
     * @return 科目编码（逗号分隔）
     */
    private String extractSubjectCodes(String formula)
    {
        if (formula == null || formula.isEmpty())
        {
            return null;
        }

        // 移除运算符，提取科目编码
        String[] parts = formula.split("[+\\-]");
        StringBuilder codes = new StringBuilder();

        for (String part : parts)
        {
            String code = part.trim();
            if (!code.isEmpty())
            {
                if (codes.length() > 0)
                {
                    codes.append(",");
                }
                codes.append(code);
            }
        }

        // 限制长度，避免数据库字段溢出
        String result = codes.toString();
        if (result.length() > 1900)
        {
            log.warn("subject_codes 过长({}字符)，已截断。formula={}", result.length(), formula);
            result = result.substring(0, 1900);
        }

        return result;
    }

    /**
     * 检查新科目并更新公式
     * 当新增会计科目时，检查是否需要更新报表公式
     *
     * @param groupid 租户ID
     * @param newSubject 新增的科目
     */
    public void checkAndUpdateFormulasForNewSubject(String groupid, FinAccountingSubjects newSubject)
    {
        log.info("检查新科目是否需要更新报表公式: groupid={}, subjectCode={}", groupid, newSubject.getSubjectCode());

        // 查询所有模板类型
        String[] templateTypes = {"BALANCE_SHEET", "INCOME_STATEMENT", "CASH_FLOW"};

        for (String templateType : templateTypes)
        {
            // 获取该模板类型的规则
            List<FinReportMappingRules> rules = mappingRulesService.selectRulesByTemplateType(groupid, templateType);

            for (FinReportMappingRules rule : rules)
            {
                if (isSubjectMatchRule(newSubject, rule))
                {
                    log.info("新科目匹配规则: subjectCode={}, ruleId={}, itemCode={}",
                            newSubject.getSubjectCode(), rule.getRuleId(), rule.getItemCode());

                    // 更新对应报表项目的公式
                    updateReportItemFormula(groupid, templateType, rule.getItemCode());
                    break;
                }
            }
        }
    }

    /**
     * 检查科目是否匹配规则
     *
     * @param subject 会计科目
     * @param rule 映射规则
     * @return 是否匹配
     */
    private boolean isSubjectMatchRule(FinAccountingSubjects subject, FinReportMappingRules rule)
    {
        boolean isMatch = false;

        switch (rule.getMatchType())
        {
            case "PREFIX":
                if (subject.getSubjectCode() != null && subject.getSubjectCode().startsWith(rule.getMatchValue()))
                {
                    isMatch = true;
                }
                break;

            case "EXACT":
                if (subject.getSubjectCode() != null && subject.getSubjectCode().equals(rule.getMatchValue()))
                {
                    isMatch = true;
                }
                break;

            case "RANGE":
                if (subject.getSubjectCode() != null &&
                    subject.getSubjectCode().compareTo(rule.getMatchValue()) >= 0 &&
                    subject.getSubjectCode().compareTo(rule.getMatchValueEnd()) <= 0)
                {
                    isMatch = true;
                }
                break;

            case "PARENT":
                if (subject.getParentCode() != null && subject.getParentCode().equals(rule.getMatchValue()))
                {
                    isMatch = true;
                }
                break;

            default:
                break;
        }

        if (isMatch)
        {
            // 检查科目类型
            if (rule.getSubjectType() != null && subject.getSubjectType() != null)
            {
                if (!rule.getSubjectType().equals(subject.getSubjectType()))
                {
                    return false;
                }
            }

            // 检查余额方向
            if (rule.getDirection() != null && subject.getDirection() != null)
            {
                if (!rule.getDirection().equals(subject.getDirection()))
                {
                    return false;
                }
            }

            // 检查是否仅匹配末级科目
            if (rule.getIsLeafOnly() != null && rule.getIsLeafOnly() == 1)
            {
                if (subject.getIsLeaf() == null || subject.getIsLeaf() != 1)
                {
                    return false;
                }
            }

            return true;
        }

        return false;
    }

    /**
     * 更新报表项目公式
     *
     * @param groupid 租户ID
     * @param templateType 模板类型
     * @param itemCode 项目编码
     */
    private void updateReportItemFormula(String groupid, String templateType, String itemCode)
    {
        // 这里需要根据实际情况获取模板ID
        // 简化实现：查询所有该类型的模板并更新
        log.info("更新报表项目公式: groupid={}, templateType={}, itemCode={}", groupid, templateType, itemCode);

        // TODO: 实现具体的更新逻辑
        // 1. 查询该租户的所有模板
        // 2. 找到对应类型的模板
        // 3. 更新该项目的公式
    }
}
