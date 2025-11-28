package com.wimoor.erp.inventory.pojo.vo;

import lombok.Data;

@Data
public class InventoryVo {
	    private String warehouseid;

	    private String materialid;
 
	    private Integer inbound;
	 
	    private Integer outbound;
	    
	    private Integer fulfillable;
}
