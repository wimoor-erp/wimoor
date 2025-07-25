package com.wimoor.amazon.auth.pojo.vo;

import java.util.Date;
import java.util.List;
import java.util.Map;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
@Data
@ApiModel(value="AmazonGroupVO对象", description="店铺授权")
public class AmazonRegionVO {
	 
	   @ApiModelProperty(value = "售卖ID")
	   String sellerid;
	   @ApiModelProperty(value = "区域")
	   String region;
	   @ApiModelProperty(value = "区域名称")
	   String regionname;
	   @ApiModelProperty(value = "广告已授权")
	   String adauth;
	   @ApiModelProperty(value = "店铺已授权")
	   String sellerauth;
	   @ApiModelProperty(value = "授权时间")
	   Date time;
	   @ApiModelProperty(value = "广告授权AuthID")
	   String advid;
	   @ApiModelProperty(value = "店铺授权AuthID")
	   String authid;
	   @ApiModelProperty(value = "国家列表")
	   Map<String,AmazonMarketVO> countrys;
	   @ApiModelProperty(value = "国家列表")
	   List<AmazonMarketVO> countrysList;
	   
}
