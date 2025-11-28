package com.wimoor.finance.voucher.controller;

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
import com.wimoor.finance.voucher.domain.FinVoucherEntries;
import com.wimoor.finance.voucher.service.IFinVoucherEntriesService;
import com.wimoor.common.core.web.controller.BaseController;
import com.wimoor.common.core.web.domain.Result;
import com.wimoor.common.core.utils.poi.ExcelUtil;
import com.wimoor.common.core.web.page.TableDataInfo;

/**
 * 查凭证Controller
 * 
 * @author wimoor
 * @date 2025-11-04
 */
@RestController
@RequestMapping("/entries")
public class FinVoucherEntriesController extends BaseController
{
    @Autowired
    private IFinVoucherEntriesService finVoucherEntriesService;

    /**
     * 查询凭证分录明细列表
     */
    @GetMapping("/list")
    public TableDataInfo list(FinVoucherEntries finVoucherEntries)
    {
        startPage();
        List<FinVoucherEntries> list = finVoucherEntriesService.selectFinVoucherEntriesList(finVoucherEntries);
        TableDataInfo data = getDataTable(list);
        return data;
    }

    /**
     * 导出凭证分录明细列表
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, FinVoucherEntries finVoucherEntries)
    {
        List<FinVoucherEntries> list = finVoucherEntriesService.selectFinVoucherEntriesList(finVoucherEntries);
        ExcelUtil<FinVoucherEntries> util = new ExcelUtil<FinVoucherEntries>(FinVoucherEntries.class);
        util.exportExcel(response, list, "凭证分录明细数据");
    }

    /**
     * 获取凭证分录明细详细信息
     */
    @GetMapping(value = "/{entryId}")
    public Result getInfo(@PathVariable("entryId") Long entryId)
    {
        return success(finVoucherEntriesService.selectFinVoucherEntriesByEntryId(entryId));
    }

    /**
     * 获取凭证分录明细详细信息
     */
    @GetMapping(value = "/getEntries")
    public Result getInfos( Long voucherId)
    {
        return success(finVoucherEntriesService.selectFinVoucherEntriesListByVoucherId(voucherId));
    }


    /**
     * 新增凭证分录明细
     */
    @PostMapping
    public Result add(@RequestBody FinVoucherEntries finVoucherEntries)
    {
        return toResult(finVoucherEntriesService.insertFinVoucherEntries(finVoucherEntries));
    }

    /**
     * 修改凭证分录明细
     */
    @PutMapping
    public Result edit(@RequestBody FinVoucherEntries finVoucherEntries)
    {
        return toResult(finVoucherEntriesService.updateFinVoucherEntries(finVoucherEntries));
    }

    /**
     * 删除凭证分录明细
     */
	@DeleteMapping("/{entryIds}")
    public Result remove(@PathVariable Long[] entryIds)
    {
        return toResult(finVoucherEntriesService.deleteFinVoucherEntriesByEntryIds(entryIds));
    }



}
