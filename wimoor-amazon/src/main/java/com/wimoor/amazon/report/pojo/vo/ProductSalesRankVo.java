package com.wimoor.amazon.report.pojo.vo;

import java.math.BigDecimal;

import lombok.Data;
@Data
public class ProductSalesRankVo {
	 String groupname;
	 String marketname;
	 String catename;
	 String image;
	 String name;
	 String sku;
	 Integer quantity;
	 BigDecimal orderprice ;
     Integer fulfillable;
}
