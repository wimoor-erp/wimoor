package com.wimoor.amazon.inventory.pojo.entity;

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
@TableName("t_inventory_report")  
@ApiModel(value="InventoryReport对象", description="库存")
public class InventoryReport extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6949703954116678409L;

	@TableField(value= "sku")
    private String sku;

	@TableField(value= "byday")
    private Date byday;

	@TableField(value= "marketplaceid")
    private String marketplaceid;

	@TableField(value= "fnsku")
    private String fnsku;

	@TableField(value= "asin")
    private String asin;

	@TableField(value= "pcondition")
    private String pcondition;

	@TableField(value= "your_price")
    private BigDecimal yourPrice;

	@TableField(value= "mfn_listing_exists")
    private String mfnListingExists;

	@TableField(value= "mfn_fulfillable_quantity")
    private Integer mfnFulfillableQuantity;

	@TableField(value= "afn_listing_exists")
    private String afnListingExists;

	@TableField(value= "afn_warehouse_quantity")
    private Integer afnWarehouseQuantity;

	@TableField(value= "afn_fulfillable_quantity")
    private Integer afnFulfillableQuantity;

	@TableField(value= "afn_unsellable_quantity")
    private Integer afnUnsellableQuantity;

	@TableField(value= "afn_reserved_quantity")
    private Integer afnReservedQuantity;

	@TableField(value= "afn_total_quantity")
    private Integer afnTotalQuantity;

	@TableField(value= "per_unit_volume")
    private BigDecimal perUnitVolume;

	@TableField(value= "afn_inbound_working_quantity")
    private Integer afnInboundWorkingQuantity;

	@TableField(value= "afn_inbound_shipped_quantity")
    private Integer afnInboundShippedQuantity;

	@TableField(value= "afn_inbound_receiving_quantity")
    private Integer afnInboundReceivingQuantity;
	
	@TableField(value= "afn_reserved_future_supply")
    private Integer afnReservedFutureSupply;

	@TableField(value= "afn_researching_quantity")
    private Integer afnResearchingQuantity;

	@TableField(value= "afn_future_supply_buyable")
    private Integer afnFutureSupplyBuyable;
 
	@TableField(value= "isnewest")
    private Boolean isnewest;
	
	@TableField(value= "amazonAuthId")
    private String amazonAuthId;
 
}