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
import com.wimoor.finance.code.domain.FinCodeRuleRelation;
import com.wimoor.finance.code.service.IFinCodeRuleRelationService;
import com.wimoor.common.core.web.controller.BaseController;
import com.wimoor.common.core.web.domain.Result;
import com.wimoor.common.core.utils.poi.ExcelUtil;
import com.wimoor.common.core.web.page.TableDataInfo;

/**
 * 编码规则关联Controller
 * 
 * @author wimoor
 * @date 2025-11-03
 */
@RestController
@RequestMapping("/rule/relation")
public class FinCodeRuleRelationController extends BaseController
{
    @Autowired
    private IFinCodeRuleRelationService finCodeRuleRelationService;

    /**
     * 查询编码规则关联列表
     */
    @GetMapping("/list")
    public TableDataInfo list(FinCodeRuleRelation finCodeRuleRelation)
    {
        startPage();
        List<FinCodeRuleRelation> list = finCodeRuleRelationService.selectFinCodeRuleRelationList(finCodeRuleRelation);
        return getDataTable(list);
    }

    /**
     * 导出编码规则关联列表
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, FinCodeRuleRelation finCodeRuleRelation)
    {
        List<FinCodeRuleRelation> list = finCodeRuleRelationService.selectFinCodeRuleRelationList(finCodeRuleRelation);
        ExcelUtil<FinCodeRuleRelation> util = new ExcelUtil<FinCodeRuleRelation>(FinCodeRuleRelation.class);
        util.exportExcel(response, list, "编码规则关联数据");
    }

    /**
     * 获取编码规则关联详细信息
     */
    @GetMapping(value = "/{id}")
    public Result getInfo(@PathVariable("id") Long id)
    {
        return success(finCodeRuleRelationService.selectFinCodeRuleRelationById(id));
    }

    /**
     * 新增编码规则关联
     */
    @PostMapping
    public Result add(@RequestBody FinCodeRuleRelation finCodeRuleRelation)
    {
        return toResult(finCodeRuleRelationService.insertFinCodeRuleRelation(finCodeRuleRelation));
    }

    /**
     * 修改编码规则关联
     */
    @PutMapping
    public Result edit(@RequestBody FinCodeRuleRelation finCodeRuleRelation)
    {
        return toResult(finCodeRuleRelationService.updateFinCodeRuleRelation(finCodeRuleRelation));
    }

    /**
     * 删除编码规则关联
     */
	@DeleteMapping("/{ids}")
    public Result remove(@PathVariable Long[] ids)
    {
        return toResult(finCodeRuleRelationService.deleteFinCodeRuleRelationByIds(ids));
    }
}
