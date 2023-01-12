package com.wimoor.erp.inventory.pojo.vo;

import java.io.Serializable;

import lombok.Data;

@Data
public class MaterialInventoryVo  implements Serializable{
 
    /**
	 * 
	 */
	private static final long serialVersionUID = -4539469199280973784L;

    private String warehouseid;

    private String materialid;

    private String warehousename;
    
    private String name;
    
    private String sku;
    
    private Integer inbound;
 
    private Integer outbound;
    
    private Integer fulfillable;
    
    private Integer canassembly;
    
    private Integer canconsumable;
}