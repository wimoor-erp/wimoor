package com.wimoor.amazon.inboundV2.pojo.dto;

import com.wimoor.amazon.common.pojo.entity.QueryApiPageDTO;

import com.wimoor.amazon.inboundV2.pojo.entity.ShipInboundPlan;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class  InboundPlansDTO extends QueryApiPageDTO {
	String status;
	List<ShipInboundPlan> list;
}
