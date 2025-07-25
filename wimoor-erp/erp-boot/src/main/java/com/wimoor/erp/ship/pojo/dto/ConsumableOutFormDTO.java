package com.wimoor.erp.ship.pojo.dto;

import java.util.List;


import com.wimoor.erp.stock.pojo.entity.OutwhFormConsumable;
import lombok.Data;
@Data
public class ConsumableOutFormDTO {
	List<ConsumableSkuDTO> skulist;
	List<PackageSkuDTO> pkglist;
	List<OutwhFormConsumable> consumablelist;
	String warehouseid;
	String number;
	String shipmentid;

}
