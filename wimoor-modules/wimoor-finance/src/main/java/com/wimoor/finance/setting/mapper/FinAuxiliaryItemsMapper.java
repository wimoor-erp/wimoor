package com.wimoor.finance.setting.mapper;

import java.util.List;
import com.wimoor.finance.setting.domain.FinAuxiliaryItems;

/**
 * 辅助核算具体项目Mapper接口
 * 
 * @author wimoor
 * @date 2025-11-04
 */
public interface FinAuxiliaryItemsMapper 
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
     * 删除辅助核算具体项目
     * 
     * @param itemId 辅助核算具体项目主键
     * @return 结果
     */
    public int deleteFinAuxiliaryItemsByItemId(Long itemId);

    /**
     * 批量删除辅助核算具体项目
     * 
     * @param itemIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFinAuxiliaryItemsByItemIds(Long[] itemIds);
}
