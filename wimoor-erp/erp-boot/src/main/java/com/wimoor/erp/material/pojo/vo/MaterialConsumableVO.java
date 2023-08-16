package com.wimoor.erp.material.pojo.vo;




import java.math.BigDecimal;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="MaterialConsumableVO对象", description="产品耗材对应关系")
public class MaterialConsumableVO{

	@ApiModelProperty(value = "耗材名称")
	String name;
	
	@ApiModelProperty(value = "耗材图片")
	String image;
	
	@ApiModelProperty(value = "耗材所需数量")
	BigDecimal amount;
	
	@ApiModelProperty(value = "主SKU-ID")
	String materialid;
	
	@ApiModelProperty(value = "耗材SKU-ID")
	String id;
	
	@ApiModelProperty(value = "耗材SKU")
	String sku;
	
	@ApiModelProperty(value = "耗材价格")
	BigDecimal price;
	
	@ApiModelProperty(value = "耗材SKU-待入库库存")
	Integer inbound;
	
	@ApiModelProperty(value = "耗材SKU-待出库库存")
	Integer outbound;
	
	@ApiModelProperty(value = "耗材SKU-可用库存")
	Integer fulfillable;
	
	Integer needPlan;
	
	
}
