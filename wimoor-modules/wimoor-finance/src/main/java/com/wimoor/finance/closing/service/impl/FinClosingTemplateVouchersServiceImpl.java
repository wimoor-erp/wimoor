package com.wimoor.finance.closing.service.impl;

import com.wimoor.finance.closing.domain.FinClosingTemplateVouchers;
import com.wimoor.finance.closing.mapper.FinClosingTemplateVouchersMapper;
import com.wimoor.finance.closing.service.IFinClosingTemplateVouchersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 结账模板凭证Service业务层处理
 * 
 * @author wimoor
 * @date 2026-03-20
 */
@Service
public class FinClosingTemplateVouchersServiceImpl implements IFinClosingTemplateVouchersService 
{
    @Autowired
    private FinClosingTemplateVouchersMapper finClosingTemplateVouchersMapper;

    /**
     * 查询结账模板凭证
     * 
     * @param id 结账模板凭证主键
     * @return 结账模板凭证
     */
    @Override
    public FinClosingTemplateVouchers selectFinClosingTemplateVouchersById(String id)
    {
        return finClosingTemplateVouchersMapper.selectFinClosingTemplateVouchersById(id);
    }

    /**
     * 查询结账模板凭证列表
     * 
     * @param finClosingTemplateVouchers 结账模板凭证
     * @return 结账模板凭证
     */
    @Override
    public List<FinClosingTemplateVouchers> selectFinClosingTemplateVouchersList(FinClosingTemplateVouchers finClosingTemplateVouchers)
    {
        return finClosingTemplateVouchersMapper.selectFinClosingTemplateVouchersList(finClosingTemplateVouchers);
    }

    @Override
    public FinClosingTemplateVouchers selectByTemplateId(String templateId) {
        return finClosingTemplateVouchersMapper.selectByTemplateId(templateId);
    }

    /**
     * 新增结账模板凭证
     * 
     * @param finClosingTemplateVouchers 结账模板凭证
     * @return 结果
     */
    @Override
    public int insertFinClosingTemplateVouchers(FinClosingTemplateVouchers finClosingTemplateVouchers)
    {
        return finClosingTemplateVouchersMapper.insertFinClosingTemplateVouchers(finClosingTemplateVouchers);
    }

    /**
     * 修改结账模板凭证
     * 
     * @param finClosingTemplateVouchers 结账模板凭证
     * @return 结果
     */
    @Override
    public int updateFinClosingTemplateVouchers(FinClosingTemplateVouchers finClosingTemplateVouchers)
    {
        return finClosingTemplateVouchersMapper.updateFinClosingTemplateVouchers(finClosingTemplateVouchers);
    }

    /**
     * 批量删除结账模板凭证
     * 
     * @param ids 需要删除的结账模板凭证主键
     * @return 结果
     */
    @Override
    public int deleteFinClosingTemplateVouchersByIds(String[] ids)
    {
        return finClosingTemplateVouchersMapper.deleteFinClosingTemplateVouchersByIds(ids);
    }

    /**
     * 删除结账模板凭证信息
     * 
     * @param id 结账模板凭证主键
     * @return 结果
     */
    @Override
    public int deleteFinClosingTemplateVouchersById(String id)
    {
        return finClosingTemplateVouchersMapper.deleteFinClosingTemplateVouchersById(id);
    }

    @Override
    public int deleteByVoucherId(String voucherId) {
        return finClosingTemplateVouchersMapper.deleteByVoucherId(voucherId);
    }

    @Override
    public FinClosingTemplateVouchers selectByTemplate(FinClosingTemplateVouchers template) {
        return         finClosingTemplateVouchersMapper.selectByTemplate(template);
    }
}
