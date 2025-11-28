package com.wimoor.finance.report.mapper;

import java.util.List;
import com.wimoor.finance.report.domain.FinReportItems;

/**
 * 报表项目Mapper接口
 * 
 * @author wimoor
 * @date 2025-11-04
 */
public interface FinReportItemsMapper 
{
    /**
     * 查询报表项目
     * 
     * @param itemId 报表项目主键
     * @return 报表项目
     */
    public FinReportItems selectFinReportItemsByItemId(Long itemId);

    /**
     * 查询报表项目列表
     * 
     * @param finReportItems 报表项目
     * @return 报表项目集合
     */
    public List<FinReportItems> selectFinReportItemsList(FinReportItems finReportItems);

    /**
     * 新增报表项目
     * 
     * @param finReportItems 报表项目
     * @return 结果
     */
    public int insertFinReportItems(FinReportItems finReportItems);

    /**
     * 修改报表项目
     * 
     * @param finReportItems 报表项目
     * @return 结果
     */
    public int updateFinReportItems(FinReportItems finReportItems);

    /**
     * 删除报表项目
     * 
     * @param itemId 报表项目主键
     * @return 结果
     */
    public int deleteFinReportItemsByItemId(Long itemId);

    /**
     * 批量删除报表项目
     * 
     * @param itemIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFinReportItemsByItemIds(Long[] itemIds);
}
