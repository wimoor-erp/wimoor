package com.wimoor.finance.report.service.impl;

import com.wimoor.finance.report.domain.FinReportItems;
import com.wimoor.finance.report.domain.FinReportMappingRules;
import com.wimoor.finance.report.mapper.FinReportItemsMapper;
import com.wimoor.finance.report.service.IFinReportItemsService;
import com.wimoor.finance.report.service.IFinReportMappingRulesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 报表项目Service业务层处理
 *
 * @author wimoor
 * @date 2025-11-04
 */
@Service
public class FinReportItemsServiceImpl implements IFinReportItemsService
{
    private static final Logger log = LoggerFactory.getLogger(FinReportItemsServiceImpl.class);
    
    @Autowired
    private FinReportItemsMapper finReportItemsMapper;
    
    @Autowired
    private IFinReportMappingRulesService finReportMappingRulesService;

    /**
     * 查询报表项目
     *
     * @param itemId 报表项目主键
     * @return 报表项目
     */
    @Override
    public FinReportItems selectFinReportItemsByItemId(Long itemId)
    {
        return finReportItemsMapper.selectFinReportItemsByItemId(itemId);
    }

    /**
     * 查询报表项目列表
     *
     * @param finReportItems 报表项目
     * @return 报表项目
     */
    @Override
    public List<FinReportItems> selectFinReportItemsList(FinReportItems finReportItems)
    {
        //帮我reportItems进行排序按lineNumber
        List<FinReportItems> reportItems = finReportItemsMapper.selectFinReportItemsList(finReportItems);
        //先按level升序排序，再按linenumber升序排序
        reportItems.sort((item1, item2) -> {
            // 先按level比较
            if(item1.getItemLevel()!=null&&item2.getItemLevel()!=null){
                if(item1.getItemLevel().equals(item2.getItemLevel())&&item1.getItemLevel()==3){
                    // 如果level相同，则按linenumber比较
                    int line1=item1.getLineNumber()!=null?item1.getLineNumber():0;
                    int line2=item2.getLineNumber()!=null?item2.getLineNumber():0;
                    return Integer.compare(line1, line2);
                }
            }
            return item1.getItemId()!=null?item1.getItemId().compareTo(item2.getItemId()):0;

        });

        return reportItems;
    }

    /**
     * 查询所有父级项目列表（用于下拉选择）
     *
     * @param templateId 报表模板ID
     * @return 父级项目集合
     */
    @Override
    public List<FinReportItems> selectAllParentItems(Long templateId)
    {
        FinReportItems query = new FinReportItems();
        query.setTemplateId(templateId);
        List<FinReportItems> allItems = finReportItemsMapper.selectFinReportItemsList(query);

        // 过滤出可以作为父级的项目：非末级项目或者级别小于4的项目
        List<FinReportItems> parentItems = allItems.stream()
                .filter(item -> item.getIsLeaf() == null || item.getIsLeaf() == false ||
                        (item.getItemLevel() != null && item.getItemLevel() < 4))
                .sorted((item1, item2) -> {
                    // 先按itemLevel升序排序，再按lineNumber升序排序
                    if (item1.getItemLevel() != null && item2.getItemLevel() != null) {
                        int levelCompare = item1.getItemLevel().compareTo(item2.getItemLevel());
                        if (levelCompare != 0) {
                            return levelCompare;
                        }
                    }
                    return Integer.compare(
                            item1.getLineNumber() != null ? item1.getLineNumber() : 0,
                            item2.getLineNumber() != null ? item2.getLineNumber() : 0
                    );
                })
                .collect(Collectors.toList());

        return parentItems;
    }

    /**
     * 新增报表项目
     *
     * @param finReportItems 报表项目
     * @return 结果
     */
    @Override
    public int insertFinReportItems(FinReportItems finReportItems)
    {
        return finReportItemsMapper.insertFinReportItems(finReportItems);
    }

    /**
     * 修改报表项目
     *
     * @param finReportItems 报表项目
     * @return 结果
     */
    @Override
    public int updateFinReportItems(FinReportItems finReportItems)
    {
        return finReportItemsMapper.updateFinReportItems(finReportItems);
    }

    /**
     * 批量删除报表项目
     *
     * @param itemIds 需要删除的报表项目主键集合
     * @return 结果
     */
    @Override
    public int deleteFinReportItemsByItemIds(Long[] itemIds)
    {
        return finReportItemsMapper.deleteFinReportItemsByItemIds(itemIds);
    }

    /**
     * 删除报表项目信息
     *
     * @param itemId 报表项目主键
     * @return 结果
     */
    @Override
    public int deleteFinReportItemsByItemId(Long itemId)
    {
        return finReportItemsMapper.deleteFinReportItemsByItemId(itemId);
    }
    
    /**
     * 初始化模板的报表项目
     * 会删除现有项目并根据模板类型重新创建默认项目
     *
     * @param templateId 模板ID
     * @param groupid 租户ID
     * @param templateType 模板类型
     * @return 创建的项目数量
     */
    @Override
    @Transactional
    public int initTemplateItems(Long templateId, String groupid, String templateType)
    {
        log.info("开始初始化模板项目: templateId={}, groupid={}, templateType={}", templateId, groupid, templateType);
        
        // 1. 删除该模板下的所有现有项目
        FinReportItems deleteQuery = new FinReportItems();
        deleteQuery.setTemplateId(templateId);
        deleteQuery.setGroupid(groupid);
        List<FinReportItems> existingItems = finReportItemsMapper.selectFinReportItemsList(deleteQuery);
        
        if (existingItems != null && !existingItems.isEmpty())
        {
            Long[] itemIds = existingItems.stream()
                .map(FinReportItems::getItemId)
                .toArray(Long[]::new);
            finReportItemsMapper.deleteFinReportItemsByItemIds(itemIds);
            log.info("删除了 {} 个现有项目", itemIds.length);
        }
        
        // 2. 根据模板类型创建默认项目
        List<FinReportItems> defaultItems = createDefaultItems(templateId, groupid, templateType);
        
        // 3. 批量插入默认项目
        int createdCount = 0;
        for (FinReportItems item : defaultItems)
        {
            finReportItemsMapper.insertFinReportItems(item);
            createdCount++;
        }
        
        // 4. 删除旧的映射规则并创建新的默认映射规则
        finReportMappingRulesService.deleteRulesByTenantAndType(groupid, templateType);
        createDefaultMappingRules(groupid, templateType);
        
        log.info("初始化完成，创建了 {} 个项目", createdCount);
        return createdCount;
    }
    
    /**
     * 根据模板类型创建默认的报表项目列表
     */
    private List<FinReportItems> createDefaultItems(Long templateId, String groupid, String templateType)
    {
        List<FinReportItems> items = new ArrayList<>();
        
        switch (templateType)
        {
            case "BALANCE_SHEET":
                items = createBalanceSheetItems(templateId, groupid);
                break;
            case "INCOME_STATEMENT":
                items = createIncomeStatementItems(templateId, groupid);
                break;
            case "CASH_FLOW":
                items = createCashFlowItems(templateId, groupid);
                break;
            case "EQUITY_CHANGE":
                items = createEquityChangeItems(templateId, groupid);
                break;
            default:
                log.warn("未知的模板类型: {}", templateType);
                break;
        }
        
        return items;
    }
    
