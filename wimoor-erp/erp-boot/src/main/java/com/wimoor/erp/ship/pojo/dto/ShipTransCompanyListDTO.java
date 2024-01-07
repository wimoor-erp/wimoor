package com.wimoor.erp.ship.pojo.dto;


import com.wimoor.common.pojo.entity.BasePageQuery;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="ShipTransCompanyListDTO对象", description="货件")
public class ShipTransCompanyListDTO extends BasePageQuery{
	String search ;
	String isdelete;
}
