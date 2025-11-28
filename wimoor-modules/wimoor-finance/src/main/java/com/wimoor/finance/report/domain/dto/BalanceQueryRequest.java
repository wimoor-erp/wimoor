package com.wimoor.finance.report.domain.dto;

// BalanceQueryRequest.java

import java.util.List;

/**
 * 余额查询请求参数
 */
public class BalanceQueryRequest {

    /**
     * 科目编码列表
     */
    private List<String> subjectCodes;

    /**
     * 科目编码 (支持模糊查询)
     */
    private String subjectCode;

    /**
     * 科目名称 (支持模糊查询)
     */
    private String subjectName;

    /**
     * 开始期间 (格式: yyyy-MM 或 yyyy-MM-dd)
     */
    private String startPeriod;

    /**
     * 结束期间 (格式: yyyy-MM 或 yyyy-MM-dd)
     */
    private String endPeriod;

    /**
     * 余额类型 (BEGIN_BALANCE:期初余额, END_BALANCE:期末余额, DEBIT_TOTAL:借方总金额, CREDIT_TOTAL:贷方总金额)
     */
    private String amountType = "END_BALANCE";

    /**
     * 科目级别
     */
    private Integer subjectLevel;

    /**
     * 科目类型 (1:资产, 2:负债, 3:权益, 4:成本, 5:损益)
     */
    private Integer subjectType;

    /**
     * 科目方向 (1:借方, -1:贷方)
     */
    private Integer direction;

    /**
     * 公司代码
     */
    private String groupid;

    /**
     * 是否只查询末级科目
     */
    private Boolean leafOnly = false;

    /**
     * 是否只查询余额不为零的科目
     */
    private Boolean nonZeroBalanceOnly = false;

    /**
     * 是否包含禁用科目
     */
    private Boolean includeDisabled = false;

    /**
     * 分页参数 - 页码
     */
    private Integer pageNum = 1;

    /**
     * 分页参数 - 每页大小
     */
    private Integer pageSize = 20;

    /**
     * 排序字段
     */
    private String sortField = "subjectCode";

    /**
     * 排序方向 (ASC, DESC)
     */
    private String sortOrder = "ASC";

    // 构造方法
    public BalanceQueryRequest() {
    }

    /**
     * 获取余额类型
     */
    public String getAmountType() {
        return amountType;
    }

    /**
     * 设置余额类型
     */
    public void setAmountType(String amountType) {
        this.amountType = amountType;
    }

    // Getter 和 Setter 方法
    public List<String> getSubjectCodes() {
        return subjectCodes;
    }

    public void setSubjectCodes(List<String> subjectCodes) {
        this.subjectCodes = subjectCodes;
    }

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

    public Integer getDirection() {
        return direction;
    }

    public void setDirection(Integer direction) {
        this.direction = direction;
    }
    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
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

    public Boolean getIncludeDisabled() {
        return includeDisabled;
    }

    public void setIncludeDisabled(Boolean includeDisabled) {
        this.includeDisabled = includeDisabled;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getSortField() {
        return sortField;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    // 业务方法

    /**
     * 获取偏移量 (用于分页查询)
     */
    public Integer getOffset() {
        return (pageNum - 1) * pageSize;
    }

    /**
     * 验证查询参数有效性
     */
    public boolean isValid() {
        if (startPeriod != null && endPeriod != null) {
            return startPeriod.compareTo(endPeriod) <= 0;
        }
        return true;
    }

    /**
     * 检查是否按科目编码查询
     */
    public boolean hasSubjectCodeCondition() {
        return subjectCode != null && !subjectCode.trim().isEmpty();
    }

    /**
     * 检查是否按科目名称查询
     */
    public boolean hasSubjectNameCondition() {
        return subjectName != null && !subjectName.trim().isEmpty();
    }

    /**
     * 检查是否按期间范围查询
     */
    public boolean hasPeriodRange() {
        return startPeriod != null && endPeriod != null;
    }

    /**
     * 检查是否包含科目编码列表查询
     */
    public boolean hasSubjectCodes() {
        return subjectCodes != null && !subjectCodes.isEmpty();
    }

    @Override
    public String toString() {
        return "BalanceQueryRequest{" +
                "subjectCode='" + subjectCode + '\'' +
                ", subjectName='" + subjectName + '\'' +
                ", startPeriod='" + startPeriod + '\'' +
                ", endPeriod='" + endPeriod + '\'' +
                ", subjectType=" + subjectType +
                ", groupid='" + groupid + '\'' +
                ", pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                '}';
    }
}