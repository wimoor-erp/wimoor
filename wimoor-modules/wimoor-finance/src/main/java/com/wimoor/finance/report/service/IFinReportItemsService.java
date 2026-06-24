package com.wimoor.finance.report.service;

import com.wimoor.finance.report.domain.FinReportItems;

import java.util.List;

/**
 * 报表项目Service接口
 *
 * @author wimoor
 * @date 2025-11-04
 */
public interface IFinReportItemsService
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
     * 查询所有父级项目列表（用于下拉选择）
     *
     * @param templateId 报表模板ID
     * @return 父级项目集合
     */
    public List<FinReportItems> selectAllParentItems(Long templateId);

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
     * 批量删除报表项目
     *
     * @param itemIds 需要删除的报表项目主键集合
     * @return 结果
     */
    public int deleteFinReportItemsByItemIds(Long[] itemIds);

    /**
     * 删除报表项目信息
     *
     * @param itemId 报表项目主键
     * @return 结果
     */
    public int deleteFinReportItemsByItemId(Long itemId);
    
    /**
     * 初始化模板的报表项目
     * 会删除现有项目并根据模板类型重新创建默认项目
     *
     * @param templateId 模板ID
     * @param groupid 租户ID
     * @param templateType 模板类型
     * @return 创建的项目数量
     */
    public int initTemplateItems(Long templateId, String groupid, String templateType);
}