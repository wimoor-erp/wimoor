package com.wimoor.finance.closing.controller;

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
import com.wimoor.finance.closing.domain.FinClosingTemplateVouchers;
import com.wimoor.finance.closing.service.IFinClosingTemplateVouchersService;
import com.wimoor.common.core.web.controller.BaseController;
import com.wimoor.common.core.web.domain.Result;
import com.wimoor.common.core.utils.poi.ExcelUtil;
import com.wimoor.common.core.web.page.TableDataInfo;

/**
 * 结账模板凭证Controller
 * 
 * @author wimoor
 * @date 2026-03-20
 */
@RestController
@RequestMapping("/closingTemplateVouchers")
public class FinClosingTemplateVouchersController extends BaseController
{
    @Autowired
    private IFinClosingTemplateVouchersService finClosingTemplateVouchersService;

    /**
     * 查询结账模板凭证列表
     */
    @GetMapping("/list")
    public TableDataInfo list(FinClosingTemplateVouchers finClosingTemplateVouchers)
    {
        startPage();
        List<FinClosingTemplateVouchers> list = finClosingTemplateVouchersService.selectFinClosingTemplateVouchersList(finClosingTemplateVouchers);
        return getDataTable(list);
    }

    /**
     * 导出结账模板凭证列表
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, FinClosingTemplateVouchers finClosingTemplateVouchers)
    {
        List<FinClosingTemplateVouchers> list = finClosingTemplateVouchersService.selectFinClosingTemplateVouchersList(finClosingTemplateVouchers);
        ExcelUtil<FinClosingTemplateVouchers> util = new ExcelUtil<FinClosingTemplateVouchers>(FinClosingTemplateVouchers.class);
        util.exportExcel(response, list, "结账模板凭证数据");
    }

    /**
     * 获取结账模板凭证详细信息
     */
    @GetMapping(value = "/{id}")
    public Result getInfo(@PathVariable("id") String id)
    {
        return success(finClosingTemplateVouchersService.selectFinClosingTemplateVouchersById(id));
    }

    /**
     * 新增结账模板凭证
     */
    @PostMapping
    public Result add(@RequestBody FinClosingTemplateVouchers finClosingTemplateVouchers)
    {
        return toResult(finClosingTemplateVouchersService.insertFinClosingTemplateVouchers(finClosingTemplateVouchers));
    }

    /**
     * 修改结账模板凭证
     */
    @PutMapping
    public Result edit(@RequestBody FinClosingTemplateVouchers finClosingTemplateVouchers)
    {
        return toResult(finClosingTemplateVouchersService.updateFinClosingTemplateVouchers(finClosingTemplateVouchers));
    }

    /**
     * 删除结账模板凭证
     */
	@DeleteMapping("/{ids}")
    public Result remove(@PathVariable String[] ids)
    {
        return toResult(finClosingTemplateVouchersService.deleteFinClosingTemplateVouchersByIds(ids));
    }
}
