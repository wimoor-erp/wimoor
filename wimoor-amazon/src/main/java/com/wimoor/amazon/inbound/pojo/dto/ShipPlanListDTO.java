package com.wimoor.amazon.inbound.pojo.dto;


import com.wimoor.common.pojo.entity.BasePageQuery;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="ShipPlanListDTO对象", description="货件计划dto")
public class ShipPlanListDTO extends BasePageQuery{

	@ApiModelProperty(value = "店铺ID")
    private String groupid;
	
	@ApiModelProperty(value = "站点")
    private String marketplaceid;
	
	@ApiModelProperty(value = "仓库ID")
    private String warehouseid;
	
	@ApiModelProperty(value = "开始时间")
    private String fromDate;
	
	@ApiModelProperty(value = "结束时间")
    private String toDate;
	
	@ApiModelProperty(value = "计划状态")
    private String auditstatus;
	
	@ApiModelProperty(value = "查询内容")
    private String search;
	
	@ApiModelProperty(value = "查询类型")
    private String searchtype;
	
	@ApiModelProperty(value = "计划ID")
    private String planid;
	
	@ApiModelProperty(value = "公司ID(自动填充)")
    private String shopid;
	
	
	
	
}
