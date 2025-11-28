package com.wimoor.finance.code.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.wimoor.finance.code.mapper.FinCodeRuleMapper;
import com.wimoor.finance.code.domain.FinCodeRule;
import com.wimoor.finance.code.service.IFinCodeRuleService;

/**
 * 编码规则Service业务层处理
 * 
 * @author wimoor
 * @date 2025-11-03
 */
@Service
public class FinCodeRuleServiceImpl implements IFinCodeRuleService 
{
    @Autowired
    private FinCodeRuleMapper finCodeRuleMapper;

    /**
     * 查询编码规则
     * 
     * @param id 编码规则主键
     * @return 编码规则
     */
    @Override
    public FinCodeRule selectFinCodeRuleById(Long id)
    {
        return finCodeRuleMapper.selectFinCodeRuleById(id);
    }

    /**
     * 查询编码规则列表
     * 
     * @param finCodeRule 编码规则
     * @return 编码规则
     */
    @Override
    public List<FinCodeRule> selectFinCodeRuleList(FinCodeRule finCodeRule)
    {
        return finCodeRuleMapper.selectFinCodeRuleList(finCodeRule);
    }

    /**
     * 新增编码规则
     * 
     * @param finCodeRule 编码规则
     * @return 结果
     */
    @Override
    public int insertFinCodeRule(FinCodeRule finCodeRule)
    {
        return finCodeRuleMapper.insertFinCodeRule(finCodeRule);
    }

    /**
     * 修改编码规则
     * 
     * @param finCodeRule 编码规则
     * @return 结果
     */
    @Override
    public int updateFinCodeRule(FinCodeRule finCodeRule)
    {
        return finCodeRuleMapper.updateFinCodeRule(finCodeRule);
    }

    /**
     * 批量删除编码规则
     * 
     * @param ids 需要删除的编码规则主键
     * @return 结果
     */
    @Override
    public int deleteFinCodeRuleByIds(Long[] ids)
    {
        return finCodeRuleMapper.deleteFinCodeRuleByIds(ids);
    }

    /**
     * 删除编码规则信息
     * 
     * @param id 编码规则主键
     * @return 结果
     */
    @Override
    public int deleteFinCodeRuleById(Long id)
    {
        return finCodeRuleMapper.deleteFinCodeRuleById(id);
    }

    @Override
    public Optional<FinCodeRule> findByRuleCode(String ruleCode,String groupid) {
        return Optional.ofNullable(this.finCodeRuleMapper.findByRuleCode(ruleCode,groupid));
    }

    @Override
    public List<FinCodeRule> findByStatus(Long status,String groupid) {
        return this.finCodeRuleMapper.findByStatus(status,groupid);
    }

    @Override
    public List<FinCodeRule> findByRuleTypeAndStatus(String ruleType, int status,String groupid) {
        return this.finCodeRuleMapper.findByRuleTypeAndStatus(groupid,ruleType, status);
    }

    @Override
    public List<FinCodeRule> findByTenantIdAndRuleTypeAndStatus(String groupid, String ruleType, long status) {
        return this.finCodeRuleMapper.findByTenantIdAndRuleTypeAndStatus(groupid, ruleType, status);
    }
}
