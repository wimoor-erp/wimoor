package com.wimoor.amazon.product.pojo.dto;

import com.wimoor.common.pojo.entity.BasePageQuery;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="ShipPlanDTO对象", description="发货计划")
public class ProductAsyncInitDTO extends BasePageQuery{
	String groupid ;
	String marketplaceid ;
	List<String> marketplaceids ;
	List<String> groupids ;
	String shopid ;
	List<String> pidlist;
	List<String> types;
	String selecteds;
	String range;
}
