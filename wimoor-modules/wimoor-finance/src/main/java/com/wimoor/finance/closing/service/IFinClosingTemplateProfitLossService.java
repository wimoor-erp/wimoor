package com.wimoor.finance.closing.service;

import java.util.List;
import com.wimoor.finance.closing.domain.FinClosingTemplateProfitLoss;

/**
 * 结转损益凭证模板配置Service接口
 * 
 * @author wimoor
 * @date 2026-06-10
 */
public interface IFinClosingTemplateProfitLossService 
{
    /**
     * 查询结转损益凭证模板配置
     * 
     * @param id 结转损益凭证模板配置主键
     * @return 结转损益凭证模板配置
     */
    public FinClosingTemplateProfitLoss selectFinClosingTemplateProfitLossById(String id);

    /**
     * 查询结转损益凭证模板配置列表
     * 
     * @param finClosingTemplateProfitLoss 结转损益凭证模板配置
     * @return 结转损益凭证模板配置集合
     */
    public List<FinClosingTemplateProfitLoss> selectFinClosingTemplateProfitLossList(FinClosingTemplateProfitLoss finClosingTemplateProfitLoss);

    /**
     * 新增结转损益凭证模板配置
     * 
     * @param finClosingTemplateProfitLoss 结转损益凭证模板配置
     * @return 结果
     */
    public int insertFinClosingTemplateProfitLoss(FinClosingTemplateProfitLoss finClosingTemplateProfitLoss);

    /**
     * 修改结转损益凭证模板配置
     * 
     * @param finClosingTemplateProfitLoss 结转损益凭证模板配置
     * @return 结果
     */
    public int updateFinClosingTemplateProfitLoss(FinClosingTemplateProfitLoss finClosingTemplateProfitLoss);

    /**
     * 批量删除结转损益凭证模板配置
     * 
     * @param ids 需要删除的结转损益凭证模板配置主键集合
     * @return 结果
     */
    public int deleteFinClosingTemplateProfitLossByIds(String[] ids);

    /**
     * 删除结转损益凭证模板配置信息
     * 
     * @param id 结转损益凭证模板配置主键
     * @return 结果
     */
    public int deleteFinClosingTemplateProfitLossById(String id);
}
