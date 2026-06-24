package com.wimoor.finance.closing.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.wimoor.finance.closing.mapper.FinClosingTemplateProfitLossMapper;
import com.wimoor.finance.closing.domain.FinClosingTemplateProfitLoss;
import com.wimoor.finance.closing.service.IFinClosingTemplateProfitLossService;

/**
 * 结转损益凭证模板配置Service业务层处理
 * 
 * @author wimoor
 * @date 2026-06-10
 */
@Service
public class FinClosingTemplateProfitLossServiceImpl implements IFinClosingTemplateProfitLossService 
{
    @Autowired
    private FinClosingTemplateProfitLossMapper finClosingTemplateProfitLossMapper;

    /**
     * 查询结转损益凭证模板配置
     * 
     * @param id 结转损益凭证模板配置主键
     * @return 结转损益凭证模板配置
     */
    @Override
    public FinClosingTemplateProfitLoss selectFinClosingTemplateProfitLossById(String id)
    {
        return finClosingTemplateProfitLossMapper.selectFinClosingTemplateProfitLossById(id);
    }

    /**
     * 查询结转损益凭证模板配置列表
     * 
     * @param finClosingTemplateProfitLoss 结转损益凭证模板配置
     * @return 结转损益凭证模板配置
     */
    @Override
    public List<FinClosingTemplateProfitLoss> selectFinClosingTemplateProfitLossList(FinClosingTemplateProfitLoss finClosingTemplateProfitLoss)
    {
        return finClosingTemplateProfitLossMapper.selectFinClosingTemplateProfitLossList(finClosingTemplateProfitLoss);
    }

    /**
     * 新增结转损益凭证模板配置
     * 
     * @param finClosingTemplateProfitLoss 结转损益凭证模板配置
     * @return 结果
     */
    @Override
    public int insertFinClosingTemplateProfitLoss(FinClosingTemplateProfitLoss finClosingTemplateProfitLoss)
    {
        return finClosingTemplateProfitLossMapper.insertFinClosingTemplateProfitLoss(finClosingTemplateProfitLoss);
    }

    /**
     * 修改结转损益凭证模板配置
     * 
     * @param finClosingTemplateProfitLoss 结转损益凭证模板配置
     * @return 结果
     */
    @Override
    public int updateFinClosingTemplateProfitLoss(FinClosingTemplateProfitLoss finClosingTemplateProfitLoss)
    {
        return finClosingTemplateProfitLossMapper.updateFinClosingTemplateProfitLoss(finClosingTemplateProfitLoss);
    }

    /**
     * 批量删除结转损益凭证模板配置
     * 
     * @param ids 需要删除的结转损益凭证模板配置主键
     * @return 结果
     */
    @Override
    public int deleteFinClosingTemplateProfitLossByIds(String[] ids)
    {
        return finClosingTemplateProfitLossMapper.deleteFinClosingTemplateProfitLossByIds(ids);
    }

    /**
     * 删除结转损益凭证模板配置信息
     * 
     * @param id 结转损益凭证模板配置主键
     * @return 结果
     */
    @Override
    public int deleteFinClosingTemplateProfitLossById(String id)
    {
        return finClosingTemplateProfitLossMapper.deleteFinClosingTemplateProfitLossById(id);
    }
}
