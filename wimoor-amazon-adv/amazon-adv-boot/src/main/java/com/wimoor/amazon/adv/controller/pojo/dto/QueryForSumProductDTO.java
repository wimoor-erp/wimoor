package com.wimoor.amazon.adv.controller.pojo.dto;

import lombok.Data;

@Data 
public class QueryForSumProductDTO { 
	String begin ;
	String end;
	String type ;
	String groupid ;
	String profileid ;
	String marketplaceid;
	String currency;
}
