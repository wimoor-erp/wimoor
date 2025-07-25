package com.wimoor.amazon.follow.pojo.dto;


import java.math.BigDecimal;

import com.wimoor.common.pojo.entity.BasePageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="ProductFollowListDTO对象", description="获取商品跟卖列表")
public class ProductFollowListDTO extends BasePageQuery{
		
	@ApiModelProperty(value = "店铺id", example = "")
	String groupid ;
	@ApiModelProperty(value = "站点", example = "")
	String marketplaceid ;
	@ApiModelProperty(value = "国家", example = "")
	String country ;
	@ApiModelProperty(value = "产品负责人", example = "")
	String ownerid ;
	@ApiModelProperty(value = "最高单量", example = "")
	Integer maxorders;
	@ApiModelProperty(value = "最低单量", example = "")
	Integer loworders;
	@ApiModelProperty(value = "最高价格", example = "")
	BigDecimal maxprice;
	@ApiModelProperty(value = "最低价格", example = "")
	BigDecimal lowprice;
	@ApiModelProperty(value = "开始时间", example = "")
	String fromDate;
	@ApiModelProperty(value = "结束时间", example = "")
	String toDate;
	@ApiModelProperty(value = "查询", example = "")
	String search;
	@ApiModelProperty(value = "开始出单时间", example = "")
	String startDate;
	@ApiModelProperty(value = "结束出单时间", example = "")
	String endDate;
	@ApiModelProperty(value = "跟卖时间id", example = "")
	String followDateId;
	@ApiModelProperty(value = "承受价", example = "")
	String isoverprice;
	@ApiModelProperty(value = "购物车所属", example = "")
	String isshopowner;
	@ApiModelProperty(value = "购物车资格", example = "")
	String isshopcart;
	String shopid;
	String amazonauthid;
	String status;
	BigDecimal buyprice;
	
}