    /**
     * 创建资产负债表默认项目
     */
    private List<FinReportItems> createBalanceSheetItems(Long templateId, String groupid)
    {
        List<FinReportItems> items = new ArrayList<>();
        
        // ===== 资产部分 =====
        // 叶子项使用 DIRECT/FORMULA 直接从科目余额取数，不依赖映射规则
        // 分组标题项使用 FORMULA 空公式，只起标题作用，不显示数据
        // 合计项使用 FORMULA 引用子项求和

        // --- 流动资产 ---
         items.add(createItem(templateId, groupid, "ASSET_CURRENT", "流动资产", "ASSET", 2, 1, "ASSET", null, "FORMULA", "", null, null, null, true, false));
         items.add(createItem(templateId, groupid, "ASSET_CASH", "货币资金", "ASSET_CURRENT", 3, 2, "ASSET", null, "CUSTOM", "CUSTOM:ASSET_CASH", null, "END_BALANCE", null, true, true));
         items.add(createItem(templateId, groupid, "ASSET_FINANCIAL", "交易性金融资产", "ASSET_CURRENT", 3, 3, "ASSET", null, "CUSTOM", "CUSTOM:ASSET_FINANCIAL", null, "END_BALANCE", null, true, true));
         items.add(createItem(templateId, groupid, "ASSET_BILLS", "应收票据", "ASSET_CURRENT", 3, 4, "ASSET", null, "CUSTOM", "CUSTOM:ASSET_BILLS", null, "END_BALANCE", null, true, true));
         items.add(createItem(templateId, groupid, "ASSET_RECEIVABLE", "应收账款", "ASSET_CURRENT", 3, 5, "ASSET", null, "CUSTOM", "CUSTOM:ASSET_RECEIVABLE", null, "END_BALANCE", null, true, true));
         items.add(createItem(templateId, groupid, "ASSET_OTHER_RECEIVABLE", "其他应收款", "ASSET_CURRENT", 3, 6, "ASSET", null, "CUSTOM", "CUSTOM:ASSET_OTHER_RECEIVABLE", null, "END_BALANCE", null, true, true));
         items.add(createItem(templateId, groupid, "ASSET_PREPAY", "预付款项", "ASSET_CURRENT", 3, 7, "ASSET", null, "CUSTOM", "CUSTOM:ASSET_PREPAY", null, "END_BALANCE", null, true, true));
         items.add(createItem(templateId, groupid, "ASSET_INVENTORY", "存货", "ASSET_CURRENT", 3, 8, "ASSET", null, "CUSTOM", "CUSTOM:ASSET_INVENTORY", null, "END_BALANCE", null, true, true));
         items.add(createItem(templateId, groupid, "ASSET_CURRENT_OTHER", "其他流动资产", "ASSET_CURRENT", 3, 9, "ASSET", null, "CUSTOM", "CUSTOM:ASSET_CURRENT_OTHER", null, null, null, true, true));
        items.add(createItem(templateId, groupid, "ASSET_CURRENT_TOTAL", "流动资产合计", "ASSET", 2, 10, "ASSET", null, "FORMULA", "ASSET_CASH+ASSET_FINANCIAL+ASSET_BILLS+ASSET_RECEIVABLE+ASSET_OTHER_RECEIVABLE+ASSET_PREPAY+ASSET_INVENTORY+ASSET_CURRENT_OTHER", null, null, null, true, true));

        // --- 非流动资产 ---
         items.add(createItem(templateId, groupid, "ASSET_NON_CURRENT", "非流动资产", "ASSET", 2, 11, "ASSET", null, "FORMULA", "", null, null, null, true, false));
         items.add(createItem(templateId, groupid, "ASSET_LONG_INVEST", "长期股权投资", "ASSET_NON_CURRENT", 3, 12, "ASSET", null, "CUSTOM", "CUSTOM:ASSET_LONG_INVEST", null, "END_BALANCE", null, true, true));
         items.add(createItem(templateId, groupid, "ASSET_FIXED", "固定资产", "ASSET_NON_CURRENT", 3, 13, "ASSET", null, "CUSTOM", "CUSTOM:ASSET_FIXED", null, "END_BALANCE", null, true, true));
         items.add(createItem(templateId, groupid, "ASSET_CONSTRUCTION", "在建工程", "ASSET_NON_CURRENT", 3, 14, "ASSET", null, "CUSTOM", "CUSTOM:ASSET_CONSTRUCTION", null, "END_BALANCE", null, true, true));
         items.add(createItem(templateId, groupid, "ASSET_INTANGIBLE", "无形资产", "ASSET_NON_CURRENT", 3, 15, "ASSET", null, "CUSTOM", "CUSTOM:ASSET_INTANGIBLE", null, "END_BALANCE", null, true, true));
         items.add(createItem(templateId, groupid, "ASSET_DEFERRED", "长期待摊费用", "ASSET_NON_CURRENT", 3, 16, "ASSET", null, "CUSTOM", "CUSTOM:ASSET_DEFERRED", null, "END_BALANCE", null, true, true));
        items.add(createItem(templateId, groupid, "ASSET_NON_CURRENT_OTHER", "其他非流动资产", "ASSET_NON_CURRENT", 3, 17, "ASSET", null, "CUSTOM", "CUSTOM:ASSET_NON_CURRENT_OTHER", null, null, null, true, true));
        items.add(createItem(templateId, groupid, "ASSET_NON_CURRENT_TOTAL", "非流动资产合计", "ASSET", 2, 18, "ASSET", null, "FORMULA", "ASSET_LONG_INVEST+ASSET_FIXED+ASSET_CONSTRUCTION+ASSET_INTANGIBLE+ASSET_DEFERRED+ASSET_NON_CURRENT_OTHER", null, null, null, true, true));

        // --- 资产总计（必须排在两个合计项之后）---
        items.add(createItem(templateId, groupid, "ASSET", "资产", null, 2, 19, "ASSET", null, "FORMULA", "ASSET_CURRENT_TOTAL+ASSET_NON_CURRENT_TOTAL", null, null, null, true, true));

        // ===== 负债和所有者权益部分 =====

        // --- 流动负债 ---
         items.add(createItem(templateId, groupid, "LIABILITY_CURRENT", "流动负债", "LIABILITY", 3, 21, "LIABILITY", null, "FORMULA", "", null, null, null, true, false));
         items.add(createItem(templateId, groupid, "LIABILITY_SHORT_LOAN", "短期借款", "LIABILITY_CURRENT", 4, 22, "LIABILITY", null, "CUSTOM", "CUSTOM:LIABILITY_SHORT_LOAN", null, "END_BALANCE", null, true, true));
         items.add(createItem(templateId, groupid, "LIABILITY_BILLS", "应付票据", "LIABILITY_CURRENT", 4, 23, "LIABILITY", null, "CUSTOM", "CUSTOM:LIABILITY_BILLS", null, "END_BALANCE", null, true, true));
         items.add(createItem(templateId, groupid, "LIABILITY_PAYABLE", "应付账款", "LIABILITY_CURRENT", 4, 24, "LIABILITY", null, "CUSTOM", "CUSTOM:LIABILITY_PAYABLE", null, "END_BALANCE", null, true, true));
         items.add(createItem(templateId, groupid, "LIABILITY_ADVANCE", "预收款项", "LIABILITY_CURRENT", 4, 25, "LIABILITY", null, "CUSTOM", "CUSTOM:LIABILITY_ADVANCE", null, "END_BALANCE", null, true, true));
         items.add(createItem(templateId, groupid, "LIABILITY_SALARY", "应付职工薪酬", "LIABILITY_CURRENT", 4, 26, "LIABILITY", null, "CUSTOM", "CUSTOM:LIABILITY_SALARY", null, "END_BALANCE", null, true, true));
         items.add(createItem(templateId, groupid, "LIABILITY_TAX", "应交税费", "LIABILITY_CURRENT", 4, 27, "LIABILITY", null, "CUSTOM", "CUSTOM:LIABILITY_TAX", null, "END_BALANCE", null, true, true));
         items.add(createItem(templateId, groupid, "LIABILITY_CURRENT_OTHER", "其他流动负债", "LIABILITY_CURRENT", 4, 28, "LIABILITY", null, "CUSTOM", "CUSTOM:LIABILITY_CURRENT_OTHER", null, null, null, true, true));
        items.add(createItem(templateId, groupid, "LIABILITY_CURRENT_TOTAL", "流动负债合计", "LIABILITY", 3, 29, "LIABILITY", null, "FORMULA", "LIABILITY_SHORT_LOAN+LIABILITY_BILLS+LIABILITY_PAYABLE+LIABILITY_ADVANCE+LIABILITY_SALARY+LIABILITY_TAX+LIABILITY_CURRENT_OTHER", null, null, null, true, true));

        // --- 非流动负债 ---
         items.add(createItem(templateId, groupid, "LIABILITY_NON_CURRENT", "非流动负债", "LIABILITY", 3, 30, "LIABILITY", null, "FORMULA", "", null, null, null, true, false));
         items.add(createItem(templateId, groupid, "LIABILITY_LONG_LOAN", "长期借款", "LIABILITY_NON_CURRENT", 4, 31, "LIABILITY", null, "CUSTOM", "CUSTOM:LIABILITY_LONG_LOAN", null, "END_BALANCE", null, true, true));
         items.add(createItem(templateId, groupid, "LIABILITY_BONDS", "应付债券", "LIABILITY_NON_CURRENT", 4, 32, "LIABILITY", null, "CUSTOM", "CUSTOM:LIABILITY_BONDS", null, "END_BALANCE", null, true, true));
        items.add(createItem(templateId, groupid, "LIABILITY_NON_CURRENT_OTHER", "其他非流动负债", "LIABILITY_NON_CURRENT", 4, 33, "LIABILITY", null, "CUSTOM", "CUSTOM:LIABILITY_NON_CURRENT_OTHER", null, null, null, true, true));
        items.add(createItem(templateId, groupid, "LIABILITY_NON_CURRENT_TOTAL", "非流动负债合计", "LIABILITY", 3, 34, "LIABILITY", null, "FORMULA", "LIABILITY_LONG_LOAN+LIABILITY_BONDS+LIABILITY_NON_CURRENT_OTHER", null, null, null, true, true));

        // --- 负债合计（必须排在两个合计项之后）---
        items.add(createItem(templateId, groupid, "LIABILITY", "负债", "LIABILITY_EQUITY", 2, 35, "LIABILITY", null, "FORMULA", "LIABILITY_CURRENT_TOTAL+LIABILITY_NON_CURRENT_TOTAL", null, null, null, true, true));

        // ===== 权益部分 =====
         items.add(createItem(templateId, groupid, "EQUITY", "所有者权益", "LIABILITY_EQUITY", 2, 36, "EQUITY", null, "FORMULA", "", null, null, null, true, false));
         items.add(createItem(templateId, groupid, "EQUITY_CAPITAL", "实收资本", "EQUITY", 3, 37, "EQUITY", null, "CUSTOM", "CUSTOM:EQUITY_CAPITAL", null, "END_BALANCE", null, true, true));
         items.add(createItem(templateId, groupid, "EQUITY_RESERVE", "资本公积", "EQUITY", 3, 38, "EQUITY", null, "CUSTOM", "CUSTOM:EQUITY_RESERVE", null, "END_BALANCE", null, true, true));
         items.add(createItem(templateId, groupid, "EQUITY_SURPLUS", "盈余公积", "EQUITY", 3, 39, "EQUITY", null, "CUSTOM", "CUSTOM:EQUITY_SURPLUS", null, "END_BALANCE", null, true, true));
         items.add(createItem(templateId, groupid, "EQUITY_RETAINED", "未分配利润", "EQUITY", 3, 40, "EQUITY", null, "CUSTOM", "CUSTOM:EQUITY_RETAINED", null, "END_BALANCE", null, true, true));
        items.add(createItem(templateId, groupid, "EQUITY_OTHER", "其他权益", "EQUITY", 3, 41, "EQUITY", null, "CUSTOM", "CUSTOM:EQUITY_OTHER", null, null, null, true, true));
        items.add(createItem(templateId, groupid, "EQUITY_TOTAL", "权益合计", "EQUITY", 3, 42, "EQUITY", null, "FORMULA", "EQUITY_CAPITAL+EQUITY_RESERVE+EQUITY_SURPLUS+EQUITY_RETAINED+EQUITY_OTHER", null, null, null, true, true));

        // --- 负债和权益总计 ---
        items.add(createItem(templateId, groupid, "LIABILITY_EQUITY_TOTAL", "负债和权益总计", null, 2, 43, "LIABILITY", null, "FORMULA", "LIABILITY+EQUITY_TOTAL", null, null, null, true, true));
        
        return items;
    }
    
