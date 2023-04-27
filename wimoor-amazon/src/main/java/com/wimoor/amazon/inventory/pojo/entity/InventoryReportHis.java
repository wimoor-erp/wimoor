package com.wimoor.amazon.inventory.pojo.entity;

import java.math.BigDecimal;
import java.util.Date;
 
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import lombok.Data;

 
@Data
@TableName("t_inventory_report_his")  
@ApiModel(value="InventoryReportHis对象", description="库存历史")
public class InventoryReportHis   {
 
	@TableId(value="id")
    private String id;

 
    @TableField(value="byday")
    private Date byday;
    
	@TableField(value="sku")
    private String sku;

	@TableField(value="marketplaceid")
    private String marketplaceid;

	@TableField(value="fnsku")
    private String fnsku;

	@TableField(value="asin")
    private String asin;

	@TableField(value="pcondition")
    private String pcondition;

	@TableField(value="your_price")
    private BigDecimal yourPrice;

	@TableField(value="mfn_listing_exists")
    private String mfnListingExists;

	@TableField(value="mfn_fulfillable_quantity")
    private Integer mfnFulfillableQuantity;

	@TableField(value="afn_listing_exists")
    private String afnListingExists;

	@TableField(value="afn_warehouse_quantity")
    private Integer afnWarehouseQuantity;

	@TableField(value="afn_fulfillable_quantity")
    private Integer afnFulfillableQuantity;

	@TableField(value="afn_unsellable_quantity")
    private Integer afnUnsellableQuantity;

	@TableField(value="afn_reserved_quantity")
    private Integer afnReservedQuantity;

	@TableField(value="afn_total_quantity")
    private Integer afnTotalQuantity;

	@TableField(value="per_unit_volume")
    private BigDecimal perUnitVolume;

	@TableField(value="afn_inbound_working_quantity")
    private Integer afnInboundWorkingQuantity;

	@TableField(value="afn_inbound_shipped_quantity")
    private Integer afnInboundShippedQuantity;

	@TableField(value="afn_inbound_receiving_quantity")
    private Integer afnInboundReceivingQuantity;
	
	@TableField(value= "afn_reserved_future_supply")
    private Integer afnReservedFutureSupply;

	@TableField(value= "afn_researching_quantity")
    private Integer afnResearchingQuantity;

	@TableField(value= "afn_future_supply_buyable")
    private Integer afnFutureSupplyBuyable;

	@TableField(value="isnewest")
    private Boolean isnewest;

	@TableField(value="amazonAuthId")
    private String amazonauthid;
 
}