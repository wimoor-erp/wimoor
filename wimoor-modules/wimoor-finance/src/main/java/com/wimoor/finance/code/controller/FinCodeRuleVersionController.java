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
import com.wimoor.finance.code.domain.FinCodeRuleVersion;
import com.wimoor.finance.code.service.IFinCodeRuleVersionService;
import com.wimoor.common.core.web.controller.BaseController;
import com.wimoor.common.core.web.domain.Result;
import com.wimoor.common.core.utils.poi.ExcelUtil;
import com.wimoor.common.core.web.page.TableDataInfo;

/**
 * 编码规则版本Controller
 * 
 * @author wimoor
 * @date 2025-11-03
 */
@RestController
@RequestMapping("/rule/version")
public class FinCodeRuleVersionController extends BaseController
{
    @Autowired
    private IFinCodeRuleVersionService finCodeRuleVersionService;

    /**
     * 查询编码规则版本列表
     */
    @GetMapping("/list")
    public TableDataInfo list(FinCodeRuleVersion finCodeRuleVersion)
    {
        startPage();
        List<FinCodeRuleVersion> list = finCodeRuleVersionService.selectFinCodeRuleVersionList(finCodeRuleVersion);
        return getDataTable(list);
    }

    /**
     * 导出编码规则版本列表
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, FinCodeRuleVersion finCodeRuleVersion)
    {
        List<FinCodeRuleVersion> list = finCodeRuleVersionService.selectFinCodeRuleVersionList(finCodeRuleVersion);
        ExcelUtil<FinCodeRuleVersion> util = new ExcelUtil<FinCodeRuleVersion>(FinCodeRuleVersion.class);
        util.exportExcel(response, list, "编码规则版本数据");
    }

    /**
     * 获取编码规则版本详细信息
     */
    @GetMapping(value = "/{id}")
    public Result getInfo(@PathVariable("id") Long id)
    {
        return success(finCodeRuleVersionService.selectFinCodeRuleVersionById(id));
    }

    /**
     * 新增编码规则版本
     */
    @PostMapping
    public Result add(@RequestBody FinCodeRuleVersion finCodeRuleVersion)
    {
        return toResult(finCodeRuleVersionService.insertFinCodeRuleVersion(finCodeRuleVersion));
    }

    /**
     * 修改编码规则版本
     */
    @PutMapping
    public Result edit(@RequestBody FinCodeRuleVersion finCodeRuleVersion)
    {
        return toResult(finCodeRuleVersionService.updateFinCodeRuleVersion(finCodeRuleVersion));
    }

    /**
     * 删除编码规则版本
     */
	@DeleteMapping("/{ids}")
    public Result remove(@PathVariable Long[] ids)
    {
        return toResult(finCodeRuleVersionService.deleteFinCodeRuleVersionByIds(ids));
    }
}
