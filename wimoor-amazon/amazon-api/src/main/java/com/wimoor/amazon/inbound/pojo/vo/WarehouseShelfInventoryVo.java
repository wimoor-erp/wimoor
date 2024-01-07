package com.wimoor.amazon.inbound.pojo.vo;

 

import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="WarehouseShelfInventoryVo对象", description="仓库库位库存信息")
public class WarehouseShelfInventoryVo {
	@ApiModelProperty(value = "仓位中产品对应库存ID")
	String id;
	
	@ApiModelProperty(value = "仓位ID")
	String shelfid;
	
	@ApiModelProperty(value = "产品ID")
	String materialid;
	
	@ApiModelProperty(value = "库位名称含父级名称")
	String shelfname;
	
	@ApiModelProperty(value = "库位库存数量[显示暂存库存时为暂存库存数量]")
	Long quantity;
	
	@ApiModelProperty(value = "占用尺寸")
	Float size;
	
	@ApiModelProperty(value = "产品SKU")
	String sku;
	
	@ApiModelProperty(value = "产品名称")
	String name;
	
	@ApiModelProperty(value = "产品图片")
	String image;
	
	@ApiModelProperty(value = "库位库存数量别名[显示暂存库存时为暂存库存数量别名]")
	Long amount;
	
	@ApiModelProperty(value = "库存，显示暂存库存时SKU的所有库存")
	Long warehousequantity;
	
	@ApiModelProperty(value = "库存，显示暂存库存时SKU的所有库位库存")
	Long shelfquantity;
	
	@ApiModelProperty(value = "库位的编码层级")
	String treepath;
	
	@ApiModelProperty(value = "仓库名称")
	String warehousename;
	
	@ApiModelProperty(value = "仓库ID")
	String warehouseid;
	
	@ApiModelProperty(value = "操作时间")
	Date opttime;
	
}
