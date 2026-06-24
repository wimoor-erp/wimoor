package com.wimoor.finance.closing.mapper;

import java.util.List;
import com.wimoor.finance.closing.domain.FinClosingTemplateProfitLoss;

/**
 * 结转损益凭证模板配置Mapper接口
 * 
 * @author wimoor
 * @date 2026-06-10
 */
public interface FinClosingTemplateProfitLossMapper 
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
     * 删除结转损益凭证模板配置
     * 
     * @param id 结转损益凭证模板配置主键
     * @return 结果
     */
    public int deleteFinClosingTemplateProfitLossById(String id);

    /**
     * 批量删除结转损益凭证模板配置
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFinClosingTemplateProfitLossByIds(String[] ids);
}
