package com.wimoor.finance.setting.service;

import java.util.List;
import com.wimoor.finance.setting.domain.FinSubjectType;

/**
 * 科目类别Service接口
 * 
 * @author wimoor
 * @date 2026-04-03
 */
public interface IFinSubjectTypeService 
{
    /**
     * 查询科目类别
     * 
     * @param id 科目类别主键
     * @return 科目类别
     */
    public FinSubjectType selectFinSubjectTypeById(Integer id);

    /**
     * 查询科目类别列表
     * 
     * @param finSubjectType 科目类别
     * @return 科目类别集合
     */
    public List<FinSubjectType> selectFinSubjectTypeList(FinSubjectType finSubjectType);

    /**
     * 新增科目类别
     * 
     * @param finSubjectType 科目类别
     * @return 结果
     */
    public int insertFinSubjectType(FinSubjectType finSubjectType);

    /**
     * 修改科目类别
     * 
     * @param finSubjectType 科目类别
     * @return 结果
     */
    public int updateFinSubjectType(FinSubjectType finSubjectType);

    /**
     * 批量删除科目类别
     * 
     * @param ids 需要删除的科目类别主键集合
     * @return 结果
     */
    public int deleteFinSubjectTypeByIds(Integer[] ids);

    /**
     * 删除科目类别信息
     * 
     * @param id 科目类别主键
     * @return 结果
     */
    public int deleteFinSubjectTypeById(Integer id);

    /**
     * 根据科目类型获取科目类别列表
     * 
     * @param categoryType 科目类型
     * @return 科目类别集合
     */
    public List<FinSubjectType> selectFinSubjectTypeByCategoryType(String categoryType);
}
