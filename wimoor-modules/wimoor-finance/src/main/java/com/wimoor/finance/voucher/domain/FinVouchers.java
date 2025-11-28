package com.wimoor.finance.voucher.domain;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.wimoor.common.core.annotation.Excel;
import com.wimoor.common.core.web.domain.BaseEntity;

/**
 * 记账凭证对象 fin_vouchers
 * 
 * @author wimoor
 * @date 2025-11-04
 */
public class FinVouchers extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 凭证主键ID */
    private Long voucherId;

    /** 租户ID */
    @Excel(name = "租户ID")
    private String groupid;

    /** 凭证类型：1-普通凭证，2-调整凭证 */
    @Excel(name = "凭证类型")
    private String voucherType;

    /** 凭证编号，如记-001 */
    @Excel(name = "凭证编号，如记-001")
    private String voucherNo;

    /** 凭证日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "凭证日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date voucherDate;

    /** 附件数量 */
    @Excel(name = "附件数量")
    private Integer attachmentCount;

    /** 凭证总金额 */
    @Excel(name = "凭证总金额")
    private BigDecimal totalAmount;

    /** 凭证状态：1-草稿，2-已审核，3-已过账，4-已作废 */
    @Excel(name = "凭证状态：1-草稿，2-已审核，3-已过账，4-已作废")
    private Integer voucherStatus;

    /** 制单人用户ID */
    @Excel(name = "制单人用户ID")
    private String preparerBy;

    private String reason;

    /** 审核人用户ID */
    @Excel(name = "审核人用户ID")
    private String auditorBy;

    /** 过账时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "过账时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date postTime;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createdTime;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date updatedTime;

    private List<FinVoucherEntries> entries;

    private List<FinVourchesFile> files;

    public List<FinVourchesFile> getFiles() {
        return files;
    }

    public void setFiles(List<FinVourchesFile> files) {
        this.files = files;
    }

    public String getVoucherType() {
        return voucherType;
    }

    public void setVoucherType(String voucherType) {
        this.voucherType = voucherType;
    }

    public void setVoucherId(Long voucherId)
    {
        this.voucherId = voucherId;
    }

    public Long getVoucherId()
    {
        return voucherId;
    }

    public void setEntries(List<FinVoucherEntries> entries)
    {
        this.entries = entries;
    }

    public List<FinVoucherEntries> getEntries()
    {
        return entries;
    }

    public void setGroupid(String groupid)
    {
        this.groupid = groupid;
    }

    public String getGroupid()
    {
        return groupid;
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

    public void setAttachmentCount(Integer attachmentCount)
    {
        this.attachmentCount = attachmentCount;
    }

    public Integer getAttachmentCount()
    {
        return attachmentCount;
    }

    public void setTotalAmount(BigDecimal totalAmount) 
    {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getTotalAmount() 
    {
        return totalAmount;
    }

    public void setVoucherStatus(Integer voucherStatus)
    {
        this.voucherStatus = voucherStatus;
    }

    public Integer getVoucherStatus()
    {
        return voucherStatus;
    }

    public void setPreparerBy(String preparerBy)
    {
        this.preparerBy = preparerBy;
    }

    public String getPreparerBy()
    {
        return preparerBy;
    }

    public void setAuditorBy(String auditorBy)
    {
        this.auditorBy = auditorBy;
    }

    public String getAuditorBy()
    {
        return auditorBy;
    }

    public void setPostTime(Date postTime) 
    {
        this.postTime = postTime;
    }

    public Date getPostTime() 
    {
        return postTime;
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
            .append("voucherId", getVoucherId())
            .append("tenantId", getGroupid())
            .append("voucherNo", getVoucherNo())
            .append("voucherDate", getVoucherDate())
            .append("attachmentCount", getAttachmentCount())
            .append("totalAmount", getTotalAmount())
            .append("voucherStatus", getVoucherStatus())
            .append("preparerBy", getPreparerBy())
            .append("auditorBy", getAuditorBy())
            .append("postTime", getPostTime())
            .append("createdTime", getCreatedTime())
            .append("updatedTime", getUpdatedTime())
            .toString();
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
