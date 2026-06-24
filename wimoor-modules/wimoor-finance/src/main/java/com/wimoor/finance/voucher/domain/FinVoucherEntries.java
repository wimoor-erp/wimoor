package com.wimoor.finance.voucher.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wimoor.common.core.annotation.Excel;
import com.wimoor.common.core.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 凭证分录明细对象 fin_voucher_entries
 *
 * @author wimoor
 * @date 2025-11-04
 */
public class FinVoucherEntries extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 分录主键ID */
    private Long entryId;

    /** 租户ID */
    private String groupid;

    @Excel(name = "账套公司")
    private String groupName;

    /** 关联的凭证ID */
    @Excel(name = "关联的凭证ID")
    private Long voucherId;

    /** 分录序号，从1开始 */
    @Excel(name = "分录序号，从1开始")
    private Long entryNo;

    /** 会计科目ID */
    private String subjectId;

    @Excel(name = "会计科目")
    private String subjectName;
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

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createdTime;

    /** 辅助核算列表 */
    private List<FinVoucherEntriesAuxiliary> auxiliaryList;


    /** 凭证日期参数 */
    private List<String> voucherDateStr;
    private Date startDate;
    private Date endDate;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getOriginalAmount() {
        return originalAmount;
    }

    public void setOriginalAmount(BigDecimal originalAmount) {
        this.originalAmount = originalAmount;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
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

    public void setEntryId(Long entryId)
    {
        this.entryId = entryId;
    }

    public Long getEntryId()
    {
        return entryId;
    }

    public void setVoucherDateStr(List<String> voucherDateStr) {
        this.voucherDateStr = voucherDateStr;
    }

    public List<String> getVoucherDateStr() {
        return voucherDateStr;
    }

    public void setStartDate(Date startDate) {this.startDate = startDate;}
    public Date getStartDate() {return startDate;}

    public void setEndDate(Date endDate) {this.endDate = endDate;}
    public Date getEndDate() {return endDate;}

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setGroupid(String groupid)
    {
        this.groupid = groupid;
    }

    public String getGroupid()
    {
        return groupid;
    }

    public void setGroupName(String groupName) {this.groupName = groupName;}

    public String getGroupName() {return groupName;}

    public void setVoucherId(Long voucherId)
    {
        this.voucherId = voucherId;
    }

    public Long getVoucherId()
    {
        return voucherId;
    }

    public void setEntryNo(Long entryNo)
    {
        this.entryNo = entryNo;
    }

    public Long getEntryNo()
    {
        return entryNo;
    }

    public void setSubjectId(String subjectId)
    {
        this.subjectId = subjectId;
    }

    public String getSubjectId()
    {
        return subjectId;
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

    public void setCreatedTime(Date createdTime)
    {
        this.createdTime = createdTime;
    }

    public Date getCreatedTime()
    {
        return createdTime;
    }

    public List<FinVoucherEntriesAuxiliary> getAuxiliaryList() {
        return auxiliaryList;
    }

    public void setAuxiliaryList(List<FinVoucherEntriesAuxiliary> auxiliaryList) {
        this.auxiliaryList = auxiliaryList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("entryId", getEntryId())
                .append("groupid", getGroupid())
                .append("voucherId", getVoucherId())
                .append("entryNo", getEntryNo())
                .append("subjectId", getSubjectId())
                .append("debitAmount", getDebitAmount())
                .append("creditAmount", getCreditAmount())
                .append("summary", getSummary())
                .append("createdTime", getCreatedTime())
                .toString();
    }
}
