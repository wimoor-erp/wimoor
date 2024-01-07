package com.wimoor.amazon.common.pojo.dto;

import lombok.Data;

@Data
public class SalesChartDTO {
	String groupid;
	String marketplaceid;
	String amazonAuthId;
	String sku;
	String msku;
	String lineType;
	Integer daysize;
}
