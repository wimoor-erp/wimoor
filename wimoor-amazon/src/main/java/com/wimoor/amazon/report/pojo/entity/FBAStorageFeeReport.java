package com.wimoor.amazon.report.pojo.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import lombok.Data;

 
@Data
@TableName("t_fba_storage_fee_report")  
@ApiModel(value="FBAStorageFeeReport对象", description="SKU 仓储费")
public class FBAStorageFeeReport {
	@TableId(value="id",type=IdType.AUTO)
    private Integer id;

	@TableField(value="asin")
    private String asin;

   @TableField(value="fnsku")
    private String fnsku;

   @TableField(value="fulfillment_center")
    private String fulfillmentCenter;

   @TableField(value="country")
    private String country;

   @TableField(value="longest_side")
    private BigDecimal longestSide;

   @TableField(value="median_side")
    private BigDecimal medianSide;

   @TableField(value="shortest_side")
    private BigDecimal shortestSide;

   @TableField(value="unit_of_dimension")
    private String unitOfDimension;

   @TableField(value="weight")
    private BigDecimal weight;

   @TableField(value="unit_of_weight")
    private String unitOfWeight;

   @TableField(value="item_volume")
    private BigDecimal itemVolume;

   @TableField(value="volume_units")
    private String volumeUnits;

   @TableField(value="product_size_tier")
    private String productSizeTier;

   @TableField(value="avg_quantity_on_hand")
    private BigDecimal avgQuantityOnHand;

   @TableField(value="avg_quantity_pending_removal")
    private BigDecimal avgQuantityPendingRemoval;

   @TableField(value="total_item_volume")
    private BigDecimal totalItemVolume;

   @TableField(value="month")
    private String month;

   @TableField(value="storage_rate")
    private BigDecimal storageRate;

   @TableField(value="currency")
    private String currency;

   @TableField(value="monthly_storage_fee")
    private BigDecimal monthlyStorageFee;

   @TableField(value="category")
    private String category;

   @TableField(value="eligible_for_inv_discount")
    private Boolean eligibleForInvDiscount;

   @TableField(value="qualifies_for_inv_discount")
    private Boolean qualifiesForInvDiscount;
    
	@TableField(value="amazonauthid")
    private String amazonauthid;

	@TableField(value="lastupdate")
	private Date lastupdate;
	 
    
}