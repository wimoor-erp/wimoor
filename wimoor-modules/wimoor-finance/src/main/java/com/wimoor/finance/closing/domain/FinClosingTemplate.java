package com.wimoor.finance.closing.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wimoor.common.core.annotation.Excel;
import com.wimoor.common.core.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 财务结算的各个模版名称对象 fin_closing_template
 * 
 * @author wimoor
 * @date 2026-03-17
 */
public class FinClosingTemplate extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private String id;

    /** 名称 */
    @Excel(name = "名称")
    private String name;

    /** 账套 */
    @Excel(name = "账套")
    private String groupid;

    @Excel(name = "类型")
    private String ftype;

    @Excel(name = "类型")
    private String voucherType;

    @Excel(name = "国家")
    private String country;
    /** 是否系统 */
    @Excel(name = "是否系统")
    private Integer issystem;

    /** 是否禁用 */
    @Excel(name = "是否禁用")
    private Integer disabled;

    //carryover_type
    //结转方式按科目方向调汇  取值1和0
    @Excel(name = "结转方式")
    private Integer carryoverType;

    //voucher_class
    //凭证分类  取值1和0
    @Excel(name = "凭证分类")
    private Integer voucherClass;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createdTime;

    /** 修改时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "修改时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date updatedTime;
    /** 修改人 */
    @Excel(name = "修改人")
    private String modifyBy;

    private String vourchesId;

    private String period;

    private Integer findex;

    public Integer getFindex() {
        return findex;
    }
    public void setFIndex(Integer findex) {
        this.findex = findex;
    }
    public String getPeriod() {
        return period;
    }
    public void setPeriod(String period) {
        this.period = period;
    }

    public String getVoucherType() {
        return voucherType;
    }

    public void setVoucherType(String voucherType) {
        this.voucherType = voucherType;
    }

    public String getFtype() {
        return ftype;
    }

    public void setFtype(String ftype) {
        this.ftype = ftype;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getId() 
    {
        return id;
    }

    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }

    public void setGroupid(String groupid) 
    {
        this.groupid = groupid;
    }

    public String getGroupid() 
    {
        return groupid;
    }

    public void setIssystem(Integer issystem) 
    {
        this.issystem = issystem;
    }

    public Integer getIssystem() 
    {
        return issystem;
    }

    public void setDisabled(Integer disabled) 
    {
        this.disabled = disabled;
    }

    public Integer getDisabled() 
    {
        return disabled;
    }

    public void setCarryoverType(Integer carryoverType)
    {
        this.carryoverType = carryoverType;
    }

    public Integer getCarryoverType()
    {
        return carryoverType;
    }

    public void setVoucherClass(Integer voucherClass)
    {
        this.voucherClass = voucherClass;
    }

    public Integer getVoucherClass()
    {
        return voucherClass;
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

    public String getVourchesId() {
        return vourchesId;
    }

    public void setVourchesId(String vourchesId) {
        this.vourchesId = vourchesId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("name", getName())
            .append("groupid", getGroupid())
            .append("issystem", getIssystem())
            .append("disabled", getDisabled())
            .append("carryoverType", getCarryoverType())
            .append("voucherClass", getVoucherClass())
            .append("createdTime", getCreatedTime())
            .append("updatedTime", getUpdatedTime())
            .append("modifyBy", getModifyBy())
            .append("createBy", getCreateBy())
            .toString();
    }
}