    /**
     * 创建默认映射规则
     */
    private void createDefaultMappingRules(String groupid, String templateType)
    {
        List<FinReportMappingRules> rules = new ArrayList<>();
        switch (templateType)
        {
            case "BALANCE_SHEET":
                rules = createBalanceSheetMappingRules(groupid);
                break;
            case "INCOME_STATEMENT":
                rules = createIncomeStatementMappingRules(groupid);
                break;
            case "CASH_FLOW":
                rules = createCashFlowMappingRules(groupid);
                break;
            case "EQUITY_CHANGE":
                rules = createEquityChangeMappingRules(groupid);
                break;
            default:
                break;
        }
        for (FinReportMappingRules rule : rules)
        {
            finReportMappingRulesService.insertFinReportMappingRules(rule);
        }
        log.info("创建了 {} 条默认映射规则", rules.size());
    }
    
    /**
     * 创建资产负债表默认映射规则（PREFIX 类型，自动包含子科目）
     */
    private List<FinReportMappingRules> createBalanceSheetMappingRules(String groupid)
    {
        List<FinReportMappingRules> rules = new ArrayList<>();
        int priority = 1;
        
        // 流动资产 (direction=1 借方)
        rules.add(createRule(groupid, "BALANCE_SHEET", "ASSET_CASH", "库存现金", "PREFIX", "1001", "ADD", priority++, 1));
        rules.add(createRule(groupid, "BALANCE_SHEET", "ASSET_CASH", "银行存款", "PREFIX", "1002", "ADD", priority++, 1));
        rules.add(createRule(groupid, "BALANCE_SHEET", "ASSET_CASH", "其他货币资金", "PREFIX", "1012", "ADD", priority++, 1));
        rules.add(createRule(groupid, "BALANCE_SHEET", "ASSET_FINANCIAL", "交易性金融资产", "PREFIX", "1101", "ADD", priority++, 1));
        rules.add(createRule(groupid, "BALANCE_SHEET", "ASSET_BILLS", "应收票据", "PREFIX", "1121", "ADD", priority++, 1));
        rules.add(createRule(groupid, "BALANCE_SHEET", "ASSET_RECEIVABLE", "应收账款", "PREFIX", "1122", "ADD", priority++, 1));
        rules.add(createRule(groupid, "BALANCE_SHEET", "ASSET_OTHER_RECEIVABLE", "其他应收款", "PREFIX", "1221", "ADD", priority++, 1));
        rules.add(createRule(groupid, "BALANCE_SHEET", "ASSET_RECEIVABLE", "坏账准备(减)", "PREFIX", "1231", "SUBTRACT", priority++, 2));
        rules.add(createRule(groupid, "BALANCE_SHEET", "ASSET_PREPAY", "预付款项", "PREFIX", "1123", "ADD", priority++, 1));
        rules.add(createRule(groupid, "BALANCE_SHEET", "ASSET_INVENTORY", "材料采购", "PREFIX", "1401", "ADD", priority++, 1));
        rules.add(createRule(groupid, "BALANCE_SHEET", "ASSET_INVENTORY", "原材料", "PREFIX", "1403", "ADD", priority++, 1));
        rules.add(createRule(groupid, "BALANCE_SHEET", "ASSET_INVENTORY", "库存商品", "PREFIX", "1405", "ADD", priority++, 1));
        rules.add(createRule(groupid, "BALANCE_SHEET", "ASSET_INVENTORY", "发出商品", "PREFIX", "1406", "ADD", priority++, 1));
        rules.add(createRule(groupid, "BALANCE_SHEET", "ASSET_INVENTORY", "委托代销商品", "PREFIX", "1408", "ADD", priority++, 1));
        rules.add(createRule(groupid, "BALANCE_SHEET", "ASSET_INVENTORY", "周转材料", "PREFIX", "1411", "ADD", priority++, 1));
        rules.add(createRule(groupid, "BALANCE_SHEET", "ASSET_INVENTORY", "存货跌价准备(减)", "PREFIX", "1471", "SUBTRACT", priority++, 2));
        
        // 非流动资产 (direction=1 借方)
        rules.add(createRule(groupid, "BALANCE_SHEET", "ASSET_LONG_INVEST", "长期股权投资", "PREFIX", "1511", "ADD", priority++, 1));
        rules.add(createRule(groupid, "BALANCE_SHEET", "ASSET_FIXED", "固定资产", "PREFIX", "1601", "ADD", priority++, 1));
        rules.add(createRule(groupid, "BALANCE_SHEET", "ASSET_FIXED", "累计折旧(减)", "PREFIX", "1602", "SUBTRACT", priority++, 2));
        rules.add(createRule(groupid, "BALANCE_SHEET", "ASSET_FIXED", "固定资产减值准备(减)", "PREFIX", "1603", "SUBTRACT", priority++, 2));
        rules.add(createRule(groupid, "BALANCE_SHEET", "ASSET_CONSTRUCTION", "在建工程", "PREFIX", "1604", "ADD", priority++, 1));
        rules.add(createRule(groupid, "BALANCE_SHEET", "ASSET_INTANGIBLE", "无形资产", "PREFIX", "1701", "ADD", priority++, 1));
        rules.add(createRule(groupid, "BALANCE_SHEET", "ASSET_INTANGIBLE", "累计摊销(减)", "PREFIX", "1702", "SUBTRACT", priority++, 2));
        rules.add(createRule(groupid, "BALANCE_SHEET", "ASSET_INTANGIBLE", "无形资产减值准备(减)", "PREFIX", "1703", "SUBTRACT", priority++, 2));
        rules.add(createRule(groupid, "BALANCE_SHEET", "ASSET_DEFERRED", "长期待摊费用", "PREFIX", "1801", "ADD", priority++, 1));
        
        // 流动负债 (direction=2 贷方)
        rules.add(createRule(groupid, "BALANCE_SHEET", "LIABILITY_SHORT_LOAN", "短期借款", "PREFIX", "2001", "ADD", priority++, 2));
        rules.add(createRule(groupid, "BALANCE_SHEET", "LIABILITY_BILLS", "应付票据", "PREFIX", "2201", "ADD", priority++, 2));
        rules.add(createRule(groupid, "BALANCE_SHEET", "LIABILITY_PAYABLE", "应付账款", "PREFIX", "2202", "ADD", priority++, 2));
        rules.add(createRule(groupid, "BALANCE_SHEET", "LIABILITY_ADVANCE", "预收款项", "PREFIX", "2203", "ADD", priority++, 2));
        rules.add(createRule(groupid, "BALANCE_SHEET", "LIABILITY_SALARY", "应付职工薪酬", "PREFIX", "2211", "ADD", priority++, 2));
        rules.add(createRule(groupid, "BALANCE_SHEET", "LIABILITY_TAX", "应交税费", "PREFIX", "2221", "ADD", priority++, 2));
        
        // 非流动负债 (direction=2 贷方)
        rules.add(createRule(groupid, "BALANCE_SHEET", "LIABILITY_LONG_LOAN", "长期借款", "PREFIX", "2501", "ADD", priority++, 2));
        rules.add(createRule(groupid, "BALANCE_SHEET", "LIABILITY_BONDS", "应付债券", "PREFIX", "2502", "ADD", priority++, 2));
        
        // 权益 (direction=2 贷方)
        rules.add(createRule(groupid, "BALANCE_SHEET", "EQUITY_CAPITAL", "实收资本", "PREFIX", "3001", "ADD", priority++, 2));
        rules.add(createRule(groupid, "BALANCE_SHEET", "EQUITY_RESERVE", "资本公积", "PREFIX", "3002", "ADD", priority++, 2));
        rules.add(createRule(groupid, "BALANCE_SHEET", "EQUITY_SURPLUS", "盈余公积", "PREFIX", "3101", "ADD", priority++, 2));
        rules.add(createRule(groupid, "BALANCE_SHEET", "EQUITY_RETAINED", "本年利润", "PREFIX", "3103", "ADD", priority++, 2));
        rules.add(createRule(groupid, "BALANCE_SHEET", "EQUITY_RETAINED", "未分配利润", "PREFIX", "3104", "ADD", priority++, 2));
        
        return rules;
    }
    
