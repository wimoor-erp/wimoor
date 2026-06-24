package com.wimoor.finance.closing.service.impl;

import com.wimoor.finance.closing.domain.FinClosingTemplate;
import com.wimoor.finance.closing.mapper.FinClosingTemplateMapper;
import com.wimoor.finance.closing.service.IFinClosingTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 财务结算的各个模版名称Service业务层处理
 * 
 * @author wimoor
 * @date 2026-03-17
 */
@Service
public class FinClosingTemplateServiceImpl implements IFinClosingTemplateService 
{
    @Autowired
    private FinClosingTemplateMapper finClosingTemplateMapper;

    /**
     * 查询财务结算的各个模版名称
     * 
     * @param id 财务结算的各个模版名称主键
     * @return 财务结算的各个模版名称
     */
    @Override
    public FinClosingTemplate selectFinClosingTemplateById(String id)
    {
        return finClosingTemplateMapper.selectFinClosingTemplateById(id);
    }

    /**
     * 查询财务结算的各个模版名称列表
     * 
     * @param finClosingTemplate 财务结算的各个模版名称
     * @return 财务结算的各个模版名称
     */
    @Override
    public List<FinClosingTemplate> selectFinClosingTemplateList(FinClosingTemplate finClosingTemplate)
    {
        return finClosingTemplateMapper.selectFinClosingTemplateList(finClosingTemplate);
    }

    /**
     * 新增财务结算的各个模版名称
     * 
     * @param finClosingTemplate 财务结算的各个模版名称
     * @return 结果
     */
    @Override
    public int insertFinClosingTemplate(FinClosingTemplate finClosingTemplate)
    {
        return finClosingTemplateMapper.insertFinClosingTemplate(finClosingTemplate);
    }

    /**
     * 修改财务结算的各个模版名称
     * 
     * @param finClosingTemplate 财务结算的各个模版名称
     * @return 结果
     */
    @Override
    public int updateFinClosingTemplate(FinClosingTemplate finClosingTemplate)
    {
        return finClosingTemplateMapper.updateFinClosingTemplate(finClosingTemplate);
    }

    /**
     * 批量删除财务结算的各个模版名称
     * 
     * @param ids 需要删除的财务结算的各个模版名称主键
     * @return 结果
     */
    @Override
    public int deleteFinClosingTemplateByIds(String[] ids)
    {
        return finClosingTemplateMapper.deleteFinClosingTemplateByIds(ids);
    }

    /**
     * 删除财务结算的各个模版名称信息
     * 
     * @param id 财务结算的各个模版名称主键
     * @return 结果
     */
    @Override
    public int deleteFinClosingTemplateById(String id)
    {
        return finClosingTemplateMapper.deleteFinClosingTemplateById(id);
    }

    /**
     * 根据租户ID和模板类型查询模板
     * 
     * @param groupId 租户ID
     * @param ftype 模板类型
     * @return 模板信息
     */
    @Override
    public FinClosingTemplate selectByGroupAndType(String groupId, String ftype)
    {
        FinClosingTemplate query = new FinClosingTemplate();
        query.setGroupid(groupId);
        query.setFtype(ftype);
        List<FinClosingTemplate> list = finClosingTemplateMapper.selectFinClosingTemplateList(query);
        if (list != null && !list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }
}
