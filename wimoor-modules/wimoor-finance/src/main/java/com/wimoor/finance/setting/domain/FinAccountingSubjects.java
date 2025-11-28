package com.wimoor.finance.setting.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.wimoor.common.core.annotation.Excel;
import com.wimoor.common.core.web.domain.TreeEntity;

/**
 * 会计科目对象 fin_accounting_subjects
 * 
 * @author wimoor
 * @date 2025-11-05
 */
public class FinAccountingSubjects extends TreeEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long subjectId;

    /** 科目编码 */
    @Excel(name = "科目编码")
    private String subjectCode;

    /** 租户ID */
    @Excel(name = "租户ID")
    private String groupid;

    /** 科目名称 */
    @Excel(name = "科目名称")
    private String subjectName;

    /** 科目级别 */
    @Excel(name = "科目级别")
    private Integer subjectLevel;

    /** 父级科目编码 */
    @Excel(name = "父级科目编码")
    private String parentCode;

    /** 科目类型 */
    @Excel(name = "科目类型")
    private Integer subjectType;

    /** 余额方向 */
    @Excel(name = "余额方向")
    private Integer direction;

    /** 末级科目 */
    @Excel(name = "末级科目")
    private Integer isLeaf;

    /** 状态 */
    @Excel(name = "状态")
    private Integer status;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createdTime;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date updatedTime;

    public void setSubjectId(Long subjectId)
    {
        this.subjectId = subjectId;
    }

    public Long getSubjectId()
    {
        return subjectId;
    }

    public void setSubjectCode(String subjectCode) 
    {
        this.subjectCode = subjectCode;
    }

    public String getSubjectCode() 
    {
        return subjectCode;
    }

    public void setGroupid(String groupid) 
    {
        this.groupid = groupid;
    }

    public String getGroupid() 
    {
        return groupid;
    }

    public void setSubjectName(String subjectName) 
    {
        this.subjectName = subjectName;
    }

    public String getSubjectName() 
    {
        return subjectName;
    }

    public void setSubjectLevel(Integer subjectLevel)
    {
        this.subjectLevel = subjectLevel;
    }

    public Integer getSubjectLevel()
    {
        return subjectLevel;
    }

    public void setParentCode(String parentCode) 
    {
        this.parentCode = parentCode;
    }

    public String getParentCode() 
    {
        return parentCode;
    }

    public void setSubjectType(Integer subjectType)
    {
        this.subjectType = subjectType;
    }

    public Integer getSubjectType()
    {
        return subjectType;
    }

    public void setDirection(Integer direction)
    {
        this.direction = direction;
    }

    public Integer getDirection()
    {
        return direction;
    }

    public void setIsLeaf(Integer isLeaf)
    {
        this.isLeaf = isLeaf;
    }

    public Integer getIsLeaf()
    {
        return isLeaf;
    }

    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public Integer getStatus()
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
            .append("subjectId", getSubjectId())
            .append("subjectCode", getSubjectCode())
            .append("groupid", getGroupid())
            .append("subjectName", getSubjectName())
            .append("subjectLevel", getSubjectLevel())
            .append("parentCode", getParentCode())
            .append("subjectType", getSubjectType())
            .append("direction", getDirection())
            .append("isLeaf", getIsLeaf())
            .append("status", getStatus())
            .append("createdTime", getCreatedTime())
            .append("updatedTime", getUpdatedTime())
            .toString();
    }

    public boolean isDebitBalance() {
        return direction == 1;
    }
}
