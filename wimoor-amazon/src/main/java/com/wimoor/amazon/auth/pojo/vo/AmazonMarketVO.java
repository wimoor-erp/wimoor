package com.wimoor.amazon.auth.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
@Data
@ApiModel(value="AmazonGroupVO对象", description="店铺授权国家")
public class AmazonMarketVO {
	 @ApiModelProperty(value = "国家编码")
	   String countrycode;
	   @ApiModelProperty(value = "国家名称")
	   String countryname;
	   @ApiModelProperty(value = "站点编码")
	   String marketplaceid;
	   @ApiModelProperty(value = "SellerId")
	   String sellerid; 
}
