package com.wimoor.finance.setting.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.wimoor.common.core.annotation.Excel;
import com.wimoor.common.core.web.domain.BaseEntity;

/**
 * 辅助核算类别对象 fin_auxiliary_types
 * 
 * @author wimoor
 * @date 2025-11-04
 */
public class FinAuxiliaryTypes extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 辅助核算类型ID */
    private Long typeId;

    /** 租户ID */
    private String groupid;

    /** 类型名称：部门、员工、客户等 */
    @Excel(name = "类型名称：部门、员工、客户等")
    private String typeName;

    /** 类型编码：DEPT、EMP、CUST等 */
    @Excel(name = "类型编码：DEPT、EMP、CUST等")
    private String typeCode;

    public void setTypeId(Long typeId) 
    {
        this.typeId = typeId;
    }

    public Long getTypeId() 
    {
        return typeId;
    }

    public void setGroupid(String groupid) 
    {
        this.groupid = groupid;
    }

    public String getGroupid() 
    {
        return groupid;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("typeId", getTypeId())
            .append("groupid", getGroupid())
            .append("typeName", getTypeName())
            .append("typeCode", getTypeCode())
            .toString();
    }
}
