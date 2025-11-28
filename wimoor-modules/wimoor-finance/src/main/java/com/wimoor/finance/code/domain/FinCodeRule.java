package com.wimoor.finance.code.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.wimoor.common.core.annotation.Excel;
import com.wimoor.common.core.web.domain.BaseEntity;

/**
 * 编码规则对象 fin_code_rule
 * 
 * @author wimoor
 * @date 2025-11-03
 */
public class FinCodeRule extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String groupid;

    /** 规则编码，唯一标识 */
    @Excel(name = "规则编码，唯一标识")
    private String ruleCode;

    /** 规则名称 */
    @Excel(name = "规则名称")
    private String ruleName;

    /** 规则类型: SUBJECT-科目, VOUCHER-凭证, AUXILIARY-辅助核算, ASSET-固定资产, REPORT-报表 */
    @Excel(name = "规则类型: SUBJECT-科目, VOUCHER-凭证, AUXILIARY-辅助核算, ASSET-固定资产, REPORT-报表")
    private String ruleType;

    /** 规则模板，JSON格式存储复杂规则 */
    @Excel(name = "规则模板，JSON格式存储复杂规则")
    private String ruleTemplate;

    /** 编码结构，如: 4-2-2, YYYYMM+SEQ, TYPE+YYYY+SEQ */
    @Excel(name = "编码结构，如: 4-2-2, YYYYMM+SEQ, TYPE+YYYY+SEQ")
    private String ruleStructure;

    /** 分隔符，如: . - _ 等 */
    @Excel(name = "分隔符，如: . - _ 等")
    private String separator;

    /** 编码前缀 */
    @Excel(name = "编码前缀")
    private String prefix;

    /** 编码后缀 */
    @Excel(name = "编码后缀")
    private String suffix;

    /** 是否自动递增: 0-否, 1-是 */
    @Excel(name = "是否自动递增: 0-否, 1-是")
    private Long autoIncrement;

    /** 当前序列值 */
    @Excel(name = "当前序列值")
    private Long currentValue;

    /** 最小长度，0表示不限制 */
    @Excel(name = "最小长度，0表示不限制")
    private Long minLength;

    /** 最大长度，0表示不限制 */
    @Excel(name = "最大长度，0表示不限制")
    private Long maxLength;

    /** 重置频率: NEVER-从不, DAILY-每日, MONTHLY-每月, YEARLY-每年 */
    @Excel(name = "重置频率: NEVER-从不, DAILY-每日, MONTHLY-每月, YEARLY-每年")
    private String resetFrequency;

    /** 最后重置时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "最后重置时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date lastResetTime;

    /** 规则描述 */
    @Excel(name = "规则描述")
    private String description;

    /** 状态: 0-停用, 1-启用 */
    @Excel(name = "状态: 0-停用, 1-启用")
    private Long status;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createdTime;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date updatedTime;

    /** 创建人 */
    @Excel(name = "创建人")
    private String createdBy;

    /** 更新人 */
    @Excel(name = "更新人")
    private String updatedBy;

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

    public void setRuleName(String ruleName) 
    {
        this.ruleName = ruleName;
    }

    public String getRuleName() 
    {
        return ruleName;
    }

    public void setRuleType(String ruleType) 
    {
        this.ruleType = ruleType;
    }

    public String getRuleType() 
    {
        return ruleType;
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

    public void setAutoIncrement(Long autoIncrement) 
    {
        this.autoIncrement = autoIncrement;
    }

    public boolean getAutoIncrement()
    {
        return autoIncrement != 0;
    }

    public void setCurrentValue(Long currentValue) 
    {
        this.currentValue = currentValue;
    }

    public Long getCurrentValue() 
    {
        return currentValue;
    }

    public void setMinLength(Long minLength) 
    {
        this.minLength = minLength;
    }

    public Long getMinLength() 
    {
        return minLength;
    }

    public void setMaxLength(Long maxLength) 
    {
        this.maxLength = maxLength;
    }

    public Long getMaxLength() 
    {
        return maxLength;
    }

    public void setResetFrequency(String resetFrequency) 
    {
        this.resetFrequency = resetFrequency;
    }

    public String getResetFrequency() 
    {
        return resetFrequency;
    }

    public void setLastResetTime(Date lastResetTime) 
    {
        this.lastResetTime = lastResetTime;
    }

    public Date getLastResetTime() 
    {
        return lastResetTime;
    }

    public void setDescription(String description) 
    {
        this.description = description;
    }

    public String getDescription() 
    {
        return description;
    }

    public void setStatus(Long status) 
    {
        this.status = status;
    }

    public Long getStatus() 
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

    public void setUpdatedTime(Date updatedTime) 
    {
        this.updatedTime = updatedTime;
    }

    public Date getUpdatedTime() 
    {
        return updatedTime;
    }

    public void setCreatedBy(String createdBy) 
    {
        this.createdBy = createdBy;
    }

    public String getCreatedBy() 
    {
        return createdBy;
    }

    public void setUpdatedBy(String updatedBy) 
    {
        this.updatedBy = updatedBy;
    }

    public String getUpdatedBy() 
    {
        return updatedBy;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("tenantId", getGroupid())
            .append("ruleCode", getRuleCode())
            .append("ruleName", getRuleName())
            .append("ruleType", getRuleType())
            .append("ruleTemplate", getRuleTemplate())
            .append("ruleStructure", getRuleStructure())
            .append("separator", getSeparator())
            .append("prefix", getPrefix())
            .append("suffix", getSuffix())
            .append("autoIncrement", getAutoIncrement())
            .append("currentValue", getCurrentValue())
            .append("minLength", getMinLength())
            .append("maxLength", getMaxLength())
            .append("resetFrequency", getResetFrequency())
            .append("lastResetTime", getLastResetTime())
            .append("description", getDescription())
            .append("status", getStatus())
            .append("createdTime", getCreatedTime())
            .append("updatedTime", getUpdatedTime())
            .append("createdBy", getCreatedBy())
            .append("updatedBy", getUpdatedBy())
            .toString();
    }
}
