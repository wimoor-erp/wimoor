package com.wimoor.erp.purchase.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName( "t_erp_purchase_planmodelitemsub")
public class PurchasePlanModelItemsub   {
	@TableField(value= "itemid")
	private String itemid;

	@TableField(value= "sku")
	private String sku;

	@TableField(value= "marketplaceid")
	private String marketplaceid;

	@TableField(value= "groupid")
	private String groupid;
	
	@TableField(value= "needship")
	private Integer needship;

	@TableField(value= "salesday")
	private Integer salesday;

	@TableField(value= "aftersalesday")
	private Integer aftersalesday;
 
}