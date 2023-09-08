package com.wimoor.amazon.product.pojo.dto;


import java.util.List;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(value="PlanDetailDTO对象", description="发货计划詳情")
public class PlanDetailDTO {
	String groupid;
	String warehouseid;
	String msku;
	String sku;
	String plantype;
	List<String> groupids ;
	List<String> marketplaceids ;
	List<String> mskus ;
	Boolean plansimple;
	Boolean iseu;
	Integer amount;
	String shopid;
}
