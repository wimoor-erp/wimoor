package com.wimoor.finance.report.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.wimoor.finance.report.domain.FinReportTemplates;
import com.wimoor.finance.report.service.IFinReportTemplatesService;
import com.wimoor.common.core.web.controller.BaseController;
import com.wimoor.common.core.web.domain.Result;
import com.wimoor.common.core.utils.poi.ExcelUtil;
import com.wimoor.common.core.web.page.TableDataInfo;

/**
 * 财务报表模板Controller
 * 
 * @author wimoor
 * @date 2025-11-04
 */
@RestController
@RequestMapping("/report/templates")
public class FinReportTemplatesController extends BaseController
{
    @Autowired
    private IFinReportTemplatesService finReportTemplatesService;

    /**
     * 查询财务报表模板列表
     */
    @GetMapping("/list")
    public TableDataInfo list(FinReportTemplates finReportTemplates)
    {
        startPage();
        List<FinReportTemplates> list = finReportTemplatesService.selectFinReportTemplatesList(finReportTemplates);
        return getDataTable(list);
    }

    /**
     * 导出财务报表模板列表
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, FinReportTemplates finReportTemplates)
    {
        List<FinReportTemplates> list = finReportTemplatesService.selectFinReportTemplatesList(finReportTemplates);
        ExcelUtil<FinReportTemplates> util = new ExcelUtil<FinReportTemplates>(FinReportTemplates.class);
        util.exportExcel(response, list, "财务报表模板数据");
    }

    /**
     * 获取财务报表模板详细信息
     */
    @GetMapping(value = "/{templateId}")
    public Result getInfo(@PathVariable("templateId") Long templateId)
    {
        return success(finReportTemplatesService.selectFinReportTemplatesByTemplateId(templateId));
    }

    /**
     * 新增财务报表模板
     */
    @PostMapping
    public Result add(@RequestBody FinReportTemplates finReportTemplates)
    {
        return toResult(finReportTemplatesService.insertFinReportTemplates(finReportTemplates));
    }

    /**
     * 修改财务报表模板
     */
    @PutMapping
    public Result edit(@RequestBody FinReportTemplates finReportTemplates)
    {
        return toResult(finReportTemplatesService.updateFinReportTemplates(finReportTemplates));
    }

    /**
     * 删除财务报表模板
     */
	@DeleteMapping("/{templateIds}")
    public Result remove(@PathVariable Long[] templateIds)
    {
        return toResult(finReportTemplatesService.deleteFinReportTemplatesByTemplateIds(templateIds));
    }
}
