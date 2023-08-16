package com.wimoor.amazon.finances.pojo.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.jeffreyning.mybatisplus.anno.MppMultiId;
import com.wimoor.amazon.util.UUIDUtil;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
 
@Data
@TableName("t_amz_settlement_summary_sku")  
@ApiModel(value="AmzSettlementSummarySku对象", description="SKU账期分析")
public class AmzSettlementSummarySku implements Serializable{
	
	/**
	 * 
	 */
	@ApiModelProperty(value = "ID")
	@MppMultiId
	@TableField(value="id")
    String id;
	
	public String getId() {
		if(id==null) {
			id=UUIDUtil.getUUIDshort();
		}
		return id;
	}
	
 
	private static final long serialVersionUID = -4840675120459455649L;

	@TableField(value="order_amount")
    private Integer orderAmount;

	@TableField(value="sales")
    private Integer sales;
	
	@TableField(value="refundsales")
    private Integer refundsales;
	
	@TableField(value="refundorder")
    private Integer refundorder;
	
	@TableField(value="principal")
    private BigDecimal principal;

	@TableField(value="commission")
    private BigDecimal commission;

	@TableField(value="refund")
    private BigDecimal refund;

	@TableField(value="fbafee")
    private BigDecimal fbafee;
	
	@TableField(value="otherfee")
	private BigDecimal otherfee;
	
	@TableField(value="shipping")
	private BigDecimal shipping;
	
	@TableField(value="promotion")
	private BigDecimal promotion;
	
	@TableField(value="tax")
	private BigDecimal tax;
	
	@MppMultiId
	@TableField(value="amazonAuthId")
    private String amazonauthid;
	
	@TableId(value="settlementid")
    private String settlementId;

	@TableField(value="sku")
    private String sku;
	

	@TableField(value="currency")
    private String currency;
	
	@TableField(value="marketplace_name")
    private String marketplaceName;

	@MppMultiId
	@TableField(value="posted_date")
    private Date postedDate;
 
	@TableField(value="share_storage_fee")
	private BigDecimal shareStorageFee;
	
	@TableField(value="share_long_storage_fee")
	private BigDecimal shareLongStorageFee;
	
	@TableField(value="share_adv_spend_fee")
	private BigDecimal shareAdvSpendFee;
	
	@TableField(value="share_coupon_redemption_fee")
	private BigDecimal shareCouponRedemptionFee;
	
	@TableField(value="share_reserve_fee")
	private BigDecimal shareReserveFee;
	
	@TableField(value="share_reimbursement_fee")
	private BigDecimal shareReimbursementFee;
	
	@TableField(value="share_shop_other_fee")
	private BigDecimal shareShopOtherFee;
	
	@TableField(value="local_price")
	private BigDecimal localPrice;
	
	@TableField(value="local_return_tax")
	private BigDecimal localReturnTax;
	
	@TableField(value="local_other_cost")
	private BigDecimal localOtherCost;
	
	@TableField(value="profit_local_shipmentfee")
	private BigDecimal profitLocalShipmentfee;
	
	@TableField(value="profit_vat")
	private BigDecimal profitVat;
	
	@TableField(value="profit_companytax")
	private BigDecimal profitCompanytax;
	
	@TableField(value="profit_customstax")
	private BigDecimal profitCustomstax;
	
	@TableField(value="profit_exchangelost")
	private BigDecimal profitExchangelost;
	
	@TableField(value="profit_marketfee")
	private BigDecimal profitMarketfee;
	
	@TableField(value="profit_lostrate")
	private BigDecimal profitLostrate;
 
	@TableField(value="profit_otherfee")
	private BigDecimal profitOtherfee;

	private String pid;
	private String mid;
	private String owner;
    
}