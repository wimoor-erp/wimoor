package com.wimoor.amazon.orders.pojo.dto;

import com.wimoor.common.pojo.entity.BasePageQuery;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="AmazonTodayProductOrdersDTO对象", description="获取今日订单列表")
public class AmazonTodayProductOrdersDTO extends BasePageQuery{
	String groupid ;
	String marketplaceid ;
	String channel ;
	String status ;
	String color ;
	String searchtype ;
	String search ;
	String pointname ;
	String isbusiness;
	String isorder ;
}
