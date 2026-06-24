package com.wimoor.finance.closing.controller;

import com.wimoor.common.core.utils.poi.ExcelUtil;
import com.wimoor.common.core.web.controller.BaseController;
import com.wimoor.common.core.web.domain.Result;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.finance.closing.domain.FinClosingTemplateAmazon;
import com.wimoor.finance.closing.service.IFinClosingTemplateAmazonService;
import com.wimoor.finance.setting.service.IFinAccountingSubjectsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * 结账科目对应关系Controller
 * 
 * @author wimoor
 * @date 2026-03-19
 */
@RestController
@RequestMapping("/templateAmazon")
public class FinClosingTemplateAmazonController extends BaseController
{
    @Autowired
    private IFinClosingTemplateAmazonService finClosingTemplateAmazonService;
    @Autowired
    private IFinAccountingSubjectsService finAccountingSubjectsService;
    /**
     * 查询结账科目对应关系列表
     */
    @GetMapping("/list")
    public Result list(FinClosingTemplateAmazon finClosingTemplateAmazon)
    {
        List<FinClosingTemplateAmazon> list = finClosingTemplateAmazonService.selectFinClosingTemplateAmazonList(finClosingTemplateAmazon);
        finAccountingSubjectsService.setFinTemplateSubject(list);
        return  success(list);
    }

    /**
     * 导出结账科目对应关系列表
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, FinClosingTemplateAmazon finClosingTemplateAmazon)
    {
        List<FinClosingTemplateAmazon> list = finClosingTemplateAmazonService.selectFinClosingTemplateAmazonList(finClosingTemplateAmazon);
        ExcelUtil<FinClosingTemplateAmazon> util = new ExcelUtil<FinClosingTemplateAmazon>(FinClosingTemplateAmazon.class);
        util.exportExcel(response, list, "结账科目对应关系数据");
    }

    /**
     * 获取结账科目对应关系详细信息
     */
    @GetMapping(value = "/{id}")
    public Result getInfo(@PathVariable("id") String id)
    {
        return success(finClosingTemplateAmazonService.selectFinClosingTemplateAmazonById(id));
    }

    /**
     * 新增结账科目对应关系
     */
    @PostMapping
    public Result add(@RequestBody FinClosingTemplateAmazon finClosingTemplateAmazon)
    {
        UserInfo userinfo = UserInfoContext.get();
        finClosingTemplateAmazon.setCreatedTime(new Date());
        finClosingTemplateAmazon.setUpdatedTime(new Date());
        finClosingTemplateAmazon.setCreateBy(userinfo.getUserName());
        finClosingTemplateAmazon.setModifyBy(userinfo.getUserName());
        return toResult(finClosingTemplateAmazonService.insertFinClosingTemplateAmazon(finClosingTemplateAmazon));
    }

    /**
     * 修改结账科目对应关系
     */
    @PutMapping
    public Result edit(@RequestBody FinClosingTemplateAmazon finClosingTemplateAmazon)
    {
        UserInfo userinfo = UserInfoContext.get();
        finClosingTemplateAmazon.setUpdatedTime(new Date());
        finClosingTemplateAmazon.setModifyBy(userinfo.getUserName());
        return toResult(finClosingTemplateAmazonService.updateFinClosingTemplateAmazon(finClosingTemplateAmazon));
    }

    /**
     * 删除结账科目对应关系
     */
	@DeleteMapping("/{ids}")
    public Result remove(@PathVariable List<String> ids)
    {
        return toResult(finClosingTemplateAmazonService.deleteFinClosingTemplateAmazonByIds(ids));
    }
}
