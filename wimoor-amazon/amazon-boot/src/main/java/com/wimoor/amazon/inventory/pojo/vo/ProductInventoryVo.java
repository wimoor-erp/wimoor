package com.wimoor.amazon.inventory.pojo.vo;

import java.io.Serializable;

import lombok.Data;

@Data
public class ProductInventoryVo implements  Serializable {
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
    ProductInventoryVo summary;
}
