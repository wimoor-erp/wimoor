package com.wimoor.finance.setting.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.wimoor.finance.setting.mapper.FinAuxiliaryTypesMapper;
import com.wimoor.finance.setting.domain.FinAuxiliaryTypes;
import com.wimoor.finance.setting.service.IFinAuxiliaryTypesService;

/**
 * 辅助核算类别Service业务层处理
 * 
 * @author wimoor
 * @date 2025-11-04
 */
@Service
public class FinAuxiliaryTypesServiceImpl implements IFinAuxiliaryTypesService 
{
    @Autowired
    private FinAuxiliaryTypesMapper finAuxiliaryTypesMapper;

    /**
     * 查询辅助核算类别
     * 
     * @param typeId 辅助核算类别主键
     * @return 辅助核算类别
     */
    @Override
    public FinAuxiliaryTypes selectFinAuxiliaryTypesByTypeId(Long typeId)
    {
        return finAuxiliaryTypesMapper.selectFinAuxiliaryTypesByTypeId(typeId);
    }

    /**
     * 查询辅助核算类别列表
     * 
     * @param finAuxiliaryTypes 辅助核算类别
     * @return 辅助核算类别
     */
    @Override
    public List<FinAuxiliaryTypes> selectFinAuxiliaryTypesList(FinAuxiliaryTypes finAuxiliaryTypes)
    {
        return finAuxiliaryTypesMapper.selectFinAuxiliaryTypesList(finAuxiliaryTypes);
    }

    /**
     * 新增辅助核算类别
     * 
     * @param finAuxiliaryTypes 辅助核算类别
     * @return 结果
     */
    @Override
    public int insertFinAuxiliaryTypes(FinAuxiliaryTypes finAuxiliaryTypes)
    {
        return finAuxiliaryTypesMapper.insertFinAuxiliaryTypes(finAuxiliaryTypes);
    }

    /**
     * 修改辅助核算类别
     * 
     * @param finAuxiliaryTypes 辅助核算类别
     * @return 结果
     */
    @Override
    public int updateFinAuxiliaryTypes(FinAuxiliaryTypes finAuxiliaryTypes)
    {
        return finAuxiliaryTypesMapper.updateFinAuxiliaryTypes(finAuxiliaryTypes);
    }

    /**
     * 批量删除辅助核算类别
     * 
     * @param typeIds 需要删除的辅助核算类别主键
     * @return 结果
     */
    @Override
    public int deleteFinAuxiliaryTypesByTypeIds(Long[] typeIds)
    {
        return finAuxiliaryTypesMapper.deleteFinAuxiliaryTypesByTypeIds(typeIds);
    }

    /**
     * 删除辅助核算类别信息
     * 
     * @param typeId 辅助核算类别主键
     * @return 结果
     */
    @Override
    public int deleteFinAuxiliaryTypesByTypeId(Long typeId)
    {
        return finAuxiliaryTypesMapper.deleteFinAuxiliaryTypesByTypeId(typeId);
    }
}
