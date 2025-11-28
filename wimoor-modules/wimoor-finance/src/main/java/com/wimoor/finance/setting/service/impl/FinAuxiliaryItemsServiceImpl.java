package com.wimoor.finance.setting.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.wimoor.finance.setting.mapper.FinAuxiliaryItemsMapper;
import com.wimoor.finance.setting.domain.FinAuxiliaryItems;
import com.wimoor.finance.setting.service.IFinAuxiliaryItemsService;

/**
 * 辅助核算具体项目Service业务层处理
 * 
 * @author wimoor
 * @date 2025-11-04
 */
@Service
public class FinAuxiliaryItemsServiceImpl implements IFinAuxiliaryItemsService 
{
    @Autowired
    private FinAuxiliaryItemsMapper finAuxiliaryItemsMapper;

    /**
     * 查询辅助核算具体项目
     * 
     * @param itemId 辅助核算具体项目主键
     * @return 辅助核算具体项目
     */
    @Override
    public FinAuxiliaryItems selectFinAuxiliaryItemsByItemId(Long itemId)
    {
        return finAuxiliaryItemsMapper.selectFinAuxiliaryItemsByItemId(itemId);
    }

    /**
     * 查询辅助核算具体项目列表
     * 
     * @param finAuxiliaryItems 辅助核算具体项目
     * @return 辅助核算具体项目
     */
    @Override
    public List<FinAuxiliaryItems> selectFinAuxiliaryItemsList(FinAuxiliaryItems finAuxiliaryItems)
    {
        return finAuxiliaryItemsMapper.selectFinAuxiliaryItemsList(finAuxiliaryItems);
    }

    /**
     * 新增辅助核算具体项目
     * 
     * @param finAuxiliaryItems 辅助核算具体项目
     * @return 结果
     */
    @Override
    public int insertFinAuxiliaryItems(FinAuxiliaryItems finAuxiliaryItems)
    {
        return finAuxiliaryItemsMapper.insertFinAuxiliaryItems(finAuxiliaryItems);
    }

    /**
     * 修改辅助核算具体项目
     * 
     * @param finAuxiliaryItems 辅助核算具体项目
     * @return 结果
     */
    @Override
    public int updateFinAuxiliaryItems(FinAuxiliaryItems finAuxiliaryItems)
    {
        return finAuxiliaryItemsMapper.updateFinAuxiliaryItems(finAuxiliaryItems);
    }

    /**
     * 批量删除辅助核算具体项目
     * 
     * @param itemIds 需要删除的辅助核算具体项目主键
     * @return 结果
     */
    @Override
    public int deleteFinAuxiliaryItemsByItemIds(Long[] itemIds)
    {
        return finAuxiliaryItemsMapper.deleteFinAuxiliaryItemsByItemIds(itemIds);
    }

    /**
     * 删除辅助核算具体项目信息
     * 
     * @param itemId 辅助核算具体项目主键
     * @return 结果
     */
    @Override
    public int deleteFinAuxiliaryItemsByItemId(Long itemId)
    {
        return finAuxiliaryItemsMapper.deleteFinAuxiliaryItemsByItemId(itemId);
    }
}
