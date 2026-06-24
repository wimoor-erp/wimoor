package com.wimoor.finance.setting.domain;

import com.wimoor.common.core.annotation.Excel;
import com.wimoor.common.core.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigInteger;

/**
 * 辅助核算具体项目对象 fin_auxiliary_items
 * 
 * @author wimoor
 * @date 2025-11-04
 */
public class FinAuxiliaryItems extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 辅助核算项目ID */
    private BigInteger itemId;

    /** 租户ID */
    private String groupid;

    /** 公司ID */
    private String shopid;

    /** 辅助核算类型ID */
    private Long typeId;

    /** 辅助核算类型编码 */
    private String typeCode;

    /** 项目编码 */
    @Excel(name = "项目编码")
    private String itemCode;

    /** 项目名称 */
    @Excel(name = "项目名称")
    private String itemName;

    /** 状态 */
    @Excel(name = "状态")
    private Long status;



    public void setItemId(BigInteger itemId)
    {
        this.itemId = itemId;
    }

    public BigInteger getItemId()
    {
        return itemId;
    }


    public void setShopid(String shopid)
    {
        this.shopid = shopid;
    }

    public String getShopid()
    {
        return shopid;
    }

    public void setGroupid(String groupid) 
    {
        this.groupid = groupid;
    }

    public String getGroupid() 
    {
        return groupid;
    }

    public void setTypeId(Long typeId) 
    {
        this.typeId = typeId;
    }

    public Long getTypeId() 
    {
        return typeId;
    }

    public void setTypeCode(String typeCode)
    {
        this.typeCode = typeCode;
    }

    public String getTypeCode()
    {
        return typeCode;
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

    public void setStatus(Long status) 
    {
        this.status = status;
    }

    public Long getStatus() 
    {
        return status;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("itemId", getItemId())
            .append("groupid", getGroupid())
            .append("typeId", getTypeId())
            .append("typeCode", getTypeCode())
            .append("itemCode", getItemCode())
            .append("itemName", getItemName())
            .append("status", getStatus())
            .toString();
    }
}
