package com.wimoor.finance.setting.controller;

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
import com.wimoor.finance.setting.domain.FinAuxiliaryTypes;
import com.wimoor.finance.setting.service.IFinAuxiliaryTypesService;
import com.wimoor.common.core.web.controller.BaseController;
import com.wimoor.common.core.web.domain.Result;
import com.wimoor.common.core.utils.poi.ExcelUtil;
import com.wimoor.common.core.web.page.TableDataInfo;

/**
 * 辅助核算类别Controller
 * 
 * @author wimoor
 * @date 2025-11-04
 */
@RestController
@RequestMapping("/auxiliary/types")
public class FinAuxiliaryTypesController extends BaseController
{
    @Autowired
    private IFinAuxiliaryTypesService finAuxiliaryTypesService;

    /**
     * 查询辅助核算类别列表
     */
    @GetMapping("/list")
    public TableDataInfo list(FinAuxiliaryTypes finAuxiliaryTypes)
    {
        startPage();
        List<FinAuxiliaryTypes> list = finAuxiliaryTypesService.selectFinAuxiliaryTypesList(finAuxiliaryTypes);
        return getDataTable(list);
    }

    /**
     * 导出辅助核算类别列表
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, FinAuxiliaryTypes finAuxiliaryTypes)
    {
        List<FinAuxiliaryTypes> list = finAuxiliaryTypesService.selectFinAuxiliaryTypesList(finAuxiliaryTypes);
        ExcelUtil<FinAuxiliaryTypes> util = new ExcelUtil<FinAuxiliaryTypes>(FinAuxiliaryTypes.class);
        util.exportExcel(response, list, "辅助核算类别数据");
    }

    /**
     * 获取辅助核算类别详细信息
     */
    @GetMapping(value = "/{typeId}")
    public Result getInfo(@PathVariable("typeId") Long typeId)
    {
        return success(finAuxiliaryTypesService.selectFinAuxiliaryTypesByTypeId(typeId));
    }

    /**
     * 新增辅助核算类别
     */
    @PostMapping
    public Result add(@RequestBody FinAuxiliaryTypes finAuxiliaryTypes)
    {
        return toResult(finAuxiliaryTypesService.insertFinAuxiliaryTypes(finAuxiliaryTypes));
    }

    /**
     * 修改辅助核算类别
     */
    @PutMapping
    public Result edit(@RequestBody FinAuxiliaryTypes finAuxiliaryTypes)
    {
        return toResult(finAuxiliaryTypesService.updateFinAuxiliaryTypes(finAuxiliaryTypes));
    }

    /**
     * 删除辅助核算类别
     */
	@DeleteMapping("/{typeIds}")
    public Result remove(@PathVariable Long[] typeIds)
    {
        return toResult(finAuxiliaryTypesService.deleteFinAuxiliaryTypesByTypeIds(typeIds));
    }
}
