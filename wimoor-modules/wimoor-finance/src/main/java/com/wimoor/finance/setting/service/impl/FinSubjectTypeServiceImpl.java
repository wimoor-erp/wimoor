package com.wimoor.finance.setting.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.wimoor.finance.setting.mapper.FinSubjectTypeMapper;
import com.wimoor.finance.setting.domain.FinSubjectType;
import com.wimoor.finance.setting.service.IFinSubjectTypeService;

/**
 * 科目类别Service业务层处理
 * 
 * @author wimoor
 * @date 2026-04-03
 */
@Service
public class FinSubjectTypeServiceImpl implements IFinSubjectTypeService 
{
    @Autowired
    private FinSubjectTypeMapper finSubjectTypeMapper;

    /**
     * 查询科目类别
     * 
     * @param id 科目类别主键
     * @return 科目类别
     */
    @Override
    public FinSubjectType selectFinSubjectTypeById(Integer id)
    {
        return finSubjectTypeMapper.selectFinSubjectTypeById(id);
    }

    /**
     * 查询科目类别列表
     * 
     * @param finSubjectType 科目类别
     * @return 科目类别
     */
    @Override
    public List<FinSubjectType> selectFinSubjectTypeList(FinSubjectType finSubjectType)
    {
        return finSubjectTypeMapper.selectFinSubjectTypeList(finSubjectType);
    }

    /**
     * 新增科目类别
     * 
     * @param finSubjectType 科目类别
     * @return 结果
     */
    @Override
    public int insertFinSubjectType(FinSubjectType finSubjectType)
    {
        return finSubjectTypeMapper.insertFinSubjectType(finSubjectType);
    }

    /**
     * 修改科目类别
     * 
     * @param finSubjectType 科目类别
     * @return 结果
     */
    @Override
    public int updateFinSubjectType(FinSubjectType finSubjectType)
    {
        return finSubjectTypeMapper.updateFinSubjectType(finSubjectType);
    }

    /**
     * 批量删除科目类别
     * 
     * @param ids 需要删除的科目类别主键
     * @return 结果
     */
    @Override
    public int deleteFinSubjectTypeByIds(Integer[] ids)
    {
        return finSubjectTypeMapper.deleteFinSubjectTypeByIds(ids);
    }

    /**
     * 删除科目类别信息
     * 
     * @param id 科目类别主键
     * @return 结果
     */
    @Override
    public int deleteFinSubjectTypeById(Integer id)
    {
        return finSubjectTypeMapper.deleteFinSubjectTypeById(id);
    }

    /**
     * 根据科目类型获取科目类别列表
     * 
     * @param categoryType 科目类型
     * @return 科目类别集合
     */
    @Override
    public List<FinSubjectType> selectFinSubjectTypeByCategoryType(String categoryType)
    {
        return finSubjectTypeMapper.selectFinSubjectTypeByCategoryType(categoryType);
    }
}