    /**
     * 创建利润表默认映射规则
     * 收入类科目贷方发生额为正数，成本费用类科目借方发生额为正数
     */
    private List<FinReportMappingRules> createIncomeStatementMappingRules(String groupid)
    {
        List<FinReportMappingRules> rules = new ArrayList<>();
        int priority = 1;
        
        // 收入类 (direction=2 贷方，收入科目贷方发生额)
        rules.add(createRule(groupid, "INCOME_STATEMENT", "REVENUE_MAIN", "主营业务收入", "PREFIX", "5001", "ADD", priority++, 2));
        rules.add(createRule(groupid, "INCOME_STATEMENT", "REVENUE_OTHER", "其他业务收入", "PREFIX", "5051", "ADD", priority++, 2));
        
        // 成本类 (direction=1 借方，成本科目借方发生额)
        rules.add(createRule(groupid, "INCOME_STATEMENT", "COST_MAIN", "主营业务成本", "PREFIX", "5401", "ADD", priority++, 1));
        rules.add(createRule(groupid, "INCOME_STATEMENT", "COST_OTHER", "其他业务成本", "PREFIX", "5402", "ADD", priority++, 1));
        
        // 税金及附加 (direction=1 借方)
        rules.add(createRule(groupid, "INCOME_STATEMENT", "TAX_SURCHARGE", "税金及附加", "PREFIX", "5403", "ADD", priority++, 1));
        
        // 期间费用 (direction=1 借方)
        rules.add(createRule(groupid, "INCOME_STATEMENT", "EXPENSE_SELLING", "销售费用", "PREFIX", "5601", "ADD", priority++, 1));
        rules.add(createRule(groupid, "INCOME_STATEMENT", "EXPENSE_ADMIN", "管理费用", "PREFIX", "5602", "ADD", priority++, 1));
        rules.add(createRule(groupid, "INCOME_STATEMENT", "EXPENSE_FINANCIAL", "财务费用", "PREFIX", "5603", "ADD", priority++, 1));
        
        // 投资收益 (direction=2 贷方)
        rules.add(createRule(groupid, "INCOME_STATEMENT", "INCOME_INVESTMENT", "投资收益", "PREFIX", "5111", "ADD", priority++, 2));
        
        // 营业外收入 (direction=2 贷方)
        rules.add(createRule(groupid, "INCOME_STATEMENT", "INCOME_NON_OPERATING", "营业外收入", "PREFIX", "6301", "ADD", priority++, 2));
        
        // 营业外支出 (direction=1 借方)
        rules.add(createRule(groupid, "INCOME_STATEMENT", "EXPENSE_NON_OPERATING", "营业外支出", "PREFIX", "6711", "ADD", priority++, 1));
        
        // 所得税费用 (direction=1 借方)
        rules.add(createRule(groupid, "INCOME_STATEMENT", "EXPENSE_INCOME_TAX", "所得税费用", "PREFIX", "5801", "ADD", priority++, 1));
        
        return rules;
    }

