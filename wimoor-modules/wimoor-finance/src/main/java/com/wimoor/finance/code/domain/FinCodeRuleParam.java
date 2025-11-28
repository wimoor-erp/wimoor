package com.wimoor.finance.code.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.wimoor.common.core.annotation.Excel;
import com.wimoor.common.core.web.domain.BaseEntity;

/**
 * 编码规则参数对象 fin_code_rule_param
 * 
 * @author wimoor
 * @date 2025-11-03
 */
public class FinCodeRuleParam extends BaseEntity
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

    /** 参数键 */
    @Excel(name = "参数键")
    private String paramKey;

    /** 参数值 */
    @Excel(name = "参数值")
    private String paramValue;

    /** 参数类型: STRING, NUMBER, DATE, BOOLEAN */
    @Excel(name = "参数类型: STRING, NUMBER, DATE, BOOLEAN")
    private String paramType;

    /** 参数顺序 */
    @Excel(name = "参数顺序")
    private Long paramOrder;

    /** 是否必填: 0-否, 1-是 */
    @Excel(name = "是否必填: 0-否, 1-是")
    private Long required;

    /** 参数描述 */
    @Excel(name = "参数描述")
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

    public void setParamKey(String paramKey) 
    {
        this.paramKey = paramKey;
    }

    public String getParamKey() 
    {
        return paramKey;
    }

    public void setParamValue(String paramValue) 
    {
        this.paramValue = paramValue;
    }

    public String getParamValue() 
    {
        return paramValue;
    }

    public void setParamType(String paramType) 
    {
        this.paramType = paramType;
    }

    public String getParamType() 
    {
        return paramType;
    }

    public void setParamOrder(Long paramOrder) 
    {
        this.paramOrder = paramOrder;
    }

    public Long getParamOrder() 
    {
        return paramOrder;
    }

    public void setRequired(Long required) 
    {
        this.required = required;
    }

    public Long getRequired() 
    {
        return required;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("groupid", getGroupid())
            .append("ruleCode", getRuleCode())
            .append("paramKey", getParamKey())
            .append("paramValue", getParamValue())
            .append("paramType", getParamType())
            .append("paramOrder", getParamOrder())
            .append("required", getRequired())
            .append("description", getDescription())
            .append("status", getStatus())
            .append("createdTime", getCreatedTime())
            .append("updatedTime", getUpdatedTime())
            .toString();
    }
}
