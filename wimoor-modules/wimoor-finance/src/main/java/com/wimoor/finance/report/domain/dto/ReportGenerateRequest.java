package com.wimoor.finance.report.domain.dto;


public class ReportGenerateRequest {

    private String templateCode;

    private String period;

    private String comparePeriod;

    private String groupid;

    private Integer amountUnit = 1; // 1-元, 1000-千元, 10000-万元

    private Boolean includeComparison = false;

    private Boolean includeChartData = true;

    // Getters and Setters
    public String getTemplateCode() { return templateCode; }
    public void setTemplateCode(String templateCode) { this.templateCode = templateCode; }

    public String getPeriod() { return period; }
    public void setPeriod(String period) { this.period = period; }

    public String getComparePeriod() { return comparePeriod; }
    public void setComparePeriod(String comparePeriod) { this.comparePeriod = comparePeriod; }

    public String getGroupid() { return groupid; }
    public void setGroupid(String groupid) { this.groupid = groupid; }

    public Integer getAmountUnit() { return amountUnit; }
    public void setAmountUnit(Integer amountUnit) { this.amountUnit = amountUnit; }

    public Boolean getIncludeComparison() { return includeComparison; }
    public void setIncludeComparison(Boolean includeComparison) { this.includeComparison = includeComparison; }

    public Boolean getIncludeChartData() { return includeChartData; }
    public void setIncludeChartData(Boolean includeChartData) { this.includeChartData = includeChartData; }
}