    /**
     * 创建现金流量表默认映射规则
     * 现金流量表应从业务科目取数，而非现金科目
     * 流入项目：映射到产生现金流入的业务科目贷方发生额
     * 流出项目：映射到产生现金流出的业务科目借方发生额
     */
    private List<FinReportMappingRules> createCashFlowMappingRules(String groupid)
    {
        List<FinReportMappingRules> rules = new ArrayList<>();
        int priority = 1;

        // ===== 经营活动现金流入 =====
        // 销售商品、提供劳务收到的现金 → 主营业务收入(贷方发生额)
        rules.add(createRule(groupid, "CASH_FLOW", "CASH_IN_SALES", "主营业务收入", "PREFIX", "5001", "ADD", priority++, 2));
        rules.add(createRule(groupid, "CASH_FLOW", "CASH_IN_SALES", "其他业务收入", "PREFIX", "5051", "ADD", priority++, 2));
        
        // 收到的税费返还 → 应交税费(贷方发生额，表示退税)
        rules.add(createRule(groupid, "CASH_FLOW", "CASH_IN_TAX_REFUND", "应交税费", "PREFIX", "2221", "ADD", priority++, 2));
        
        // 收到其他与经营活动有关的现金 → 其他应收款(贷方减少)、营业外收入
        rules.add(createRule(groupid, "CASH_FLOW", "CASH_IN_OTHER_OPERATING", "其他应收款", "PREFIX", "1221", "ADD", priority++, 2));
        rules.add(createRule(groupid, "CASH_FLOW", "CASH_IN_OTHER_OPERATING", "营业外收入", "PREFIX", "6301", "ADD", priority++, 2));

        // ===== 经营活动现金流出 =====
        // 购买商品、接受劳务支付的现金 → 主营业务成本(借方发生额)
        rules.add(createRule(groupid, "CASH_FLOW", "CASH_OUT_PURCHASE", "主营业务成本", "PREFIX", "5401", "ADD", priority++, 1));
        rules.add(createRule(groupid, "CASH_FLOW", "CASH_OUT_PURCHASE", "其他业务成本", "PREFIX", "5402", "ADD", priority++, 1));
        
        // 支付给职工以及为职工支付的现金 → 应付职工薪酬(借方发生额，表示实际支付)
        rules.add(createRule(groupid, "CASH_FLOW", "CASH_OUT_EMPLOYEE", "应付职工薪酬", "PREFIX", "2211", "ADD", priority++, 1));
        
        // 支付的各项税费 → 应交税费(借方发生额，表示实际缴纳)
        rules.add(createRule(groupid, "CASH_FLOW", "CASH_OUT_TAX", "应交税费", "PREFIX", "2221", "ADD", priority++, 1));
        
        // 支付其他与经营活动有关的现金 → 销售费用、管理费用、财务费用
        rules.add(createRule(groupid, "CASH_FLOW", "CASH_OUT_OTHER_OPERATING", "销售费用", "PREFIX", "5601", "ADD", priority++, 1));
        rules.add(createRule(groupid, "CASH_FLOW", "CASH_OUT_OTHER_OPERATING", "管理费用", "PREFIX", "5602", "ADD", priority++, 1));
        rules.add(createRule(groupid, "CASH_FLOW", "CASH_OUT_OTHER_OPERATING", "财务费用", "PREFIX", "5603", "ADD", priority++, 1));

        // ===== 投资活动现金流入 =====
        // 收回投资收到的现金 → 长期股权投资(贷方)、交易性金融资产(贷方)
        rules.add(createRule(groupid, "CASH_FLOW", "CASH_IN_WITHDRAW", "长期股权投资", "PREFIX", "1511", "ADD", priority++, 2));
        rules.add(createRule(groupid, "CASH_FLOW", "CASH_IN_WITHDRAW", "交易性金融资产", "PREFIX", "1101", "ADD", priority++, 2));
        
        // 取得投资收益收到的现金 → 投资收益(贷方)
        rules.add(createRule(groupid, "CASH_FLOW", "CASH_IN_INVEST_PROFIT", "投资收益", "PREFIX", "5111", "ADD", priority++, 2));

        // ===== 投资活动现金流出 =====
        // 购建固定资产、无形资产支付的现金 → 固定资产(借方)、在建工程(借方)、无形资产(借方)
        rules.add(createRule(groupid, "CASH_FLOW", "CASH_OUT_LONG_ASSET", "固定资产", "PREFIX", "1601", "ADD", priority++, 1));
        rules.add(createRule(groupid, "CASH_FLOW", "CASH_OUT_LONG_ASSET", "在建工程", "PREFIX", "1604", "ADD", priority++, 1));
        rules.add(createRule(groupid, "CASH_FLOW", "CASH_OUT_LONG_ASSET", "无形资产", "PREFIX", "1701", "ADD", priority++, 1));
        
        // 投资支付的现金 → 长期股权投资(借方)、交易性金融资产(借方)
        rules.add(createRule(groupid, "CASH_FLOW", "CASH_OUT_INVEST", "长期股权投资", "PREFIX", "1511", "ADD", priority++, 1));
        rules.add(createRule(groupid, "CASH_FLOW", "CASH_OUT_INVEST", "交易性金融资产", "PREFIX", "1101", "ADD", priority++, 1));

        // ===== 筹资活动现金流入 =====
        // 吸收投资收到的现金 → 实收资本(贷方)、资本公积(贷方)
        rules.add(createRule(groupid, "CASH_FLOW", "CASH_IN_CAPITAL", "实收资本", "PREFIX", "3001", "ADD", priority++, 2));
        rules.add(createRule(groupid, "CASH_FLOW", "CASH_IN_CAPITAL", "资本公积", "PREFIX", "3002", "ADD", priority++, 2));
        
        // 取得借款收到的现金 → 短期借款(贷方)、长期借款(贷方)
        rules.add(createRule(groupid, "CASH_FLOW", "CASH_IN_LOAN", "短期借款", "PREFIX", "2001", "ADD", priority++, 2));
        rules.add(createRule(groupid, "CASH_FLOW", "CASH_IN_LOAN", "长期借款", "PREFIX", "2501", "ADD", priority++, 2));

        // ===== 筹资活动现金流出 =====
        // 偿还债务支付的现金 → 短期借款(借方)、长期借款(借方)
        rules.add(createRule(groupid, "CASH_FLOW", "CASH_OUT_DEBT", "短期借款", "PREFIX", "2001", "ADD", priority++, 1));
        rules.add(createRule(groupid, "CASH_FLOW", "CASH_OUT_DEBT", "长期借款", "PREFIX", "2501", "ADD", priority++, 1));
        
        // 分配股利、利润支付的现金 → 应付股利、利润分配
        rules.add(createRule(groupid, "CASH_FLOW", "CASH_OUT_DIVIDEND", "应付股利", "PREFIX", "2232", "ADD", priority++, 1));
        rules.add(createRule(groupid, "CASH_FLOW", "CASH_OUT_DIVIDEND", "利润分配", "PREFIX", "3104", "ADD", priority++, 1));

        return rules;
    }

