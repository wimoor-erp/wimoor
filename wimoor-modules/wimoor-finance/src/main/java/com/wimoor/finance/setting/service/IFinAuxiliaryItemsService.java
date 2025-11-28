package com.wimoor.finance.setting.service;

import java.util.List;
import com.wimoor.finance.setting.domain.FinAuxiliaryItems;

/**
 * 辅助核算具体项目Service接口
 * 
 * @author wimoor
 * @date 2025-11-04
 */
public interface IFinAuxiliaryItemsService 
{
    /**
     * 查询辅助核算具体项目
     * 
     * @param itemId 辅助核算具体项目主键
     * @return 辅助核算具体项目
     */
    public FinAuxiliaryItems selectFinAuxiliaryItemsByItemId(Long itemId);

    /**
     * 查询辅助核算具体项目列表
     * 
     * @param finAuxiliaryItems 辅助核算具体项目
     * @return 辅助核算具体项目集合
     */
    public List<FinAuxiliaryItems> selectFinAuxiliaryItemsList(FinAuxiliaryItems finAuxiliaryItems);

    /**
     * 新增辅助核算具体项目
     * 
     * @param finAuxiliaryItems 辅助核算具体项目
     * @return 结果
     */
    public int insertFinAuxiliaryItems(FinAuxiliaryItems finAuxiliaryItems);

    /**
     * 修改辅助核算具体项目
     * 
     * @param finAuxiliaryItems 辅助核算具体项目
     * @return 结果
     */
    public int updateFinAuxiliaryItems(FinAuxiliaryItems finAuxiliaryItems);

    /**
     * 批量删除辅助核算具体项目
     * 
     * @param itemIds 需要删除的辅助核算具体项目主键集合
     * @return 结果
     */
    public int deleteFinAuxiliaryItemsByItemIds(Long[] itemIds);

    /**
     * 删除辅助核算具体项目信息
     * 
     * @param itemId 辅助核算具体项目主键
     * @return 结果
     */
    public int deleteFinAuxiliaryItemsByItemId(Long itemId);
}
