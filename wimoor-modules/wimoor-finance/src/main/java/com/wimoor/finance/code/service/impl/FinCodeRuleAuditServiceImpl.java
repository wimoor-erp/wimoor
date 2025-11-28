package com.wimoor.finance.code.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.wimoor.finance.code.mapper.FinCodeRuleAuditMapper;
import com.wimoor.finance.code.domain.FinCodeRuleAudit;
import com.wimoor.finance.code.service.IFinCodeRuleAuditService;

/**
 * 编码规则审计Service业务层处理
 * 
 * @author wimoor
 * @date 2025-11-03
 */
@Service
public class FinCodeRuleAuditServiceImpl implements IFinCodeRuleAuditService 
{
    @Autowired
    private FinCodeRuleAuditMapper finCodeRuleAuditMapper;

    /**
     * 查询编码规则审计
     * 
     * @param id 编码规则审计主键
     * @return 编码规则审计
     */
    @Override
    public FinCodeRuleAudit selectFinCodeRuleAuditById(Long id)
    {
        return finCodeRuleAuditMapper.selectFinCodeRuleAuditById(id);
    }

    /**
     * 查询编码规则审计列表
     * 
     * @param finCodeRuleAudit 编码规则审计
     * @return 编码规则审计
     */
    @Override
    public List<FinCodeRuleAudit> selectFinCodeRuleAuditList(FinCodeRuleAudit finCodeRuleAudit)
    {
        return finCodeRuleAuditMapper.selectFinCodeRuleAuditList(finCodeRuleAudit);
    }

    /**
     * 新增编码规则审计
     * 
     * @param finCodeRuleAudit 编码规则审计
     * @return 结果
     */
    @Override
    public int insertFinCodeRuleAudit(FinCodeRuleAudit finCodeRuleAudit)
    {
        return finCodeRuleAuditMapper.insertFinCodeRuleAudit(finCodeRuleAudit);
    }

    /**
     * 修改编码规则审计
     * 
     * @param finCodeRuleAudit 编码规则审计
     * @return 结果
     */
    @Override
    public int updateFinCodeRuleAudit(FinCodeRuleAudit finCodeRuleAudit)
    {
        return finCodeRuleAuditMapper.updateFinCodeRuleAudit(finCodeRuleAudit);
    }

    /**
     * 批量删除编码规则审计
     * 
     * @param ids 需要删除的编码规则审计主键
     * @return 结果
     */
    @Override
    public int deleteFinCodeRuleAuditByIds(Long[] ids)
    {
        return finCodeRuleAuditMapper.deleteFinCodeRuleAuditByIds(ids);
    }

    /**
     * 删除编码规则审计信息
     * 
     * @param id 编码规则审计主键
     * @return 结果
     */
    @Override
    public int deleteFinCodeRuleAuditById(Long id)
    {
        return finCodeRuleAuditMapper.deleteFinCodeRuleAuditById(id);
    }
}
