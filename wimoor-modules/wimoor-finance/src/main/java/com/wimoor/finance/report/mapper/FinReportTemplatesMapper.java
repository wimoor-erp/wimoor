package com.wimoor.finance.report.mapper;

import java.util.List;
import com.wimoor.finance.report.domain.FinReportTemplates;

/**
 * 财务报表模板Mapper接口
 * 
 * @author wimoor
 * @date 2025-11-04
 */
public interface FinReportTemplatesMapper 
{
    /**
     * 查询财务报表模板
     * 
     * @param templateId 财务报表模板主键
     * @return 财务报表模板
     */
    public FinReportTemplates selectFinReportTemplatesByTemplateId(Long templateId);

    /**
     * 查询财务报表模板列表
     * 
     * @param finReportTemplates 财务报表模板
     * @return 财务报表模板集合
     */
    public List<FinReportTemplates> selectFinReportTemplatesList(FinReportTemplates finReportTemplates);

    /**
     * 新增财务报表模板
     * 
     * @param finReportTemplates 财务报表模板
     * @return 结果
     */
    public int insertFinReportTemplates(FinReportTemplates finReportTemplates);

    /**
     * 修改财务报表模板
     * 
     * @param finReportTemplates 财务报表模板
     * @return 结果
     */
    public int updateFinReportTemplates(FinReportTemplates finReportTemplates);

    /**
     * 删除财务报表模板
     * 
     * @param templateId 财务报表模板主键
     * @return 结果
     */
    public int deleteFinReportTemplatesByTemplateId(Long templateId);

    /**
     * 批量删除财务报表模板
     * 
     * @param templateIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFinReportTemplatesByTemplateIds(Long[] templateIds);
}
