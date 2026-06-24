package com.wimoor.finance.ledger.controller;

import com.wimoor.common.core.web.controller.BaseController;
import com.wimoor.common.result.Result;
import com.wimoor.finance.ledger.domain.dto.SubjectBalanceQueryDTO;
import com.wimoor.finance.ledger.domain.dto.SubjectBalanceTableDTO;
import com.wimoor.finance.ledger.service.ISubjectBalanceReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 科目余额表控制器
 */
@RestController
@RequestMapping("/api/report/subject-balance")
public class SubjectBalanceReportController extends BaseController {

    @Autowired
    private ISubjectBalanceReportService subjectBalanceReportService;

    /**
     * 查询科目余额表（树形结构）
     *
     * @param queryDTO 查询条件
     * @return 树形结构科目余额
     */
    @PostMapping("/tree")
    public Result<List<SubjectBalanceTableDTO>> queryTree(@RequestBody SubjectBalanceQueryDTO queryDTO) {
        return Result.success(subjectBalanceReportService.querySubjectBalanceTree(queryDTO));
    }

    /**
     * 获取科目余额汇总信息
     *
     * @param groupid 集团ID
     * @param period  期间
     * @return 汇总统计
     */
    @GetMapping("/summary")
    public Result<Map<String, Object>> getSummary(
            @RequestParam String groupid,
            @RequestParam String period) {
        return Result.success(subjectBalanceReportService.getBalanceSummary(groupid, period));
    }

    /**
     * 导出科目余额表数据
     *
     * @param queryDTO 查询条件
     * @return Excel数据
     */
    @PostMapping("/export")
    public Result<List<Map<String, Object>>> export(@RequestBody SubjectBalanceQueryDTO queryDTO) {
        return Result.success(subjectBalanceReportService.exportSubjectBalance(queryDTO));
    }

}