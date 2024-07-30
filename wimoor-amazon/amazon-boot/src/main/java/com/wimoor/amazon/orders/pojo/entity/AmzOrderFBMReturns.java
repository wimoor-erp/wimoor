package com.wimoor.amazon.orders.pojo.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.jeffreyning.mybatisplus.anno.MppMultiId;

import io.swagger.annotations.ApiModel;
import lombok.Data;
@Data
@TableName("t_amz_fbm_returns_report")  
@ApiModel(value="AmzFBMOrderReturns对象", description="自发货退货订单")
public class AmzOrderFBMReturns implements Serializable{
	
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
	@TableField(value= "purchase_date")
    private Date purchaseDate;
	
	@MppMultiId
	@TableField(value= "order_id")
    private String orderId;
	
	@MppMultiId
	@TableField(value= "sellerid")
	private String sellerid;
	
	@TableField(value= "asin")
    private String asin;
 
	@TableField(value= "quantity")
    private Integer quantity;

	@TableField(value= "amount")
    private BigDecimal amount;
	 
	@TableField(value= "order_quantity")
    private Integer orderQuantity;

	@TableField(value= "order_amount")
    private BigDecimal orderAmount;
	
	@TableField(value= "amazon_rma_id")
    private String amazonRmaId;

	@TableField(value= "merchant_rma_id")
    private String merchantRmaId;

	@TableField(value= "label_type")
    private String labelType;
	
	@TableField(value= "label_cost")
    private String labelCost;
	
	@TableField(value= "reason")
    private String reason;

	@TableField(value= "status")
    private String status;

	@TableField(value= "currency")
    private String currency;
	
	@TableField(value= "carrier")
    private String carrier;
	
	@TableField(value= "trackingid")
    private String trackingid;
	
	@TableField(value= "labelpaidby")
    private String labelpaidby;
	
	@TableField(value= "azclaim")
    private String azclaim;
	
	@TableField(value= "isprime")
    private String isprime;
	
	@TableField(value= "inpolicy")
    private String inpolicy;
	
	@TableField(value= "returntype")
    private String returntype;

	@TableField(value= "resolution")
    private String resolution;
	
	@TableField(value= "invoice_number")
    private String invoiceNumber;
	
	@TableField(value= "return_delivery_date")
    private Date returnDeliveryDate;
	
	@TableField(value= "safet_action_reason")
    private String safetActionReason;
	
	@TableField(value= "safet_claim_id")
    private String safetClaimId;
	
	@TableField(value= "safet_claim_state")
    private String safetClaimState;
	
	@TableField(value= "safet_claim_creation_time")
    private Date safetClaimCreationTime;
	
	@TableField(value= "safet_claim_reimbursement_amount")
    private String safetClaimReimbursementAmount;
	

	@TableField(value= "amazonauthid")
    private String amazonauthid;
	
	@TableField(value= "marketplaceid")
    private String marketplaceid;
 
	@TableField(value= "refreshtime")
    private Date refreshtime;
}