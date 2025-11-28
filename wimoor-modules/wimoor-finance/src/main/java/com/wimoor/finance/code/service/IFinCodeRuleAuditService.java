package com.wimoor.finance.code.service;

import java.util.List;
import com.wimoor.finance.code.domain.FinCodeRuleAudit;

/**
 * 编码规则审计Service接口
 * 
 * @author wimoor
 * @date 2025-11-03
 */
public interface IFinCodeRuleAuditService 
{
    /**
     * 查询编码规则审计
     * 
     * @param id 编码规则审计主键
     * @return 编码规则审计
     */
    public FinCodeRuleAudit selectFinCodeRuleAuditById(Long id);

    /**
     * 查询编码规则审计列表
     * 
     * @param finCodeRuleAudit 编码规则审计
     * @return 编码规则审计集合
     */
    public List<FinCodeRuleAudit> selectFinCodeRuleAuditList(FinCodeRuleAudit finCodeRuleAudit);

    /**
     * 新增编码规则审计
     * 
     * @param finCodeRuleAudit 编码规则审计
     * @return 结果
     */
    public int insertFinCodeRuleAudit(FinCodeRuleAudit finCodeRuleAudit);

    /**
     * 修改编码规则审计
     * 
     * @param finCodeRuleAudit 编码规则审计
     * @return 结果
     */
    public int updateFinCodeRuleAudit(FinCodeRuleAudit finCodeRuleAudit);

    /**
     * 批量删除编码规则审计
     * 
     * @param ids 需要删除的编码规则审计主键集合
     * @return 结果
     */
    public int deleteFinCodeRuleAuditByIds(Long[] ids);

    /**
     * 删除编码规则审计信息
     * 
     * @param id 编码规则审计主键
     * @return 结果
     */
    public int deleteFinCodeRuleAuditById(Long id);
}
