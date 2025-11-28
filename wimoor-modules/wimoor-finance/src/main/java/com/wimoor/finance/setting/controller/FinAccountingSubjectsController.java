package com.wimoor.finance.setting.controller;

import java.util.List;
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
import com.wimoor.finance.setting.domain.FinAccountingSubjects;
import com.wimoor.finance.setting.service.IFinAccountingSubjectsService;
import com.wimoor.common.core.web.controller.BaseController;
import com.wimoor.common.core.web.domain.Result;
import com.wimoor.common.core.utils.poi.ExcelUtil;

/**
 * 会计科目Controller
 * 
 * @author wimoor
 * @date 2025-11-05
 */
@RestController
@RequestMapping("/subjects")
public class FinAccountingSubjectsController extends BaseController
{
    @Autowired
    private IFinAccountingSubjectsService finAccountingSubjectsService;

    /**
     * 查询会计科目列表
     */
    @GetMapping("/list")
    public Result list(FinAccountingSubjects finAccountingSubjects)
    {
        List<FinAccountingSubjects> list = finAccountingSubjectsService.selectFinAccountingSubjectsList(finAccountingSubjects);
        return success(list);
    }

    /**
     * 导出会计科目列表
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, FinAccountingSubjects finAccountingSubjects)
    {
        List<FinAccountingSubjects> list = finAccountingSubjectsService.selectFinAccountingSubjectsList(finAccountingSubjects);
        ExcelUtil<FinAccountingSubjects> util = new ExcelUtil<FinAccountingSubjects>(FinAccountingSubjects.class);
        util.exportExcel(response, list, "会计科目数据");
    }

    /**
     * 获取会计科目详细信息
     */
    @GetMapping(value = "/{subjectId}")
    public Result getInfo(@PathVariable("subjectId") String subjectId)
    {
        return success(finAccountingSubjectsService.selectFinAccountingSubjectsBySubjectId(subjectId));
    }

    /**
     * 新增会计科目
     */
    @PostMapping
    public Result add(@RequestBody FinAccountingSubjects finAccountingSubjects)
    {
        return toResult(finAccountingSubjectsService.insertFinAccountingSubjects(finAccountingSubjects));
    }

    /**
     * 修改会计科目
     */
    @PutMapping
    public Result edit(@RequestBody FinAccountingSubjects finAccountingSubjects)
    {
        return toResult(finAccountingSubjectsService.updateFinAccountingSubjects(finAccountingSubjects));
    }

    /**
     * 删除会计科目
     */
	@DeleteMapping("/{subjectIds}")
    public Result remove(@PathVariable String[] subjectIds)
    {
        return toResult(finAccountingSubjectsService.deleteFinAccountingSubjectsBySubjectIds(subjectIds));
    }

     /**
      * 初始化会计科目
      *
      * @param groupId 集团ID
      * @return 结果
      */
    @GetMapping("/init/{groupId}")
    public Result init(@PathVariable("groupId") String groupId)
    {   finAccountingSubjectsService.initFinAccountingSubjects(groupId);
        return success();
    }
}
