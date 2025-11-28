package com.wimoor.finance.report.domain.dto;


import java.util.List;
import java.util.Map;

public class ReportGenerateResponse {
    private boolean success;
    private String message;
    private String templateCode;
    private String templateName;
    private String period;
    private String reportDate;
    private List<ReportItemValueDTO> items;
    private Map<String, Object> financialRatios;
    private Map<String, Object> chartData;
    private Map<String, Object> summary;

    // Getters and Setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getTemplateCode() { return templateCode; }
    public void setTemplateCode(String templateCode) { this.templateCode = templateCode; }

    public String getTemplateName() { return templateName; }
    public void setTemplateName(String templateName) { this.templateName = templateName; }

    public String getPeriod() { return period; }
    public void setPeriod(String period) { this.period = period; }

    public String getReportDate() { return reportDate; }
    public void setReportDate(String reportDate) { this.reportDate = reportDate; }

    public List<ReportItemValueDTO> getItems() { return items; }
    public void setItems(List<ReportItemValueDTO> items) { this.items = items; }

    public Map<String, Object> getFinancialRatios() { return financialRatios; }
    public void setFinancialRatios(Map<String, Object> financialRatios) { this.financialRatios = financialRatios; }

    public Map<String, Object> getChartData() { return chartData; }
    public void setChartData(Map<String, Object> chartData) { this.chartData = chartData; }

    public Map<String, Object> getSummary() { return summary; }
    public void setSummary(Map<String, Object> summary) { this.summary = summary; }
}