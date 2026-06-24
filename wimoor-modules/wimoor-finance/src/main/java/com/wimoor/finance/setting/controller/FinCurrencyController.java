package com.wimoor.finance.setting.controller;

import com.wimoor.common.core.utils.poi.ExcelUtil;
import com.wimoor.common.core.web.controller.BaseController;
import com.wimoor.common.core.web.domain.Result;
import com.wimoor.common.core.web.page.TableDataInfo;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.finance.setting.domain.FinCurrency;
import com.wimoor.finance.setting.service.IFinCurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * currencyController
 * 
 * @author wimoor
 * @date 2026-01-15
 */
@RestController
@RequestMapping("/currency")
public class FinCurrencyController extends BaseController
{
    @Autowired
    private IFinCurrencyService finCurrencyService;

    /**
     * 查询currency列表
     */
    @GetMapping("/list")
    public TableDataInfo list(FinCurrency finCurrency)
    {
        startPage();
        List<FinCurrency> list = finCurrencyService.selectFinCurrencyList(finCurrency);
        //当列表为空时，至少返回一个带有CNY币种的list
        if (list.isEmpty()) {
            FinCurrency cny = new FinCurrency();
            cny.setCode("CNY");
            cny.setName("人民币");
            cny.setRate(new BigDecimal("1.00"));
            cny.setIslocal(1);
            return getDataTable(List.of(cny));
        }else{
            return getDataTable(list);
        }
    }

    /**
     * 导出currency列表
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, FinCurrency finCurrency)
    {
        List<FinCurrency> list = finCurrencyService.selectFinCurrencyList(finCurrency);
        ExcelUtil<FinCurrency> util = new ExcelUtil<FinCurrency>(FinCurrency.class);
        util.exportExcel(response, list, "currency数据");
    }

    /**
     * 获取currency详细信息
     */
    @GetMapping("/getInfo")
    public Result getInfo(@RequestParam("code") String code,@RequestParam("groupid") String groupid)
    {
        return success(finCurrencyService.selectFinCurrencyByCode(code,groupid));
    }

    /**
     * 新增currency
     */
    @PostMapping
    public Result save(@RequestBody FinCurrency finCurrency)
    {
        UserInfo userinfo = UserInfoContext.get();
        FinCurrency exist = finCurrencyService.selectFinCurrencyByCode(finCurrency.getCode(), finCurrency.getGroupid());
        if(exist!=null){
            finCurrency.setUpdateBy(userinfo.getUserName());
            finCurrency.setUpdatedTime(new Date());
            return toResult(finCurrencyService.updateFinCurrency(finCurrency));
        }else{
            finCurrency.setCreateBy(userinfo.getUserName());
            finCurrency.setCreatedTime(new Date());
            return toResult(finCurrencyService.insertFinCurrency(finCurrency));
        }
    }


    /**
     * 删除currency
     */
	@DeleteMapping("/{codes}")
    public Result remove(@PathVariable String[] codes)
    {
        return toResult(finCurrencyService.deleteFinCurrencyByCodes(codes));
    }
}
