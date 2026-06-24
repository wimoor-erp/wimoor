package com.wimoor.finance.setting.controller;

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
import com.wimoor.finance.setting.domain.FinSubjectType;
import com.wimoor.finance.setting.service.IFinSubjectTypeService;
import com.wimoor.common.core.web.controller.BaseController;
import com.wimoor.common.core.web.domain.Result;
import com.wimoor.common.core.utils.poi.ExcelUtil;
import com.wimoor.common.core.web.page.TableDataInfo;

/**
 * 科目类别Controller
 * 
 * @author wimoor
 * @date 2026-04-03
 */
@RestController
@RequestMapping("/subjectType")
public class FinSubjectTypeController extends BaseController
{
    @Autowired
    private IFinSubjectTypeService finSubjectTypeService;

    /**
     * 查询科目类别列表
     */
    @GetMapping("/list")
    public TableDataInfo list(FinSubjectType finSubjectType)
    {
        startPage();
        List<FinSubjectType> list = finSubjectTypeService.selectFinSubjectTypeList(finSubjectType);
        return getDataTable(list);
    }

    /**
     * 导出科目类别列表
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, FinSubjectType finSubjectType)
    {
        List<FinSubjectType> list = finSubjectTypeService.selectFinSubjectTypeList(finSubjectType);
        ExcelUtil<FinSubjectType> util = new ExcelUtil<FinSubjectType>(FinSubjectType.class);
        util.exportExcel(response, list, "科目类别数据");
    }

    /**
     * 获取科目类别详细信息
     */
    @GetMapping(value = "/{id}")
    public Result getInfo(@PathVariable("id") Integer id)
    {
        return success(finSubjectTypeService.selectFinSubjectTypeById(id));
    }

    /**
     * 新增科目类别
     */
    @PostMapping
    public Result add(@RequestBody FinSubjectType finSubjectType)
    {
        return toResult(finSubjectTypeService.insertFinSubjectType(finSubjectType));
    }

    /**
     * 修改科目类别
     */
    @PutMapping
    public Result edit(@RequestBody FinSubjectType finSubjectType)
    {
        return toResult(finSubjectTypeService.updateFinSubjectType(finSubjectType));
    }

    /**
     * 删除科目类别
     */
	@DeleteMapping("/{ids}")
    public Result remove(@PathVariable Integer[] ids)
    {
        return toResult(finSubjectTypeService.deleteFinSubjectTypeByIds(ids));
    }

    /**
     * 根据科目类型获取科目类别列表
     */
    @GetMapping("/byCategoryType/{categoryType}")
    public Result listByCategoryType(@PathVariable("categoryType") String categoryType)
    {
        return success(finSubjectTypeService.selectFinSubjectTypeByCategoryType(categoryType));
    }
}
