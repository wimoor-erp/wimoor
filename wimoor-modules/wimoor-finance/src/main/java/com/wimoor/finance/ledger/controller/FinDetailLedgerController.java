package com.wimoor.finance.ledger.controller;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;

import com.wimoor.finance.ledger.domain.dto.FinLedgerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.wimoor.finance.ledger.domain.FinDetailLedger;
import com.wimoor.finance.ledger.service.IFinDetailLedgerService;
import com.wimoor.common.core.web.controller.BaseController;
import com.wimoor.common.core.web.domain.Result;
import com.wimoor.common.core.utils.poi.ExcelUtil;
import com.wimoor.common.core.web.page.TableDataInfo;

/**
 * 明细账表Controller
 * 
 * @author wimoor
 * @date 2025-11-04
 */
@RestController
@RequestMapping("/detail_ledger")
public class FinDetailLedgerController extends BaseController
{
    @Autowired
    private IFinDetailLedgerService finDetailLedgerService;

    /**
     * 查询明细账表列表
     */
    @GetMapping("/list")
    public TableDataInfo list(FinDetailLedger finDetailLedger)
    {
        startPage();
        List<FinDetailLedger> list = finDetailLedgerService.selectFinDetailLedgerList(finDetailLedger);
        return getDataTable(list);
    }

    /**
     * 查询明细账表列表
     */
    @PostMapping("/listInfo")
    public TableDataInfo listInfo(@RequestBody FinLedgerDTO finDetailLedger)
    {
        startPage();
        List<Map<String,Object>> list = finDetailLedgerService.selectList(finDetailLedger);
        return getDataTable(list);
    }


    /**
     * 导出明细账表列表
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, FinDetailLedger finDetailLedger)
    {
        List<FinDetailLedger> list = finDetailLedgerService.selectFinDetailLedgerList(finDetailLedger);
        ExcelUtil<FinDetailLedger> util = new ExcelUtil<FinDetailLedger>(FinDetailLedger.class);
        util.exportExcel(response, list, "明细账表数据");
    }

    /**
     * 获取明细账表详细信息
     */
    @GetMapping(value = "/{detailId}")
    public Result getInfo(@PathVariable("detailId") Long detailId)
    {
        return success(finDetailLedgerService.selectFinDetailLedgerByDetailId(detailId));
    }

    /**
     * 新增明细账表
     */
    @PostMapping
    public Result add(@RequestBody FinDetailLedger finDetailLedger)
    {
        return toResult(finDetailLedgerService.insertFinDetailLedger(finDetailLedger));
    }

    /**
     * 修改明细账表
     */
    @PutMapping
    public Result edit(@RequestBody FinDetailLedger finDetailLedger)
    {
        return toResult(finDetailLedgerService.updateFinDetailLedger(finDetailLedger));
    }

    /**
     * 删除明细账表
     */
	@DeleteMapping("/{detailIds}")
    public Result remove(@PathVariable Long[] detailIds)
    {
        return toResult(finDetailLedgerService.deleteFinDetailLedgerByDetailIds(detailIds));
    }
}
