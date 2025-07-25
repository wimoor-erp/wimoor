package com.wimoor.amazon.inboundV2.pojo.dto;

import com.amazon.spapi.model.fulfillmentinboundV20240320.ListShipmentBoxesResponse;
import com.amazon.spapi.model.fulfillmentinboundV20240320.ListShipmentItemsResponse;
import com.wimoor.amazon.common.pojo.entity.QueryApiPageDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode(callSuper = true)
public class ShipmentItemsDTO extends QueryApiPageDTO {
	String formid;
	String shipmentid;
	Boolean needsync;
	ListShipmentItemsResponse items;
	ListShipmentBoxesResponse box;
}
