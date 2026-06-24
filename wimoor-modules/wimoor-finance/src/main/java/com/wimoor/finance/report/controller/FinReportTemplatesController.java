package com.wimoor.finance.report.controller;

import com.wimoor.common.core.utils.poi.ExcelUtil;
import com.wimoor.common.core.web.controller.BaseController;
import com.wimoor.common.core.web.domain.Result;
import com.wimoor.common.core.web.page.TableDataInfo;
import com.wimoor.finance.report.domain.FinReportTemplates;
import com.wimoor.finance.report.service.IFinReportItemsService;
import com.wimoor.finance.report.service.IFinReportTemplatesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

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
    
    @Autowired
    private IFinReportItemsService finReportItemsService;

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
    
    /**
     * 初始化模板的报表项目
     * 会删除现有项目并根据模板类型重新创建默认项目
     */
    @PostMapping("/init-items/{templateId}")
    public Result initTemplateItems(@PathVariable Long templateId, 
                                     @RequestParam String groupid,
                                     @RequestParam String templateType)
    {
        try
        {
            int count = finReportItemsService.initTemplateItems(templateId, groupid, templateType);
            return success("初始化成功，创建了 " + count + " 个报表项目");
        }
        catch (Exception e)
        {
            return error("初始化失败: " + e.getMessage());
        }
    }
}
