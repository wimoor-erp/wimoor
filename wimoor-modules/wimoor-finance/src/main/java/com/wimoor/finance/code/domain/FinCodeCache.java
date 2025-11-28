package com.wimoor.finance.code.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.wimoor.common.core.annotation.Excel;
import com.wimoor.common.core.web.domain.BaseEntity;

/**
 * 编码缓存对象 fin_code_cache
 * 
 * @author wimoor
 * @date 2025-11-03
 */
public class FinCodeCache extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 缓存键 */
    @Excel(name = "缓存键")
    private String cacheKey;

    /** 缓存值 */
    @Excel(name = "缓存值")
    private String cacheValue;

    /** 规则编码 */
    @Excel(name = "规则编码")
    private String ruleCode;

    /** 账套ID */
    @Excel(name = "账套ID")
    private Long accountSetId;

    /** 业务日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "业务日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date businessDate;

    /** 过期时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "过期时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date expireTime;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createdTime;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setCacheKey(String cacheKey) 
    {
        this.cacheKey = cacheKey;
    }

    public String getCacheKey() 
    {
        return cacheKey;
    }

    public void setCacheValue(String cacheValue) 
    {
        this.cacheValue = cacheValue;
    }

    public String getCacheValue() 
    {
        return cacheValue;
    }

    public void setRuleCode(String ruleCode) 
    {
        this.ruleCode = ruleCode;
    }

    public String getRuleCode() 
    {
        return ruleCode;
    }

    public void setAccountSetId(Long accountSetId) 
    {
        this.accountSetId = accountSetId;
    }

    public Long getAccountSetId() 
    {
        return accountSetId;
    }

    public void setBusinessDate(Date businessDate) 
    {
        this.businessDate = businessDate;
    }

    public Date getBusinessDate() 
    {
        return businessDate;
    }

    public void setExpireTime(Date expireTime) 
    {
        this.expireTime = expireTime;
    }

    public Date getExpireTime() 
    {
        return expireTime;
    }

    public void setCreatedTime(Date createdTime) 
    {
        this.createdTime = createdTime;
    }

    public Date getCreatedTime() 
    {
        return createdTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("cacheKey", getCacheKey())
            .append("cacheValue", getCacheValue())
            .append("ruleCode", getRuleCode())
            .append("accountSetId", getAccountSetId())
            .append("businessDate", getBusinessDate())
            .append("expireTime", getExpireTime())
            .append("createdTime", getCreatedTime())
            .toString();
    }
}
