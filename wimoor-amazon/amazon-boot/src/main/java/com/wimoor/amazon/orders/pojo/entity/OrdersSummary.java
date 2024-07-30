package com.wimoor.amazon.orders.pojo.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

 
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import lombok.Data;
 
@Data
@TableName("t_orders_summary")  
@ApiModel(value="OrdersSummary对象", description="订单汇总")
public class OrdersSummary implements Serializable  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4396306849383021530L;
 
	@TableField(value=  "id")
	private String id;
	
	@TableField(value=  "purchase_date")
	private Date purchaseDate;
	
	@TableField(value=  "amazonauthid")
	private String amazonauthid;

	@TableField(value=  "marketplaceid")
	private String marketplaceid;

	@TableField(value=  "sku")
	private String sku;

	@TableField(value=  "asin")
	private String asin;

	@TableField(value=  "quantity")
	private Integer quantity;

	@TableField(value=  "ordersum")
	private Integer ordersum;

	@TableField(value=  "orderprice")
	private BigDecimal orderprice;
 
}