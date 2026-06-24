package com.wimoor.finance.closing.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wimoor.common.core.annotation.Excel;
import com.wimoor.common.core.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 结账科目对应关系对象 fin_closing_template_amazon
 * 
 * @author wimoor
 * @date 2026-03-19
 */
public class FinClosingTemplateAmazon extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private String id;

    /** 模板ID */
    @Excel(name = "模板ID")
    private String closingTemplateId;

    /** 摘要 */
    @Excel(name = "摘要")
    private String summary;

    /** 科目ID */
    @Excel(name = "科目ID")
    private String subjectId;

    /** 模板字段 */
    @Excel(name = "模板字段")
    private String amountField;

    private String subjectName;

    private Integer direction;

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

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public Integer getDirection() {
        return direction;
    }

    public void setDirection(Integer direction) {
        this.direction = direction;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getId() 
    {
        return id;
    }

    public void setClosingTemplateId(String closingTemplateId) 
    {
        this.closingTemplateId = closingTemplateId;
    }

    public String getClosingTemplateId() 
    {
        return closingTemplateId;
    }

    public void setSummary(String summary) 
    {
        this.summary = summary;
    }

    public String getSummary() 
    {
        return summary;
    }

    public void setSubjectId(String subjectId) 
    {
        this.subjectId = subjectId;
    }

    public String getSubjectId() 
    {
        return subjectId;
    }

    public void setAmountField(String amountField) 
    {
        this.amountField = amountField;
    }

    public String getAmountField() 
    {
        return amountField;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("closingTemplateId", getClosingTemplateId())
            .append("summary", getSummary())
            .append("subjectId", getSubjectId())
            .append("amountField", getAmountField())
            .append("createdTime", getCreatedTime())
            .append("updatedTime", getUpdatedTime())
            .append("modifyBy", getModifyBy())
            .append("createBy", getCreateBy())
            .toString();
    }
}
