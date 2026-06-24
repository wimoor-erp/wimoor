package com.wimoor.finance.closing.service;

import com.wimoor.finance.closing.domain.FinClosingTemplate;

import java.util.List;

/**
 * 财务结算的各个模版名称Service接口
 * 
 * @author wimoor
 * @date 2026-03-17
 */
public interface IFinClosingTemplateService 
{
    /**
     * 查询财务结算的各个模版名称
     * 
     * @param id 财务结算的各个模版名称主键
     * @return 财务结算的各个模版名称
     */
    public FinClosingTemplate selectFinClosingTemplateById(String id);

    /**
     * 查询财务结算的各个模版名称列表
     * 
     * @param finClosingTemplate 财务结算的各个模版名称
     * @return 财务结算的各个模版名称集合
     */
    public List<FinClosingTemplate> selectFinClosingTemplateList(FinClosingTemplate finClosingTemplate);

    /**
     * 新增财务结算的各个模版名称
     * 
     * @param finClosingTemplate 财务结算的各个模版名称
     * @return 结果
     */
    public int insertFinClosingTemplate(FinClosingTemplate finClosingTemplate);

    /**
     * 修改财务结算的各个模版名称
     * 
     * @param finClosingTemplate 财务结算的各个模版名称
     * @return 结果
     */
    public int updateFinClosingTemplate(FinClosingTemplate finClosingTemplate);

    /**
     * 批量删除财务结算的各个模版名称
     * 
     * @param ids 需要删除的财务结算的各个模版名称主键集合
     * @return 结果
     */
    public int deleteFinClosingTemplateByIds(String[] ids);

    /**
     * 删除财务结算的各个模版名称信息
     * 
     * @param id 财务结算的各个模版名称主键
     * @return 结果
     */
    public int deleteFinClosingTemplateById(String id);

    /**
     * 根据租户ID和模板类型查询模板
     * 
     * @param groupId 租户ID
     * @param ftype 模板类型
     * @return 模板信息
     */
    public FinClosingTemplate selectByGroupAndType(String groupId, String ftype);
}
