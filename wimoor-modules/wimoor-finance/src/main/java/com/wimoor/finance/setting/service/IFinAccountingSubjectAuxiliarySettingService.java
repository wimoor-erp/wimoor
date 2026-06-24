package com.wimoor.finance.setting.service;

import com.wimoor.finance.setting.domain.FinAccountingSubjectAuxiliarySetting;

import java.util.List;

/**
 * 会计科目对应辅助核算分类设置Service接口
 * 
 * @author wimoor
 * @date 2026-05-26
 */
public interface IFinAccountingSubjectAuxiliarySettingService 
{
    /**
     * 查询会计科目对应辅助核算分类设置
     * 
     * @param id 会计科目对应辅助核算分类设置主键
     * @return 会计科目对应辅助核算分类设置
     */
    public FinAccountingSubjectAuxiliarySetting selectFinAccountingSubjectAuxiliarySettingById(String id);

    /**
     * 查询会计科目对应辅助核算分类设置列表
     * 
     * @param finAccountingSubjectAuxiliarySetting 会计科目对应辅助核算分类设置
     * @return 会计科目对应辅助核算分类设置集合
     */
    public List<FinAccountingSubjectAuxiliarySetting> selectFinAccountingSubjectAuxiliarySettingList(FinAccountingSubjectAuxiliarySetting finAccountingSubjectAuxiliarySetting);

    /**
     * 新增会计科目对应辅助核算分类设置
     * 
     * @param finAccountingSubjectAuxiliarySetting 会计科目对应辅助核算分类设置
     * @return 结果
     */
    public int insertFinAccountingSubjectAuxiliarySetting(FinAccountingSubjectAuxiliarySetting finAccountingSubjectAuxiliarySetting);

    /**
     * 修改会计科目对应辅助核算分类设置
     * 
     * @param finAccountingSubjectAuxiliarySetting 会计科目对应辅助核算分类设置
     * @return 结果
     */
    public int updateFinAccountingSubjectAuxiliarySetting(FinAccountingSubjectAuxiliarySetting finAccountingSubjectAuxiliarySetting);

    /**
     * 批量删除会计科目对应辅助核算分类设置
     * 
     * @param ids 需要删除的会计科目对应辅助核算分类设置主键集合
     * @return 结果
     */
    public int deleteFinAccountingSubjectAuxiliarySettingByIds(String[] ids);

    /**
     * 删除会计科目对应辅助核算分类设置信息
     * 
     * @param id 会计科目对应辅助核算分类设置主键
     * @return 结果
     */
    public int deleteFinAccountingSubjectAuxiliarySettingById(String id);
}
