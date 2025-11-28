package com.wimoor.finance.code.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.wimoor.common.core.annotation.Excel;
import com.wimoor.common.core.web.domain.BaseEntity;

/**
 * 编码规则版本对象 fin_code_rule_version
 * 
 * @author wimoor
 * @date 2025-11-03
 */
public class FinCodeRuleVersion extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String groupid;

    /** 规则编码 */
    @Excel(name = "规则编码")
    private String ruleCode;

    /** 版本号 */
    @Excel(name = "版本号")
    private Long version;

    /** 规则模板 */
    @Excel(name = "规则模板")
    private String ruleTemplate;

    /** 编码结构 */
    @Excel(name = "编码结构")
    private String ruleStructure;

    /** 分隔符 */
    @Excel(name = "分隔符")
    private String separator;

    /** 编码前缀 */
    @Excel(name = "编码前缀")
    private String prefix;

    /** 编码后缀 */
    @Excel(name = "编码后缀")
    private String suffix;

    /** 版本描述 */
    @Excel(name = "版本描述")
    private String description;

    /** 变更原因 */
    @Excel(name = "变更原因")
    private String changeReason;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createdTime;

    /** 创建人 */
    @Excel(name = "创建人")
    private String createdBy;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setGroupid(String groupid)
    {
        this.groupid = groupid;
    }

    public String getGroupid()
    {
        return groupid;
    }

    public void setRuleCode(String ruleCode) 
    {
        this.ruleCode = ruleCode;
    }

    public String getRuleCode() 
    {
        return ruleCode;
    }

    public void setVersion(Long version) 
    {
        this.version = version;
    }

    public Long getVersion() 
    {
        return version;
    }

    public void setRuleTemplate(String ruleTemplate) 
    {
        this.ruleTemplate = ruleTemplate;
    }

    public String getRuleTemplate() 
    {
        return ruleTemplate;
    }

    public void setRuleStructure(String ruleStructure) 
    {
        this.ruleStructure = ruleStructure;
    }

    public String getRuleStructure() 
    {
        return ruleStructure;
    }

    public void setSeparator(String separator) 
    {
        this.separator = separator;
    }

    public String getSeparator() 
    {
        return separator;
    }

    public void setPrefix(String prefix) 
    {
        this.prefix = prefix;
    }

    public String getPrefix() 
    {
        return prefix;
    }

    public void setSuffix(String suffix) 
    {
        this.suffix = suffix;
    }

    public String getSuffix() 
    {
        return suffix;
    }

    public void setDescription(String description) 
    {
        this.description = description;
    }

    public String getDescription() 
    {
        return description;
    }

    public void setChangeReason(String changeReason) 
    {
        this.changeReason = changeReason;
    }

    public String getChangeReason() 
    {
        return changeReason;
    }

    public void setCreatedTime(Date createdTime) 
    {
        this.createdTime = createdTime;
    }

    public Date getCreatedTime() 
    {
        return createdTime;
    }

    public void setCreatedBy(String createdBy) 
    {
        this.createdBy = createdBy;
    }

    public String getCreatedBy() 
    {
        return createdBy;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("tenantId", getGroupid())
            .append("ruleCode", getRuleCode())
            .append("version", getVersion())
            .append("ruleTemplate", getRuleTemplate())
            .append("ruleStructure", getRuleStructure())
            .append("separator", getSeparator())
            .append("prefix", getPrefix())
            .append("suffix", getSuffix())
            .append("description", getDescription())
            .append("changeReason", getChangeReason())
            .append("createdTime", getCreatedTime())
            .append("createdBy", getCreatedBy())
            .toString();
    }
}
