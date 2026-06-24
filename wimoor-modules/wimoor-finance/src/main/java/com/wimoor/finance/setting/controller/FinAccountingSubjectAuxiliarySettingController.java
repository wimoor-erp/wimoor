package com.wimoor.finance.setting.controller;

import com.wimoor.common.core.utils.poi.ExcelUtil;
import com.wimoor.common.core.web.controller.BaseController;
import com.wimoor.common.core.web.domain.Result;
import com.wimoor.common.core.web.page.TableDataInfo;
import com.wimoor.finance.setting.domain.FinAccountingSubjectAuxiliarySetting;
import com.wimoor.finance.setting.service.IFinAccountingSubjectAuxiliarySettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 会计科目对应辅助核算分类设置Controller
 * 
 * @author wimoor
 * @date 2026-05-26
 */
@RestController
@RequestMapping("/setting")
public class FinAccountingSubjectAuxiliarySettingController extends BaseController
{
    @Autowired
    private IFinAccountingSubjectAuxiliarySettingService finAccountingSubjectAuxiliarySettingService;

    /**
     * 查询会计科目对应辅助核算分类设置列表
     */
    @GetMapping("/list")
    public TableDataInfo list(FinAccountingSubjectAuxiliarySetting finAccountingSubjectAuxiliarySetting)
    {
        startPage();
        List<FinAccountingSubjectAuxiliarySetting> list = finAccountingSubjectAuxiliarySettingService.selectFinAccountingSubjectAuxiliarySettingList(finAccountingSubjectAuxiliarySetting);
        return getDataTable(list);
    }

    /**
     * 导出会计科目对应辅助核算分类设置列表
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, FinAccountingSubjectAuxiliarySetting finAccountingSubjectAuxiliarySetting)
    {
        List<FinAccountingSubjectAuxiliarySetting> list = finAccountingSubjectAuxiliarySettingService.selectFinAccountingSubjectAuxiliarySettingList(finAccountingSubjectAuxiliarySetting);
        ExcelUtil<FinAccountingSubjectAuxiliarySetting> util = new ExcelUtil<FinAccountingSubjectAuxiliarySetting>(FinAccountingSubjectAuxiliarySetting.class);
        util.exportExcel(response, list, "会计科目对应辅助核算分类设置数据");
    }

    /**
     * 获取会计科目对应辅助核算分类设置详细信息
     */
    @GetMapping(value = "/{id}")
    public Result getInfo(@PathVariable("id") String id)
    {
        return success(finAccountingSubjectAuxiliarySettingService.selectFinAccountingSubjectAuxiliarySettingById(id));
    }

    /**
     * 新增会计科目对应辅助核算分类设置
     */
    @PostMapping
    public Result add(@RequestBody FinAccountingSubjectAuxiliarySetting finAccountingSubjectAuxiliarySetting)
    {
        return toResult(finAccountingSubjectAuxiliarySettingService.insertFinAccountingSubjectAuxiliarySetting(finAccountingSubjectAuxiliarySetting));
    }

    /**
     * 修改会计科目对应辅助核算分类设置
     */
    @PutMapping
    public Result edit(@RequestBody FinAccountingSubjectAuxiliarySetting finAccountingSubjectAuxiliarySetting)
    {
        return toResult(finAccountingSubjectAuxiliarySettingService.updateFinAccountingSubjectAuxiliarySetting(finAccountingSubjectAuxiliarySetting));
    }

    /**
     * 删除会计科目对应辅助核算分类设置
     */
	@DeleteMapping("/{ids}")
    public Result remove(@PathVariable String[] ids)
    {
        return toResult(finAccountingSubjectAuxiliarySettingService.deleteFinAccountingSubjectAuxiliarySettingByIds(ids));
    }
}
