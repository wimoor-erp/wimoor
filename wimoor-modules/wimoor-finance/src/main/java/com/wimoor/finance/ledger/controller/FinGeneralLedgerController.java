package com.wimoor.finance.ledger.controller;

import cn.hutool.core.bean.BeanUtil;
import com.wimoor.common.core.utils.poi.ExcelUtil;
import com.wimoor.common.core.web.controller.BaseController;
import com.wimoor.common.core.web.domain.Result;
import com.wimoor.common.core.web.page.TableDataInfo;
import com.wimoor.finance.ledger.domain.FinGeneralLedger;
import com.wimoor.finance.ledger.domain.dto.FinLedgerDTO;
import com.wimoor.finance.ledger.service.IFinGeneralLedgerService;
import com.wimoor.finance.setting.service.IFinAccountingSubjectsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
    @Autowired
    private IFinAccountingSubjectsService finAccountingSubjectsService;
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
    @GetMapping("/listInfo")
    public TableDataInfo listInfo(FinLedgerDTO finGeneralLedger)
    {
        startPage();
        List<Map<String,Object>> list = finGeneralLedgerService.selectList(finGeneralLedger);//subjectId
        TableDataInfo page = getDataTable(list);
        finAccountingSubjectsService.setFinLedgerSubject(page);
        return page;
    }


    /**
     * 查询顶级科目总账汇总
     */
    @GetMapping("/topLevelSummary")
    public TableDataInfo topLevelSummary(FinLedgerDTO dto)
    {
        List<Map<String, Object>> list = finGeneralLedgerService.selectTopLevelSummary(dto);
        return getDataTable(list);
    }

    /**
     * 查询顶级科目总账汇总（完整数据，包含期初余额、本期合计、本年累计）
     */
    @GetMapping("/topLevelSummaryFull")
    public TableDataInfo topLevelSummaryFull(FinLedgerDTO dto)
    {
        List<Map<String, Object>> list = finGeneralLedgerService.selectTopLevelSummaryFull(dto);
        return getDataTable(list);
    }

    /**
     * 导出总账列表
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, FinLedgerDTO dto)
    {
        List<Map<String, Object>> list = finGeneralLedgerService.selectList(dto);
        List<FinGeneralLedger> ledgerList = new ArrayList<>(); // Changed from ExcelUtil<FinGeneralLedger> to ExcelUtil<Map<String, Object>>
        for (Map<String, Object> map : list) {
            FinGeneralLedger finGeneralLedger = new FinGeneralLedger() ;
            BeanUtil.fillBeanWithMap(map, finGeneralLedger, true);
            ledgerList.add(finGeneralLedger);
        }
        ExcelUtil<FinGeneralLedger> util = new ExcelUtil<FinGeneralLedger>(FinGeneralLedger.class);
        util.exportExcel(response, ledgerList, "总账数据");
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

    /**
     * 重建总账 - 从凭证分录重新汇总
     */
    @PostMapping("/rebuild")
    public Result rebuild(@RequestParam("groupid") String groupid)
    {
        try {
            int count = finGeneralLedgerService.rebuildGeneralLedger(groupid);
            return success("重建完成，共生成 " + count + " 条总账记录");
        } catch (Exception e) {
            return error("重建失败：" + e.getMessage());
        }
    }

    /**
     * 验证总账公式：期初余额 + 本期借方 - 本期贷方 = 期末余额
     */
    @GetMapping("/verify")
    public Result verify(@RequestParam("groupid") String groupid)
    {
        List<Map<String, Object>> errors = finGeneralLedgerService.verifyLedgerFormula(groupid);
        if (errors.isEmpty()) {
            return success("验证通过，所有记录都满足公式：期初余额 + 本期借方 - 本期贷方 = 期末余额");
        } else {
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("message", "发现 " + errors.size() + " 条记录不满足公式");
            result.put("errors", errors);
            return success(result);
        }
    }
}
