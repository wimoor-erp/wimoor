package com.wimoor.finance.voucher.domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.wimoor.common.core.annotation.Excel;
import com.wimoor.common.core.web.domain.BaseEntity;

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
    @Excel(name = "租户ID")
    private String groupid;

    /** 关联的凭证ID */
    @Excel(name = "关联的凭证ID")
    private Long voucherId;

    /** 分录序号，从1开始 */
    @Excel(name = "分录序号，从1开始")
    private Long entryNo;

    /** 会计科目ID */
    @Excel(name = "会计科目ID")
    private String subjectId;

    /** 借方金额 */
    @Excel(name = "借方金额")
    private BigDecimal debitAmount;

    /** 贷方金额 */
    @Excel(name = "贷方金额")
    private BigDecimal creditAmount;

    /** 摘要说明 */
    @Excel(name = "摘要说明")
    private String summary;

    /** 辅助核算类型：1-部门，2-员工，3-客户，4-供应商，5-项目 */
    @Excel(name = "辅助核算类型：1-部门，2-员工，3-客户，4-供应商，5-项目")
    private Long auxiliaryType;

    /** 辅助核算对象ID */
    @Excel(name = "辅助核算对象ID")
    private Long auxiliaryId;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createdTime;


    private String subjectName;

    /** 凭证日期参数 */
    private List<String> voucherDateStr;
    private Date startDate;
    private Date endDate;


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

    public void setCreatedTime(Date createdTime)
    {
        this.createdTime = createdTime;
    }

    public Date getCreatedTime()
    {
        return createdTime;
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
                .append("auxiliaryType", getAuxiliaryType())
                .append("auxiliaryId", getAuxiliaryId())
                .append("createdTime", getCreatedTime())
                .toString();
    }
}
