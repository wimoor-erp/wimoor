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
import com.wimoor.finance.ledger.domain.FinGeneralLedger;
import com.wimoor.finance.ledger.service.IFinGeneralLedgerService;
import com.wimoor.common.core.web.controller.BaseController;
import com.wimoor.common.core.web.domain.Result;
import com.wimoor.common.core.utils.poi.ExcelUtil;
import com.wimoor.common.core.web.page.TableDataInfo;

/**
 * 总账Controller
 * 
 * @author wimoor
 * @date 2025-11-04
 */
@RestController
@RequestMapping("/ledger")
public class FinGeneralLedgerController extends BaseController
{
    @Autowired
    private IFinGeneralLedgerService finGeneralLedgerService;

    /**
     * 查询总账列表
     */
    @GetMapping("/list")
    public TableDataInfo list(FinGeneralLedger finGeneralLedger)
    {
        startPage();
        List<FinGeneralLedger> list = finGeneralLedgerService.selectFinGeneralLedgerList(finGeneralLedger);
        return getDataTable(list);
    }

    /**
     * 查询总账列表
     */
    @PostMapping("/listInfo")
    public TableDataInfo listInfo(@RequestBody FinLedgerDTO finGeneralLedger)
    {
        startPage();
        List<Map<String,Object>> list = finGeneralLedgerService.selectList(finGeneralLedger);
        return getDataTable(list);
    }


    /**
     * 导出总账列表
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, FinGeneralLedger finGeneralLedger)
    {
        List<FinGeneralLedger> list = finGeneralLedgerService.selectFinGeneralLedgerList(finGeneralLedger);
        ExcelUtil<FinGeneralLedger> util = new ExcelUtil<FinGeneralLedger>(FinGeneralLedger.class);
        util.exportExcel(response, list, "总账数据");
    }

    /**
     * 获取总账详细信息
     */
    @GetMapping(value = "/{ledgerId}")
    public Result getInfo(@PathVariable("ledgerId") Long ledgerId)
    {
        return success(finGeneralLedgerService.selectFinGeneralLedgerByLedgerId(ledgerId));
    }

    /**
     * 新增总账
     */
    @PostMapping
    public Result add(@RequestBody FinGeneralLedger finGeneralLedger)
    {
        return toResult(finGeneralLedgerService.insertFinGeneralLedger(finGeneralLedger));
    }

    /**
     * 修改总账
     */
    @PutMapping
    public Result edit(@RequestBody FinGeneralLedger finGeneralLedger)
    {
        return toResult(finGeneralLedgerService.updateFinGeneralLedger(finGeneralLedger));
    }

    /**
     * 删除总账
     */
	@DeleteMapping("/{ledgerIds}")
    public Result remove(@PathVariable Long[] ledgerIds)
    {
        return toResult(finGeneralLedgerService.deleteFinGeneralLedgerByLedgerIds(ledgerIds));
    }
}
