package com.wimoor.amazon.inventory.pojo.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class ProductInventoryVo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5908339865128507446L;
	String groupid;
	String groupname;
	String marketplaceid;
    String warehouse;
    String sku;
    String pname;
    String image;
    String afnWarehouseQuantity;
    String afnFulfillableQuantity;
    String afnReservedQuantity;
    String afnInboundWorkingQuantity;
    String afnInboundShippedQuantity;
    String afnInboundReceivingQuantity;
    String afnUnsellableQuantity;
    String afnResearchingQuantity;
    String afnTotalQuantity;
    Integer stockingCycle; // 安全库存周期
    Integer minCycle; // 最小发货周期
    BigDecimal firstLegCharges; // 头程运输成本
    ProductInventoryVo summary;
}