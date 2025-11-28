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
import com.wimoor.finance.setting.domain.FinAuxiliaryItems;
import com.wimoor.finance.setting.service.IFinAuxiliaryItemsService;
import com.wimoor.common.core.web.controller.BaseController;
import com.wimoor.common.core.web.domain.Result;
import com.wimoor.common.core.utils.poi.ExcelUtil;
import com.wimoor.common.core.web.page.TableDataInfo;

/**
 * 辅助核算具体项目Controller
 * 
 * @author wimoor
 * @date 2025-11-04
 */
@RestController
@RequestMapping("/auxiliary/items")
public class FinAuxiliaryItemsController extends BaseController
{
    @Autowired
    private IFinAuxiliaryItemsService finAuxiliaryItemsService;

    /**
     * 查询辅助核算具体项目列表
     */
    @GetMapping("/list")
    public TableDataInfo list(FinAuxiliaryItems finAuxiliaryItems)
    {
        startPage();
        List<FinAuxiliaryItems> list = finAuxiliaryItemsService.selectFinAuxiliaryItemsList(finAuxiliaryItems);
        return getDataTable(list);
    }

    /**
     * 导出辅助核算具体项目列表
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, FinAuxiliaryItems finAuxiliaryItems)
    {
        List<FinAuxiliaryItems> list = finAuxiliaryItemsService.selectFinAuxiliaryItemsList(finAuxiliaryItems);
        ExcelUtil<FinAuxiliaryItems> util = new ExcelUtil<FinAuxiliaryItems>(FinAuxiliaryItems.class);
        util.exportExcel(response, list, "辅助核算具体项目数据");
    }

    /**
     * 获取辅助核算具体项目详细信息
     */
    @GetMapping(value = "/{itemId}")
    public Result getInfo(@PathVariable("itemId") Long itemId)
    {
        return success(finAuxiliaryItemsService.selectFinAuxiliaryItemsByItemId(itemId));
    }

    /**
     * 新增辅助核算具体项目
     */
    @PostMapping
    public Result add(@RequestBody FinAuxiliaryItems finAuxiliaryItems)
    {
        return toResult(finAuxiliaryItemsService.insertFinAuxiliaryItems(finAuxiliaryItems));
    }

    /**
     * 修改辅助核算具体项目
     */
    @PutMapping
    public Result edit(@RequestBody FinAuxiliaryItems finAuxiliaryItems)
    {
        return toResult(finAuxiliaryItemsService.updateFinAuxiliaryItems(finAuxiliaryItems));
    }

    /**
     * 删除辅助核算具体项目
     */
	@DeleteMapping("/{itemIds}")
    public Result remove(@PathVariable Long[] itemIds)
    {
        return toResult(finAuxiliaryItemsService.deleteFinAuxiliaryItemsByItemIds(itemIds));
    }
}
