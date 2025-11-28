package com.wimoor.finance.voucher.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wimoor.common.core.annotation.Excel;
import com.wimoor.common.core.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;
import java.util.List;

/**
 * 凭证分录明细对象 fin_voucher_entries
 * 
 * @author wimoor
 * @date 2025-11-04
 */

public class FinVoucherDTO extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 租户ID */
    @Excel(name = "租户ID")
    private String groupid;

    /** 关联的凭证ID */
    @Excel(name = "关联的凭证ID")
    private Long voucherId;

    /** 分录序号，从1开始 */
    @Excel(name = "分录序号，从1开始")
    private Long entryNo;

    /** 凭证号 */
    @Excel(name = "凭证号")
    private String voucherNo;

    /** 会计科目ID */
    @Excel(name = "会计科目ID")
    private String subjectId;

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

    /** 凭证日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "凭证日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date voucherDate;

    private String subjectName;

    /** 凭证日期参数 */
    private Date startDate;
    private Date endDate;

    /** 制单人用户ID */
    @Excel(name = "制单人用户ID")
    private String preparerBy;

    /** 审核人用户ID */
    @Excel(name = "审核人用户ID")
    private String auditorBy;

    public String getAuditorBy() {
        return auditorBy;
    }

    public void setAuditorBy(String auditorBy) {
        this.auditorBy = auditorBy;
    }

    public String getPreparerBy() {
        return preparerBy;
    }

    public void setPreparerBy(String preparerBy) {
        this.preparerBy = preparerBy;
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    private List<String> voucherDateStr;

    public List<String> getVoucherDateStr() {
        return voucherDateStr;
    }

    public void setVoucherDateStr(List<String> voucherDateStr) {
        this.voucherDateStr = voucherDateStr;
    }

    public void setStartDate(Date startDate) {this.startDate = startDate;}
    public Date getStartDate() {return startDate;}

    public void setEndDate(Date endDate) {this.endDate = endDate;}
    public Date getEndDate() {return endDate;}

    public void setVoucherDate(Date voucherDate)
    {
        this.voucherDate = voucherDate;
    }

    public Date getVoucherDate()
    {
        return voucherDate;
    }

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

    public void setVoucherNo(String voucherNo)
    {
        this.voucherNo = voucherNo;
    }

    public String getVoucherNo()
    {
        return voucherNo;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("tenantId", getGroupid())
            .append("voucherId", getVoucherId())
            .append("entryNo", getEntryNo())
            .append("subjectId", getSubjectId())
            .append("summary", getSummary())
            .append("auxiliaryType", getAuxiliaryType())
            .append("auxiliaryId", getAuxiliaryId())
            .append("createdTime", getCreatedTime())
            .toString();
    }
}
