package com.wimoor.amazon.inbound.pojo.dto;

import java.util.Date;
import java.util.List;
import java.util.Map;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="ShipInboundItemReceiveDTO对象", description="批次成本receive")
public class ShipInboundItemReceiveDTO {

	private List<Map<String,Object>> itemlist;
	
	private Date date;
	
	private Integer day;
	
}
