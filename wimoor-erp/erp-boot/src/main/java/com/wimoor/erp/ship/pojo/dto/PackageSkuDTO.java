package com.wimoor.erp.ship.pojo.dto;


import lombok.Data;

@Data
public class PackageSkuDTO {

	 //仓库ID
    private String warehouseid;

    //物料，产品ID
    private String sku;
    
    //物料，产品ID
    private String materialid;

    //要操作的数量
    private Integer amount;
     
}
