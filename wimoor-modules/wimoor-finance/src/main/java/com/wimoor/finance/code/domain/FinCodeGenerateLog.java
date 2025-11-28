package com.wimoor.finance.code.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.wimoor.common.core.annotation.Excel;
import com.wimoor.common.core.web.domain.BaseEntity;

/**
 * 编码生成记录对象 fin_code_generate_log
 * 
 * @author wimoor
 * @date 2025-11-03
 */
public class FinCodeGenerateLog extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 租户ID */
    @Excel(name = "租户ID")
    private String groupid;

    /** 生成的编码 */
    @Excel(name = "生成的编码")
    private String generatedCode;

    /** 规则编码 */
    @Excel(name = "规则编码")
    private String ruleCode;

    /** 规则名称 */
    @Excel(name = "规则名称")
    private String ruleName;

    /** 业务类型 */
    @Excel(name = "业务类型")
    private String businessType;

    /** 业务名称 */
    @Excel(name = "业务名称")
    private String businessName;

    /** 账套ID */
    @Excel(name = "账套ID")
    private Long accountSetId;

    /** 父级编码 */
    @Excel(name = "父级编码")
    private String parentCode;

    /** 自定义参数，JSON格式 */
    @Excel(name = "自定义参数，JSON格式")
    private String customParams;

    /** 生成时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "生成时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date generatedTime;

    /** 操作人 */
    @Excel(name = "操作人")
    private String operator;

    /** IP地址 */
    @Excel(name = "IP地址")
    private String ipAddress;

    /** 用户代理 */
    @Excel(name = "用户代理")
    private String userAgent;

    /** 租户ID */
    public void setGroupid(String groupid)
    {
        this.groupid = groupid;
    }

    public String getGroupid()
    {
        return groupid;
    }

    /** 主键ID */
    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setGeneratedCode(String generatedCode) 
    {
        this.generatedCode = generatedCode;
    }

    public String getGeneratedCode() 
    {
        return generatedCode;
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

    public void setBusinessType(String businessType) 
    {
        this.businessType = businessType;
    }

    public String getBusinessType() 
    {
        return businessType;
    }

    public void setBusinessName(String businessName) 
    {
        this.businessName = businessName;
    }

    public String getBusinessName() 
    {
        return businessName;
    }

    public void setAccountSetId(Long accountSetId) 
    {
        this.accountSetId = accountSetId;
    }

    public Long getAccountSetId() 
    {
        return accountSetId;
    }

    public void setParentCode(String parentCode) 
    {
        this.parentCode = parentCode;
    }

    public String getParentCode() 
    {
        return parentCode;
    }

    public void setCustomParams(String customParams) 
    {
        this.customParams = customParams;
    }

    public String getCustomParams() 
    {
        return customParams;
    }

    public void setGeneratedTime(Date generatedTime) 
    {
        this.generatedTime = generatedTime;
    }

    public Date getGeneratedTime() 
    {
        return generatedTime;
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

    public void setUserAgent(String userAgent) 
    {
        this.userAgent = userAgent;
    }

    public String getUserAgent() 
    {
        return userAgent;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("generatedCode", getGeneratedCode())
            .append("ruleCode", getRuleCode())
            .append("ruleName", getRuleName())
            .append("businessType", getBusinessType())
            .append("businessName", getBusinessName())
            .append("accountSetId", getAccountSetId())
            .append("parentCode", getParentCode())
            .append("customParams", getCustomParams())
            .append("generatedTime", getGeneratedTime())
            .append("operator", getOperator())
            .append("ipAddress", getIpAddress())
            .append("userAgent", getUserAgent())
            .toString();
    }
}
