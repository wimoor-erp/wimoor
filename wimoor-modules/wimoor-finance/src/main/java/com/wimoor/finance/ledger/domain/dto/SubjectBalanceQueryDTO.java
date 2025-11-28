package com.wimoor.finance.ledger.domain.dto;

/**
 * 科目余额查询参数
 */
public class SubjectBalanceQueryDTO {

    /**
     * 科目编码 (支持模糊查询)
     */
    private String subjectCode;

    /**
     * 科目名称 (支持模糊查询)
     */
    private String subjectName;

    /**
     * 开始期间 (yyyy-MM)
     */
    private String startPeriod;

    /**
     * 结束期间 (yyyy-MM)
     */
    private String endPeriod;

    /**
     * 科目级别
     */
    private Integer subjectLevel;

    /**
     * 科目类型
     */
    private Integer subjectType;

    /**
     * 公司代码
     */
    private String companyCode;

    /**
     * 是否只查询末级科目
     */
    private Boolean leafOnly = false;

    /**
     * 余额不为零的科目
     */
    private Boolean nonZeroBalanceOnly = false;

    // Getter 和 Setter 方法
    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getStartPeriod() {
        return startPeriod;
    }

    public void setStartPeriod(String startPeriod) {
        this.startPeriod = startPeriod;
    }

    public String getEndPeriod() {
        return endPeriod;
    }

    public void setEndPeriod(String endPeriod) {
        this.endPeriod = endPeriod;
    }

    public Integer getSubjectLevel() {
        return subjectLevel;
    }

    public void setSubjectLevel(Integer subjectLevel) {
        this.subjectLevel = subjectLevel;
    }

    public Integer getSubjectType() {
        return subjectType;
    }

    public void setSubjectType(Integer subjectType) {
        this.subjectType = subjectType;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public Boolean getLeafOnly() {
        return leafOnly;
    }

    public void setLeafOnly(Boolean leafOnly) {
        this.leafOnly = leafOnly;
    }

    public Boolean getNonZeroBalanceOnly() {
        return nonZeroBalanceOnly;
    }

    public void setNonZeroBalanceOnly(Boolean nonZeroBalanceOnly) {
        this.nonZeroBalanceOnly = nonZeroBalanceOnly;
    }
}