package com.wimoor.finance.voucher.controller;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

import com.wimoor.finance.voucher.domain.FinVoucherEntries;
import com.wimoor.finance.voucher.domain.dto.FinVoucherDTO;
import com.wimoor.finance.voucher.service.util.BatchPostingResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.wimoor.finance.voucher.domain.FinVouchers;
import com.wimoor.finance.voucher.service.IFinVouchersService;
import com.wimoor.common.core.web.controller.BaseController;
import com.wimoor.common.core.web.domain.Result;
import com.wimoor.common.core.utils.poi.ExcelUtil;
import com.wimoor.common.core.web.page.TableDataInfo;

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

    /**
     * 查询记账凭证列表
     */
    @GetMapping("/list")
    public TableDataInfo list(FinVoucherDTO dto)
    {
        startPage();
        handlerDateStr(dto);
        List<FinVouchers> list = finVouchersService.selectFinVouchersList(dto);
        return getDataTable(list);
    }

    /**
     * 导出记账凭证列表
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, FinVoucherDTO dto)
    {   handlerDateStr(dto);
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
    private  void handlerDateStr(FinVoucherDTO finVoucherEntries){
        if(finVoucherEntries.getVoucherDateStr()!=null){
            String dateStr = finVoucherEntries.getVoucherDateStr().get(0);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM dd yyyy HH:mm:ss 'GMT'Z '('中国标准时间')'", Locale.ENGLISH);
            ZonedDateTime zonedDateTime = ZonedDateTime.parse(dateStr, formatter);
            // 转换为传统的Date对象
            Date start_date = Date.from(zonedDateTime.toInstant());
            finVoucherEntries.setStartDate(start_date);

            String dateStr2 = finVoucherEntries.getVoucherDateStr().get(1);
            DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("EEE MMM dd yyyy HH:mm:ss 'GMT'Z '('中国标准时间')'", Locale.ENGLISH);
            ZonedDateTime zonedDateTime2 = ZonedDateTime.parse(dateStr2, formatter2);
            // 转换为传统的Date对象
            Calendar calendar = Calendar.getInstance();
            Date end_date = Date.from(zonedDateTime2.toInstant());
            calendar.setTime(end_date);
            // 设置为该月第一天
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            // 然后设置为下个月的第一天
            calendar.add(Calendar.MONTH, 1);
            // 再减去一天，得到当前月的最后一天
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            finVoucherEntries.setEndDate(calendar.getTime());
        }
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
