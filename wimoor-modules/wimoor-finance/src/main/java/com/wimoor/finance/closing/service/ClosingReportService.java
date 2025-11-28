package com.wimoor.finance.closing.service;

import com.wimoor.finance.closing.domain.ClosingReport;
import com.wimoor.finance.report.domain.dto.ReportGenerateRequest;
import com.wimoor.finance.report.service.IFinReportTemplatesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ClosingReportService {
    @Autowired
    private IFinReportTemplatesService finReportTemplatesService;

    public ClosingReport generateClosingReport(String groupid, String period, String operator) {
        ClosingReport report = new ClosingReport();
        report.setGroupid(groupid);
        report.setPeriod(period);
        report.setOperator(operator);
        report.setClosingTime(LocalDateTime.now());

        // 动态生成三大报表（不存储）
        ReportGenerateRequest request = new ReportGenerateRequest();
        request.setGroupid(groupid);
        request.setPeriod(period);
        request.setTemplateCode("BALANCE_SHEET");
        report.setBalanceSheet(finReportTemplatesService.generateReport(request));
        ReportGenerateRequest request2 = new ReportGenerateRequest();
        request2.setGroupid(groupid);
        request2.setPeriod(period);
        request2.setTemplateCode("PROFIT_STATEMENT");
        report.setProfitStatement(finReportTemplatesService.generateReport(request2));
        ReportGenerateRequest request3 = new ReportGenerateRequest();
        request3.setGroupid(groupid);
        request3.setPeriod(period);
        request3.setTemplateCode("CASH_FLOW");
        report.setCashFlowStatement(finReportTemplatesService.generateReport(request3));

        // 结账验证结果
//        report.setValidationResults(validateClosing(groupid, period));
//
//        // 结账操作摘要
//        report.setClosingSummary(generateClosingSummary(groupid, period));

        return report; // 返回对象但不持久化，或者只存储元数据
    }

    // 可选的轻量级存储（只存摘要）
//    public void saveClosingReportSummary(ClosingReport report) {
//        ClosingReportSummary summary = new ClosingReportSummary();
//        summary.setGroupid(report.getGroupid());
//        summary.setPeriod(report.getPeriod());
//        summary.setClosingTime(report.getClosingTime());
//        summary.setOperator(report.getOperator());
//        summary.setValidationStatus(report.getValidationResults().getStatus());
//        summary.setTotalAssets(report.getBalanceSheet().getTotalAssets());
//        summary.setNetProfit(report.getProfitStatement().getNetProfit());
//
//
//    }
}
