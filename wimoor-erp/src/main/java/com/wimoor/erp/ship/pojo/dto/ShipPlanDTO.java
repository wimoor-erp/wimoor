package com.wimoor.erp.ship.pojo.dto;

import com.wimoor.common.pojo.entity.BasePageQuery;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="ShipPlanDTO对象", description="发货计划")
public class ShipPlanDTO extends BasePageQuery{
	String groupid ;
	String warehouseid ;
	String planid ;
	String marketplaceid ;
	String color;
	String search ;
	String selected ;
	String status ;
	String status2;
	String owner ;
	String categoryid;
	String shopid ;
	String orderparam ;
	String sortparam ;
	String ordercountry;
	String issfg ;
	String skulist ;
	String proname;
	String remark ;
	String defoutwarehouseid ;
	String samesearch;
	String searchtype;
}
