package com.wimoor.amazon.adv.controller.pojo.dto;

import com.wimoor.amazon.adv.common.pojo.BasePageQuery;

import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode(callSuper = true)
public class OperationLogQuery extends BasePageQuery{
	String profileid;
	String marketplaceid;
	String groupid;
	String search ;
	String fromDate;
	String endDate;
}
