package com.wimoor.finance.closing.service;

import com.wimoor.finance.closing.domain.FinClosingTemplateItem;

import java.util.List;

/**
 * 结账科目对应关系Service接口
 * 
 * @author wimoor
 * @date 2026-03-19
 */
public interface IFinClosingTemplateItemService
{
    /**
     * 查询结账科目对应关系
     * 
     * @param id 结账科目对应关系主键
     * @return 结账科目对应关系
     */
    public FinClosingTemplateItem selectFinClosingTemplateItemById(String id);

    /**
     * 查询结账科目对应关系列表
     * 
     * @param finClosingTemplateItem 结账科目对应关系
     * @return 结账科目对应关系集合
     */
    public List<FinClosingTemplateItem> selectFinClosingTemplateItemList(FinClosingTemplateItem finClosingTemplateItem);

    /**
     * 新增结账科目对应关系
     * 
     * @param finClosingTemplateItem 结账科目对应关系
     * @return 结果
     */
    public int insertFinClosingTemplateItem(FinClosingTemplateItem finClosingTemplateItem);

    /**
     * 修改结账科目对应关系
     * 
     * @param finClosingTemplateItem 结账科目对应关系
     * @return 结果
     */
    public int updateFinClosingTemplateItem(FinClosingTemplateItem finClosingTemplateItem);

    /**
     * 批量删除结账科目对应关系
     * 
     * @param ids 需要删除的结账科目对应关系主键集合
     * @return 结果
     */
    public int deleteFinClosingTemplateItemByIds(List<String> ids);

    /**
     * 删除结账科目对应关系信息
     * 
     * @param id 结账科目对应关系主键
     * @return 结果
     */
    public int deleteFinClosingTemplateItemById(String id);
}