    /**
     * 创建所有者权益变动表默认映射规则
     * 期初/期末使用 DIRECT 直接取科目余额，不需要映射规则
     * 变动项目使用 CUSTOM，通过映射规则计算发生额
     * 科目：实收资本(3001)、资本公积(3002)、盈余公积(3101)、本年利润(3103)、利润分配(3104)
     */
    private List<FinReportMappingRules> createEquityChangeMappingRules(String groupid)
    {
        List<FinReportMappingRules> rules = new ArrayList<>();
        int priority = 1;

        // 综合收益总额（本年利润贷方余额）
        rules.add(createRule(groupid, "EQUITY_CHANGE", "CHANGE_PROFIT_NET", "综合收益总额", "PREFIX", "3103", "ADD", priority++, 2));

        // 所有者投入资本（实收资本贷方发生额）
        rules.add(createRule(groupid, "EQUITY_CHANGE", "CHANGE_CAPITAL_IN", "所有者投入资本", "PREFIX", "3001", "ADD", priority++, 2));

        // 所有者减少资本（实收资本借方发生额）
        rules.add(createRule(groupid, "EQUITY_CHANGE", "CHANGE_CAPITAL_REDUCE", "所有者减少资本", "PREFIX", "3001", "ADD", priority++, 1));

        // 提取盈余公积（盈余公积增加，从未分配利润转入）
        rules.add(createRule(groupid, "EQUITY_CHANGE", "CHANGE_SURPLUS_EXTRACT", "提取盈余公积", "PREFIX", "3101", "ADD", priority++, 2));

        // 对所有者的分配（未分配利润减少）
        rules.add(createRule(groupid, "EQUITY_CHANGE", "CHANGE_DIVIDEND", "对所有者的分配", "PREFIX", "3104", "ADD", priority++, 1));

        // 资本公积转增资本（资本公积减少）
        rules.add(createRule(groupid, "EQUITY_CHANGE", "CHANGE_RESERVE_TO_CAPITAL", "资本公积转增资本", "PREFIX", "3002", "ADD", priority++, 1));

        // 盈余公积转增资本（盈余公积减少）
        rules.add(createRule(groupid, "EQUITY_CHANGE", "CHANGE_SURPLUS_TO_CAPITAL", "盈余公积转增资本", "PREFIX", "3101", "ADD", priority++, 1));

        // 盈余公积弥补亏损（盈余公积减少）
        rules.add(createRule(groupid, "EQUITY_CHANGE", "CHANGE_SURPLUS_TO_LOSS", "盈余公积弥补亏损", "PREFIX", "3101", "ADD", priority++, 1));

        // 其他内部结转（用户可自定义映射）
        rules.add(createRule(groupid, "EQUITY_CHANGE", "CHANGE_OTHER_TRANSFER", "其他内部结转", "PREFIX", "3104", "ADD", priority++, 2));

        return rules;
    }
    
    private FinReportMappingRules createRule(String groupid, String templateType, String itemCode,
                                              String ruleName, String matchType, String matchValue,
                                              String operator, int priority)
    {
        return createRule(groupid, templateType, itemCode, ruleName, matchType, matchValue, operator, priority, null);
    }

    private FinReportMappingRules createRule(String groupid, String templateType, String itemCode,
                                              String ruleName, String matchType, String matchValue,
                                              String operator, int priority, Integer direction)
    {
        FinReportMappingRules rule = new FinReportMappingRules();
        rule.setGroupid(groupid);
        rule.setTemplateType(templateType);
        rule.setRuleType("SUBJECT");
        rule.setItemCode(itemCode);
        rule.setRuleName(ruleName);
        rule.setMatchType(matchType);
        rule.setMatchValue(matchValue);
        rule.setOperator(operator);
        rule.setPriority(priority);
        rule.setDirection(direction);
        rule.setStatus(1);
        return rule;
    }
    
    /**
     * 创建利润表默认项目
     */
    private List<FinReportItems> createIncomeStatementItems(Long templateId, String groupid)
    {
        List<FinReportItems> items = new ArrayList<>();
        int lineNumber = 1;
        
        // 收入类 (INCOME使用SUM(CHILDREN)汇总所有子项，无需额外的合计项)
        items.add(createItem(templateId, groupid, "INCOME", "收入", null, 1, lineNumber++, "INCOME", null, "CALCULATED", "SUM(CHILDREN)", null, null, null, true, false));
        items.add(createItem(templateId, groupid, "INCOME_MAIN", "主营业务收入", "INCOME", 2, lineNumber++, "INCOME", null, "CUSTOM", "CUSTOM:REVENUE_MAIN", null, "END_BALANCE", null, true, true));
        items.add(createItem(templateId, groupid, "INCOME_OTHER", "其他业务收入", "INCOME", 2, lineNumber++, "INCOME", null, "CUSTOM", "CUSTOM:REVENUE_OTHER", null, "END_BALANCE", null, true, true));
        
        // 成本类 (COST使用SUM(CHILDREN)汇总所有子项，无需额外的合计项)
        items.add(createItem(templateId, groupid, "COST", "成本", null, 1, lineNumber++, "COST", null, "CALCULATED", "SUM(CHILDREN)", null, null, null, true, false));
        items.add(createItem(templateId, groupid, "COST_MAIN", "主营业务成本", "COST", 2, lineNumber++, "COST", null, "CUSTOM", "CUSTOM:COST_MAIN", null, "END_BALANCE", null, true, true));
        items.add(createItem(templateId, groupid, "COST_OTHER", "其他业务成本", "COST", 2, lineNumber++, "COST", null, "CUSTOM", "CUSTOM:COST_OTHER", null, "END_BALANCE", null, true, true));
        
        // 税金及附加
        items.add(createItem(templateId, groupid, "TAX_ADDITIONAL", "税金及附加", null, 1, lineNumber++, "TAX", null, "CUSTOM", "CUSTOM:TAX_SURCHARGE", null, "END_BALANCE", null, true, true));
        
        // 期间费用 (EXPENSE使用SUM(CHILDREN)汇总所有子项，无需额外的合计项)
        items.add(createItem(templateId, groupid, "EXPENSE", "期间费用", null, 1, lineNumber++, "EXPENSE", null, "CALCULATED", "SUM(CHILDREN)", null, null, null, true, false));
        items.add(createItem(templateId, groupid, "EXPENSE_SALES", "销售费用", "EXPENSE", 2, lineNumber++, "EXPENSE", null, "CUSTOM", "CUSTOM:EXPENSE_SELLING", null, "END_BALANCE", null, true, true));
        items.add(createItem(templateId, groupid, "EXPENSE_MANAGE", "管理费用", "EXPENSE", 2, lineNumber++, "EXPENSE", null, "CUSTOM", "CUSTOM:EXPENSE_ADMIN", null, "END_BALANCE", null, true, true));
        items.add(createItem(templateId, groupid, "EXPENSE_FINANCE", "财务费用", "EXPENSE", 2, lineNumber++, "EXPENSE", null, "CUSTOM", "CUSTOM:EXPENSE_FINANCIAL", null, "END_BALANCE", null, true, true));
        
        // 其他收益
        items.add(createItem(templateId, groupid, "INCOME_INVESTMENT", "投资收益", null, 1, lineNumber++, "INCOME", null, "CUSTOM", "CUSTOM:INCOME_INVESTMENT", null, "END_BALANCE", null, true, true));
        
        // 营业利润 (保持FORMULA，因为涉及加减混合运算)
        items.add(createItem(templateId, groupid, "PROFIT_OPERATING", "营业利润", null, 1, lineNumber++, "PROFIT", null, "FORMULA", "INCOME-COST-TAX_ADDITIONAL-EXPENSE+INCOME_INVESTMENT", null, null, null, true, false));
        
        // 营业外收支
        items.add(createItem(templateId, groupid, "INCOME_NON_OPERATING", "营业外收入", null, 1, lineNumber++, "INCOME", null, "CUSTOM", "CUSTOM:INCOME_NON_OPERATING", null, "END_BALANCE", null, true, true));
        items.add(createItem(templateId, groupid, "EXPENSE_NON_OPERATING", "营业外支出", null, 1, lineNumber++, "EXPENSE", null, "CUSTOM", "CUSTOM:EXPENSE_NON_OPERATING", null, "END_BALANCE", null, true, true));
        items.add(createItem(templateId, groupid, "INCOME_EXPENSE_NON_OPERATING_NET", "营业外收支净额", null, 1, lineNumber++, "INCOME", null, "FORMULA", "INCOME_NON_OPERATING-EXPENSE_NON_OPERATING", null, null, null, true, false));
        
        // 利润总额
        items.add(createItem(templateId, groupid, "PROFIT_TOTAL", "利润总额", null, 1, lineNumber++, "PROFIT", null, "FORMULA", "PROFIT_OPERATING+INCOME_EXPENSE_NON_OPERATING_NET", null, null, null, true, false));
        
        // 所得税费用
        items.add(createItem(templateId, groupid, "EXPENSE_INCOME_TAX", "所得税费用", null, 1, lineNumber++, "EXPENSE", null, "CUSTOM", "CUSTOM:EXPENSE_INCOME_TAX", null, "END_BALANCE", null, true, true));
        
        // 净利润
        items.add(createItem(templateId, groupid, "PROFIT_NET", "净利润", null, 1, lineNumber++, "PROFIT", null, "FORMULA", "PROFIT_TOTAL-EXPENSE_INCOME_TAX", null, null, null, true, false));
        
        // 每股收益
        items.add(createItem(templateId, groupid, "EPS_BASIC", "基本每股收益", null, 1, lineNumber++, "EPS", null, "CUSTOM", "CUSTOM:EPS_BASIC", null, null, null, true, false));
        items.add(createItem(templateId, groupid, "EPS_DILUTED", "稀释每股收益", null, 1, lineNumber++, "EPS", null, "CUSTOM", "CUSTOM:EPS_DILUTED", null, null, null, true, false));
        
        return items;
    }
    
