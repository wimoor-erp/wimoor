package com.wimoor.finance.code.mapper;

import java.util.List;
import com.wimoor.finance.code.domain.FinCodeRuleRelation;

/**
 * 编码规则关联Mapper接口
 * 
 * @author wimoor
 * @date 2025-11-03
 */
public interface FinCodeRuleRelationMapper 
{
    /**
     * 查询编码规则关联
     * 
     * @param id 编码规则关联主键
     * @return 编码规则关联
     */
    public FinCodeRuleRelation selectFinCodeRuleRelationById(Long id);

    /**
     * 查询编码规则关联列表
     * 
     * @param finCodeRuleRelation 编码规则关联
     * @return 编码规则关联集合
     */
    public List<FinCodeRuleRelation> selectFinCodeRuleRelationList(FinCodeRuleRelation finCodeRuleRelation);

    /**
     * 新增编码规则关联
     * 
     * @param finCodeRuleRelation 编码规则关联
     * @return 结果
     */
    public int insertFinCodeRuleRelation(FinCodeRuleRelation finCodeRuleRelation);

    /**
     * 修改编码规则关联
     * 
     * @param finCodeRuleRelation 编码规则关联
     * @return 结果
     */
    public int updateFinCodeRuleRelation(FinCodeRuleRelation finCodeRuleRelation);

    /**
     * 删除编码规则关联
     * 
     * @param id 编码规则关联主键
     * @return 结果
     */
    public int deleteFinCodeRuleRelationById(Long id);

    /**
     * 批量删除编码规则关联
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFinCodeRuleRelationByIds(Long[] ids);
}
