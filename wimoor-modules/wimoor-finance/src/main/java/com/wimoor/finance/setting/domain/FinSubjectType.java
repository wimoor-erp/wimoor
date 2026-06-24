package com.wimoor.finance.setting.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.wimoor.common.core.annotation.Excel;
import com.wimoor.common.core.web.domain.BaseEntity;

/**
 * 科目类别对象 fin_subject_type
 * 
 * @author wimoor
 * @date 2026-04-03
 */
public class FinSubjectType extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Integer id;

    /** 类别编码，唯一 */
    @Excel(name = "类别编码，唯一")
    private String code;

    /** 类别名称 */
    @Excel(name = "类别名称")
    private String name;

    /** 父级类别编码，顶级类别的父级为NULL */
    @Excel(name = "父级类别编码，顶级类别的父级为NULL")
    private String parentCode;

    /** 层级：1=一级科目类别，2=二级类别，3=三级类别 */
    @Excel(name = "层级：1=一级科目类别，2=二级类别，3=三级类别")
    private Integer level;

    /** 科目大类标识，用于业务逻辑判断 */
    @Excel(name = "科目大类标识，用于业务逻辑判断")
    private String categoryType;

    /** 排序号，数值越小越靠前 */
    @Excel(name = "排序号，数值越小越靠前")
    private Integer sortOrder;

    /** 是否为固定预置项（TRUE=系统固定，不可删除/修改名称） */
    @Excel(name = "是否为固定预置项", readConverterExp = "T=RUE=系统固定，不可删除/修改名称")
    private Integer isFixed;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createdTime;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date updatedTime;

    public void setId(Integer id) 
    {
        this.id = id;
    }

    public Integer getId() 
    {
        return id;
    }

    public void setCode(String code) 
    {
        this.code = code;
    }

    public String getCode() 
    {
        return code;
    }

    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }

    public void setParentCode(String parentCode) 
    {
        this.parentCode = parentCode;
    }

    public String getParentCode() 
    {
        return parentCode;
    }

    public void setLevel(Integer level) 
    {
        this.level = level;
    }

    public Integer getLevel() 
    {
        return level;
    }

    public void setCategoryType(String categoryType) 
    {
        this.categoryType = categoryType;
    }

    public String getCategoryType() 
    {
        return categoryType;
    }

    public void setSortOrder(Integer sortOrder) 
    {
        this.sortOrder = sortOrder;
    }

    public Integer getSortOrder() 
    {
        return sortOrder;
    }

    public void setIsFixed(Integer isFixed) 
    {
        this.isFixed = isFixed;
    }

    public Integer getIsFixed() 
    {
        return isFixed;
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
            .append("code", getCode())
            .append("name", getName())
            .append("parentCode", getParentCode())
            .append("level", getLevel())
            .append("categoryType", getCategoryType())
            .append("sortOrder", getSortOrder())
            .append("isFixed", getIsFixed())
            .append("createdTime", getCreatedTime())
            .append("updatedTime", getUpdatedTime())
            .toString();
    }
}