    /**
     * 创建现金流量表默认项目
     */
    private List<FinReportItems> createCashFlowItems(Long templateId, String groupid)
    {
        List<FinReportItems> items = new ArrayList<>();
        int lineNumber = 1;
        
        // 经营活动现金流量 (流入-流出=净额)
        items.add(createItem(templateId, groupid, "CASH_OPERATING", "经营活动现金流量", null, 1, lineNumber++, "CASH", null, "FORMULA", "CASH_IN_SALES+CASH_IN_TAX_REFUND+CASH_IN_OTHER_OPERATING-CASH_OUT_PURCHASE-CASH_OUT_EMPLOYEE-CASH_OUT_TAX-CASH_OUT_OTHER_OPERATING", null, null, null, true, false));
        items.add(createItem(templateId, groupid, "CASH_IN_SALES", "销售商品、提供劳务收到的现金", "CASH_OPERATING", 2, lineNumber++, "CASH", null, "CUSTOM", "CUSTOM:CASH_IN_SALES", null, null, null, true, false));
        items.add(createItem(templateId, groupid, "CASH_IN_TAX_REFUND", "收到的税费返还", "CASH_OPERATING", 2, lineNumber++, "CASH", null, "CUSTOM", "CUSTOM:CASH_IN_TAX_REFUND", null, null, null, true, false));
        items.add(createItem(templateId, groupid, "CASH_IN_OTHER_OPERATING", "收到其他与经营活动有关的现金", "CASH_OPERATING", 2, lineNumber++, "CASH", null, "CUSTOM", "CUSTOM:CASH_IN_OTHER_OPERATING", null, null, null, true, false));
        items.add(createItem(templateId, groupid, "CASH_OUT_PURCHASE", "购买商品、接受劳务支付的现金", "CASH_OPERATING", 2, lineNumber++, "CASH", null, "CUSTOM", "CUSTOM:CASH_OUT_PURCHASE", null, null, null, true, false));
        items.add(createItem(templateId, groupid, "CASH_OUT_EMPLOYEE", "支付给职工以及为职工支付的现金", "CASH_OPERATING", 2, lineNumber++, "CASH", null, "CUSTOM", "CUSTOM:CASH_OUT_EMPLOYEE", null, null, null, true, false));
        items.add(createItem(templateId, groupid, "CASH_OUT_TAX", "支付的各项税费", "CASH_OPERATING", 2, lineNumber++, "CASH", null, "CUSTOM", "CUSTOM:CASH_OUT_TAX", null, null, null, true, false));
        items.add(createItem(templateId, groupid, "CASH_OUT_OTHER_OPERATING", "支付其他与经营活动有关的现金", "CASH_OPERATING", 2, lineNumber++, "CASH", null, "CUSTOM", "CUSTOM:CASH_OUT_OTHER_OPERATING", null, null, null, true, false));
        
        // 投资活动现金流量 (流入-流出=净额)
        items.add(createItem(templateId, groupid, "CASH_INVESTING", "投资活动现金流量", null, 1, lineNumber++, "CASH", null, "FORMULA", "CASH_IN_WITHDRAW+CASH_IN_INVEST_PROFIT-CASH_OUT_LONG_ASSET-CASH_OUT_INVEST", null, null, null, true, false));
        items.add(createItem(templateId, groupid, "CASH_IN_WITHDRAW", "收回投资收到的现金", "CASH_INVESTING", 2, lineNumber++, "CASH", null, "CUSTOM", "CUSTOM:CASH_IN_WITHDRAW", null, null, null, true, false));
        items.add(createItem(templateId, groupid, "CASH_IN_INVEST_PROFIT", "取得投资收益收到的现金", "CASH_INVESTING", 2, lineNumber++, "CASH", null, "CUSTOM", "CUSTOM:CASH_IN_INVEST_PROFIT", null, null, null, true, false));
        items.add(createItem(templateId, groupid, "CASH_OUT_LONG_ASSET", "购建固定资产、无形资产和其他长期资产支付的现金", "CASH_INVESTING", 2, lineNumber++, "CASH", null, "CUSTOM", "CUSTOM:CASH_OUT_LONG_ASSET", null, null, null, true, false));
        items.add(createItem(templateId, groupid, "CASH_OUT_INVEST", "投资支付的现金", "CASH_INVESTING", 2, lineNumber++, "CASH", null, "CUSTOM", "CUSTOM:CASH_OUT_INVEST", null, null, null, true, false));
        
        // 筹资活动现金流量 (流入-流出=净额)
        items.add(createItem(templateId, groupid, "CASH_FINANCING", "筹资活动现金流量", null, 1, lineNumber++, "CASH", null, "FORMULA", "CASH_IN_CAPITAL+CASH_IN_LOAN-CASH_OUT_DEBT-CASH_OUT_DIVIDEND", null, null, null, true, false));
        items.add(createItem(templateId, groupid, "CASH_IN_CAPITAL", "吸收投资收到的现金", "CASH_FINANCING", 2, lineNumber++, "CASH", null, "CUSTOM", "CUSTOM:CASH_IN_CAPITAL", null, null, null, true, false));
        items.add(createItem(templateId, groupid, "CASH_IN_LOAN", "取得借款收到的现金", "CASH_FINANCING", 2, lineNumber++, "CASH", null, "CUSTOM", "CUSTOM:CASH_IN_LOAN", null, null, null, true, false));
        items.add(createItem(templateId, groupid, "CASH_OUT_DEBT", "偿还债务支付的现金", "CASH_FINANCING", 2, lineNumber++, "CASH", null, "CUSTOM", "CUSTOM:CASH_OUT_DEBT", null, null, null, true, false));
        items.add(createItem(templateId, groupid, "CASH_OUT_DIVIDEND", "分配股利、利润或偿付利息支付的现金", "CASH_FINANCING", 2, lineNumber++, "CASH", null, "CUSTOM", "CUSTOM:CASH_OUT_DIVIDEND", null, null, null, true, false));
        
        // 汇率变动影响
        items.add(createItem(templateId, groupid, "CASH_EXCHANGE_RATE", "汇率变动对现金的影响", null, 1, lineNumber++, "CASH", null, "CUSTOM", "CUSTOM:CASH_EXCHANGE_RATE", null, null, null, true, false));
        
        // 现金净增加额
        items.add(createItem(templateId, groupid, "CASH_NET_INCREASE", "现金净增加额", null, 1, lineNumber++, "CASH", null, "FORMULA", "CASH_OPERATING+CASH_INVESTING+CASH_FINANCING+CASH_EXCHANGE_RATE", null, null, null, true, false));
        
        return items;
    }
    
