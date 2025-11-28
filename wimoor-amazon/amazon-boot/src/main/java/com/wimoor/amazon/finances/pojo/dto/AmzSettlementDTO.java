package com.wimoor.amazon.finances.pojo.dto;

import com.wimoor.common.pojo.entity.BasePageQuery;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="AmzSettlementAccStatementDTO对象", description="查询结算")
public class AmzSettlementDTO extends BasePageQuery{
	String fromDate;
	String endDate ;
	String datetype;
	String country;
	String marketplaceid;
	String marketplace_name;
	String groupid ;
	String search ;
	String currency;
	String ownerid ;
	String color;
	String id ;
	String amazonAuthId;
	String sku;
	String parentasin;
	String charttype;
	String settlement_id;
	String isother;
	String fatype;
	String categoryid;
}
