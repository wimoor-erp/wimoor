package com.wimoor.finance.closing.controller;

import com.wimoor.common.core.utils.poi.ExcelUtil;
import com.wimoor.common.core.web.controller.BaseController;
import com.wimoor.common.core.web.domain.Result;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.finance.closing.domain.FinClosingTemplateItem;
import com.wimoor.finance.closing.service.IFinClosingTemplateItemService;
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
@RequestMapping("/templateItem")
public class FinClosingTemplateItemController extends BaseController
{
    @Autowired
    private IFinClosingTemplateItemService finClosingTemplateItemService;
    @Autowired
    private IFinAccountingSubjectsService finAccountingSubjectsService;
    /**
     * 查询结账科目对应关系列表
     */
    @GetMapping("/list")
    public Result list(FinClosingTemplateItem finClosingTemplateItem)
    {
        List<FinClosingTemplateItem> list = finClosingTemplateItemService.selectFinClosingTemplateItemList(finClosingTemplateItem);
        finAccountingSubjectsService.setFinTemplateItemSubject(list);
        return  success(list);
    }

    /**
     * 导出结账科目对应关系列表
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, FinClosingTemplateItem finClosingTemplateItem)
    {
        List<FinClosingTemplateItem> list = finClosingTemplateItemService.selectFinClosingTemplateItemList(finClosingTemplateItem);
        ExcelUtil<FinClosingTemplateItem> util = new ExcelUtil<FinClosingTemplateItem>(FinClosingTemplateItem.class);
        util.exportExcel(response, list, "结账科目对应关系数据");
    }

    /**
     * 获取结账科目对应关系详细信息
     */
    @GetMapping(value = "/{id}")
    public Result getInfo(@PathVariable("id") String id)
    {
        return success(finClosingTemplateItemService.selectFinClosingTemplateItemById(id));
    }

    /**
     * 新增结账科目对应关系
     */
    @PostMapping
    public Result add(@RequestBody FinClosingTemplateItem finClosingTemplateItem)
    {
        UserInfo userinfo = UserInfoContext.get();
        finClosingTemplateItem.setCreatedTime(new Date());
        finClosingTemplateItem.setUpdatedTime(new Date());
        finClosingTemplateItem.setCreateBy(userinfo.getUserName());
        finClosingTemplateItem.setModifyBy(userinfo.getUserName());
        return toResult(finClosingTemplateItemService.insertFinClosingTemplateItem(finClosingTemplateItem));
    }

    /**
     * 修改结账科目对应关系
     */
    @PutMapping
    public Result edit(@RequestBody FinClosingTemplateItem finClosingTemplateItem)
    {
        UserInfo userinfo = UserInfoContext.get();
        finClosingTemplateItem.setUpdatedTime(new Date());
        finClosingTemplateItem.setModifyBy(userinfo.getUserName());
        return toResult(finClosingTemplateItemService.updateFinClosingTemplateItem(finClosingTemplateItem));
    }

    /**
     * 删除结账科目对应关系
     */
	@DeleteMapping("/{ids}")
    public Result remove(@PathVariable List<String> ids)
    {
        return toResult(finClosingTemplateItemService.deleteFinClosingTemplateItemByIds(ids));
    }
}
