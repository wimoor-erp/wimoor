package com.wimoor.finance.closing.controller;

import com.wimoor.common.core.utils.poi.ExcelUtil;
import com.wimoor.common.core.web.controller.BaseController;
import com.wimoor.common.core.web.domain.Result;
import com.wimoor.common.core.web.page.TableDataInfo;
import com.wimoor.finance.closing.domain.FinClosingTemplateFeishu;
import com.wimoor.finance.closing.service.IFinClosingTemplateFeishuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 结算飞书格同步Controller
 * 
 * @author wimoor
 * @date 2026-05-11
 */
@RestController
@RequestMapping("/closing_template/feishu")
public class FinClosingTemplateFeishuController extends BaseController
{
    @Autowired
    private IFinClosingTemplateFeishuService finClosingTemplateFeishuService;

    /**
     * 查询结算飞书格同步列表
     */
    @GetMapping("/list")
    public TableDataInfo list(FinClosingTemplateFeishu finClosingTemplateFeishu)
    {
        startPage();
        List<FinClosingTemplateFeishu> list = finClosingTemplateFeishuService.selectFinClosingTemplateFeishuList(finClosingTemplateFeishu);
        return getDataTable(list);
    }

    /**
     * 导出结算飞书格同步列表
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, FinClosingTemplateFeishu finClosingTemplateFeishu)
    {
        List<FinClosingTemplateFeishu> list = finClosingTemplateFeishuService.selectFinClosingTemplateFeishuList(finClosingTemplateFeishu);
        ExcelUtil<FinClosingTemplateFeishu> util = new ExcelUtil<FinClosingTemplateFeishu>(FinClosingTemplateFeishu.class);
        util.exportExcel(response, list, "结算飞书格同步数据");
    }

    /**
     * 获取结算飞书格同步详细信息
     */
    @GetMapping(value = "/{templateid}")
    public Result getInfo(@PathVariable("templateid") String templateid)
    {
        return success(finClosingTemplateFeishuService.selectFinClosingTemplateFeishuByTemplateid(templateid));
    }

    /**
     * 新增结算飞书格同步
     */
    @PostMapping
    public Result add(@RequestBody FinClosingTemplateFeishu finClosingTemplateFeishu)
    {
        return toResult(finClosingTemplateFeishuService.insertFinClosingTemplateFeishu(finClosingTemplateFeishu));
    }

    /**
     * 修改结算飞书格同步
     */
    @PutMapping
    public Result edit(@RequestBody FinClosingTemplateFeishu finClosingTemplateFeishu)
    {
        return toResult(finClosingTemplateFeishuService.updateFinClosingTemplateFeishu(finClosingTemplateFeishu));
    }

    /**
     * 删除结算飞书格同步
     */
	@DeleteMapping("/{templateids}")
    public Result remove(@PathVariable String[] templateids)
    {
        return toResult(finClosingTemplateFeishuService.deleteFinClosingTemplateFeishuByTemplateids(templateids));
    }
}
