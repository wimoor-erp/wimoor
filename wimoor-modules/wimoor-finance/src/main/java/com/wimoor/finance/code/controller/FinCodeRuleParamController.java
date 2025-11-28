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
import com.wimoor.finance.code.domain.FinCodeRuleParam;
import com.wimoor.finance.code.service.IFinCodeRuleParamService;
import com.wimoor.common.core.web.controller.BaseController;
import com.wimoor.common.core.web.domain.Result;
import com.wimoor.common.core.utils.poi.ExcelUtil;
import com.wimoor.common.core.web.page.TableDataInfo;

/**
 * 编码规则参数Controller
 * 
 * @author wimoor
 * @date 2025-11-03
 */
@RestController
@RequestMapping("/rule/param")
public class FinCodeRuleParamController extends BaseController
{
    @Autowired
    private IFinCodeRuleParamService finCodeRuleParamService;

    /**
     * 查询编码规则参数列表
     */
    @GetMapping("/list")
    public TableDataInfo list(FinCodeRuleParam finCodeRuleParam)
    {
        startPage();
        List<FinCodeRuleParam> list = finCodeRuleParamService.selectFinCodeRuleParamList(finCodeRuleParam);
        return getDataTable(list);
    }

    /**
     * 导出编码规则参数列表
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, FinCodeRuleParam finCodeRuleParam)
    {
        List<FinCodeRuleParam> list = finCodeRuleParamService.selectFinCodeRuleParamList(finCodeRuleParam);
        ExcelUtil<FinCodeRuleParam> util = new ExcelUtil<FinCodeRuleParam>(FinCodeRuleParam.class);
        util.exportExcel(response, list, "编码规则参数数据");
    }

    /**
     * 获取编码规则参数详细信息
     */
    @GetMapping(value = "/{id}")
    public Result getInfo(@PathVariable("id") Long id)
    {
        return success(finCodeRuleParamService.selectFinCodeRuleParamById(id));
    }

    /**
     * 新增编码规则参数
     */
    @PostMapping
    public Result add(@RequestBody FinCodeRuleParam finCodeRuleParam)
    {
        return toResult(finCodeRuleParamService.insertFinCodeRuleParam(finCodeRuleParam));
    }

    /**
     * 修改编码规则参数
     */
    @PutMapping
    public Result edit(@RequestBody FinCodeRuleParam finCodeRuleParam)
    {
        return toResult(finCodeRuleParamService.updateFinCodeRuleParam(finCodeRuleParam));
    }

    /**
     * 删除编码规则参数
     */
	@DeleteMapping("/{ids}")
    public Result remove(@PathVariable Long[] ids)
    {
        return toResult(finCodeRuleParamService.deleteFinCodeRuleParamByIds(ids));
    }
}
