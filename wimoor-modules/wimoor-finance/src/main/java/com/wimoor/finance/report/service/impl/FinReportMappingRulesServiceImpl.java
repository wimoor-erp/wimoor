package com.wimoor.finance.report.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.wimoor.finance.report.domain.FinReportMappingRules;
import com.wimoor.finance.report.mapper.FinReportMappingRulesMapper;
import com.wimoor.finance.report.service.IFinReportMappingRulesService;

/**
 * 报表映射规则Service业务层处理
 *
 * @author wimoor
 * @date 2025-06-08
 */
@Service
public class FinReportMappingRulesServiceImpl implements IFinReportMappingRulesService
{
    @Autowired
    private FinReportMappingRulesMapper finReportMappingRulesMapper;

    /**
     * 查询报表映射规则
     *
     * @param ruleId 规则ID
     * @return 报表映射规则
     */
    @Override
    public FinReportMappingRules selectFinReportMappingRulesByRuleId(Long ruleId)
    {
        return finReportMappingRulesMapper.selectFinReportMappingRulesByRuleId(ruleId);
    }

    /**
     * 查询报表映射规则列表
     *
     * @param finReportMappingRules 报表映射规则
     * @return 报表映射规则
     */
    @Override
    public List<FinReportMappingRules> selectFinReportMappingRulesList(FinReportMappingRules finReportMappingRules)
    {
        return finReportMappingRulesMapper.selectFinReportMappingRulesList(finReportMappingRules);
    }

    /**
     * 根据模板类型查询规则列表
     *
     * @param groupid 租户ID
     * @param templateType 模板类型
     * @return 规则集合
     */
    @Override
    public List<FinReportMappingRules> selectRulesByTemplateType(String groupid, String templateType)
    {
        return finReportMappingRulesMapper.selectRulesByTemplateType(groupid, templateType);
    }

    /**
     * 根据项目编码查询规则列表
     *
     * @param groupid 租户ID
     * @param itemCode 项目编码
     * @return 规则集合
     */
    @Override
    public List<FinReportMappingRules> selectRulesByItemCode(String groupid, String itemCode)
    {
        return finReportMappingRulesMapper.selectRulesByItemCode(groupid, itemCode);
    }

    /**
     * 新增报表映射规则
     *
     * @param finReportMappingRules 报表映射规则
     * @return 结果
     */
    @Override
    public int insertFinReportMappingRules(FinReportMappingRules finReportMappingRules)
    {
        return finReportMappingRulesMapper.insertFinReportMappingRules(finReportMappingRules);
    }

    /**
     * 批量新增报表映射规则
     *
     * @param list 规则列表
     * @return 结果
     */
    @Override
    public int batchInsertFinReportMappingRules(List<FinReportMappingRules> list)
    {
        return finReportMappingRulesMapper.batchInsertFinReportMappingRules(list);
    }

    /**
     * 修改报表映射规则
     *
     * @param finReportMappingRules 报表映射规则
     * @return 结果
     */
    @Override
    public int updateFinReportMappingRules(FinReportMappingRules finReportMappingRules)
    {
        return finReportMappingRulesMapper.updateFinReportMappingRules(finReportMappingRules);
    }

    /**
     * 批量删除报表映射规则
     *
     * @param ruleIds 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteFinReportMappingRulesByRuleIds(Long[] ruleIds)
    {
        return finReportMappingRulesMapper.deleteFinReportMappingRulesByRuleIds(ruleIds);
    }

    /**
     * 删除报表映射规则信息
     *
     * @param ruleId 规则ID
     * @return 结果
     */
    @Override
    public int deleteFinReportMappingRulesByRuleId(Long ruleId)
    {
        return finReportMappingRulesMapper.deleteFinReportMappingRulesByRuleId(ruleId);
    }

    /**
     * 根据租户ID和模板类型删除规则
     *
     * @param groupid 租户ID
     * @param templateType 模板类型
     * @return 结果
     */
    @Override
    public int deleteRulesByTenantAndType(String groupid, String templateType)
    {
        return finReportMappingRulesMapper.deleteRulesByTenantAndType(groupid, templateType);
    }

