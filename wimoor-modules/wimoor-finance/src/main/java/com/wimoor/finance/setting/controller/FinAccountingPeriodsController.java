package com.wimoor.finance.setting.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.finance.closing.service.ReverseClosingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.wimoor.finance.setting.domain.FinAccountingPeriods;
import com.wimoor.finance.setting.service.IFinAccountingPeriodsService;
import com.wimoor.common.core.web.controller.BaseController;
import com.wimoor.common.core.web.domain.Result;
import com.wimoor.common.core.utils.poi.ExcelUtil;
import com.wimoor.common.core.web.page.TableDataInfo;
import com.wimoor.finance.closing.service.AccountingClosingService;
/**
 * 会计期间管理Controller
 * 
 * @author wimoor
 * @date 2025-11-04
 */
@RestController
@RequestMapping("/periods")
public class FinAccountingPeriodsController extends BaseController
{
    @Autowired
    private IFinAccountingPeriodsService finAccountingPeriodsService;
    @Autowired
    private AccountingClosingService accountingClosingService;
    @Autowired
    ReverseClosingService reverseClosingService;
    /**
     * 查询会计期间管理列表
     */
    @GetMapping("/list")
    public TableDataInfo list(FinAccountingPeriods finAccountingPeriods)
    {
        startPage();
        List<FinAccountingPeriods> list = finAccountingPeriodsService.selectFinAccountingPeriodsList(finAccountingPeriods);
        return getDataTable(list);
    }

    /**
     * 导出会计期间管理列表
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, FinAccountingPeriods finAccountingPeriods)
    {
        List<FinAccountingPeriods> list = finAccountingPeriodsService.selectFinAccountingPeriodsList(finAccountingPeriods);
        ExcelUtil<FinAccountingPeriods> util = new ExcelUtil<FinAccountingPeriods>(FinAccountingPeriods.class);
        util.exportExcel(response, list, "会计期间管理数据");
    }

    /**
     * 获取会计期间管理详细信息
     */
    @GetMapping(value = "/{periodId}")
    public Result getInfo(@PathVariable("periodId") Long periodId)
    {
        return success(finAccountingPeriodsService.selectFinAccountingPeriodsByPeriodId(periodId));
    }

    /**
     * 新增会计期间管理
     */
    @PostMapping
    public Result add(@RequestBody FinAccountingPeriods finAccountingPeriods)
    {
        return toResult(finAccountingPeriodsService.insertFinAccountingPeriods(finAccountingPeriods));
    }

    /**
     * 修改会计期间管理
     */
    @PutMapping
    public Result edit(@RequestBody FinAccountingPeriods finAccountingPeriods)
    {
        return toResult(finAccountingPeriodsService.updateFinAccountingPeriods(finAccountingPeriods));
    }

    /**
     * 删除会计期间管理
     */
	@DeleteMapping("/{periodIds}")
    public Result remove(@PathVariable Long[] periodIds)
    {
        return toResult(finAccountingPeriodsService.deleteFinAccountingPeriodsByPeriodIds(periodIds));
    }


    /**
     * 会计期间管理
     */
    @GetMapping("/init/{groupId}")
    public Result init(@PathVariable String groupId)
    {   finAccountingPeriodsService.initAccountingPeriods(groupId);
        return success();
    }
    /**
     * 会计期间管理
     */
    @GetMapping("/initByYear")
    public Result initByYear(@RequestParam String year)
    {   finAccountingPeriodsService.initAccountingPeriods(Integer.valueOf(year));
        return success();
    }
    /**
     * 会计期间管理
     */
    @GetMapping("/init")
    public Result init()
    {   finAccountingPeriodsService.initAccountingPeriods();
        return success();
    }

    @GetMapping("/closing")
    public Result closing(@RequestParam String groupId,@RequestParam String period)
    {   UserInfo user= UserInfoContext.get();
        String operator=user.getUserName();
        accountingClosingService.executeFullClosing(groupId,period,operator);
        return success();
    }
    @GetMapping("/unclosing")
    public Result unclosing(@RequestParam String groupId,@RequestParam String period,@RequestParam String reason)
    {   UserInfo user= UserInfoContext.get();
        String operator=user.getUserName();
        reverseClosingService.executeReverseClosing(groupId,period,operator,reason);
        return success();
    }

}
