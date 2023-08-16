package com.wimoor.erp.warehouse.pojo.dto;


import com.wimoor.common.pojo.entity.BasePageQuery;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="ShelfInvListDto对象", description="申请库位库存列表条件和暂存库存列表")
public class ShelfInvListDto extends BasePageQuery{
	@ApiModelProperty(value = "仓库ID")
	String addressid;
	
	@ApiModelProperty(value = "仓库ID")
	String warehouseid;
	
	@ApiModelProperty(value = "SKU查询", example = "TSS001")
	String search ;
	
	@ApiModelProperty(value = "SKU查询", example = "TSS001")
	String sku ;
	
	@ApiModelProperty(value = "库位ID", example = "123456789")
	String shelfid ;
	
	@ApiModelProperty(value = "库位库存条件,是否查询子库位库存，默认为true，当shelfid为空时必须为true", example = "true")
	String allchildren ;
	
	@ApiModelProperty(value = "产品ID", example = "123456789")
	String materialid ;
 
	@ApiModelProperty(value = "产品类型", example = "product,consumable,package")
	String mtype ;
	
	@ApiModelProperty(value = "盘点单ID", example = "123456789")
    String stocktakingid;
	
	String isstocking;
	String selected;
	
	@ApiModelProperty(value = "查看库存")
	Boolean hasInv;
}
