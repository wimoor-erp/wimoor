package com.wimoor.amazon.finances.pojo.entity;

import java.math.BigDecimal;
import java.util.Date;
 

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@TableName("t_fba_estimated_fees")  
@ApiModel(value="FBAEstimatedFee对象", description="SKU FBA费用")
public class FBAEstimatedFee  {
	@TableField(value="sku")
    private String sku;

	@TableField(value="asin")
    private String asin;

	@TableId(value="amazonauthid")
    private String amazonauthid;

	@TableField(value="marketplaceid")
    private String marketplaceid;
	
	@TableField(value="fnsku")
    private String fnsku;

	@TableField(value="product_name")
    private String productName;

	@TableField(value="product_group")
    private String productGroup;
	
	@TableField(value="brand")
	private String brand;
	
	@TableField(value="fulfilled_by")
    private String fulfilledBy;
	
	
	@TableField(value="has_local_inventory")
    private String hasLocalInventory;
	
	@TableField(value="your_price")
    private BigDecimal yourPrice;

	@TableField(value="sales_price")
    private BigDecimal salesPrice;

	@TableField(value="longest_side")
    private BigDecimal longestSide;

	@TableField(value="median_side")
    private BigDecimal medianSide;

	@TableField(value="shortest_side")
    private BigDecimal shortestSide;

	@TableField(value="length_and_girth")
    private BigDecimal lengthAndGirth;

	@TableField(value="unit_of_dimension")
    private String unitOfDimension;
	
	@TableField(value="item_package_weight")
    private BigDecimal itemPackageWeight;

	@TableField(value="unit_of_weight")
    private String unitOfWeight;

	@TableField(value="product_size_tier")
    private String productSizeTier;

	@TableField(value="currency")
    private String currency;

	@TableField(value="estimated_fee_total")
    private BigDecimal estimatedFeeTotal;

	@TableField(value="estimated_referral_fee_per_unit")
    private BigDecimal estimatedReferralFeePerUnit;

	@TableField(value="estimated_variable_closing_fee")
    private BigDecimal estimatedVariableClosingFee;

	@TableField(value="estimated_order_handling_fee_per_order")
    private BigDecimal estimatedOrderHandlingFeePerOrder;

	@TableField(value="estimated_pick_pack_fee_per_unit")
    private BigDecimal estimatedPickPackFeePerUnit;

	@TableField(value="estimated_weight_handling_fee_per_unit")
    private BigDecimal estimatedWeightHandlingFeePerUnit;

	@TableField(value="expected_fulfillment_fee_per_unit")
    private BigDecimal expectedFulfillmentFeePerUnit;

	@TableField(value="estimated_future_fee")
    private BigDecimal estimatedFutureFee;

	@TableField(value="estimated_future_order_handling_fee_per_order")
    private BigDecimal estimatedFutureOrderHandlingFeePerOrder;

	@TableField(value="estimated_future_pick_pack_fee_per_unit")
    private BigDecimal estimatedFuturePickPackFeePerUnit;

	@TableField(value="estimated_future_weight_handling_fee_per_unit")
    private BigDecimal estimatedFutureWeightHandlingFeePerUnit;

	@TableField(value="expected_future_fulfillment_fee_per_unit")
    private BigDecimal expectedFutureFulfillmentFeePerUnit;
	
	@TableField(value="expected_domestic_fulfilment_fee_per_unit")
    private BigDecimal expectedDomesticFulfillmentFeePerUnit;
	
	@TableField(value="expected_efn_fulfilment_fee_per_unit_uk")
	private BigDecimal expectedEfnFulfilmentFeePerUnitUk;
	
	@TableField(value="expected_efn_fulfilment_fee_per_unit_de")
	private BigDecimal expectedEfnFulfilmentFeePerUnitDe;
	
	@TableField(value="expected_efn_fulfilment_fee_per_unit_it")
	private BigDecimal expectedEfnFulfilmentFeePerUnitIt;
	
	@TableField(value="expected_efn_fulfilment_fee_per_unit_es")
	private BigDecimal expectedEfnFulfilmentFeePerUnitEs;
	
	@TableField(value="expected_efn_fulfilment_fee_per_unit_fr")
	private BigDecimal expectedEfnFulfilmentFeePerUnitFr;
	
	@TableField(value="expected_efn_fulfilment_fee_per_unit_se")
	private BigDecimal expectedEfnFulfilmentFeePerUnitSe;
	
	 
	@TableField(value="estimated_fixed_closing_fee")
	private BigDecimal estimatedFixedClosingFee;
	
	@TableField(value="lastupdate")
	private Date lastupdate;
	 
}