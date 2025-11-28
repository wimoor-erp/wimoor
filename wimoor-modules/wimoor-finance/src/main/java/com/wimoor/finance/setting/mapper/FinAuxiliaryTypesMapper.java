package com.wimoor.finance.setting.mapper;

import java.util.List;
import com.wimoor.finance.setting.domain.FinAuxiliaryTypes;

/**
 * 辅助核算类别Mapper接口
 * 
 * @author wimoor
 * @date 2025-11-04
 */
public interface FinAuxiliaryTypesMapper 
{
    /**
     * 查询辅助核算类别
     * 
     * @param typeId 辅助核算类别主键
     * @return 辅助核算类别
     */
    public FinAuxiliaryTypes selectFinAuxiliaryTypesByTypeId(Long typeId);

    /**
     * 查询辅助核算类别列表
     * 
     * @param finAuxiliaryTypes 辅助核算类别
     * @return 辅助核算类别集合
     */
    public List<FinAuxiliaryTypes> selectFinAuxiliaryTypesList(FinAuxiliaryTypes finAuxiliaryTypes);

    /**
     * 新增辅助核算类别
     * 
     * @param finAuxiliaryTypes 辅助核算类别
     * @return 结果
     */
    public int insertFinAuxiliaryTypes(FinAuxiliaryTypes finAuxiliaryTypes);

    /**
     * 修改辅助核算类别
     * 
     * @param finAuxiliaryTypes 辅助核算类别
     * @return 结果
     */
    public int updateFinAuxiliaryTypes(FinAuxiliaryTypes finAuxiliaryTypes);

    /**
     * 删除辅助核算类别
     * 
     * @param typeId 辅助核算类别主键
     * @return 结果
     */
    public int deleteFinAuxiliaryTypesByTypeId(Long typeId);

    /**
     * 批量删除辅助核算类别
     * 
     * @param typeIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFinAuxiliaryTypesByTypeIds(Long[] typeIds);
}
