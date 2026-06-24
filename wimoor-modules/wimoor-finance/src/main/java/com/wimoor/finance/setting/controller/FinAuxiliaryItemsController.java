package com.wimoor.finance.setting.controller;

import com.wimoor.common.core.utils.poi.ExcelUtil;
import com.wimoor.common.core.web.controller.BaseController;
import com.wimoor.common.core.web.domain.Result;
import com.wimoor.common.core.web.page.TableDataInfo;
import com.wimoor.common.user.UserInfo;
import com.wimoor.common.user.UserInfoContext;
import com.wimoor.finance.setting.domain.FinAuxiliaryItems;
import com.wimoor.finance.setting.service.IFinAuxiliaryItemsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * 辅助核算具体项目Controller
 * 
 * @author wimoor
 * @date 2025-11-04
 */
@RestController
@RequestMapping("/auxiliary/items")
public class FinAuxiliaryItemsController extends BaseController
{
    @Autowired
    private IFinAuxiliaryItemsService finAuxiliaryItemsService;

    /**
     * 查询辅助核算具体项目列表
     */
    @GetMapping("/list")
    public TableDataInfo list(FinAuxiliaryItems finAuxiliaryItems)
    {
        startPage();
        UserInfo userInfo = UserInfoContext.get();
        String companyid = userInfo.getCompanyid();
        finAuxiliaryItems.setShopid(companyid);
        List<FinAuxiliaryItems> list = finAuxiliaryItemsService.selectFinAuxiliaryItemsList(finAuxiliaryItems);
        return getDataTable(list);
    }

    /**
     * 导出辅助核算具体项目列表
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, FinAuxiliaryItems finAuxiliaryItems)
    {
        List<FinAuxiliaryItems> list = finAuxiliaryItemsService.selectFinAuxiliaryItemsList(finAuxiliaryItems);
        ExcelUtil<FinAuxiliaryItems> util = new ExcelUtil<FinAuxiliaryItems>(FinAuxiliaryItems.class);
        util.exportExcel(response, list, "辅助核算具体项目数据");
    }

    /**
     * 获取辅助核算具体项目详细信息
     */
    @GetMapping(value = "/{itemId}")
    public Result getInfo(@PathVariable("itemId") Long itemId)
    {
        return success(finAuxiliaryItemsService.selectFinAuxiliaryItemsByItemId(itemId));
    }

    /**
     * 新增辅助核算具体项目
     */
    @PostMapping
    public Result add(@RequestBody FinAuxiliaryItems finAuxiliaryItems)
    {
        finAuxiliaryItems.setCreateTime(new Date());
        return toResult(finAuxiliaryItemsService.insertFinAuxiliaryItems(finAuxiliaryItems));
    }

    private String getUsername() {
        UserInfo userInfo = UserInfoContext.get();
        return userInfo.getUserName();
    }

    /**
     * 修改辅助核算具体项目
     */
    @PutMapping
    public Result edit(@RequestBody FinAuxiliaryItems finAuxiliaryItems)
    {
        return toResult(finAuxiliaryItemsService.updateFinAuxiliaryItems(finAuxiliaryItems));
    }

    /**
     * 删除辅助核算具体项目
     */
	@DeleteMapping("/{itemIds}")
    public Result remove(@PathVariable Long[] itemIds)
    {
        return toResult(finAuxiliaryItemsService.deleteFinAuxiliaryItemsByItemIds(itemIds));
    }

    /**
     * 从ERP模块同步供应商数据到辅助核算项目
     */
    @PostMapping("/syncSupplier")
    public Result syncSupplier(@RequestBody FinAuxiliaryItems finAuxiliaryItems)
    {
        UserInfo userInfo = UserInfoContext.get();
        String companyid = userInfo.getCompanyid();
        finAuxiliaryItems.setShopid(companyid);
        int count = finAuxiliaryItemsService.syncSupplierFromErp(finAuxiliaryItems);
        return success("同步成功，新增 " + count + " 条记录");
    }

    /**
     * 从Admin模块同步员工数据到辅助核算项目
     */
    @PostMapping("/syncEmployee")
    public Result syncEmployee(@RequestBody FinAuxiliaryItems finAuxiliaryItems)
    {
        UserInfo userInfo = UserInfoContext.get();
        String userid = userInfo.getId().toString();
        finAuxiliaryItems.setShopid(userid);
        int count = finAuxiliaryItemsService.syncEmployeeFromAdmin(finAuxiliaryItems);
        return success("同步成功，新增 " + count + " 条记录");
    }

    /**
     * 从Admin模块同步部门数据到辅助核算项目
     */
    @PostMapping("/syncDept")
    public Result syncDept(@RequestBody FinAuxiliaryItems finAuxiliaryItems)
    {
        UserInfo userInfo = UserInfoContext.get();
        String companyid = userInfo.getCompanyid();
        finAuxiliaryItems.setShopid(companyid);
        int count = finAuxiliaryItemsService.syncDeptFromAdmin(finAuxiliaryItems);
        return success("同步成功，新增 " + count + " 条记录");
    }
}
