package com.wimoor.finance.ledger.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.wimoor.common.core.annotation.Excel;
import com.wimoor.common.core.web.domain.BaseEntity;

/**
 * 总账对象 fin_general_ledger
 * 
 * @author wimoor
 * @date 2025-11-04
 */
public class FinGeneralLedger extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 总账记录ID */
    private Long ledgerId;

    /** 租户ID */
    @Excel(name = "租户ID")
    private String groupid;

    /** 会计科目ID */
    @Excel(name = "会计科目ID")
    private Long subjectId;

    /** 会计期间，格式：YYYYMM */
    @Excel(name = "会计期间，格式：YYYYMM")
    private String period;

    /** 期初余额 */
    @Excel(name = "期初余额")
    private BigDecimal beginBalance;

    /** 期初余额方向：1-借，2-贷 */
    @Excel(name = "期初余额方向：1-借，2-贷")
    private Integer beginDirection;

    /** 本期借方合计 */
    @Excel(name = "本期借方合计")
    private BigDecimal debitTotal;

    /** 本期贷方合计 */
    @Excel(name = "本期贷方合计")
    private BigDecimal creditTotal;

    /** 期末余额 */
    @Excel(name = "期末余额")
    private BigDecimal endBalance;

    /** 期末余额方向：1-借，2-贷 */
    @Excel(name = "期末余额方向：1-借，2-贷")
    private Integer endDirection;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createdTime;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date updatedTime;

    public void setLedgerId(Long ledgerId) 
    {
        this.ledgerId = ledgerId;
    }

    public Long getLedgerId() 
    {
        return ledgerId;
    }

    public void setGroupid(String groupid) 
    {
        this.groupid = groupid;
    }

    public String getGroupid() 
    {
        return groupid;
    }

    public void setSubjectId(Long subjectId)
    {
        this.subjectId = subjectId;
    }

    public Long getSubjectId()
    {
        return subjectId;
    }

    public void setPeriod(String period) 
    {
        this.period = period;
    }

    public String getPeriod() 
    {
        return period;
    }

    public void setBeginBalance(BigDecimal beginBalance) 
    {
        this.beginBalance = beginBalance;
    }

    public BigDecimal getBeginBalance() 
    {
        return beginBalance;
    }

    public void setBeginDirection(Integer beginDirection)
    {
        this.beginDirection = beginDirection;
    }

    public Integer getBeginDirection()
    {
        return beginDirection;
    }

    public void setDebitTotal(BigDecimal debitTotal) 
    {
        this.debitTotal = debitTotal;
    }

    public BigDecimal getDebitTotal() 
    {
        return debitTotal;
    }

    public void setCreditTotal(BigDecimal creditTotal) 
    {
        this.creditTotal = creditTotal;
    }

    public BigDecimal getCreditTotal() 
    {
        return creditTotal;
    }

    public void setEndBalance(BigDecimal endBalance) 
    {
        this.endBalance = endBalance;
    }

    public BigDecimal getEndBalance() 
    {
        return endBalance;
    }

    public void setEndDirection(Integer endDirection)
    {
        this.endDirection = endDirection;
    }

    public Integer getEndDirection()
    {
        return endDirection;
    }

    public void setCreatedTime(Date createdTime) 
    {
        this.createdTime = createdTime;
    }

    public Date getCreatedTime() 
    {
        return createdTime;
    }

    public void setUpdatedTime(Date updatedTime) 
    {
        this.updatedTime = updatedTime;
    }

    public Date getUpdatedTime() 
    {
        return updatedTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("ledgerId", getLedgerId())
            .append("groupid", getGroupid())
            .append("subjectId", getSubjectId())
            .append("period", getPeriod())
            .append("beginBalance", getBeginBalance())
            .append("beginDirection", getBeginDirection())
            .append("debitTotal", getDebitTotal())
            .append("creditTotal", getCreditTotal())
            .append("endBalance", getEndBalance())
            .append("endDirection", getEndDirection())
            .append("createdTime", getCreatedTime())
            .append("updatedTime", getUpdatedTime())
            .toString();
    }
}
