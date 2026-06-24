package com.wimoor.finance.setting.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wimoor.common.core.annotation.Excel;
import com.wimoor.common.core.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 会计科目对应辅助核算分类设置对象 fin_accounting_subject_auxiliary_setting
 * 
 * @author wimoor
 * @date 2026-05-26
 */
public class FinAccountingSubjectAuxiliarySetting extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private String id;

    /** 科目ID */
    @Excel(name = "科目ID")
    private String subjectId;

    /** 辅助核算分类ID */
    @Excel(name = "辅助核算分类ID")
    private String auxiliaryTypeId;

    /** 辅助核算类型名称 */
    @Excel(name = "辅助核算类型名称")
    private String typeName;

    /** 辅助核算类型编码 */
    @Excel(name = "辅助核算类型编码")
    private String typeCode;

    /** 租户ID */
    @Excel(name = "租户ID")
    private String groupid;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createdTime;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date updatedTime;

    public void setId(String id) 
    {
        this.id = id;
    }

    public String getId() 
    {
        return id;
    }

    public void setSubjectId(String subjectId) 
    {
        this.subjectId = subjectId;
    }

    public String getSubjectId() 
    {
        return subjectId;
    }

    public void setAuxiliaryTypeId(String auxiliaryTypeId) 
    {
        this.auxiliaryTypeId = auxiliaryTypeId;
    }

    public String getAuxiliaryTypeId() 
    {
        return auxiliaryTypeId;
    }

    public void setTypeName(String typeName) 
    {
        this.typeName = typeName;
    }

    public String getTypeName() 
    {
        return typeName;
    }

    public void setTypeCode(String typeCode) 
    {
        this.typeCode = typeCode;
    }

    public String getTypeCode() 
    {
        return typeCode;
    }

    public void setGroupid(String groupid) 
    {
        this.groupid = groupid;
    }

    public String getGroupid() 
    {
        return groupid;
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
            .append("subjectId", getSubjectId())
            .append("auxiliaryTypeId", getAuxiliaryTypeId())
            .append("typeName", getTypeName())
            .append("typeCode", getTypeCode())
            .append("groupid", getGroupid())
            .append("createdTime", getCreatedTime())
            .append("updatedTime", getUpdatedTime())
            .toString();
    }
}
