package com.wimoor.finance.report.service;

import java.util.List;
import java.util.Map;

import com.wimoor.finance.report.domain.FinReportTemplates;
import com.wimoor.finance.report.domain.dto.ReportGenerateRequest;
import com.wimoor.finance.report.domain.dto.ReportGenerateResponse;

/**
 * 财务报表模板Service接口
 * 
 * @author wimoor
 * @date 2025-11-04
 */
public interface IFinReportTemplatesService 
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
     * 批量删除财务报表模板
     * 
     * @param templateIds 需要删除的财务报表模板主键集合
     * @return 结果
     */
    public int deleteFinReportTemplatesByTemplateIds(Long[] templateIds);

    /**
     * 删除财务报表模板信息
     * 
     * @param templateId 财务报表模板主键
     * @return 结果
     */
    public int deleteFinReportTemplatesByTemplateId(Long templateId);


    ReportGenerateResponse generateReport(ReportGenerateRequest request);

    Map<String, Object> validateReportData(String groupid, String templateCode, String period);

    Map<String, Object> getReportTemplates(String groupid);

    void clearReportCache(String groupid, String templateCode, String period);
}
