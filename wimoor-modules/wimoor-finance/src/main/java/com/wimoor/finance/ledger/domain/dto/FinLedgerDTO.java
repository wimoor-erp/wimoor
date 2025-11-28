package com.wimoor.finance.ledger.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wimoor.common.core.annotation.Excel;
import com.wimoor.common.core.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 明细账表对象 fin_detail_ledger
 * 
 * @author wimoor
 * @date 2025-11-04
 */
public class FinLedgerDTO extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 明细账记录ID */
    private Long detailId;

    /** 租户ID */
    @Excel(name = "租户ID")
    private String groupid;

    private String period;
    /** 会计科目ID */
    @Excel(name = "会计科目ID")
    private String subjectId;

    /** 凭证ID */
    @Excel(name = "凭证ID")
    private Long voucherId;

    /** 凭证分录ID */
    @Excel(name = "凭证分录ID")
    private Long entryId;

    /** 凭证日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "凭证日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date voucherDate;

    /** 凭证编号 */
    @Excel(name = "凭证编号")
    private String voucherNo;

    /** 摘要 */
    @Excel(name = "摘要")
    private String summary;

    /** 借方金额 */
    @Excel(name = "借方金额")
    private BigDecimal debitAmount;

    /** 贷方金额 */
    @Excel(name = "贷方金额")
    private BigDecimal creditAmount;

    /** 当前余额 */
    @Excel(name = "当前余额")
    private BigDecimal balance;

    /** 余额方向：1-借，2-贷 */
    @Excel(name = "余额方向：1-借，2-贷")
    private Integer balanceDirection;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createdTime;

    public void setDetailId(Long detailId) 
    {
        this.detailId = detailId;
    }

    public Long getDetailId() 
    {
        return detailId;
    }

    public void setGroupid(String groupid) 
    {
        this.groupid = groupid;
    }

    public String getGroupid() 
    {
        return groupid;
    }

    public void setSubjectId(String subjectId) 
    {
        this.subjectId = subjectId;
    }

    public String getSubjectId() 
    {
        return subjectId;
    }

    public void setVoucherId(Long voucherId) 
    {
        this.voucherId = voucherId;
    }

    public Long getVoucherId() 
    {
        return voucherId;
    }

    public void setEntryId(Long entryId) 
    {
        this.entryId = entryId;
    }

    public Long getEntryId() 
    {
        return entryId;
    }

    public void setVoucherDate(Date voucherDate) 
    {
        this.voucherDate = voucherDate;
    }

    public Date getVoucherDate() 
    {
        return voucherDate;
    }

    public void setVoucherNo(String voucherNo) 
    {
        this.voucherNo = voucherNo;
    }

    public String getVoucherNo() 
    {
        return voucherNo;
    }

    public void setSummary(String summary) 
    {
        this.summary = summary;
    }

    public String getSummary() 
    {
        return summary;
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

    public void setBalance(BigDecimal balance) 
    {
        this.balance = balance;
    }

    public BigDecimal getBalance() 
    {
        return balance;
    }

    public void setBalanceDirection(Integer balanceDirection)
    {
        this.balanceDirection = balanceDirection;
    }

    public Integer getBalanceDirection()
    {
        return balanceDirection;
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
            .append("detailId", getDetailId())
            .append("groupid", getGroupid())
            .append("subjectId", getSubjectId())
            .append("voucherId", getVoucherId())
            .append("entryId", getEntryId())
            .append("voucherDate", getVoucherDate())
            .append("voucherNo", getVoucherNo())
            .append("summary", getSummary())
            .append("debitAmount", getDebitAmount())
            .append("creditAmount", getCreditAmount())
            .append("balance", getBalance())
            .append("balanceDirection", getBalanceDirection())
            .append("createdTime", getCreatedTime())
            .toString();
    }
}
