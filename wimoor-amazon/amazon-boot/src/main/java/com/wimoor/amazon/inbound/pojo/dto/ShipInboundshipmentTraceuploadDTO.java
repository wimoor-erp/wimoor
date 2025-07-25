package com.wimoor.amazon.inbound.pojo.dto;

import com.wimoor.common.pojo.entity.BasePageQuery;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="ShipInboundshipmentTraceuploadDTO对象", description="获取追踪信息dto")
public class ShipInboundshipmentTraceuploadDTO extends BasePageQuery{

	@ApiModelProperty(value = "仓库ID")
    private String shipmentid;
	
	@ApiModelProperty(value = "开始时间")
    private String fromDate;
	
	@ApiModelProperty(value = "结束时间")
    private String toDate;
	

}
