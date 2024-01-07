package com.wimoor.amazon.finances.pojo.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import lombok.Data;

 
@Data
@TableName("t_fba_storage_fee_report")  
@ApiModel(value="FBAStorageFeeReport对象", description="SKU 仓储费")
public class FBAStorageFeeReport  implements Serializable{
	private static final long serialVersionUID = -1334189272914233560L;

	@TableField(value = "id" )
    String id; 

	@TableField(value="asin")
    private String asin;

   @TableField(value="fnsku")
    private String fnsku;

   @TableField(value="fulfillment_center")
    private String fulfillmentCenter;

   @TableField(value="country")
    private String countryCode;

   @TableField(value="longest_side")
    private BigDecimal longestSide;

   @TableField(value="median_side")
    private BigDecimal medianSide;

   @TableField(value="shortest_side")
    private BigDecimal shortestSide;
   
   @TableField(value="measurement_units")
    private String measurementUnits;

   @TableField(value="weight")
    private BigDecimal weight;

   @TableField(value="weight_units")
    private String weightUnits;

   @TableField(value="item_volume")
    private BigDecimal itemVolume;

   @TableField(value="volume_units")
    private String volumeUnits;

    @TableField(value="product_size_tier")
    private String productSizeTier;
   
   @TableField(value="average_quantity_on_hand")
    private BigDecimal averageQuantityOnHand;

   @TableField(value="average_quantity_pending_removal")
    private BigDecimal averageQuantityPendingRemoval;

   @TableField(value="estimated_total_item_volume")
    private BigDecimal estimatedTotalItemVolume;

   @TableField(value="`month`")
    private String monthOfCharge;
   
   @TableField(value="storage_utilization_ratio")
    private String storageUtilizationRatio;

   @TableField(value="storage_utilization_ratio_units")
    private String storageUtilizationRatioUnits;
   
   @TableField(value="base_rate")
   private BigDecimal baseRate;
  
   @TableField(value="utilization_surcharge_rate")
   private BigDecimal utilizationSurchargeRate;
   
   @TableField(value="currency")
    private String currency;

   @TableField(value="monthly_storage_fee")
   private BigDecimal estimatedMonthlyStorageFee;

   @TableField(value="dangerous_goods_storage_type")
    private String dangerousGoodsStorageType;

   @TableField(value="eligible_for_inventory_discount")
    private String eligibleForInventoryDiscount;

   @TableField(value="qualifies_for_inventory_discount")
    private String qualifiesForInventoryDiscount;
    
   @TableField(value="total_incentive_fee_amount")
   private BigDecimal totalIncentiveFeeAmount;
   
   @TableField(value="breakdown_incentive_fee_amount")
   private String breakdownIncentiveFeeAmount;
   
   @TableField(value="average_quantity_customer_orders")
   private BigDecimal averageQuantityCustomerOrders;
   
	@TableField(value="amazonauthid")
    private String amazonauthid;

	@TableField(value="lastupdate")
	private Date lastupdate;
	 
    
}