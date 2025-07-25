package com.wimoor.amazon.inbound.pojo.dto;


import com.wimoor.common.pojo.entity.BasePageQuery;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="ShipmentReportByLogisticsDTO对象", description="报表")
public class ShipmentReportByLogisticsDTO  extends BasePageQuery{
	String shopid  ;
	String companyid  ;
	String channelid ;
	String warehouseid;  
	String type  ;
	String search ;
	String datetype ;
	String groupid  ;
	String marketplaceid  ;
	String searchtype ;
    List<String> groupby;
	String fromDate  ;
	String toDate   ;
}
