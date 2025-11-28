package com.wimoor.finance.report.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.wimoor.common.core.annotation.Excel;
import com.wimoor.common.core.web.domain.BaseEntity;

/**
 * 财务报表模板对象 fin_report_templates
 * 
 * @author wimoor
 * @date 2025-11-04
 */
public class FinReportTemplates extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 报表模板ID */
    private Long templateId;

    /** 租户ID */
    @Excel(name = "租户ID")
    private String groupid;

    /** 模板名称：资产负债表、利润表等 */
    @Excel(name = "模板名称：资产负债表、利润表等")
    private String templateName;

    /** 模板类型：1-资产负债表，2-利润表，3-现金流量表 */
    @Excel(name = "模板类型：1-资产负债表，2-利润表，3-现金流量表")
    private String templateType;

    /** 报表编码：BALANCE_SHEET、INCOME_STATEMENT等 */
    @Excel(name = "报表编码：BALANCE_SHEET、INCOME_STATEMENT等")
    private String templateCode;

    @Excel(name = "描述")
    private String description;
    /** 状态：0-停用，1-启用 */
    @Excel(name = "状态：0-停用，1-启用")
    private Integer status;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createdTime;

    public void setTemplateId(Long templateId) 
    {
        this.templateId = templateId;
    }

    public Long getTemplateId() 
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

    public void setTemplateName(String templateName) 
    {
        this.templateName = templateName;
    }

    public String getTemplateName() 
    {
        return templateName;
    }

    public void setTemplateType(String templateType)
    {
        this.templateType = templateType;
    }

    public String getTemplateType()
    {
        return templateType;
    }

    public void setTemplateCode(String templateCode)
    {
        this.templateCode = templateCode;
    }

    public String getTemplateCode()
    {
        return templateCode;
    }

    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public Integer getStatus()
    {
        return status;
    }

    public void setCreatedTime(Date createdTime) 
    {
        this.createdTime = createdTime;
    }

    public Date getCreatedTime() 
    {
        return createdTime;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getDescription()
    {
        return description;
    }
    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("templateId", getTemplateId())
            .append("groupid", getGroupid())
            .append("templateName", getTemplateName())
            .append("templateType", getTemplateType())
            .append("templateCode", getTemplateCode())
            .append("status", getStatus())
            .append("createdTime", getCreatedTime())
            .toString();
    }
}
