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
import com.wimoor.finance.code.domain.FinCodeRuleAudit;
import com.wimoor.finance.code.service.IFinCodeRuleAuditService;
import com.wimoor.common.core.web.controller.BaseController;
import com.wimoor.common.core.web.domain.Result;
import com.wimoor.common.core.utils.poi.ExcelUtil;
import com.wimoor.common.core.web.page.TableDataInfo;

/**
 * 编码规则审计Controller
 * 
 * @author wimoor
 * @date 2025-11-03
 */
@RestController
@RequestMapping("/rule/audit")
public class FinCodeRuleAuditController extends BaseController
{
    @Autowired
    private IFinCodeRuleAuditService finCodeRuleAuditService;

    /**
     * 查询编码规则审计列表
     */
    @GetMapping("/list")
    public TableDataInfo list(FinCodeRuleAudit finCodeRuleAudit)
    {
        startPage();
        List<FinCodeRuleAudit> list = finCodeRuleAuditService.selectFinCodeRuleAuditList(finCodeRuleAudit);
        return getDataTable(list);
    }

    /**
     * 导出编码规则审计列表
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, FinCodeRuleAudit finCodeRuleAudit)
    {
        List<FinCodeRuleAudit> list = finCodeRuleAuditService.selectFinCodeRuleAuditList(finCodeRuleAudit);
        ExcelUtil<FinCodeRuleAudit> util = new ExcelUtil<FinCodeRuleAudit>(FinCodeRuleAudit.class);
        util.exportExcel(response, list, "编码规则审计数据");
    }

    /**
     * 获取编码规则审计详细信息
     */
    @GetMapping(value = "/{id}")
    public Result getInfo(@PathVariable("id") Long id)
    {
        return success(finCodeRuleAuditService.selectFinCodeRuleAuditById(id));
    }

    /**
     * 新增编码规则审计
     */
    @PostMapping
    public Result add(@RequestBody FinCodeRuleAudit finCodeRuleAudit)
    {
        return toResult(finCodeRuleAuditService.insertFinCodeRuleAudit(finCodeRuleAudit));
    }

    /**
     * 修改编码规则审计
     */
    @PutMapping
    public Result edit(@RequestBody FinCodeRuleAudit finCodeRuleAudit)
    {
        return toResult(finCodeRuleAuditService.updateFinCodeRuleAudit(finCodeRuleAudit));
    }

    /**
     * 删除编码规则审计
     */
	@DeleteMapping("/{ids}")
    public Result remove(@PathVariable Long[] ids)
    {
        return toResult(finCodeRuleAuditService.deleteFinCodeRuleAuditByIds(ids));
    }
}
