package com.wimoor.erp.ship.pojo.dto;

import java.math.BigDecimal;

import lombok.Data;
@Data
public class ConsumableSkuDTO {
  
		
	    //仓库ID
	    private String warehouseid;

	    //物料，产品ID
	    private String sku;
	    
	    //物料，产品ID
	    private String materialid;

	    //要操作的数量
	    private Integer amount;
	    
	    //结余库存
	    private BigDecimal residue;
	    
	    //操作来自那个表单
	    private  String formid;
	   
		
	 
}
