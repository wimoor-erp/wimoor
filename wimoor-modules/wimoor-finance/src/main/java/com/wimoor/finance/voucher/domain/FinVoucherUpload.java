package com.wimoor.finance.voucher.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wimoor.common.core.annotation.Excel;
import com.wimoor.common.core.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 凭证分录明细，存储凭证的每一笔分录信息对象 fin_voucher_upload
 * 
 * @author wimoor
 * @date 2025-11-28
 */
public class FinVoucherUpload extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private String id;

    private Integer rowIndex;

    /** ID */
    private String shopid;

    /** 租户ID */
    @Excel(name = "租户名称")
    private String groupName;

    @Excel(name = "租户ID")
    private String groupid;

    /** 凭证类型 */
    @Excel(name = "凭证类型")
    private String voucherType;

    /** 凭证编码 */
    @Excel(name = "凭证编码")
    private String voucherNo;

    /** 凭证日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "凭证日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date voucherDate;

    @Excel(name = "会计科目编码")
    private String subjectCode;

    /** 会计科目ID */
    @Excel(name = "会计科目")
    private String subjectName;

    Integer entryNo;
    /** 借方金额 */
    @Excel(name = "借方金额")
    private BigDecimal debitAmount;

    /** 贷方金额 */
    @Excel(name = "贷方金额")
    private BigDecimal creditAmount;

    /** 摘要说明 */
    @Excel(name = "摘要说明")
    private String summary;

    @Excel(name = "币种")
    private String currency;

    @Excel(name = "原始金额")
    private BigDecimal originalAmount;

    @Excel(name = "汇率")
    private BigDecimal exchangeRate;

    @Excel(name = "数量")
    Integer quantity;

    @Excel(name = "价格")
    Integer unitPrice;

    /** 辅助核算类型：1-部门，2-员工，3-客户，4-供应商，5-项目 */
    @Excel(name = "辅助核算类型：1-部门，2-员工，3-客户，4-供应商，5-项目")
    private Long auxiliaryType;

    /** 辅助核算对象ID */
    @Excel(name = "辅助核算对象ID")
    private Long auxiliaryId;

    /** 提交人 */
    @Excel(name = "提交人")
    private String preparerBy;


    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createdTime;

    /** 关联的凭证ID */
    @Excel(name = "关联的凭证ID")
    private String voucherId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Integer status;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String statusLog;

    public BigDecimal getOriginalAmount() {
        return originalAmount;
    }

    public void setOriginalAmount(BigDecimal originalAmount) {
        this.originalAmount = originalAmount;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Integer unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Integer getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(Integer rowIndex) {
        this.rowIndex = rowIndex;
    }

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public Integer getEntryNo() {
        return entryNo;
    }

    public void setEntryNo(Integer entryNo) {
        this.entryNo = entryNo;
    }

    public String getShopid() {
        return shopid;
    }

    public void setShopid(String shopid) {
        this.shopid = shopid;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getId() 
    {
        return id;
    }


    public void setVoucherType(String voucherType) 
    {
        this.voucherType = voucherType;
    }

    public String getVoucherType() 
    {
        return voucherType;
    }

    public void setVoucherNo(String voucherNo) 
    {
        this.voucherNo = voucherNo;
    }

    public String getVoucherNo() 
    {
        return voucherNo;
    }

    public void setVoucherDate(Date voucherDate) 
    {
        this.voucherDate = voucherDate;
    }

    public Date getVoucherDate() 
    {
        return voucherDate;
    }


    public void setDebitAmount(BigDecimal debitAmount) 
    {
        this.debitAmount = debitAmount;
    }

    public BigDecimal getDebitAmount() 
    {
        return debitAmount;
    }

    public void setCreditAmount(BigDecimal creditAmount) 
    {
        this.creditAmount = creditAmount;
    }

    public BigDecimal getCreditAmount() 
    {
        return creditAmount;
    }

    public void setSummary(String summary) 
    {
        this.summary = summary;
    }

    public String getSummary() 
    {
        return summary;
    }

    public void setAuxiliaryType(Long auxiliaryType) 
    {
        this.auxiliaryType = auxiliaryType;
    }

    public Long getAuxiliaryType() 
    {
        return auxiliaryType;
    }

    public void setAuxiliaryId(Long auxiliaryId) 
    {
        this.auxiliaryId = auxiliaryId;
    }

    public Long getAuxiliaryId() 
    {
        return auxiliaryId;
    }

    public void setPreparerBy(String preparerBy) 
    {
        this.preparerBy = preparerBy;
    }

    public String getPreparerBy() 
    {
        return preparerBy;
    }

    public void setCreatedTime(Date createdTime) 
    {
        this.createdTime = createdTime;
    }

    public Date getCreatedTime() 
    {
        return createdTime;
    }

    public void setVoucherId(String voucherId) 
    {
        this.voucherId = voucherId;
    }

    public String getVoucherId() 
    {
        return voucherId;
    }

    public void setStatus(Integer isSuccess)
    {
        this.status = isSuccess;
    }

    public Integer getStatus()
    {
        return status;
    }

    public void setStatusLog(String statusLog) 
    {
        this.statusLog = statusLog;
    }

    public String getStatusLog() 
    {
        return statusLog;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("groupName", getGroupName())
            .append("voucherType", getVoucherType())
            .append("voucherNo", getVoucherNo())
            .append("voucherDate", getVoucherDate())
            .append("subjectName", getSubjectName())
            .append("debitAmount", getDebitAmount())
            .append("creditAmount", getCreditAmount())
            .append("summary", getSummary())
            .append("auxiliaryType", getAuxiliaryType())
            .append("auxiliaryId", getAuxiliaryId())
            .append("preparerBy", getPreparerBy())
            .append("createdTime", getCreatedTime())
            .append("voucherId", getVoucherId())
            .append("status", getStatus())
            .append("statusLog", getStatusLog())
            .toString();
    }
}
