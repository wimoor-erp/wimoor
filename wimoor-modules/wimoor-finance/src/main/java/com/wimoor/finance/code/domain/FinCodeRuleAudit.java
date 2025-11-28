package com.wimoor.finance.code.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.wimoor.common.core.annotation.Excel;
import com.wimoor.common.core.web.domain.BaseEntity;

/**
 * 编码规则审计对象 fin_code_rule_audit
 * 
 * @author wimoor
 * @date 2025-11-03
 */
public class FinCodeRuleAudit extends BaseEntity
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

    /** 操作类型: CREATE-创建, UPDATE-更新, DELETE-删除, ENABLE-启用, DISABLE-停用 */
    @Excel(name = "操作类型: CREATE-创建, UPDATE-更新, DELETE-删除, ENABLE-启用, DISABLE-停用")
    private String operationType;

    /** 操作详情 */
    @Excel(name = "操作详情")
    private String operationDetail;

    /** 旧值 */
    @Excel(name = "旧值")
    private String oldValue;

    /** 新值 */
    @Excel(name = "新值")
    private String newValue;

    /** 操作时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "操作时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date operationTime;

    /** 操作人 */
    @Excel(name = "操作人")
    private String operator;

    /** IP地址 */
    @Excel(name = "IP地址")
    private String ipAddress;

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

    public void setOperationType(String operationType) 
    {
        this.operationType = operationType;
    }

    public String getOperationType() 
    {
        return operationType;
    }

    public void setOperationDetail(String operationDetail) 
    {
        this.operationDetail = operationDetail;
    }

    public String getOperationDetail() 
    {
        return operationDetail;
    }

    public void setOldValue(String oldValue) 
    {
        this.oldValue = oldValue;
    }

    public String getOldValue() 
    {
        return oldValue;
    }

    public void setNewValue(String newValue) 
    {
        this.newValue = newValue;
    }

    public String getNewValue() 
    {
        return newValue;
    }

    public void setOperationTime(Date operationTime) 
    {
        this.operationTime = operationTime;
    }

    public Date getOperationTime() 
    {
        return operationTime;
    }

    public void setOperator(String operator) 
    {
        this.operator = operator;
    }

    public String getOperator() 
    {
        return operator;
    }

    public void setIpAddress(String ipAddress) 
    {
        this.ipAddress = ipAddress;
    }

    public String getIpAddress() 
    {
        return ipAddress;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("tenantId", getGroupid())
            .append("ruleCode", getRuleCode())
            .append("operationType", getOperationType())
            .append("operationDetail", getOperationDetail())
            .append("oldValue", getOldValue())
            .append("newValue", getNewValue())
            .append("operationTime", getOperationTime())
            .append("operator", getOperator())
            .append("ipAddress", getIpAddress())
            .toString();
    }
}
