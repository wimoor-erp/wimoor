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
import com.wimoor.finance.code.domain.FinCodeRuleCategory;
import com.wimoor.finance.code.service.IFinCodeRuleCategoryService;
import com.wimoor.common.core.web.controller.BaseController;
import com.wimoor.common.core.web.domain.Result;
import com.wimoor.common.core.utils.poi.ExcelUtil;
import com.wimoor.common.core.web.page.TableDataInfo;

/**
 * 编码规则与分类关联Controller
 * 
 * @author wimoor
 * @date 2025-11-03
 */
@RestController
@RequestMapping("/rule/category")
public class FinCodeRuleCategoryController extends BaseController
{
    @Autowired
    private IFinCodeRuleCategoryService finCodeRuleCategoryService;

    /**
     * 查询编码规则与分类关联列表
     */
    @GetMapping("/list")
    public TableDataInfo list(FinCodeRuleCategory finCodeRuleCategory)
    {
        startPage();
        List<FinCodeRuleCategory> list = finCodeRuleCategoryService.selectFinCodeRuleCategoryList(finCodeRuleCategory);
        return getDataTable(list);
    }

    /**
     * 导出编码规则与分类关联列表
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, FinCodeRuleCategory finCodeRuleCategory)
    {
        List<FinCodeRuleCategory> list = finCodeRuleCategoryService.selectFinCodeRuleCategoryList(finCodeRuleCategory);
        ExcelUtil<FinCodeRuleCategory> util = new ExcelUtil<FinCodeRuleCategory>(FinCodeRuleCategory.class);
        util.exportExcel(response, list, "编码规则与分类关联数据");
    }

    /**
     * 获取编码规则与分类关联详细信息
     */
    @GetMapping(value = "/{id}")
    public Result getInfo(@PathVariable("id") Long id)
    {
        return success(finCodeRuleCategoryService.selectFinCodeRuleCategoryById(id));
    }

    /**
     * 新增编码规则与分类关联
     */
    @PostMapping
    public Result add(@RequestBody FinCodeRuleCategory finCodeRuleCategory)
    {
        return toResult(finCodeRuleCategoryService.insertFinCodeRuleCategory(finCodeRuleCategory));
    }

    /**
     * 修改编码规则与分类关联
     */
    @PutMapping
    public Result edit(@RequestBody FinCodeRuleCategory finCodeRuleCategory)
    {
        return toResult(finCodeRuleCategoryService.updateFinCodeRuleCategory(finCodeRuleCategory));
    }

    /**
     * 删除编码规则与分类关联
     */
	@DeleteMapping("/{ids}")
    public Result remove(@PathVariable Long[] ids)
    {
        return toResult(finCodeRuleCategoryService.deleteFinCodeRuleCategoryByIds(ids));
    }
}
