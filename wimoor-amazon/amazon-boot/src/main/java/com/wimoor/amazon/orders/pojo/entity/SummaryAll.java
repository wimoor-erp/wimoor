package com.wimoor.amazon.orders.pojo.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@TableName("t_summaryall")  
@ApiModel(value="SummaryAll对象", description="订单汇总表")
public class SummaryAll implements Serializable{
 
	private static final long serialVersionUID = 9186381435900331425L;

	@TableField(value=  "id")
	private String id;
	
	@TableField(value=  "purchase_date")
	private Date purchaseDate;
	
	@TableField(value=  "amazonauthid")
	private String amazonauthid;

	@TableField(value=  "sales_channel")
	private String salesChannel;
	
	@TableField(value=  "order_status")
	private String orderStatus;

	@TableField(value=  "fulfillChannel")
	private String fulfillChannel;
	
	@TableField(value=  "discount")
	private String discount;

	@TableField(value=  "is_business_order")
	private String isBusinessOrder;

	@TableField(value=  "quantity")
	private Integer quantity;

	@TableField(value=  "ordernumber")
	private Integer ordernumber;

	@TableField(value=  "discount_amount")
	private BigDecimal discountAmount;
	
	@TableField(value=  "price")
	private BigDecimal price;
 

}
