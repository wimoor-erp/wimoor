package com.wimoor.finance.voucher.controller;

import java.util.List;
import java.io.IOException;
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
import com.wimoor.finance.voucher.domain.FinVoucherModifyLog;
import com.wimoor.finance.voucher.service.IFinVoucherModifyLogService;
import com.wimoor.common.core.web.controller.BaseController;
import com.wimoor.common.core.web.domain.Result;
import com.wimoor.common.core.utils.poi.ExcelUtil;
import com.wimoor.common.core.web.page.TableDataInfo;

/**
 * 凭证修改日志Controller
 * 
 * @author wimoor
 * @date 2025-11-22
 */
@RestController
@RequestMapping("/vouchers/log")
public class FinVoucherModifyLogController extends BaseController
{
    @Autowired
    private IFinVoucherModifyLogService finVoucherModifyLogService;

    /**
     * 查询凭证修改日志列表
     */
    @GetMapping("/list")
    public TableDataInfo list(FinVoucherModifyLog finVoucherModifyLog)
    {
        startPage();
        List<FinVoucherModifyLog> list = finVoucherModifyLogService.selectFinVoucherModifyLogList(finVoucherModifyLog);
        return getDataTable(list);
    }

    /**
     * 导出凭证修改日志列表
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, FinVoucherModifyLog finVoucherModifyLog)
    {
        List<FinVoucherModifyLog> list = finVoucherModifyLogService.selectFinVoucherModifyLogList(finVoucherModifyLog);
        ExcelUtil<FinVoucherModifyLog> util = new ExcelUtil<FinVoucherModifyLog>(FinVoucherModifyLog.class);
        util.exportExcel(response, list, "凭证修改日志数据");
    }

    /**
     * 获取凭证修改日志详细信息
     */
    @GetMapping(value = "/{logId}")
    public Result getInfo(@PathVariable("logId") String logId)
    {
        return success(finVoucherModifyLogService.selectFinVoucherModifyLogByLogId(logId));
    }

    /**
     * 新增凭证修改日志
     */
    @PostMapping
    public Result add(@RequestBody FinVoucherModifyLog finVoucherModifyLog)
    {
        return toResult(finVoucherModifyLogService.insertFinVoucherModifyLog(finVoucherModifyLog));
    }

    /**
     * 修改凭证修改日志
     */
    @PutMapping
    public Result edit(@RequestBody FinVoucherModifyLog finVoucherModifyLog)
    {
        return toResult(finVoucherModifyLogService.updateFinVoucherModifyLog(finVoucherModifyLog));
    }

    /**
     * 删除凭证修改日志
     */
	@DeleteMapping("/{logIds}")
    public Result remove(@PathVariable String[] logIds)
    {
        return toResult(finVoucherModifyLogService.deleteFinVoucherModifyLogByLogIds(logIds));
    }
}
