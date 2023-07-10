package com.wimoor.amazon.report.pojo.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@TableName("t_amz_po_rpt_day")  
@ApiModel(value="t_amz_po_rpt_day对象", description="产品订单汇总表")
public class AmazonProductSales implements Serializable{ 
	private static final long serialVersionUID = -2879405822511152569L;

	@TableField(value=  "amazonAuthId")
	private String amazonAuthId;
	
	@TableField(value=  "purchase_date")
	private Date purchaseDate;
	
	@TableField(value=  "sales_channel")
	private String salesChannel;
	
	@TableField(value=  "order_status")
	private String orderStatus;

	@TableField(value=  "sku")
	private String sku;
	
	@TableField(value=  "asin")
	private String asin;
	
	@TableField(value=  "currency")
	private String currency;

	@TableField(value=  "quantity")
	private Integer quantity;

	@TableField(value=  "ordersum")
	private Integer ordersum;

	@TableField(value=  "ship_country")
	private String  shipCountry;
	
	@TableField(value=  "price")
	private BigDecimal price;
	
	@TableField(value=  "pricermb")
	private BigDecimal pricermb;
	
}
