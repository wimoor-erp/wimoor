package com.wimoor.amazon.adv.controller.pojo.dto;

import lombok.Data;

@Data
public class QueryForDownload {
	String profileid;
	String marketplaceid;
	String groupid;
	String search ;
	String fromDate;
	String endDate;
	String reporttype;
	String campaigntype;
	String dateType;
	String currency;
	String marketplacename;
	String groupname;
	 
}
