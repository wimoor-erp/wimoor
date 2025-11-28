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
import com.wimoor.finance.report.domain.FinReportItems;
import com.wimoor.finance.report.service.IFinReportItemsService;
import com.wimoor.common.core.web.controller.BaseController;
import com.wimoor.common.core.web.domain.Result;
import com.wimoor.common.core.utils.poi.ExcelUtil;
import com.wimoor.common.core.web.page.TableDataInfo;

/**
 * 报表项目Controller
 * 
 * @author wimoor
 * @date 2025-11-04
 */
@RestController
@RequestMapping("/report/items")
public class FinReportItemsController extends BaseController
{
    @Autowired
    private IFinReportItemsService finReportItemsService;

    /**
     * 查询报表项目列表
     */
    @GetMapping("/list")
    public Result list(FinReportItems finReportItems)
    {
        List<FinReportItems> list = finReportItemsService.selectFinReportItemsList(finReportItems);
        return success(list);
    }

    /**
     * 导出报表项目列表
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, FinReportItems finReportItems)
    {
        List<FinReportItems> list = finReportItemsService.selectFinReportItemsList(finReportItems);
        ExcelUtil<FinReportItems> util = new ExcelUtil<FinReportItems>(FinReportItems.class);
        util.exportExcel(response, list, "报表项目数据");
    }

    /**
     * 获取报表项目详细信息
     */
    @GetMapping(value = "/{itemId}")
    public Result getInfo(@PathVariable("itemId") Long itemId)
    {
        return success(finReportItemsService.selectFinReportItemsByItemId(itemId));
    }

    /**
     * 新增报表项目
     */
    @PostMapping
    public Result add(@RequestBody FinReportItems finReportItems)
    {
        return toResult(finReportItemsService.insertFinReportItems(finReportItems));
    }

    /**
     * 修改报表项目
     */
    @PutMapping
    public Result edit(@RequestBody FinReportItems finReportItems)
    {
        return toResult(finReportItemsService.updateFinReportItems(finReportItems));
    }

    /**
     * 删除报表项目
     */
	@DeleteMapping("/{itemIds}")
    public Result remove(@PathVariable Long[] itemIds)
    {
        return toResult(finReportItemsService.deleteFinReportItemsByItemIds(itemIds));
    }
}
