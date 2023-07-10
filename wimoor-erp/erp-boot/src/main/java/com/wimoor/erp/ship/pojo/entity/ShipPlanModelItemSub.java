package com.wimoor.erp.ship.pojo.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName( "t_erp_ship_planmodelitemsub")
public class ShipPlanModelItemSub   {

	@TableField(value="itemid")
    private String itemid;

	@TableField(value="sku")
    private String sku;
    
	@TableField(value="marketplaceid")
    private String marketplaceid;
    
	@TableField(value= "needship")
	private Integer needship;

	@TableField(value= "salesday")
	private Integer salesday;

	@TableField(value= "aftersalesday")
	private Integer aftersalesday;

	@TableField(value= "short_time")
	private Date shortTime;
 
}