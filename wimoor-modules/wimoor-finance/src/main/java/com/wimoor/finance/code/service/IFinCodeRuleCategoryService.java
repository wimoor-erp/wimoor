package com.wimoor.finance.code.service;

import java.util.List;
import com.wimoor.finance.code.domain.FinCodeRuleCategory;

/**
 * 编码规则与分类关联Service接口
 * 
 * @author wimoor
 * @date 2025-11-03
 */
public interface IFinCodeRuleCategoryService 
{
    /**
     * 查询编码规则与分类关联
     * 
     * @param id 编码规则与分类关联主键
     * @return 编码规则与分类关联
     */
    public FinCodeRuleCategory selectFinCodeRuleCategoryById(Long id);

    /**
     * 查询编码规则与分类关联列表
     * 
     * @param finCodeRuleCategory 编码规则与分类关联
     * @return 编码规则与分类关联集合
     */
    public List<FinCodeRuleCategory> selectFinCodeRuleCategoryList(FinCodeRuleCategory finCodeRuleCategory);

    /**
     * 新增编码规则与分类关联
     * 
     * @param finCodeRuleCategory 编码规则与分类关联
     * @return 结果
     */
    public int insertFinCodeRuleCategory(FinCodeRuleCategory finCodeRuleCategory);

    /**
     * 修改编码规则与分类关联
     * 
     * @param finCodeRuleCategory 编码规则与分类关联
     * @return 结果
     */
    public int updateFinCodeRuleCategory(FinCodeRuleCategory finCodeRuleCategory);

    /**
     * 批量删除编码规则与分类关联
     * 
     * @param ids 需要删除的编码规则与分类关联主键集合
     * @return 结果
     */
    public int deleteFinCodeRuleCategoryByIds(Long[] ids);

    /**
     * 删除编码规则与分类关联信息
     * 
     * @param id 编码规则与分类关联主键
     * @return 结果
     */
    public int deleteFinCodeRuleCategoryById(Long id);
}
