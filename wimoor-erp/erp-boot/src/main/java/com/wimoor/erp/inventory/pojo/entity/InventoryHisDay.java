package com.wimoor.erp.inventory.pojo.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName( "t_erp_inventory_his_day")
public class InventoryHisDay {

	@TableField(value= "inbound")
	private Integer inbound;

	@TableField(value= "fulfillable")
	private Integer fulfillable;
	
	@TableField(value= "outbound")
	private Integer outbound;
	
	@TableField(value= "warehouseid")
	private String warehouseid;

	@TableField(value= "shopid")
	private String shopid;

	@TableField(value= "materialid")
	private String materialid;

	@TableField(value= "day")
	private Date day;
	
	@TableField(value= "opttime")
	private Date opttime;
	 
	@TableField(value= "operator")
	private String operator;
}