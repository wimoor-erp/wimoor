package com.wimoor.finance.code.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.wimoor.finance.code.mapper.FinCodeCategoryMapper;
import com.wimoor.finance.code.domain.FinCodeCategory;
import com.wimoor.finance.code.service.IFinCodeCategoryService;

/**
 * 编码规则分类Service业务层处理
 * 
 * @author wimoor
 * @date 2025-11-03
 */
@Service
public class FinCodeCategoryServiceImpl implements IFinCodeCategoryService 
{
    @Autowired
    private FinCodeCategoryMapper finCodeCategoryMapper;

    /**
     * 查询编码规则分类
     * 
     * @param id 编码规则分类主键
     * @return 编码规则分类
     */
    @Override
    public FinCodeCategory selectFinCodeCategoryById(Long id)
    {
        return finCodeCategoryMapper.selectFinCodeCategoryById(id);
    }

    /**
     * 查询编码规则分类列表
     * 
     * @param finCodeCategory 编码规则分类
     * @return 编码规则分类
     */
    @Override
    public List<FinCodeCategory> selectFinCodeCategoryList(FinCodeCategory finCodeCategory)
    {
        return finCodeCategoryMapper.selectFinCodeCategoryList(finCodeCategory);
    }

    /**
     * 新增编码规则分类
     * 
     * @param finCodeCategory 编码规则分类
     * @return 结果
     */
    @Override
    public int insertFinCodeCategory(FinCodeCategory finCodeCategory)
    {
        return finCodeCategoryMapper.insertFinCodeCategory(finCodeCategory);
    }

    /**
     * 修改编码规则分类
     * 
     * @param finCodeCategory 编码规则分类
     * @return 结果
     */
    @Override
    public int updateFinCodeCategory(FinCodeCategory finCodeCategory)
    {
        return finCodeCategoryMapper.updateFinCodeCategory(finCodeCategory);
    }

    /**
     * 批量删除编码规则分类
     * 
     * @param ids 需要删除的编码规则分类主键
     * @return 结果
     */
    @Override
    public int deleteFinCodeCategoryByIds(Long[] ids)
    {
        return finCodeCategoryMapper.deleteFinCodeCategoryByIds(ids);
    }

    /**
     * 删除编码规则分类信息
     * 
     * @param id 编码规则分类主键
     * @return 结果
     */
    @Override
    public int deleteFinCodeCategoryById(Long id)
    {
        return finCodeCategoryMapper.deleteFinCodeCategoryById(id);
    }
}