    /**
     * 创建所有者权益变动表默认项目
     */
    private List<FinReportItems> createEquityChangeItems(Long templateId, String groupid)
    {
        List<FinReportItems> items = new ArrayList<>();
        int lineNumber = 1;
        
        // ===== 一、期初余额 (BEGINNING使用SUM(CHILDREN)汇总所有子项，无需额外的合计项)
        items.add(createItem(templateId, groupid, "BEGINNING", "一、期初余额", null, 1, lineNumber++, "EQUITY", null, "CALCULATED", "SUM(CHILDREN)", null, null, null, true, false));
        items.add(createItem(templateId, groupid, "BEGIN_CAPITAL", "　1. 实收资本（或股本）期初", "BEGINNING", 2, lineNumber++, "EQUITY", null, "DIRECT", "3001", null, "END_BALANCE", null, true, true));
        items.add(createItem(templateId, groupid, "BEGIN_RESERVE", "　2. 资本公积期初", "BEGINNING", 2, lineNumber++, "EQUITY", null, "DIRECT", "3002", null, "END_BALANCE", null, true, true));
        items.add(createItem(templateId, groupid, "BEGIN_SURPLUS", "　3. 盈余公积期初", "BEGINNING", 2, lineNumber++, "EQUITY", null, "DIRECT", "3101", null, "END_BALANCE", null, true, true));
        items.add(createItem(templateId, groupid, "BEGIN_RETAINED", "　4. 未分配利润期初", "BEGINNING", 2, lineNumber++, "EQUITY", null, "DIRECT", "3104", null, "END_BALANCE", null, true, true));
        
        // ===== 二、本年增减变动 (CHANGES使用SUM(CHILDREN)汇总所有子项，无需额外的合计项)
        items.add(createItem(templateId, groupid, "CHANGES", "二、本年增减变动", null, 1, lineNumber++, "EQUITY", null, "CALCULATED", "SUM(CHILDREN)", null, null, null, true, false));
        
        // （一）综合收益总额
        items.add(createItem(templateId, groupid, "CHANGE_PROFIT_NET", "（一）综合收益总额", "CHANGES", 2, lineNumber++, "EQUITY", null, "CUSTOM", "CUSTOM:CHANGE_PROFIT_NET", null, null, null, true, true));
        
        // （二）所有者投入和减少资本
        items.add(createItem(templateId, groupid, "CHANGE_CAPITAL_IN", "　1. 所有者投入资本", "CHANGES", 2, lineNumber++, "EQUITY", null, "CUSTOM", "CUSTOM:CHANGE_CAPITAL_IN", null, null, null, true, true));
        items.add(createItem(templateId, groupid, "CHANGE_CAPITAL_REDUCE", "　2. 所有者减少资本", "CHANGES", 2, lineNumber++, "EQUITY", null, "CUSTOM", "CUSTOM:CHANGE_CAPITAL_REDUCE", null, null, null, true, true));
        
        // （三）利润分配
        items.add(createItem(templateId, groupid, "CHANGE_SURPLUS_EXTRACT", "　1. 提取盈余公积", "CHANGES", 2, lineNumber++, "EQUITY", null, "CUSTOM", "CUSTOM:CHANGE_SURPLUS_EXTRACT", null, null, null, true, true));
        items.add(createItem(templateId, groupid, "CHANGE_DIVIDEND", "　2. 对所有者（或股东）的分配", "CHANGES", 2, lineNumber++, "EQUITY", null, "CUSTOM", "CUSTOM:CHANGE_DIVIDEND", null, null, null, true, true));
        
        // （四）所有者权益内部结转
        items.add(createItem(templateId, groupid, "CHANGE_RESERVE_TO_CAPITAL", "　1. 资本公积转增资本（或股本）", "CHANGES", 2, lineNumber++, "EQUITY", null, "CUSTOM", "CUSTOM:CHANGE_RESERVE_TO_CAPITAL", null, null, null, true, true));
        items.add(createItem(templateId, groupid, "CHANGE_SURPLUS_TO_CAPITAL", "　2. 盈余公积转增资本（或股本）", "CHANGES", 2, lineNumber++, "EQUITY", null, "CUSTOM", "CUSTOM:CHANGE_SURPLUS_TO_CAPITAL", null, null, null, true, true));
        items.add(createItem(templateId, groupid, "CHANGE_SURPLUS_TO_LOSS", "　3. 盈余公积弥补亏损", "CHANGES", 2, lineNumber++, "EQUITY", null, "CUSTOM", "CUSTOM:CHANGE_SURPLUS_TO_LOSS", null, null, null, true, true));
        items.add(createItem(templateId, groupid, "CHANGE_OTHER_TRANSFER", "　4. 其他内部结转", "CHANGES", 2, lineNumber++, "EQUITY", null, "CUSTOM", "CUSTOM:CHANGE_OTHER_TRANSFER", null, null, null, true, true));
        
        // ===== 三、期末余额 (ENDING使用SUM(CHILDREN)汇总所有子项，无需额外的合计项)
        items.add(createItem(templateId, groupid, "ENDING", "三、期末余额", null, 1, lineNumber++, "EQUITY", null, "CALCULATED", "SUM(CHILDREN)", null, null, null, true, false));
        items.add(createItem(templateId, groupid, "END_CAPITAL", "　1. 实收资本（或股本）期末", "ENDING", 2, lineNumber++, "EQUITY", null, "DIRECT", "3001", null, "END_BALANCE", null, true, true));
        items.add(createItem(templateId, groupid, "END_RESERVE", "　2. 资本公积期末", "ENDING", 2, lineNumber++, "EQUITY", null, "DIRECT", "3002", null, "END_BALANCE", null, true, true));
        items.add(createItem(templateId, groupid, "END_SURPLUS", "　3. 盈余公积期末", "ENDING", 2, lineNumber++, "EQUITY", null, "DIRECT", "3101", null, "END_BALANCE", null, true, true));
        items.add(createItem(templateId, groupid, "END_RETAINED", "　4. 未分配利润期末", "ENDING", 2, lineNumber++, "EQUITY", null, "DIRECT", "3104", null, "END_BALANCE", null, true, true));
        
        return items;
    }
    
    /**
     * 创建单个报表项目
     */
    private FinReportItems createItem(Long templateId, String groupid, String itemCode, String itemName,
                                       String parentCode, Integer itemLevel, Integer lineNumber,
                                       String itemType, String itemCategory, String formulaType,
                                       String calculationRule, String formulaContent, String amountType,
                                       String direction, Boolean isShow, Boolean isLeaf)
    {
        FinReportItems item = new FinReportItems();
        item.setTemplateId(templateId);
        item.setGroupid(groupid);
        item.setItemCode(itemCode);
        item.setItemName(itemName);
        item.setParentCode(parentCode);
        item.setItemLevel(itemLevel);
        item.setLineNumber(lineNumber);
        item.setItemType(itemType);
        item.setItemCategory(itemCategory);
        item.setFormulaType(formulaType);
        item.setCalculationRule(calculationRule);
        item.setFormulaContent(formulaContent);
        item.setDataSource(formulaType.equals("DIRECT") ? "SUBJECT" : (formulaType.equals("CUSTOM") ? "CUSTOM" : null));
        item.setSubjectCodes(formulaType.equals("DIRECT") ? calculationRule : null);
        item.setAmountType(amountType != null ? amountType : "END_BALANCE");
        item.setDirection(direction);
        item.setDisplayFormat("NORMAL");
        item.setIsShowZero(true);
        item.setIsShow(isShow);
        item.setIsLeaf(isLeaf);
        item.setSortOrder(lineNumber);
        item.setStatus(1);
        
        return item;
    }
}