package com.wimoor.finance.setting.service.impl;

import com.wimoor.finance.setting.domain.FinAccountingSubjectAuxiliarySetting;
import com.wimoor.finance.setting.mapper.FinAccountingSubjectAuxiliarySettingMapper;
import com.wimoor.finance.setting.service.IFinAccountingSubjectAuxiliarySettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 会计科目对应辅助核算分类设置Service业务层处理
 * 
 * @author wimoor
 * @date 2026-05-26
 */
@Service
public class FinAccountingSubjectAuxiliarySettingServiceImpl implements IFinAccountingSubjectAuxiliarySettingService 
{
    @Autowired
    private FinAccountingSubjectAuxiliarySettingMapper finAccountingSubjectAuxiliarySettingMapper;

    /**
     * 查询会计科目对应辅助核算分类设置
     * 
     * @param id 会计科目对应辅助核算分类设置主键
     * @return 会计科目对应辅助核算分类设置
     */
    @Override
    public FinAccountingSubjectAuxiliarySetting selectFinAccountingSubjectAuxiliarySettingById(String id)
    {
        return finAccountingSubjectAuxiliarySettingMapper.selectFinAccountingSubjectAuxiliarySettingById(id);
    }

    /**
     * 查询会计科目对应辅助核算分类设置列表
     * 
     * @param finAccountingSubjectAuxiliarySetting 会计科目对应辅助核算分类设置
     * @return 会计科目对应辅助核算分类设置
     */
    @Override
    public List<FinAccountingSubjectAuxiliarySetting> selectFinAccountingSubjectAuxiliarySettingList(FinAccountingSubjectAuxiliarySetting finAccountingSubjectAuxiliarySetting)
    {
        return finAccountingSubjectAuxiliarySettingMapper.selectFinAccountingSubjectAuxiliarySettingList(finAccountingSubjectAuxiliarySetting);
    }

    /**
     * 新增会计科目对应辅助核算分类设置
     * 
     * @param finAccountingSubjectAuxiliarySetting 会计科目对应辅助核算分类设置
     * @return 结果
     */
    @Override
    public int insertFinAccountingSubjectAuxiliarySetting(FinAccountingSubjectAuxiliarySetting finAccountingSubjectAuxiliarySetting)
    {
        return finAccountingSubjectAuxiliarySettingMapper.insertFinAccountingSubjectAuxiliarySetting(finAccountingSubjectAuxiliarySetting);
    }

    /**
     * 修改会计科目对应辅助核算分类设置
     * 
     * @param finAccountingSubjectAuxiliarySetting 会计科目对应辅助核算分类设置
     * @return 结果
     */
    @Override
    public int updateFinAccountingSubjectAuxiliarySetting(FinAccountingSubjectAuxiliarySetting finAccountingSubjectAuxiliarySetting)
    {
        return finAccountingSubjectAuxiliarySettingMapper.updateFinAccountingSubjectAuxiliarySetting(finAccountingSubjectAuxiliarySetting);
    }

    /**
     * 批量删除会计科目对应辅助核算分类设置
     * 
     * @param ids 需要删除的会计科目对应辅助核算分类设置主键
     * @return 结果
     */
    @Override
    public int deleteFinAccountingSubjectAuxiliarySettingByIds(String[] ids)
    {
        return finAccountingSubjectAuxiliarySettingMapper.deleteFinAccountingSubjectAuxiliarySettingByIds(ids);
    }

    /**
     * 删除会计科目对应辅助核算分类设置信息
     * 
     * @param id 会计科目对应辅助核算分类设置主键
     * @return 结果
     */
    @Override
    public int deleteFinAccountingSubjectAuxiliarySettingById(String id)
    {
        return finAccountingSubjectAuxiliarySettingMapper.deleteFinAccountingSubjectAuxiliarySettingById(id);
    }
}
