package com.wimoor.finance.closing.service.impl;

import com.wimoor.finance.closing.domain.FinClosingTemplateItem;
import com.wimoor.finance.closing.mapper.FinClosingTemplateItemMapper;
import com.wimoor.finance.closing.service.IFinClosingTemplateItemService;
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
public class FinClosingTemplateItemServiceImpl implements IFinClosingTemplateItemService
{
    @Autowired
    private FinClosingTemplateItemMapper finClosingTemplateItemMapper;

    /**
     * 查询结账科目对应关系
     * 
     * @param id 结账科目对应关系主键
     * @return 结账科目对应关系
     */
    @Override
    public FinClosingTemplateItem selectFinClosingTemplateItemById(String id)
    {
        return finClosingTemplateItemMapper.selectFinClosingTemplateItemById(id);
    }

    /**
     * 查询结账科目对应关系列表
     * 
     * @param finClosingTemplateItem 结账科目对应关系
     * @return 结账科目对应关系
     */
    @Override
    public List<FinClosingTemplateItem> selectFinClosingTemplateItemList(FinClosingTemplateItem finClosingTemplateItem)
    {
        return finClosingTemplateItemMapper.selectFinClosingTemplateItemList(finClosingTemplateItem);
    }

    /**
     * 新增结账科目对应关系
     * 
     * @param finClosingTemplateItem 结账科目对应关系
     * @return 结果
     */
    @Override
    public int insertFinClosingTemplateItem(FinClosingTemplateItem finClosingTemplateItem)
    {
        return finClosingTemplateItemMapper.insertFinClosingTemplateItem(finClosingTemplateItem);
    }

    /**
     * 修改结账科目对应关系
     * 
     * @param finClosingTemplateItem 结账科目对应关系
     * @return 结果
     */
    @Override
    public int updateFinClosingTemplateItem(FinClosingTemplateItem finClosingTemplateItem)
    {
        return finClosingTemplateItemMapper.updateFinClosingTemplateItem(finClosingTemplateItem);
    }

    /**
     * 批量删除结账科目对应关系
     * 
     * @param ids 需要删除的结账科目对应关系主键
     * @return 结果
     */
    @Override
    public int deleteFinClosingTemplateItemByIds(List<String> ids)
    {
        return finClosingTemplateItemMapper.deleteFinClosingTemplateItemByIds(ids);
    }

    /**
     * 删除结账科目对应关系信息
     * 
     * @param id 结账科目对应关系主键
     * @return 结果
     */
    @Override
    public int deleteFinClosingTemplateItemById(String id)
    {
        return finClosingTemplateItemMapper.deleteFinClosingTemplateItemById(id);
    }
}
