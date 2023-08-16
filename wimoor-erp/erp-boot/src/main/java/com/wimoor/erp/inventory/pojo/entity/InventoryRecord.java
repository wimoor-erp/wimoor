package com.wimoor.erp.inventory.pojo.entity;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.erp.common.pojo.entity.ErpBaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_erp_inventory_record")
public class InventoryRecord  extends ErpBaseEntity{
 
    /**
	 * 
	 */
	private static final long serialVersionUID = 8730850011629280930L;

	@TableField(value= "shopid")
    private String shopid;

    @TableField(value= "warehouseid")
    private String warehouseid;

    @TableField(value= "materialid")
    private String materialid;

    @TableField(value= "formid")
    private String formid;

    @TableField(value= "formtype")
    private String formtype;

    @TableField(value= "operate")
    private String operate;
 
    
    @TableField(value="number")
    private String number;

    @NotNull(message="数量不能为空")
    @TableField(value="quantity")
    private Integer quantity;
    
    @NotNull(message="数量不能为空")
    @TableField(value="invqty")
    private Integer invqty;
    
    @NotNull(message="状态不能为空")
	@Size(min=1,max=36,message="状态的长度不能超过36个字符")
    @TableField(value="status")
    private String status;
    
    @TableField(value="startfulfillable")
    private Integer startfulfillable;

    @TableField(value="fulfillable")
    private Integer fulfillable;
    
    @TableField(value="endfulfillable")
    private Integer endfulfillable;
    
    @TableField(value="startinbound")
    private Integer startinbound;

    @TableField(value="inbound")
    private Integer inbound;
    
    @TableField(value="endinbound")
    private Integer endinbound;
    
    @TableField(value="startoutbound")
    private Integer startoutbound;

    @TableField(value="outbound")
    private Integer outbound;
    
    @TableField(value="endoutbound")
    private Integer endoutbound;
 
}