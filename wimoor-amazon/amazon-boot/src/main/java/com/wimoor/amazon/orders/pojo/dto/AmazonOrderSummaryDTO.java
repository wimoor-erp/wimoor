package com.wimoor.amazon.orders.pojo.dto;

import java.math.BigDecimal;
import java.util.List;


import io.swagger.annotations.ApiModel;
import lombok.Data;
@Data
@ApiModel(value="AmazonOrderSummaryDTO对象", description="获取订单汇总图表")
public class AmazonOrderSummaryDTO {
	List<String> salechannel;
	List<String> fulfillmentChannel;
	List<String> isBusinessOrder;
	List<String> orderStatus;
	String bytime;
	String sku;
	String groupid;
	String discountfrom;
	String discountto;
    String enddate;
    String fromdate;
 
	public BigDecimal getDiscountfrom() {
		if (discountfrom == null)
			return null;
		return new BigDecimal(discountfrom).divide(new BigDecimal("100"));
	}

 
	public BigDecimal getDiscountto() {
		if (discountto == null)
			return null;
		return new BigDecimal(discountto).divide(new BigDecimal("100"));
	}
 
}
