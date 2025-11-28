package com.wimoor.finance.code.mapper;

import java.util.List;
import com.wimoor.finance.code.domain.FinCodeCategory;

/**
 * 编码规则分类Mapper接口
 * 
 * @author wimoor
 * @date 2025-11-03
 */
public interface FinCodeCategoryMapper 
{
    /**
     * 查询编码规则分类
     * 
     * @param id 编码规则分类主键
     * @return 编码规则分类
     */
    public FinCodeCategory selectFinCodeCategoryById(Long id);

    /**
     * 查询编码规则分类列表
     * 
     * @param finCodeCategory 编码规则分类
     * @return 编码规则分类集合
     */
    public List<FinCodeCategory> selectFinCodeCategoryList(FinCodeCategory finCodeCategory);

    /**
     * 新增编码规则分类
     * 
     * @param finCodeCategory 编码规则分类
     * @return 结果
     */
    public int insertFinCodeCategory(FinCodeCategory finCodeCategory);

    /**
     * 修改编码规则分类
     * 
     * @param finCodeCategory 编码规则分类
     * @return 结果
     */
    public int updateFinCodeCategory(FinCodeCategory finCodeCategory);

    /**
     * 删除编码规则分类
     * 
     * @param id 编码规则分类主键
     * @return 结果
     */
    public int deleteFinCodeCategoryById(Long id);

    /**
     * 批量删除编码规则分类
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFinCodeCategoryByIds(Long[] ids);
}
