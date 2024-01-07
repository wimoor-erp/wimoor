package com.wimoor.amazon.product.pojo.dto;

import java.util.List;
import java.util.Map;

import com.wimoor.common.pojo.entity.BasePageQuery;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="ShipPlanDTO对象", description="发货计划")
public class PlanDTO extends BasePageQuery{
	String groupid ;
	String warehouseid ;
	String planid ;
	String plantype ;
	List<String> marketplaceids ;
	List<String> groupids ;
	String shortdays;
	String search ;
	Boolean selected ;
	Boolean expendall;
	String status ;
	String status2;
	String owner ;
	String categoryid;
	String shopid ;
	String orderparam ;
	String sortparam ;
	String ordercountry;
	String msku;
	Integer issfg ;
	String ischeck;
	Boolean plansimple;
	String skuarray ;
	String name;
	String small;
	String different;
	String remark ;
	String currentRank;
	String hasAddFee;
	String defoutwarehouseid ;
	String samesearch;
	String searchtype;
	List<String> mskulist;
	List<Map<String,Object>> planitem;
	List<String> skulist;
	List<String> pidlist;
	List<String> tags;
}