    /**
     * 初始化默认规则到租户
     *
     * @param groupid 租户ID
     * @return 初始化的规则数量
     */
    @Override
    public int initDefaultRulesForTenant(String groupid)
    {
        // 先删除该租户的现有规则
        finReportMappingRulesMapper.deleteRulesByTenantAndType(groupid, "BALANCE_SHEET");
        finReportMappingRulesMapper.deleteRulesByTenantAndType(groupid, "INCOME_STATEMENT");
        finReportMappingRulesMapper.deleteRulesByTenantAndType(groupid, "CASH_FLOW");
        finReportMappingRulesMapper.deleteRulesByTenantAndType(groupid, "EQUITY_CHANGE");

        // 创建默认规则列表
        List<FinReportMappingRules> defaultRules = createDefaultRules(groupid);

        // 批量插入
        if (defaultRules != null && defaultRules.size() > 0)
        {
            return finReportMappingRulesMapper.batchInsertFinReportMappingRules(defaultRules);
        }
        return 0;
    }

    /**
     * 创建默认规则列表
     *
     * @param groupid 租户ID
     * @return 默认规则列表
     */
    private List<FinReportMappingRules> createDefaultRules(String groupid)
    {
        List<FinReportMappingRules> rules = new ArrayList<>();

        // ========== 资产负债表规则 ==========

        // 流动资产
        rules.add(createRule(groupid, "BALANCE_SHEET", "ASSET_CASH", "库存现金", "PREFIX", "1001", null, "ADD", 1, 1, 1, 10, "1001-库存现金"));
        rules.add(createRule(groupid, "BALANCE_SHEET", "ASSET_CASH", "银行存款", "PREFIX", "1002", null, "ADD", 1, 1, 1, 10, "1002-银行存款"));
        rules.add(createRule(groupid, "BALANCE_SHEET", "ASSET_CASH", "其他货币资金", "PREFIX", "1012", null, "ADD", 1, 1, 1, 10, "1012-其他货币资金"));

        rules.add(createRule(groupid, "BALANCE_SHEET", "ASSET_FINANCIAL", "交易性金融资产", "PREFIX", "1101", null, "ADD", 1, 1, 1, 10, "1101-交易性金融资产"));
        rules.add(createRule(groupid, "BALANCE_SHEET", "ASSET_BILLS", "应收票据", "PREFIX", "1121", null, "ADD", 1, 1, 1, 10, "1121-应收票据"));

        rules.add(createRule(groupid, "BALANCE_SHEET", "ASSET_RECEIVABLE", "应收账款", "PREFIX", "1122", null, "ADD", 1, 1, 1, 10, "1122-应收账款"));
        rules.add(createRule(groupid, "BALANCE_SHEET", "ASSET_RECEIVABLE", "坏账准备", "PREFIX", "1231", null, "SUBTRACT", 1, 2, 1, 20, "1231-坏账准备（减项）"));

        rules.add(createRule(groupid, "BALANCE_SHEET", "ASSET_PREPAY", "预付款项", "PREFIX", "1123", null, "ADD", 1, 1, 1, 10, "1123-预付款项"));
        rules.add(createRule(groupid, "BALANCE_SHEET", "ASSET_INTEREST_RECEIVABLE", "应收利息", "PREFIX", "1132", null, "ADD", 1, 1, 1, 10, "1132-应收利息"));
        rules.add(createRule(groupid, "BALANCE_SHEET", "ASSET_DIVIDEND_RECEIVABLE", "应收股利", "PREFIX", "1131", null, "ADD", 1, 1, 1, 10, "1131-应收股利"));
        rules.add(createRule(groupid, "BALANCE_SHEET", "ASSET_OTHER_RECEIVABLE", "其他应收款", "PREFIX", "1221", null, "ADD", 1, 1, 1, 10, "1221-其他应收款"));

        // 存货
        rules.add(createRule(groupid, "BALANCE_SHEET", "ASSET_INVENTORY", "材料采购", "PREFIX", "1401", null, "ADD", 1, 1, 1, 10, "1401-材料采购"));
        rules.add(createRule(groupid, "BALANCE_SHEET", "ASSET_INVENTORY", "在途物资", "PREFIX", "1402", null, "ADD", 1, 1, 1, 10, "1402-在途物资"));
        rules.add(createRule(groupid, "BALANCE_SHEET", "ASSET_INVENTORY", "原材料", "PREFIX", "1403", null, "ADD", 1, 1, 1, 10, "1403-原材料"));
        rules.add(createRule(groupid, "BALANCE_SHEET", "ASSET_INVENTORY", "库存商品", "PREFIX", "1405", null, "ADD", 1, 1, 1, 10, "1405-库存商品"));
        rules.add(createRule(groupid, "BALANCE_SHEET", "ASSET_INVENTORY", "发出商品", "PREFIX", "1406", null, "ADD", 1, 1, 1, 10, "1406-发出商品"));
        rules.add(createRule(groupid, "BALANCE_SHEET", "ASSET_INVENTORY", "委托加工物资", "PREFIX", "1408", null, "ADD", 1, 1, 1, 10, "1408-委托加工物资"));
        rules.add(createRule(groupid, "BALANCE_SHEET", "ASSET_INVENTORY", "周转材料", "PREFIX", "1411", null, "ADD", 1, 1, 1, 10, "1411-周转材料"));
        rules.add(createRule(groupid, "BALANCE_SHEET", "ASSET_INVENTORY", "存货跌价准备", "PREFIX", "1471", null, "SUBTRACT", 1, 2, 1, 20, "1471-存货跌价准备（减项）"));

        // 其他流动资产
        rules.add(createRule(groupid, "BALANCE_SHEET", "ASSET_CURRENT_OTHER", "待摊费用", "PREFIX", "1301", null, "ADD", 1, 1, 1, 10, "1301-待摊费用"));

        // 非流动资产
        rules.add(createRule(groupid, "BALANCE_SHEET", "ASSET_LONG_INVEST", "长期股权投资", "PREFIX", "1511", null, "ADD", 1, 1, 1, 10, "1511-长期股权投资"));
        rules.add(createRule(groupid, "BALANCE_SHEET", "ASSET_FIXED", "固定资产", "PREFIX", "1601", null, "ADD", 1, 1, 1, 10, "1601-固定资产"));
        rules.add(createRule(groupid, "BALANCE_SHEET", "ASSET_FIXED", "累计折旧", "PREFIX", "1602", null, "SUBTRACT", 1, 2, 1, 20, "1602-累计折旧（减项）"));
        rules.add(createRule(groupid, "BALANCE_SHEET", "ASSET_FIXED", "固定资产减值准备", "PREFIX", "1603", null, "SUBTRACT", 1, 2, 1, 20, "1603-固定资产减值准备（减项）"));
        rules.add(createRule(groupid, "BALANCE_SHEET", "ASSET_CONSTRUCTION", "在建工程", "PREFIX", "1604", null, "ADD", 1, 1, 1, 10, "1604-在建工程"));
        rules.add(createRule(groupid, "BALANCE_SHEET", "ASSET_INTANGIBLE", "无形资产", "PREFIX", "1701", null, "ADD", 1, 1, 1, 10, "1701-无形资产"));
        rules.add(createRule(groupid, "BALANCE_SHEET", "ASSET_INTANGIBLE", "累计摊销", "PREFIX", "1702", null, "SUBTRACT", 1, 2, 1, 20, "1702-累计摊销（减项）"));
        rules.add(createRule(groupid, "BALANCE_SHEET", "ASSET_INTANGIBLE", "无形资产减值准备", "PREFIX", "1703", null, "SUBTRACT", 1, 2, 1, 20, "1703-无形资产减值准备（减项）"));
        rules.add(createRule(groupid, "BALANCE_SHEET", "ASSET_DEFERRED", "长期待摊费用", "PREFIX", "1801", null, "ADD", 1, 1, 1, 10, "1801-长期待摊费用"));
        rules.add(createRule(groupid, "BALANCE_SHEET", "ASSET_DEFERRED_TAX", "递延所得税资产", "PREFIX", "1811", null, "ADD", 1, 1, 1, 10, "1811-递延所得税资产"));
        rules.add(createRule(groupid, "BALANCE_SHEET", "ASSET_NON_CURRENT_OTHER", "其他非流动资产", "PREFIX", "1901", null, "ADD", 1, 1, 1, 10, "1901-其他非流动资产"));

        // 流动负债
        rules.add(createRule(groupid, "BALANCE_SHEET", "LIABILITY_SHORT_LOAN", "短期借款", "PREFIX", "2001", null, "ADD", 2, 2, 1, 10, "2001-短期借款"));
        rules.add(createRule(groupid, "BALANCE_SHEET", "LIABILITY_BILLS", "应付票据", "PREFIX", "2201", null, "ADD", 2, 2, 1, 10, "2201-应付票据"));
        rules.add(createRule(groupid, "BALANCE_SHEET", "LIABILITY_PAYABLE", "应付账款", "PREFIX", "2202", null, "ADD", 2, 2, 1, 10, "2202-应付账款"));
        rules.add(createRule(groupid, "BALANCE_SHEET", "LIABILITY_ADVANCE", "预收款项", "PREFIX", "2203", null, "ADD", 2, 2, 1, 10, "2203-预收款项"));
        rules.add(createRule(groupid, "BALANCE_SHEET", "LIABILITY_SALARY", "应付职工薪酬", "PREFIX", "2211", null, "ADD", 2, 2, 1, 10, "2211-应付职工薪酬"));
        rules.add(createRule(groupid, "BALANCE_SHEET", "LIABILITY_TAX", "应交税费", "PREFIX", "2221", null, "ADD", 2, 2, 1, 10, "2221-应交税费"));
        rules.add(createRule(groupid, "BALANCE_SHEET", "LIABILITY_INTEREST_PAYABLE", "应付利息", "PREFIX", "2231", null, "ADD", 2, 2, 1, 10, "2231-应付利息"));
        rules.add(createRule(groupid, "BALANCE_SHEET", "LIABILITY_DIVIDEND_PAYABLE", "应付股利", "PREFIX", "2232", null, "ADD", 2, 2, 1, 10, "2232-应付股利"));
        rules.add(createRule(groupid, "BALANCE_SHEET", "LIABILITY_OTHER_PAYABLE", "其他应付款", "PREFIX", "2241", null, "ADD", 2, 2, 1, 10, "2241-其他应付款"));
        rules.add(createRule(groupid, "BALANCE_SHEET", "LIABILITY_CURRENT_OTHER", "其他流动负债", "PREFIX", "2311", null, "ADD", 2, 2, 1, 10, "2311-其他流动负债"));

        // 非流动负债
        rules.add(createRule(groupid, "BALANCE_SHEET", "LIABILITY_LONG_LOAN", "长期借款", "PREFIX", "2501", null, "ADD", 2, 2, 1, 10, "2501-长期借款"));
        rules.add(createRule(groupid, "BALANCE_SHEET", "LIABILITY_BONDS", "应付债券", "PREFIX", "2502", null, "ADD", 2, 2, 1, 10, "2502-应付债券"));
        rules.add(createRule(groupid, "BALANCE_SHEET", "LIABILITY_LONG_PAYABLE", "长期应付款", "PREFIX", "2701", null, "ADD", 2, 2, 1, 10, "2701-长期应付款"));
        rules.add(createRule(groupid, "BALANCE_SHEET", "LIABILITY_DEFERRED_TAX", "递延所得税负债", "PREFIX", "2901", null, "ADD", 2, 2, 1, 10, "2901-递延所得税负债"));
        rules.add(createRule(groupid, "BALANCE_SHEET", "LIABILITY_NON_CURRENT_OTHER", "其他非流动负债", "PREFIX", "2911", null, "ADD", 2, 2, 1, 10, "2911-其他非流动负债"));

        // 所有者权益
        rules.add(createRule(groupid, "BALANCE_SHEET", "EQUITY_CAPITAL", "实收资本", "PREFIX", "4001", null, "ADD", 3, 2, 1, 10, "4001-实收资本"));
        rules.add(createRule(groupid, "BALANCE_SHEET", "EQUITY_RESERVE", "资本公积", "PREFIX", "4002", null, "ADD", 3, 2, 1, 10, "4002-资本公积"));
        rules.add(createRule(groupid, "BALANCE_SHEET", "EQUITY_SURPLUS", "盈余公积", "PREFIX", "4101", null, "ADD", 3, 2, 1, 10, "4101-盈余公积"));
        rules.add(createRule(groupid, "BALANCE_SHEET", "EQUITY_RETAINED", "未分配利润", "PREFIX", "4104", null, "ADD", 3, 2, 1, 10, "4104-未分配利润"));

        // ========== 利润表规则 ==========
        // 使用小企业会计准则 5xxx 编码，PREFIX 策略自动匹配所有子科目

        // 收入类
        rules.add(createRule(groupid, "INCOME_STATEMENT", "REVENUE_MAIN", "主营业务收入", "PREFIX", "5001", null, "ADD", 12, 2, 0, 10, "5001-主营业务收入(含客户自建子科目)"));
        rules.add(createRule(groupid, "INCOME_STATEMENT", "REVENUE_OTHER", "其他业务收入", "PREFIX", "5051", null, "ADD", 13, 2, 0, 10, "5051-其他业务收入(含客户自建子科目)"));
        rules.add(createRule(groupid, "INCOME_STATEMENT", "INCOME_INVESTMENT", "投资收益", "PREFIX", "5111", null, "ADD", 13, 2, 0, 10, "5111-投资收益(含客户自建子科目)"));
        rules.add(createRule(groupid, "INCOME_STATEMENT", "INCOME_NON_OPERATING", "营业外收入", "PREFIX", "5301", null, "ADD", 13, 2, 0, 10, "5301-营业外收入(含客户自建子科目)"));

        // 成本费用类
        rules.add(createRule(groupid, "INCOME_STATEMENT", "COST_MAIN", "主营业务成本", "PREFIX", "5401", null, "ADD", 16, 1, 0, 10, "5401-主营业务成本(含客户自建子科目)"));
        rules.add(createRule(groupid, "INCOME_STATEMENT", "COST_OTHER", "其他业务成本", "PREFIX", "5402", null, "ADD", 15, 1, 0, 10, "5402-其他业务成本(含客户自建子科目)"));
        rules.add(createRule(groupid, "INCOME_STATEMENT", "TAX_SURCHARGE", "税金及附加", "PREFIX", "5403", null, "ADD", 16, 1, 0, 10, "5403-税金及附加(含客户自建子科目)"));
        rules.add(createRule(groupid, "INCOME_STATEMENT", "EXPENSE_SELLING", "销售费用", "PREFIX", "5601", null, "ADD", 14, 1, 0, 10, "5601-销售费用(含客户自建子科目)"));
        rules.add(createRule(groupid, "INCOME_STATEMENT", "EXPENSE_ADMIN", "管理费用", "PREFIX", "5602", null, "ADD", 14, 1, 0, 10, "5602-管理费用(含客户自建子科目)"));
        rules.add(createRule(groupid, "INCOME_STATEMENT", "EXPENSE_FINANCIAL", "财务费用", "PREFIX", "5603", null, "ADD", 14, 1, 0, 10, "5603-财务费用(含客户自建子科目)"));
        rules.add(createRule(groupid, "INCOME_STATEMENT", "EXPENSE_NON_OPERATING", "营业外支出", "PREFIX", "5711", null, "ADD", 15, 1, 0, 10, "5711-营业外支出(含客户自建子科目)"));
        rules.add(createRule(groupid, "INCOME_STATEMENT", "EXPENSE_INCOME_TAX", "所得税费用", "PREFIX", "5801", null, "ADD", 18, 1, 0, 10, "5801-所得税费用(含客户自建子科目)"));

        // ========== 现金流量表规则 ==========
        // 经营活动现金流入
        rules.add(createRule(groupid, "CASH_FLOW", "CASH_IN_SALES", "销售商品、提供劳务收到的现金", "PREFIX", "5001", null, "ADD", 12, 2, 0, 10, "主营业务收入对应的现金流入"));
        rules.add(createRule(groupid, "CASH_FLOW", "CASH_IN_SALES", "应收账款减少", "PREFIX", "1122", null, "ADD", 1, 1, 0, 20, "应收账款减少表示收到现金"));
        rules.add(createRule(groupid, "CASH_FLOW", "CASH_IN_SALES", "预收账款增加", "PREFIX", "2203", null, "ADD", 2, 2, 0, 30, "预收账款增加表示收到现金"));
        rules.add(createRule(groupid, "CASH_FLOW", "CASH_IN_TAX_REFUND", "收到的税费返还", "PREFIX", "2221", null, "ADD", 2, 2, 0, 10, "应交税费减少表示收到返还"));
        rules.add(createRule(groupid, "CASH_FLOW", "CASH_IN_OTHER_OPERATING", "收到其他与经营活动有关的现金", "PREFIX", "1221", null, "ADD", 1, 1, 0, 10, "其他应收款对应的现金流入"));

        // 经营活动现金流出
        rules.add(createRule(groupid, "CASH_FLOW", "CASH_OUT_PURCHASE", "购买商品、接受劳务支付的现金", "PREFIX", "5401", null, "ADD", 12, 1, 0, 10, "主营业务成本对应的现金流出"));
        rules.add(createRule(groupid, "CASH_FLOW", "CASH_OUT_PURCHASE", "应付账款减少", "PREFIX", "2202", null, "ADD", 2, 2, 0, 20, "应付账款减少表示支付现金"));
        rules.add(createRule(groupid, "CASH_FLOW", "CASH_OUT_PURCHASE", "预付账款增加", "PREFIX", "1123", null, "ADD", 1, 1, 0, 30, "预付账款增加表示支付现金"));
        rules.add(createRule(groupid, "CASH_FLOW", "CASH_OUT_EMPLOYEE", "支付给职工以及为职工支付的现金", "PREFIX", "2211", null, "ADD", 2, 2, 0, 10, "应付职工薪酬减少表示支付现金"));
        rules.add(createRule(groupid, "CASH_FLOW", "CASH_OUT_TAX", "支付的各项税费", "PREFIX", "2221", null, "ADD", 2, 1, 0, 10, "应交税费减少表示支付税款"));
        rules.add(createRule(groupid, "CASH_FLOW", "CASH_OUT_OTHER_OPERATING", "支付其他与经营活动有关的现金", "PREFIX", "1221", null, "ADD", 1, 1, 0, 10, "其他应收款增加表示支付现金"));

        // 投资活动现金流入
        rules.add(createRule(groupid, "CASH_FLOW", "CASH_IN_WITHDRAW", "收回投资收到的现金", "PREFIX", "1101", null, "ADD", 1, 1, 0, 10, "交易性金融资产减少表示收回投资"));
        rules.add(createRule(groupid, "CASH_FLOW", "CASH_IN_INVEST_PROFIT", "取得投资收益收到的现金", "PREFIX", "5111", null, "ADD", 12, 2, 0, 10, "投资收益对应的现金流入"));

        // 投资活动现金流出
        rules.add(createRule(groupid, "CASH_FLOW", "CASH_OUT_LONG_ASSET", "购建固定资产、无形资产和其他长期资产支付的现金", "PREFIX", "1601", null, "ADD", 1, 1, 0, 10, "固定资产增加表示支付现金"));
        rules.add(createRule(groupid, "CASH_FLOW", "CASH_OUT_LONG_ASSET", "在建工程增加", "PREFIX", "1604", null, "ADD", 1, 1, 0, 20, "在建工程增加表示支付现金"));
        rules.add(createRule(groupid, "CASH_FLOW", "CASH_OUT_INVEST", "投资支付的现金", "PREFIX", "1511", null, "ADD", 1, 1, 0, 10, "长期股权投资增加表示支付现金"));

        // 筹资活动现金流入
        rules.add(createRule(groupid, "CASH_FLOW", "CASH_IN_CAPITAL", "吸收投资收到的现金", "PREFIX", "4001", null, "ADD", 3, 2, 0, 10, "实收资本增加表示收到投资"));
        rules.add(createRule(groupid, "CASH_FLOW", "CASH_IN_LOAN", "取得借款收到的现金", "PREFIX", "2001", null, "ADD", 2, 2, 0, 10, "短期借款增加表示收到借款"));
        rules.add(createRule(groupid, "CASH_FLOW", "CASH_IN_LOAN", "长期借款增加", "PREFIX", "2501", null, "ADD", 2, 2, 0, 20, "长期借款增加表示收到借款"));

        // 筹资活动现金流出
        rules.add(createRule(groupid, "CASH_FLOW", "CASH_OUT_DEBT", "偿还债务支付的现金", "PREFIX", "2001", null, "ADD", 2, 1, 0, 10, "短期借款减少表示偿还债务"));
        rules.add(createRule(groupid, "CASH_FLOW", "CASH_OUT_DEBT", "长期借款减少", "PREFIX", "2501", null, "ADD", 2, 1, 0, 20, "长期借款减少表示偿还债务"));
        rules.add(createRule(groupid, "CASH_FLOW", "CASH_OUT_DIVIDEND", "分配股利、利润或偿付利息支付的现金", "PREFIX", "2231", null, "ADD", 2, 1, 0, 10, "应付利息减少表示支付利息"));
        rules.add(createRule(groupid, "CASH_FLOW", "CASH_OUT_DIVIDEND", "应付股利减少", "PREFIX", "2232", null, "ADD", 2, 1, 0, 20, "应付股利减少表示支付股利"));

        // ========== 所有者权益变动表规则 ==========
        // 期初余额
        rules.add(createRule(groupid, "EQUITY_CHANGE", "BALANCE_CAPITAL_PRIOR", "实收资本期初", "PREFIX", "4001", null, "ADD", 3, 2, 0, 10, "实收资本期初余额"));
        rules.add(createRule(groupid, "EQUITY_CHANGE", "BALANCE_RESERVE_CAPITAL_PRIOR", "资本公积期初", "PREFIX", "4002", null, "ADD", 3, 2, 0, 10, "资本公积期初余额"));
        rules.add(createRule(groupid, "EQUITY_CHANGE", "BALANCE_RESERVE_SURPLUS_PRIOR", "盈余公积期初", "PREFIX", "4101", null, "ADD", 3, 2, 0, 10, "盈余公积期初余额"));
        rules.add(createRule(groupid, "EQUITY_CHANGE", "BALANCE_RETAINED_PRIOR", "未分配利润期初", "PREFIX", "4104", null, "ADD", 3, 2, 0, 10, "未分配利润期初余额"));

        // 期末余额
        rules.add(createRule(groupid, "EQUITY_CHANGE", "BALANCE_CAPITAL_ENDING", "实收资本期末", "PREFIX", "4001", null, "ADD", 3, 2, 0, 10, "实收资本期末余额"));
        rules.add(createRule(groupid, "EQUITY_CHANGE", "BALANCE_RESERVE_CAPITAL_ENDING", "资本公积期末", "PREFIX", "4002", null, "ADD", 3, 2, 0, 10, "资本公积期末余额"));
        rules.add(createRule(groupid, "EQUITY_CHANGE", "BALANCE_RESERVE_SURPLUS_ENDING", "盈余公积期末", "PREFIX", "4101", null, "ADD", 3, 2, 0, 10, "盈余公积期末余额"));
        rules.add(createRule(groupid, "EQUITY_CHANGE", "BALANCE_RETAINED_ENDING", "未分配利润期末", "PREFIX", "4104", null, "ADD", 3, 2, 0, 10, "未分配利润期末余额"));

        // 本年增减变动
        rules.add(createRule(groupid, "EQUITY_CHANGE", "CHANGE_CAPITAL_IN", "所有者投入资本", "PREFIX", "4001", null, "ADD", 3, 2, 0, 10, "实收资本增加表示所有者投入"));
        rules.add(createRule(groupid, "EQUITY_CHANGE", "CHANGE_CAPITAL_REDUCE", "所有者减少资本", "PREFIX", "4001", null, "SUBTRACT", 3, 1, 0, 10, "实收资本减少表示所有者减少"));
        rules.add(createRule(groupid, "EQUITY_CHANGE", "CHANGE_SURPLUS_EXTRACT", "提取盈余公积", "PREFIX", "4101", null, "ADD", 3, 2, 0, 10, "盈余公积增加表示提取"));
        rules.add(createRule(groupid, "EQUITY_CHANGE", "CHANGE_DIVIDEND", "对所有者（或股东）的分配", "PREFIX", "4104", null, "SUBTRACT", 3, 1, 0, 10, "未分配利润减少表示分配"));

        return rules;
    }

    /**
     * 创建规则对象
     */
    private FinReportMappingRules createRule(String groupid, String templateType, String itemCode, String ruleName,
                                              String matchType, String matchValue, String matchValueEnd, String operator,
                                              Integer subjectType, Integer direction, Integer isLeafOnly, Integer priority, String description)
    {
        FinReportMappingRules rule = new FinReportMappingRules();
        rule.setGroupid(groupid);
        rule.setTemplateType(templateType);
        rule.setRuleType("SUBJECT");
        rule.setItemCode(itemCode);
        rule.setRuleName(ruleName);
        rule.setMatchType(matchType);
        rule.setMatchValue(matchValue);
        rule.setMatchValueEnd(matchValueEnd);
        rule.setOperator(operator);
        rule.setSubjectType(subjectType);
        rule.setDirection(direction);
        rule.setIsLeafOnly(isLeafOnly);
        rule.setPriority(priority);
        rule.setStatus(1);
        rule.setDescription(description);
        rule.setCreatedBy("system");
        return rule;
    }
}
