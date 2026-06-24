package com.wimoor.finance.closing.service.impl;

import com.wimoor.finance.closing.domain.FinClosingTemplateAmazon;
import com.wimoor.finance.closing.mapper.FinClosingTemplateAmazonMapper;
import com.wimoor.finance.closing.service.IFinClosingTemplateAmazonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 结账科目对应关系Service业务层处理
 * 
 * @author wimoor
 * @date 2026-03-19
 */
@Service
public class FinClosingTemplateAmazonServiceImpl implements IFinClosingTemplateAmazonService 
{
    @Autowired
    private FinClosingTemplateAmazonMapper finClosingTemplateAmazonMapper;

    /**
     * 查询结账科目对应关系
     * 
     * @param id 结账科目对应关系主键
     * @return 结账科目对应关系
     */
    @Override
    public FinClosingTemplateAmazon selectFinClosingTemplateAmazonById(String id)
    {
        return finClosingTemplateAmazonMapper.selectFinClosingTemplateAmazonById(id);
    }

    /**
     * 查询结账科目对应关系列表
     * 
     * @param finClosingTemplateAmazon 结账科目对应关系
     * @return 结账科目对应关系
     */
    @Override
    public List<FinClosingTemplateAmazon> selectFinClosingTemplateAmazonList(FinClosingTemplateAmazon finClosingTemplateAmazon)
    {
        return finClosingTemplateAmazonMapper.selectFinClosingTemplateAmazonList(finClosingTemplateAmazon);
    }

    /**
     * 新增结账科目对应关系
     * 
     * @param finClosingTemplateAmazon 结账科目对应关系
     * @return 结果
     */
    @Override
    public int insertFinClosingTemplateAmazon(FinClosingTemplateAmazon finClosingTemplateAmazon)
    {
        return finClosingTemplateAmazonMapper.insertFinClosingTemplateAmazon(finClosingTemplateAmazon);
    }

    /**
     * 修改结账科目对应关系
     * 
     * @param finClosingTemplateAmazon 结账科目对应关系
     * @return 结果
     */
    @Override
    public int updateFinClosingTemplateAmazon(FinClosingTemplateAmazon finClosingTemplateAmazon)
    {
        return finClosingTemplateAmazonMapper.updateFinClosingTemplateAmazon(finClosingTemplateAmazon);
    }

    /**
     * 批量删除结账科目对应关系
     * 
     * @param ids 需要删除的结账科目对应关系主键
     * @return 结果
     */
    @Override
    public int deleteFinClosingTemplateAmazonByIds(List<String> ids)
    {
        return finClosingTemplateAmazonMapper.deleteFinClosingTemplateAmazonByIds(ids);
    }

    /**
     * 删除结账科目对应关系信息
     * 
     * @param id 结账科目对应关系主键
     * @return 结果
     */
    @Override
    public int deleteFinClosingTemplateAmazonById(String id)
    {
        return finClosingTemplateAmazonMapper.deleteFinClosingTemplateAmazonById(id);
    }
}
