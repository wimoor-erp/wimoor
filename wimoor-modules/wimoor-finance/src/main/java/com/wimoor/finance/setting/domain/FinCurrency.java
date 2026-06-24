package com.wimoor.finance.setting.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.wimoor.common.core.annotation.Excel;
import com.wimoor.common.core.web.domain.BaseEntity;

/**
 * currency对象 fin_currency
 * 
 * @author wimoor
 * @date 2026-01-15
 */
public class FinCurrency extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    @Excel(name = "币种编码")
    private String code;

    /** $column.columnComment */
    private String groupid;

    /** $column.columnComment */
    @Excel(name = "币种名称")
    private String name;

    /** $column.columnComment */
    @Excel(name = "币种汇率")
    private BigDecimal rate;

    /** $column.columnComment */
    @Excel(name = "是否本位币")
    private Integer islocal;

    /** $column.columnComment */
    private Integer isauto;

    /** $column.columnComment */
    private String modifyBy;

    /** $column.columnComment */
    private Date createdTime;

    /** $column.columnComment */
    private Date updatedTime;

    public void setCode(String code) 
    {
        this.code = code;
    }

    public String getCode() 
    {
        return code;
    }

    public void setGroupid(String groupid) 
    {
        this.groupid = groupid;
    }

    public String getGroupid() 
    {
        return groupid;
    }

    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }

    public void setRate(BigDecimal rate) 
    {
        this.rate = rate;
    }

    public BigDecimal getRate() 
    {
        return rate;
    }

    public void setIslocal(Integer islocal) 
    {
        this.islocal = islocal;
    }

    public Integer getIslocal() 
    {
        return islocal;
    }

    public void setIsauto(Integer isauto) 
    {
        this.isauto = isauto;
    }

    public Integer getIsauto() 
    {
        return isauto;
    }

    public void setModifyBy(String modifyBy) 
    {
        this.modifyBy = modifyBy;
    }

    public String getModifyBy() 
    {
        return modifyBy;
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
            .append("code", getCode())
            .append("groupid", getGroupid())
            .append("name", getName())
            .append("rate", getRate())
            .append("islocal", getIslocal())
            .append("isauto", getIsauto())
            .append("modifyBy", getModifyBy())
            .append("createBy", getCreateBy())
            .append("createdTime", getCreatedTime())
            .append("updatedTime", getUpdatedTime())
            .toString();
    }
}
