package com.wimoor.finance.closing.mapper;

import com.wimoor.finance.closing.domain.FinClosingTemplateVouchers;

import java.util.List;

/**
 * 结账模板凭证Mapper接口
 * 
 * @author wimoor
 * @date 2026-03-20
 */
public interface FinClosingTemplateVouchersMapper 
{
    /**
     * 查询结账模板凭证
     * 
     * @param id 结账模板凭证主键
     * @return 结账模板凭证
     */
    public FinClosingTemplateVouchers selectFinClosingTemplateVouchersById(String id);

    /**
     * 查询结账模板凭证列表
     * 
     * @param finClosingTemplateVouchers 结账模板凭证
     * @return 结账模板凭证集合
     */
    public List<FinClosingTemplateVouchers> selectFinClosingTemplateVouchersList(FinClosingTemplateVouchers finClosingTemplateVouchers);
    
    /**
     * 根据模板ID查询结账模板凭证
     * 
     * @param templateId 模板ID
     * @return 结账模板凭证
     */
    public FinClosingTemplateVouchers selectByTemplateId(String templateId);

    /**
     * 新增结账模板凭证
     * 
     * @param finClosingTemplateVouchers 结账模板凭证
     * @return 结果
     */
    public int insertFinClosingTemplateVouchers(FinClosingTemplateVouchers finClosingTemplateVouchers);

    /**
     * 修改结账模板凭证
     * 
     * @param finClosingTemplateVouchers 结账模板凭证
     * @return 结果
     */
    public int updateFinClosingTemplateVouchers(FinClosingTemplateVouchers finClosingTemplateVouchers);

    /**
     * 删除结账模板凭证
     * 
     * @param id 结账模板凭证主键
     * @return 结果
     */
    public int deleteFinClosingTemplateVouchersById(String id);

    /**
     * 批量删除结账模板凭证
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFinClosingTemplateVouchersByIds(String[] ids);
    
    /**
     * 根据凭证ID删除结账模板凭证关联记录
     * 
     * @param voucherId 凭证ID
     * @return 结果
     */
    public int deleteByVoucherId(String voucherId);

    FinClosingTemplateVouchers selectByTemplate(FinClosingTemplateVouchers template);
}
