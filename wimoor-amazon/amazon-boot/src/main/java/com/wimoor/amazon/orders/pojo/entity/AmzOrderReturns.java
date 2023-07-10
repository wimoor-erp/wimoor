package com.wimoor.amazon.orders.pojo.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.jeffreyning.mybatisplus.anno.MppMultiId;

import io.swagger.annotations.ApiModel;
import lombok.Data;
@Data
@TableName("t_amz_returns_report")  
@ApiModel(value="AmzOrderReturns对象", description="退货订单")
public class AmzOrderReturns implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 452755306327228968L;

	@MppMultiId
	@TableField(value= "sku")
    private String sku;

	@MppMultiId
	@TableField(value= "return_date")
    private Date returnDate;

	@MppMultiId
	@TableField(value= "order_id")
    private String orderId;
	
	@MppMultiId
	@TableField(value= "sellerid")
	private String sellerid;
	
	@TableField(value= "asin")
    private String asin;

	@TableField(value= "fnsku")
    private String fnsku;

	@TableField(value= "quantity")
    private String quantity;

	@TableField(value= "fulfillment_center_id")
    private String fulfillmentCenterId;

	@TableField(value= "detailed_disposition")
    private String detailedDisposition;

	@TableField(value= "reason")
    private String reason;

	@TableField(value= "status")
    private String status;

	@TableField(value= "license_plate_number")
    private String licensePlateNumber;

	@TableField(value= "customer_comments")
    private String customerComments;
	
	@TableField(value= "marketplaceid")
    private String marketplaceid;
 
}