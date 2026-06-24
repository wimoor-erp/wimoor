package com.wimoor.finance.closing.controller;

import java.util.List;
import java.util.Date;
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
import com.wimoor.finance.closing.domain.FinClosingTemplateProfitLoss;
import com.wimoor.finance.closing.service.IFinClosingTemplateProfitLossService;
import com.wimoor.common.core.web.controller.BaseController;
import com.wimoor.common.core.web.domain.Result;
import com.wimoor.common.core.utils.poi.ExcelUtil;
import com.wimoor.common.core.web.page.TableDataInfo;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;

/**
 * 结转损益凭证模板配置Controller
 * 
 * @author wimoor
 * @date 2026-06-10
 */
@RestController
@RequestMapping("/closing_template_profit_loss")
public class FinClosingTemplateProfitLossController extends BaseController
{
    @Autowired
    private IFinClosingTemplateProfitLossService finClosingTemplateProfitLossService;

    /**
     * 查询结转损益凭证模板配置列表
     */
    @GetMapping("/list")
    public TableDataInfo list(FinClosingTemplateProfitLoss finClosingTemplateProfitLoss)
    {
        startPage();
        List<FinClosingTemplateProfitLoss> list = finClosingTemplateProfitLossService.selectFinClosingTemplateProfitLossList(finClosingTemplateProfitLoss);
        return getDataTable(list);
    }

    /**
     * 导出结转损益凭证模板配置列表
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, FinClosingTemplateProfitLoss finClosingTemplateProfitLoss)
    {
        List<FinClosingTemplateProfitLoss> list = finClosingTemplateProfitLossService.selectFinClosingTemplateProfitLossList(finClosingTemplateProfitLoss);
        ExcelUtil<FinClosingTemplateProfitLoss> util = new ExcelUtil<FinClosingTemplateProfitLoss>(FinClosingTemplateProfitLoss.class);
        util.exportExcel(response, list, "结转损益凭证模板配置数据");
    }

    /**
     * 获取结转损益凭证模板配置详细信息
     */
    @GetMapping(value = "/{id}")
    public Result getInfo(@PathVariable("id") String id)
    {
        return success(finClosingTemplateProfitLossService.selectFinClosingTemplateProfitLossById(id));
    }

    /**
     * 新增结转损益凭证模板配置
     */
    @PostMapping
    public Result add(@RequestBody FinClosingTemplateProfitLoss finClosingTemplateProfitLoss)
    {
        UserInfo userinfo = UserInfoContext.get();
        finClosingTemplateProfitLoss.setCreateBy(userinfo.getUserName());
        finClosingTemplateProfitLoss.setCreatedTime(new Date());
        finClosingTemplateProfitLoss.setModifyBy(userinfo.getUserName());
        finClosingTemplateProfitLoss.setUpdatedTime(new Date());
        return toResult(finClosingTemplateProfitLossService.insertFinClosingTemplateProfitLoss(finClosingTemplateProfitLoss));
    }

    /**
     * 修改结转损益凭证模板配置
     */
    @PutMapping
    public Result edit(@RequestBody FinClosingTemplateProfitLoss finClosingTemplateProfitLoss)
    {
        UserInfo userinfo = UserInfoContext.get();
        finClosingTemplateProfitLoss.setModifyBy(userinfo.getUserName());
        finClosingTemplateProfitLoss.setUpdatedTime(new Date());
        return toResult(finClosingTemplateProfitLossService.updateFinClosingTemplateProfitLoss(finClosingTemplateProfitLoss));
    }

    /**
     * 删除结转损益凭证模板配置
     */
	@DeleteMapping("/{ids}")
    public Result remove(@PathVariable String[] ids)
    {
        return toResult(finClosingTemplateProfitLossService.deleteFinClosingTemplateProfitLossByIds(ids));
    }

    /**
     * 根据模板ID获取结转损益配置
     */
    @GetMapping("/template/{templateId}")
    public Result getByTemplateId(@PathVariable("templateId") String templateId)
    {
        FinClosingTemplateProfitLoss query = new FinClosingTemplateProfitLoss();
        query.setTemplateId(templateId);
        List<FinClosingTemplateProfitLoss> list = finClosingTemplateProfitLossService.selectFinClosingTemplateProfitLossList(query);
        if (list != null && !list.isEmpty()) {
            return success(list.get(0));
        }
        return success(null);
    }
}
