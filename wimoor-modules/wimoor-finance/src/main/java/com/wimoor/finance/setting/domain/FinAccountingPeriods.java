package com.wimoor.finance.setting.domain;

import java.time.LocalDate;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.wimoor.common.core.annotation.Excel;
import com.wimoor.common.core.web.domain.BaseEntity;

/**
 * 会计期间管理对象 fin_accounting_periods
 * 
 * @author wimoor
 * @date 2025-11-04
 */
public class FinAccountingPeriods extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    public   FinAccountingPeriods(){

    }
    public   FinAccountingPeriods(String accountingPeriod, String periodName,
                                  Date startDate, Date endDate ){
        this.periodCode = accountingPeriod;
        this.periodName = periodName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.periodStatus=1;
        this.isCurrent=0;
        this.createdTime=new Date();
    }
    /** 会计期间ID */
    private Long periodId;

    /** 租户ID */
    private String groupid;

    /** 期间编码，格式：YYYYMM */
    @Excel(name = "期间编码，格式：YYYYMM")
    private String periodCode;

    /** 期间名称，如：2024年1月 */
    @Excel(name = "期间名称，如：2024年1月")
    private String periodName;


    /** 期间开始日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "期间开始日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date startDate;

    /** 期间结束日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "期间结束日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date endDate;

    /** 期间状态：1-未开启，2-已开启，3-已关闭 */
    @Excel(name = "期间状态：1-未开启，2-已开启，3-已关闭")
    private Integer periodStatus;

    /** 是否当前期间：0-否，1-是 */
    @Excel(name = "是否当前期间：0-否，1-是")
    private Integer isCurrent;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date closeTime;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createdTime;

    private String remark;

    @Override
    public String getRemark() {
        return remark;
    }

    @Override
    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(Date closeTime) {
        this.closeTime = closeTime;
    }

    public void setPeriodId(Long periodId)
    {
        this.periodId = periodId;
    }

    public Long getPeriodId() 
    {
        return periodId;
    }

    public void setGroupid(String groupid)
    {
        this.groupid = groupid;
    }

    public String getGroupid()
    {
        return groupid;
    }

    public void setPeriodCode(String periodCode) 
    {
        this.periodCode = periodCode;
    }

    public String getPeriodCode() 
    {
        return periodCode;
    }

    public void setPeriodName(String periodName) 
    {
        this.periodName = periodName;
    }

    public String getPeriodName() 
    {
        return periodName;
    }

    public void setStartDate(Date startDate) 
    {
        this.startDate = startDate;
    }

    public Date getStartDate() 
    {
        return startDate;
    }

    public void setEndDate(Date endDate) 
    {
        this.endDate = endDate;
    }

    public Date getEndDate() 
    {
        return endDate;
    }

    public void setPeriodStatus(Integer periodStatus)
    {
        this.periodStatus = periodStatus;
    }

    public Integer getPeriodStatus()
    {
        return periodStatus;
    }

    public void setIsCurrent(Integer isCurrent)
    {
        this.isCurrent = isCurrent;
    }

    public Integer getIsCurrent()
    {
        return isCurrent;
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
            .append("periodId", getPeriodId())
            .append("tenantId", getGroupid())
            .append("periodCode", getPeriodCode())
            .append("periodName", getPeriodName())
            .append("startDate", getStartDate())
            .append("endDate", getEndDate())
            .append("periodStatus", getPeriodStatus())
            .append("isCurrent", getIsCurrent())
            .append("createdTime", getCreatedTime())
            .toString();
    }
}
