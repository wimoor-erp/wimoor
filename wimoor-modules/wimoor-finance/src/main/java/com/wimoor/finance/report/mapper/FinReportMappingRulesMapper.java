package com.wimoor.finance.report.mapper;

import java.util.List;
import com.wimoor.finance.report.domain.FinReportMappingRules;

/**
 * 报表映射规则Mapper接口
 *
 * @author wimoor
 * @date 2025-06-08
 */
public interface FinReportMappingRulesMapper
{
    /**
     * 查询报表映射规则
     *
     * @param ruleId 规则ID
     * @return 报表映射规则
     */
    public FinReportMappingRules selectFinReportMappingRulesByRuleId(Long ruleId);

    /**
     * 查询报表映射规则列表
     *
     * @param finReportMappingRules 报表映射规则
     * @return 报表映射规则集合
     */
    public List<FinReportMappingRules> selectFinReportMappingRulesList(FinReportMappingRules finReportMappingRules);

    /**
     * 根据模板类型查询规则列表
     *
     * @param groupid 租户ID
     * @param templateType 模板类型
     * @return 规则集合
     */
    public List<FinReportMappingRules> selectRulesByTemplateType(String groupid, String templateType);

    /**
     * 根据项目编码查询规则列表
     *
     * @param groupid 租户ID
     * @param itemCode 项目编码
     * @return 规则集合
     */
    public List<FinReportMappingRules> selectRulesByItemCode(String groupid, String itemCode);

    /**
     * 新增报表映射规则
     *
     * @param finReportMappingRules 报表映射规则
     * @return 结果
     */
    public int insertFinReportMappingRules(FinReportMappingRules finReportMappingRules);

    /**
     * 批量新增报表映射规则
     *
     * @param list 规则列表
     * @return 结果
     */
    public int batchInsertFinReportMappingRules(List<FinReportMappingRules> list);

    /**
     * 修改报表映射规则
     *
     * @param finReportMappingRules 报表映射规则
     * @return 结果
     */
    public int updateFinReportMappingRules(FinReportMappingRules finReportMappingRules);

    /**
     * 删除报表映射规则
     *
     * @param ruleId 规则ID
     * @return 结果
     */
    public int deleteFinReportMappingRulesByRuleId(Long ruleId);

    /**
     * 批量删除报表映射规则
     *
     * @param ruleIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteFinReportMappingRulesByRuleIds(Long[] ruleIds);

    /**
     * 根据租户ID和模板类型删除规则
     *
     * @param groupid 租户ID
     * @param templateType 模板类型
     * @return 结果
     */
    public int deleteRulesByTenantAndType(String groupid, String templateType);
}
