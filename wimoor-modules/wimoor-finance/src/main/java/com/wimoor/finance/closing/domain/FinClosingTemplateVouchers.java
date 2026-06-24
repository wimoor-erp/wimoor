package com.wimoor.finance.closing.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wimoor.common.core.annotation.Excel;
import com.wimoor.common.core.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 结账模板凭证对象 fin_closing_template_vouchers
 * 
 * @author wimoor
 * @date 2026-03-20
 */
public class FinClosingTemplateVouchers extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private String id;

    /** 模板ID */
    @Excel(name = "模板ID")
    private String templateId;

    /** 账套ID */
    @Excel(name = "账套ID")
    private String groupid;

    /** 凭证ID */
    @Excel(name = "凭证ID")
    private String vourchesId;

    /** 凭证日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "凭证日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date voucherDate;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createdTime;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date updatedTime;

    /** 修改人 */
    @Excel(name = "修改人")
    private String modifyBy;

    /** 创建人 */
    @Excel(name = "创建人")
    private String createBy;

    /** 数据日志 */
    @Excel(name = "数据日志")
    private String datalog;




    public void setId(String id) 
    {
        this.id = id;
    }

    public String getId() 
    {
        return id;
    }

    public void setTemplateId(String templateId) 
    {
        this.templateId = templateId;
    }

    public String getTemplateId() 
    {
        return templateId;
    }

    public void setGroupid(String groupid) 
    {
        this.groupid = groupid;
    }

    public String getGroupid() 
    {
        return groupid;
    }

    public void setVourchesId(String vourchesId) 
    {
        this.vourchesId = vourchesId;
    }

    public String getVourchesId() 
    {
        return vourchesId;
    }

    public void setVoucherDate(Date voucherDate) 
    {
        this.voucherDate = voucherDate;
    }

    public Date getVoucherDate() 
    {
        return voucherDate;
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

    public void setModifyBy(String modifyBy) 
    {
        this.modifyBy = modifyBy;
    }

    public String getModifyBy() 
    {
        return modifyBy;
    }

    public void setCreateBy(String createBy) 
    {
        this.createBy = createBy;
    }

    public String getCreateBy() 
    {
        return createBy;
    }

    public void setDatalog(String datalog)
    {
        this.datalog = datalog;
    }

    public String getDatalog()
    {
        return datalog;
    }



    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("templateId", getTemplateId())
            .append("groupid", getGroupid())
            .append("vourchesId", getVourchesId())
            .append("voucherDate", getVoucherDate())
            .append("createdTime", getCreatedTime())
            .append("updatedTime", getUpdatedTime())
            .append("modifyBy", getModifyBy())
            .append("createBy", getCreateBy())
            .toString();
    }
}
