package com.wimoor.finance.report.controller;

import com.wimoor.common.core.web.controller.BaseController;
import com.wimoor.common.result.Result;
import com.wimoor.finance.report.domain.dto.ReportGenerateRequest;
import com.wimoor.finance.report.domain.dto.ReportGenerateResponse;
import com.wimoor.finance.report.service.IFinReportTemplatesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/report")
@CrossOrigin(origins = "*")
public class ReportController extends BaseController{

    @Autowired
    private IFinReportTemplatesService finReportTemplatesService;

    @PostMapping("/generate")
    public Result<ReportGenerateResponse> generateReport(@Valid @RequestBody ReportGenerateRequest request) {
        return Result.success(finReportTemplatesService.generateReport(request));
    }

    @GetMapping("/templates")
    public Result<Map<String, Object>> getTemplates(@RequestParam String groupid) {
        return Result.success(finReportTemplatesService.getReportTemplates(groupid));
    }

    @PostMapping("/validate")
    public Result<Map<String, Object>> validateReport(@RequestParam String groupid,
                                              @RequestParam String templateCode,
                                              @RequestParam String period) {
        return Result.success(finReportTemplatesService.validateReportData(groupid, templateCode, period));
    }

    @PostMapping("/cache/clear")
    public Result<Map<String, Object>> clearCache(@RequestParam String groupid,
                                          @RequestParam(required = false) String templateCode,
                                          @RequestParam(required = false) String period) {
        finReportTemplatesService.clearReportCache(groupid, templateCode, period);

        return Result.success(Map.of(
                "success", true,
                "message", "缓存清除成功"
        ));
    }
}