package com.wimoor.finance.code.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.wimoor.common.core.annotation.Excel;
import com.wimoor.common.core.web.domain.BaseEntity;

/**
 * 编码规则关联对象 fin_code_rule_relation
 * 
 * @author wimoor
 * @date 2025-11-03
 */
public class FinCodeRuleRelation extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String groupid;

    /** 父规则编码 */
    @Excel(name = "父规则编码")
    private String parentRuleCode;

    /** 子规则编码 */
    @Excel(name = "子规则编码")
    private String childRuleCode;

    /** 关联类型: EXTENDS-继承, REFERENCES-引用, DEPENDS-依赖 */
    @Excel(name = "关联类型: EXTENDS-继承, REFERENCES-引用, DEPENDS-依赖")
    private String relationType;

    /** 条件表达式 */
    @Excel(name = "条件表达式")
    private String conditionExpression;

    /** 优先级 */
    @Excel(name = "优先级")
    private Long priority;

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

    public void setParentRuleCode(String parentRuleCode) 
    {
        this.parentRuleCode = parentRuleCode;
    }

    public String getParentRuleCode() 
    {
        return parentRuleCode;
    }

    public void setChildRuleCode(String childRuleCode) 
    {
        this.childRuleCode = childRuleCode;
    }

    public String getChildRuleCode() 
    {
        return childRuleCode;
    }

    public void setRelationType(String relationType) 
    {
        this.relationType = relationType;
    }

    public String getRelationType() 
    {
        return relationType;
    }

    public void setConditionExpression(String conditionExpression) 
    {
        this.conditionExpression = conditionExpression;
    }

    public String getConditionExpression() 
    {
        return conditionExpression;
    }

    public void setPriority(Long priority) 
    {
        this.priority = priority;
    }

    public Long getPriority() 
    {
        return priority;
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
            .append("parentRuleCode", getParentRuleCode())
            .append("childRuleCode", getChildRuleCode())
            .append("relationType", getRelationType())
            .append("conditionExpression", getConditionExpression())
            .append("priority", getPriority())
            .append("status", getStatus())
            .append("createdTime", getCreatedTime())
            .append("updatedTime", getUpdatedTime())
            .toString();
    }
}
