package com.wimoor.finance.code.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.wimoor.common.core.annotation.Excel;
import com.wimoor.common.core.web.domain.BaseEntity;

/**
 * 编码规则分类对象 fin_code_category
 * 
 * @author wimoor
 * @date 2025-11-03
 */
public class FinCodeCategory extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 分类编码 */
    @Excel(name = "分类编码")
    private String categoryCode;

    /** 分类名称 */
    @Excel(name = "分类名称")
    private String categoryName;

    /** 父级分类编码 */
    @Excel(name = "父级分类编码")
    private String parentCategoryCode;

    /** 分类级别 */
    @Excel(name = "分类级别")
    private Long categoryLevel;

    /** 分类描述 */
    @Excel(name = "分类描述")
    private String description;

    /** 排序 */
    @Excel(name = "排序")
    private Long sortOrder;

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

    public void setCategoryCode(String categoryCode) 
    {
        this.categoryCode = categoryCode;
    }

    public String getCategoryCode() 
    {
        return categoryCode;
    }

    public void setCategoryName(String categoryName) 
    {
        this.categoryName = categoryName;
    }

    public String getCategoryName() 
    {
        return categoryName;
    }

    public void setParentCategoryCode(String parentCategoryCode) 
    {
        this.parentCategoryCode = parentCategoryCode;
    }

    public String getParentCategoryCode() 
    {
        return parentCategoryCode;
    }

    public void setCategoryLevel(Long categoryLevel) 
    {
        this.categoryLevel = categoryLevel;
    }

    public Long getCategoryLevel() 
    {
        return categoryLevel;
    }

    public void setDescription(String description) 
    {
        this.description = description;
    }

    public String getDescription() 
    {
        return description;
    }

    public void setSortOrder(Long sortOrder) 
    {
        this.sortOrder = sortOrder;
    }

    public Long getSortOrder() 
    {
        return sortOrder;
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
            .append("categoryCode", getCategoryCode())
            .append("categoryName", getCategoryName())
            .append("parentCategoryCode", getParentCategoryCode())
            .append("categoryLevel", getCategoryLevel())
            .append("description", getDescription())
            .append("sortOrder", getSortOrder())
            .append("status", getStatus())
            .append("createdTime", getCreatedTime())
            .append("updatedTime", getUpdatedTime())
            .toString();
    }
}
