package com.wimoor.finance.code.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.wimoor.finance.code.mapper.FinCodeRuleCategoryMapper;
import com.wimoor.finance.code.domain.FinCodeRuleCategory;
import com.wimoor.finance.code.service.IFinCodeRuleCategoryService;

/**
 * 编码规则与分类关联Service业务层处理
 * 
 * @author wimoor
 * @date 2025-11-03
 */
@Service
public class FinCodeRuleCategoryServiceImpl implements IFinCodeRuleCategoryService 
{
    @Autowired
    private FinCodeRuleCategoryMapper finCodeRuleCategoryMapper;

    /**
     * 查询编码规则与分类关联
     * 
     * @param id 编码规则与分类关联主键
     * @return 编码规则与分类关联
     */
    @Override
    public FinCodeRuleCategory selectFinCodeRuleCategoryById(Long id)
    {
        return finCodeRuleCategoryMapper.selectFinCodeRuleCategoryById(id);
    }

    /**
     * 查询编码规则与分类关联列表
     * 
     * @param finCodeRuleCategory 编码规则与分类关联
     * @return 编码规则与分类关联
     */
    @Override
    public List<FinCodeRuleCategory> selectFinCodeRuleCategoryList(FinCodeRuleCategory finCodeRuleCategory)
    {
        return finCodeRuleCategoryMapper.selectFinCodeRuleCategoryList(finCodeRuleCategory);
    }

    /**
     * 新增编码规则与分类关联
     * 
     * @param finCodeRuleCategory 编码规则与分类关联
     * @return 结果
     */
    @Override
    public int insertFinCodeRuleCategory(FinCodeRuleCategory finCodeRuleCategory)
    {
        return finCodeRuleCategoryMapper.insertFinCodeRuleCategory(finCodeRuleCategory);
    }

    /**
     * 修改编码规则与分类关联
     * 
     * @param finCodeRuleCategory 编码规则与分类关联
     * @return 结果
     */
    @Override
    public int updateFinCodeRuleCategory(FinCodeRuleCategory finCodeRuleCategory)
    {
        return finCodeRuleCategoryMapper.updateFinCodeRuleCategory(finCodeRuleCategory);
    }

    /**
     * 批量删除编码规则与分类关联
     * 
     * @param ids 需要删除的编码规则与分类关联主键
     * @return 结果
     */
    @Override
    public int deleteFinCodeRuleCategoryByIds(Long[] ids)
    {
        return finCodeRuleCategoryMapper.deleteFinCodeRuleCategoryByIds(ids);
    }

    /**
     * 删除编码规则与分类关联信息
     * 
     * @param id 编码规则与分类关联主键
     * @return 结果
     */
    @Override
    public int deleteFinCodeRuleCategoryById(Long id)
    {
        return finCodeRuleCategoryMapper.deleteFinCodeRuleCategoryById(id);
    }
}
