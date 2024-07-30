package com.wimoor.erp.inventory.pojo.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.erp.common.pojo.entity.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName( "t_erp_inventory_his")
public class InventoryHis extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7404498932090481030L;

	@TableField(value= "quantity")
	private Integer quantity;

	@TableField(value= "operator")
	private String operator;

	@TableField(value= "opttime")
	private Date opttime;
	@TableField(value= "warehouseid")
	private String warehouseid;

	@TableField(value= "shopid")
	private String shopid;

	@TableField(value= "materialid")
	private String materialid;

	@TableField(value= "status")
	private String status;

	@TableField(value= "modifyday")
	private Date modifyday;

	 
	public void setNewInventory(Inventory addinv) {
		this.setModifyday(new Date());
		this.setMaterialid(addinv.getMaterialid());
		this.setWarehouseid(addinv.getWarehouseid());
		this.setShopid(addinv.getShopid());
		this.setStatus(addinv.getStatus());
		this.setOperator(addinv.getOperator());
		this.setOpttime(new Date());
		this.setQuantity(addinv.getQuantity());
	}

	 
}