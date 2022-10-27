package com.wimoor.amazon.report.pojo.dto;

import com.wimoor.common.pojo.entity.BasePageQuery;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="InvDayDetailDTO对象", description="查询库存")
public class InvDayDetailDTO extends BasePageQuery{
	String sku ;
	String warehouse  ;
	String fromdate;
	String enddate ;
	String groupid ;
}
