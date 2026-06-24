package com.wimoor.finance.voucher.controller;

import com.wimoor.common.core.utils.poi.ExcelUtil;
import com.wimoor.common.core.web.controller.BaseController;
import com.wimoor.common.core.web.domain.Result;
import com.wimoor.common.core.web.page.TableDataInfo;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.finance.setting.service.IFinAccountingSubjectsService;
import com.wimoor.finance.util.QueryParamUtil;
import com.wimoor.finance.voucher.domain.FinVouchers;
import com.wimoor.finance.voucher.domain.dto.FinVoucherDTO;
import com.wimoor.finance.voucher.service.IFinVouchersService;
import com.wimoor.finance.voucher.service.util.BatchPostingResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * 记账凭证Controller
 *
 * @author wimoor
 * @date 2025-11-04
 */
@RestController
@RequestMapping("/vouchers")
public class FinVouchersController extends BaseController
{
    @Autowired
    private IFinVouchersService finVouchersService;
    @Autowired
    private IFinAccountingSubjectsService finAccountingSubjectsService;
    /**
     * 查询记账凭证列表
     */
    @GetMapping("/list")
    public TableDataInfo list(FinVoucherDTO dto)
    {
        startPage();
        handlerDatePeriod(dto);
        List<FinVouchers> list = finVouchersService.selectFinVouchersList(dto);
        finAccountingSubjectsService.setFinVoucherSubject(list);
        return getDataTable(list);
    }


    /**
     * 导出记账凭证列表
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, FinVoucherDTO dto)
    {   handlerDatePeriod(dto);
        List<FinVouchers> list = finVouchersService.selectFinVouchersList(dto);
        ExcelUtil<FinVouchers> util = new ExcelUtil<FinVouchers>(FinVouchers.class);
        util.exportExcel(response, list, "记账凭证数据");
    }

    /**
     * 查询下一个凭证号
     */
    @PostMapping(value = "/nextVoucherNo")
    public Result selectNextVoucherNo(@RequestBody FinVouchers finVouchers)
    {
        return success(finVouchersService.selectNextVoucherNo(finVouchers));
    }

    /**
     * 获取记账凭证详细信息
     */
    @GetMapping(value = "/{voucherId}")
    public Result getInfo(@PathVariable("voucherId") Long voucherId)
    {
        return success(finVouchersService.selectFinVouchersByVoucherId(voucherId));
    }

    /**
     * 新增记账凭证
     */
    @PostMapping
    public Result add(@RequestBody FinVouchers finVouchers)
    {
        UserInfo userinfo = UserInfoContext.get();
        finVouchers.setPreparerBy(userinfo.getUserName());
        if (finVouchers.getDataSource() == null) {
            finVouchers.setDataSource(1); // 默认手动录入
        }
        return toResult(finVouchersService.insertFinVouchers(finVouchers));
    }

    @PostMapping("/files")
    public Result filesUpdate(@RequestBody FinVouchers finVouchers)
    {
        return toResult(finVouchersService.filesFinVouchers(finVouchers));
    }

    /**
     * 修改记账凭证
     */
    @PutMapping
    public Result edit(@RequestBody FinVouchers finVouchers)
    {
        return toResult(finVouchersService.updateFinVouchers(finVouchers));
    }

    /**
     * 删除记账凭证
     */
    @DeleteMapping("/{voucherIds}")
    public Result remove(@PathVariable Long[] voucherIds)
    {
        return toResult(finVouchersService.deleteFinVouchersByVoucherIds(voucherIds));
    }
    private  void handlerDatePeriod(FinVoucherDTO finVoucherEntries){
        if(finVoucherEntries.getVoucherId()!=null){
            return;
        }
        Date[] dateRange = QueryParamUtil.parseDatePeriodRange(finVoucherEntries.getStartPeriod(), finVoucherEntries.getEndPeriod());
        Date startDate = dateRange[0];
        Date endDate = dateRange[1];
        finVoucherEntries.setStartDate(startDate);
        finVoucherEntries.setEndDate(endDate);
    }


    /**
     * 审核记账凭证
     */
    @PostMapping("/approve")
    public Result approve(@RequestBody List<Long> finVoucherIds)
    {
        if (finVoucherIds != null && finVoucherIds.size() > 0) {
            int result = finVouchersService.approveFinVouchers(finVoucherIds);
            return success(result);
        } else {
            return error("请正确选择凭证！");
        }
    }


    /*
     * 记账凭证过账
     * */

    @PostMapping("/passVouchers")
    public Result passVouchers(@RequestBody List<Long> finVoucherIds) {
        if (finVoucherIds != null && finVoucherIds.size() > 0) {
            BatchPostingResult result = finVouchersService.batchPostVouchers(finVoucherIds);
            if(result.getFailureVouchers().size()==0){
                return success(result);
            }else{
                return error("以下凭证过账失败："+result.getFailureVouchers());
            }
        } else {
            return error("请正确选择凭证！");
        }
    }
}
