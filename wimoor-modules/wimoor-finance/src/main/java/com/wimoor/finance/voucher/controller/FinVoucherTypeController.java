package com.wimoor.finance.voucher.controller;

import com.wimoor.common.core.utils.poi.ExcelUtil;
import com.wimoor.common.core.web.controller.BaseController;
import com.wimoor.common.core.web.domain.Result;
import com.wimoor.common.core.web.page.TableDataInfo;
import com.wimoor.finance.voucher.service.IFinVoucherTypeService;
import com.wimoor.finance.voucher.domain.FinVoucherType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 凭证字Controller
 * 
 * @author wimoor
 * @date 2026-06-08
 */
@RestController
@RequestMapping("/type")
public class FinVoucherTypeController extends BaseController
{
    @Autowired
    private IFinVoucherTypeService finVoucherTypeService;

    /**
     * 查询凭证字列表
     */
    @GetMapping("/list")
    public TableDataInfo list(FinVoucherType finVoucherType)
    {
        startPage();
        List<FinVoucherType> list = finVoucherTypeService.selectFinVoucherTypeList(finVoucherType);
        return getDataTable(list);
    }

    /**
     * 导出凭证字列表
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, FinVoucherType finVoucherType)
    {
        List<FinVoucherType> list = finVoucherTypeService.selectFinVoucherTypeList(finVoucherType);
        ExcelUtil<FinVoucherType> util = new ExcelUtil<FinVoucherType>(FinVoucherType.class);
        util.exportExcel(response, list, "凭证字数据");
    }

    /**
     * 获取凭证字详细信息
     */
    @GetMapping(value = "/{id}")
    public Result getInfo(@PathVariable("id") String id)
    {
        return success(finVoucherTypeService.selectFinVoucherTypeById(id));
    }

    /**
     * 新增凭证字
     */
    @PostMapping
    public Result add(@RequestBody FinVoucherType finVoucherType)
    {
        return toResult(finVoucherTypeService.insertFinVoucherType(finVoucherType));
    }

    /**
     * 修改凭证字
     */
    @PutMapping
    public Result edit(@RequestBody FinVoucherType finVoucherType)
    {
        return toResult(finVoucherTypeService.updateFinVoucherType(finVoucherType));
    }

    /**
     * 删除凭证字
     */
	@DeleteMapping("/{ids}")
    public Result remove(@PathVariable String[] ids)
    {
        return toResult(finVoucherTypeService.deleteFinVoucherTypeByIds(ids));
    }
}
