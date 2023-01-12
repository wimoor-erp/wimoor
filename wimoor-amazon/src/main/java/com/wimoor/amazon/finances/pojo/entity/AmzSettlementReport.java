package com.wimoor.amazon.finances.pojo.entity;

import java.math.BigDecimal;
import java.util.Date;
 

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wimoor.amazon.common.pojo.entity.BaseEntity;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
 
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_amz_settlement_report")  
@ApiModel(value="AmzSettlementAccStatement对象", description="账期记录结账")
public class AmzSettlementReport extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4205738407672929894L;

	@TableField(value= "settlement_id")
    private String settlementId;

	@TableField(value= "currency")
    private String currency;

	@TableField(value= "transaction_type")
    private String transactionType;

	@TableField(value= "order_id")
    private String orderId;

	@TableField(value= "merchant_order_id")
    private String merchantOrderId;

	@TableField(value= "adjustment_id")
    private String adjustmentId;

	@TableField(value= "shipment_id")
    private String shipmentId;

	@TableField(value= "marketplace_name")
    private String marketplaceName;

	@TableField(value= "amount_type")
    private String amountType;

	@TableField(value= "amount_description")
    private String amountDescription;

	@TableField(value= "amount")
    private BigDecimal amount;

	@TableField(value= "fulfillment_id")
    private String fulfillmentId;
	
	@TableField(value= "posted_date")
    private Date postedDate;

	@TableField(value= "posted_date_time")
    private Date postedDateTime;

	@TableField(value= "order_item_code")
    private String orderItemCode;

	@TableField(value= "merchant_order_item_id")
    private String merchantOrderItemId;

	@TableField(value= "merchant_adjustment_item_id")
    private String merchantAdjustmentItemId;

	@TableField(value= "sku")
    private String sku;

	@TableField(value= "quantity_purchased")
    private Integer quantityPurchased;

	@TableField(value= "promotion_id")
    private String promotionId;
	
	@TableField(value= "amazonAuthId")
	private String amazonAuthId;
	 
    
}