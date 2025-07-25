package com.wimoor.amazon.follow.pojo.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="ProductInfoFollowSaveDTO对象", description="新增商品跟卖")
public class ProductInfoFollowSaveDTO {

	String asin;
	
	String sku;
	
	String groupid;
	
	String marketplaceid;
	
	String timeid;
	
	String fulfillable;
	
	String maxorderqty;
	
	String cycle;
	
	String isrepeat;
	
	String startprice;
	
	String overprice;
	
	String remark;
	
	String shopid;
	
	String userid;
	
	String image;
	
	String amazonauthid;
	
	String id;
	
	String templateid;
	
	String lowestquantity;
}
