package com.wimoor.finance.setting.controller;

import com.wimoor.common.core.utils.poi.ExcelUtil;
import com.wimoor.common.core.web.controller.BaseController;
import com.wimoor.common.core.web.domain.Result;
import com.wimoor.common.core.web.page.TableDataInfo;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.finance.closing.service.impl.AccountingClosingService;
import com.wimoor.finance.closing.service.impl.ReverseClosingService;
import com.wimoor.finance.closing.domain.ClosingResult;
import com.wimoor.finance.closing.domain.ReverseClosingResult;
import com.wimoor.finance.setting.domain.FinAccountingPeriods;
import com.wimoor.finance.setting.service.IFinAccountingPeriodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

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
        ClosingResult result = accountingClosingService.executeFullClosing(groupId,period,operator);
        
        if(result.isSuccess()){
            // 返回成功，包含结账结果数据（含警告信息）
            return success(result.getMessage());
        } else {
            // 返回失败，包含错误信息
            return error(result.getMessage());
        }
    }
    @GetMapping("/unclosing")
    public Result unclosing(@RequestParam String groupId,@RequestParam String period,@RequestParam String reason)
    {   UserInfo user= UserInfoContext.get();
        String operator=user.getUserName();
        try {
            ReverseClosingResult result = reverseClosingService.executeReverseClosing(groupId,period,operator,reason);
            if(result.isSuccess()){
                // 返回成功，包含反结账结果数据
                return success(result.getOperationSummary());
            } else {
                // 返回失败，包含错误信息
                return error(result.getMessage());
            }
        } catch (Exception e) {
            logger.error("反结账异常: groupid={}, period={}", groupId, period, e);
            return error("反结账失败: " + e.getMessage());
        }
    }

    /**
     * 开期操作
     */
    @GetMapping("/open")
    public Result open(@RequestParam String groupId,@RequestParam String period)
    {   boolean result = finAccountingPeriodsService.openPeriod(groupId, period);
        if (result) {
            return success("开期成功");
        } else {
            return error("开期失败");
        }
    }

    /**
     * 过账操作
     */
    @GetMapping("/post")
    public Result post(@RequestParam String groupId,@RequestParam String period)
    {   boolean result = finAccountingPeriodsService.postPeriod(groupId, period);
        if (result) {
            return success("过账成功");
        } else {
            return error("过账失败");
        }
    }

    /**
     * 获取当前会计期间
     */
    @GetMapping("/current")
    public Result getCurrentPeriod(@RequestParam String groupId)
    {   FinAccountingPeriods period = finAccountingPeriodsService.getCurrentPeriod(groupId);
        return success(period);
    }

    @GetMapping("/groups")
    public Result getGroups()
    {   UserInfo user= UserInfoContext.get();
        List<Map<String,Object>> groups = finAccountingPeriodsService.getGroups(user);
        return success(groups);
    }

    /**
     * 修正会计期间状态
     * 逻辑：
     * 1. 检查是否有结转损益的凭证
     * 2. 如果没有，当前会计期间应该是最早的凭证对应的会计期间
     * 3. 如果有，最后的结转损益凭证的会计期间的下一个会计期间就是当前会计期间
     * 4. 当前及之前的会计期间为开启状态，未来的为未开启状态
     */
    @GetMapping("/fixCurrentPeriod")
    public Result fixCurrentPeriod(@RequestParam String groupId)
    {
        finAccountingPeriodsService.fixCurrentPeriod(groupId);
        return success("会计期间修正成功");
    }

}
