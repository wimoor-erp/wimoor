package com.wimoor.amazon.common.pojo.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class SummaryMutilParameterQueryDTO {
	String shopid;
	String groupid;
	String endDate;
	String beginDate;
	String marketplaceid;
	String salechannel;
	String bytime;
	String fulfillChannel;
	List<String> orderStatus;
	BigDecimal discountfrom;
	BigDecimal discountto;
	List<String> is_business_order;
}
