package com.wimoor.finance.closing.mapper;

import com.wimoor.finance.closing.domain.FinClosingTemplate;
import java.util.List;

/**
 * 财务结算的各个模版名称Mapper接口
 * 
 * @author wimoor
 * @date 2026-03-17
 */
public interface FinClosingTemplateMapper 
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
     * 删除财务结算的各个模版名称
     * 
     * @param id 财务结算的各个模版名称主键
     * @return 结果
     */
    public int deleteFinClosingTemplateById(String id);

    /**
     * 批量删除财务结算的各个模版名称
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFinClosingTemplateByIds(String[] ids);
}
