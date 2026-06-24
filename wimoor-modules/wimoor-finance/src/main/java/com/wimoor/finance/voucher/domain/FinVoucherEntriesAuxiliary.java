package com.wimoor.finance.voucher.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.wimoor.common.core.annotation.Excel;
import com.wimoor.common.core.web.domain.BaseEntity;

/**
 * 凭证分录辅助核算对象 fin_voucher_entries_auxiliary
 * 
 * @author wimoor
 * @date 2026-05-26
 */
public class FinVoucherEntriesAuxiliary extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private String id;

    /** 关联分录 */
    @Excel(name = "关联分录")
    private String entryId;

    /** 核算类型：CUSTOMER, DEPARTMENT, PROJECT, SUPPLIER, EMPLOYEE */
    @Excel(name = "核算类型：CUSTOMER, DEPARTMENT, PROJECT, SUPPLIER, EMPLOYEE")
    private String auxiliaryTypeId;

    /** 具体核算项的ID（对应客户主表、部门主表的ID） */
    @Excel(name = "具体核算项的ID", readConverterExp = "对=应客户主表、部门主表的ID")
    private String auxiliaryItemId;

    /** 项目编码 */
    private String itemCode;

    /** 项目名称 */
    private String itemName;

    /** 辅助核算类型名称 */
    private String typeName;

    /** 租户ID */
    @Excel(name = "租户ID")
    private String groupid;

    public void setId(String id) 
    {
        this.id = id;
    }

    public String getId() 
    {
        return id;
    }

    public void setEntryId(String entryId) 
    {
        this.entryId = entryId;
    }

    public String getEntryId() 
    {
        return entryId;
    }

    public void setAuxiliaryTypeId(String auxiliaryTypeId) 
    {
        this.auxiliaryTypeId = auxiliaryTypeId;
    }

    public String getAuxiliaryTypeId() 
    {
        return auxiliaryTypeId;
    }

    public void setAuxiliaryItemId(String auxiliaryItemId) 
    {
        this.auxiliaryItemId = auxiliaryItemId;
    }

    public String getAuxiliaryItemId() 
    {
        return auxiliaryItemId;
    }

    public void setItemCode(String itemCode)
    {
        this.itemCode = itemCode;
    }

    public String getItemCode()
    {
        return itemCode;
    }

    public void setItemName(String itemName)
    {
        this.itemName = itemName;
    }

    public String getItemName()
    {
        return itemName;
    }

    public void setTypeName(String typeName)
    {
        this.typeName = typeName;
    }

    public String getTypeName()
    {
        return typeName;
    }

    public void setGroupid(String groupid) 
    {
        this.groupid = groupid;
    }

    public String getGroupid() 
    {
        return groupid;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("entryId", getEntryId())
            .append("auxiliaryTypeId", getAuxiliaryTypeId())
            .append("auxiliaryItemId", getAuxiliaryItemId())
            .append("groupid", getGroupid())
            .toString();
    }
}
