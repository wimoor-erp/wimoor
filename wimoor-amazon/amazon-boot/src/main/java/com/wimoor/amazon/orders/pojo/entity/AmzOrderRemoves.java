package com.wimoor.amazon.orders.pojo.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import lombok.Data;

 
@Data
@TableName("t_amz_order_remove_report")  
@ApiModel(value="AmzOrderRemoves对象", description="移除订单")
public class AmzOrderRemoves  {
 
	@TableField(value= "order_id")
    private String orderId;
 
	@TableField(value= "sku")
    private String sku;
	
	@TableField(value= "amazonAuthId")
    private String amazonauthid;
	
	@TableField(value= "purchase_date")
    private Date purchaseDate;

	@TableField(value= "last_updated_date")
    private Date lastUpdatedDate;

	@TableField(value= "order_type")
    private String orderType;

	@TableField(value= "order_status")
    private String orderStatus;

	@TableField(value= "fnsku")
    private String fnsku;

	@TableField(value= "disposition")
    private String disposition;

	@TableField(value= "requested_quantity")
    private Integer requestedQuantity;

	@TableField(value= "cancelled_quantity")
    private Integer cancelledQuantity;

	@TableField(value= "disposed_quantity")
    private Integer disposedQuantity;

	@TableField(value= "shipped_quantity")
    private Integer shippedQuantity;

	@TableField(value= "in_process_quantity")
    private Integer inProcessQuantity;

	@TableField(value= "removal_fee")
    private BigDecimal removalFee;

	@TableField(value= "currency")
    private String currency;

    
    
}