package com.wimoor.finance.code.controller;

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
import com.wimoor.finance.code.domain.FinCodeCategory;
import com.wimoor.finance.code.service.IFinCodeCategoryService;
import com.wimoor.common.core.web.controller.BaseController;
import com.wimoor.common.core.web.domain.Result;
import com.wimoor.common.core.utils.poi.ExcelUtil;
import com.wimoor.common.core.web.page.TableDataInfo;

/**
 * 编码规则分类Controller
 * 
 * @author wimoor
 * @date 2025-11-03
 */
@RestController
@RequestMapping("/category")
public class FinCodeCategoryController extends BaseController
{
    @Autowired
    private IFinCodeCategoryService finCodeCategoryService;

    /**
     * 查询编码规则分类列表
     */
    @GetMapping("/list")
    public TableDataInfo list(FinCodeCategory finCodeCategory)
    {
        startPage();
        List<FinCodeCategory> list = finCodeCategoryService.selectFinCodeCategoryList(finCodeCategory);
        return getDataTable(list);
    }

    /**
     * 导出编码规则分类列表
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, FinCodeCategory finCodeCategory)
    {
        List<FinCodeCategory> list = finCodeCategoryService.selectFinCodeCategoryList(finCodeCategory);
        ExcelUtil<FinCodeCategory> util = new ExcelUtil<FinCodeCategory>(FinCodeCategory.class);
        util.exportExcel(response, list, "编码规则分类数据");
    }

    /**
     * 获取编码规则分类详细信息
     */
    @GetMapping(value = "/{id}")
    public Result getInfo(@PathVariable("id") Long id)
    {
        return success(finCodeCategoryService.selectFinCodeCategoryById(id));
    }

    /**
     * 新增编码规则分类
     */
    @PostMapping
    public Result add(@RequestBody FinCodeCategory finCodeCategory)
    {
        return toResult(finCodeCategoryService.insertFinCodeCategory(finCodeCategory));
    }

    /**
     * 修改编码规则分类
     */
    @PutMapping
    public Result edit(@RequestBody FinCodeCategory finCodeCategory)
    {
        return toResult(finCodeCategoryService.updateFinCodeCategory(finCodeCategory));
    }

    /**
     * 删除编码规则分类
     */
	@DeleteMapping("/{ids}")
    public Result remove(@PathVariable Long[] ids)
    {
        return toResult(finCodeCategoryService.deleteFinCodeCategoryByIds(ids));
    }
}
