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
import com.wimoor.finance.code.domain.FinCodeRule;
import com.wimoor.finance.code.service.IFinCodeRuleService;
import com.wimoor.common.core.web.controller.BaseController;
import com.wimoor.common.core.web.domain.Result;
import com.wimoor.common.core.utils.poi.ExcelUtil;
import com.wimoor.common.core.web.page.TableDataInfo;

/**
 * 编码规则Controller
 * 
 * @author wimoor
 * @date 2025-11-03
 */
@RestController
@RequestMapping("/rule")
public class FinCodeRuleController extends BaseController
{
    @Autowired
    private IFinCodeRuleService finCodeRuleService;

    /**
     * 查询编码规则列表
     */
    @GetMapping("/list")
    public TableDataInfo list(FinCodeRule finCodeRule)
    {
        startPage();
        List<FinCodeRule> list = finCodeRuleService.selectFinCodeRuleList(finCodeRule);
        return getDataTable(list);
    }

    /**
     * 导出编码规则列表
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, FinCodeRule finCodeRule)
    {
        List<FinCodeRule> list = finCodeRuleService.selectFinCodeRuleList(finCodeRule);
        ExcelUtil<FinCodeRule> util = new ExcelUtil<FinCodeRule>(FinCodeRule.class);
        util.exportExcel(response, list, "编码规则数据");
    }

    /**
     * 获取编码规则详细信息
     */
    @GetMapping(value = "/{id}")
    public Result getInfo(@PathVariable("id") Long id)
    {
        return success(finCodeRuleService.selectFinCodeRuleById(id));
    }

    /**
     * 新增编码规则
     */
    @PostMapping
    public Result add(@RequestBody FinCodeRule finCodeRule)
    {
        return toResult(finCodeRuleService.insertFinCodeRule(finCodeRule));
    }

    /**
     * 修改编码规则
     */
    @PutMapping
    public Result edit(@RequestBody FinCodeRule finCodeRule)
    {
        return toResult(finCodeRuleService.updateFinCodeRule(finCodeRule));
    }

    /**
     * 删除编码规则
     */
	@DeleteMapping("/{ids}")
    public Result remove(@PathVariable Long[] ids)
    {
        return toResult(finCodeRuleService.deleteFinCodeRuleByIds(ids));
    }


}
