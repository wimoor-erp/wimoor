package com.wimoor.finance.report.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.wimoor.common.core.web.controller.BaseController;
import com.wimoor.common.core.web.domain.Result;
import com.wimoor.common.core.web.page.TableDataInfo;
import com.wimoor.finance.report.domain.FinReportMappingRules;
import com.wimoor.finance.report.service.IFinReportMappingRulesService;
import com.wimoor.finance.report.service.impl.FinReportAutoSyncService;

/**
 * 报表映射规则Controller
 *
 * @author wimoor
 * @date 2025-06-08
 */
@RestController
@RequestMapping("/report/mapping-rules")
public class FinReportMappingRulesController extends BaseController
{
    @Autowired
    private IFinReportMappingRulesService mappingRulesService;

    @Autowired
    private FinReportAutoSyncService autoSyncService;

    /**
     * 查询报表映射规则列表
     */
    @GetMapping("/list")
    public TableDataInfo list(FinReportMappingRules finReportMappingRules)
    {
        startPage();
        List<FinReportMappingRules> list = mappingRulesService.selectFinReportMappingRulesList(finReportMappingRules);
        return getDataTable(list);
    }

    /**
     * 根据模板类型查询规则列表
     */
    @GetMapping("/list/{templateType}")
    public Result listByTemplateType(@PathVariable String templateType,
                                     @RequestParam String groupid)
    {
        List<FinReportMappingRules> list = mappingRulesService.selectRulesByTemplateType(groupid, templateType);
        return success(list);
    }

    /**
     * 根据项目编码查询规则列表
     */
    @GetMapping("/list/item/{itemCode}")
    public Result listByItemCode(@PathVariable String itemCode,
                                 @RequestParam String groupid)
    {
        List<FinReportMappingRules> list = mappingRulesService.selectRulesByItemCode(groupid, itemCode);
        return success(list);
    }

    /**
     * 获取报表映射规则详细信息
     */
    @GetMapping(value = "/{ruleId}")
    public Result getInfo(@PathVariable("ruleId") Long ruleId)
    {
        return success(mappingRulesService.selectFinReportMappingRulesByRuleId(ruleId));
    }

    /**
     * 新增报表映射规则
     */
    @PostMapping
    public Result add(@RequestBody FinReportMappingRules finReportMappingRules)
    {
        return toResult(mappingRulesService.insertFinReportMappingRules(finReportMappingRules));
    }

    /**
     * 批量新增报表映射规则
     */
    @PostMapping("/batch")
    public Result batchAdd(@RequestBody List<FinReportMappingRules> list)
    {
        return toResult(mappingRulesService.batchInsertFinReportMappingRules(list));
    }

    /**
     * 修改报表映射规则
     */
    @PutMapping
    public Result edit(@RequestBody FinReportMappingRules finReportMappingRules)
    {
        return toResult(mappingRulesService.updateFinReportMappingRules(finReportMappingRules));
    }

    /**
     * 删除报表映射规则
     */
    @DeleteMapping("/{ruleIds}")
    public Result remove(@PathVariable Long[] ruleIds)
    {
        return toResult(mappingRulesService.deleteFinReportMappingRulesByRuleIds(ruleIds));
    }

    /**
     * 初始化默认规则
     */
    @PostMapping("/init-default/{groupid}")
    public Result initDefaultRules(@PathVariable String groupid)
    {
        int count = mappingRulesService.initDefaultRulesForTenant(groupid);
        return success("成功初始化" + count + "条默认规则");
    }

    /**
     * 同步报表公式
     */
    @PostMapping("/sync/{groupid}/{templateId}/{templateType}")
    public Result syncReportFormulas(@PathVariable String groupid,
                                     @PathVariable Long templateId,
                                     @PathVariable String templateType)
    {
        int count = autoSyncService.syncReportFormulas(groupid, templateId, templateType);
        return success("成功更新" + count + "个报表项目的公式");
    }
}
