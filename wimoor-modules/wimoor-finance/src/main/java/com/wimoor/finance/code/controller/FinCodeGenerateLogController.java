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
import com.wimoor.finance.code.domain.FinCodeGenerateLog;
import com.wimoor.finance.code.service.IFinCodeGenerateLogService;
import com.wimoor.common.core.web.controller.BaseController;
import com.wimoor.common.core.web.domain.Result;
import com.wimoor.common.core.utils.poi.ExcelUtil;
import com.wimoor.common.core.web.page.TableDataInfo;

/**
 * 编码生成记录Controller
 * 
 * @author wimoor
 * @date 2025-11-03
 */
@RestController
@RequestMapping("/log")
public class FinCodeGenerateLogController extends BaseController
{
    @Autowired
    private IFinCodeGenerateLogService finCodeGenerateLogService;

    /**
     * 查询编码生成记录列表
     */
    @GetMapping("/list")
    public TableDataInfo list(FinCodeGenerateLog finCodeGenerateLog)
    {
        startPage();
        List<FinCodeGenerateLog> list = finCodeGenerateLogService.selectFinCodeGenerateLogList(finCodeGenerateLog);
        return getDataTable(list);
    }

    /**
     * 导出编码生成记录列表
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, FinCodeGenerateLog finCodeGenerateLog)
    {
        List<FinCodeGenerateLog> list = finCodeGenerateLogService.selectFinCodeGenerateLogList(finCodeGenerateLog);
        ExcelUtil<FinCodeGenerateLog> util = new ExcelUtil<FinCodeGenerateLog>(FinCodeGenerateLog.class);
        util.exportExcel(response, list, "编码生成记录数据");
    }

    /**
     * 获取编码生成记录详细信息
     */
    @GetMapping(value = "/{id}")
    public Result getInfo(@PathVariable("id") Long id)
    {
        return success(finCodeGenerateLogService.selectFinCodeGenerateLogById(id));
    }

    /**
     * 新增编码生成记录
     */
    @PostMapping
    public Result add(@RequestBody FinCodeGenerateLog finCodeGenerateLog)
    {
        return toResult(finCodeGenerateLogService.insertFinCodeGenerateLog(finCodeGenerateLog));
    }

    /**
     * 修改编码生成记录
     */
    @PutMapping
    public Result edit(@RequestBody FinCodeGenerateLog finCodeGenerateLog)
    {
        return toResult(finCodeGenerateLogService.updateFinCodeGenerateLog(finCodeGenerateLog));
    }

    /**
     * 删除编码生成记录
     */
	@DeleteMapping("/{ids}")
    public Result remove(@PathVariable Long[] ids)
    {
        return toResult(finCodeGenerateLogService.deleteFinCodeGenerateLogByIds(ids));
    }
}
