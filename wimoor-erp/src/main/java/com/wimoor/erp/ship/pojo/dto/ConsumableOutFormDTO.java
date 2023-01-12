package com.wimoor.erp.ship.pojo.dto;

import java.util.List;

import com.wimoor.erp.inventory.pojo.entity.InventoryParameter;

import lombok.Data;
@Data
public class ConsumableOutFormDTO {
	List<InventoryParameter> skulist;
	String warehouseid;
	String shipmentid;
}
