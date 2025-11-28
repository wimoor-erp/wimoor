package com.wimoor.finance.code.service;

import java.util.List;
import java.util.Optional;

import com.wimoor.finance.code.domain.FinCodeRule;

/**
 * 编码规则Service接口
 * 
 * @author wimoor
 * @date 2025-11-03
 */
public interface IFinCodeRuleService 
{
    /**
     * 查询编码规则
     * 
     * @param id 编码规则主键
     * @return 编码规则
     */
    public FinCodeRule selectFinCodeRuleById(Long id);

    /**
     * 查询编码规则列表
     * 
     * @param finCodeRule 编码规则
     * @return 编码规则集合
     */
    public List<FinCodeRule> selectFinCodeRuleList(FinCodeRule finCodeRule);

    /**
     * 新增编码规则
     * 
     * @param finCodeRule 编码规则
     * @return 结果
     */
    public int insertFinCodeRule(FinCodeRule finCodeRule);

    /**
     * 修改编码规则
     * 
     * @param finCodeRule 编码规则
     * @return 结果
     */
    public int updateFinCodeRule(FinCodeRule finCodeRule);

    /**
     * 批量删除编码规则
     * 
     * @param ids 需要删除的编码规则主键集合
     * @return 结果
     */
    public int deleteFinCodeRuleByIds(Long[] ids);

    /**
     * 删除编码规则信息
     * 
     * @param id 编码规则主键
     * @return 结果
     */
    public int deleteFinCodeRuleById(Long id);

    Optional<FinCodeRule> findByRuleCode(String ruleCode,String groupid);

    List<FinCodeRule> findByStatus(Long status,String groupid);

    List<FinCodeRule> findByRuleTypeAndStatus(String ruleType, int status,String groupid);

    List<FinCodeRule> findByTenantIdAndRuleTypeAndStatus(String groupid, String ruleType, long status);
}
