package com.wimoor.erp.ship.pojo.dto;

import com.wimoor.common.pojo.entity.BasePageQuery;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="ShipTransDetailDTO对象", description="货件")
public class ShipTransDetailDTO extends BasePageQuery{
	String marketplaceid;
	String transtype;
	String priceunits;
	String company;
	String search;
}
