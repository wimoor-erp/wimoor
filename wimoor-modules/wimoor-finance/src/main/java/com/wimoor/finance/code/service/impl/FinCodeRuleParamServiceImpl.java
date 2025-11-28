package com.wimoor.finance.code.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.wimoor.finance.code.mapper.FinCodeRuleParamMapper;
import com.wimoor.finance.code.domain.FinCodeRuleParam;
import com.wimoor.finance.code.service.IFinCodeRuleParamService;

/**
 * 编码规则参数Service业务层处理
 * 
 * @author wimoor
 * @date 2025-11-03
 */
@Service
public class FinCodeRuleParamServiceImpl implements IFinCodeRuleParamService 
{
    @Autowired
    private FinCodeRuleParamMapper finCodeRuleParamMapper;

    /**
     * 查询编码规则参数
     * 
     * @param id 编码规则参数主键
     * @return 编码规则参数
     */
    @Override
    public FinCodeRuleParam selectFinCodeRuleParamById(Long id)
    {
        return finCodeRuleParamMapper.selectFinCodeRuleParamById(id);
    }

    /**
     * 查询编码规则参数列表
     * 
     * @param finCodeRuleParam 编码规则参数
     * @return 编码规则参数
     */
    @Override
    public List<FinCodeRuleParam> selectFinCodeRuleParamList(FinCodeRuleParam finCodeRuleParam)
    {
        return finCodeRuleParamMapper.selectFinCodeRuleParamList(finCodeRuleParam);
    }

    /**
     * 新增编码规则参数
     * 
     * @param finCodeRuleParam 编码规则参数
     * @return 结果
     */
    @Override
    public int insertFinCodeRuleParam(FinCodeRuleParam finCodeRuleParam)
    {
        return finCodeRuleParamMapper.insertFinCodeRuleParam(finCodeRuleParam);
    }

    /**
     * 修改编码规则参数
     * 
     * @param finCodeRuleParam 编码规则参数
     * @return 结果
     */
    @Override
    public int updateFinCodeRuleParam(FinCodeRuleParam finCodeRuleParam)
    {
        return finCodeRuleParamMapper.updateFinCodeRuleParam(finCodeRuleParam);
    }

    /**
     * 批量删除编码规则参数
     * 
     * @param ids 需要删除的编码规则参数主键
     * @return 结果
     */
    @Override
    public int deleteFinCodeRuleParamByIds(Long[] ids)
    {
        return finCodeRuleParamMapper.deleteFinCodeRuleParamByIds(ids);
    }

    /**
     * 删除编码规则参数信息
     * 
     * @param id 编码规则参数主键
     * @return 结果
     */
    @Override
    public int deleteFinCodeRuleParamById(Long id)
    {
        return finCodeRuleParamMapper.deleteFinCodeRuleParamById(id);
    }
}
