package com.wimoor.finance.code.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.wimoor.finance.code.mapper.FinCodeRuleRelationMapper;
import com.wimoor.finance.code.domain.FinCodeRuleRelation;
import com.wimoor.finance.code.service.IFinCodeRuleRelationService;

/**
 * 编码规则关联Service业务层处理
 * 
 * @author wimoor
 * @date 2025-11-03
 */
@Service
public class FinCodeRuleRelationServiceImpl implements IFinCodeRuleRelationService 
{
    @Autowired
    private FinCodeRuleRelationMapper finCodeRuleRelationMapper;

    /**
     * 查询编码规则关联
     * 
     * @param id 编码规则关联主键
     * @return 编码规则关联
     */
    @Override
    public FinCodeRuleRelation selectFinCodeRuleRelationById(Long id)
    {
        return finCodeRuleRelationMapper.selectFinCodeRuleRelationById(id);
    }

    /**
     * 查询编码规则关联列表
     * 
     * @param finCodeRuleRelation 编码规则关联
     * @return 编码规则关联
     */
    @Override
    public List<FinCodeRuleRelation> selectFinCodeRuleRelationList(FinCodeRuleRelation finCodeRuleRelation)
    {
        return finCodeRuleRelationMapper.selectFinCodeRuleRelationList(finCodeRuleRelation);
    }

    /**
     * 新增编码规则关联
     * 
     * @param finCodeRuleRelation 编码规则关联
     * @return 结果
     */
    @Override
    public int insertFinCodeRuleRelation(FinCodeRuleRelation finCodeRuleRelation)
    {
        return finCodeRuleRelationMapper.insertFinCodeRuleRelation(finCodeRuleRelation);
    }

    /**
     * 修改编码规则关联
     * 
     * @param finCodeRuleRelation 编码规则关联
     * @return 结果
     */
    @Override
    public int updateFinCodeRuleRelation(FinCodeRuleRelation finCodeRuleRelation)
    {
        return finCodeRuleRelationMapper.updateFinCodeRuleRelation(finCodeRuleRelation);
    }

    /**
     * 批量删除编码规则关联
     * 
     * @param ids 需要删除的编码规则关联主键
     * @return 结果
     */
    @Override
    public int deleteFinCodeRuleRelationByIds(Long[] ids)
    {
        return finCodeRuleRelationMapper.deleteFinCodeRuleRelationByIds(ids);
    }

    /**
     * 删除编码规则关联信息
     * 
     * @param id 编码规则关联主键
     * @return 结果
     */
    @Override
    public int deleteFinCodeRuleRelationById(Long id)
    {
        return finCodeRuleRelationMapper.deleteFinCodeRuleRelationById(id);
    }
}
