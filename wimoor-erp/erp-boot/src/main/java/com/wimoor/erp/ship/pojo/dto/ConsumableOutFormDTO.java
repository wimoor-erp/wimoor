package com.wimoor.erp.ship.pojo.dto;

import java.util.List;


import lombok.Data;
@Data
public class ConsumableOutFormDTO {
	List<ConsumableSkuDTO> skulist;
	List<PackageSkuDTO> pkglist;
	String warehouseid;
	String number;
	String shipmentid;
}
