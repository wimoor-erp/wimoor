package com.wimoor.finance.voucher.controller;

import com.wimoor.common.core.utils.poi.ExcelUtil;
import com.wimoor.common.core.web.controller.BaseController;
import com.wimoor.common.core.web.domain.Result;
import com.wimoor.common.core.web.page.TableDataInfo;
import com.wimoor.finance.voucher.domain.FinVoucherEntriesAuxiliary;
import com.wimoor.finance.voucher.service.IFinVoucherEntriesAuxiliaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 凭证分录辅助核算Controller
 * 
 * @author wimoor
 * @date 2026-05-26
 */
@RestController
@RequestMapping("/voucher")
public class FinVoucherEntriesAuxiliaryController extends BaseController
{
    @Autowired
    private IFinVoucherEntriesAuxiliaryService finVoucherEntriesAuxiliaryService;

    /**
     * 查询凭证分录辅助核算列表
     */
    @GetMapping("/list")
    public TableDataInfo list(FinVoucherEntriesAuxiliary finVoucherEntriesAuxiliary)
    {
        startPage();
        List<FinVoucherEntriesAuxiliary> list = finVoucherEntriesAuxiliaryService.selectFinVoucherEntriesAuxiliaryList(finVoucherEntriesAuxiliary);
        return getDataTable(list);
    }

    /**
     * 导出凭证分录辅助核算列表
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, FinVoucherEntriesAuxiliary finVoucherEntriesAuxiliary)
    {
        List<FinVoucherEntriesAuxiliary> list = finVoucherEntriesAuxiliaryService.selectFinVoucherEntriesAuxiliaryList(finVoucherEntriesAuxiliary);
        ExcelUtil<FinVoucherEntriesAuxiliary> util = new ExcelUtil<FinVoucherEntriesAuxiliary>(FinVoucherEntriesAuxiliary.class);
        util.exportExcel(response, list, "凭证分录辅助核算数据");
    }

    /**
     * 获取凭证分录辅助核算详细信息
     */
    @GetMapping(value = "/{id}")
    public Result getInfo(@PathVariable("id") String id)
    {
        return success(finVoucherEntriesAuxiliaryService.selectFinVoucherEntriesAuxiliaryById(id));
    }

    /**
     * 新增凭证分录辅助核算
     */
    @PostMapping
    public Result add(@RequestBody FinVoucherEntriesAuxiliary finVoucherEntriesAuxiliary)
    {
        return toResult(finVoucherEntriesAuxiliaryService.insertFinVoucherEntriesAuxiliary(finVoucherEntriesAuxiliary));
    }

    /**
     * 修改凭证分录辅助核算
     */
    @PutMapping
    public Result edit(@RequestBody FinVoucherEntriesAuxiliary finVoucherEntriesAuxiliary)
    {
        return toResult(finVoucherEntriesAuxiliaryService.updateFinVoucherEntriesAuxiliary(finVoucherEntriesAuxiliary));
    }

    /**
     * 删除凭证分录辅助核算
     */
	@DeleteMapping("/{ids}")
    public Result remove(@PathVariable String[] ids)
    {
        return toResult(finVoucherEntriesAuxiliaryService.deleteFinVoucherEntriesAuxiliaryByIds(ids));
    }
}
