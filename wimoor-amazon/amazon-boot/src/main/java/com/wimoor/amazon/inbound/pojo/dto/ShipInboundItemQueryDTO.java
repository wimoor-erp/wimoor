package com.wimoor.amazon.inbound.pojo.dto;

import com.wimoor.common.pojo.entity.BasePageQuery;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="ShipInboundShipmenSummaryDTO对象", description="批次成本")
public class ShipInboundItemQueryDTO extends BasePageQuery{

	private String fromDate;
	
	private String toDate;
	
	private String search;
	
	private String searchType;
	
	private String status;
	
	private String groupid;
	
	private String marketplaceid;
	
	private String owner;
	
}
