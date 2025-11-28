package com.wimoor.finance.code.mapper;

import java.util.List;
import com.wimoor.finance.code.domain.FinCodeRule;
import org.apache.ibatis.annotations.Param;

/**
 * 编码规则Mapper接口
 * 
 * @author wimoor
 * @date 2025-11-03
 */
public interface FinCodeRuleMapper 
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
     * 删除编码规则
     * 
     * @param id 编码规则主键
     * @return 结果
     */
    public int deleteFinCodeRuleById(Long id);

    /**
     * 批量删除编码规则
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFinCodeRuleByIds(Long[] ids);

    List<FinCodeRule> findByStatus(@Param("status") Long status, @Param("groupid") String groupid);

    List<FinCodeRule> findByRuleTypeAndStatus(@Param("groupid") String groupid, @Param("ruleType") String ruleType, @Param("status") int i);

    FinCodeRule findByRuleCode(@Param("ruleCode") String ruleCode, @Param("groupid") String groupid);

    /**
     * 查询租户下指定规则类型和状态的编码规则列表
     *
     * @param groupid 租户ID
     * @param ruleType 规则类型
     * @param status 状态
     * @return 编码规则集合
     */
    List<FinCodeRule> findByTenantIdAndRuleTypeAndStatus(@Param("groupid") String groupid, @Param("ruleType") String ruleType, @Param("status") long status);
}
