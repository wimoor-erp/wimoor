package com.wimoor.finance.closing.mapper;

import com.wimoor.finance.closing.domain.FinClosingTemplateAmazon;

import java.util.List;

/**
 * 结账科目对应关系Mapper接口
 * 
 * @author wimoor
 * @date 2026-03-19
 */
public interface FinClosingTemplateAmazonMapper 
{
    /**
     * 查询结账科目对应关系
     * 
     * @param id 结账科目对应关系主键
     * @return 结账科目对应关系
     */
    public FinClosingTemplateAmazon selectFinClosingTemplateAmazonById(String id);

    /**
     * 查询结账科目对应关系列表
     * 
     * @param finClosingTemplateAmazon 结账科目对应关系
     * @return 结账科目对应关系集合
     */
    public List<FinClosingTemplateAmazon> selectFinClosingTemplateAmazonList(FinClosingTemplateAmazon finClosingTemplateAmazon);

    /**
     * 新增结账科目对应关系
     * 
     * @param finClosingTemplateAmazon 结账科目对应关系
     * @return 结果
     */
    public int insertFinClosingTemplateAmazon(FinClosingTemplateAmazon finClosingTemplateAmazon);

    /**
     * 修改结账科目对应关系
     * 
     * @param finClosingTemplateAmazon 结账科目对应关系
     * @return 结果
     */
    public int updateFinClosingTemplateAmazon(FinClosingTemplateAmazon finClosingTemplateAmazon);

    /**
     * 删除结账科目对应关系
     * 
     * @param id 结账科目对应关系主键
     * @return 结果
     */
    public int deleteFinClosingTemplateAmazonById(String id);

    /**
     * 批量删除结账科目对应关系
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFinClosingTemplateAmazonByIds(List<String> ids);
}
